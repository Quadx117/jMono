package jMono_Framework.dotNet.io;

import jMono_Framework.dotNet.As;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryReader implements Closeable
{
	private final int MaxCharBytesSize = 128;

    private Stream   m_stream;
    private byte[]   m_buffer;
//    private Decoder  m_decoder;	// TODO: Is it needed ?
    private byte[]   m_charBytes;
    private char[]   m_singleChar;
    private char[]   m_charBuffer;
    private int      m_maxCharsSize;  // From MaxCharBytesSize & Encoding

    // Performance optimization for read() w/ Unicode.  Speeds us up by ~40% 
    private boolean     m_2BytesPerChar;
    private boolean     m_isMemoryStream; // "do we sit on MemoryStream?" for Read/ReadInt32 perf
    private boolean     m_leaveOpen;

    // TODO: Encoding (Charset, CharsetEncoder, CharsetDecoder or StandardCharsets ?)
    public BinaryReader(Stream input)  {
//    	this(input, new UTF8Encoding(), false);
    	this(input, false);
    }

//    public BinaryReader(Stream input, Encoding encoding)  {
//    	this(input, encoding, false);
//    }

//    public BinaryReader(Stream input, Encoding encoding, boolean leaveOpen) {
    public BinaryReader(Stream input, boolean leaveOpen) {
        if (input==null) {
            throw new NullPointerException("input");
        }
//        if (encoding==null) {
//            throw new NullPointerException("encoding");
//        }
        if (!input.canRead())
            throw new IllegalStateException("Argument_StreamNotReadable");
//        Contract.EndContractBlock();
        m_stream = input;
//        m_decoder = encoding.GetDecoder();
//        m_maxCharsSize = encoding.GetMaxCharCount(MaxCharBytesSize);
        int minBufferSize = 16; // encoding.GetMaxByteCount(1);  // max bytes per one char
//        if (minBufferSize < 16) 
//            minBufferSize = 16;
        m_buffer = new byte[minBufferSize];
        // m_charBuffer and m_charBytes will be left null.

        // For Encodings that always use 2 bytes per char (or more), 
        // special case them here to make read() & Peek() faster.
//        m_2BytesPerChar = encoding is UnicodeEncoding;
        // check if BinaryReader is based on MemoryStream, and keep this for it's life
        // we cannot use "as" operator, since derived classes are not allowed
//        m_isMemoryStream = (m_stream.GetType() == typeof(MemoryStream));
        m_isMemoryStream = (m_stream.getClass().equals(MemoryStream.class));
        m_leaveOpen = leaveOpen;

//        Contract.Assert(m_decoder!=null, "[BinaryReader.ctor]m_decoder!=null");
    }

    public Stream getBaseStream() {
            return m_stream;
        }

    @Override
	public void close()// throws IOException
	{
    	dispose(true);
	}
    
    protected void dispose(boolean disposing) {
        if (disposing) {
            Stream copyOfStream = m_stream;
            m_stream = null;
            if (copyOfStream != null && !m_leaveOpen)
                copyOfStream.close();
        }
        m_stream = null;
        m_buffer = null;
//        m_decoder = null;
        m_charBytes = null;
        m_singleChar = null;
        m_charBuffer = null;
    }

    public void dispose()
    {
        dispose(true);
    }

	public int peekChar()
	{
//		Contract.Ensures(Contract.Result<int>() >= -1);

		if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();

		if (!m_stream.canSeek())
			return -1;
		long origPos = m_stream.getPosition();
		int ch = read();
		m_stream.setPosition(origPos);
		return ch;
	}

	public int read()
	{
//		Contract.Ensures(Contract.Result<int>() >= -1);

		if (m_stream==null) {
			throw new RuntimeException("File not open");	// __Error.FileNotOpen();
		}
		return internalReadOneChar();
    }

    public boolean readBoolean()
    {
        fillBuffer(1);
        return (m_buffer[0]!=0);
    }

    public byte readByte()
    {
        // Inlined to avoid some method call overhead with FillBuffer.
        if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();

        int b = m_stream.readByte();
//        if (b == -1)
        	// TODO: need to check what to do here.
//        	throw new RuntimeException("end of file");	// __Error.EndOfFile();
        return (byte) b;
    }

//    [CLSCompliant(false)]
    public byte readSByte()
    {
        fillBuffer(1);
        return (byte)(m_buffer[0]);
    }

	public char readChar()
	{
		int value = read();
//		if (value==-1)
//		{
			// TODO: need to check what to do here.
//			throw new RuntimeException("end of file");	// __Error.EndOfFile();
//		}
		return (char)value;
	}

    public short readInt16()
    {
        fillBuffer(2);
        return (short)((m_buffer[0] & 0xff) | (m_buffer[1] << 8) & 0xff00);
    }

//    [CLSCompliant(false)]
    public int readUInt16()
    {
        fillBuffer(2);
        return ((int)((m_buffer[0] & 0xff) | (m_buffer[1] << 8) & 0xff00)) & 0xFFFF;
    }

    public int readInt32()
    {
        if (m_isMemoryStream) {
            if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();
            // read directly from MemoryStream buffer
            MemoryStream mStream = As.as(m_stream, MemoryStream.class);
            assert(mStream != null) : "m_stream as MemoryStream != null";

            return mStream.internalReadInt32();
        }
        else
        {
            fillBuffer(4);
            return (int)((m_buffer[0] & 0xff) | ((m_buffer[1] << 8) & 0xff00) | ((m_buffer[2] << 16) & 0xff0000) | ((m_buffer[3] << 24) & 0xff000000));
        }
    }

//    [CLSCompliant(false)]
    public long readUInt32()
    {
        fillBuffer(4);
        return ((long)((m_buffer[0] & 0xff) | ((m_buffer[1] << 8) & 0xff00) | ((m_buffer[2] << 16) & 0xff0000) | ((m_buffer[3] << 24) & 0xff000000))) & 0xFFFFFFFFL;
    }

    public long readInt64()
    {
        fillBuffer(8);
        int lo = (int)((m_buffer[0] & 0xff) | ((m_buffer[1] << 8) & 0xff00) | ((m_buffer[2] << 16) & 0xff0000) | ((m_buffer[3] << 24) & 0xff000000));
        int hi = (int)((m_buffer[4] & 0xff) | ((m_buffer[5] << 8) & 0xff00) | ((m_buffer[6] << 16) & 0xff0000) | ((m_buffer[7] << 24) & 0xff000000));
        return (long) ((long)hi) << 32 | lo;
    }

//    [CLSCompliant(false)]
//    public ulong ReadUInt64() {
//        FillBuffer(8);
//        uint lo = (uint)(m_buffer[0] | m_buffer[1] << 8 |
//                         m_buffer[2] << 16 | m_buffer[3] << 24);
//        uint hi = (uint)(m_buffer[4] | m_buffer[5] << 8 |
//                         m_buffer[6] << 16 | m_buffer[7] << 24);
//        return ((ulong)hi) << 32 | lo;
//    }

//    [System.Security.SecuritySafeCritical]  // auto-generated
    // TODO: public unsafe float ReadSingle()
//    public unsafe float ReadSingle() {
//        FillBuffer(4);
//        uint tmpBuffer = (uint)(m_buffer[0] | m_buffer[1] << 8 | m_buffer[2] << 16 | m_buffer[3] << 24);
//        return *((float*)&tmpBuffer);
//    }
    public float readSingle()
	{
		return ByteBuffer.wrap(this.readBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	}

//    [System.Security.SecuritySafeCritical]  // auto-generated
//    public unsafe double ReadDouble() {
//        FillBuffer(8);
//        uint lo = (uint)(m_buffer[0] | m_buffer[1] << 8 |
//            m_buffer[2] << 16 | m_buffer[3] << 24);
//        uint hi = (uint)(m_buffer[4] | m_buffer[5] << 8 |
//            m_buffer[6] << 16 | m_buffer[7] << 24);

//        ulong tmpBuffer = ((ulong)hi) << 32 | lo;
//        return *((double*)&tmpBuffer);
//    }

    // TODO: public BigDecimal readDecimal()
//    public BigDecimal readDecimal()
//    {
//        fillBuffer(16);
//        try {
//            return new BigDecimal((char[])m_buffer, 0,8);
//        }
//        catch (ArgumentException e) {
//             ReadDecimal cannot leak out ArgumentException
//            throw new IOException("Arg_DecBitCtor", e);
//        }
//    }

    // TODO: validate readString()
    public String readString()
    {
//        Contract.Ensures(Contract.Result<String>() != null);

        if (m_stream == null)
        	throw new RuntimeException("File not open");	// __Error.FileNotOpen();

        int currPos = 0;
        int n;
        int stringLength;
        int readLength;
        int charsRead;

        // Length of the string in bytes, not chars
        stringLength = read7BitEncodedInt();
        // TODO: What should we do if we have a bad stream
//        if (stringLength<0) {
//            throw new IOException("IO.IO_InvalidStringLen_Len: " + stringLength);
//        }

        if (stringLength==0) {
            return "";
        }

        if (m_charBytes==null) {
            m_charBytes  = new byte[MaxCharBytesSize];
        }
        
        if (m_charBuffer == null) {
            m_charBuffer = new char[m_maxCharsSize];
        }
        
        // TODO: validate readString()
        // NOTE: I added these two lines instead of the rest of the method
        byte[] result = readBytes(stringLength);
        return new String(result);
        
//        StringBuilder sb = null; 
//        do
//        {
//            readLength = ((stringLength - currPos)>MaxCharBytesSize)?MaxCharBytesSize:(stringLength - currPos);

//            n = m_stream.read(m_charBytes, 0, readLength);
//            if (n==0) {
            	// TODO: need to check what to do here.
//            	throw new RuntimeException("end of file");	// __Error.EndOfFile();
//            }

//            charsRead = m_decoder.GetChars(m_charBytes, 0, n, m_charBuffer, 0);

//            if (currPos == 0 && n == stringLength)
//                return new String(m_charBuffer, 0, charsRead);

//            if (sb == null)
//                sb = StringBuilderCache.Acquire(stringLength); // Actual string length in chars may be smaller.
//            sb.Append(m_charBuffer, 0, charsRead);
//            currPos +=n;
        
//        } while (currPos<stringLength);

//        return StringBuilderCache.GetStringAndRelease(sb);
    }

//    [SecuritySafeCritical]
    // TODO: public int read(char[] buffer, int index, int count)
//    public int read(char[] buffer, int index, int count)
//    {
//        if (buffer==null) {
//            throw new NullPointerException("buffer: ArgumentNull_Buffer");
//        }
//        if (index < 0) {
//            throw new IllegalArgumentException("index: ArgumentOutOfRange_NeedNonNegNum");
//        }
//        if (count < 0) {
//            throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
//        }
//        if (buffer.length - index < count) {
//            throw new IllegalArgumentException("Argument_InvalidOffLen");
//        }
//        Contract.Ensures(Contract.Result<int>() >= 0);
//        Contract.Ensures(Contract.Result<int>() <= count);
//        Contract.EndContractBlock();

//        if (m_stream==null)
//        	throw new RuntimeException("File not open");	// __Error.FileNotOpen();

        // SafeCritical: index and count have already been verified to be a valid range for the buffer
//        return internalReadChars(buffer, index, count);
//    }

    // TODO: Need to finish this method.
	private int internalReadChars(char[] buffer, int index, int count)
	{
//		Contract.Requires(buffer != null);
//		Contract.Requires(index >= 0 && count >= 0);
		assert(m_stream != null);

		int numBytes = 0;
		int charsRemaining = count;

		if (m_charBytes==null)
		{
			m_charBytes = new byte[MaxCharBytesSize];
		}

		while (charsRemaining > 0)
		{
			int charsRead = 0;
			// We really want to know what the minimum number of bytes per char
			// is for our encoding.  Otherwise for UnicodeEncoding we'd have to
			// do ~1+log(n) reads to read n characters.
			numBytes = charsRemaining;

			// special case for DecoderNLS subclasses when there is a hanging byte from the previous loop
//			DecoderNLS decoder = m_decoder as DecoderNLS;
//			if (decoder != null && decoder.HasState && numBytes > 1)
//			{
//				numBytes -= 1;
//			}

			if (m_2BytesPerChar)
				numBytes <<= 1;
			if (numBytes > MaxCharBytesSize)
				numBytes = MaxCharBytesSize;

			int position = 0;
			byte[] byteBuffer = null;
			if (m_isMemoryStream)
			{
				MemoryStream mStream = As.as(m_stream, MemoryStream.class);
				assert(mStream != null) : "m_stream as MemoryStream != null";

				position = mStream.internalGetPosition();
				numBytes = mStream.internalEmulateRead(numBytes);
				byteBuffer = mStream.internalGetBuffer();
			}
			else
			{
				numBytes = m_stream.read(m_charBytes, 0, numBytes);
				byteBuffer = m_charBytes;                  
			}

			if (numBytes == 0)
			{
				return (count - charsRemaining);
			}

			assert(byteBuffer != null) : "expected byteBuffer to be non-null";
//			unsafe {
//				fixed (byte* pBytes = byteBuffer)
//				fixed (char* pChars = buffer) {
//					charsRead = m_decoder.GetChars(pBytes + position, numBytes, pChars + index, charsRemaining, false);
//				}
//			}

			charsRemaining -= charsRead;
			index+=charsRead;
		}

		// this should never fail
		assert(charsRemaining >= 0) : "We read too many characters.";

		// we may have read fewer than the number of characters requested if end of stream reached 
		// or if the encoding makes the char count too big for the buffer (e.g. fallback sequence)
		return (count - charsRemaining);
	}

    // TODO: Need to take care of numBytes
	private int internalReadOneChar()
	{
		// I know having a separate InternalReadOneChar method seems a little 
		// redundant, but this makes a scenario like the security parser code
		// 20% faster, in addition to the optimizations for UnicodeEncoding I
		// put in InternalReadChars.   
		int charsRead = 0;
		int numBytes = 0;
		long posSav = 0L;

		if (m_stream.canSeek())
			posSav = m_stream.getPosition();

		if (m_charBytes==null)
		{
			m_charBytes = new byte[MaxCharBytesSize];
		}
		if (m_singleChar==null)
		{
			m_singleChar = new char[1];
		}

		while (charsRead == 0)
		{
			// We really want to know what the minimum number of bytes per char
			// is for our encoding.  Otherwise for UnicodeEncoding we'd have to
			// do ~1+log(n) reads to read n characters.
			// Assume 1 byte can be 1 char unless m_2BytesPerChar is true.
			numBytes = m_2BytesPerChar ? 2 : 1;

			int r = m_stream.readByte();
			m_charBytes[0] = (byte) r;
			if (r == -1)
				numBytes = 0;
			if (numBytes == 2)
			{
				r = m_stream.readByte();
				m_charBytes[1] = (byte) r;
				if (r == -1)
					numBytes = 1;
			}

			if (numBytes==0)
			{
				// Console.WriteLine("Found no bytes.  We're outta here.");
				return -1;
			}

			assert(numBytes == 1 || numBytes == 2) : "BinaryReader::InternalReadOneChar assumes it's reading one or 2 bytes only.";

			m_singleChar[0] = (char) m_charBytes[0]; // ByteBuffer.wrap(m_charBytes).getChar();
			charsRead = 1;
//			try
//			{
//				charsRead = m_decoder.GetChars(m_charBytes, 0, numBytes, m_singleChar, 0);
//			}
//			catch
//			{
				// Handle surrogate char 

//				if (m_stream.canSeek())
//					m_stream.Seek((posSav - m_stream.Position), SeekOrigin.Current);
				//else - we can't do much here

//				throw;
//			}

			assert(charsRead < 2) : "InternalReadOneChar - assuming we only got 0 or 1 char, not 2!";
			// Console.WriteLine("That became: " + charsRead + " characters.");
		}
		if (charsRead == 0)
			return -1;
		return m_singleChar[0];
	}

    public char[] readChars(int count)
    {
        if (count<0) {
            throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
        }
//        Contract.Ensures(Contract.Result<char[]>() != null);
//        Contract.Ensures(Contract.Result<char[]>().Length <= count);
//        Contract.EndContractBlock();
        if (m_stream == null) {
        	throw new RuntimeException("File not open");	// __Error.FileNotOpen();
        }

        if (count == 0) {
            return new char[0]; // EmptyArray<Char>.Value;
        }

        // SafeCritical: we own the chars buffer, and therefore can guarantee that the index and count are valid
        char[] chars = new char[count];
        int n = internalReadChars(chars, 0, count);
        if (n!=count) {
            char[] copy = new char[n];
//            Buffer.InternalBlockCopy(chars, 0, copy, 0, 2*n); // sizeof(char)
            System.arraycopy(chars, 0, copy, 0, 2*n);
            chars = copy;
        }

        return chars;
    }

    public int read(byte[] buffer, int index, int count)
    {
        if (buffer==null)
            throw new NullPointerException("buffer: ArgumentNull_Buffer");
        if (index < 0)
            throw new IllegalArgumentException("index: ArgumentOutOfRange_NeedNonNegNum");
        if (count < 0)
            throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
        if (buffer.length - index < count)
            throw new IllegalArgumentException("Argument_InvalidOffLen");
//        Contract.Ensures(Contract.Result<int>() >= 0);
//        Contract.Ensures(Contract.Result<int>() <= count);
//        Contract.EndContractBlock();

        if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();
        return m_stream.read(buffer, index, count);
    }

    public byte[] readBytes(int count)
    {
        if (count < 0) throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
//        Contract.Ensures(Contract.Result<byte[]>() != null);
//        Contract.Ensures(Contract.Result<byte[]>().Length <= Contract.OldValue(count));
//        Contract.EndContractBlock();
        if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();

        if (count == 0) {
            return new byte[0]; // EmptyArray<Byte>.Value;
        }

        byte[] result = new byte[count];

        int numRead = 0;
        do {
            int n = m_stream.read(result, numRead, count);
            if (n == 0)
                break;
            numRead += n;
            count -= n;
        } while (count > 0);

        if (numRead != result.length) {
            // Trim array.  This should happen on EOF & possibly net streams.
            byte[] copy = new byte[numRead];
//            Buffer.InternalBlockCopy(result, 0, copy, 0, numRead);
            System.arraycopy(result, 0, copy, 0, numRead);
            result = copy;
        }

        return result;
    }

    protected void fillBuffer(int numBytes)
    {
        if (m_buffer != null && (numBytes < 0 || numBytes > m_buffer.length)) {
            throw new IllegalArgumentException("numBytes: ArgumentOutOfRange_BinaryReaderFillBuffer");
        }
        int bytesRead=0;
        int n = 0;

        if (m_stream==null) throw new RuntimeException("File not open");	// __Error.FileNotOpen();

        // Need to find a good threshold for calling ReadByte() repeatedly
        // vs. calling Read(byte[], int, int) for both buffered & unbuffered
        // streams.
        if (numBytes==1) {
            n = m_stream.readByte();
//            if (n==-1)
            	// TODO: need to check what to do here.
//            	throw new RuntimeException("end of file");	// __Error.EndOfFile();
            m_buffer[0] = (byte)n;
            return;
        }

        do {
            n = m_stream.read(m_buffer, bytesRead, numBytes-bytesRead);
//            if (n==0)
//            {
            	// TODO: need to check what to do here.
//            	throw new RuntimeException("end of file");	// __Error.EndOfFile();
//            }
            bytesRead+=n;
        } while (bytesRead<numBytes);
    }

    protected int read7BitEncodedInt()
	{
        // Read out an Int32 7 bits at a time.  The high bit
        // of the byte when on means to continue reading more bytes.
        int count = 0;
        int shift = 0;
        byte b;
        do
        {
            // Check for a corrupted stream.  Read a max of 5 bytes.
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
