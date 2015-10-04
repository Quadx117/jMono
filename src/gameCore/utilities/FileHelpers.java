package gameCore.utilities;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class FileHelpers
{
	public static final char ForwardSlash = '/';
	public static final String ForwardSlashString = String.valueOf(ForwardSlash);
	public static final char BackwardSlash = '\\';

	// #if WINRT
	// public static final char NotSeparator = ForwardSlash;
	// public static final char Separator = BackwardSlash;
	// #else
	public static final char NotSeparator = File.separatorChar == BackwardSlash ? ForwardSlash : BackwardSlash;
	public static final char Separator = File.separatorChar;

	// #endif

	public static String normalizeFilePathSeparators(String name)
	{
		return name.replace(NotSeparator, Separator);
	}

	// / <summary>
	// / Combines the filePath and relativeFile based on relativeFile being a file in the same
	// location as filePath.
	// / Relative directory operators (..) are also resolved
	// / </summary>
	// / <example>"A\B\C.txt","D.txt" becomes "A\B\D.txt"</example>
	// / <example>"A\B\C.txt","..\D.txt" becomes "A\D.txt"</example>
	// / <param name="filePath">Path to the file we are starting from</param>
	// / <param name="relativeFile">Relative location of another file to resolve the path to</param>
	public static String resolveRelativePath(String filePath, String relativeFile)
	{
		// Uri accepts forward slashes
		filePath = filePath.replace(BackwardSlash, ForwardSlash);

		boolean hasForwardSlash = filePath.startsWith(ForwardSlashString);
		if (!hasForwardSlash)
			filePath = ForwardSlashString + filePath;

		// Get a uri for filePath using the file:// schema and no host.
		URI src = null;
		URI dst = null;

		try
		{
			src = new URI("file://" + filePath);
			dst = new URI(src + relativeFile);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		// The uri now contains the path to the relativeFile with
		// relative addresses resolved... get the local path.
		String localPath = dst.getSchemeSpecificPart();  // dst.LocalPath

		if (!hasForwardSlash && localPath.startsWith("/"))
			localPath = localPath.substring(1);

		// Convert the directory separator characters to the
		// correct platform specific separator.
		return normalizeFilePathSeparators(localPath);
	}
}
