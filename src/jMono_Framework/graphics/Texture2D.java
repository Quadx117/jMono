package jMono_Framework.graphics;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.io.Stream;
import jMono_Framework.utilities.ReflectionHelpers;

import java.io.OutputStream;

public class Texture2D extends Texture
{
    protected enum SurfaceType
    {
        Texture,
        RenderTarget,
        SwapChainRenderTarget,
    }

    protected int width;
    protected int height;
    protected int arraySize;

    private float _texelWidth;
    private float _texelHeight;

    protected float getTexelWidth()
    {
        return _texelWidth;
    }

    protected float getTexelHeight()
    {
        return _texelHeight;
    }

    /**
     * Returns the dimensions of the texture.
     * 
     * @return The dimensions of the texture.
     */
    public Rectangle getBounds()
    {
        return new Rectangle(0, 0, this.width, this.height);
    }

    /**
     * Creates a new texture of the given size.
     * 
     * @param graphicsDevice
     * @param width
     * @param height
     * @throws NullPointerException
     *         If the {@code graphicsDevice} is {@code null}.
     * @throws IllegalArgumentException
     *         If the current {@link GraphicsDevice} can't work with texture arrays.
     */
    public Texture2D(GraphicsDevice graphicsDevice, int width, int height) throws NullPointerException, IllegalArgumentException
    {
        this(graphicsDevice, width, height, false, SurfaceFormat.Color, SurfaceType.Texture, false, 1);
    }

