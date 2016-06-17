package jMono_Framework.dotNet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class contains static method that simulate the result of the "as" keyword in .NET.
 * It also contains other helper methods to convert various array types to other types.
 * 
 * @author Eric
 *
 */
public class As
{
	/**
	 * Make sure we can't instantiate this class
	 */
	private As() {}
	
	public static <T> T as(Object obj, Class<T> type)
	{
		return type.isInstance(obj) ? type.cast(obj) : null;
	}

	public static byte[] floatArrayToByteArray(float[] values)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);
		// NOTE: Needed to match endianness in C# / MonoGame
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		for (float value : values)
		{
			buffer.putFloat(value);
		}

		return buffer.array();
	}
}
