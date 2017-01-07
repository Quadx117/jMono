package jMono_Framework.dotNet.io;

import java.io.IOException;
import java.util.Arrays;

public class MemoryStream extends Stream
{
	private byte[] _buffer;    // Either allocated internally or externally.
	private int _origin;       // For user-provided arrays, start at this origin
	private int _position;     // read/write head.
	// [ContractPublicPropertyName("Length")]
	private int _length;       // Number of bytes within the memory stream
	private int _capacity;     // length of usable portion of buffer for stream
	// Note that _capacity == _buffer.Length for non-user-provided byte[]'s

	private boolean _expandable;  // User-provided buffers aren't expandable.
	private boolean _writable;    // Can user write to this stream?
	private boolean _exposable;   // Whether the array can be returned to the user.
	private boolean _isOpen;      // Is this stream open or closed?

	// [NonSerialized]
	// private Task<int> _lastReadTask; // The last successful task returned from ReadAsync

	private final int MemStreamMaxLength = Integer.MAX_VALUE;

	public MemoryStream()
	{
		this(0);
	}

	public MemoryStream(int capacity)
	{
		if (capacity < 0)
		{
			throw new IllegalArgumentException("capacity: ArgumentOutOfRange_NegativeCapacity");
		}
		// Contract.EndContractBlock();

		// TODO: more research on EmptyArray<byte>.Value
		_buffer = new byte[capacity]; // capacity != 0 ? new byte[capacity] : EmptyArray<byte>.Value;
		_capacity = capacity;
		_expandable = true;
		_writable = true;
		_exposable = true;
		_origin = 0;      // Must be 0 for byte[]'s created by MemoryStream
		_isOpen = true;
	}

	public MemoryStream(byte[] buffer)
	{
		this(buffer, true);
	}

	public MemoryStream(byte[] buffer, boolean writable)
	{
		if (buffer == null)
			throw new NullPointerException("buffer: ArgumentNull_Buffer");
		// Contract.EndContractBlock();
		_buffer = buffer;
		_length = _capacity = buffer.length;
		_writable = writable;
		_exposable = false;
		_origin = 0;
		_isOpen = true;
	}

	public MemoryStream(byte[] buffer, int index, int count)
	{
		this(buffer, index, count, true, false);
	}

	public MemoryStream(byte[] buffer, int index, int count, boolean writable)
	{
		this(buffer, index, count, writable, false);
	}

	public MemoryStream(byte[] buffer, int index, int count, boolean writable, boolean publiclyVisible)
	{
		if (buffer == null)
			throw new NullPointerException("buffer; ArgumentNull_Buffer");
		if (index < 0)
			throw new IllegalArgumentException("index : ArgumentOutOfRange_NeedNonNegNum");
		if (count < 0)
			throw new IllegalArgumentException("count : ArgumentOutOfRange_NeedNonNegNum");
		if (buffer.length - index < count)
			throw new IllegalArgumentException("Argument_InvalidOffLen");
		// Contract.EndContractBlock();

		_buffer = buffer;
		_origin = _position = index;
		_length = _capacity = index + count;
		_writable = writable;
		_exposable = publiclyVisible;  // Can TryGetBuffer/GetBuffer return the array?
		_expandable = false;
		_isOpen = true;
	}

	@Override
	public boolean canRead()
	{
		return _isOpen;
	}

	@Override
	public boolean canSeek()
	{
		return _isOpen;
	}

	@Override
	public boolean canWrite()
	{
		return _writable;
	}

	private void ensureWriteable()
	{
		if (!canWrite())
			throw new IllegalStateException("Write is not supported");
	}

	@Override
	protected void dispose(boolean disposing)
	{
		try
		{
			if (disposing)
			{
				_isOpen = false;
				_writable = false;
				_expandable = false;
				// Don't set buffer to null - allow TryGetBuffer, GetBuffer & ToArray to work.
				// _lastReadTask = null;
			}
		}
		finally
		{
			// Call super.Close() to cleanup async IO resources
			super.dispose(disposing);
		}
	}

