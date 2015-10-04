package gameCore.content;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentTypeReader<T>
{
	private Class<?> targetType;
// #if ANDROID
	// Keep this static so we only call Game.Activity.Assets.List() once
	// No need to call it for each file if the list will never change.
	// We do need one file list per folder though.
	static Map<String, String[]> filesInFolders = new HashMap<String, String[]>();

// #endif

	public boolean canDeserializeIntoExistingObject()
	{
		return false;
	}

	public Class<?> getTargetType()
	{
		return this.targetType;
	}

	public int getTypeVersion()
	{
		return 0;	// The default version (unless overridden) is zero
	}

	protected ContentTypeReader(Class<?> targetType)
	{
		this.targetType = targetType;
	}

	protected void initialize(ContentTypeReaderManager manager)
	{
		// Do nothing. Are we supposed to add ourselves to the manager?
	}

	protected abstract T read(ContentReader input, T existingInstance);

// #if ANDROID
	// protected static String normalize(String fileName, String[] extensions)
	// {
	// int index = fileName.LastIndexOf(Path.DirectorySeparatorChar);
	// String path = "";
	// String file = fileName;
	// if (index >= 0)
	// {
	// file = fileName.substring(index + 1, fileName.length() - index - 1);
	// path = fileName.substring(0, index);
	// }

	// // Only read the assets file list once
	// String[] files = null;
	// if (!filesInFolders.TryGetValue(path, out files))
	// {
	// files = Android.App.Application.Context.Assets.List(path);
	// filesInFolders[path] = files;
	// }

	// if (files.Any(s => s == file))
	// return fileName;

	// FirstOrDefault returns null as the default if the file is not found. This crashed
	// Path.Combine so check
	// for it first.
	// String file2 = files.FirstOrDefault(s => extensions.Any(ext => s.ToLowerInvariant() ==
	// (file.ToLowerInvariant() + ext)));
	// if (String.IsNullOrEmpty(file2))
	// return null;
	// return Path.Combine(path, file2);
	// }
// #else
	public static String normalize(String fileName, String[] extensions)
	{
   // #if WINRT
		// if (MetroHelper.AppDataFileExists(fileName))
		// return fileName;
   // #else
		File f = new File(fileName);
		if (f.isFile())
			return fileName;
   // #endif

		for (String ext : extensions)
		{
			// Concat the file name with valid extensions
			String fileNamePlusExt = fileName + ext;

   // #if WINRT
			// if (MetroHelper.AppDataFileExists(fileNamePlusExt))
			// return fileNamePlusExt;
   // #else
			f = new File(fileNamePlusExt);
			if (f.isFile())
				return fileNamePlusExt;
   // #endif
		}

		return null;
	}
// #endif
}

// TODO: Find a way to mimic that in Java
/*
 * public abstract class ContentTypeReader<T> : ContentTypeReader
 * {
 * protected ContentTypeReader()
 * {
 * super(typeof(T));
 * }
 * 
 * @Override
 * protected Object read(ContentReader input, Object existingInstance)
 * {
 * // as per the documentation
 * http://msdn.microsoft.com/en-us/library/microsoft.xna.framework.content
 * .contenttypereader.read.aspx
 * // existingInstance
 * // The object receiving the data, or null if a new instance of the object should be created.
 * if (existingInstance == null) {
 * return this.read (input, default(T));
 * }
 * else {
 * return this.read (input, (T)existingInstance);
 * }
 * 
 * //return Read(input, (T)existingInstance);
 * }
 * 
 * protected abstract T read(ContentReader input, T existingInstance);
 * 
 * }
 * }
 */
