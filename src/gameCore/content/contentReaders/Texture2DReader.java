package gameCore.content.contentReaders;

import gameCore.content.ContentReader;
import gameCore.content.ContentTypeReader;
import gameCore.dotNet.BitConverter;
import gameCore.graphics.DxtUtil;
import gameCore.graphics.SurfaceFormat;
import gameCore.graphics.Texture2D;
import gameCore.math.MathHelper;

// TODO: See if I need to create ContentTypeReader<T>
public class Texture2DReader extends ContentTypeReader<Texture2D>
{
	public Texture2DReader()
	{
		super(Texture2D.class);
		// Do nothing
	}

// #if ANDROID
	// static string[] supportedExtensions = new string[] { ".jpg", ".bmp", ".jpeg", ".png", ".gif" };
// #else
	static String[] supportedExtensions = new String[] { ".jpg", ".bmp", ".jpeg", ".png", ".gif", ".pict", ".tga" };
// #endif

	public static String normalize(String fileName)
	{
		return normalize(fileName, supportedExtensions);
	}

	@Override
	protected Texture2D read(ContentReader reader, Texture2D existingInstance)
	{
		Texture2D texture = null;

		SurfaceFormat surfaceFormat = SurfaceFormat.valueOf(reader.readInt32());
		int width = reader.readInt32();
		int height = reader.readInt32();
		int levelCount = reader.readInt32();
		int levelCountOutput = levelCount;

		// If the system does not fully support Power of Two textures,
		// skip any mip maps supplied with any non PoT textures.
		if (levelCount > 1 && !reader.getGraphicsDevice().getGraphicsCapabilities().supportsNonPowerOfTwo() &&
				(!MathHelper.isPowerOfTwo(width) || !MathHelper.isPowerOfTwo(height)))
		{
			levelCountOutput = 1;
			System.out.println("Device does not support non Power of Two textures. Skipping mipmaps.");
		}

		SurfaceFormat convertedFormat = surfaceFormat;
		switch (surfaceFormat)
		{
			case Dxt1:
			case Dxt1a:
				if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsDxt1())
					convertedFormat = SurfaceFormat.Color;
				break;
			case Dxt1SRgb:
				if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsDxt1())
					convertedFormat = SurfaceFormat.ColorSRgb;
				break;
			case Dxt3:
			case Dxt5:
				if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc())
					convertedFormat = SurfaceFormat.Color;
				break;
			case Dxt3SRgb:
			case Dxt5SRgb:
				if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc())
					convertedFormat = SurfaceFormat.ColorSRgb;
				break;
			case NormalizedByte4:
				convertedFormat = SurfaceFormat.Color;
				break;
			default:
				break;
		}

		texture = (existingInstance != null) ? existingInstance : new Texture2D(reader.getGraphicsDevice(), width,
				height, levelCountOutput > 1, convertedFormat);

		for (int level = 0; level < levelCount; level++)
		{
			int levelDataSizeInBytes = reader.readInt32();
			byte[] levelData = reader.readBytes(levelDataSizeInBytes);
			int levelWidth = width >> level;
			int levelHeight = height >> level;

			if (level >= levelCountOutput)
				continue;

			// Convert the image data if required
			switch (surfaceFormat)
			{
				case Dxt1:
				case Dxt1SRgb:
				case Dxt1a:
					if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsDxt1()
							&& convertedFormat == SurfaceFormat.Color)
						levelData = DxtUtil.decompressDxt1(levelData, levelWidth, levelHeight);
					break;
				case Dxt3:
				case Dxt3SRgb:
					if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc())
						if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc()
								&& convertedFormat == SurfaceFormat.Color)
							levelData = DxtUtil.decompressDxt3(levelData, levelWidth, levelHeight);
					break;
				case Dxt5:
				case Dxt5SRgb:
					if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc())
						if (!reader.getGraphicsDevice().getGraphicsCapabilities().supportsS3tc()
								&& convertedFormat == SurfaceFormat.Color)
							levelData = DxtUtil.decompressDxt5(levelData, levelWidth, levelHeight);
					break;
				case Bgra5551:
				{
// #if OPENGL
					// Shift the channels to suit OpenGL
					int offset = 0;
					for (int y = 0; y < levelHeight; y++)
					{
						for (int x = 0; x < levelWidth; x++)
						{
							short pixel = BitConverter.toUInt16(levelData, offset);
							pixel = (short) (((pixel & 0x7FFF) << 1) | ((pixel & 0x8000) >> 15));
							levelData[offset] = (byte) (pixel);
							levelData[offset + 1] = (byte) (pixel >> 8);
							offset += 2;
						}
					}
// #endif
				}
					break;
				case Bgra4444:
				{
// #if OPENGL
					// Shift the channels to suit OpenGL
					int offset = 0;
					for (int y = 0; y < levelHeight; y++)
					{
						for (int x = 0; x < levelWidth; x++)
						{
							short pixel = BitConverter.toUInt16(levelData, offset);
							pixel = (short) (((pixel & 0x0FFF) << 4) | ((pixel & 0xF000) >> 12));
							levelData[offset] = (byte) (pixel);
							levelData[offset + 1] = (byte) (pixel >> 8);
							offset += 2;
						}
					}
// #endif
				}
					break;
				case NormalizedByte4:
				{
					int bytesPerPixel = surfaceFormat.getSize();
					int pitch = levelWidth * bytesPerPixel;
					for (int y = 0; y < levelHeight; y++)
					{
						for (int x = 0; x < levelWidth; x++)
						{
							int color = BitConverter.toInt32(levelData, y * pitch + x * bytesPerPixel);
							levelData[y * pitch + x * 4] = (byte) (((color >> 16) & 0xff)); // R:=W
							levelData[y * pitch + x * 4 + 1] = (byte) (((color >> 8) & 0xff)); // G:=V
							levelData[y * pitch + x * 4 + 2] = (byte) (((color) & 0xff)); // B:=U
							levelData[y * pitch + x * 4 + 3] = (byte) (((color >> 24) & 0xff)); // A:=Q
						}
					}
				}
					break;
				default:
					break;
			}

			texture.setData(level, null, levelData, 0, levelData.length);
		}

		return texture;
	}
}