	// returns a boolean saying whether we allocated a new array.
	private boolean ensureCapacity(int value)
	{
		// Check for overflow
		if (value < 0)
//			throw new IOException("IO.IO_StreamTooLong");
			throw new IllegalArgumentException("IO.IO_StreamTooLong");
		if (value > _capacity)
		{
			int newCapacity = value;
			if (newCapacity < 256)
				newCapacity = 256;
			// We are ok with this overflowing since the next statement will deal
			// with the cases where _capacity*2 overflows.
			if (newCapacity < _capacity * 2)
				newCapacity = _capacity * 2;
			// We want to expand the array up to Array.MaxArrayLengthOneDimensional
			// And we want to give the user the value that they asked for
			// TODO: More research on Array.MaxByteArrayLength
			// see : https://github.com/dotnet/coreclr/blob/master/src/mscorlib/src/System/Array.cs
			// if ((int)(_capacity * 2) > Array.MaxByteArrayLength)
			// newCapacity = value > Array.MaxByteArrayLength ? value : Array.MaxByteArrayLength;

			setCapacity(newCapacity);
			return true;
		}
		return false;
	}

	@Override
	public void flush()
	{}

	// [HostProtection(ExternalThreading=true)]
	// [ComVisible(false)]
	// @Override
	// public Task FlushAsync(CancellationToken cancellationToken) {

	// if (cancellationToken.IsCancellationRequested)
	// return Task.FromCanceled(cancellationToken);

	// try {

	// Flush();
	// return Task.CompletedTask;

	// } catch(Exception ex) {

	// return Task.FromException(ex);
	// }
	// }

	public byte[] getBuffer()
	{
		if (!_exposable)
			throw new UnsupportedOperationException("UnauthorizedAccess_MemStreamBuffer");
		return _buffer;
	}

	// TODO: search for ArraySegment<byte>
	// public boolean TryGetBuffer(out ArraySegment<byte> buffer) {
	// if (!_exposable) {
	// buffer = default(ArraySegment<byte>);
	// return false;
	// }

	// buffer = new ArraySegment<byte>(_buffer, offset:_origin, count:(_length - _origin));
	// return true;
	// }

	// -------------- PERF: Internal functions for fast direct access of MemoryStream buffer (cf. BinaryReader for
	// usage) ---------------

	// PERF: Internal sibling of GetBuffer, always returns a buffer (cf. GetBuffer())
	protected byte[] internalGetBuffer()
	{
		return _buffer;
	}

	// PERF: Get origin and length - used in ResourceWriter.
	// internal void InternalGetOriginAndLength(out int origin, out int length)
	// {
	// if (!_isOpen) throw new IllegalStateException("Stream is closed");
	// origin = _origin;
	// length = _length;
	// }

	// PERF: True cursor position, we don't need _origin for direct access
	protected int internalGetPosition()
	{
		if (!_isOpen) throw new IllegalStateException("Stream is closed");
		return _position;
	}

	// PERF: Takes out Int32 as fast as possible
	 protected int internalReadInt32()
	 {
		 if (!_isOpen)
			 throw new IllegalStateException("Stream is closed");

		 int pos = (_position += 4); // use temp to avoid a race condition
		 if (pos > _length)
		 {
			 _position = _length;
			// TODO: need to check what to do here.
         	throw new RuntimeException("end of file");	// __Error.EndOfFile();
		 }
		 return (int)((_buffer[pos-4] & 0xff) | ((_buffer[pos-3] << 8) & 0xff00) | ((_buffer[pos-2] << 16) & 0xff0000) | ((_buffer[pos-1] << 24) & 0xff000000));
	 }

	// PERF: Get actual length of bytes available for read; do sanity checks; shift position - i.e. everything except
	// actual copying bytes
	protected int internalEmulateRead(int count)
	{
		if (!_isOpen) throw new IllegalStateException("Stream is closed");

		int n = _length - _position;
		if (n > count) n = count;
		if (n < 0) n = 0;

		assert(_position + n >= 0) : "_position + n >= 0"; // len is less than 2^31 -1.
		_position += n;
		return n;
	}