    /**
     * Creates a new texture of a given size with a surface format and optional mipmaps.
     * 
     * @param graphicsDevice
     * @param width
     * @param height
     * @param mipmap
     * @param format
     * @throws NullPointerException
     *         If the {@code graphicsDevice} is {@code null}.
     * @throws IllegalArgumentException
     *         If the current {@link GraphicsDevice} can't work with texture arrays.
     */
    public Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format) throws NullPointerException, IllegalArgumentException
    {
        this(graphicsDevice, width, height, mipmap, format, SurfaceType.Texture, false, 1);
    }

    /**
     * Creates a new texture array of a given size with a surface format and optional mipmaps.
     * 
     * @param graphicsDevice
     * @param width
     * @param height
     * @param mipmap
     * @param format
     * @param arraySize
     * 
     * @throws NullPointerException
     *         If the {@code graphicsDevice} is {@code null}.
     * @throws IllegalArgumentException
     *         If the current {@link GraphicsDevice} can't work with texture arrays.
     */
    public Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format, int arraySize) throws NullPointerException, IllegalArgumentException
    {
        this(graphicsDevice, width, height, mipmap, format, SurfaceType.Texture, false, arraySize);
    }

    /**
     * Creates a new texture of a given size with a surface format and optional mipmaps.
     * 
     * @param graphicsDevice
     * @param width
     * @param height
     * @param mipmap
     * @param format
     * @param type
     * @throws NullPointerException
     *         If the {@code graphicsDevice} is {@code null}.
     * @throws IllegalArgumentException
     *         If the current {@link GraphicsDevice} can't work with texture arrays.
     */
    protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format, SurfaceType type) throws NullPointerException, IllegalArgumentException
    {
        this(graphicsDevice, width, height, mipmap, format, type, false, 1);
    }

    protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap,
                        SurfaceFormat format, SurfaceType type, boolean shared, int arraySize)
            throws NullPointerException, IllegalArgumentException
    {
        if (graphicsDevice == null)
            throw new NullPointerException("Graphics Device Cannot Be Null");
        if (width <= 0)
            throw new IllegalArgumentException("width: Texture width must be greater than zero");
        if (height <= 0)
            throw new IllegalArgumentException("height: Texture height must be greater than zero");
        if (arraySize > 1 && !graphicsDevice.getGraphicsCapabilities().supportsTextureArrays)
            throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySize");

        this.setGraphicsDevice(graphicsDevice);
        this.width = width;
        this.height = height;
        this._texelWidth = 1f / (float) width;
        this._texelHeight = 1f / (float) height;

        this._format = format;
        this._levelCount = mipmap ? calculateMipLevels(width, height) : 1;
        this.arraySize = arraySize;

        // Texture will be assigned by the swap chain.
        if (type.equals(SurfaceType.SwapChainRenderTarget))
            return;

        platformConstruct(width, height, mipmap, format, type, shared);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    // TODO(Eric): Should I create all primitives type or send the primitives through
    // this method as their Wrapper Class counterpart ?
    // This means creating a bunch of utilities method to do the conversion
    /**
     * Changes the pixels of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param level
     *        Layer of the texture to modify.
     * @param arraySlice
     *        Index inside the texture array.
     * @param rect
     *        Area to modify.
     * @param data
     *        New data for the texture.
     * @param startIndex
     *        Start position of data.
     * @param elementCount
     *        Number of pixels to read.
     * @throws IllegalArgumentException
     *         If arraySlice is greater than 0, and the GraphicsDevice does not support texture
     *         arrays.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void setData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount) throws IllegalArgumentException, NullPointerException
    {
        Rectangle checkedRect = new Rectangle();
        validateParams(level, arraySlice, rect, data, startIndex, elementCount, checkedRect);
        platformSetData(level, arraySlice, rect, data, startIndex, elementCount);
    }

    // NOTE(Eric): Added this method, see previous TODO
    public void setData(int level, int arraySlice, Rectangle rect, byte[] data, int startIndex, int elementCount)
    {
        if (data == null)
            throw new NullPointerException("data cannot be null");

        if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
            throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");

        platformSetData(level, arraySlice, rect, data, startIndex, elementCount);
    }

    // TODO(Eric): Should I create all primitives type or send the primitives through
    // this method as their Wrapper Class counterpart ?
    // This means creating a bunch of utilities method to do the conversion
    /**
     * Changes the pixels of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param level
     *        Layer of the texture to modify.
     * @param rect
     *        Area to modify.
     * @param data
     *        New data for the texture.
     * @param startIndex
     *        Start position of data.
     * @param elementCount
     *        Number of pixels to read.
     */
    public <T> void setData(int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        this.setData(level, 0, rect, data, startIndex, elementCount);
    }

    // NOTE(Eric): Added this method, see previous TODO
    public void setData(int level, Rectangle rect, byte[] data, int startIndex, int elementCount)
    {
        this.setData(level, 0, rect, data, startIndex, elementCount);
    }

    /**
     * Changes the texture's pixels
     * 
     * @param <T>
     *        The type of data.
     * @param data
     *        New data for the texture.
     * @param startIndex
     *        Start position of data.
     * @param elementCount
     *        Number of pixels to read.
     */
    public <T> void setData(T[] data, int startIndex, int elementCount)
    {
        this.setData(0, null, data, startIndex, elementCount);
    }

    /**
     * Changes the texture's pixels
     * 
     * @param <T>
     *        The type of data.
     * @param data
     *        New data for the texture.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void setData(T[] data) throws NullPointerException
    {
        if (data == null)
            throw new NullPointerException("data");
        this.setData(0, null, data, 0, data.length);
    }

    // NOTE(Eric): Added this method, see previous TODO
    // TODO(Eric): Do something like content.load(T[] data, Class.class)?
    public <T> void setData(Color[] data)
    {
        int length = data.length * 4; // Color is made of four byte components
        byte[] byteArray = new byte[length];
        for (int i = 0, j = 0; i < data.length; ++i, j += 4)
        {
            byteArray[j + 0] = (byte) data[i].getRed();
            byteArray[j + 1] = (byte) data[i].getGreen();
            byteArray[j + 2] = (byte) data[i].getBlue();
            byteArray[j + 3] = (byte) data[i].getAlpha();
        }
        this.setData(0, null, byteArray, 0, byteArray.length);
    }

    // TODO(Eric): Do something like content.load(T[] data, Class.class)?
    /**
     * Retrieves the contents of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param level
     *        Layer of the texture.
     * @param arraySlice
     *        Index inside the texture array.
     * @param rect
     *        Area of the texture to retrieve.
     * @param data
     *        Destination array for the data.
     * @param startIndex
     *        Starting index of data where to write the pixel data.
     * @param elementCount
     *        Number of pixels to read.
     * @throws IllegalArgumentException
     *         If data is null, data.length is too short or if arraySlice is greater than 0 and the
     *         GraphicsDevice doesn't support texture arrays.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void getData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount) throws IllegalArgumentException, NullPointerException
    {
        Rectangle checkedRect = new Rectangle();
        validateParams(level, arraySlice, rect, data, startIndex, elementCount, checkedRect);
        platformGetData(level, arraySlice, rect, data, startIndex, elementCount);
    }

    /**
     * Retrieves the contents of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param level
     *        Layer of the texture.
     * @param rect
     *        Area of the texture.
     * @param data
     *        Destination array for the texture data.
     * @param startIndex
     *        First position in data where to write the pixel data.
     * @param elementCount
     *        Number of pixels to read.
     * @throws IllegalArgumentException
     *         If data is null, data.length is too short or if arraySlice is greater than 0 and the
     *         GraphicsDevice doesn't support texture arrays.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void getData(int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        this.getData(level, 0, rect, data, startIndex, elementCount);
    }

    /**
     * Retrieves the contents of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param data
     *        Destination array for the texture data.
     * @param startIndex
     *        First position in data where to write the pixel data.
     * @param elementCount
     *        Number of pixels to read.
     * @throws IllegalArgumentException
     *         If data is null, data.length is too short or if arraySlice is greater than 0 and the
     *         GraphicsDevice doesn't support texture arrays.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void getData(T[] data, int startIndex, int elementCount)
    {
        this.getData(0, null, data, startIndex, elementCount);
    }

    /**
     * Retrieves the contents of the texture.
     * 
     * @param <T>
     *        The type of data.
     * @param data
     *        Destination array for the texture data.
     * @throws IllegalArgumentException
     *         If data is null, data.length is too short or if arraySlice is greater than 0 and the
     *         GraphicsDevice doesn't support texture arrays.
     * @throws NullPointerException
     *         If {@code data} is {@code null}.
     */
    public <T> void getData(T[] data) throws NullPointerException
    {
        if (data == null)
            throw new NullPointerException("data");
        this.getData(0, null, data, 0, data.length);
    }

    /**
     * Creates a Texture2D from a stream, supported formats bmp, gif, jpg, png, tif and dds (only
     * for simple textures).
     * May work with other formats, but will not work with tga files.
     * 
     * @param graphicsDevice
     * @param stream
     * @return
     * @throws NullPointerException
     *         If the {@code graphicsDevice} or {@code stream} is {@code null}.
     * @throws IllegalStateException
     *         If the image format is not supported.
     */
    public static Texture2D fromStream(GraphicsDevice graphicsDevice, Stream stream) throws NullPointerException, IllegalStateException
    {
        if (graphicsDevice == null)
            throw new NullPointerException("graphicsDevice");
        if (stream == null)
            throw new NullPointerException("stream");

        try
        {
            return platformFromStream(graphicsDevice, stream);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("This image format is not supported", e);
        }
    }

    // TODO(Eric): See if I can use my Stream class here
    /**
     * Converts the texture to a JPG image
     * 
     * @param stream
     *        Destination for the image.
     * @param width
     * @param height
     */
    public void saveAsJpeg(OutputStream stream, int width, int height)
    {
        platformSaveAsJpeg(stream, width, height);
    }

    // TODO(Eric): See if I can use my Stream class here
    /**
     * Converts the texture to a PNG image
     * 
     * @param stream
     *        Destination for the image.
     * @param width
     * @param height
     */
    public void saveAsPng(OutputStream stream, int width, int height)
    {
        platformSaveAsPng(stream, width, height);
    }

    // This method allows games that use Texture2D.fromStream
    // to reload their textures after the GL context is lost.
    public void reload(Stream textureStream)
    {
        platformReload(textureStream);
    }

    // Converts Pixel Data from ARGB to ABGR
    private static void convertToABGR(int pixelHeight, int pixelWidth, int[] pixels)
    {
        int pixelCount = pixelWidth * pixelHeight;
        for (int i = 0; i < pixelCount; ++i)
        {
            int pixel = pixels[i];
            pixels[i] = (pixel & 0xFF00FF00) | ((pixel & 0x00FF0000) >> 16) | ((pixel & 0x000000FF) << 16);
        }
    }

    private <T> void validateParams(int level, int arraySlice, Rectangle rect, T[] data,
                                    int startIndex, int elementCount, Rectangle checkedRect) throws IllegalArgumentException, NullPointerException
    {
        Rectangle textureBounds = new Rectangle(0, 0, Math.max(width >> level, 1), Math.max(height >> level, 1));
        checkedRect = (rect != null) ? rect : textureBounds;
        if (level < 0 || level >= getLevelCount())
            throw new IllegalArgumentException("level must be smaller than the number of levels in this texture.");
        if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
            throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");
        if (arraySlice < 0 || arraySlice >= arraySize)
            throw new IllegalArgumentException("arraySlice must be smaller than the ArraySize of this texture and larger than 0.");
        if (!textureBounds.contains(checkedRect) || checkedRect.width <= 0 || checkedRect.height <= 0)
            throw new IllegalArgumentException("rect: Rectangle must be inside the texture bounds");
        if (data == null)
            throw new NullPointerException("data");
        // TODO(Eric): Find a way to do this check in Java
        int tSize = ReflectionHelpers.sizeOf(data.getClass());
        int fSize = getFormat().getSize();
        if (tSize > fSize || fSize % tSize != 0)
            throw new IllegalArgumentException("Type T is of an invalid size for the format of this texture.");
        if (startIndex < 0 || startIndex >= data.length)
            throw new IllegalArgumentException("startIndex must be at least zero and smaller than data.Length.");
        if (data.length < startIndex + elementCount)
            throw new IllegalArgumentException("The data array is too small.");

        int dataByteSize;
        if (getFormat().isCompressedFormat())
        {
            // round x and y down to next multiple of four; width and height up to next multiple of
            // four
            int roundedWidth = (checkedRect.width + 3) & ~0x3;
            int roundedHeight = (checkedRect.height + 3) & ~0x3;
            checkedRect.x = checkedRect.x & ~0x3;
            checkedRect.y = checkedRect.y & ~0x3;
// #if OPENGL
            // OpenGL only: The last two mip levels require the width and height to be
            // passed as 2x2 and 1x1, but there needs to be enough data passed to occupy
            // a 4x4 block.
            // checkedRect.Width < 4 && textureBounds.Width < 4 ? textureBounds.Width :
            // roundedWidth,
            // checkedRect.Height < 4 && textureBounds.Height < 4 ? textureBounds.Height :
            // roundedHeight);
// #else
            checkedRect.width = roundedWidth;
            checkedRect.height = roundedHeight;
// #endif
            dataByteSize = roundedWidth * roundedHeight * fSize / 16;
        }
        else
        {
            dataByteSize = checkedRect.width * checkedRect.height * fSize;
        }
        if (elementCount * tSize != dataByteSize)
            throw new IllegalArgumentException(String.format("elementCount is not the right size, " +
                                                             "elementCount * sizeof(T) is %d, but data size is %d.",
                                                             elementCount * tSize, dataByteSize));
    }

    // @formatter:off
    // ########################################################################
    // #                        Platform specific code                        #
    // ########################################################################
    // @formatter:on

    //
    // NOTE(Eric): Adapted from Texture2D.DirectX.ca
    //

    private boolean _shared;

    private boolean _renderTarget;
    private boolean _mipmap;

    // private SampleDescription _sampleDescription;

    private void platformConstruct(int width, int height, boolean mipmap, SurfaceFormat format, SurfaceType type, boolean shared)
    {
        _shared = shared;

        _renderTarget = (type.equals(SurfaceType.RenderTarget));
        _mipmap = mipmap;
    }

    // TODO(Eric): Do something like content.load(T[] data, Class.class)?
    private <T> void platformSetData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        int elementSizeInByte = ReflectionHelpers.sizeOf(data.getClass());
        // var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
        // Use try..finally to make sure dataHandle is freed in case of an error
        try
        {
            int startBytes = startIndex * elementSizeInByte;
            // var dataPtr = (IntPtr)(dataHandle.AddrOfPinnedObject().ToInt64() + startBytes);
            // var dataPtr = (IntPtr)(dataHandle.AddrOfPinnedObject().ToInt64() + startBytes);
            // var region = new ResourceRegion();
            // region.Top = rect.Top;
            // region.Front = 0;
            // region.Back = 1;
            // region.Bottom = rect.Bottom;
            // region.Left = rect.Left;
            // region.Right = rect.Right;

            // TODO: We need to deal with threaded contexts here!
            int subresourceIndex = calculateSubresourceIndex(arraySlice, level);
            // var d3dContext = GraphicsDevice._d3dContext;
            // lock (d3dContext)
            // d3dContext.UpdateSubresource(GetTexture(), subresourceIndex, region, dataPtr,
            // GetPitch(rect.Width), 0);
        }
        finally
        {
            // dataHandle.Free();
        }
    }

    private <T> void platformGetData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        // Create a temp staging resource for copying the data.
        //
        // TODO: We should probably be pooling these staging resources
        // and not creating a new one each time.
        //
        int min = _format.isCompressedFormat() ? 4 : 1;
        int levelWidth = Math.max(width >> level, min);
        int levelHeight = Math.max(height >> level, min);

        // var desc = new SharpDX.Direct3D11.Texture2DDescription();
        // desc.Width = levelWidth;
        // desc.Height = levelHeight;
        // desc.MipLevels = 1;
        // desc.ArraySize = 1;
        // desc.Format = SharpDXHelper.ToFormat(_format);
        // desc.BindFlags = SharpDX.Direct3D11.BindFlags.None;
        // desc.CpuAccessFlags = SharpDX.Direct3D11.CpuAccessFlags.Read;
        // desc.SampleDescription.Count = 1;
        // desc.SampleDescription.Quality = 0;
        // desc.Usage = SharpDX.Direct3D11.ResourceUsage.Staging;
        // desc.OptionFlags = SharpDX.Direct3D11.ResourceOptionFlags.None;

        // Save sampling description.
        // _sampleDescription = desc.SampleDescription;

        // var d3dContext = GraphicsDevice._d3dContext;
        // try (var stagingTex = new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc))
        {
            // synchronized(d3dContext)
            {
                // int subresourceIndex = calculateSubresourceIndex(arraySlice, level);

                // Copy the data from the GPU to the staging texture.
                // int elementsInRow = rect.Width;
                // int rows = rect.Height;
                // var region = new ResourceRegion(rect.Left, rect.Top, 0, rect.Right, rect.Bottom,
                // 1);
                // d3dContext.CopySubresourceRegion(GetTexture(), subresourceIndex, region,
                // stagingTex, 0);

                // Copy the data to the array.
                // SharpDX.DataStream stream = null;
                try
                {
                    // var databox = d3dContext.MapSubresource(stagingTex, 0,
                    // SharpDX.Direct3D11.MapMode.Read, SharpDX.Direct3D11.MapFlags.None, out
                    // stream);

                    int elementSize = _format.getSize();
                    if (_format.isCompressedFormat())
                    {
                        // for 4x4 block compression formats an element is one block, so
                        // elementsInRow
                        // and number of rows are 1/4 of number of pixels in width and height of the
                        // rectangle
                        // elementsInRow /= 4;
                        // rows /= 4;
                    }
                    // int rowSize = elementSize * elementsInRow;
                    // if (rowSize == databox.RowPitch)
                    // stream.ReadRange(data, startIndex, elementCount);
                    // else
                    {
                        // Some drivers may add pitch to rows.
                        // We need to copy each row separatly and skip trailing zeros.
                        // stream.Seek(startIndex, SeekOrigin.Begin);

                        // int elementSizeInByte = Marshal.SizeOf(typeof(T));
                        // for (int row = 0; row < rows; ++row)
                        // {
                        // int i;
                        // for (i = row * rowSize / elementSizeInByte; i < (row + 1) * rowSize /
                        // elementSizeInByte; i++)
                        // data[i] = stream.Read<T>();

                        // if (i >= elementCount)
                        // break;

                        // stream.Seek(databox.RowPitch - rowSize, SeekOrigin.Current);
                        // }
                    }
                }
                finally
                {
                    // SharpDX.Utilities.Dispose( ref stream);
                }
            }
        }
    }

    private int calculateSubresourceIndex(int arraySlice, int level)
    {
        return arraySlice * _levelCount + level;
    }

    private static Texture2D platformFromStream(GraphicsDevice graphicsDevice, Stream stream)
    {
// #if WINDOWS_PHONE
        // WriteableBitmap bitmap = null;
        // Threading.BlockOnUIThread(() =>
        // {
        // try
        // {
        // BitmapImage bitmapImage = new BitmapImage();
        // bitmapImage.SetSource(stream);
        // bitmap = new WriteableBitmap(bitmapImage);
        // }
        // catch { }
        // });

        // Convert from ARGB to ABGR
        // convertToABGR(bitmap.PixelHeight, bitmap.PixelWidth, bitmap.Pixels);

        // Texture2D texture = new Texture2D(graphicsDevice, bitmap.PixelWidth, bitmap.PixelHeight);
        // texture.SetData<int>(bitmap.Pixels);
        // return texture;
// #endif
// #if !WINDOWS_PHONE

        // if (!stream.CanSeek)
        // throw new NotSupportedException("stream must support seek operations");

        // For reference this implementation was ultimately found through this post:
        // http://stackoverflow.com/questions/9602102/loading-textures-with-sharpdx-in-metro
        // Texture2D toReturn = null;
        // SharpDX.WIC.BitmapDecoder decoder;

        // using (var bitmap = LoadBitmap(stream, out decoder))
        // using (decoder)
        // {
        // SharpDX.Direct3D11.Texture2D sharpDxTexture = CreateTex2DFromBitmap(bitmap,
        // graphicsDevice);

        // toReturn = new Texture2D(graphicsDevice, bitmap.Size.Width, bitmap.Size.Height);

        // toReturn._texture = sharpDxTexture;
        // }
        // return toReturn;
// #endif
        throw new UnsupportedOperationException();
    }

    // TODO(Eric): See if I can use my Stream class here
    private void platformSaveAsJpeg(OutputStream stream, int width, int height)
    {
// #if WINDOWS_STOREAPP || WINDOWS_UAP
        // saveAsImage(BitmapEncoder.JpegEncoderId, stream, width, height);
// #endif
// #if WINDOWS_PHONE

        // TODO(Eric): Do I want to keep that like so or create GraphicsExtensions.getSize
        // byte[] pixelData = new byte[width * height * GraphicsExtensions.getSize(Format)];
        byte[] pixelData = new byte[width * height * _format.getSize()];
        // getData(pixelData);

        // We Must convert from BGRA to RGBA
        convertToRGBA(height, width, pixelData);

        // var waitEvent = new ManualResetEventSlim(false);
        // Deployment.Current.Dispatcher.BeginInvoke(() =>
        // {
        // var bitmap = new WriteableBitmap(Width, Height);
        // System.Buffer.BlockCopy(pixelData, 0, bitmap.Pixels, 0, pixelData.Length);
        // bitmap.SaveJpeg(stream, width, height, 0, 100);
        // waitEvent.Set();
        // });

        // waitEvent.Wait();
// #endif
// #if !WINDOWS_STOREAPP && !WINDOWS_PHONE && !WINDOWS_UAP
        throw new UnsupportedOperationException();
// #endif
    }

    // Converts Pixel Data from BGRA to RGBA
    private static void convertToRGBA(int pixelHeight, int pixelWidth, byte[] pixels)
    {
        int offset = 0;

        for (int row = 0; row < (int) pixelHeight; ++row)
        {
            for (int col = 0; col < (int) pixelWidth; ++col)
            {
                offset = (row * (int) pixelWidth * 4) + (col * 4);

                byte B = pixels[offset];
                byte R = pixels[offset + 2];

                pixels[offset] = R;
                pixels[offset + 2] = B;
            }
        }
    }

    // TODO(Eric): See if I can use my Stream class here
    private void platformSaveAsPng(OutputStream stream, int width, int height)
    {
        // PngWriter pngWriter = new PngWriter();
        // pngWriter.write(this, stream);
        throw new UnsupportedOperationException();
    }

