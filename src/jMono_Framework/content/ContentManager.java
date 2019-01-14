package jMono_Framework.content;

import jMono_Framework.TitleContainer;
import jMono_Framework.dotNet.IServiceProvider;
import jMono_Framework.dotNet.io.BinaryReader;
import jMono_Framework.dotNet.io.MemoryStream;
import jMono_Framework.dotNet.io.Stream;
import jMono_Framework.graphics.GraphicsResource;
import jMono_Framework.graphics.IGraphicsDeviceService;
import jMono_Framework.utilities.StringHelpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ContentManager implements AutoCloseable
{
    final byte ContentCompressedLzx = (byte) 0x80;
    final byte ContentCompressedLz4 = 0x40;

    private String _rootDirectory = "";
    private IServiceProvider serviceProvider;
    private IGraphicsDeviceService graphicsDeviceService;
    private Map<String, Object> loadedAssets = new HashMap<String, Object>();
    private List<AutoCloseable> disposableAssets = new ArrayList<AutoCloseable>();
    private boolean disposed;
    private byte[] scratchBuffer;

    private static Object ContentManagerLock = new Object();
    private static List<WeakReference<?>> ContentManagers = new ArrayList<WeakReference<?>>();

    private static List<Character> targetPlatformIdentifiers = new ArrayList<Character>();
    static
    {
        targetPlatformIdentifiers.add('w'); // Windows (DirectX)
        targetPlatformIdentifiers.add('x'); // Xbox360
        targetPlatformIdentifiers.add('i'); // iOS
        targetPlatformIdentifiers.add('a'); // Android
        targetPlatformIdentifiers.add('d'); // DesktopGL
        targetPlatformIdentifiers.add('X'); // MacOSX
        targetPlatformIdentifiers.add('W'); // WindowsStoreApp
        targetPlatformIdentifiers.add('n'); // NativeClient
        targetPlatformIdentifiers.add('M'); // WindowsPhone8
        targetPlatformIdentifiers.add('r'); // RaspberryPi
        targetPlatformIdentifiers.add('P'); // PlayStation4
        targetPlatformIdentifiers.add('v'); // PSVita
        targetPlatformIdentifiers.add('O'); // XboxOne

        // NOTE: There are additional idenfiers for consoles that
        // are not defined in this repository. Be sure to ask the
        // console port maintainers to ensure no collisions occur.

        // Legacy identifiers... these could be reused in the
        // future if we feel enough time has passed.

        // Old WindowsGL and Linux platform chars
        targetPlatformIdentifiers.add('p'); // PlayStationMobile
        targetPlatformIdentifiers.add('g'); // Windows (OpenGL)
        targetPlatformIdentifiers.add('l'); // Linux

        // Allow any per-platform static initialization to occur.
        // NOTE(Eric): Unused in MonoGame 3.6
        // platformStaticInit();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void addContentManager(ContentManager contentManager)
    {
        synchronized (ContentManagerLock)
        {
            // Check if the list contains this content manager already. Also take
            // the opportunity to prune the list of any finalized content managers.
            boolean contains = false;
            for (int i = ContentManagers.size() - 1; i >= 0; --i)
            {
                // TODO(Eric): test this to make sure it works
                WeakReference<?> contentRef = ContentManagers.get(i);
                if (contentRef.get() == contentManager)
                    contains = true;
                if (contentRef.get() == null) // C# !contentRef.IsAlive
                    ContentManagers.remove(i);
            }
            if (!contains)
                ContentManagers.add(new WeakReference(contentManager));
        }
    }

    private static void removeContentManager(ContentManager contentManager)
    {
        synchronized (ContentManagerLock)
        {
            // Check if the list contains this content manager and remove it. Also
            // take the opportunity to prune the list of any finalized content managers.
            for (int i = ContentManagers.size() - 1; i >= 0; --i)
            {
                WeakReference<?> contentRef = ContentManagers.get(i);
                if (contentRef.get() == null || contentRef.get() == contentManager)
                    ContentManagers.remove(i);
            }
        }
    }

    protected static void reloadGraphicsContent()
    {
        synchronized (ContentManagerLock)
        {
            // Reload the graphic assets of each content manager. Also take the
            // opportunity to prune the list of any finalized content managers.
            for (int i = ContentManagers.size() - 1; i >= 0; --i)
            {
                WeakReference<?> contentRef = ContentManagers.get(i);
                if (contentRef.get() != null)
                {
                    ContentManager contentManager = (ContentManager) contentRef.get();
                    if (contentManager != null)
                        contentManager.reloadGraphicsAssets();
                }
                else
                {
                    ContentManagers.remove(i);
                }
            }
        }
    }

    // Use C# destructor syntax for finalization code.
    // This destructor will run only if the Dispose method
    // does not get called.
    // It gives your base class the opportunity to finalize.
    // Do not provide destructors in types derived from this class.
    @Override
    public void finalize()
    {
        // Do not re-create Dispose clean-up code here.
        // Calling Dispose(false) is optimal in terms of
        // readability and maintainability.
        dispose(false);
    }

    public ContentManager(IServiceProvider serviceProvider)
    {
        if (serviceProvider == null)
        {
            throw new NullPointerException("serviceProvider");
        }
        this.serviceProvider = serviceProvider;
        addContentManager(this);
    }

    public ContentManager(IServiceProvider serviceProvider, String rootDirectory)
    {
        if (serviceProvider == null)
        {
            throw new NullPointerException("serviceProvider");
        }
        if (rootDirectory == null)
        {
            throw new NullPointerException("rootDirectory");
        }
        this._rootDirectory = rootDirectory;
        this.serviceProvider = serviceProvider;
        addContentManager(this);
    }

    public void close()
    {
        dispose(true);
        // Tell the garbage collector not to call the finalizer
        // since all the cleanup will already be done.
        // GC.SuppressFinalize(this);
        // Once disposed, content manager wont be used again
        removeContentManager(this);
    }

    // If disposing is true, it was called explicitly and we should dispose managed objects.
    // If disposing is false, it was called by the finalizer and managed objects should not be
    // disposed.
    protected void dispose(boolean disposing)
    {
        if (!disposed)
        {
            if (disposing)
            {
                unload();
            }
            disposed = true;
        }
    }

    // NOTE(Eric): I had to add a parameter for the type because of Java's type erasure
    @SuppressWarnings("unchecked")
    public <T> T load(String assetName, Class<?> type)
    {
        if (StringHelpers.isNullOrEmpty(assetName))
        {
            throw new NullPointerException("assetName");
        }
        if (disposed)
        {
            throw new RuntimeException("ContentManager is disposed");
        }

        T result = null;

        // On some platforms, name and slash direction matter.
        // We store the asset by a /-seperating key rather than how the
        // path to the file was passed to us to avoid
        // loading "content/asset1.xnb" and "content\\ASSET1.xnb" as if they were two
        // different files. This matches stock XNA behavior.
        // The dictionary will ignore case differences
        String key = assetName.replace('\\', '/');

        // Check for a previously loaded asset first
        Object asset = loadedAssets.get(key);
        if (asset != null)
        {
            if (type.isAssignableFrom(asset.getClass()))
            {
                return (T) asset;
            }
        }

        // Load the asset.
        result = readAsset(assetName, null, type);

        loadedAssets.put(key, result);
        return result;
    }

    protected Stream openStream(String assetName) throws ContentLoadException
    {
        Stream stream = null;
        try
        {
            Path assetPath = Paths.get(_rootDirectory, assetName + ".xnb");

            // This is primarily for editor support.
            // Setting the RootDirectory to an absolute path is useful in editor
            // situations, but TitleContainer can ONLY be passed relative paths.
// #if DESKTOPGL || MONOMAC || WINDOWS
            if (assetPath.isAbsolute())
                // stream = Files.newInputStream(assetPath, StandardOpenOption.READ);
                // stream = new FileInputStream(assetPath.toString());
                stream = new MemoryStream(Files.readAllBytes(assetPath));
            else
// #endif
            {
                // TODO(Eric): Should I create my own FileStream extends Stream instead ?
                FileInputStream fis = TitleContainer.openStream(assetPath.toString().replace('\\', '/'));
// #if ANDROID
                // Read the asset into memory in one go. This results in a ~50% reduction
                // in load times on Android due to slow Android asset streams.
                long size = fis.getChannel().size();
                if (size > Integer.MAX_VALUE)
                    throw new ContentLoadException("File is to big");
                byte[] data = new byte[(int) size];
                int bytesRead = fis.read(data);
                fis.close();
                if (bytesRead != size)
                    throw new ContentLoadException("Unable to read the whole file");
                stream = new MemoryStream(data);

                // TODO(Eric): Original code in MonoGame 3.6
                // MemoryStream memStream = new MemoryStream();
                // stream.CopyTo(memStream);
                // memStream.Seek(0, SeekOrigin.Begin);
                // stream.Close();
                // stream = memStream;
// #endif
            }
        }
        catch (FileNotFoundException fileNotFound)
        {
            throw new ContentLoadException("The content file was not found.", fileNotFound);
        }
        catch (IOException io)
        {
            throw new ContentLoadException("The content file was not found.", io);
        }
// #if !WINRT
        // catch (DirectoryNotFoundException directoryNotFound)
        // {
        // throw new ContentLoadException("The directory was not found.", directoryNotFound);
        // }
// #endif
        catch (Exception exception)
        {
            throw new ContentLoadException("Opening stream error.", exception);
        }
        return stream;
    }

    @SuppressWarnings("unchecked")
    protected <T> T readAsset(String assetName, Consumer<AutoCloseable> recordDisposableObject, Class<?> type)
    {
        if (StringHelpers.isNullOrEmpty(assetName))
        {
            throw new NullPointerException("assetName");
        }
        if (disposed)
        {
            throw new RuntimeException("ContentManager is disposed");
        }

        String originalAssetName = assetName;
        Object result = null;

        if (this.graphicsDeviceService == null)
        {
            // TODO(Eric): Can I simply cast it or should I use As.as()
            this.graphicsDeviceService = (IGraphicsDeviceService) serviceProvider.getService(IGraphicsDeviceService.class);
            if (this.graphicsDeviceService == null)
            {
                throw new IllegalStateException("No Graphics Device Service");
            }
        }

        // Try to load as XNB file
        Stream stream = openStream(assetName);
        try (BinaryReader xnbReader = new BinaryReader(stream))
        {
            try (ContentReader reader = getContentReaderFromXnb(assetName, stream, xnbReader, recordDisposableObject))
            {
                result = reader.readAsset();
                if (result instanceof GraphicsResource)
                    ((GraphicsResource) result).name = originalAssetName;
            }
        }

        if (result == null)
            throw new ContentLoadException("Could not load " + originalAssetName + " asset!");

        return (T) result;
    }

    private ContentReader getContentReaderFromXnb(String originalAssetName, final Stream stream,
                                                  BinaryReader xnbReader, Consumer<AutoCloseable> recordDisposableObject)
    {
        // The first 4 bytes should be the "XNB" header. We use that to detect an invalid file
        byte x = xnbReader.readByte();
        byte n = xnbReader.readByte();
        byte b = xnbReader.readByte();
        byte platform = xnbReader.readByte();

        if (x != 'X' || n != 'N' || b != 'B' ||
            !(targetPlatformIdentifiers.contains((char) platform)))
        {
            throw new ContentLoadException("Asset does not appear to be a valid XNB file. Did you process your content for Windows?");
        }

        byte version = xnbReader.readByte();
        byte flags = xnbReader.readByte();

        boolean compressedLzx = (flags & ContentCompressedLzx) != 0;
        boolean compressedLz4 = (flags & ContentCompressedLz4) != 0;
        if (version != 5 && version != 4)
        {
            throw new ContentLoadException("Invalid XNB version");
        }

        // The next int32 is the length of the XNB file
        int xnbLength = xnbReader.readInt32();

        Stream decompressedStream = null;
        if (compressedLzx || compressedLz4)
        {
            // Decompress the xnb
            int decompressedSize = xnbReader.readInt32();

            if (compressedLzx)
            {
                int compressedSize = xnbLength - 14;
                // TODO(Eric): Decompression
                // decompressedStream = new LzxDecoderStream(stream, decompressedSize,
                // compressedSize);
            }
            else if (compressedLz4)
            {
                // TODO(Eric): Decompression
                // decompressedStream = new Lz4DecoderStream(stream);
            }
        }
        else
        {
            decompressedStream = stream;
        }

        ContentReader reader = new ContentReader(this, decompressedStream, this.graphicsDeviceService.getGraphicsDevice(),
                                                 originalAssetName, version, recordDisposableObject);

        return reader;
    }

    protected void recordDisposable(AutoCloseable disposable)
    {
        assert (disposable != null) : "The disposable is null!";

        // Avoid recording disposable objects twice. ReloadAsset will try to record the disposables
        // again.
        // We don't know which asset recorded which disposable so just guard against storing
        // multiple of the same instance.
        if (!disposableAssets.contains(disposable))
            disposableAssets.add(disposable);
    }

    // / <summary>
    // / Virtual property to allow a derived ContentManager to have it's assets reloaded
    // / </summary>
    protected Map<String, Object> getLoadedAssets()
    {
        return loadedAssets;
    }

    protected void reloadGraphicsAssets()
    {
        for (Map.Entry<String, Object> asset : loadedAssets.entrySet())
        {
            // This never executes as asset.Key is never null. This just forces the
            // linker to include the ReloadAsset function when AOT compiled.
            if (asset.getKey() == null)
                reloadAsset(asset.getKey(), Convert.ChangeType(asset.getValue(), asset.getValue().getClass()));

// #if WINDOWS_STOREAPP || WINDOWS_UAP
            // var methodInfo =
            // typeof(ContentManager).GetType().GetTypeInfo().GetDeclaredMethod("ReloadAsset");
// #else
            // var methodInfo = typeof(ContentManager).GetMethod("ReloadAsset",
            // BindingFlags.NonPublic | BindingFlags.Instance);
            var methodInfo = ContentManager.class.GetMethod("ReloadAsset", BindingFlags.NonPublic | BindingFlags.Instance);
// #endif
            var genericMethod = methodInfo.MakeGenericMethod(asset.Value.GetType());
            genericMethod.Invoke(this,
                                 new Object[] { asset.getKey(), Convert.ChangeType(asset.getValue(), asset.getValue().getClass()) });
        }
    }

    protected <T> void reloadAsset(String originalAssetName, T currentAsset, Class<?> type)
    {
        String assetName = originalAssetName;
        if (StringHelpers.isNullOrEmpty(assetName))
        {
            throw new NullPointerException("assetName");
        }
        if (disposed)
        {
            throw new RuntimeException("ContentManager");
        }

        if (this.graphicsDeviceService == null)
        {
            // TODO(Eric): Can I simply cast it or should I use As.as()
            this.graphicsDeviceService = (IGraphicsDeviceService) serviceProvider.getService(IGraphicsDeviceService.class);
            if (this.graphicsDeviceService == null)
            {
                throw new IllegalStateException("No Graphics Device Service");
            }
        }

        Stream stream = openStream(assetName);
        try (BinaryReader xnbReader = new BinaryReader(stream))
        {
            try (ContentReader reader = getContentReaderFromXnb(assetName, stream, xnbReader, null))
            {
                // TODO(Eric): reader.ReadAsset<T>(currentAsset); in ContentReader
                reader.readAsset(currentAsset);
            }
        }
    }

    public void unload()
    {
        // Look for disposable assets.
        for (AutoCloseable disposable : disposableAssets)
        {
            if (disposable != null)
                try
                {
                    disposable.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
        disposableAssets.clear();
        loadedAssets.clear();
    }

    public String getRootDirectory()
    {
        return _rootDirectory;
    }

    public void setRootDirectory(String value)
    {
        _rootDirectory = value;
    }

    // TODO(Eric): Do I want this as a String or a Path ?
    public String getRootDirectoryFullPath()
    {
        return Paths.get(TitleContainer.getLocation(), getRootDirectory()).toString();
    }

    public IServiceProvider getServiceProvider()
    {
        return this.serviceProvider;
    }

    public byte[] getScratchBuffer(int size)
    {
        size = Math.max(size, 1024 * 1024);
        if (scratchBuffer == null || scratchBuffer.length < size)
            scratchBuffer = new byte[size];
        return scratchBuffer;
    }

}
