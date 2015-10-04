package gameCore.dotNet;

import java.nio.ByteBuffer;

// TODO: Comments and missing methods
/**
 * Converts base data types to an array of bytes, and an array of bytes to base data types.
 * This class provides the same functionality as it's .NET analog
 * 
 * @see https://msdn.microsoft.com/en-us/library/system.bitconverter%28v=vs.110%29.aspx
 * @see https://raw.githubusercontent.com/peace-maker/lysis-java/master/src/lysis/BitConverter.java
 * @author Eric Perron
 *
 */
public class BitConverter
{
	public static short toInt16(byte[] bytes, int offset)
	{
		short result = (short) ((int) bytes[offset] & 0xff);
		result |= ((int) bytes[offset + 1] & 0xff) << 8;
		return (short) (result & 0xffff);
	}

	// TODO: If the actual value is important and not just the byte
	//       representation, then we should change the return type to int
	public static short toUInt16(byte[] bytes, int offset)
	{
		short result = (short) ((int) bytes[offset + 1] & 0xff);
		result |= ((int) bytes[offset] & 0xff) << 8;
		return (short) (result & 0xffff);
	}

	public static int toInt32(byte[] bytes, int offset)
	{
		int result = (int) bytes[offset] & 0xff;
		result |= ((int) bytes[offset + 1] & 0xff) << 8;
		result |= ((int) bytes[offset + 2] & 0xff) << 16;
		result |= ((int) bytes[offset + 3] & 0xff) << 24;
		return result;
	}

	public static long toUInt32(byte[] bytes, int offset)
	{
		long result = (int) bytes[offset] & 0xff;
		result |= ((int) bytes[offset + 1] & 0xff) << 8;
		result |= ((int) bytes[offset + 2] & 0xff) << 16;
		result |= ((int) bytes[offset + 3] & 0xff) << 24;
		return result & 0xFFFFFFFFL;
	}

	public static long toUInt64(byte[] bytes, int offset)
	{
		long result = 0;
		for (int i = 0; i <= 56; i += 8)
		{
			result |= ((int) bytes[offset++] & 0xff) << i;
		}
		return result;
	}

	public static byte[] getBytes(int value)
	{
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (value >> 24);
		bytes[1] = (byte) (value >> 16);
		bytes[2] = (byte) (value >> 8);
		bytes[3] = (byte) (value);
		return bytes;
	}

	public static byte[] getBytes(long value)
	{
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (value >> 24);
		bytes[1] = (byte) (value >> 16);
		bytes[2] = (byte) (value >> 8);
		bytes[3] = (byte) (value);
		return bytes;
	}

	public static float toSingle(byte[] b, long offset)
	{
		ByteBuffer buf = ByteBuffer.wrap(b, (int) offset, 4);
		float outp = buf.getFloat();
		return outp;
	}
}
