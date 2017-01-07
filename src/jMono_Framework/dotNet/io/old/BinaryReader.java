package jMono_Framework.dotNet.io.old;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A java class that is capable of parsing .NET BinaryWriter encoded files.
 * Some methods are not implemented as I have no need for them.
 * 
 * @see https://github.com/dotnet/coreclr/blob/master/src/mscorlib/src/System/IO/BinaryReader.cs
 * @author Eric Perron
 *
 */
public class BinaryReader extends FilterInputStream
{

	public BinaryReader(InputStream in)
	{
		super(in);
	}

	/**
	 * Reads a 4-byte signed integer from the current stream and advances the current position of
	 * the stream by four bytes.
	 * 
	 * @return
	 */
	public int readInt32()
	{
		return ByteBuffer.wrap(this.readBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	/**
	 * Reads a 4-byte unsigned integer from the current stream and advances the position of the
	 * stream by four bytes. Returns a long as java does not have the means to have unsigned values.
	 * 
	 * @return
	 */
	public long readUInt32()
	{
		return this.readInt32() & 0xFFFFFFFFL;
	}

	/**
	 * Reads a 2-byte signed integer from the current stream and advances the current position of
	 * the stream by two bytes.
	 * 
	 * @return
	 */
	public short readInt16()
	{
		return ByteBuffer.wrap(this.readBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	/**
	 * Reads a 2-byte unsigned integer from the current stream using little-endian encoding and
	 * advances the position of the stream by two bytes. Returns a 32-bit int as there are not
	 * 16-bit ints in java.
	 * 
	 * @return
	 */
	public int readUInt16()
	{
		return this.readInt16() & 0xFFFF;
	}

	/**
	 * Reads a string from the current stream. The string is prefixed with the length, encoded as an
	 * integer seven bits at a time.
	 * 
	 * @return
	 */
	public String readString()
	{
		byte[] result = null;
		try
		{
			result = this.readBytes(this.getStringLength());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return new String(result);
	}

	/**
	 * Reads a Boolean value from the current stream and advances the current position of the stream
	 * by one byte.
	 * 
	 * @return
	 */
	public boolean readBoolean()
	{
		return this.readBytes(1)[0] != 0;
	}

	/**
	 * Reads a 4-byte floating point value from the current stream and advances the current position
	 * of the stream by four bytes.
	 * 
	 * @return
	 */
	public float readSingle()
	{
		return ByteBuffer.wrap(this.readBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	}

	/**
	 * Source: https://github.com/vrecan/Thaw-Giant/blob/master/src/main/java/com/vreco/thawgiant/
	 * BinaryUtil.java
	 * Binary files are encoded with a variable length prefix that tells you
	 * the size of the string. The prefix is encoded in a 7bit format where the
	 * 8th bit tells you if you should continue. If the 8th bit is set it means
	 * you need to read the next byte.
	 * 
	 * @param bytes
	 * @return
	 */
	private int getStringLength() throws IOException
	{
		int count = 0;
		int shift = 0;
		boolean more = true;
		while (more)
		{
			byte b = (byte) this.read();
			count |= (b & 0x7F) << shift;
			shift += 7;
			if ((b & 0x80) == 0)
			{
				more = false;
			}
		}
		return count;
	}

	public byte readByte()
	{
		Byte value = 0;
		value = readBytes(1)[0];
		return value;
	}

	/**
	 * Reads the specified number of bytes from the current stream into a byte array and advances
	 * the current position by that number of bytes.
	 * 
	 * @param length
	 * @return
	 */
	public byte[] readBytes(int length)
	{
		byte[] bytes = new byte[length];
		try
		{
			this.read(bytes);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 
	 * @return
	 */
	public char readChar()
	{
		int value = -1;
		try
		{
			value = read();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// TODO: Should I do something when I get to the end of the file ?
//		if (value == -1)
//		{
//			__Error.EndOfFile();
//		}
		return (char) value;
	}

	protected int read7BitEncodedInt()
	{
		// Read out an Int32 7 bits at a time. The high bit
		// of the byte when on means to continue reading more bytes.
		int count = 0;
		int shift = 0;
		byte b;
		do
		{
			// Check for a corrupted stream. Read a max of 5 bytes.
			// In a future version, add a DataFormatException.
			if (shift == 5 * 7)  // 5 bytes max per Int32, shift += 7
				throw new NumberFormatException("Format_Bad7BitInt32");

			// ReadByte handles end of stream cases for us.
			b = readByte();
			count |= (b & 0x7F) << shift;
			shift += 7;
		} while ((b & 0x80) != 0);
		return count;
	}
}
// TODO: Finish comments as per MSDN :
// https://msdn.microsoft.com/en-us/library/system.io.binaryreader%28v=vs.110%29.aspx
// https://github.com/dotnet/coreclr/blob/master/src/mscorlib/src/System/IO/BinaryReader.cs

// TODO: implement other methods and validate the existing one