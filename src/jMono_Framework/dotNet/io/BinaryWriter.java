package jMono_Framework.dotNet.io;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;

public class BinaryWriter implements Closeable
{
	public static final BinaryWriter Null = new BinaryWriter();
    
    protected Stream OutStream;
    private byte[] _buffer;    // temp space for writing primitives to.
    // TODO: Encoding (Charset, CharsetEncoder, CharsetDecoder or StandardCharsets ?)
//    private Encoding _encoding;
//    private Encoder _encoder;

    private boolean _leaveOpen;

    // This field should never have been serialized and has not been used since before v2.0.
    // However, this type is serializable, and we need to keep the field name around when deserializing.
    // Also, we'll make .NET FX 4.5 not break if it's missing.
// #pragma warning disable 169
    private char[] _tmpOneCharBuffer;
// #pragma warning restore 169

    // Perf optimization stuff
    private byte[] _largeByteBuffer;  // temp space for writing chars.
    private int _maxChars;   // max # of chars we can put in _largeByteBuffer
    // Size should be around the max number of chars/string * Encoding's max bytes/char
    private final int LargeByteBufferSize = 256;

    // Protected default constructor that sets the output stream
    // to a null stream (a bit bucket).
    protected BinaryWriter()
    {
        OutStream = Stream.Null;
        _buffer = new byte[16];
//        _encoding = new UTF8Encoding(false, true);
//        _encoder = _encoding.GetEncoder();
    }

    // TODO: Encoding (Charset, CharsetEncoder, CharsetDecoder or StandardCharsets ?)
    public BinaryWriter(Stream output)
    {
//    	this(output, new UTF8Encoding(false, true), false);
    	this(output, false);
    }

    // TODO: Encoding (Charset, CharsetEncoder, CharsetDecoder or StandardCharsets ?)
//    public BinaryWriter(Stream output, Encoding encoding)
//    {
//    	this(output, encoding, false);
//    }

 // TODO: Encoding (Charset, CharsetEncoder, CharsetDecoder or StandardCharsets ?)
//    public BinaryWriter(Stream output, Encoding encoding, boolean leaveOpen)
    public BinaryWriter(Stream output, boolean leaveOpen)
    {
        if (output==null)
            throw new NullPointerException("output");
//        if (encoding==null)
//            throw new NullPointerException("encoding");
        if (!output.canWrite())
            throw new IllegalArgumentException("Argument_StreamNotWritable");
//        Contract.EndContractBlock();

        OutStream = output;
        _buffer = new byte[16];
//        _encoding = encoding;
//        _encoder = _encoding.GetEncoder();
        _leaveOpen = leaveOpen;
    }

    // Closes this writer and releases any system resources associated with the
    // writer. Following a call to Close, any operations on the writer
    // may raise exceptions.
    @Override
	public void close()
	{
    	dispose(true);
	}

