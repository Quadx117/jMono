package jMono_Framework.graphics;

import jMono_Framework.Rectangle;

public class TextureCube extends Texture
{
	protected int size;

	/**
	 * Returns the width and height of the cube map face in pixels.
	 * 
	 * @return The width and height of a cube map face in pixels.
	 */
	public int getSize()
	{
		return size;
	}

	public TextureCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format)
	{
		this(graphicsDevice, size, mipMap, format, false);
	}

	protected TextureCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format, boolean renderTarget)
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("graphicsDevice");
		}
		this.setGraphicsDevice(graphicsDevice);
		this.size = size;
		this._format = format;
		this._levelCount = mipMap ? calculateMipLevels(size) : 1;

		platformConstruct(graphicsDevice, size, mipMap, format, renderTarget);
	}

	/**
	 * Returns a copy of cube texture data specifying a cube map face.
	 * 
	 * @param cubeMapFace
	 *        The cube map face.
	 * @param data
	 *        The data.
	 */
	public <T> void getData(CubeMapFace cubeMapFace, T[] data)
	{
		platformGetData(cubeMapFace, data);
	}

	public <T> void setData(CubeMapFace face, T[] data)
	{
		setData(face, 0, null, data, 0, data.length);
	}

	public <T> void setData(CubeMapFace face, T[] data, int startIndex, int elementCount)
	{
		setData(face, 0, null, data, startIndex, elementCount);
	}

	public <T> void setData(CubeMapFace face, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data");

		// byte elementSizeInByte = Marshal.SizeOf(typeof(T));
		// var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
		// Use try..finally to make sure dataHandle is freed in case of an error
		try
		{
			// IntPtr dataPtr = (IntPtr) (dataHandle.AddrOfPinnedObject().ToInt64() + startIndex * elementSizeInByte);

			int xOffset, yOffset, width, height;
			if (rect != null)
			{
				xOffset = rect.x;
				yOffset = rect.y;
				width = rect.width;
				height = rect.height;
			}
			else
			{
				xOffset = 0;
				yOffset = 0;
				width = Math.max(1, this.size >> level);
				height = Math.max(1, this.size >> level);

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
// #if DIRECTX
					// width = (width + 3) & ~3;
					// height = (height + 3) & ~3;
// #else
					if (width > 4)
						width = (width + 3) & ~3;
					if (height > 4)
						height = (height + 3) & ~3;
// #endif
				}
			}
//			platformSetData(face, level, dataPtr, xOffset, yOffset, width, height);
			platformSetData(face, level, xOffset, yOffset, width, height);
		}
		finally
		{
			// dataHandle.Free();
		}
	}

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	private boolean _renderTarget;
	private boolean _mipMap;

	private void platformConstruct(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format, boolean renderTarget)
	{
		_renderTarget = renderTarget;
		_mipMap = mipMap;

		// Create texture
		getTexture();
	}

	// TODO: More research on this
	//internal override SharpDX.Direct3D11.Resource CreateTexture()
	@Override
	protected int[] createTexture()
	{
//		var description = new Texture2DDescription
//		{
//			Width = size,
//			Height = size,
//			MipLevels = _levelCount,
//			ArraySize = 6, // A texture cube is a 2D texture array with 6 textures.
//			Format = SharpDXHelper.ToFormat(_format),
//			BindFlags = BindFlags.ShaderResource,
//			CpuAccessFlags = CpuAccessFlags.None,
//			SampleDescription = { Count = 1, Quality = 0 },
//			Usage = ResourceUsage.Default,
//			OptionFlags = ResourceOptionFlags.TextureCube
//		};

		if (_renderTarget)
		{
//			description.BindFlags |= BindFlags.RenderTarget;
//			if (_mipMap)
//				description.OptionFlags |= ResourceOptionFlags.GenerateMipMaps;
		}

//		return new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, description);
		return new int[size]; // TODO: Need to validate that
	}

	private <T> void platformGetData(CubeMapFace cubeMapFace, T[] data)
	{
		// Create a temp staging resource for copying the data.
		// 
		// TODO: Like in Texture2D, we should probably be pooling these staging resources
		// and not creating a new one each time.
		//
//		var desc = new Texture2DDescription
//		{
//			Width = size,
//			Height = size,
//			MipLevels = 1,
//			ArraySize = 1,
//			Format = SharpDXHelper.ToFormat(_format),
//			SampleDescription = new SampleDescription(1, 0),
//			BindFlags = BindFlags.None,
//			CpuAccessFlags = CpuAccessFlags.Read,
//			Usage = ResourceUsage.Staging,
//			OptionFlags = ResourceOptionFlags.None,
//		};

//		var d3dContext = GraphicsDevice._d3dContext;
//		using (var stagingTex = new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc))
//		{
//			lock (d3dContext)
//			{
				// Copy the data from the GPU to the staging texture.
//				int subresourceIndex = CalculateSubresourceIndex(cubeMapFace, 0);
//				d3dContext.CopySubresourceRegion(GetTexture(), subresourceIndex, null, stagingTex, 0);

				// Copy the data to the array.
//				DataStream stream = null;
//				try
//				{
//					var databox = d3dContext.MapSubresource(stagingTex, 0, MapMode.Read, MapFlags.None, out stream);

//					const int startIndex = 0;
//					var elementCount = data.Length;
//					var elementSize = _format.GetSize();
//					var elementsInRow = size;
//					var rows = size;
//					var rowSize = elementSize * elementsInRow;
//					if (rowSize == databox.RowPitch)
//						stream.ReadRange(data, startIndex, elementCount);
//					else
//					{
						// Some drivers may add pitch to rows.
						// We need to copy each row separatly and skip trailing zeros.
//						stream.Seek(startIndex, SeekOrigin.Begin);

//						int elementSizeInByte = Marshal.SizeOf(typeof(T));
//						for (var row = 0; row < rows; row++)
//						{
//							int i;
//							for (i = row * rowSize / elementSizeInByte; i < (row + 1) * rowSize / elementSizeInByte; i++)
//								data[i] = stream.Read<T>();

//							if (i >= elementCount)
//								break;

//							stream.Seek(databox.RowPitch - rowSize, SeekOrigin.Current);
//						}
//					}
//				}
//				finally
//				{
//					SharpDX.Utilities.Dispose(ref stream);
//				}
//			}
//		}
	}

//	private <T> void platformSetData(CubeMapFace face, int level, IntPtr dataPtr, int xOffset, int yOffset, int width, int height)
	private <T> void platformSetData(CubeMapFace face, int level, int xOffset, int yOffset, int width, int height)
	{
//		var box = new DataBox(dataPtr, GetPitch(width), 0);

//		int subresourceIndex = CalculateSubresourceIndex(face, level);

//		var region = new ResourceRegion
//		{
//			Top = yOffset,
//			Front = 0,
//			Back = 1,
//			Bottom = yOffset + height,
//			Left = xOffset,
//			Right = xOffset + width
//		};

//		var d3dContext = GraphicsDevice._d3dContext;
//		lock (d3dContext)
//			d3dContext.UpdateSubresource(box, GetTexture(), subresourceIndex, region);
	}

}
