package gameCore.content;

import gameCore.content.contentReaders.Texture2DReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ContentTypeReaderManager
{
	private static Object _locker;

	private static Map<Class<?>, ContentTypeReader> _contentReadersCache;

	private Map<Class<?>, ContentTypeReader> _contentReaders;

	private static final String _assemblyName;

	static
	{
		_locker = new Object();
		_contentReadersCache = new HashMap<Class<?>, ContentTypeReader>(255);

// #if WINRT
		// _assemblyName = typeof(ContentTypeReaderManager).GetTypeInfo().Assembly.FullName;
// #else
		// TODO: Find the java equivalent if needed
		// _assemblyName = typeof(ContentTypeReaderManager).Assembly.FullName;
		_assemblyName = "";
// #endif
	}

	public ContentTypeReader getTypeReader(Class<?> targetType)
	{
		return _contentReaders.get(targetType);
	}

	// Trick to prevent the linker removing the code, but not actually execute the code
	static boolean falseflag = false;

	protected ContentTypeReader[] loadAssetReaders(ContentReader reader)
	{
		// Trick to prevent the linker removing the code, but not actually execute the code
		if (falseflag)
		{
			// TODO: Create all the readers
			// Dummy variables required for it to work on iDevices ** DO NOT DELETE **
			// This forces the classes not to be optimized out when deploying to iDevices
			// ByteReader hByteReader = new ByteReader();
			// SByteReader hSByteReader = new SByteReader();
			// DateTimeReader hDateTimeReader = new DateTimeReader();
			// DecimalReader hDecimalReader = new DecimalReader();
			// BoundingSphereReader hBoundingSphereReader = new BoundingSphereReader();
			// BoundingFrustumReader hBoundingFrustumReader = new BoundingFrustumReader();
			// RayReader hRayReader = new RayReader();
			// ListReader hCharListReader = new ListReader<Char>();
			// ListReader hRectangleListReader = new ListReader<Rectangle>();
			// ArrayReader hRectangleArrayReader = new ArrayReader<Rectangle>();
			// ListReader hVector3ListReader = new ListReader<Vector3>();
			// ListReader hStringListReader = new ListReader<StringReader>();
			// ListReader hIntListReader = new ListReader<Int32>();
			// SpriteFontReader hSpriteFontReader = new SpriteFontReader();
			Texture2DReader hTexture2DReader = new Texture2DReader();
			// CharReader hCharReader = new CharReader();
			// RectangleReader hRectangleReader = new RectangleReader();
			// StringReader hStringReader = new StringReader();
			// Vector2Reader hVector2Reader = new Vector2Reader();
			// Vector3Reader hVector3Reader = new Vector3Reader();
			// Vector4Reader hVector4Reader = new Vector4Reader();
			// CurveReader hCurveReader = new CurveReader();
			// IndexBufferReader hIndexBufferReader = new IndexBufferReader();
			// BoundingBoxReader hBoundingBoxReader = new BoundingBoxReader();
			// MatrixReader hMatrixReader = new MatrixReader();
			// BasicEffectReader hBasicEffectReader = new BasicEffectReader();
			// VertexBufferReader hVertexBufferReader = new VertexBufferReader();
			// AlphaTestEffectReader hAlphaTestEffectReader = new AlphaTestEffectReader();
			// EnumReader hEnumSpriteEffectsReader = new EnumReader<Graphics.SpriteEffects>();
			// ArrayReader hArrayFloatReader = new ArrayReader<float>();
			// ArrayReader hArrayVector2Reader = new ArrayReader<Vector2>();
			// ListReader hListVector2Reader = new ListReader<Vector2>();
			// ArrayReader hArrayMatrixReader = new ArrayReader<Matrix>();
			// EnumReader hEnumBlendReader = new EnumReader<Graphics.Blend>();
			// NullableReader hNullableRectReader = new NullableReader<Rectangle>();
			// EffectMaterialReader hEffectMaterialReader = new EffectMaterialReader();
			// ExternalReferenceReader hExternalReferenceReader = new ExternalReferenceReader();
			// SoundEffectReader hSoundEffectReader = new SoundEffectReader();
			// SongReader hSongReader = new SongReader();
			// ModelReader hModelReader = new ModelReader();
			// Int32Reader hInt32Reader = new Int32Reader();

			// At the moment the Video class doesn't exist
			// on all platforms... Allow it to compile anyway.
// #if ANDROID || IOS || MONOMAC || (WINDOWS && !OPENGL) || (WINRT && !WINDOWS_PHONE)
			// VideoReader hVideoReader = new VideoReader();
// #endif
		}

		// The first content byte i read tells me the number of content readers in this XNB file
		int numberOfReaders = reader.read7BitEncodedInt();
		ContentTypeReader[] contentReaders = new ContentTypeReader[numberOfReaders];
		BitSet needsInitialize = new BitSet(numberOfReaders);
		_contentReaders = new HashMap<Class<?>, ContentTypeReader>(numberOfReaders);

		// Lock until we're done allocating and initializing any new
		// content type readers... this ensures we can load content
		// from multiple threads and still cache the readers.
		synchronized (_locker)
		{
			// For each reader in the file, we read out the length of the string which contains the
			// type of the reader,
			// then we read out the string. Finally we instantiate an instance of that reader using
			// reflection
			for (int i = 0; i < numberOfReaders; ++i)
			{
				// This string tells us what reader we need to decode the following data
				// string readerTypeString = reader.ReadString();
				String originalReaderTypeString = reader.readString();

				Supplier<ContentTypeReader> readerFunc;
				readerFunc = typeCreators.get(originalReaderTypeString);
				if (readerFunc != null)
				{
					contentReaders[i] = readerFunc.get();
					needsInitialize.set(i, true);
				}
				else
				{
					// System.Diagnostics.Debug.WriteLine(originalReaderTypeString);

					// Need to resolve namespace differences
					String readerTypeString = originalReaderTypeString;

					readerTypeString = prepareType(readerTypeString);

					// Class<?> l_readerType = readerTypeString.getClass();
					Class<?> l_readerType = null;
					try
					{
						l_readerType = Class.forName(readerTypeString);
					}
					catch (ClassNotFoundException e1)
					{
						System.err.println("The path to the reader in the xnb file needs to be changed manually until we create our own asset pipeline.");
						e1.printStackTrace();
					}
					if (l_readerType != null)
					{
						ContentTypeReader typeReader = _contentReadersCache.get(l_readerType);
						if (typeReader == null)
						{
							try
							{
								typeReader = (ContentTypeReader) l_readerType.getConstructor().newInstance();
							}
							catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException e)
							{
								// If you are getting here, the Mono runtime is most likely not able
								// to JIT the type.
								// In particular, MonoTouch needs help instantiating types that are
								// only defined in strings in Xnb files.
								e.printStackTrace();
								throw new UnsupportedOperationException(
										"Failed to get default constructor for ContentTypeReader. To work around, add a creation function to ContentTypeReaderManager.addTypeCreator() "
												+ "with the following failed type string: " + originalReaderTypeString,
										e);
							}

							needsInitialize.set(i, true);

							_contentReadersCache.put(l_readerType, typeReader);
						}

						contentReaders[i] = typeReader;
					}
					else
						throw new ContentLoadException(
								"Could not find ContentTypeReader Type. Please ensure the name of the Assembly that contains the Type matches the assembly in the full type name: "
										+ originalReaderTypeString + " (" + readerTypeString + ")");
				}

				Class<?> targetType = contentReaders[i].getTargetType();
				if (targetType != null)
					_contentReaders.put(targetType, contentReaders[i]);

				// I think the next 4 bytes refer to the "Version" of the type reader,
				// although it always seems to be zero
				reader.readInt32();
			}

			// Initialize any new readers.
			for (int i = 0; i < contentReaders.length; i++)
			{
				if (needsInitialize.get(i))
					contentReaders[i].initialize(this);
			}

		} // lock (_locker)

		return contentReaders;
	}

	// / <summary>
	// / Removes Version, Culture and PublicKeyToken from a type string.
	// / </summary>
	// / <remarks>
	// / Supports multiple generic types (e.g. Map&lt;TKey,TValue&gt;) and nested generic types
	// (e.g. List&lt;List&lt;int&gt;&gt;).
	// / </remarks>
	// / <param name="type">
	// / A <see cref="System.String"/>
	// / </param>
	// / <returns>
	// / A <see cref="System.String"/>
	// / </returns>
	public static String prepareType(String type)
	{
		// TODO: validate the regex. was new[] {"[["} in orignial code
		// but that's a char[]
		// Needed to support nested types
		int count = type.split("\\[").length - 1;

		String preparedType = type;

		for (int i = 0; i < count; ++i)
		{
			// TODO: Need to check what "[$1]" means in .NET
			preparedType = preparedType.replaceAll("[(.+?), Version=.+?]", "[$1]");
		}

		// Handle non generic types
		if (preparedType.contains("PublicKeyToken"))
			preparedType = preparedType.replaceAll("(.+?), Version=.+?$", "$1");

		// TODO: For WinRT this is most likely broken!
		preparedType = preparedType.replace(", Microsoft.Xna.Framework.Graphics", String.format(", %s", _assemblyName));
		preparedType = preparedType.replace(", Microsoft.Xna.Framework.Video", String.format(", %s", _assemblyName));
		preparedType = preparedType.replace(", Microsoft.Xna.Framework", String.format(", %s", _assemblyName));

		return preparedType;
	}

	// Static map of type names to creation functions. Required as iOS requires all types at compile
	// time
	private static Map<String, Supplier<ContentTypeReader>> typeCreators = new HashMap<String, Supplier<ContentTypeReader>>();

	// / <summary>
	// / Adds the type creator.
	// / </summary>
	// / <param name='typeString'>
	// / Type string.
	// / </param>
	// / <param name='createFunction'>
	// / Create function.
	// / </param>
	public static void addTypeCreator(String typeString, Supplier<ContentTypeReader> createFunction)
	{
		if (!typeCreators.containsKey(typeString))
			typeCreators.put(typeString, createFunction);
	}

	public static void clearTypeCreators()
	{
		typeCreators.clear();
	}

}
