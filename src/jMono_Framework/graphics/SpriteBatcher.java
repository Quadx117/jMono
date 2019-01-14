package jMono_Framework.graphics;

import jMono_Framework.graphics.effect.Effect;
import jMono_Framework.graphics.effect.EffectPass;
import jMono_Framework.graphics.effect.EffectPassCollection;
import jMono_Framework.graphics.vertices.PrimitiveType;
import jMono_Framework.graphics.vertices.VertexPositionColorTexture;

import java.util.Arrays;

/**
 * This class handles the queuing of batch items into the GPU by creating the triangle tesselations
 * that are used to draw the sprite textures. This class supports int.MaxValue number of sprites to
 * be batched and will process them into short.MaxValue groups (strided by 6 for the number of
 * vertices sent to the GPU).
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class SpriteBatcher
{
    /*
     * Note that this class is fundamental to high performance for SpriteBatch games. Please
     * exercise caution when making changes to this class.
     */

    /**
     * Initialization size for the batch item list and queue.
     */
    private final int InitialBatchSize = 256;
    /**
     * The maximum number of batch items that can be processed per iteration.
     */
    private final int MaxBatchSize = Short.MAX_VALUE / 6; // 6 = 4 vertices unique and 2 shared, per quad
    /**
     * Initialization size for the vertex array, in batch units.
     */
    private final int InitialVertexArraySize = 256;

    // TODO:(Eric): Check whether or not it would be more efficient using java.util.ArrayList
    /**
     * The list of batch items to process.
     */
    private SpriteBatchItem[] _batchItemList; // cannot be final since it needs to be resizeable.
    /// <summary>
    /// Index pointer to the next available SpriteBatchItem in _batchItemList.
    /// </summary>
    private int _batchItemCount;

    /**
     * The target graphics device.
     */
    private final GraphicsDevice _device;

    /**
     * Vertex index array. The values in this array never change.
     */
    private short[] _index;

    private VertexPositionColorTexture[] _vertexArray;

    public SpriteBatcher(GraphicsDevice device)
    {
        _device = device;

        _batchItemList = new SpriteBatchItem[InitialBatchSize];
		_batchItemCount = 0;

		for (int i = 0; i < InitialBatchSize; ++i)
            _batchItemList[i] = new SpriteBatchItem();

        ensureArrayCapacity(InitialBatchSize);
    }

    /**
     * Reuse a previously allocated {@code SpriteBatchItem} from the item pool.
	 * If there is none available grow the pool and initialize new items.
     * 
     * @return A SpriteBatchItem instance.
     */
    public SpriteBatchItem createBatchItem()
    {
		if (_batchItemCount >= _batchItemList.length)
        {
            int oldSize = _batchItemList.length;
            int newSize = oldSize + oldSize/2; // grow by x1.5
            newSize = (newSize + 63) & (~63); // grow in chunks of 64.
            _batchItemList = Arrays.copyOf(_batchItemList, newSize);
            for(int i=oldSize; i<newSize; ++i)
                _batchItemList[i]=new SpriteBatchItem();

            ensureArrayCapacity(Math.min(newSize, MaxBatchSize));
        }
        SpriteBatchItem item = _batchItemList[_batchItemCount++];
        return item;
    }

    /**
     * Resize and recreate the missing indices for the index and vertex position color buffers.
     * 
     * @param numBatchItems
     *        The number of Items that we want to test.
     */
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
            newIndex = Arrays.copyOf(_index, _index.length);
            start = _index.length / 6;
        }
		int index = start * 6;
        for (int i = start; i < numBatchItems; ++i, index +=6)
        {
            // @formatter:off
			//
            //  TL    TR
            //   0----1 0,1,2,3 = index offsets for vertex indices
            //   |   /| TL,TR,BL,BR are vertex references in SpriteBatchItem.
            //   |  / |
            //   | /  |
            //   |/   |
            //   2----3
            //  BL    BR
            //
		    // @formatter:on

            // Triangle 1
            newIndex[index + 0] = (short) (i * 4);
            newIndex[index + 1] = (short) (i * 4 + 1);
            newIndex[index + 2] = (short) (i * 4 + 2);
            // Triangle 2
            newIndex[index + 3] = (short) (i * 4 + 1);
            newIndex[index + 4] = (short) (i * 4 + 3);
            newIndex[index + 5] = (short) (i * 4 + 2);
        }
        _index = newIndex;

        _vertexArray = new VertexPositionColorTexture[4 * numBatchItems];
    }

    /**
     * Sorts the batch items and then groups batch drawing into maximal allowed batch sets that
     * do not overflow the 16 bit array indices for vertices.
     * 
     * @param sortMode
     *        The type of depth sorting desired for the rendering.
     * @param effect
     *        The custom effect to apply to the drawn geometry
     */
    public void drawBatch(SpriteSortMode sortMode, Effect effect)
    {
		if (effect != null && effect.isDisposed())
            throw new IllegalArgumentException("effect is disposed");

        // nothing to do
        if (_batchItemCount == 0)
            return;

        // sort the batch items
        switch (sortMode)
        {
            case Texture:
            case FrontToBack:
            case BackToFront:
                Arrays.sort(_batchItemList, 0, _batchItemCount);
                break;
            default:
                break;
        }

        // Determine how many iterations through the drawing code we need to make
        int batchIndex = 0;
        int batchCount = _batchItemCount;

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

            // Draw the batches
            for (int i = 0; i < numBatchesToProcess; ++i, ++batchIndex)
            {
                SpriteBatchItem item = _batchItemList[batchIndex];
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
            }
            // flush the remaining vertexArray data
            flushVertexArray(startIndex, index, effect, tex);
            // Update our batch count to continue the process of culling down
            // large batches
            batchCount -= numBatchesToProcess;
        }
		// return items to the pool.  
        _batchItemCount = 0;
    }

    /**
     * Sends the triangle list to the graphics device. Here is where the actual drawing starts.
     * 
     * @param start
     *        Start index of vertices to draw. Not used except to compute the count of vertices to
     *        draw.
     * @param end
     *        End index of vertices to draw. Not used except to compute the count of vertices to
     *        draw.
     * @param effect
     *        The custom effect to apply to the geometry.
     * @param texture
     *        The texture we want to draw.
     */
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

                _device.drawUserIndexedPrimitives(PrimitiveType.TriangleList,
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
            _device.drawUserIndexedPrimitives(PrimitiveType.TriangleList,
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
