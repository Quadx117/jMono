package jMono_Framework.content.contentReaders;

import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;
import jMono_Framework.media.Song;
import jMono_Framework.utilities.FileHelpers;
import jMono_Framework.utilities.StringHelpers;

import java.nio.file.Paths;

public class SongReader extends ContentTypeReader<Song>
{
// #if ANDROID
	// static string[] supportedExtensions = new string[] { ".mp3", ".ogg", ".mid" };
// #else
	static String[] supportedExtensions = new String[] { ".mp3" };
// #endif

	protected static String normalize(String fileName)
	{
		return normalize(fileName, supportedExtensions);
	}

	public SongReader()
	{
		super(Song.class);
	}

	@Override
	protected Song read(ContentReader input, Song existingInstance)
	{
		String path = input.readString();

		if (!StringHelpers.isNullOrEmpty(path))
		{
			// Add the ContentManager's RootDirectory
			String dirPath = Paths.get(input.getContentManager().getRootDirectoryFullPath(), input.getAssetName()).toString();

			// Resolve the relative path
			path = FileHelpers.resolveRelativePath(dirPath, path);
		}

		int durationMs = input.readObject();

		return new Song(path, durationMs);
	}
}
