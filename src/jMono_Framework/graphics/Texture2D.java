package jMono_Framework.graphics;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.io.Stream;

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

	public Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format, int arraySize)
	{
		this(graphicsDevice, width, height, mipmap, format, SurfaceType.Texture, false, arraySize);
	}

	protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format, SurfaceType type)
	{
		this(graphicsDevice, width, height, mipmap, format, type, false, 1);
	}

	protected Texture2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipmap, SurfaceFormat format, SurfaceType type, boolean shared, int arraySize)
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("Graphics Device Cannot Be Null");
		}
		if (arraySize > 1 && !graphicsDevice.getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySize");

		this.setGraphicsDevice(graphicsDevice);
		this.width = width;
		this.height = height;
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

	// TODO: Should I create all primitives type or send the primitives through
	// this method as their Wrapper Class counterpart ?
	// This means creating a bunch of utilities method to do the conversion
	public <T> void setData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data cannot be null");

		if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");

		platformSetData(level, arraySlice, rect, data, startIndex, elementCount);
	}

	// NOTE: Added this method, see previous TODO
	public void setData(int level, int arraySlice, Rectangle rect, byte[] data, int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data cannot be null");

		if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");

		platformSetData(level, arraySlice, rect, data, startIndex, elementCount);
	}

	// TODO: Should I create all primitives type or send the primitives through
	// this method as their Wrapper Class counterpart ?
	// This means creating a bunch of utilities method to do the conversion
	public <T> void setData(int level, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		this.setData(level, 0, rect, data, startIndex, elementCount);
	}

	// NOTE: Added this method, see previous TODO
	public void setData(int level, Rectangle rect, byte[] data, int startIndex, int elementCount)
	{
		this.setData(level, 0, rect, data, startIndex, elementCount);
	}

	public <T> void setData(T[] data, int startIndex, int elementCount)
	{
		this.setData(0, null, data, startIndex, elementCount);
	}

	public <T> void setData(T[] data)
	{
		this.setData(0, null, data, 0, data.length);
	}

	// NOTE: Added this method, see previous TODO
	// TODO: Do something like content.load(T[] data, Class.class)?
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

	// TODO: Do something like content.load(T[] data, Class.class)?
	public <T> void getData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null || data.length == 0)
			throw new NullPointerException("data cannot be null");
		if (data.length < startIndex + elementCount)
			throw new IllegalArgumentException("The data passed has a length of " + data.length + " but " + elementCount + " pixels have been requested.");
		if (arraySlice > 0 && !getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device: arraySlice");

		platformGetData(level, arraySlice, rect, data, startIndex, elementCount);
	}

	public <T> void getData(int level, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		this.getData(level, 0, rect, data, startIndex, elementCount);
	}

	public <T> void getData(T[] data, int startIndex, int elementCount)
	{
		this.getData(0, null, data, startIndex, elementCount);
	}

	public <T> void getData(T[] data)
	{
		this.getData(0, null, data, 0, data.length);
	}

	public static Texture2D fromStream(GraphicsDevice graphicsDevice, Stream stream)
	{
		return platformFromStream(graphicsDevice, stream);
	}

	// TODO: See if I can use my Stream class here
	public void saveAsJpeg(OutputStream stream, int width, int height)
	{
		platformSaveAsJpeg(stream, width, height);
	}

	// TODO: See if I can use my Stream class here
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

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	private boolean _shared;

	private boolean _renderTarget;
	private boolean _mipmap;
	private void platformConstruct(int width, int height, boolean mipmap, SurfaceFormat format, SurfaceType type, boolean shared)
	{
		_shared = shared;

		_renderTarget = (type.equals(SurfaceType.RenderTarget));
		_mipmap = mipmap;

		// Create texture
		getTexture();
	}

	// TODO: Do something like content.load(T[] data, Class.class)?
	private <T> void platformSetData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		// TODO: Create my own Marshal Class (like the load method) ?