    protected void dispose(boolean disposing)
    {
        if (disposing) {
            if (_leaveOpen)
				try
				{
					OutStream.flush();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
                OutStream.close();
        }
    }

    public void dispose()
    {
        dispose(true);
    }

    /*
     * Returns the stream associate with the writer. It flushes all pending
     * writes before returning. All subclasses should override Flush to
     * ensure that all buffered data is sent to the stream.
     */
    public Stream getBaseStream()
    {
            flush();
            return OutStream;
    }

    // Clears all buffers for this writer and causes any buffered data to be
    // written to the underlying device. 
    public void flush() 
    {
        try
		{
			OutStream.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public long seek(int offset, SeekOrigin origin)
    {
        return OutStream.seek(offset, origin);
    }
    
    // Writes a boolean to this stream. A single byte is written to the stream
    // with the value 0 representing false or the value 1 representing true.
    // 
    public void write(boolean value) {
        _buffer[0] = (byte) (value ? 1 : 0);
        try
		{
			OutStream.write(_buffer, 0, 1);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Writes a byte to this stream. The current position of the stream is
    // advanced by one.
    // 
    public void write(byte value) 
    {
        OutStream.writeByte(value);
    }
    
    // Writes a signed byte to this stream. The current position of the stream 
    // is advanced by one.
    // 
//    public void write(sbyte value) 
//    {
//        OutStream.writeByte((byte) value);
//    }

    // Writes a byte array to this stream.
    // 
    // This default implementation calls the Write(Object, int, int)
    // method to write the byte array.
    // 
    public void write(byte[] buffer) {
        if (buffer == null)
            throw new NullPointerException("buffer");
//        Contract.EndContractBlock();
        try
		{
			OutStream.write(buffer, 0, buffer.length);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Writes a section of a byte array to this stream.
    //
    // This default implementation calls the Write(Object, int, int)
    // method to write the byte array.
    // 
    public void write(byte[] buffer, int index, int count) {
        try
		{
			OutStream.write(buffer, index, count);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    // Writes a character to this stream. The current position of the stream is
    // advanced by two.
    // Note this method cannot handle surrogates properly in UTF-8.
    // 
    // TODO: Need to test this. 
    public void write(char ch) {
//        if (Char.IsSurrogate(ch))
//            throw new ArgumentException(Environment.GetResourceString("Arg_SurrogatesNotAllowedAsSingleChar"));
//        Contract.EndContractBlock();

//        assert(_encoding.GetMaxByteCount(1) <= 16) : "_encoding.GetMaxByteCount(1) <= 16)";
        int numBytes = 0;
//        fixed(byte * pBytes = _buffer) {
//            numBytes = _encoder.GetBytes(&ch, 1, pBytes, 16, true);
        byte[] tmp = String.valueOf(ch).getBytes();
        numBytes = tmp.length;
//        }
        try
		{
			OutStream.write(tmp, 0, numBytes);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Writes a character array to this stream.
    // 
    // This default implementation calls the Write(Object, int, int)
    // method to write the character array.
    // 
    public void write(char[] chars) 
    {
        if (chars == null)
            throw new NullPointerException("chars");
//        Contract.EndContractBlock();

//        byte[] bytes = _encoding.GetBytes(chars, 0, chars.length);
        byte[] bytes = String.valueOf(chars).getBytes();
        try
		{
			OutStream.write(bytes, 0, bytes.length);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Writes a section of a character array to this stream.
    //
    // This default implementation calls the Write(Object, int, int)
    // method to write the character array.
    // 
    public void write(char[] chars, int index, int count) 
    {
//        byte[] bytes = _encoding.GetBytes(chars, index, count);
    	byte[] bytes = String.valueOf(chars).getBytes();
        try
		{
			OutStream.write(bytes, index, count);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    // Writes a double to this stream. The current position of the stream is
    // advanced by eight.
    public void write(double value)
    {
    	long tmpValue = Double.doubleToLongBits(value);
        _buffer[0] = (byte) tmpValue;
        _buffer[1] = (byte) (tmpValue >> 8);
        _buffer[2] = (byte) (tmpValue >> 16);
        _buffer[3] = (byte) (tmpValue >> 24);
        _buffer[4] = (byte) (tmpValue >> 32);
        _buffer[5] = (byte) (tmpValue >> 40);
        _buffer[6] = (byte) (tmpValue >> 48);
        _buffer[7] = (byte) (tmpValue >> 56);
        try
		{
			OutStream.write(_buffer, 0, 8);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void write(BigDecimal value)
    {
//    	BigDecimal.GetBytes(value,_buffer);
//    	byte[] tmp = value.unscaledValue().toByteArray();
//        try
//		{
//			OutStream.write(tmp, 0, 16);
//		}
//		catch (IOException e)
//		{
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	write(value.unscaledValue().longValueExact());
    }

    // Writes a two-byte signed integer to this stream. The current position of
    // the stream is advanced by two.
    // 
    public void write(short value)
    {
        _buffer[0] = (byte) value;
        _buffer[1] = (byte) (value >> 8);
        try
		{
			OutStream.write(_buffer, 0, 2);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Writes a two-byte unsigned integer to this stream. The current position
    // of the stream is advanced by two.
    // 
//    public void write(ushort value)
//    {
//        _buffer[0] = (byte) value;
//        _buffer[1] = (byte) (value >> 8);
//        OutStream.write(_buffer, 0, 2);
//    }

    // Writes a four-byte signed integer to this stream. The current position
    // of the stream is advanced by four.
    // 
    public void write(int value)
    {
        _buffer[0] = (byte) value;
        _buffer[1] = (byte) (value >> 8);
        _buffer[2] = (byte) (value >> 16);
        _buffer[3] = (byte) (value >> 24);
        try
		{
			OutStream.write(_buffer, 0, 4);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Writes a four-byte unsigned integer to this stream. The current position
    // of the stream is advanced by four.
    // 
//    public void write(uint value)
//    {
//        _buffer[0] = (byte) value;
//        _buffer[1] = (byte) (value >> 8);
//        _buffer[2] = (byte) (value >> 16);
//        _buffer[3] = (byte) (value >> 24);
//        OutStream.Write(_buffer, 0, 4);
//    }

    // Writes an eight-byte signed integer to this stream. The current position
    // of the stream is advanced by eight.
    // 
    public void write(long value)
    {
        _buffer[0] = (byte) value;
        _buffer[1] = (byte) (value >> 8);
        _buffer[2] = (byte) (value >> 16);
        _buffer[3] = (byte) (value >> 24);
        _buffer[4] = (byte) (value >> 32);
        _buffer[5] = (byte) (value >> 40);
        _buffer[6] = (byte) (value >> 48);
        _buffer[7] = (byte) (value >> 56);
        try
		{
			OutStream.write(_buffer, 0, 8);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Writes an eight-byte unsigned integer to this stream. The current 
    // position of the stream is advanced by eight.
    // 
//    public void Write(ulong value)
//    {
//        _buffer[0] = (byte) value;
//        _buffer[1] = (byte) (value >> 8);
//        _buffer[2] = (byte) (value >> 16);
//        _buffer[3] = (byte) (value >> 24);
//        _buffer[4] = (byte) (value >> 32);
//        _buffer[5] = (byte) (value >> 40);
//        _buffer[6] = (byte) (value >> 48);
//        _buffer[7] = (byte) (value >> 56);
//        OutStream.Write(_buffer, 0, 8);
//    }

    // Writes a float to this stream. The current position of the stream is
    // advanced by four.
    public void write(float value)
    {
//        uint TmpValue = *(uint *)&value;
    	int tmpValue = Float.floatToIntBits(value);
        _buffer[0] = (byte) tmpValue;
        _buffer[1] = (byte) (tmpValue >> 8);
        _buffer[2] = (byte) (tmpValue >> 16);
        _buffer[3] = (byte) (tmpValue >> 24);
        try
		{
			OutStream.write(_buffer, 0, 4);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    // Writes a length-prefixed string to this stream in the BinaryWriter's
    // current Encoding. This method first writes the length of the string as 
    // a four-byte unsigned integer, and then writes that many characters 
    // to the stream.
    // 
    public void write(String value) 
    {
    	if (value==null)
    		throw new NullPointerException("value");
//		Contract.EndContractBlock();

//		int len = _encoding.GetByteCount(value);
    	byte[] bytes = value.getBytes();
    	int len = bytes.length;
    	write7BitEncodedInt(len);

//    	if (_largeByteBuffer == null)
//    	{
//			_largeByteBuffer = new byte[LargeByteBufferSize];
//			_maxChars = LargeByteBufferSize / _encoding.GetMaxByteCount(1);
//		}

//		if (len <= LargeByteBufferSize)
//		{
    		//Contract.Assert(len == _encoding.GetBytes(chars, 0, chars.Length, _largeByteBuffer, 0), "encoding's GetByteCount & GetBytes gave different answers!  encoding type: "+_encoding.GetType().Name);
//			 _encoding.GetBytes(value, 0, value.length(), _largeByteBuffer, 0);
//			OutStream.write(_largeByteBuffer, 0, len);
//		}
//		else
//		{
    		// Aggressively try to not allocate memory in this loop for
    		// runtime performance reasons.  Use an Encoder to write out 
    		// the string correctly (handling surrogates crossing buffer
    		// boundaries properly).  
//			int charStart = 0;
//			int numLeft = value.length();
// #if _DEBUG
//			int totalBytes = 0;
// #endif
//			while (numLeft > 0)
//			{
    			// Figure out how many chars to process this round.
//				int charCount = (numLeft > _maxChars) ? _maxChars : numLeft;
//				int byteLen;
//				fixed(char* pChars = value)
//				{
//					fixed(byte* pBytes = _largeByteBuffer)
//					{
//						byteLen = _encoder.GetBytes(pChars + charStart, charCount, pBytes, LargeByteBufferSize, charCount == numLeft);
//					}
//				}
// #if _DEBUG
//				totalBytes += byteLen;
//				assert (totalBytes <= len && byteLen <= LargeByteBufferSize) : "BinaryWriter::Write(String) - More bytes encoded than expected!";
// #endif
//    			OutStream.write(_largeByteBuffer, 0, byteLen);
//    			charStart += charCount;
//    			numLeft -= charCount;
//			}
// #if _DEBUG
//    		assert(totalBytes == len) : "BinaryWriter::Write(String) - Didn't write out all the bytes!";
// #endif
//		}
    	write(bytes);
    }
    
    // TODO: Need to test this.
    protected void write7BitEncodedInt(int value)
    {
        // Write out an int 7 bits at a time.  The high bit of the byte,
        // when on, tells reader to continue reading more bytes.
//        uint v = (uint) value;   // support negative numbers
    	int v = value;
        while (v >= 0x80)
        {
            write((byte) (v | 0x80));
            v >>= 7;
        }
        write((byte)v);
    }
}
