package gameCore.graphics;

import gameCore.graphics.effect.Effect;
import gameCore.graphics.effect.EffectPass;
import gameCore.graphics.effect.EffectPassCollection;
import gameCore.graphics.vertices.PrimitiveType;
import gameCore.graphics.vertices.VertexPositionColorTexture;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * This class handles the queuing of batch items into the GPU by creating the triangle tesselations
 * that are used to draw the sprite textures. This class supports int.MaxValue number of sprites to
 * be batched and will process them into short.MaxValue groups (strided by 6 for the number of
 * vertices sent to the GPU).
 * 
 * @author Eric
 *
 */
public class SpriteBatcher
{
	/*
	 * Note that this class is fundamental to high performance for SpriteBatch games. Please
	 * exercise caution when making changes to this class.
	 */

	// / <summary>
	// / Initialization size for the batch item list and queue.
	// / </summary>
	private final int InitialBatchSize = 256;
	// / <summary>
	// / The maximum number of batch items that can be processed per iteration
	// / </summary>
	private final int MaxBatchSize = Short.MAX_VALUE / 6; // 6 = 4 vertices unique and 2 shared, per
	// / <summary>
	// / Initialization size for the vertex array, in batch units.
	// / </summary>
	private final int InitialVertexArraySize = 256;

	// / <summary>
	// / The list of batch items to process.
	// / </summary>
	private List<SpriteBatchItem> _batchItemList;

	// / <summary>
	// / The available SpriteBatchItem queue so that we reuse these objects when we can.
	// / </summary>
	private Queue<SpriteBatchItem> _freeBatchItemQueue;

	// / <summary>
	// / The target graphics device.
	// / </summary>
	private GraphicsDevice _device;

	// / <summary>
	// / Vertex index array. The values in this array never change.
	// / </summary>
	private short[] _index;

	private VertexPositionColorTexture[] _vertexArray;

	public SpriteBatcher(GraphicsDevice device)
	{
		_device = device;

		_batchItemList = new ArrayList<SpriteBatchItem>(InitialBatchSize);
		_freeBatchItemQueue = new ArrayDeque<SpriteBatchItem>(InitialBatchSize);

		ensureArrayCapacity(InitialBatchSize);
	}

	// / <summary>
	// / Create an instance of SpriteBatchItem if there is none available in the free item queue.
	// Otherwise,
	// / a previously allocated SpriteBatchItem is reused.
	// / </summary>
	// / <returns></returns>
	public SpriteBatchItem createBatchItem()
	{
		SpriteBatchItem item;
		if (_freeBatchItemQueue.size() > 0)
			item = _freeBatchItemQueue.remove();
		else
			item = new SpriteBatchItem();
		_batchItemList.add(item);
		return item;
	}

	// / <summary>
	// / Resize and recreate the missing indices for the index and vertex position color buffers.
	// / </summary>
	// / <param name="numBatchItems"></param>
	private void ensureArrayCapacity(int numBatchItems)
	{
		int neededCapacity = 6 * numBatchItems;
		if (_index != null && neededCapacity <= _index.length)
		{
			// Short circuit out of here because we have enough capacity.
			return;
		}
		short[] newIndex = new short[6 * numBatchItems];
		int start = 0;
		if (_index != null)
		{
			// _index.CopyTo(newIndex, 0);
			Arrays.copyOf(newIndex, _index.length);
			start = _index.length / 6;
		}
		for (int i = start; i < numBatchItems; ++i)
		{
			/*
             *  TL    TR
             *   0----1 0,1,2,3 = index offsets for vertex indices
             *   |   /| TL,TR,BL,BR are vertex references in SpriteBatchItem.
             *   |  / |
             *   | /  |
             *   |/   |
             *   2----3
             *  BL    BR
             */
			// Triangle 1
			newIndex[i * 6 + 0] = (short) (i * 4);
			newIndex[i * 6 + 1] = (short) (i * 4 + 1);
			newIndex[i * 6 + 2] = (short) (i * 4 + 2);
			// Triangle 2
			newIndex[i * 6 + 3] = (short) (i * 4 + 1);
			newIndex[i * 6 + 4] = (short) (i * 4 + 3);
			newIndex[i * 6 + 5] = (short) (i * 4 + 2);
		}
		_index = newIndex;

		_vertexArray = new VertexPositionColorTexture[4 * numBatchItems];
	}

	// / <summary>
	// / Reference comparison of the underlying Texture objects for each given SpriteBatchitem.
	// / </summary>
	// / <param name="a"></param>
	// / <param name="b"></param>
	// / <returns>0 if they are not reference equal, and 1 if so.</returns>
	static int compareTexture(SpriteBatchItem a, SpriteBatchItem b)
	{
		return (a.texture == b.texture) ? 0 : 1;
	}

	// TODO: Delete after testing since not used
	/*
	 * static Comparator<SpriteBatchItem> textureComparator = new Comparator<SpriteBatchItem>() {
	 * public int compare(SpriteBatchItem a, SpriteBatchItem b) {
	 * return (a.texture == b.texture) ? 0 : 1;
	 * }
	 * };
	 */

	// / <summary>
	// / Compares the Depth of a against b returning -1 if a is less than b,
	// / 0 if equal, and 1 if a is greater than b. The test uses Float.compare(float, float)
	// / </summary>
	// / <param name="a"></param>
	// / <param name="b"></param>
	// / <returns>-1 if a is less than b, 0 if equal, and 1 if a is greater than b</returns>
	static int compareDepth(SpriteBatchItem a, SpriteBatchItem b)
	{
		return Float.compare(a.depth, b.depth);
	}