//		var elementSizeInByte = Marshal.SizeOf(typeof(T));
//		var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
		// Use try..finally to make sure dataHandle is freed in case of an error
		try
		{
//			var startBytes = startIndex * elementSizeInByte;
//			var dataPtr = (IntPtr)(dataHandle.AddrOfPinnedObject().ToInt64() + startBytes);
			int x, y, w, h;
			if (rect != null)
			{
				x = rect.x;
				y = rect.y;
				w = rect.width;
				h = rect.height;
			}
			else
			{
				x = 0;
				y = 0;
				w = Math.max(width >> level, 1);
				h = Math.max(height >> level, 1);

				// For DXT textures the width and height of each level is a multiple of 4.
				// OpenGL only: The last two mip levels require the width and height to be 
				// passed as 2x2 and 1x1, but there needs to be enough data passed to occupy 
				// a 4x4 block. 
				// Ref: http://www.mentby.com/Group/mac-opengl/issue-with-dxt-mipmapped-textures.html 
				if (_format.equals(SurfaceFormat.Dxt1) ||
					_format.equals(SurfaceFormat.Dxt1SRgb) ||
					_format.equals(SurfaceFormat.Dxt1a) ||
					_format.equals(SurfaceFormat.Dxt3) ||
					_format.equals(SurfaceFormat.Dxt3SRgb) ||
					_format.equals(SurfaceFormat.Dxt5) ||
					_format.equals(SurfaceFormat.Dxt5SRgb))
				{
					w = (w + 3) & ~3;
					h = (h + 3) & ~3;
				}
			}

//			var box = new SharpDX.DataBox(dataPtr, GetPitch(w), 0);

//			var region = new SharpDX.Direct3D11.ResourceRegion();
//			region.Top = y;
//			region.Front = 0;
//			region.Back = 1;
//			region.Bottom = y + h;
//			region.Left = x;
//			region.Right = x + w;

			// TODO: We need to deal with threaded contexts here!
			int subresourceIndex = calculateSubresourceIndex(arraySlice, level);
//			var d3dContext = GraphicsDevice._d3dContext;
//			synchronized(d3dContext)
//				d3dContext.UpdateSubresource(box, GetTexture(), subresourceIndex, region);
		}
		finally
		{
//			dataHandle.Free();
		}
    }

	private <T> void platformGetData(int level, int arraySlice, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		// Create a temp staging resource for copying the data.
		//
		// TODO: We should probably be pooling these staging resources
		// and not creating a new one each time.
		//
		int levelWidth = Math.max(width >> level, 1);
		int levelHeight = Math.max(height >> level, 1);

//		var desc = new SharpDX.Direct3D11.Texture2DDescription();
//		desc.Width = levelWidth;
//		desc.Height = levelHeight;
//		desc.MipLevels = 1;
//		desc.ArraySize = 1;
//		desc.Format = SharpDXHelper.ToFormat(_format);
//		desc.BindFlags = SharpDX.Direct3D11.BindFlags.None;
//		desc.CpuAccessFlags = SharpDX.Direct3D11.CpuAccessFlags.Read;
//		desc.SampleDescription.Count = 1;
//		desc.SampleDescription.Quality = 0;
//		desc.Usage = SharpDX.Direct3D11.ResourceUsage.Staging;
//		desc.OptionFlags = SharpDX.Direct3D11.ResourceOptionFlags.None;

//		var d3dContext = GraphicsDevice._d3dContext;
//		try (var stagingTex = new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc))
		{
//			synchronized(d3dContext)
			{
				int subresourceIndex = calculateSubresourceIndex(arraySlice, level);

				// Copy the data from the GPU to the staging texture.
				int elementsInRow;
				int rows;
				if (rect != null)
				{
					elementsInRow = rect.width;
					rows = rect.height;
//					d3dContext.CopySubresourceRegion(GetTexture(), subresourceIndex, new SharpDX.Direct3D11.ResourceRegion(rect.Value.Left, rect.Value.Top, 0, rect.Value.Right, rect.Value.Bottom, 1), stagingTex, 0, 0, 0, 0);
				}
				else
				{
					elementsInRow = levelWidth;
					rows = height;
//					d3dContext.CopySubresourceRegion(GetTexture(), subresourceIndex, null, stagingTex, 0, 0, 0, 0);
				}

				// Copy the data to the array.
//				SharpDX.DataStream stream = null;
				try
				{
//					var databox = d3dContext.MapSubresource(stagingTex, 0, SharpDX.Direct3D11.MapMode.Read, SharpDX.Direct3D11.MapFlags.None, out stream);

					int elementSize = _format.getSize();
					int rowSize = elementSize * elementsInRow;
//					if (rowSize == databox.RowPitch)
//						stream.ReadRange(data, startIndex, elementCount);
//					else
					{
						// Some drivers may add pitch to rows.
						// We need to copy each row separatly and skip trailing zeros.
//						stream.Seek(startIndex, SeekOrigin.Begin);

//						int elementSizeInByte = Marshal.SizeOf(typeof(T));
//						for (int row = 0; row < rows; ++row)
//						{
//							int i;
//							for (i = row * rowSize / elementSizeInByte; i < (row + 1) * rowSize / elementSizeInByte; i++)
//								data[i] = stream.Read<T>();

//							if (i >= elementCount)
//								break;

//							stream.Seek(databox.RowPitch - rowSize, SeekOrigin.Current);
//						}
					}
				}
				finally
				{
//					SharpDX.Utilities.Dispose( ref stream);
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

	// TODO: See if I can use my Stream class here
	private void platformSaveAsJpeg(OutputStream stream, int width, int height)
	{
//#if WINDOWS_STOREAPP || WINDOWS_UAP
//		saveAsImage(BitmapEncoder.JpegEncoderId, stream, width, height);
//#endif
//#if WINDOWS_PHONE

		// TODO: Do I want to keep that like so or create GraphicsExtensions.getSize
//		byte[] pixelData = new byte[width * height * GraphicsExtensions.getSize(Format)];
		byte[] pixelData = new byte[width * height * _format.getSize()];
//		getData(pixelData);

		//We Must convert from BGRA to RGBA
		convertToRGBA(height, width, pixelData);

//		var waitEvent = new ManualResetEventSlim(false);
//		Deployment.Current.Dispatcher.BeginInvoke(() =>
//		{
//			var bitmap = new WriteableBitmap(Width, Height);
//			System.Buffer.BlockCopy(pixelData, 0, bitmap.Pixels, 0, pixelData.Length);
//			bitmap.SaveJpeg(stream, width, height, 0, 100);
//			waitEvent.Set();
//		});

//		waitEvent.Wait();
//#endif
//#if !WINDOWS_STOREAPP && !WINDOWS_PHONE && !WINDOWS_UAP
		throw new UnsupportedOperationException();
//#endif
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

	// TODO: See if I can use my Stream class here
	private void platformSaveAsPng(OutputStream stream, int width, int height)
	{
//		PngWriter pngWriter = new PngWriter();
//		pngWriter.write(this, stream);
		throw new UnsupportedOperationException();
	}

//#if WINDOWS_STOREAPP || WINDOWS_UAP
//	private void saveAsImage(Guid encoderId, Stream stream, int width, int height)
//	{
		// TODO: Do I want to keep that like so or create GraphicsExtensions.getSize
//		byte[] pixelData = new byte[Width * Height * GraphicsExtensions.GetSize(Format)];
//		byte[] pixelData = new byte[width * height * _format.getSize()];
//		getData(pixelData);

		// TODO: We need to convert from Format to R8G8B8A8!

		// TODO: We should implement async SaveAsPng() for WinRT.
//		Task.Run(async () =>
//		{
			// Create a temporary memory stream for writing the png.
//			var memstream = new InMemoryRandomAccessStream();

			// Write the png.
//			var encoder = await BitmapEncoder.CreateAsync(encoderId, memstream);
//			encoder.SetPixelData(BitmapPixelFormat.Rgba8, BitmapAlphaMode.Ignore, (uint)width, (uint)height, 96, 96, pixelData);
//			await encoder.FlushAsync();

			// Copy the memory stream into the real output stream.
//			memstream.Seek(0);
//			memstream.AsStreamForRead().CopyTo(stream);

//		}).Wait();
//	}
//#endif

	// TODO: More research on this
	// internal override SharpDX.Direct3D11.Resource CreateTexture()
	@Override
	protected int[] createTexture()
	{
		// TODO: Move this to SetData() if we want to make Immutable textures!
//		var desc = new SharpDX.Direct3D11.Texture2DDescription();
//		desc.Width = width;
//		desc.Height = height;
//		desc.MipLevels = _levelCount;
//		desc.ArraySize = ArraySize;
//		desc.Format = SharpDXHelper.ToFormat(_format);
//		desc.BindFlags = SharpDX.Direct3D11.BindFlags.ShaderResource;
//		desc.CpuAccessFlags = SharpDX.Direct3D11.CpuAccessFlags.None;
//		desc.SampleDescription.Count = 1;
//		desc.SampleDescription.Quality = 0;
//		desc.Usage = SharpDX.Direct3D11.ResourceUsage.Default;
//		desc.OptionFlags = SharpDX.Direct3D11.ResourceOptionFlags.None;

		if (_renderTarget)
		{
//			desc.BindFlags |= SharpDX.Direct3D11.BindFlags.RenderTarget;
			if (_mipmap)
			{
				// Note: XNA 4 does not have a method Texture.GenerateMipMaps() 
				// because generation of mipmaps is not supported on the Xbox 360.
				// TODO: New method Texture.GenerateMipMaps() required.
//				desc.OptionFlags |= SharpDX.Direct3D11.ResourceOptionFlags.GenerateMipMaps;
			}
		}
//		if (_shared)
//			desc.OptionFlags |= SharpDX.Direct3D11.ResourceOptionFlags.Shared;

//		return new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc);
		return new int[width * height];
	}

	// TODO: from Texture2D.OpenGL.cs
	// This method allows games that use Texture2D.fromStream
	// to reload their textures after the GL context is lost.
	private void platformReload(Stream textureStream)
	{
		// GenerateGLTextureIfRequired();
		// FillTextureFromStream(textureStream);
	}

	// NOTE: I added this method
	// TODO: Do something like content.load(T[] data, Class.class)?
	// TODO: Should I create all primitives type or send the primitives through
	// this method as their Wrapper Class counterpart ?
	// This means creating a bunch of utilities method to do the conversion
	// NOTE(Eric): The data is expected to be in the RGBA format
	private void platformSetData(int level, int arraySlice, Rectangle rect, byte[] data, int startIndex, int elementCount)
	{
		int elementSize = 4; //_format.getSize();
		this._texture = new int[elementCount / elementSize];
		for (int i = 0, j = 0; i < elementCount; i += 4, ++j)
		{
			this._texture[j] = (data[i + 3] & 0xFF) << 24 |	// A component
							   (data[i + 2] & 0xFF) << 16 |	// B component
							   (data[i + 1] & 0xFF) <<  8 |	// G component
							   (data[i + 0] & 0xFF);		// R component
		}
	}

}
