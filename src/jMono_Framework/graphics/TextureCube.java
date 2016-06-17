package jMono_Framework.graphics;

import jMono_Framework.Rectangle;

public class TextureCube extends Texture
{
	protected int size;

	/**
	 * Gets the width and height of the cube map face in pixels.
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

	protected TextureCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format,
			boolean renderTarget)
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("graphicsDevice");
		}

		this.setGraphicsDevice(graphicsDevice);
		this.size = size;
		this._format = format;
		this._levelCount = mipMap ? calculateMipLevels(size) : 1;

		// platformConstruct(graphicsDevice, size, mipMap, format, renderTarget);
	}

	/**
	 * Gets a copy of cube texture data specifying a cube map face.
	 * 
	 * @param cubeMapFace
	 *        The cube map face.
	 * @param data
	 *        The data.
	 */
	// where T : struct
	public <T> void getData(CubeMapFace cubeMapFace, T[] data)
	{
		// TODO: See other TextureCubeFiles
		// platformGetData(cubeMapFace, data);
	}

	// where T : struct
	public <T> void setData(CubeMapFace face, T[] data)
	{
		setData(face, 0, null, data, 0, data.length);
	}

	// where T : struct
	public <T> void setData(CubeMapFace face, T[] data, int startIndex, int elementCount)
	{
		setData(face, 0, null, data, startIndex, elementCount);
	}

	// where T : // struct
	public <T> void setData(CubeMapFace face, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
	{
		if (data == null)
			throw new NullPointerException("data");

		// byte elementSizeInByte = Marshal.SizeOf(typeof(T));
		// var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
		// Use try..finally to make sure dataHandle is freed in case of an error
		try
		{
			// IntPtr dataPtr = (IntPtr) (dataHandle.AddrOfPinnedObject().ToInt64() + startIndex *
			// elementSizeInByte);

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
				// Ref:
				// http://www.mentby.com/Group/mac-opengl/issue-with-dxt-mipmapped-textures.html
				if (_format == SurfaceFormat.Dxt1 ||		//
					_format == SurfaceFormat.Dxt1SRgb || 	//
					_format == SurfaceFormat.Dxt1a ||		//
					_format == SurfaceFormat.Dxt3 ||		//
					_format == SurfaceFormat.Dxt3SRgb ||	//
					_format == SurfaceFormat.Dxt5 ||		//
					_format == SurfaceFormat.Dxt5SRgb)
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
			// platformSetData(face, level, dataPtr, xOffset, yOffset, width, height);
		}
		finally
		{
			// dataHandle.Free();
		}
	}

}
