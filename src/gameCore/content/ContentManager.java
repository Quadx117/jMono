package gameCore.content;

import gameCore.TitleContainer;
import gameCore.content.contentReaders.SpriteFontReader;
import gameCore.content.contentReaders.Texture2DReader;
import gameCore.dotNet.BinaryReader;
import gameCore.dotNet.IServiceProvider;
import gameCore.graphics.GraphicsResource;
import gameCore.graphics.IGraphicsDeviceService;
import gameCore.graphics.SpriteFont;
import gameCore.graphics.Texture;
import gameCore.graphics.Texture2D;
import gameCore.graphics.effect.Effect;
import gameCore.utilities.StringHelpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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
	// private Map<String, Object> loadedAssets = new HashMap<String,
	// Object>(StringComparer.OrdinalIgnoreCase);
	private Map<String, Object> loadedAssets = new HashMap<String, Object>();
	private List<AutoCloseable> disposableAssets = new ArrayList<AutoCloseable>();
	private boolean disposed;

	private static Object ContentManagerLock = new Object();
	private static List<WeakReference<?>> ContentManagers = new ArrayList<WeakReference<?>>();

	static List<Character> targetPlatformIdentifiers = new ArrayList<Character>();
	static
	{
		targetPlatformIdentifiers.add('w'); // Windows (DirectX)
		targetPlatformIdentifiers.add('x'); // Xbox360
		targetPlatformIdentifiers.add('m'); // WindowsPhone
		targetPlatformIdentifiers.add('i'); // iOS
		targetPlatformIdentifiers.add('a'); // Android
		targetPlatformIdentifiers.add('d'); // DesktopGL
		targetPlatformIdentifiers.add('X'); // MacOSX
		targetPlatformIdentifiers.add('W'); // WindowsStoreApp
		targetPlatformIdentifiers.add('n'); // NativeClient
		targetPlatformIdentifiers.add('u'); // Ouya
		targetPlatformIdentifiers.add('p'); // PlayStationMobile
		targetPlatformIdentifiers.add('M'); // WindowsPhone8
		targetPlatformIdentifiers.add('r'); // RaspberryPi
		targetPlatformIdentifiers.add('P'); // PlayStation4

		// Old WindowsGL and Linux platform chars
		targetPlatformIdentifiers.add('w');
		targetPlatformIdentifiers.add('l');
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
				// TODO: test this to make sure it works
				WeakReference<?> contentRef = ContentManagers.get(i);
				// if (!contentRef.IsAlive)
				if (contentRef.get() == null)
				{
					ContentManagers.remove(i);
				}
				else if (contentRef.get() == contentManager)
				{
					contains = true;
				}
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
				// if (!contentRef.IsAlive || ReferenceEquals(contentRef.Target, contentManager))
				if (contentRef == null || contentRef.get() == contentManager)
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
				// if (contentRef.IsAlive)
				if (contentRef != null)
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

	// TODO: Delete this comment when done testing
	// NOTE: I had to add a parameter for the type because of Java's type erasure
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

	protected FileInputStream openStream(String assetName) throws ContentLoadException
	{
		FileInputStream stream = null;
		try
		{
			Path assetPath = Paths.get(_rootDirectory, assetName + ".xnb");

			// This is primarily for editor support.
			// Setting the RootDirectory to an absolute path is useful in editor
			// situations, but TitleContainer can ONLY be passed relative paths.
// #if DESKTOPGL || MONOMAC || WINDOWS
			if (assetPath.isAbsolute())
				// stream = Files.newInputStream(assetPath, StandardOpenOption.READ);
				stream = new FileInputStream(assetPath.toString());
			else
// #endif
				stream = TitleContainer.openStream(assetPath.toString().replace('\\', '/'));
// #if ANDROID
			// Read the asset into memory in one go. This results in a ~50% reduction
			// in load times on Android due to slow Android asset streams.
//			ByteArrayInputStream memStream = new ByteArrayInputStream();
//			stream.CopyTo(memStream);
//			memStream.Seek(0, SeekOrigin.Begin);
//			stream.close();
//			stream = memStream;
// #endif
		}
		catch (IOException io)
		{
			throw new ContentLoadException("The content file was not found.", io);
		}
//		catch (FileNotFoundException fileNotFound)
//		{
//			throw new ContentLoadException("The content file was not found.", fileNotFound);
//		}
// #if !WINRT
//		catch (DirectoryNotFoundException directoryNotFound)
//		{
//			throw new ContentLoadException("The directory was not found.", directoryNotFound);
//		}
// #endif
//		catch (Exception exception)
//		{
//			throw new ContentLoadException("Opening stream error.", exception);
//		}
		return stream;
	}

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
			// TODO: Can I simply cast it or should I use As.as()
			this.graphicsDeviceService = (IGraphicsDeviceService) serviceProvider.getService(IGraphicsDeviceService.class);
			if (this.graphicsDeviceService == null)
			{
				throw new IllegalStateException("No Graphics Device Service");
			}
		}
		
		FileInputStream stream = null;
		try
        {
			//try load it traditionally
			stream = openStream(assetName);

            // Try to load as XNB file
            try
            {
                try (BinaryReader xnbReader = new BinaryReader(stream))
                {
                    try (ContentReader reader = getContentReaderFromXnb(assetName, stream, xnbReader, recordDisposableObject))
                    {
                        result = reader.readAsset();
                        // if (result is GraphicsResource)
                        if (result instanceof GraphicsResource)
                            ((GraphicsResource)result).name = originalAssetName;
                    }
                }
				catch (IOException e)
				{
					e.printStackTrace();
				}
            }
            finally
            {
                if (stream != null)
                {
                    try
					{
						stream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
                }
            }
        }
        catch (ContentLoadException ex)
        {
			//MonoGame try to load as a non-content file

            assetName = TitleContainer.getFilename(Paths.get(_rootDirectory, assetName).toString());

            assetName = normalize(assetName, type);

			if (StringHelpers.isNullOrEmpty(assetName))
			{
				throw new ContentLoadException("Could not load " + originalAssetName + " asset as a non-content file!", ex);
			}

            result = readRawAsset(assetName, originalAssetName, type);

            // Because Raw Assets skip the ContentReader step, they need to have their
            // disopsables recorded here. Doing it outside of this catch will 
            // result in disposables being logged twice.
            if (result instanceof AutoCloseable)
            {
                if (recordDisposableObject != null)
                	// TODO: Should I use As.as
                    // recordDisposableObject(result as AutoCloseable);
                	recordDisposableObject.accept((AutoCloseable) result);
                else
                	// TODO: Should I use As.as
                    // disposableAssets.add(result as AutoCloseable);
                	disposableAssets.add((AutoCloseable) result);
            }
		}
        
		if (result == null)
			throw new ContentLoadException("Could not load " + originalAssetName + " asset!");

		return (T)result;
	}

	// TODO: typeof(T) problem
	protected <T> String normalize(String assetName, Class<?> type)
	{
		if (type.getClass().equals(Texture2D.class) || type.getClass().equals(Texture.class))
		{
			return Texture2DReader.normalize(assetName);
		}
		else if (type.getClass().equals(SpriteFont.class))
		{
			return SpriteFontReader.normalize(assetName);
		}
// #if !WINRT
		// TODO: Do I need these ?
//		else if ((typeof(T) == typeof(Song)))
//		{
//			return SongReader.normalize(assetName);
//		}
//		else if ((typeof(T) == typeof(SoundEffect)))
//		{
//			return SoundEffectReader.normalize(assetName);
//		}
// #endif
//		else if (type.getClass().equals(Effect.class))
//		{
//			return EffectReader.normalize(assetName);
//		}
		return null;
	}

	protected <T> Object readRawAsset(String assetName, String originalAssetName, Class<?> type)
    {
        if (type.getClass().equals(Texture2D.class) || type.getClass().equals(Texture.class))
        {
            try (InputStream assetStream = TitleContainer.openStream(assetName))
            {
                Texture2D texture = Texture2D.fromStream(
                    graphicsDeviceService.getGraphicsDevice(), assetStream);
                texture.name = originalAssetName;
                return texture;
            }
			catch (IOException e)
			{
				e.printStackTrace();
			}
        }
        else if (type.getClass().equals(SpriteFont.class))
        {
        	// NOTE: This was already commented in the original file.
            //result = new SpriteFont(Texture2D.FromFile(graphicsDeviceService.GraphicsDevice,assetName), null, null, null, 0, 0.0f, null, null);
            throw new UnsupportedOperationException();
        }
// #if !DIRECTX
        // TODO: Do I need these ?
//        else if ((typeof(T) == typeof(Song)))
//        {
//            return new Song(assetName);
//        }
//        else if ((typeof(T) == typeof(SoundEffect)))
//        {
//            try (InputStream s = TitleContainer.openStream(assetName))
//            {
//                return SoundEffect.fromStream(s);
//            }
//        }
// #endif
        else if (type.getClass().equals(Effect.class))
        {
            try (InputStream assetStream = TitleContainer.openStream(assetName))
            {
            	ByteArrayOutputStream output = new ByteArrayOutputStream();
            	long count = 0;
            	int n = 0;
            	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            	while (EOF != (n = assetStream.read(buffer))) {
            	    output.write(buffer, 0, n);
            	    count += n;
            	}
            	byte[] data = output.toByteArray();
                return new Effect(this.graphicsDeviceService.getGraphicsDevice(), data);
            }
			catch (IOException e)
			{
				e.printStackTrace();
			}
        }
        return null;
    }

	private ContentReader getContentReaderFromXnb(String originalAssetName, final InputStream stream,
			BinaryReader xnbReader, Consumer<AutoCloseable> recordDisposableObject)
	{
		// The first 4 bytes should be the "XNB" header. i use that to detect an invalid file
		byte x = xnbReader.readByte();
		byte n = xnbReader.readByte();
		byte b = xnbReader.readByte();
		byte platform = xnbReader.readByte();

		if (x != 'X' || n != 'N' || b != 'B' ||
				!(targetPlatformIdentifiers.contains((char) platform)))
		{
			throw new ContentLoadException(
					"Asset does not appear to be a valid XNB file. Did you process your content for Windows?");
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

		ContentReader reader;
		// TODO: Decompression
/*		if (compressedLzx || compressedLz4)
		{
			// Decompress the xnb
			int decompressedSize = xnbReader.readInt32();
			ByteArrayOutputStream decompressedStream = null;

			if (compressedLzx)
			{
				// thanks to ShinAli (https://bitbucket.org/alisci01/xnbdecompressor)
				// default window size for XNB encoded files is 64Kb (need 16 bits to represent it)
				LzxDecoder dec = new LzxDecoder(16);
				decompressedStream = new ByteArrayOutputStream(decompressedSize);
				int compressedSize = xnbLength - 14;
				long startPos = stream.Position;
				long pos = startPos;

				while (pos - startPos < compressedSize)
				{
					// the compressed stream is seperated into blocks that will decompress
					// into 32Kb or some other size if specified.
					// normal, 32Kb output blocks will have a short indicating the size
					// of the block before the block starts
					// blocks that have a defined output will be preceded by a byte of value
					// 0xFF (255), then a short indicating the output size and another
					// for the block size
					// all shorts for these cases are encoded in big endian order
					int hi = stream.readByte();
					int lo = stream.readByte();
					int block_size = (hi << 8) | lo;
					int frame_size = 0x8000; // frame size is 32Kb by default
					// does this block define a frame size?
					if (hi == 0xFF)
					{
						hi = lo;
						lo = (byte) stream.readByte();
						frame_size = (hi << 8) | lo;
						hi = (byte) stream.readByte();
						lo = (byte) stream.readByte();
						block_size = (hi << 8) | lo;
						pos += 5;
					}
					else
						pos += 2;

					// either says there is nothing to decode
					if (block_size == 0 || frame_size == 0)
						break;

					dec.decompress(stream, block_size, decompressedStream, frame_size);
					pos += block_size;

					// reset the position of the input just incase the bit buffer
					// read in some unused bytes
					stream.Seek(pos, SeekOrigin.Begin);
				}

				if (decompressedStream.Position != decompressedSize)
				{
					throw new ContentLoadException("Decompression of " + originalAssetName + " failed. ");
				}

				decompressedStream.Seek(0, SeekOrigin.Begin);
			}
			else if (compressedLz4)
			{
				// Decompress to a byte[] because Windows 8 doesn't support MemoryStream.GetBuffer()
				byte[] buffer = new byte[decompressedSize];
				try (Lz4DecoderStream decoderStream = new Lz4DecoderStream(stream))
				{
					if (decoderStream.Read(buffer, 0, buffer.length) != decompressedSize)
					{
						throw new ContentLoadException("Decompression of " + originalAssetName + " failed. ");
					}
				}
				// Creating the MemoryStream with a byte[] shares the buffer so it doesn't allocate
				// any more memory
				decompressedStream = new ByteArrayInputStream(buffer);
			}

			reader = new ContentReader(this, decompressedStream, this.graphicsDeviceService.getGraphicsDevice(),
					originalAssetName, version, recordDisposableObject);
		}
		else*/
		{
			reader = new ContentReader(this, stream, this.graphicsDeviceService.getGraphicsDevice(),
					originalAssetName, version, recordDisposableObject);
		}
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
			// var methodInfo = typeof(ContentManager).GetType().GetTypeInfo().GetDeclaredMethod("ReloadAsset");
// #else
			// var methodInfo = typeof(ContentManager).GetMethod("ReloadAsset", BindingFlags.NonPublic | BindingFlags.Instance);
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
			// TODO: Can I simply cast it or should I use As.as()
			this.graphicsDeviceService = (IGraphicsDeviceService) serviceProvider.getService(IGraphicsDeviceService.class);
			if (this.graphicsDeviceService == null)
			{
				throw new IllegalStateException("No Graphics Device Service");
			}
		}
		
		InputStream stream = null;
		try
		{
            //try load it traditionally
            stream = openStream(assetName);

            // Try to load as XNB file
            try
            {
                try (BinaryReader xnbReader = new BinaryReader(stream))
                {
                    try (ContentReader reader = getContentReaderFromXnb(assetName, stream, xnbReader, null))
                    {
                        reader.initializeTypeReaders();
                        reader.readObject2(currentAsset);
                        reader.readSharedResources();
                    }
                }
				catch (IOException e)
				{
					e.printStackTrace();
				}
            }
            finally
            {
                if (stream != null)
                {
                    try
					{
						stream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
                }
            }
		}
		catch (ContentLoadException e)
		{
			// Try to reload as a non-xnb file.
            // Just textures supported for now.

            assetName = TitleContainer.getFilename(Paths.get(_rootDirectory, assetName).toString());

            assetName = normalize(assetName, type);

            reloadRawAsset(currentAsset, assetName, originalAssetName);
        }
	}

	protected <T> void reloadRawAsset(T asset, String assetName, String originalAssetName)
    {
        if (asset instanceof Texture2D)
        {
            try (InputStream assetStream = TitleContainer.openStream(assetName))
            {
            	// TODO: Can I simply cast it or should I use As.as()
            	Texture2D textureAsset = (Texture2D) asset;
                textureAsset.reload(assetStream);
            }
			catch (IOException e)
			{
				e.printStackTrace();
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
	
	// TODO: Do I want this as a String or a Path ?
	protected String getRootDirectoryFullPath()
	{
		return Paths.get(TitleContainer.getLocation(), getRootDirectory()).toString();
	}

	public IServiceProvider getServiceProvider()
	{
		return this.serviceProvider;
	}
	
	// NOTE: Added these for the readRawAsset() method
	private final int EOF = -1;
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
}
