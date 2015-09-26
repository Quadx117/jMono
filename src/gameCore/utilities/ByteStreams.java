package gameCore.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides utility methods for working with byte arrays and I/O streams.
 * This class is taken from google's guava api.
 * 
 * @see https://raw.githubusercontent.com/google/guava/master/guava/src/com/google/common/io/ByteStreams.java
 * @author Eric
 *
 */
public final class ByteStreams {

	private static final int BUF_SIZE = 8192;

	private ByteStreams() {}

	/**
	 * Copies all bytes from the input stream to the output stream.
	 * Does not close or flush either stream.
	 *
	 * @param from
	 *        the input stream to read from
	 * @param to
	 *        the output stream to write to
	 * @return the number of bytes copied
	 * @throws IOException
	 *         if an I/O error occurs
	 */
	public static long copy(InputStream from, OutputStream to) throws IOException {
		if (from == null) {
		      throw new NullPointerException("from cannot be null");
		}
		if (to == null) {
			throw new NullPointerException("to cannot be null");
		}
		
		byte[] buf = new byte[BUF_SIZE];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}
}
