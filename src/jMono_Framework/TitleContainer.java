package jMono_Framework;

import jMono_Framework.utilities.FileHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TitleContainer
{
    // static partial void PlatformInit();

    // NOTE(Eric): Added this since this is a static class in C#
    /**
     * Make sure we can't instantiate this class
     */
    private TitleContainer() {}

    // NOTE(Eric): This block also include code from TitleContainer.Desktop.cs
    static
    {
// #if WINDOWS || DESKTOPGL
// #if DESKTOPGL
        // Check for the package Resources Folder first. This is where the assets
        // will be bundled.
        // if (CurrentPlatform.OS == OS.MacOSX)
        // Location = Path.Combine (AppDomain.CurrentDomain.BaseDirectory, "..", "Resources");
        // if (!Directory.Exists (Location))
// #endif
        // NOTE(Eric): In VisualStudio for FrameworkTest :
        // "C:\\Users\\Eric\\Documents\\Visual Studio 2013\\Projects\\FrameworkTest\\FrameworkTest\\bin\\Windows\\Debug\\"
        // Location = AppDomain.CurrentDomain.BaseDirectory;
        Location = getBaseDirectory();
// #endif
    }

    static private String Location;

    static public String getLocation()
    {
        return Location;
    }

    // NOTE(Eric): This method also include code from TitleContainer.Desktop.cs
    /**
     * Returns an open stream to an existing file in the title storage area.
     * 
     * @param name
     *        The filepath relative to the title storage area.
     * @return A open stream or null if the file is not found.
     */
    public static FileInputStream openStream(String name) throws FileNotFoundException
    {
        if (name == null || name.isEmpty())
            throw new NullPointerException("name");

        // Normalize the file path.
        String safeName = normalizeRelativePath(name);

        // We do not accept absolute paths here.
        Path safeNamePath = Paths.get(safeName);
        if (safeNamePath.isAbsolute())
            throw new IllegalArgumentException("Invalid filename. TitleContainer.openStream requires a relative path.");

        Path absolutePath = Paths.get(Location, safeName);

        // Call the platform code to open the stream. Any errors
        // at this point should result in a file not found.
        FileInputStream stream = null;
        try
        {
            // stream = Files.newInputStream(absolutePath, StandardOpenOption.READ);
            stream = new FileInputStream(absolutePath.toString());
            // if (stream == null)
                // throw fileNotFoundException(name, null);
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw fileNotFoundException(name, e);
        }

        return stream;
    }

    private static FileNotFoundException fileNotFoundException(String name, Exception inner)
    {
        String msg = "Error loading \"" + name + "\". File not found.";
        if (inner != null)
            msg = msg + "\nThe inner exception is:\n" + inner.toString();
        return new FileNotFoundException(msg);
    }

    public static String normalizeRelativePath(String name)
    {
        String path = "";
        try
        {
            URL url = new URL("file:///" + name);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), new String());
            path = uri.getSchemeSpecificPart().substring(3);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return FileHelpers.normalizeFilePathSeparators(path);
    }

    // TODO(Eric): If this is needed elsewhere, put in helper class (Assembly)
    private static String getBaseDirectory()
    {
        // TODO(Eric): Find a better way for this so we don't rely on the location of this class
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
            // since this is where we want to put our Content folder with the resources.
            applicationDir = new File(applicationDir).getParentFile().getParent();
        }

        return applicationDir;
    }

}
