package gameCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TitleContainer
{
	/**
	 * Make sure we can't instantiate this class
	 */
	private TitleContainer() {}

	static
	{
// #if WINDOWS || DESKTOPGL
		// NOTE: In VisualStudio for FrameworkTest :
		// "C:\\Users\\Eric\\Documents\\Visual Studio 2013\\Projects\\FrameworkTest\\FrameworkTest\\bin\\Windows\\Debug\\"
		// Location = AppDomain.CurrentDomain.BaseDirectory;
		Location = getBaseDirectory();
// #elif WINRT
		// Location = Windows.ApplicationModel.Package.Current.InstalledLocation.Path;
// #elif IOS || MONOMAC
		// Location = NSBundle.MainBundle.ResourcePath;
// #else
		// Location = ""; // string.Empty;
// #endif

// #if IOS
		// SupportRetina = UIScreen.MainScreen.Scale >= 2.0f;
		// RetinaScale = (int)Math.Round(UIScreen.MainScreen.Scale);
// #endif
	}

	static private String Location;
	static public String getLocation() { return Location; }

// #if IOS
	// static private boolean SupportRetina;
	// static public boolean supportRetina()
	// { return SupportRetina; }
	// static private int RetinaScale;
	// static public int getRetinaScale()
	// { return RetinaScale; }
// #endif

// #if WINRT

	// private static async Task<Stream> openStreamAsync(String name)
	// {
	// var package = Windows.ApplicationModel.Package.Current;
	//
	// try
	// {
	// var storageFile = await package.InstalledLocation.GetFileAsync(name);
	// var randomAccessStream = await storageFile.OpenReadAsync();
	// return randomAccessStream.AsStreamForRead();
	// }
	// catch (IOException)
	// {
	// // The file must not exist... return a null stream.
	// return null;
	// }
	// }

// #endif // WINRT

	// / <summary>
	// / Returns an open stream to an exsiting file in the title storage area.
	// / </summary>
	// / <param name="name">The filepath relative to the title storage area.</param>
	// / <returns>A open stream or null if the file is not found.</returns>
	public static FileInputStream openStream(String name)
	{
		// Normalize the file path.
		String safeName = getFilename(name);
		Path safeNamePath = Paths.get(safeName);

		// We do not accept absolute paths here.
		if (safeNamePath.isAbsolute())
			throw new IllegalArgumentException("Invalid filename. TitleContainer.openStream requires a relative path.");

// #if WINRT
		// var stream = Task.Run( () => OpenStreamAsync(safeName).Result ).Result;
		// if (stream == null)
		// throw new FileNotFoundException(name);

		// return stream;
// #elif ANDROID
		// return Android.App.Application.Context.Assets.Open(safeName);
// #elif IOS
		// var absolutePath = Path.Combine(Location, safeName);
		// if (SupportRetina)
		// {
		// for (var scale = RetinaScale; scale >= 2; scale--)
		// {
		// // Insert the @#x immediately prior to the extension. If this file exists
		// // and we are on a Retina device, return this file instead.
		// var absolutePathX = Path.Combine(Path.GetDirectoryName(absolutePath),
		// Path.GetFileNameWithoutExtension(absolutePath)
		// + "@" + scale + "x" + Path.GetExtension(absolutePath));
		// if (File.Exists(absolutePathX))
		// return File.OpenRead(absolutePathX);
		// }
		// }
		// return File.OpenRead(absolutePath);
// #else
		Path absolutePath = Paths.get(Location, safeName);

		FileInputStream result = null;
		try
		{
			// result = Files.newInputStream(absolutePath, StandardOpenOption.READ);
			result = new FileInputStream(absolutePath.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
// #endif
	}

	// TODO: This is just path normalization. Remove this
	// and replace it with a proper utility function. I suspect
	// this same logic is duplicated all over the code base.
	public static String getFilename(String name)
	{
		String result = "";
		try
		{
			URI a = new URI("file:///" + name);
			// result = FileHelpers.normalizeFilePathSeparators(a.getSchemeSpecificPart().substring(1));
			result = a.getSchemeSpecificPart().substring(3);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	// TODO: If this is needed elsewhere, put in helper class (Assembly)
	private static String getBaseDirectory()
	{
		String applicationDir = TitleContainer.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		// TODO: Can we make it work for .jar files with content packaged in it ?
		if (applicationDir.endsWith(".exe"))
		{
			applicationDir = new File(applicationDir).getParent();
		}
		else
		{
			// Add the path to the class files
			applicationDir += TitleContainer.class.getName().replace('.', '/');

			// Step two level up as we are only interested in the bin directory
			// since this is where we want to put our res folder with the content.
			applicationDir = new File(applicationDir).getParentFile().getParent();
		}

		return applicationDir;
	}

}
