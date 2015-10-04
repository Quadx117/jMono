package gameCore.graphics;

import gameCore.dotNet.BinaryReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// TODO: If the actual value is important and not just the byte
//		 representation, then we should change the unsigned types
//		 to the type of higher rank.
public class DxtUtil
{
	public static byte[] decompressDxt1(byte[] imageData, int width, int height)
	{
		try (ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData))
		{
			return decompressDxt1(imageStream, width, height);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// TODO: Added this return value in case the try fails.
		//		 Should maybe find another way to handle this.
		return null;
	}

	protected static byte[] decompressDxt1(InputStream imageStream, int width, int height)
	{
		byte[] imageData = new byte[width * height * 4];

		try (BinaryReader imageReader = new BinaryReader(imageStream))
		{
			int blockCountX = (width + 3) / 4;
			int blockCountY = (height + 3) / 4;

			for (int y = 0; y < blockCountY; y++)
			{
				for (int x = 0; x < blockCountX; x++)
				{
					decompressDxt1Block(imageReader, x, y, blockCountX, width, height, imageData);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return imageData;
	}

	private static void decompressDxt1Block(BinaryReader imageReader, int x, int y, int blockCountX, int width,
			int height, byte[] imageData)
	{
		short c0 = (short) imageReader.readUInt16();
		short c1 = (short) imageReader.readUInt16();

		byte r0 = 0, g0 = 0, b0 = 0;
		byte r1 = 0, g1 = 0, b1 = 0;
		convertRgb565ToRgb888(c0, r0, g0, b0);
		convertRgb565ToRgb888(c1, r1, g1, b1);

		int lookupTable = (int) imageReader.readUInt32();

		for (int blockY = 0; blockY < 4; ++blockY)
		{
			for (int blockX = 0; blockX < 4; ++blockX)
			{
				byte r = 0, g = 0, b = 0, a = (byte) 255;
				int index = (lookupTable >> 2 * (4 * blockY + blockX)) & 0x03;

				if (c0 > c1)
				{
					switch (index)
					{
						case 0:
							r = r0;
							g = g0;
							b = b0;
							break;
						case 1:
							r = r1;
							g = g1;
							b = b1;
							break;
						case 2:
							r = (byte) ((2 * r0 + r1) / 3);
							g = (byte) ((2 * g0 + g1) / 3);
							b = (byte) ((2 * b0 + b1) / 3);
							break;
						case 3:
							r = (byte) ((r0 + 2 * r1) / 3);
							g = (byte) ((g0 + 2 * g1) / 3);
							b = (byte) ((b0 + 2 * b1) / 3);
							break;
					}
				}
				else
				{
					switch (index)
					{
						case 0:
							r = r0;
							g = g0;
							b = b0;
							break;
						case 1:
							r = r1;
							g = g1;
							b = b1;
							break;
						case 2:
							r = (byte) ((r0 + r1) / 2);
							g = (byte) ((g0 + g1) / 2);
							b = (byte) ((b0 + b1) / 2);
							break;
						case 3:
							r = 0;
							g = 0;
							b = 0;
							a = 0;
							break;
					}
				}

				int px = (x << 2) + blockX;
				int py = (y << 2) + blockY;
				if ((px < width) && (py < height))
				{
					int offset = ((py * width) + px) << 2;
					imageData[offset] = r;
					imageData[offset + 1] = g;
					imageData[offset + 2] = b;
					imageData[offset + 3] = a;
				}
			}
		}
	}

	public static byte[] decompressDxt3(byte[] imageData, int width, int height)
    {
        try (ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData)){
        	return DecompressDxt3(imageStream, width, height);
        }
		catch (IOException e)
		{
			e.printStackTrace();
		}
        // TODO: Added this return value in case the try fails.
     	//		 Should maybe find another way to handle this.
     	return null;
    }

	protected static byte[] DecompressDxt3(InputStream imageStream, int width, int height)
	{
		byte[] imageData = new byte[width * height * 4];

		try (BinaryReader imageReader = new BinaryReader(imageStream))
		{
			int blockCountX = (width + 3) / 4;
			int blockCountY = (height + 3) / 4;

			for (int y = 0; y < blockCountY; y++)
			{
				for (int x = 0; x < blockCountX; x++)
				{
					decompressDxt3Block(imageReader, x, y, blockCountX, width, height, imageData);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return imageData;
	}

	private static void decompressDxt3Block(BinaryReader imageReader, int x, int y, int blockCountX, int width, int height, byte[] imageData)
    {
        byte a0 = imageReader.readByte();
		byte a1 = imageReader.readByte();
		byte a2 = imageReader.readByte();
		byte a3 = imageReader.readByte();
		byte a4 = imageReader.readByte();
		byte a5 = imageReader.readByte();
		byte a6 = imageReader.readByte();
		byte a7 = imageReader.readByte();
        
        short c0 = (short) imageReader.readUInt16();
        short c1 = (short) imageReader.readUInt16();

		byte r0 = 0, g0 = 0, b0 = 0;
		byte r1 = 0, g1 = 0, b1 = 0;
		convertRgb565ToRgb888(c0, r0, g0, b0);
		convertRgb565ToRgb888(c1, r1, g1, b1);

        int lookupTable = (int) imageReader.readUInt32();

		int alphaIndex = 0;
        for (int blockY = 0; blockY < 4; blockY++)
        {
            for (int blockX = 0; blockX < 4; blockX++)
            {
				byte r = 0, g = 0, b = 0, a = 0;

                int index = (lookupTable >> 2 * (4 * blockY + blockX)) & 0x03;
				
				switch (alphaIndex)
				{
					case 0:
						a = (byte)((a0 & 0x0F) | ((a0 & 0x0F) << 4));
						break;
					case 1:
						a = (byte)((a0 & 0xF0) | ((a0 & 0xF0) >> 4));
						break;
					case 2:
						a = (byte)((a1 & 0x0F) | ((a1 & 0x0F) << 4));
						break;
					case 3:
						a = (byte)((a1 & 0xF0) | ((a1 & 0xF0) >> 4));
						break;
					case 4:
						a = (byte)((a2 & 0x0F) | ((a2 & 0x0F) << 4));
						break;
					case 5:
						a = (byte)((a2 & 0xF0) | ((a2 & 0xF0) >> 4));
						break;
					case 6:
						a = (byte)((a3 & 0x0F) | ((a3 & 0x0F) << 4));
						break;
					case 7:
						a = (byte)((a3 & 0xF0) | ((a3 & 0xF0) >> 4));
						break;
					case 8:
						a = (byte)((a4 & 0x0F) | ((a4 & 0x0F) << 4));
						break;
					case 9:
						a = (byte)((a4 & 0xF0) | ((a4 & 0xF0) >> 4));
						break;
					case 10:
						a = (byte)((a5 & 0x0F) | ((a5 & 0x0F) << 4));
						break;
					case 11:
						a = (byte)((a5 & 0xF0) | ((a5 & 0xF0) >> 4));
						break;
					case 12:
						a = (byte)((a6 & 0x0F) | ((a6 & 0x0F) << 4));
						break;
					case 13:
						a = (byte)((a6 & 0xF0) | ((a6 & 0xF0) >> 4));
						break;
					case 14:
						a = (byte)((a7 & 0x0F) | ((a7 & 0x0F) << 4));
						break;
					case 15:
						a = (byte)((a7 & 0xF0) | ((a7 & 0xF0) >> 4));
						break;
				}
				++alphaIndex;

                switch (index)
                {
					case 0:
						r = r0;
						g = g0;
						b = b0;
						break;
					case 1:
						r = r1;
						g = g1;
						b = b1;
						break;
					case 2:
						r = (byte)((2 * r0 + r1) / 3);
						g = (byte)((2 * g0 + g1) / 3);
						b = (byte)((2 * b0 + b1) / 3);
						break;
					case 3:
						r = (byte)((r0 + 2 * r1) / 3);
						g = (byte)((g0 + 2 * g1) / 3);
						b = (byte)((b0 + 2 * b1) / 3);
						break;
				}

				int px = (x << 2) + blockX;
				int py = (y << 2) + blockY;
				if ((px < width) && (py < height))
				{
					int offset = ((py * width) + px) << 2;
					imageData[offset] = r;
					imageData[offset + 1] = g;
					imageData[offset + 2] = b;
					imageData[offset + 3] = a;
				}
			}
        }
    }

	public static byte[] decompressDxt5(byte[] imageData, int width, int height)
    {
        try (ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData)){
        	return decompressDxt5(imageStream, width, height);
        }
		catch (IOException e)
		{
			e.printStackTrace();
		}
        // TODO: Added this return value in case the try fails.
     	//		 Should maybe find another way to handle this.
     	return null;
    }

	protected static byte[] decompressDxt5(InputStream imageStream, int width, int height)
	{
		byte[] imageData = new byte[width * height * 4];

		try (BinaryReader imageReader = new BinaryReader(imageStream))
		{
			int blockCountX = (width + 3) / 4;
			int blockCountY = (height + 3) / 4;

			for (int y = 0; y < blockCountY; y++)
			{
				for (int x = 0; x < blockCountX; x++)
				{
					decompressDxt5Block(imageReader, x, y, blockCountX, width, height, imageData);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return imageData;
	}

	private static void decompressDxt5Block(BinaryReader imageReader, int x, int y, int blockCountX, int width, int height, byte[] imageData)
    {
        byte alpha0 = imageReader.readByte();
        byte alpha1 = imageReader.readByte();

        long alphaMask = (long)imageReader.readByte();
        alphaMask += (long)imageReader.readByte() << 8;
        alphaMask += (long)imageReader.readByte() << 16;
        alphaMask += (long)imageReader.readByte() << 24;
        alphaMask += (long)imageReader.readByte() << 32;
        alphaMask += (long)imageReader.readByte() << 40;

        short c0 = (short) imageReader.readUInt16();
        short c1 = (short) imageReader.readUInt16();

		byte r0 = 0, g0 = 0, b0 = 0;
		byte r1 = 0, g1 = 0, b1 = 0;
		convertRgb565ToRgb888(c0, r0, g0, b0);
		convertRgb565ToRgb888(c1, r1, g1, b1);

        int lookupTable = (int) imageReader.readUInt32();

        for (int blockY = 0; blockY < 4; blockY++)
        {
            for (int blockX = 0; blockX < 4; blockX++)
            {
				byte r = 0, g = 0, b = 0, a = (byte) 255;
                int index = (lookupTable >> 2 * (4 * blockY + blockX)) & 0x03;
                
                int alphaIndex = (int)((alphaMask >> 3 * (4 * blockY + blockX)) & 0x07);
                if (alphaIndex == 0)
				{
                    a = alpha0;
                }
				else if (alphaIndex == 1)
				{
                    a = alpha1;
                }
				else if (alpha0 > alpha1)
				{
                    a = (byte)(((8 - alphaIndex) * alpha0 + (alphaIndex - 1) * alpha1) / 7);
                }
				else if (alphaIndex == 6)
				{
                    a = 0;
                }
				else if (alphaIndex == 7)
				{
                    a = (byte) 0xff;
                }
				else
				{
                    a = (byte)(((6 - alphaIndex) * alpha0 + (alphaIndex - 1) * alpha1) / 5);
                }

				switch (index)
				{
					case 0:
						r = r0;
						g = g0;
						b = b0;
						break;
					case 1:
						r = r1;
						g = g1;
						b = b1;
						break;
					case 2:
						r = (byte)((2 * r0 + r1) / 3);
						g = (byte)((2 * g0 + g1) / 3);
						b = (byte)((2 * b0 + b1) / 3);
						break;
					case 3:
						r = (byte)((r0 + 2 * r1) / 3);
						g = (byte)((g0 + 2 * g1) / 3);
						b = (byte)((b0 + 2 * b1) / 3);
						break;
				}

				int px = (x << 2) + blockX;
				int py = (y << 2) + blockY;
				if ((px < width) && (py < height))
				{
					int offset = ((py * width) + px) << 2;
					imageData[offset] = r;
					imageData[offset + 1] = g;
					imageData[offset + 2] = b;
					imageData[offset + 3] = a;
				}
			}
        }
    }

	// private static void ConvertRgb565ToRgb888(ushort color, out byte r, out byte g, out byte b)
	private static void convertRgb565ToRgb888(short color, Byte r, Byte g, Byte b)
	{
		int temp;

		temp = (color >> 11) * 255 + 16;
		r = (byte) ((temp / 32 + temp) / 32);
		temp = ((color & 0x07E0) >> 5) * 255 + 32;
		g = (byte) ((temp / 64 + temp) / 64);
		temp = (color & 0x001F) * 255 + 16;
		b = (byte) ((temp / 32 + temp) / 32);
	}
}