// #if WINDOWS_STOREAPP || WINDOWS_UAP
    // private void saveAsImage(Guid encoderId, Stream stream, int width, int height)
    // {
    // TODO(Eric): Do I want to keep that like so or create GraphicsExtensions.getSize
    // byte[] pixelData = new byte[Width * Height * GraphicsExtensions.GetSize(Format)];
    // byte[] pixelData = new byte[width * height * _format.getSize()];
    // getData(pixelData);

    // TODO: We need to convert from Format to R8G8B8A8!

    // TODO: We should implement async SaveAsPng() for WinRT.
    // Task.Run(async () =>
    // {
    // Create a temporary memory stream for writing the png.
    // var memstream = new InMemoryRandomAccessStream();

    // Write the png.
    // var encoder = await BitmapEncoder.CreateAsync(encoderId, memstream);
    // encoder.SetPixelData(BitmapPixelFormat.Rgba8, BitmapAlphaMode.Ignore, (uint)width,
    // (uint)height, 96, 96, pixelData);
    // await encoder.FlushAsync();

    // Copy the memory stream into the real output stream.
    // memstream.Seek(0);
    // memstream.AsStreamForRead().CopyTo(stream);

    // }).Wait();
    // }
// #endif
// #if !WINDOWS_PHONE

    // static SharpDX.Direct3D11.Texture2D CreateTex2DFromBitmap(BitmapSource bsource,
    // GraphicsDevice device)
    // {

    // Texture2DDescription desc;
    // desc.Width = bsource.Size.Width;
    // desc.Height = bsource.Size.Height;
    // desc.ArraySize = 1;
    // desc.BindFlags = BindFlags.ShaderResource;
    // desc.Usage = ResourceUsage.Default;
    // desc.CpuAccessFlags = CpuAccessFlags.None;
    // desc.Format = SharpDX.DXGI.Format.R8G8B8A8_UNorm;
    // desc.MipLevels = 1;
    // desc.OptionFlags = ResourceOptionFlags.None;
    // desc.SampleDescription.Count = 1;
    // desc.SampleDescription.Quality = 0;

    // using(DataStream s = new DataStream(bsource.Size.Height * bsource.Size.Width * 4, true,
    // true))
    // {
    // bsource.CopyPixels(bsource.Size.Width * 4, s);
    //
    // DataRectangle rect = new DataRectangle(s.DataPointer, bsource.Size.Width * 4);
    //
    // return new SharpDX.Direct3D11.Texture2D(device._d3dDevice, desc, rect);
    // }
    // }

    // static ImagingFactory imgfactory;
    // private static BitmapSource LoadBitmap(Stream stream, out SharpDX.WIC.BitmapDecoder decoder)
    // {
    // if (imgfactory == null)
    // {
    // imgfactory = new ImagingFactory();
    // }

    // decoder = new SharpDX.WIC.BitmapDecoder(
    // imgfactory,
    // stream,
    // DecodeOptions.CacheOnDemand
    // );

    // var fconv = new FormatConverter(imgfactory);

    // fconv.Initialize(
    // decoder.GetFrame(0),
    // PixelFormat.Format32bppPRGBA,
    // BitmapDitherType.None, null,
    // 0.0, BitmapPaletteType.Custom);

    // return fconv;
    // }

