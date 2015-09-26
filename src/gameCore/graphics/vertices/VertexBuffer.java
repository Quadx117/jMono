package gameCore.graphics.vertices;

import gameCore.graphics.GraphicsResource;
import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.SetDataOptions;

;

public class VertexBuffer extends GraphicsResource {

	protected boolean _isDynamic;

	private int vertexCount;

	public int getVertexCount() {
		return vertexCount;
	}

	private VertexDeclaration vertexDeclaration;

	public VertexDeclaration getVertexDeclaration() {
		return vertexDeclaration;
	}

	private BufferUsage bufferUsage;

	public BufferUsage getBufferUsage() {
		return bufferUsage;
	}

	protected VertexBuffer(GraphicsDevice graphicsDevice, VertexDeclaration vertexDeclaration, int vertexCount,
			BufferUsage bufferUsage, boolean dynamic)
	{
		if (graphicsDevice == null)
			throw new NullPointerException("Graphics Device Cannot Be null");

		this.graphicsDevice = graphicsDevice;
		this.vertexDeclaration = vertexDeclaration;
		this.vertexCount = vertexCount;
		this.bufferUsage = bufferUsage;

		// Make sure the graphics device is assigned in the vertex declaration.
		if (vertexDeclaration.getGraphicsDevice() != graphicsDevice)
			vertexDeclaration.setGraphicsDevice(graphicsDevice);

		_isDynamic = dynamic;

		platformConstruct();
	}

	// TODO: See other VertexBuffer files.
	private void platformConstruct() {
		throw new UnsupportedOperationException();
	}

	public VertexBuffer(GraphicsDevice graphicsDevice, VertexDeclaration vertexDeclaration, int vertexCount,
			BufferUsage bufferUsage)
	{
		this(graphicsDevice, vertexDeclaration, vertexCount, bufferUsage, false);
	}

	public VertexBuffer(GraphicsDevice graphicsDevice, Class<?> type, int vertexCount, BufferUsage bufferUsage) {
		this(graphicsDevice, VertexDeclaration.fromType(type), vertexCount, bufferUsage, false);
	}

	// / <summary>
	// / The GraphicsDevice is resetting, so GPU resources must be recreated.
	// / </summary>
	@Override
	protected void graphicsDeviceResetting() {
		platformGraphicsDeviceResetting();
	}

	// TODO: See other VertexBuffer files.
	private void platformGraphicsDeviceResetting() {
		throw new UnsupportedOperationException();
	}

	public <T> void getData(int offsetInBytes, T[] data, int startIndex, int elementCount, int vertexStride) // where T : struct
	{
		if (data == null)
			throw new NullPointerException("data : This method does not accept null for this parameter.");
		if (data.length < (startIndex + elementCount))
			throw new IllegalArgumentException("elementCount: This parameter must be a valid index within the array.");
		if (bufferUsage == BufferUsage.WriteOnly)
			throw new UnsupportedOperationException(
					"Calling GetData on a resource that was created with BufferUsage.WriteOnly is not supported.");
		if ((elementCount * vertexStride) > (vertexCount * vertexDeclaration.getVertexStride()))
			throw new IllegalArgumentException("The array is not the correct size for the amount of data requested.");

		platformGetData(offsetInBytes, data, startIndex, elementCount, vertexStride);
	}

	// TODO: See other VertexBuffer files.
	private <T> void platformGetData(int offsetInBytes, T[] data, int startIndex, int elementCount, int vertexStride) {
		throw new UnsupportedOperationException();
	}

	public <T> void getData(T[] data, int startIndex, int elementCount) // where T : struct
	{
		// TODO: elementSizeIntByte
		// int elementSizeInByte = Marshal.SizeOf(typeof(T));
		this.getData(0, data, startIndex, elementCount, 1);
	}

	public <T> void getData(T[] data) // where T : struct
	{
		// TODO: elementSizeIntByte
		// byte elementSizeInByte = Marshal.SizeOf(typeof(T));
		this.getData(0, data, 0, data.length, 1);
	}

	public <T> void setData(int offsetInBytes, T[] data, int startIndex, int elementCount, int vertexStride) // where T : struct
	{
		setDataInternal(offsetInBytes, data, startIndex, elementCount, vertexDeclaration.getVertexStride(),
				SetDataOptions.None);
	}

	public <T> void setData(T[] data, int startIndex, int elementCount) // where T : struct
	{
		setDataInternal(0, data, startIndex, elementCount, vertexDeclaration.getVertexStride(), SetDataOptions.None);
	}

	public <T> void setData(T[] data) // where T : struct
	{
		setDataInternal(0, data, 0, data.length, vertexDeclaration.getVertexStride(), SetDataOptions.None);
	}

	protected <T> void setDataInternal(int offsetInBytes, T[] data, int startIndex, int elementCount, int vertexStride,
			SetDataOptions options) // where T : struct
	{
		if (data == null)
			throw new NullPointerException("data is null");
		if (data.length < (startIndex + elementCount))
			throw new IllegalArgumentException(
					"The array specified in the data parameter is not the correct size for the amount of data requested.");

		int bufferSize = vertexCount * vertexDeclaration.getVertexStride();
		if ((vertexStride > bufferSize) || (vertexStride < vertexDeclaration.getVertexStride()))
			throw new IllegalArgumentException(
					"One of the following conditions is true:\nThe vertex stride is larger than the vertex buffer.\nThe vertex stride is too small for the type of data requested.");

		// TODO: elementSizeIntByte
		// int elementSizeInBytes = Marshal.SizeOf(typeof(T));
		platformSetDataInternal(offsetInBytes, data, startIndex, elementCount, vertexStride, options, bufferSize, 1);
	}

	// TODO: See other VertexBuffer files.
	private <T> void platformSetDataInternal(int offsetInBytes, T[] data, int startIndex, int elementCount, int vertexStride,
			SetDataOptions options, int bufferSize, int elementSizeInByte) {
		throw new UnsupportedOperationException();
	}
}
