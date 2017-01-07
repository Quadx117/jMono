package jMono_Framework.dotNet.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * This abstract class is the superclass of all classes representing
 * an input stream of bytes.
 *
 * <p>
 * Applications that need to define a subclass of <code>InputStream</code>
 * must always provide a method that returns the next byte of input.
 *
 * <p>
 * This class is a combination of {@code java.io.InputStream} and {@code java.io.OutputStream}
 * and adds the functionalities of the .NET abstract class {@code System.IO.Stream}
 * 
 * @author Eric Perron (based on Microsoft .NET)
 * @see    java.io.InputStream
 * @see    java.io.OutputStream
 * @see    https://github.com/dotnet/coreclr/blob/master/src/mscorlib/src/System/IO/Stream.cs
 */
public abstract class Stream implements Closeable, Flushable
{
	// ########################################################################
	// #                         java.io.InputStream                          #
	// ########################################################################

	// MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to use when skipping.
	private static final int MAX_SKIP_BUFFER_SIZE = 2048;

	/**
	 * Reads the next byte of data from the input stream. The value byte is
	 * returned as an <code>int</code> in the range <code>0</code> to <code>255</code>.
	 * If no byte is available because the end of the stream has been reached, the
	 * value <code>-1</code> is returned. This method blocks until input data is available,
	 * the end of the stream is detected, or an exception is thrown.
	 *
	 * <p>
	 * A subclass must provide an implementation of this method.
	 *
	 * @return the next byte of data, or <code>-1</code> if the end of the
	 *         stream is reached.
	 * @exception IOException
	 *            if an I/O error occurs.
	 */
	public abstract int read() throws IOException;

	/**
	 * Reads some number of bytes from the input stream and stores them into
	 * the buffer array <code>b</code>. The number of bytes actually read is
	 * returned as an integer. This method blocks until input data is
	 * available, end of file is detected, or an exception is thrown.
	 *
	 * <p>
	 * If the length of <code>b</code> is zero, then no bytes are read and <code>0</code>
	 * is returned; otherwise, there is an attempt to read at least one byte. If no byte is
	 * available because the stream is at the end of the file, the value <code>-1</code> is
	 * returned; otherwise, at least one byte is read and stored into <code>b</code>.
	 *
	 * <p>
	 * The first byte read is stored into element <code>b[0]</code>, the next one into <code>b[1]</code>, and so on. The
	 * number of bytes read is, at most, equal to the length of <code>b</code>. Let <i>k</i> be the number of bytes
	 * actually read; these bytes will be stored in elements <code>b[0]</code> through <code>b[</code><i>k</i>
	 * <code>-1]</code>, leaving elements <code>b[</code><i>k</i><code>]</code> through <code>b[b.length-1]</code>
	 * unaffected.
	 *
	 * <p>
	 * The <code>read(b)</code> method for class <code>InputStream</code> has the same effect as:
	 * 
	 * <pre>
	 * <code> read(b, 0, b.length) </code>
	 * </pre>
	 *
	 * @param b
	 *        the buffer into which the data is read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
	 *         end of
	 *         the stream has been reached.
	 * @exception IOException
	 *            If the first byte cannot be read for any reason
	 *            other than the end of the file, if the input stream has been closed, or
	 *            if some other I/O error occurs.
	 * @exception NullPointerException
	 *            if <code>b</code> is <code>null</code>.
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte b[]) throws IOException
	{
		return read(b, 0, b.length);
	}

	/**
	 * Reads up to <code>len</code> bytes of data from the input stream into
	 * an array of bytes. An attempt is made to read as many as <code>len</code> bytes, but a smaller number may be
	 * read.
	 * The number of bytes actually read is returned as an integer.
	 *
	 * <p>
	 * This method blocks until input data is available, end of file is detected, or an exception is thrown.
	 *
	 * <p>
	 * If <code>len</code> is zero, then no bytes are read and <code>0</code> is returned; otherwise, there is an
	 * attempt to read at least one byte. If no byte is available because the stream is at end of file, the value
	 * <code>-1</code> is returned; otherwise, at least one byte is read and stored into <code>b</code>.
	 *
	 * <p>
	 * The first byte read is stored into element <code>b[off]</code>, the next one into <code>b[off+1]</code>, and so
	 * on. The number of bytes read is, at most, equal to <code>len</code>. Let <i>k</i> be the number of bytes actually
	 * read; these bytes will be stored in elements <code>b[off]</code> through <code>b[off+</code><i>k</i>
	 * <code>-1]</code>, leaving elements <code>b[off+</code><i>k</i><code>]</code> through <code>b[off+len-1]</code>
	 * unaffected.
	 *
	 * <p>
	 * In every case, elements <code>b[0]</code> through <code>b[off]</code> and elements <code>b[off+len]</code>
	 * through <code>b[b.length-1]</code> are unaffected.
	 *
	 * <p>
	 * The <code>read(b,</code> <code>off,</code> <code>len)</code> method for class <code>InputStream</code> simply
	 * calls the method <code>read()</code> repeatedly. If the first such call results in an <code>IOException</code>,
	 * that exception is returned from the call to the <code>read(b,</code> <code>off,</code> <code>len)</code> method.
	 * If any subsequent call to <code>read()</code> results in a <code>IOException</code>, the exception is caught and
	 * treated as if it were end of file; the bytes read up to that point are stored into <code>b</code> and the number
	 * of bytes read before the exception occurred is returned. The default implementation of this method blocks until
	 * the requested amount of input data <code>len</code> has been read, end of file is detected, or an exception is
	 * thrown. Subclasses are encouraged to provide a more efficient implementation of this method.
	 *
	 * @param b
	 *        the buffer into which the data is read.
	 * @param off
	 *        the start offset in array <code>b</code> at which the data is written.
	 * @param len
	 *        the maximum number of bytes to read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
	 *         end of
	 *         the stream has been reached.
	 * @exception IOException
	 *            If the first byte cannot be read for any reason
	 *            other than end of file, or if the input stream has been closed, or if
	 *            some other I/O error occurs.
	 * @exception NullPointerException
	 *            If <code>b</code> is <code>null</code>.
	 * @exception IndexOutOfBoundsException
	 *            If <code>off</code> is negative, <code>len</code> is negative, or <code>len</code> is greater than
	 *            <code>b.length - off</code>
	 * @see java.io.InputStream#read()
	 */
	public int read(byte b[], int off, int len)
	{
		if (b == null)
		{
			throw new NullPointerException();
		}
		else if (off < 0 || len < 0 || len > b.length - off)
		{
			throw new IndexOutOfBoundsException();
		}
		else if (len == 0)
		{
			return 0;
		}

		int c = -1;
		try
		{
			c = read();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c == -1)
		{
			return -1;
		}
		b[off] = (byte) c;

		int i = 1;
		try
		{
			for (; i < len; ++i)
			{
				c = read();
				if (c == -1)
				{
					break;
				}
				b[off + i] = (byte) c;
			}
		}
		catch (IOException ee)
		{
		}
		return i;
	}