// #endif
    // TODO(Eric): More research on this
    // internal override SharpDX.Direct3D11.Resource CreateTexture()
    @Override
    protected int[] createTexture()
    {
        // TODO: Move this to SetData() if we want to make Immutable textures!
        // var desc = new SharpDX.Direct3D11.Texture2DDescription();
        // desc.Width = width;
        // desc.Height = height;
        // desc.MipLevels = _levelCount;
        // desc.ArraySize = ArraySize;
        // desc.Format = SharpDXHelper.ToFormat(_format);
        // desc.BindFlags = SharpDX.Direct3D11.BindFlags.ShaderResource;
        // desc.CpuAccessFlags = SharpDX.Direct3D11.CpuAccessFlags.None;
        // desc.SampleDescription.Count = 1;
        // desc.SampleDescription.Quality = 0;
        // desc.Usage = SharpDX.Direct3D11.ResourceUsage.Default;
        // desc.OptionFlags = SharpDX.Direct3D11.ResourceOptionFlags.None;

        if (_renderTarget)
        {
            // desc.BindFlags |= SharpDX.Direct3D11.BindFlags.RenderTarget;
            if (_mipmap)
            {
                // Note: XNA 4 does not have a method Texture.GenerateMipMaps()
                // because generation of mipmaps is not supported on the Xbox 360.
                // TODO: New method Texture.GenerateMipMaps() required.
                // desc.OptionFlags |= SharpDX.Direct3D11.ResourceOptionFlags.GenerateMipMaps;
            }
        }
        // if (_shared)
        // desc.OptionFlags |= SharpDX.Direct3D11.ResourceOptionFlags.Shared;

        // Save sampling description.
        // _sampleDescription = desc.SampleDescription;

        // return new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc);
        return new int[width * height];
    }

    // internal SampleDescription GetTextureSampleDescription()
    // {
    // return _sampleDescription;
    // }

    // TODO(Eric): from Texture2D.OpenGL.cs
    // This method allows games that use Texture2D.fromStream
    // to reload their textures after the GL context is lost.
    private void platformReload(Stream textureStream)
    {
// #if WINDOWS_PHONE
        // Deployment.Current.Dispatcher.BeginInvoke(() =>
        // {
        // try
        // {
        // BitmapImage bitmapImage = new BitmapImage();
        // bitmapImage.SetSource(textureStream);
        // WriteableBitmap bitmap = new WriteableBitmap(bitmapImage);

        // Convert from ARGB to ABGR
        // ConvertToABGR(bitmap.PixelHeight, bitmap.PixelWidth, bitmap.Pixels);

        // this.SetData<int>(bitmap.Pixels);

        // textureStream.Dispose();
        // }
        // catch { }
        // });
// #endif
    }

    // NOTE(Eric): I added this method
    // TODO(Eric): Do something like content.load(T[] data, Class.class)?
    // TODO(Eric): Should I create all primitives type or send the primitives through
    // this method as their Wrapper Class counterpart ?
    // This means creating a bunch of utility methods to do the conversion.
    // NOTE(Eric): The data is expected to be in the RGBA format
    private void platformSetData(int level, int arraySlice, Rectangle rect, byte[] data, int startIndex, int elementCount)
    {
        int elementSize = _format.getSize();
        this._texture = new int[elementCount / elementSize];
        for (int i = 0, j = 0; i < elementCount; i += 4, ++j)
        {
            _texture[j] = (data[i + 3] & 0xFF) << 24 | // A component
                          (data[i + 2] & 0xFF) << 16 | // B component
                          (data[i + 1] & 0xFF) << 8 |  // G component
                          (data[i + 0] & 0xFF);        // R component
        }
    }

}
