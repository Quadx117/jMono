package gameCore.utilities;

public class Hash {

	/**
	 * Compute a hash from a byte array.
	 * 
	 * <p>
	 * Modified FNV Hash in C# http://stackoverflow.com/a/468084
	 * 
	 * @param data
	 *        The date for which to compute the hash.
	 * @return The computed hash value.
	 */
	public static int computeHash(byte[] data) {
		final int p = 16777619;
		int hash = (int) 166136261;

		for (int i = 0; i < data.length; ++i)
			hash = (hash ^ data[i]) * p;

		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return hash;
	}

	// TODO: finish this method
	// / <summary>
	// / Compute a hash from the content of a stream and restore the position.
	// / </summary>
	// / <remarks>
	// / Modified FNV Hash in C#
	// / http://stackoverflow.com/a/468084
	// / </remarks>
	// public static int computeHash(Stream stream)
	// {
	// System.Diagnostics.Debug.Assert(stream.CanSeek);
	//
	// final int p = 16777619;
	// int hash = (int)2166136261;
	//
	// var prevPosition = stream.Position;
	// stream.Position = 0;
	//
	// var data = new byte[1024];
	// int length;
	// while((length = stream.Read(data, 0, data.Length)) != 0)
	// {
	// for (var i = 0; i < length; i++)
	// hash = (hash ^ data[i]) * p;
	// }
	//
	// // Restore stream position.
	// stream.Position = prevPosition;
	//
	// hash += hash << 13;
	// hash ^= hash >> 7;
	// hash += hash << 3;
	// hash ^= hash >> 17;
	// hash += hash << 5;
	// return hash;
	// }
}
