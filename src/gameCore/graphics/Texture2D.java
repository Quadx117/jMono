package gameCore.graphics;

import java.awt.Rectangle;
import java.util.stream.Stream;

public class Texture2D extends Texture
{
	protected enum SurfaceType
	{
		Texture, //
		RenderTarget, //
		SwapChainRenderTarget,
	}

	protected int width;
	protected int height;
	protected int arraySize;

	public Rectangle getBounds()
	{
		return new Rectangle(0, 0, this.width, this.height);
	}

	public Texture2D(GraphicsDevice graphicsDevice, int width, int height)
	{
		this(graphicsDevice, width, height, false, SurfaceFormat.Color, SurfaceType.Texture, false, 1);
	}

	public Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format)
	{
		this(graphicsDevice, width, height, mipmap, format, SurfaceType.Texture, false, 1);
	}
	
	public Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format,
			int arraySize)
	{
		this(graphicsDevice, width, height, mipmap, format, SurfaceType.Texture, false, arraySize);
	}

	protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format,
			SurfaceType type)
	{
		this(graphicsDevice, width, height, mipmap, format, type, false, 1);
	}

	protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format,
			SurfaceType type, boolean shared, int arraySize)
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("Graphics Device Cannot Be Null");
		}
		if (arraySize > 1 && !graphicsDevice.getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySize");
		
		this.graphicsDevice = graphicsDevice;
		this.width = width;
		this.height = height;
		this._format = format;
		this._levelCount = mipmap ? calculateMipLevels(width, height) : 1;
		this.arraySize = arraySize;

		// Texture will be assigned by the swap chain.
		if (type == SurfaceType.SwapChainRenderTarget)
			return;

		// TODO: See other Texture2D files
		// platformConstruct(width, height, mipmap, format, type, shared);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	// where T : struct
	public <T> void setData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data cannot be null");

		if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");
		
		// TODO: See other Texture2D files.
		// platformSetData(level, arraySlice, rect, data, startIndex, elementCount);
	}

	 // where T : struct
	public <T> void setData(int level, Rectangle rect, T[] data, int startIndex, int elementCount) 
	{
		this.setData(level, 0, rect, data, startIndex, elementCount);
	}
	
	// where T : struct
	public <T> void setData(T[] data, int startIndex, int elementCount)
	{
		this.setData(0, null, data, startIndex, elementCount);
	}

	// where T : struct
	public <T> void setData(T[] data)
	{
		this.setData(0, null, data, 0, data.length);
	}

	// where T : struct
	public <T> void getData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null || data.length == 0)
			throw new NullPointerException("data cannot be null");
		if (data.length < startIndex + elementCount)
			throw new IllegalArgumentException("The data passed has a length of " + data.length + " but "
					+ elementCount + " pixels have been requested.");
		if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");
		
		// TODO: See Texture2D files.
		// platformGetData(level, rect, data, startIndex, elementCount);
	}

	// where T : struct
	public <T> void getData(int level, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		this.getData(level, 0, rect, data, startIndex, elementCount);
	}
	
	// where T : struct
	public <T> void getData(T[] data, int startIndex, int elementCount)
	{
		this.getData(0, null, data, startIndex, elementCount);
	}

	// where T : struct
	public <T> void getData(T[] data)
	{
		this.getData(0, null, data, 0, data.length);
	}

	public static Texture2D fromStream(GraphicsDevice graphicsDevice, Stream stream)
	{
		return platformFromStream(graphicsDevice, stream);
	}

	public void saveAsJpeg(Stream stream, int width, int height)
	{
		platformSaveAsJpeg(stream, width, height);
	}

	public void saveAsPng(Stream stream, int width, int height)
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
			int pixel = (int) pixels[i];
			pixels[i] = (int) ((pixel & 0xFF00FF00) | ((pixel & 0x00FF0000) >> 16) | ((pixel & 0x000000FF) << 16));
		}
	}

	// TODO: From Texture2D.DirectX.cs
	// TODO: See the others Texture2D files
	private static Texture2D platformFromStream(GraphicsDevice graphicsDevice, Stream stream)
	{
// #if WINDOWS_PHONE
		// WriteableBitmap bitmap = null;
		// Threading.BlockOnUIThread(() =>
		// {
		// BitmapImage bitmapImage = new BitmapImage();
		// bitmapImage.SetSource(stream);
		// bitmap = new WriteableBitmap(bitmapImage);
		// });

		// Convert from ARGB to ABGR
		// ConvertToABGR(bitmap.PixelHeight, bitmap.PixelWidth, bitmap.Pixels);

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

	private void platformSaveAsJpeg(Stream stream, int width, int height)
	{
// #if MONOMAC || WINDOWS
		// TODO: Look into this method.
		// SaveAsImage(stream, width, height, ImageFormat.Jpeg);
// #if WINDOWS_STOREAPP
		// SaveAsImage(BitmapEncoder.JpegEncoderId, stream, width, height);
// #elsif WINDOWS_PHONE

		byte[] pixelData = new byte[width * height * _format.getSize()];
		// GetData(pixelData);

		// We Must convert from BGRA to RGBA
		convertToRGBA(height, width, pixelData);

		// var waitEvent = new ManualResetEventSlim(false);
		// Deployment.Current.Dispatcher.BeginInvoke(() =>
		// {
		// var bitmap = new WriteableBitmap(width, height);
		// System.Buffer.BlockCopy(pixelData, 0, bitmap.Pixels, 0, pixelData.Length);
		// bitmap.SaveJpeg(stream, width, height, 0, 100);
		// waitEvent.Set();
		// });

		// waitEvent.Wait();
// #else
		throw new UnsupportedOperationException();
// #endif
	}

	// TODO: from Texture2D.DirectX.cs
	private void platformSaveAsPng(Stream stream, int width, int height)
	{
// #if WINDOWS_STOREAPP
		// TODO: Look into this method.
		// SaveAsImage(BitmapEncoder.PngEncoderId, stream, width, height);
// #else
		// TODO: We need to find a simple stand alone
		// PNG encoder if we want to support this.
		throw new UnsupportedOperationException();
// #endif
	}

	// TODO: from Texture2D.OpenGL.cs
	// This method allows games that use Texture2D.fromStream
	// to reload their textures after the GL context is lost.
	private void platformReload(Stream textureStream)
	{
		// GenerateGLTextureIfRequired();
		// FillTextureFromStream(textureStream);
	}

}