	/**
	 * Skips over and discards <code>n</code> bytes of data from this input
	 * stream. The <code>skip</code> method may, for a variety of reasons, end
	 * up skipping over some smaller number of bytes, possibly <code>0</code>.
	 * This may result from any of a number of conditions; reaching end of file
	 * before <code>n</code> bytes have been skipped is only one possibility.
	 * The actual number of bytes skipped is returned. If {@code n} is
	 * negative, the {@code skip} method for class {@code InputStream} always
	 * returns 0, and no bytes are skipped. Subclasses may handle the negative
	 * value differently.
	 *
	 * <p>
	 * The <code>skip</code> method of this class creates a byte array and then repeatedly reads into it until
	 * <code>n</code> bytes have been read or the end of the stream has been reached. Subclasses are encouraged to
	 * provide a more efficient implementation of this method. For instance, the implementation may depend on the
	 * ability to seek.
	 *
	 * @param n
	 *        the number of bytes to be skipped.
	 * @return the actual number of bytes skipped.
	 * @exception IOException
	 *            if the stream does not support seek,
	 *            or if some other I/O error occurs.
	 */
	public long skip(long n) throws IOException
	{

		long remaining = n;
		int nr;

		if (n <= 0)
		{
			return 0;
		}

		int size = (int) Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
		byte[] skipBuffer = new byte[size];
		while (remaining > 0)
		{
			nr = read(skipBuffer, 0, (int) Math.min(size, remaining));
			if (nr < 0)
			{
				break;
			}
			remaining -= nr;
		}

		return n - remaining;
	}

	/**
	 * Returns an estimate of the number of bytes that can be read (or
	 * skipped over) from this input stream without blocking by the next
	 * invocation of a method for this input stream. The next invocation
	 * might be the same thread or another thread. A single read or skip of this
	 * many bytes will not block, but may read or skip fewer bytes.
	 *
	 * <p>
	 * Note that while some implementations of {@code InputStream} will return the total number of bytes in the stream,
	 * many will not. It is never correct to use the return value of this method to allocate a buffer intended to hold
	 * all data in this stream.
	 *
	 * <p>
	 * A subclass' implementation of this method may choose to throw an {@link IOException} if this input stream has
	 * been closed by invoking the {@link #close()} method.
	 *
	 * <p>
	 * The {@code available} method for class {@code InputStream} always returns {@code 0}.
	 *
	 * <p>
	 * This method should be overridden by subclasses.
	 *
	 * @return an estimate of the number of bytes that can be read (or skipped
	 *         over) from this input stream without blocking or {@code 0} when
	 *         it reaches the end of the input stream.
	 * @exception IOException
	 *            if an I/O error occurs.
	 */
	public int available() throws IOException
	{
		return 0;
	}

	/**
	 * Marks the current position in this input stream. A subsequent call to
	 * the <code>reset</code> method repositions this stream at the last marked
	 * position so that subsequent reads re-read the same bytes.
	 *
	 * <p>
	 * The <code>readlimit</code> arguments tells this input stream to allow that many bytes to be read before the mark
	 * position gets invalidated.
	 *
	 * <p>
	 * The general contract of <code>mark</code> is that, if the method <code>markSupported</code> returns
	 * <code>true</code>, the stream somehow remembers all the bytes read after the call to <code>mark</code> and stands
	 * ready to supply those same bytes again if and whenever the method <code>reset</code> is called. However, the
	 * stream is not required to remember any data at all if more than <code>readlimit</code> bytes are read from the
	 * stream before <code>reset</code> is called.
	 *
	 * <p>
	 * Marking a closed stream should not have any effect on the stream.
	 *
	 * <p>
	 * The <code>mark</code> method of <code>InputStream</code> does nothing.
	 *
	 * @param readlimit
	 *        the maximum limit of bytes that can be read before
	 *        the mark position becomes invalid.
	 * @see java.io.InputStream#reset()
	 */
	public synchronized void mark(int readlimit)
	{}

	/**
	 * Repositions this stream to the position at the time the <code>mark</code> method was last called on this input
	 * stream.
	 *
	 * <p>
	 * The general contract of <code>reset</code> is:
	 *
	 * <ul>
	 * <li>If the method <code>markSupported</code> returns <code>true</code>, then:
	 *
	 * <ul>
	 * <li>If the method <code>mark</code> has not been called since the stream was created, or the number of bytes read
	 * from the stream since <code>mark</code> was last called is larger than the argument to <code>mark</code> at that
	 * last call, then an <code>IOException</code> might be thrown.
	 *
	 * <li>If such an <code>IOException</code> is not thrown, then the stream is reset to a state such that all the
	 * bytes read since the most recent call to <code>mark</code> (or since the start of the file, if <code>mark</code>
	 * has not been called) will be resupplied to subsequent callers of the <code>read</code> method, followed by any
	 * bytes that otherwise would have been the next input data as of the time of the call to <code>reset</code>.
	 * </ul>
	 *
	 * <li>If the method <code>markSupported</code> returns <code>false</code>, then:
	 *
	 * <ul>
	 * <li>The call to <code>reset</code> may throw an <code>IOException</code>.
	 *
	 * <li>If an <code>IOException</code> is not thrown, then the stream is reset to a fixed state that depends on the
	 * particular type of the input stream and how it was created. The bytes that will be supplied to subsequent callers
	 * of the <code>read</code> method depend on the particular type of the input stream.
	 * </ul>
	 * </ul>
	 *
	 * <p>
	 * The method <code>reset</code> for class <code>InputStream</code> does nothing except throw an
	 * <code>IOException</code>.
	 *
	 * @exception IOException
	 *            if this stream has not been marked or if the
	 *            mark has been invalidated.
	 * @see java.io.InputStream#mark(int)
	 * @see java.io.IOException
	 */
	public synchronized void reset() throws IOException
	{
		throw new IOException("mark/reset not supported");
	}

	/**
	 * Tests if this input stream supports the <code>mark</code> and <code>reset</code> methods. Whether or not
	 * <code>mark</code> and <code>reset</code> are supported is an invariant property of a
	 * particular input stream instance. The <code>markSupported</code> method
	 * of <code>InputStream</code> returns <code>false</code>.
	 *
	 * @return <code>true</code> if this stream instance supports the mark
	 *         and reset methods; <code>false</code> otherwise.
	 * @see java.io.InputStream#mark(int)
	 * @see java.io.InputStream#reset()
	 */
	public boolean markSupported()
	{
		return false;
	}

	// ########################################################################
	// #                         java.io.OutputStream                         #
	// ########################################################################

	/**
	 * Writes the specified byte to this output stream. The general
	 * contract for <code>write</code> is that one byte is written
	 * to the output stream. The byte to be written is the eight
	 * low-order bits of the argument <code>b</code>. The 24
	 * high-order bits of <code>b</code> are ignored.
	 * <p>
	 * Subclasses of <code>OutputStream</code> must provide an implementation for this method.
	 *
	 * @param b
	 *        the <code>byte</code>.
	 * @exception IOException
	 *            if an I/O error occurs. In particular,
	 *            an <code>IOException</code> may be thrown if the
	 *            output stream has been closed.
	 */
	public abstract void write(int b) throws IOException;

