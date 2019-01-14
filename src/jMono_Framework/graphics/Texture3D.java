package jMono_Framework.graphics;

import jMono_Framework.utilities.ReflectionHelpers;

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

    public Texture3D(GraphicsDevice graphicsDevice, int width, int height, int depth, boolean mipMap, SurfaceFormat format)
    {
        this(graphicsDevice, width, height, depth, mipMap, format, false);
    }

    protected Texture3D(GraphicsDevice graphicsDevice, int width, int height, int depth, boolean mipMap, SurfaceFormat format, boolean renderTarget)
    {
        if (graphicsDevice == null)
            throw new NullPointerException("graphicsDevice"); // , FrameworkResources.ResourceCreationWhenDeviceIsNull);
        if (width <= 0)
            throw new IllegalArgumentException("width: Texture width must be greater than zero");
        if (height <= 0)
            throw new IllegalArgumentException("height: Texture height must be greater than zero");
        if (depth <= 0)
            throw new IllegalArgumentException("depth: Texture depth must be greater than zero");

        this.setGraphicsDevice(graphicsDevice);
        this._width = width;
        this._height = height;
        this._depth = depth;
        this._levelCount = 1;
        this._format = format;

        platformConstruct(graphicsDevice, width, height, depth, mipMap, format, renderTarget);
    }

    public <T> void setData(T[] data)
    {
        if (data == null)
            throw new NullPointerException("data");
        setData(data, 0, data.length);
    }

    public <T> void setData(T[] data, int startIndex, int elementCount)
    {
        setData(0, 0, 0, _width, _height, 0, _depth, data, startIndex, elementCount);
    }

    public <T> void setData(int level,
                            int left, int top, int right, int bottom, int front, int back,
                            T[] data, int startIndex, int elementCount)
    {
        validateParams(level, left, top, right, bottom, front, back, data, startIndex, elementCount);

        int width = right - left;
        int height = bottom - top;
        int depth = back - front;

        platformSetData(level, left, top, right, bottom, front, back, data, startIndex, elementCount, width, height, depth);
    }

    /**
     * Returns a copy of 3D texture data, specifying a mipmap level, source box, start index, and
     * number of elements.
     * 
     * @param level
     *        Mipmap level.
     * @param left
     *        Position of the left side of the box on the x-axis.
     * @param top
     *        Position of the top of the box on the y-axis.
     * @param right
     *        Position of the right side of the box on the x-axis.
     * @param bottom
     *        Position of the bottom of the box on the y-axis.
     * @param front
     *        Position of the front of the box on the z-axis.
     * @param back
     *        Position of the back of the box on the z-axis.
     * @param data
     *        Array of data.
     * @param startIndex
     *        Index of the first element to get.
     * @param elementCount
     *        Number of elements to get.
     */
    public <T> void getData(int level, int left, int top, int right, int bottom, int front, int back, T[] data, int startIndex, int elementCount)
    {
        validateParams(level, left, top, right, bottom, front, back, data, startIndex, elementCount);
        platformGetData(level, left, top, right, bottom, front, back, data, startIndex, elementCount);
    }

    /**
     * Gets a copy of 3D texture data, specifying a start index and number of elements.
     * 
     * @param data
     *        Array of data.
     * @param startIndex
     *        Index of the first element to get.
     * @param elementCount
     *        Number of elements to get.
     */
    public <T> void getData(T[] data, int startIndex, int elementCount)
    {
        getData(0, 0, 0, _width, _height, 0, _depth, data, startIndex, elementCount);
    }

    /**
     * Gets a copy of 3D texture data.
     * 
     * @param data
     *        Array of data.
     */
    public <T> void getData(T[] data)
    {
        if (data == null)
            throw new NullPointerException("data");
        getData(data, 0, data.length);
    }

    private <T> void validateParams(int level,
                                    int left, int top, int right, int bottom, int front, int back,
                                    T[] data, int startIndex, int elementCount)
    {
        int texWidth = Math.max(getWidth() >> level, 1);
        int texHeight = Math.max(getHeight() >> level, 1);
        int texDepth = Math.max(getDepth() >> level, 1);
        int width = right - left;
        int height = bottom - top;
        int depth = back - front;

        if (left < 0 || top < 0 || back < 0 || right > texWidth || bottom > texHeight || front > texDepth)
            throw new IllegalArgumentException("Area must remain inside texture bounds");
        // Disallow negative box size
        if (left >= right || top >= bottom || front >= back)
            throw new IllegalArgumentException("Neither box size nor box position can be negative");
        if (level < 0 || level >= getLevelCount())
            throw new IllegalArgumentException("level must be smaller than the number of levels in this texture.");
        if (data == null)
            throw new NullPointerException("data");
        int tSize = ReflectionHelpers.sizeOf(data.getClass());
        int fSize = getFormat().getSize();
        if (tSize > fSize || fSize % tSize != 0)
            throw new IllegalArgumentException("Type T is of an invalid size for the format of this texture.");
        if (startIndex < 0 || startIndex >= data.length)
            throw new IllegalArgumentException("startIndex must be at least zero and smaller than data.Length.");
        if (data.length < startIndex + elementCount)
            throw new IllegalArgumentException("The data array is too small.");

        int dataByteSize = width * height * depth * fSize;
        if (elementCount * tSize != dataByteSize)
            throw new IllegalArgumentException(String.format("elementCount is not the right size, " +
                                                             "elementCount * sizeof(T) is {0}, but data size is {1}.",
                                                             elementCount * tSize, dataByteSize));
    }

    // @formatter:off
    // ########################################################################
    // #                        Platform specific code                        #
    // ########################################################################
    // @formatter:on

    // NOTE(Eric): Adapted from Texture3D.DirectX.cs

    private boolean renderTarget;
    private boolean mipMap;

    private void platformConstruct(GraphicsDevice graphicsDevice, int width, int height, int depth, boolean mipMap, SurfaceFormat format, boolean renderTarget)
    {
        this.renderTarget = renderTarget;
        this.mipMap = mipMap;

        if (mipMap)
            this._levelCount = calculateMipLevels(width, height, depth);

        // Create texture
        getTexture();
    }

    // TODO(Eric): More research on this
    // protected SharpDX.Direct3D11.Resource CreateTexture()
    @Override
    protected int[] createTexture()
    {
        // var description = new Texture3DDescription
        // {
        // Width = _width,
        // Height = _height,
        // Depth = _depth,
        // MipLevels = _levelCount,
        // Format = SharpDXHelper.ToFormat(_format),
        // BindFlags = BindFlags.ShaderResource,
        // CpuAccessFlags = CpuAccessFlags.None,
        // Usage = ResourceUsage.Default,
        // OptionFlags = ResourceOptionFlags.None,
        // };

        if (renderTarget)
        {
            // description.BindFlags |= BindFlags.RenderTarget;
            if (mipMap)
            {
                // Note: XNA 4 does not have a method Texture.GenerateMipMaps()
                // because generation of mipmaps is not supported on the Xbox 360.
                // TODO: New method Texture.GenerateMipMaps() required.
                // description.OptionFlags |= ResourceOptionFlags.GenerateMipMaps;
            }
        }

        // return new SharpDX.Direct3D11.Texture3D(GraphicsDevice._d3dDevice, description);
        return new int[_width * _height * _depth];
    }

    private <T> void platformSetData(int level,
                                     int left, int top, int right, int bottom, int front, int back,
                                     T[] data, int startIndex, int elementCount, int width, int height, int depth)
    {
        // var elementSizeInByte = Marshal.SizeOf(typeof(T));
        // var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
        // try
        // {
        // var dataPtr = (IntPtr)(dataHandle.AddrOfPinnedObject().ToInt64() + startIndex *
        // elementSizeInByte);

        // int rowPitch = getPitch(width);
        // int slicePitch = rowPitch * height; // For 3D texture: Size of 2D image.
        // var box = new DataBox(dataPtr, rowPitch, slicePitch);

        // int subresourceIndex = level;

        // var region = new ResourceRegion(left, top, front, right, bottom, back);

        // var d3dContext = GraphicsDevice._d3dContext;
        // lock (d3dContext)
        // d3dContext.UpdateSubresource(box, GetTexture(), subresourceIndex, region);
        // }
        // finally
        // {
        // dataHandle.Free();
        // }
    }

    private <T> void platformGetData(int level, int left, int top, int right, int bottom, int front, int back, T[] data, int startIndex, int elementCount)
    {
        // Create a temp staging resource for copying the data.
        //
        // TODO: Like in Texture2D, we should probably be pooling these staging resources
        // and not creating a new one each time.
        //
        // var desc = new Texture3DDescription
        // {
        // Width = _width,
        // Height = _height,
        // Depth = _depth,
        // MipLevels = 1,
        // Format = SharpDXHelper.ToFormat(_format),
        // BindFlags = BindFlags.None,
        // CpuAccessFlags = CpuAccessFlags.Read,
        // Usage = ResourceUsage.Staging,
        // OptionFlags = ResourceOptionFlags.None,
        // };

        // var d3dContext = GraphicsDevice._d3dContext;
        // using (var stagingTex = new SharpDX.Direct3D11.Texture3D(GraphicsDevice._d3dDevice,
        // desc))
        // {
        // lock (d3dContext)
        // {
        // Copy the data from the GPU to the staging texture.
        // d3dContext.CopySubresourceRegion(GetTexture(), level, new ResourceRegion(left, top,
        // front, right, bottom, back), stagingTex, 0);

        // Copy the data to the array.
        // DataStream stream = null;
        // try
        // {
        // var databox = d3dContext.MapSubresource(stagingTex, 0, MapMode.Read, MapFlags.None, out
        // stream);

        // Some drivers may add pitch to rows or slices.
        // We need to copy each row separately and skip trailing zeros.
        // int currentIndex = startIndex;
        // int elementSize = _format.getSize();
        // int elementsInRow = right - left;
        // int rowsInSlice = bottom - top;
        // for (int slice = front; slice < back; slice++)
        // {
        // for (int row = top; row < bottom; row++)
        // {
        // stream.ReadRange(data, currentIndex, elementsInRow);
        // stream.Seek(databox.RowPitch - (elementSize * elementsInRow), SeekOrigin.Current);
        // currentIndex += elementsInRow;
        // }
        // stream.Seek(databox.SlicePitch - (databox.RowPitch * rowsInSlice), SeekOrigin.Current);
        // }
        // }
        // finally
        // {
        // SharpDX.Utilities.Dispose(ref stream);
        // }
        // }
        // }
    }
}
