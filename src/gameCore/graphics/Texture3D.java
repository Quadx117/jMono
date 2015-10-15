package gameCore.graphics;

public class Texture3D extends Texture
{
	private int _width;
	private int _height;
	private int _depth;

	public int getWidth()
	{
		return _width;
	}

	public int getHeight()
	{
		return _height;
	}

	public int getDepth()
	{
		return _depth;
	}

	public Texture3D(GraphicsDevice graphicsDevice, int width, int height, int depth, boolean mipMap,
			SurfaceFormat format)
	{
		this(graphicsDevice, width, height, depth, mipMap, format, false);
	}

	protected Texture3D(GraphicsDevice graphicsDevice, int width, int height, int depth, boolean mipMap,
			SurfaceFormat format, boolean renderTarget)
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("graphicsDevice");
		}
		this.setGraphicsDevice(graphicsDevice);
		this._width = width;
		this._height = height;
		this._depth = depth;
		this._levelCount = 1;
		this._format = format;

		// TODO: See other Texture3D files.
		// platformConstruct(graphicsDevice, width, height, depth, mipMap, format, renderTarget);
	}

	// where T : struct
	public <T> void setData(T[] data)
	{
		setData(data, 0, data.length);
	}

	// where T : struct
	public <T> void setData(T[] data, int startIndex, int elementCount)
	{
		setData(0, 0, 0, _width, _height, 0, _depth, data, startIndex, elementCount);
	}

	// where T : struct
	public <T> void setData(int level, int left, int top, int right, int bottom, int front, int back, T[] data,
			int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data");

		int width = right - left;
		int height = bottom - top;
		int depth = back - front;

		// TODO: See other Textrure3D files
		// platformSetData(level, left, top, right, bottom, front, back, data, startIndex,
		// elementCount, width, height, depth);
	}

	// / <summary>
	// / Gets a copy of 3D texture data, specifying a mipmap level, source box, start index, and
	// number of elements.
	// / </summary>
	// / <typeparam name="T">The type of the elements in the array.</typeparam>
	// / <param name="level">Mipmap level.</param>
	// / <param name="left">Position of the left side of the box on the x-axis.</param>
	// / <param name="top">Position of the top of the box on the y-axis.</param>
	// / <param name="right">Position of the right side of the box on the x-axis.</param>
	// / <param name="bottom">Position of the bottom of the box on the y-axis.</param>
	// / <param name="front">Position of the front of the box on the z-axis.</param>
	// / <param name="back">Position of the back of the box on the z-axis.</param>
	// / <param name="data">Array of data.</param>
	// / <param name="startIndex">Index of the first element to get.</param>
	// / <param name="elementCount">Number of elements to get.</param>
	// where T : struct
	public <T> void getData(int level, int left, int top, int right, int bottom, int front, int back, T[] data,
			int startIndex, int elementCount)
	{
		if (data == null || data.length == 0)
			throw new NullPointerException("data cannot be null");
		if (data.length < startIndex + elementCount)
			throw new IllegalArgumentException("The data passed has a length of " + data.length + " but "
					+ elementCount + " pixels have been requested.");

		// Disallow negative box size
		if ((left < 0 || left >= right) ||	//
			(top < 0 || top >= bottom) ||	//
			(front < 0 || front >= back))
			throw new IllegalArgumentException("Neither box size nor box position can be negative");

		// TODO: See other Texture3d files
		// platformGetData(level, left, top, right, bottom, front, back, data, startIndex,
		// elementCount);
	}

	// / <summary>
	// / Gets a copy of 3D texture data, specifying a start index and number of elements.
	// / </summary>
	// / <typeparam name="T">The type of the elements in the array.</typeparam>
	// / <param name="data">Array of data.</param>
	// / <param name="startIndex">Index of the first element to get.</param>
	// / <param name="elementCount">Number of elements to get.</param>
	// where T : struct
	public <T> void getData(T[] data, int startIndex, int elementCount)
	{
		getData(0, 0, 0, _width, _height, 0, _depth, data, startIndex, elementCount);
	}

	// / <summary>
	// / Gets a copy of 3D texture data.
	// / </summary>
	// / <typeparam name="T">The type of the elements in the array.</typeparam>
	// / <param name="data">Array of data.</param>
	// where T : struct
	public <T> void getData(T[] data)
	{
		getData(data, 0, data.length);
	}

}