	/**
	 * Writes <code>b.length</code> bytes from the specified byte array
	 * to this output stream. The general contract for <code>write(b)</code> is that it should have exactly the same
	 * effect as the call <code>write(b, 0, b.length)</code>.
	 *
	 * @param b
	 *        the data.
	 * @exception IOException
	 *            if an I/O error occurs.
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte b[]) throws IOException
	{
		write(b, 0, b.length);
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array
	 * starting at offset <code>off</code> to this output stream.
	 * The general contract for <code>write(b, off, len)</code> is that
	 * some of the bytes in the array <code>b</code> are written to the
	 * output stream in order; element <code>b[off]</code> is the first
	 * byte written and <code>b[off+len-1]</code> is the last byte written
	 * by this operation.
	 * <p>
	 * The <code>write</code> method of <code>OutputStream</code> calls the write method of one argument on each of the
	 * bytes to be written out. Subclasses are encouraged to override this method and provide a more efficient
	 * implementation.
	 * <p>
	 * If <code>b</code> is <code>null</code>, a <code>NullPointerException</code> is thrown.
	 * <p>
	 * If <code>off</code> is negative, or <code>len</code> is negative, or <code>off+len</code> is greater than the
	 * length of the array <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is thrown.
	 *
	 * @param b
	 *        the data.
	 * @param off
	 *        the start offset in the data.
	 * @param len
	 *        the number of bytes to write.
	 * @exception IOException
	 *            if an I/O error occurs. In particular,
	 *            an <code>IOException</code> is thrown if the output
	 *            stream is closed.
	 */
	public void write(byte b[], int off, int len) throws IOException
	{
		if (b == null)
		{
			throw new NullPointerException();
		}
		else if ((off < 0) || (off > b.length) || (len < 0) ||
				((off + len) > b.length) || ((off + len) < 0))
		{
			throw new IndexOutOfBoundsException();
		}
		else if (len == 0)
		{
			return;
		}
		for (int i = 0; i < len; ++i)
		{
			write(b[off + i]);
		}
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes
	 * to be written out. The general contract of <code>flush</code> is
	 * that calling it is an indication that, if any bytes previously
	 * written have been buffered by the implementation of the output
	 * stream, such bytes should immediately be written to their
	 * intended destination.
	 * <p>
	 * If the intended destination of this stream is an abstraction provided by the underlying operating system, for
	 * example a file, then flushing the stream guarantees only that bytes previously written to the stream are passed
	 * to the operating system for writing; it does not guarantee that they are actually written to a physical device
	 * such as a disk drive.
	 * <p>
	 * The <code>flush</code> method of <code>OutputStream</code> does nothing.
	 *
	 * @exception IOException
	 *            if an I/O error occurs.
	 */
	public void flush() throws IOException
	{}

	/**
	 * Closes this output stream and releases any system resources
	 * associated with this stream. The general contract of <code>close</code> is that it closes the output stream. A
	 * closed stream cannot perform
	 * output operations and cannot be reopened.
	 * <p>
	 * The <code>close</code> method of <code>OutputStream</code> does nothing.
	 *
	 * @exception IOException
	 *            if an I/O error occurs.
	 */
	// public void close() throws IOException
	// {}

	// ########################################################################
	// #                           System.IO.Stream                           #
	// ########################################################################

	public static final Stream Null = new Stream.NullStream();

	// We pick a value that is the largest multiple of 4096 that is still smaller than
	// the large object heap threshold (85K).
	// The CopyTo/CopyToAsync buffer is short-lived and is likely to be collected at Gen0,
	// and it offers a significant improvement in Copy performance.
	private final int _DefaultCopyBufferSize = 81920;

	// To implement Async IO operations on streams that don't support async IO

	// [NonSerialized]
	// private ReadWriteTask _activeReadWriteTask;
	// [NonSerialized]
	// private SemaphoreSlim _asyncActiveSemaphore;

	// private SemaphoreSlim ensureAsyncActiveSemaphoreInitialized()
	// {
	// Lazily-initialize _asyncActiveSemaphore. As we're never accessing the SemaphoreSlim's
	// WaitHandle, we don't need to worry about Disposing it.
	// return LazyInitializer.EnsureInitialized(ref _asyncActiveSemaphore, () => new SemaphoreSlim(1, 1));
	// }

	public abstract boolean canRead();

	// If CanSeek is false, Position, Seek, Length, and SetLength should throw.
	public abstract boolean canSeek();

	// [ComVisible(false)]
	public boolean canTimeout()
	{
		return false;
	}

	public abstract boolean canWrite();

	public abstract long getLength();

	public abstract long getPosition();

	public abstract void setPosition(long value);

	// [ComVisible(false)]
	public int getReadTimeout()
	{
		// Contract.Ensures(Contract.Result<int>() >= 0);
		throw new UnsupportedOperationException("InvalidOperation_TimeoutsNotSupported");
	}

	public void setReadTimeout(int value)
	{
		throw new UnsupportedOperationException("InvalidOperation_TimeoutsNotSupported");
	}

	// [ComVisible(false)]
	public int getWriteTimeout()
	{
		// Contract.Ensures(Contract.Result<int>() >= 0);
		throw new UnsupportedOperationException("InvalidOperation_TimeoutsNotSupported");
	}

	public void setWriteTimeout(int value)
	{
		throw new UnsupportedOperationException("InvalidOperation_TimeoutsNotSupported");
	}

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public Task CopyToAsync(Stream destination)
	// {
	// int bufferSize = _DefaultCopyBufferSize;

	// #if FEATURE_CORECLR
	// if (CanSeek)
	// {
	// long length = Length;
	// long position = Position;
	// if (length <= position) // Handles negative overflows
	// {
	// If we go down this branch, it means there are
	// no bytes left in this stream.

	// Ideally we would just return Task.CompletedTask here,
	// but CopyToAsync(Stream, int, CancellationToken) was already
	// virtual at the time this optimization was introduced. So
	// if it does things like argument validation (checking if destination
	// is null and throwing an exception), then await fooStream.CopyToAsync(null)
	// would no longer throw if there were no bytes left. On the other hand,
	// we also can't roll our own argument validation and return Task.CompletedTask,
	// because it would be a breaking change if the stream's override didn't throw before,
	// or in a different order. So for simplicity, we just set the bufferSize to 1
	// (not 0 since the default implementation throws for 0) and forward to the virtual method.
	// bufferSize = 1;
	// }
	// else
	// {
	// long remaining = length - position;
	// if (remaining > 0) // In the case of a positive overflow, stick to the default size
	// bufferSize = (int)Math.Min(bufferSize, remaining);
	// }
	// }
	// #endif // FEATURE_CORECLR

	// return CopyToAsync(destination, bufferSize);
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public Task CopyToAsync(Stream destination, Int32 bufferSize)
	// {
	// return CopyToAsync(destination, bufferSize, CancellationToken.None);
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public virtual Task CopyToAsync(Stream destination, Int32 bufferSize, CancellationToken cancellationToken)
	// {
	// ValidateCopyToArguments(destination, bufferSize);

	// return CopyToAsyncInternal(destination, bufferSize, cancellationToken);
	// }

	// private async Task CopyToAsyncInternal(Stream destination, Int32 bufferSize, CancellationToken cancellationToken)
	// {
	// Contract.Requires(destination != null);
	// Contract.Requires(bufferSize > 0);
	// Contract.Requires(CanRead);
	// Contract.Requires(destination.CanWrite);

	// byte[] buffer = new byte[bufferSize];
	// int bytesRead;
	// while ((bytesRead = await ReadAsync(buffer, 0, buffer.Length, cancellationToken).ConfigureAwait(false)) != 0)
	// {
	// await destination.WriteAsync(buffer, 0, bytesRead, cancellationToken).ConfigureAwait(false);
	// }
	// }

	// Reads the bytes from the current stream and writes the bytes to
	// the destination stream until all bytes are read, starting at
	// the current position.
	public void copyTo(Stream destination)
	{
		int bufferSize = _DefaultCopyBufferSize;

		// #if FEATURE_CORECLR
		if (canSeek())
		{
			long length = getLength();
			long position = getPosition();
			if (length <= position) // Handles negative overflows
			{
				// No bytes left in stream
				// Call the other overload with a bufferSize of 1,
				// in case it's made virtual in the future
				bufferSize = 1;
			}
			else
			{
				long remaining = length - position;
				if (remaining > 0) // In the case of a positive overflow, stick to the default size
					bufferSize = (int) Math.min(bufferSize, remaining);
			}
		}
		// #endif // FEATURE_CORECLR

		copyTo(destination, bufferSize);
	}

	public void copyTo(Stream destination, int bufferSize)
	{
		validateCopyToArguments(destination, bufferSize);

		byte[] buffer = new byte[bufferSize];
		int read;
		while ((read = read(buffer, 0, buffer.length)) != 0)
			try
			{
				destination.write(buffer, 0, read);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Stream used to require that all cleanup logic went into Close(),
	// which was thought up before we invented IDisposable. However, we
	// need to follow the IDisposable pattern so that users can write
	// sensible subclasses without needing to inspect all their base
	// classes, and without worrying about version brittleness, from a
	// base class switching to the Dispose pattern. We're moving
	// Stream to the Dispose(bool) pattern - that's where all subclasses
	// should put their cleanup starting in V2.
	/**
	 * Closes this stream and releases any system resources associated with this stream. The general contract of
	 * <code>close</code> is that it closes the output stream. A closed stream cannot perform input/output operations
	 * and cannot be reopened.
	 * <p>
	 * The <code>close</code> method of <code>OutputStream</code> does nothing.
	 *
	 */
	public void close()
	{
		/*
		 * These are correct, but we'd have to fix PipeStream & NetworkStream very carefully.
		 * Contract.Ensures(CanRead == false);
		 * Contract.Ensures(CanWrite == false);
		 * Contract.Ensures(CanSeek == false);
		 */

		dispose(true);
		// GC.SuppressFinalize(this);
	}

	public void dispose()
	{
		/*
		 * These are correct, but we'd have to fix PipeStream & NetworkStream very carefully.
		 * Contract.Ensures(CanRead == false);
		 * Contract.Ensures(CanWrite == false);
		 * Contract.Ensures(CanSeek == false);
		 */

		close();
	}

	protected void dispose(boolean disposing)
	{
		// Note: Never change this to call other virtual methods on Stream
		// like Write, since the state on subclasses has already been
		// torn down. This is the last code to run on cleanup for a stream.
	}

	// public abstract void flush();

	// [HostProtection(ExternalThreading=true)]
	// [ComVisible(false)]
	// public Task FlushAsync()
	// {
	// return FlushAsync(CancellationToken.None);
	// }

	// [HostProtection(ExternalThreading=true)]
	// [ComVisible(false)]
	// public virtual Task FlushAsync(CancellationToken cancellationToken)
	// {
	// return Task.Factory.StartNew(state => ((Stream)state).Flush(), this,
	// cancellationToken, TaskCreationOptions.DenyChildAttach, TaskScheduler.Default);
	// }

	// [HostProtection(ExternalThreading=true)]
	// public virtual IAsyncResult BeginRead(byte[] buffer, int offset, int count, AsyncCallback callback, Object state)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);
	// return BeginReadInternal(buffer, offset, count, callback, state, serializeAsynchronously: false, apm: true);
	// }

	// [HostProtection(ExternalThreading = true)]
	// private IAsyncResult BeginReadInternal(
	// byte[] buffer, int offset, int count, AsyncCallback callback, Object state,
	// boolean serializeAsynchronously, boolean apm)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);
	// if (!CanRead) __Error.ReadNotSupported();

	// To avoid a race with a stream's position pointer & generating race conditions
	// with private buffer indexes in our own streams that
	// don't natively support async IO operations when there are multiple
	// async requests outstanding, we will block the application's main
	// thread if it does a second IO request until the first one completes.
	// var semaphore = EnsureAsyncActiveSemaphoreInitialized();
	// Task semaphoreTask = null;
	// if (serializeAsynchronously)
	// {
	// semaphoreTask = semaphore.WaitAsync();
	// }
	// else
	// {
	// semaphore.Wait();
	// }

	// Create the task to asynchronously do a Read. This task serves both
	// as the asynchronous work item and as the IAsyncResult returned to the user.
	// var asyncResult = new ReadWriteTask(true /*isRead*/, apm, delegate
	// {
	// The ReadWriteTask stores all of the parameters to pass to Read.
	// As we're currently inside of it, we can get the current task
	// and grab the parameters from it.
	// var thisTask = Task.InternalCurrent as ReadWriteTask;
	// Contract.Assert(thisTask != null, "Inside ReadWriteTask, InternalCurrent should be the ReadWriteTask");

	// try
	// {
	// Do the Read and return the number of bytes read
	// return thisTask._stream.Read(thisTask._buffer, thisTask._offset, thisTask._count);
	// }
	// finally
	// {
	// If this implementation is part of Begin/EndXx, then the EndXx method will handle
	// finishing the async operation. However, if this is part of XxAsync, then there won't
	// be an end method, and this task is responsible for cleaning up.
	// if (!thisTask._apm)
	// {
	// thisTask._stream.FinishTrackingAsyncOperation();
	// }

	// thisTask.ClearBeginState(); // just to help alleviate some memory pressure
	// }
	// }, state, this, buffer, offset, count, callback);

	// Schedule it
	// if (semaphoreTask != null)
	// RunReadWriteTaskWhenReady(semaphoreTask, asyncResult);
	// else
	// RunReadWriteTask(asyncResult);

	// return asyncResult; // return it
	// }

	// public int EndRead(IAsyncResult asyncResult)
	// {
	// if (asyncResult == null)
	// throw new NullPointerException("asyncResult");
	// Contract.Ensures(Contract.Result<int>() >= 0);
	// Contract.EndContractBlock();

	// var readTask = _activeReadWriteTask;

	// if (readTask == null)
	// {
	// throw new
	// ArgumentException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndReadCalledMultiple"));
	// }
	// else if (readTask != asyncResult)
	// {
	// throw new
	// IllegalStateException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndReadCalledMultiple"));
	// }
	// else if (!readTask._isRead)
	// {
	// throw new
	// ArgumentException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndReadCalledMultiple"));
	// }

	// try
	// {
	// return readTask.GetAwaiter().GetResult(); // block until completion, then get result / propagate any exception
	// }
	// finally
	// {
	// FinishTrackingAsyncOperation();
	// }
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public Task<int> ReadAsync(Byte[] buffer, int offset, int count)
	// {
	// return ReadAsync(buffer, offset, count, CancellationToken.None);
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public virtual Task<int> ReadAsync(Byte[] buffer, int offset, int count, CancellationToken cancellationToken)
	// {
	// If cancellation was requested, bail early with an already completed task.
	// Otherwise, return a task that represents the Begin/End methods.
	// return cancellationToken.IsCancellationRequested
	// ? Task.FromCanceled<int>(cancellationToken)
	// : BeginEndReadAsync(buffer, offset, count);
	// }

	// [System.Security.SecuritySafeCritical]
	// [MethodImplAttribute(MethodImplOptions.InternalCall)]
	// private extern boolean HasOverriddenBeginEndRead();

	// private Task<Int32> BeginEndReadAsync(Byte[] buffer, Int32 offset, Int32 count)
	// {
	// if (!HasOverriddenBeginEndRead())
	// {
	// If the Stream does not override Begin/EndRead, then we can take an optimized path
	// that skips an extra layer of tasks / IAsyncResults.
	// return (Task<Int32>)BeginReadInternal(buffer, offset, count, null, null, serializeAsynchronously: true, apm:
	// false);
	// }

	// Otherwise, we need to wrap calls to Begin/EndWrite to ensure we use the derived type's functionality.
	// return TaskFactory<Int32>.FromAsyncTrim(
	// this, new ReadWriteParameters { Buffer = buffer, Offset = offset, Count = count },
	// (stream, args, callback, state) => stream.BeginRead(args.Buffer, args.Offset, args.Count, callback, state), //
	// cached by compiler
	// (stream, asyncResult) => stream.EndRead(asyncResult)); // cached by compiler
	// }

	// private struct ReadWriteParameters // struct for arguments to Read and Write calls
	// {
	// private byte[] Buffer;
	// private int Offset;
	// private int Count;
	// }

	// [HostProtection(ExternalThreading=true)]
	// public virtual IAsyncResult BeginWrite(byte[] buffer, int offset, int count, AsyncCallback callback, Object
	// state)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);
	// return BeginWriteInternal(buffer, offset, count, callback, state, serializeAsynchronously: false, apm: true);
	// }

	// [HostProtection(ExternalThreading = true)]
	// private IAsyncResult BeginWriteInternal(
	// byte[] buffer, int offset, int count, AsyncCallback callback, Object state,
	// boolean serializeAsynchronously, boolean apm)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);
	// if (!CanWrite) __Error.WriteNotSupported();

	// To avoid a race condition with a stream's position pointer & generating conditions
	// with private buffer indexes in our own streams that
	// don't natively support async IO operations when there are multiple
	// async requests outstanding, we will block the application's main
	// thread if it does a second IO request until the first one completes.
	// var semaphore = EnsureAsyncActiveSemaphoreInitialized();
	// Task semaphoreTask = null;
	// if (serializeAsynchronously)
	// {
	// semaphoreTask = semaphore.WaitAsync(); // kick off the asynchronous wait, but don't block
	// }
	// else
	// {
	// semaphore.Wait(); // synchronously wait here
	// }

	// Create the task to asynchronously do a Write. This task serves both
	// as the asynchronous work item and as the IAsyncResult returned to the user.
	// var asyncResult = new ReadWriteTask(false /*isRead*/, apm, delegate
	// {
	// The ReadWriteTask stores all of the parameters to pass to Write.
	// As we're currently inside of it, we can get the current task
	// and grab the parameters from it.
	// var thisTask = Task.InternalCurrent as ReadWriteTask;
	// Contract.Assert(thisTask != null, "Inside ReadWriteTask, InternalCurrent should be the ReadWriteTask");

	// try
	// {
	// Do the Write
	// thisTask._stream.Write(thisTask._buffer, thisTask._offset, thisTask._count);
	// return 0; // not used, but signature requires a value be returned
	// }
	// finally
	// {
	// If this implementation is part of Begin/EndXx, then the EndXx method will handle
	// finishing the async operation. However, if this is part of XxAsync, then there won't
	// be an end method, and this task is responsible for cleaning up.
	// if (!thisTask._apm)
	// {
	// thisTask._stream.FinishTrackingAsyncOperation();
	// }

	// thisTask.ClearBeginState(); // just to help alleviate some memory pressure
	// }
	// }, state, this, buffer, offset, count, callback);

	// Schedule it
	// if (semaphoreTask != null)
	// RunReadWriteTaskWhenReady(semaphoreTask, asyncResult);
	// else
	// RunReadWriteTask(asyncResult);

	// return asyncResult; // return it
	// }

	// private void RunReadWriteTaskWhenReady(Task asyncWaiter, ReadWriteTask readWriteTask)
	// {
	// Contract.Assert(readWriteTask != null); // Should be Contract.Requires, but CCRewrite is doing a poor job with
	// preconditions in async methods that await.
	// Contract.Assert(asyncWaiter != null); // Ditto

	// If the wait has already completed, run the task.
	// if (asyncWaiter.IsCompleted)
	// {
	// Contract.Assert(asyncWaiter.IsRanToCompletion, "The semaphore wait should always complete successfully.");
	// RunReadWriteTask(readWriteTask);
	// }
	// else // Otherwise, wait for our turn, and then run the task.
	// {
	// asyncWaiter.ContinueWith((t, state) => {
	// Contract.Assert(t.IsRanToCompletion, "The semaphore wait should always complete successfully.");
	// var rwt = (ReadWriteTask)state;
	// rwt._stream.RunReadWriteTask(rwt); // RunReadWriteTask(readWriteTask);
	// }, readWriteTask, default(CancellationToken), TaskContinuationOptions.ExecuteSynchronously,
	// TaskScheduler.Default);
	// }
	// }

	// private void RunReadWriteTask(ReadWriteTask readWriteTask)
	// {
	// Contract.Requires(readWriteTask != null);
	// Contract.Assert(_activeReadWriteTask == null, "Expected no other readers or writers");

	// Schedule the task. ScheduleAndStart must happen after the write to _activeReadWriteTask to avoid a race.
	// Internally, we're able to directly call ScheduleAndStart rather than Start, avoiding
	// two interlocked operations. However, if ReadWriteTask is ever changed to use
	// a cancellation token, this should be changed to use Start.
	// _activeReadWriteTask = readWriteTask; // store the task so that EndXx can validate it's given the right one
	// readWriteTask.m_taskScheduler = TaskScheduler.Default;
	// readWriteTask.ScheduleAndStart(needsProtection: false);
	// }

	// private void FinishTrackingAsyncOperation()
	// {
	// _activeReadWriteTask = null;
	// Contract.Assert(_asyncActiveSemaphore != null, "Must have been initialized in order to get here.");
	// _asyncActiveSemaphore.Release();
	// }

	// public virtual void EndWrite(IAsyncResult asyncResult)
	// {
	// if (asyncResult==null)
	// throw new NullPointerException("asyncResult");
	// Contract.EndContractBlock();

	// var writeTask = _activeReadWriteTask;
	// if (writeTask == null)
	// {
	// throw new
	// ArgumentException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndWriteCalledMultiple"));
	// }
	// else if (writeTask != asyncResult)
	// {
	// throw new
	// IllegalStateException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndWriteCalledMultiple"));
	// }
	// else if (writeTask._isRead)
	// {
	// throw new
	// ArgumentException(Environment.GetResourceString("InvalidOperation_WrongAsyncResultOrEndWriteCalledMultiple"));
	// }

	// try
	// {
	// writeTask.GetAwaiter().GetResult(); // block until completion, then propagate any exceptions
	// Contract.Assert(writeTask.Status == TaskStatus.RanToCompletion);
	// }
	// finally
	// {
	// FinishTrackingAsyncOperation();
	// }
	// }

	// Task used by BeginRead / BeginWrite to do Read / Write asynchronously.
	// A single instance of this task serves four purposes:
	// 1. The work item scheduled to run the Read / Write operation
	// 2. The state holding the arguments to be passed to Read / Write
	// 3. The IAsyncResult returned from BeginRead / BeginWrite
	// 4. The completion action that runs to invoke the user-provided callback.
	// This last item is a bit tricky. Before the AsyncCallback is invoked, the
	// IAsyncResult must have completed, so we can't just invoke the handler
	// from within the task, since it is the IAsyncResult, and thus it's not
	// yet completed. Instead, we use AddCompletionAction to install this
	// task as its own completion handler. That saves the need to allocate
	// a separate completion handler, it guarantees that the task will
	// have completed by the time the handler is invoked, and it allows
	// the handler to be invoked synchronously upon the completion of the
	// task. This all enables BeginRead / BeginWrite to be implemented
	// with a single allocation.
	// private sealed class ReadWriteTask : Task<int>, ITaskCompletionAction
	// {
	// private final boolean _isRead;
	// private final boolean _apm; // true if this is from Begin/EndXx; false if it's from XxAsync
	// private Stream _stream;
	// private byte [] _buffer;
	// private final int _offset;
	// private final int _count;
	// private AsyncCallback _callback;
	// private ExecutionContext _context;

	// private void ClearBeginState() // Used to allow the args to Read/Write to be made available for GC
	// {
	// _stream = null;
	// _buffer = null;
	// }

	// [SecuritySafeCritical] // necessary for EC.Capture
	// [MethodImpl(MethodImplOptions.NoInlining)]
	// public ReadWriteTask(
	// boolean isRead,
	// boolean apm,
	// Func<object,int> function, object state,
	// Stream stream, byte[] buffer, int offset, int count, AsyncCallback callback) :
	// base(function, state, CancellationToken.None, TaskCreationOptions.DenyChildAttach)
	// {
	// Contract.Requires(function != null);
	// Contract.Requires(stream != null);
	// Contract.Requires(buffer != null);
	// Contract.EndContractBlock();

	// StackCrawlMark stackMark = StackCrawlMark.LookForMyCaller;

	// Store the arguments
	// _isRead = isRead;
	// _apm = apm;
	// _stream = stream;
	// _buffer = buffer;
	// _offset = offset;
	// _count = count;

	// If a callback was provided, we need to:
	// - Store the user-provided handler
	// - Capture an ExecutionContext under which to invoke the handler
	// - Add this task as its own completion handler so that the Invoke method
	// will run the callback when this task completes.
	// if (callback != null)
	// {
	// _callback = callback;
	// _context = ExecutionContext.Capture(ref stackMark,
	// ExecutionContext.CaptureOptions.OptimizeDefaultCase | ExecutionContext.CaptureOptions.IgnoreSyncCtx);
	// base.AddCompletionAction(this);
	// }
	// }

	// [SecurityCritical] // necessary for CoreCLR
	// private static void InvokeAsyncCallback(object completedTask)
	// {
	// var rwc = (ReadWriteTask)completedTask;
	// var callback = rwc._callback;
	// rwc._callback = null;
	// callback(rwc);
	// }

	// [SecurityCritical] // necessary for CoreCLR
	// private static ContextCallback s_invokeAsyncCallback;

	// [SecuritySafeCritical] // necessary for ExecutionContext.Run
	// void ITaskCompletionAction.Invoke(Task completingTask)
	// {
	// Get the ExecutionContext. If there is none, just run the callback
	// directly, passing in the completed task as the IAsyncResult.
	// If there is one, process it with ExecutionContext.Run.
	// var context = _context;
	// if (context == null)
	// {
	// var callback = _callback;
	// _callback = null;
	// callback(completingTask);
	// }
	// else
	// {
	// _context = null;

	// var invokeAsyncCallback = s_invokeAsyncCallback;
	// if (invokeAsyncCallback == null) s_invokeAsyncCallback = invokeAsyncCallback = InvokeAsyncCallback; // benign
	// race condition

	// using(context) ExecutionContext.Run(context, invokeAsyncCallback, this, true);
	// }
	// }

	// boolean ITaskCompletionAction.InvokeMayRunArbitraryCode { get { return true; } }
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public Task WriteAsync(Byte[] buffer, int offset, int count)
	// {
	// return WriteAsync(buffer, offset, count, CancellationToken.None);
	// }

	// [HostProtection(ExternalThreading = true)]
	// [ComVisible(false)]
	// public virtual Task WriteAsync(Byte[] buffer, int offset, int count, CancellationToken cancellationToken)
	// {
	// If cancellation was requested, bail early with an already completed task.
	// Otherwise, return a task that represents the Begin/End methods.
	// return cancellationToken.IsCancellationRequested
	// ? Task.FromCanceled(cancellationToken)
	// : BeginEndWriteAsync(buffer, offset, count);
	// }

	// [System.Security.SecuritySafeCritical]
	// [MethodImplAttribute(MethodImplOptions.InternalCall)]
	// private extern boolean HasOverriddenBeginEndWrite();

	// private Task BeginEndWriteAsync(Byte[] buffer, Int32 offset, Int32 count)
	// {
	// if (!HasOverriddenBeginEndWrite())
	// {
	// If the Stream does not override Begin/EndWrite, then we can take an optimized path
	// that skips an extra layer of tasks / IAsyncResults.
	// return (Task)BeginWriteInternal(buffer, offset, count, null, null, serializeAsynchronously: true, apm: false);
	// }

	// Otherwise, we need to wrap calls to Begin/EndWrite to ensure we use the derived type's functionality.
	// return TaskFactory<VoidTaskResult>.FromAsyncTrim(
	// this, new ReadWriteParameters { Buffer=buffer, Offset=offset, Count=count },
	// (stream, args, callback, state) => stream.BeginWrite(args.Buffer, args.Offset, args.Count, callback, state), //
	// cached by compiler
	// (stream, asyncResult) => // cached by compiler
	// {
	// stream.EndWrite(asyncResult);
	// return default(VoidTaskResult);
	// });
	// }

	public abstract long seek(long offset, SeekOrigin origin);

	public abstract void setLength(long value);

	// public abstract int read(byte[] buffer, int offset, int count);

	// Reads one byte from the stream by calling Read(byte[], int, int).
	// Will return an unsigned byte cast to an int or -1 on end of stream.
	// This implementation does not perform well because it allocates a new
	// byte[] each time you call it, and should be overridden by any
	// subclass that maintains an private buffer. Then, it can help perf
	// significantly for people who are reading one byte at a time.
	public int readByte()
	{
		// Contract.Ensures(Contract.Result<int>() >= -1);
		// Contract.Ensures(Contract.Result<int>() < 256);

		byte[] oneByteArray = new byte[1];
		int r = read(oneByteArray, 0, 1);
		if (r == 0)
			return -1;
		return oneByteArray[0];
	}

	// public abstract void write(byte[] buffer, int offset, int count);

	// Writes one byte from the stream by calling Write(byte[], int, int).
	// This implementation does not perform well because it allocates a new
	// byte[] each time you call it, and should be overridden by any
	// subclass that maintains an private buffer. Then, it can help perf
	// significantly for people who are writing one byte at a time.
	public void writeByte(byte value)
	{
		byte[] oneByteArray = new byte[1];
		oneByteArray[0] = value;
		try
		{
			write(oneByteArray, 0, 1);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// [HostProtection(Synchronization=true)]
	public static Stream getSynchronized(Stream stream)
	{
		if (stream == null)
			throw new NullPointerException("stream");
		// Contract.Ensures(Contract.Result<Stream>() != null);
		// Contract.EndContractBlock();
		if (stream instanceof SyncStream)
			return stream;

		return new SyncStream(stream);
	}

	// private IAsyncResult BlockingBeginRead(byte[] buffer, int offset, int count, AsyncCallback callback, Object
	// state)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);

	// To avoid a race with a stream's position pointer & generating conditions
	// with private buffer indexes in our own streams that
	// don't natively support async IO operations when there are multiple
	// async requests outstanding, we will block the application's main
	// thread and do the IO synchronously.
	// This can't perform well - use a different approach.
	// SynchronousAsyncResult asyncResult;
	// try {
	// int numRead = Read(buffer, offset, count);
	// asyncResult = new SynchronousAsyncResult(numRead, state);
	// }
	// catch (IOException ex) {
	// asyncResult = new SynchronousAsyncResult(ex, state, isWrite: false);
	// }

	// if (callback != null) {
	// callback(asyncResult);
	// }

	// return asyncResult;
	// }

	// private static int BlockingEndRead(IAsyncResult asyncResult)
	// {
	// Contract.Ensures(Contract.Result<int>() >= 0);

	// return SynchronousAsyncResult.EndRead(asyncResult);
	// }

	// private IAsyncResult BlockingBeginWrite(byte[] buffer, int offset, int count, AsyncCallback callback, Object
	// state)
	// {
	// Contract.Ensures(Contract.Result<IAsyncResult>() != null);

	// To avoid a race condition with a stream's position pointer & generating conditions
	// with private buffer indexes in our own streams that
	// don't natively support async IO operations when there are multiple
	// async requests outstanding, we will block the application's main
	// thread and do the IO synchronously.
	// This can't perform well - use a different approach.
	// SynchronousAsyncResult asyncResult;
	// try {
	// Write(buffer, offset, count);
	// asyncResult = new SynchronousAsyncResult(state);
	// }
	// catch (IOException ex) {
	// asyncResult = new SynchronousAsyncResult(ex, state, isWrite: true);
	// }

	// if (callback != null) {
	// callback(asyncResult);
	// }

	// return asyncResult;
	// }

	// private static void BlockingEndWrite(IAsyncResult asyncResult)
	// {
	// SynchronousAsyncResult.EndWrite(asyncResult);
	// }

	private void validateCopyToArguments(Stream destination, int bufferSize)
	{
		if (destination == null)
			throw new NullPointerException("destination");
		if (bufferSize <= 0)
			throw new IllegalArgumentException("bufferSize: ArgumentOutOfRange_NeedPosNum");
		if (!canRead() && !canWrite())
			throw new IllegalStateException("ObjectDisposed_StreamClosed");
		if (!destination.canRead() && !destination.canWrite())
			throw new IllegalStateException("destination: ObjectDisposed_StreamClosed");
		if (!canRead())
			throw new IllegalStateException("NotSupported_UnreadableStream");
		if (!destination.canWrite())
			throw new IllegalStateException("NotSupported_UnwritableStream");
		// Contract.EndContractBlock();
	}

	// [Serializable]
	private static final class NullStream extends Stream
	{
		private NullStream()
		{}

		@Override
		public boolean canRead()
		{
			return true;
		}

		@Override
		public boolean canWrite()
		{
			return true;
		}

		@Override
		public boolean canSeek()
		{
			return true;
		}

		@Override
		public long getLength()
		{
			return 0;
		}

		@Override
		public long getPosition()
		{
			return 0;
		}

		@Override
		public void setPosition(long value)
		{}

		// public override Task CopyToAsync(Stream destination, int bufferSize, CancellationToken cancellationToken)
		// {
		// Validate arguments here for compat, since previously this method
		// was inherited from Stream (which did check its arguments).
		// ValidateCopyToArguments(destination, bufferSize);

		// return cancellationToken.IsCancellationRequested ?
		// Task.FromCanceled(cancellationToken) :
		// Task.CompletedTask;
		// }

		@Override
		protected void dispose(boolean disposing)
		{
			// Do nothing - we don't want NullStream singleton (static) to be closable
		}

		@Override
		public void flush()
		{}

		// [ComVisible(false)]
		// public override Task FlushAsync(CancellationToken cancellationToken)
		// {
		// return cancellationToken.IsCancellationRequested ?
		// Task.FromCanceled(cancellationToken) :
		// Task.CompletedTask;
		// }

		// [HostProtection(ExternalThreading = true)]
		// public override IAsyncResult BeginRead(byte[] buffer, int offset, int count, AsyncCallback callback, Object
		// state)
		// {
		// if (!CanRead) __Error.ReadNotSupported();

		// return BlockingBeginRead(buffer, offset, count, callback, state);
		// }

		// public override int EndRead(IAsyncResult asyncResult)
		// {
		// if (asyncResult == null)
		// throw new NullPointerException("asyncResult");
		// Contract.EndContractBlock();

		// return BlockingEndRead(asyncResult);
		// }

		// [HostProtection(ExternalThreading = true)]
		// public override IAsyncResult BeginWrite(byte[] buffer, int offset, int count, AsyncCallback callback, Object
		// state)
		// {
		// if (!CanWrite) __Error.WriteNotSupported();

		// return BlockingBeginWrite(buffer, offset, count, callback, state);
		// }

		// public override void EndWrite(IAsyncResult asyncResult)
		// {
		// if (asyncResult == null)
		// throw new NullPointerException("asyncResult");
		// Contract.EndContractBlock();

		// BlockingEndWrite(asyncResult);
		// }

		@Override
		public int read(byte[] buffer, int offset, int count)
		{
			return 0;
		}

		// [ComVisible(false)]
		// public override Task<int> ReadAsync(Byte[] buffer, int offset, int count, CancellationToken
		// cancellationToken)
		// {
		// var nullReadTask = s_nullReadTask;
		// if (nullReadTask == null)
		// s_nullReadTask = nullReadTask = new Task<int>(false, 0,
		// (TaskCreationOptions)InternalTaskOptions.DoNotDispose, CancellationToken.None); // benign race condition
		// return nullReadTask;
		// }
		// private static Task<int> s_nullReadTask;

		@Override
		public int readByte()
		{
			return -1;
		}

		@Override
		public void write(byte[] buffer, int offset, int count)
		{}

		// [ComVisible(false)]
		// public override Task WriteAsync(Byte[] buffer, int offset, int count, CancellationToken cancellationToken)
		// {
		// return cancellationToken.IsCancellationRequested ?
		// Task.FromCanceled(cancellationToken) :
		// Task.CompletedTask;
		// }

		@Override
		public void writeByte(byte value)
		{}

		@Override
		public long seek(long offset, SeekOrigin origin)
		{
			return 0;
		}

		@Override
		public void setLength(long length)
		{}

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

	// / <summary>Used as the IAsyncResult object when using asynchronous IO methods on the base Stream class.</summary>
	// private final class SynchronousAsyncResult : IAsyncResult {

	// private final Object _stateObject;
	// private final boolean _isWrite;
	// private ManualResetEvent _waitHandle;
	// private ExceptionDispatchInfo _exceptionInfo;

	// private boolean _endXxxCalled;
	// private Int32 _bytesRead;

	// private SynchronousAsyncResult(Int32 bytesRead, Object asyncStateObject) {
	// _bytesRead = bytesRead;
	// _stateObject = asyncStateObject;
	// _isWrite = false;
	// }

	// private SynchronousAsyncResult(Object asyncStateObject) {
	// _stateObject = asyncStateObject;
	// _isWrite = true;
	// }

	// private SynchronousAsyncResult(Exception ex, Object asyncStateObject, boolean isWrite) {
	// _exceptionInfo = ExceptionDispatchInfo.Capture(ex);
	// _stateObject = asyncStateObject;
	// _isWrite = isWrite;
	// }

	// public boolean IsCompleted {
	// We never hand out objects of this type to the user before the synchronous IO completed:
	// get { return true; }
	// }

	// public WaitHandle AsyncWaitHandle {
	// get {
	// return LazyInitializer.EnsureInitialized(ref _waitHandle, () => new ManualResetEvent(true));
	// }
	// }

	// public Object AsyncState {
	// get { return _stateObject; }
	// }

	// public boolean CompletedSynchronously {
	// get { return true; }
	// }

	// private void ThrowIfError() {
	// if (_exceptionInfo != null)
	// _exceptionInfo.Throw();
	// }

	// private static Int32 EndRead(IAsyncResult asyncResult) {

	// SynchronousAsyncResult ar = asyncResult as SynchronousAsyncResult;
	// if (ar == null || ar._isWrite)
	// __Error.WrongAsyncResult();

	// if (ar._endXxxCalled)
	// __Error.EndReadCalledTwice();

	// ar._endXxxCalled = true;

	// ar.ThrowIfError();
	// return ar._bytesRead;
	// }

	// private static void EndWrite(IAsyncResult asyncResult) {

	// SynchronousAsyncResult ar = asyncResult as SynchronousAsyncResult;
	// if (ar == null || !ar._isWrite)
	// __Error.WrongAsyncResult();

	// if (ar._endXxxCalled)
	// __Error.EndWriteCalledTwice();

	// ar._endXxxCalled = true;

	// ar.ThrowIfError();
	// }
	// } // class SynchronousAsyncResult

	// SyncStream is a wrapper around a stream that takes
	// a lock for every operation making it thread safe.
	// [Serializable]
	private static final class SyncStream extends Stream
	{
		private Stream _stream;

		private SyncStream(Stream stream)
		{
			if (stream == null)
				throw new NullPointerException("stream");
			// Contract.EndContractBlock();
			_stream = stream;
		}

		@Override
		public boolean canRead()
		{
			return _stream.canRead();
		}

		@Override
		public boolean canWrite()
		{
			return _stream.canWrite();
		}

		@Override
		public boolean canSeek()
		{
			return _stream.canSeek();
		}

		// [ComVisible(false)]
		@Override
		public boolean canTimeout()
		{
			return _stream.canTimeout();
		}

		@Override
		public long getLength()
		{
			synchronized (_stream)
			{
				return _stream.getLength();
			}
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

		@Override
		public long getPosition()
		{
			synchronized (_stream)
			{
				return _stream.getPosition();
			}
		}

		@Override
		public void setPosition(long value)
		{
			synchronized (_stream)
			{
				_stream.setPosition(value);
			}
		}

		// [ComVisible(false)]
		@Override
		public int getReadTimeout()
		{
			return _stream.getReadTimeout();
		}

		@Override
		public void setReadTimeout(int value)
		{
			_stream.setReadTimeout(value);
		}

		// [ComVisible(false)]
		@Override
		public int getWriteTimeout()
		{
			return _stream.getWriteTimeout();
		}

		@Override
		public void setWriteTimeout(int value)
		{
			_stream.setWriteTimeout(value);
		}

		// In the off chance that some wrapped stream has different
		// semantics for Close vs. Dispose, let's preserve that.
		@Override
		public void close()
		{
			synchronized (_stream)
			{
				try
				{
					_stream.close();
				}
				finally
				{
					super.dispose(true);
				}
			}
		}

		@Override
		protected void dispose(boolean disposing)
		{
			synchronized (_stream)
			{
				try
				{
					// Explicitly pick up a potentially methodimpl'ed Dispose
					if (disposing)
						try
						{
							((Closeable) _stream).close();
						}
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				finally
				{
					super.dispose(disposing);
				}
			}
		}

		@Override
		public void flush()
		{
			synchronized (_stream)
			{
				try
				{
					_stream.flush();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public int read(byte[] bytes, int offset, int count)
		{
			int result = -1;
			synchronized (_stream)
			{
				result = _stream.read(bytes, offset, count);
			}
			return result;
		}

		@Override
		public int readByte()
		{
			int result = -1;
			synchronized (_stream)
			{
				result = _stream.readByte();
			}
			return result;
		}

		// [HostProtection(ExternalThreading=true)]
		// public override IAsyncResult BeginRead(byte[] buffer, int offset, int count, AsyncCallback callback, Object
		// state)
		// {
		// boolean overridesBeginRead = _stream.HasOverriddenBeginEndRead();

		// synchronized(_stream)
		// {
		// If the Stream does have its own BeginRead implementation, then we must use that override.
		// If it doesn't, then we'll use the base implementation, but we'll make sure that the logic
		// which ensures only one asynchronous operation does so with an asynchronous wait rather
		// than a synchronous wait. A synchronous wait will result in a deadlock condition, because
		// the EndXx method for the outstanding async operation won't be able to acquire the lock on
		// _stream due to this call blocked while holding the lock.
		// return overridesBeginRead ?
		// _stream.BeginRead(buffer, offset, count, callback, state) :
		// _stream.BeginReadInternal(buffer, offset, count, callback, state, serializeAsynchronously: true, apm: true);
		// }
		// }

		// public override int EndRead(IAsyncResult asyncResult)
		// {
		// if (asyncResult == null)
		// throw new NullPointerException("asyncResult");
		// Contract.Ensures(Contract.Result<int>() >= 0);
		// Contract.EndContractBlock();

		// synchronized(_stream)
		// return _stream.EndRead(asyncResult);
		// }

		@Override
		public long seek(long offset, SeekOrigin origin)
		{
			synchronized (_stream)
			{
				return _stream.seek(offset, origin);
			}
		}

		@Override
		public void setLength(long length)
		{
			synchronized (_stream)
			{
				_stream.setLength(length);
			}
		}

		@Override
		public void write(byte[] bytes, int offset, int count)
		{
			synchronized (_stream)
			{
				try
				{
					_stream.write(bytes, offset, count);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void writeByte(byte b)
		{
			synchronized (_stream)
			{
				_stream.writeByte(b);
			}
		}

		// [HostProtection(ExternalThreading=true)]
		// public override IAsyncResult BeginWrite(byte[] buffer, int offset, int count, AsyncCallback callback, Object
		// state)
		// {
		// boolean overridesBeginWrite = _stream.HasOverriddenBeginEndWrite();

		// synchronized(_stream)
		// {
		// If the Stream does have its own BeginWrite implementation, then we must use that override.
		// If it doesn't, then we'll use the base implementation, but we'll make sure that the logic
		// which ensures only one asynchronous operation does so with an asynchronous wait rather
		// than a synchronous wait. A synchronous wait will result in a deadlock condition, because
		// the EndXx method for the outstanding async operation won't be able to acquire the lock on
		// _stream due to this call blocked while holding the lock.
		// return overridesBeginWrite ?
		// _stream.BeginWrite(buffer, offset, count, callback, state) :
		// _stream.BeginWriteInternal(buffer, offset, count, callback, state, serializeAsynchronously: true, apm: true);
		// }
		// }

		// public override void EndWrite(IAsyncResult asyncResult)
		// {
		// if (asyncResult == null)
		// throw new NullPointerException("asyncResult");
		// Contract.EndContractBlock();

		// synchronized(_stream)
		// _stream.EndWrite(asyncResult);
		// }
	}
}
