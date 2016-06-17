package jMono_Framework.graphics.vertices;

import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;
import jMono_Framework.graphics.SetDataOptions;

// TODO: Stuff related to sizeof and elementSize (is it useful in Java ?)
public class IndexBuffer extends GraphicsResource {

//	private boolean _isDynamic;
	private BufferUsage bufferUsage;
	private int indexCount;
	private IndexElementSize indexElementSize;

	public BufferUsage getBufferUsage() {
		return bufferUsage;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public IndexElementSize getIndexElementSize() {
		return indexElementSize;
	}

	protected IndexBuffer(GraphicsDevice graphicsDevice, Class<?> indexType, int indexCount, BufferUsage usage,
			boolean dynamic)
	{
		this(graphicsDevice, sizeForType(graphicsDevice, indexType), indexCount, usage, dynamic);
	}

	protected IndexBuffer(GraphicsDevice graphicsDevice, IndexElementSize indexElementSize, int indexCount,
			BufferUsage usage, boolean dynamic)
	{
		if (graphicsDevice == null) {
			throw new NullPointerException("GraphicsDevice is null");
		}
		this.graphicsDevice = graphicsDevice;
		this.indexElementSize = indexElementSize;
		this.indexCount = indexCount;
		this.bufferUsage = usage;

//		_isDynamic = dynamic;

		platformConstruct(indexElementSize, indexCount);
	}

	// TODO: See other IndexBuffer files
	private void platformConstruct(IndexElementSize indexElementSize, int indexCount) {
		throw new UnsupportedOperationException();
	}

	public IndexBuffer(GraphicsDevice graphicsDevice, IndexElementSize indexElementSize, int indexCount,
			BufferUsage bufferUsage)
	{
		this(graphicsDevice, indexElementSize, indexCount, bufferUsage, false);
	}

	public IndexBuffer(GraphicsDevice graphicsDevice, Class<?> indexType, int indexCount, BufferUsage usage) {
		this(graphicsDevice, sizeForType(graphicsDevice, indexType), indexCount, usage, false);
	}

	// / <summary>
	// / Gets the relevant IndexElementSize enum value for the given type.
	// / </summary>
	// / <param name="graphicsDevice">The graphics device.</param>
	// / <param name="type">The type to use for the index buffer</param>
	// / <returns>The IndexElementSize enum value that matches the type</returns>
	static IndexElementSize sizeForType(GraphicsDevice graphicsDevice, Class<?> type) {
		return IndexElementSize.SixteenBits;
/*		switch (Marshal.SizeOf(type)) {
			case 2:
				return IndexElementSize.SixteenBits;
			case 4:
				if (graphicsDevice.getGraphicsProfile() == GraphicsProfile.Reach)
					throw new IllegalArgumentException(
							"The profile does not support an elementSize of IndexElementSize.ThirtyTwoBits; use IndexElementSize.SixteenBits or a type that has a size of two bytes.");
				return IndexElementSize.ThirtyTwoBits;
			default:
				throw new IllegalArgumentException(
						"Index buffers can only be created for types that are sixteen or thirty two bits in length");
		} */
	}

	// / <summary>
	// / The GraphicsDevice is resetting, so GPU resources must be recreated.
	// / </summary>
	@Override
	protected void graphicsDeviceResetting() {
		platformGraphicsDeviceResetting();
	}

	// TODO: See other IndexBuffer files
	private void platformGraphicsDeviceResetting() {
		throw new UnsupportedOperationException();
	}

	public <T> void getData(int offsetInBytes, T[] data, int startIndex, int elementCount) // where T : struct
	{
		if (data == null)
			throw new NullPointerException("data is null");
		if (data.length < (startIndex + elementCount))
			throw new IllegalArgumentException(
					"The array specified in the data parameter is not the correct size for the amount of data requested.");
		if (bufferUsage == BufferUsage.WriteOnly)
			throw new IllegalArgumentException(
					"This IndexBuffer was created with a usage type of BufferUsage.WriteOnly. Calling GetData on a resource that was created with BufferUsage.WriteOnly is not supported.");

		platformGetData(offsetInBytes, data, startIndex, elementCount);
	}

	// TODO: See other IndexBuffer files
	private <T> void platformGetData(int offsetInBytes, T[] data, int startIndex, int elementCount) {
		throw new UnsupportedOperationException();
	}

	public <T> void getData(T[] data, int startIndex, int elementCount) // where T : struct
	{
		this.getData(0, data, startIndex, elementCount);
	}

	public <T> void getData(T[] data) // where T : struct
	{
		this.getData(0, data, 0, data.length);
	}

	public <T> void setData(int offsetInBytes, T[] data, int startIndex, int elementCount) // where T : struct
	{
		setDataInternal(offsetInBytes, data, startIndex, elementCount, SetDataOptions.None);
	}

	public <T> void SetData(T[] data, int startIndex, int elementCount) // where T : struct
	{
		setDataInternal(0, data, startIndex, elementCount, SetDataOptions.None);
	}

	public <T> void SetData(T[] data) // where T : struct
	{
		setDataInternal(0, data, 0, data.length, SetDataOptions.None);
	}

	protected <T> void setDataInternal(int offsetInBytes, T[] data, int startIndex, int elementCount,
			SetDataOptions options) // where T : struct
	{
		if (data == null)
			throw new NullPointerException("data is null");
		if (data.length < (startIndex + elementCount))
			throw new IllegalArgumentException(
					"The array specified in the data parameter is not the correct size for the amount of data requested.");

		platformSetDataInternal(offsetInBytes, data, startIndex, elementCount, options);
	}

	// TODO: See other IndexBuffer files
	private <T> void platformSetDataInternal(int offsetInBytes, T[] data, int startIndex, int elementCount, SetDataOptions options) {
		throw new UnsupportedOperationException();
	}
	
	
}