	// Gets & sets the capacity (number of bytes allocated) for this stream.
	// The capacity cannot be set to a value less than the current length
	// of the stream.
	//
	public int getCapacity()
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		return _capacity - _origin;
	}

	public void setCapacity(int value)
	{
		// Only update the capacity if the MS is expandable and the value is different than the current capacity.
		// Special behavior if the MS isn't expandable: we don't throw if value is the same as the current capacity
		if (value < getLength())
			throw new IllegalArgumentException("value: ArgumentOutOfRange_SmallCapacity");
		// Contract.Ensures(_capacity - _origin == value);
		// Contract.EndContractBlock();

		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		if (!_expandable && (value != getCapacity()))
			throw new IllegalStateException("Memory stream not expandable");

		// MemoryStream has this invariant: _origin > 0 => !expandable (see ctors)
		if (_expandable && value != _capacity)
		{
			if (value > 0)
			{
				byte[] newBuffer = new byte[value];
				// TODO: More research on Buffer.InternalBlockCopy
				// see https://github.com/dotnet/coreclr/blob/master/src/mscorlib/src/System/Buffer.cs
				if (_length > 0)
					System.arraycopy(_buffer, 0, newBuffer, 0, _length);
				// Buffer.InternalBlockCopy(_buffer, 0, newBuffer, 0, _length);
				_buffer = newBuffer;
			}
			else
			{
				_buffer = null;
			}
			_capacity = value;
		}
	}

	@Override
	public long getLength()
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		return _length - _origin;
	}

	@Override
	public long getPosition()
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		return _position - _origin;
	}

	@Override
	public void setPosition(long value)
	{
		if (value < 0)
			throw new IllegalArgumentException("value: ArgumentOutOfRange_NeedNonNegNum");
		// Contract.Ensures(Position == value);
		// Contract.EndContractBlock();

		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");

		if (value > MemStreamMaxLength)
			throw new IllegalArgumentException("value :ArgumentOutOfRange_StreamLength");
		_position = _origin + (int) value;
	}

	@Override
	public int read(byte[] buffer, int offset, int count)
	{
		if (buffer == null)
			throw new NullPointerException("buffer: ArgumentNull_Buffer");
		if (offset < 0)
			throw new IllegalArgumentException("offset: ArgumentOutOfRange_NeedNonNegNum");
		if (count < 0)
			throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
		if (buffer.length - offset < count)
			throw new IllegalArgumentException("Argument_InvalidOffLen");
		// Contract.EndContractBlock();

		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");

		int n = _length - _position;
		if (n > count)
			n = count;
		if (n <= 0)
			return 0;

		assert (_position + n >= 0) : "_position + n >= 0";  // len is less than 2^31 -1.

		if (n <= 8)
		{
			int byteCount = n;
			while (--byteCount >= 0)
				buffer[offset + byteCount] = _buffer[_position + byteCount];
		}
		else
			System.arraycopy(_buffer, _position, buffer, offset, n);
		// Buffer.InternalBlockCopy(_buffer, _position, buffer, offset, n);
		_position += n;

		return n;
	}

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// @Override
	// public Task<int> ReadAsync(Byte[] buffer, int offset, int count, CancellationToken cancellationToken)
	// {
	// if (buffer==null)
	// throw new NullPointerException("buffer: ArgumentNull_Buffer"));
	// if (offset < 0)
	// throw new IllegalArgumentException("offset: ArgumentOutOfRange_NeedNonNegNum"));
	// if (count < 0)
	// throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum"));
	// if (buffer.Length - offset < count)
	// throw new ArgumentException(Environment.GetResourceString("Argument_InvalidOffLen"));
	// Contract.EndContractBlock(); // contract validation copied from Read(...)

	// If cancellation was requested, bail early
	// if (cancellationToken.IsCancellationRequested)
	// return Task.FromCanceled<int>(cancellationToken);

	// try
	// {
	// int n = Read(buffer, offset, count);
	// var t = _lastReadTask;
	// Contract.Assert(t == null || t.Status == TaskStatus.RanToCompletion,
	// "Expected that a stored last task completed successfully");
	// return (t != null && t.Result == n) ? t : (_lastReadTask = Task.FromResult<int>(n));
	// }
	// catch (OperationCanceledException oce)
	// {
	// return Task.FromCancellation<int>(oce);
	// }
	// catch (Exception exception)
	// {
	// return Task.FromException<int>(exception);
	// }
	// }

	@Override
	public int readByte()
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");

		if (_position >= _length)
			return -1;

		return _buffer[_position++];
	}

	// @Override
	// public Task CopyToAsync(Stream destination, Int32 bufferSize, CancellationToken cancellationToken) {

	// This implementation offers beter performance compared to the base class version.

	// The parameter checks must be in sync with the base version:
	// if (destination == null)
	// throw new NullPointerException("destination");

	// if (bufferSize <= 0)
	// throw new IllegalArgumentException("bufferSize: ArgumentOutOfRange_NeedPosNum"));

	// if (!CanRead && !CanWrite)
	// throw new ObjectDisposedException(null, Environment.GetResourceString("ObjectDisposed_StreamClosed"));

	// if (!destination.CanRead && !destination.CanWrite)
	// throw new ObjectDisposedException("destination: ObjectDisposed_StreamClosed"));

	// if (!CanRead)
	// throw new NotSupportedException(Environment.GetResourceString("NotSupported_UnreadableStream"));

	// if (!destination.CanWrite)
	// throw new NotSupportedException(Environment.GetResourceString("NotSupported_UnwritableStream"));

	// Contract.EndContractBlock();

	// If we have been inherited into a subclass, the following implementation could be incorrect
	// since it does not call through to Read() or Write() which a subclass might have overriden.
	// To be safe we will only use this implementation in cases where we know it is safe to do so,
	// and delegate to our base class (which will call into Read/Write) when we are not sure.
	// if (this.GetType() != typeof(MemoryStream))
	// return super.CopyToAsync(destination, bufferSize, cancellationToken);

	// If cancelled - return fast:
	// if (cancellationToken.IsCancellationRequested)
	// return Task.FromCanceled(cancellationToken);

	// Avoid copying data from this buffer into a temp buffer:
	// (require that InternalEmulateRead does not throw,
	// otherwise it needs to be wrapped into try-catch-Task.FromException like memStrDest.Write below)

	// Int32 pos = _position;
	// Int32 n = InternalEmulateRead(_length - _position);

	// If destination is not a memory stream, write there asynchronously:
	// MemoryStream memStrDest = destination as MemoryStream;
	// if (memStrDest == null)
	// return destination.WriteAsync(_buffer, pos, n, cancellationToken);

	// try {

	// If destination is a MemoryStream, CopyTo synchronously:
	// memStrDest.Write(_buffer, pos, n);
	// return Task.CompletedTask;

	// } catch(Exception ex) {
	// return Task.FromException(ex);
	// }
	// }

	@Override
	public long seek(long offset, SeekOrigin loc)
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");

		if (offset > MemStreamMaxLength)
			throw new IllegalArgumentException("offset: ArgumentOutOfRange_StreamLength");
		switch (loc)
		{
			case Begin:
			{
				int tempPosition = _origin + (int) offset;
				if (offset < 0 || tempPosition < _origin)
//					throw new IOException("IO.IO_SeekBeforeBegin");
					throw new IllegalArgumentException("IO.IO_SeekBeforeBegin");
				_position = tempPosition;
				break;
			}
			case Current:
			{
				int tempPosition = _position + (int) offset;
				if ((_position + offset) < _origin || tempPosition < _origin)
//					throw new IOException("IO.IO_SeekBeforeBegin");
					throw new IllegalArgumentException("IO.IO_SeekBeforeBegin");
				_position = tempPosition;
				break;
			}
			case End:
			{
				int tempPosition = _length + (int) offset;
				if ((_length + offset) < _origin || tempPosition < _origin)
//					throw new IOException("IO.IO_SeekBeforeBegin");
					throw new IllegalArgumentException("IO.IO_SeekBeforeBegin");
				_position = tempPosition;
				break;
			}
			default:
				throw new IllegalArgumentException("Argument_InvalidSeekOrigin");
		}

		assert (_position >= 0) : "_position >= 0";
		return _position;
	}

	// Sets the length of the stream to a given value. The new
	// value must be nonnegative and less than the space remaining in
	// the array, Integer.MAX_VALUE - origin
	// Origin is 0 in all cases other than a MemoryStream created on
	// top of an existing array and a specific starting offset was passed
	// into the MemoryStream constructor. The upper bounds prevents any
	// situations where a stream may be created on top of an array then
	// the stream is made longer than the maximum possible length of the
	// array (Integer.MAX_VALUE).
	//
	@Override
	public void setLength(long value)
	{
		if (value < 0 || value > Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("value: ArgumentOutOfRange_StreamLength");
		}
		// Contract.Ensures(_length - _origin == value);
		// Contract.EndContractBlock();
		ensureWriteable();

		// Origin wasn't publicly exposed above.
		// assert(MemStreamMaxLength == Integer.MAX_VALUE); // Check parameter validation logic in this method if this
		// fails.
		if (value > (Integer.MAX_VALUE - _origin))
		{
			throw new IllegalArgumentException("value: ArgumentOutOfRange_StreamLength");
		}

		int newLength = _origin + (int) value;
		boolean allocatedNewArray = ensureCapacity(newLength);
		if (!allocatedNewArray && newLength > _length)
			Arrays.fill(_buffer, _length, newLength - _length, (byte) 0);
		_length = newLength;
		if (_position > newLength)
			_position = newLength;

	}

	public byte[] toArray()
	{
		// BCLDebug.Perf(_exposable, "MemoryStream::GetBuffer will let you avoid a copy.");
		byte[] copy = new byte[_length - _origin];
		System.arraycopy(_buffer, _origin, copy, 0, _length - _origin);
		// Buffer.InternalBlockCopy(_buffer, _origin, copy, 0, _length - _origin);
		return copy;
	}

	@Override
	public void write(byte[] buffer, int offset, int count) throws IOException
	{
		if (buffer == null)
			throw new NullPointerException("buffer: ArgumentNull_Buffer");
		if (offset < 0)
			throw new IllegalArgumentException("offset: ArgumentOutOfRange_NeedNonNegNum");
		if (count < 0)
			throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum");
		if (buffer.length - offset < count)
			throw new IllegalArgumentException("Argument_InvalidOffLen");
		// Contract.EndContractBlock();

		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		ensureWriteable();

		int i = _position + count;
		// Check for overflow
		if (i < 0)
			throw new IOException("IO.IO_StreamTooLong");

		if (i > _length)
		{
			boolean mustZero = _position > _length;
			if (i > _capacity)
			{
				boolean allocatedNewArray = ensureCapacity(i);
				if (allocatedNewArray)
					mustZero = false;
			}
			if (mustZero)
				Arrays.fill(_buffer, _length, i - _length, (byte) 0);
			_length = i;
		}
		if ((count <= 8) && (buffer != _buffer))
		{
			int byteCount = count;
			while (--byteCount >= 0)
				_buffer[_position + byteCount] = buffer[offset + byteCount];
		}
		else
			// Buffer.InternalBlockCopy(buffer, offset, _buffer, _position, count);
			System.arraycopy(buffer, offset, _buffer, _position, count);
		_position = i;

	}

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// @Override
	// public Task WriteAsync(Byte[] buffer, int offset, int count, CancellationToken cancellationToken)
	// {
	// if (buffer == null)
	// throw new NullPointerException("buffer: ArgumentNull_Buffer"));
	// if (offset < 0)
	// throw new IllegalArgumentException("offset: ArgumentOutOfRange_NeedNonNegNum"));
	// if (count < 0)
	// throw new IllegalArgumentException("count: ArgumentOutOfRange_NeedNonNegNum"));
	// if (buffer.Length - offset < count)
	// throw new ArgumentException(Environment.GetResourceString("Argument_InvalidOffLen"));
	// Contract.EndContractBlock(); // contract validation copied from Write(...)

	// If cancellation is already requested, bail early
	// if (cancellationToken.IsCancellationRequested)
	// return Task.FromCanceled(cancellationToken);

	// try
	// {
	// Write(buffer, offset, count);
	// return Task.CompletedTask;
	// }
	// catch (OperationCanceledException oce)
	// {
	// return Task.FromCancellation<VoidTaskResult>(oce);
	// }
	// catch (Exception exception)
	// {
	// return Task.FromException(exception);
	// }
	// }

	@Override
	public void writeByte(byte value)
	{
		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		ensureWriteable();

		if (_position >= _length)
		{
			int newLength = _position + 1;
			boolean mustZero = _position > _length;
			if (newLength >= _capacity)
			{
				boolean allocatedNewArray = ensureCapacity(newLength);
				if (allocatedNewArray)
					mustZero = false;
			}
			if (mustZero)
				Arrays.fill(_buffer, _length, _position - _length, (byte) 0);
			_length = newLength;
		}
		_buffer[_position++] = value;

	}

	// Writes this MemoryStream to another stream.
	public void writeTo(Stream stream) throws IOException
	{
		if (stream == null)
			throw new NullPointerException("stream: ArgumentNull_Stream");
		// Contract.EndContractBlock();

		if (!_isOpen)
			throw new IllegalStateException("Stream is closed");
		stream.write(_buffer, _origin, _length - _origin);
	}

	@Override
	public int read() throws IOException
	{
		throw new UnsupportedOperationException("The method read() is not supported");
	}

	@Override
	public void write(int b) throws IOException
	{
		throw new UnsupportedOperationException("The method write(int) is not supported");
	}
}

// TODO: Contract.EndContractBlock() ?
// TODO: Contract.Ensures(_capacity - _origin == value) ?
// TODO: Contract.Assert ?
// TODO: Missing methods