	// TODO: Delete after testing since not used
	/*
	 * static Comparator<SpriteBatchItem> depthComparator = new Comparator<SpriteBatchItem>() {
	 * public int compare(SpriteBatchItem a, SpriteBatchItem b) {
	 * return Float.compare(a.depth, b.depth);
	 * }
	 * };
	 */

	// / <summary>
	// / Implements the opposite of compareDepth, where b is compared against a.
	// / </summary>
	// / <param name="a"></param>
	// / <param name="b"></param>
	// / <returns>-1 if b is less than a, 0 if equal, and 1 if b is greater than a</returns>
	static int compareReverseDepth(SpriteBatchItem a, SpriteBatchItem b)
	{
		return Float.compare(b.depth, a.depth);
	}

	// TODO: Delete after testing since not used
	/*
	 * static Comparator<SpriteBatchItem> reverseDepthComparator = new Comparator<SpriteBatchItem>()
	 * {
	 * public int compare(SpriteBatchItem a, SpriteBatchItem b) {
	 * return Float.compare(b.depth, a.depth);
	 * }
	 * };
	 */
	// / <summary>
	// / Sorts the batch items and then groups batch drawing into maximal allowed batch sets that do
	// not
	// / overflow the 16 bit array indices for vertices.
	// / </summary>
	// / <param name="sortMode">The type of depth sorting desired for the rendering.</param>
	// / <param name="effect">The custom effect to apply to the drawn geometry</param>
	public void drawBatch(SpriteSortMode sortMode, Effect effect)
	{
		// nothing to do
		if (_batchItemList.size() == 0)
			return;

		// sort the batch items
		switch (sortMode)
		{
			case Texture:
				// _batchItemList.sort(textureComparator);
				_batchItemList.sort(SpriteBatcher::compareTexture);
				break;
			case FrontToBack:
				_batchItemList.sort(SpriteBatcher::compareDepth);
				break;
			case BackToFront:
				_batchItemList.sort(SpriteBatcher::compareReverseDepth);
				break;
			default:
				break;
		}

		// Determine how many iterations through the drawing code we need to make
		int batchIndex = 0;
		int batchCount = _batchItemList.size();
		
		_device._graphicsMetrics._spriteCount += (long) batchCount;
		
		// Iterate through the batches, doing short.MaxValue sets of vertices only.
		while (batchCount > 0)
		{
			// setup the vertexArray array
			int startIndex = 0;
			int index = 0;
			Texture2D tex = null;

			int numBatchesToProcess = batchCount;
			if (numBatchesToProcess > MaxBatchSize)
			{
				numBatchesToProcess = MaxBatchSize;
			}
			ensureArrayCapacity(numBatchesToProcess);
			// Draw the batches
			for (int i = 0; i < numBatchesToProcess; ++i, ++batchIndex)
			{
				SpriteBatchItem item = _batchItemList.get(batchIndex);
				// if the texture changed, we need to flush and bind the new texture
				boolean shouldFlush = item.texture != tex;
				if (shouldFlush)
				{
					flushVertexArray(startIndex, index, effect, tex);

					tex = item.texture;
					startIndex = index = 0;
					_device.getTextures().setTexture(0, tex);
				}

				// store the SpriteBatchItem data in our vertexArray
				_vertexArray[index++] = item.vertexTL;
				_vertexArray[index++] = item.vertexTR;
				_vertexArray[index++] = item.vertexBL;
				_vertexArray[index++] = item.vertexBR;

				// Release the texture and return the item to the queue.
				item.texture = null;
				_freeBatchItemQueue.add(item);
			}
			// flush the remaining vertexArray data
			flushVertexArray(startIndex, index, effect, tex);
			// Update our batch count to continue the process of culling down
			// large batches
			batchCount -= numBatchesToProcess;
		}
		_batchItemList.clear();
	}

	// / <summary>
	// / Sends the triangle list to the graphics device. Here is where the actual drawing starts.
	// / </summary>
	// / <param name="start">Start index of vertices to draw. Not used except to compute the count
	// of vertices to draw.</param>
	// / <param name="end">End index of vertices to draw. Not used except to compute the count of
	// vertices to draw.</param>
	// / <param name="effect">The custom effect to apply to the geometry</param>
	private void flushVertexArray(int start, int end, Effect effect, Texture texture)
	{
		if (start == end)
			return;

		int vertexCount = end - start;

		// If the effect is not null, then apply each pass and render the geometry
		if (effect != null)
		{
			EffectPassCollection passes = effect.currentTechnique.getPasses();
			for (EffectPass pass : passes)
			{
				pass.apply();

				// Whatever happens in pass.Apply, make sure the texture being drawn
                // ends up in Textures[0].
				_device.getTextures().setTexture(0, texture);

				_device.drawUserIndexedPrimitives(
						PrimitiveType.TriangleList,
						_vertexArray,
						0,
						vertexCount,
						_index,
						0,
						(vertexCount / 4) * 2,
						VertexPositionColorTexture.vertexDeclaration);
			}
		}
		else
		{
			// If no custom effect is defined, then simply render.
			_device.drawUserIndexedPrimitives(
					PrimitiveType.TriangleList,
					_vertexArray,
					0,
					vertexCount,
					_index,
					0,
					(vertexCount / 4) * 2,
					VertexPositionColorTexture.vertexDeclaration);
		}
	}
}
