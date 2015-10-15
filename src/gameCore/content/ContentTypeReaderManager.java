package gameCore.content;

import gameCore.Rectangle;
import gameCore.content.contentReaders.CharReader;
import gameCore.content.contentReaders.ListReader;
import gameCore.content.contentReaders.RectangleReader;
import gameCore.content.contentReaders.SpriteFontReader;
import gameCore.content.contentReaders.Texture2DReader;
import gameCore.content.contentReaders.Vector3Reader;
import gameCore.math.Vector3;

import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ContentTypeReaderManager
{
	private static Object _locker;

	private static Map<String, ContentTypeReader<?>> _contentReadersCache;

	private Map<Class<?>, ContentTypeReader<?>> _contentReaders;

	private static final String _assemblyName;

	static
	{
		_locker = new Object();
		_contentReadersCache = new HashMap<String, ContentTypeReader<?>>(255);

// #if WINRT
		// _assemblyName = typeof(ContentTypeReaderManager).GetTypeInfo().Assembly.FullName;
// #else
		// TODO: Find the java equivalent if needed
		// _assemblyName = typeof(ContentTypeReaderManager).Assembly.FullName;
		_assemblyName = "";
// #endif
	}

	public ContentTypeReader<?> getTypeReader(Class<?> targetType)
	{
		return _contentReaders.get(targetType);
	}

	// Trick to prevent the linker removing the code, but not actually execute the code
	static boolean falseflag = false;

	protected ContentTypeReader<?>[] loadAssetReaders(ContentReader reader)
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
			ListReader<Character> hCharListReader = new ListReader<Character>(Character.class);
			ListReader<Rectangle> hRectangleListReader = new ListReader<Rectangle>(Rectangle.class);
			// ArrayReader hRectangleArrayReader = new ArrayReader<Rectangle>();
			ListReader<Vector3> hVector3ListReader = new ListReader<Vector3>(Vector3.class);
			// ListReader hStringListReader = new ListReader<StringReader>();
			// ListReader hIntListReader = new ListReader<Int32>();
			SpriteFontReader hSpriteFontReader = new SpriteFontReader();
			Texture2DReader hTexture2DReader = new Texture2DReader();
			CharReader hCharReader = new CharReader();
			RectangleReader hRectangleReader = new RectangleReader();
			// StringReader hStringReader = new StringReader();
			// Vector2Reader hVector2Reader = new Vector2Reader();
			Vector3Reader hVector3Reader = new Vector3Reader();
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
		ContentTypeReader<?>[] contentReaders = new ContentTypeReader[numberOfReaders];
		BitSet needsInitialize = new BitSet(numberOfReaders);
		_contentReaders = new HashMap<Class<?>, ContentTypeReader<?>>(numberOfReaders);

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

				Supplier<ContentTypeReader<?>> readerFunc;
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
					String parameterType = originalReaderTypeString;
					String readerMapKey = originalReaderTypeString;

					readerTypeString = prepareType(readerTypeString);
					parameterType = prepareParameter(parameterType);
					readerMapKey = prepareMapKey(readerMapKey);

					Class<?> l_readerType = null;
					Class<?> l_parameterType = null;
					try
					{
						l_readerType = Class.forName(readerTypeString);
						if (parameterType != null)
						{
							l_parameterType = Class.forName(parameterType);
						}
					}
					catch (ClassNotFoundException e1)
					{
						System.err.println("The path to the reader in the xnb file needs to be changed manually until we create our own asset pipeline.");
						e1.printStackTrace();
					}
					if (l_readerType != null)
					{
						ContentTypeReader<?> typeReader = _contentReadersCache.get(readerMapKey);
						if (typeReader == null)
						{
							try
							{
								// For parameterized types get the 1 argument constructor
								if (l_readerType.equals(ListReader.class))
								{
									typeReader = (ContentTypeReader<?>) l_readerType.getConstructor(l_parameterType.getClass()).newInstance(l_parameterType);
								}
								// Otherwise, get the no arguments constructor
								else
								{
									typeReader = (ContentTypeReader<?>) l_readerType.getConstructor().newInstance();
								}
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
												+ "with the following failed type string: " + originalReaderTypeString, e);
							}

							needsInitialize.set(i, true);

							_contentReadersCache.put(readerMapKey, typeReader);
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
		// Needed to support nested types
		int count = type.split("\\[\\[").length - 1;

		String preparedType = type;

		for (int i = 0; i < count; ++i)
		{
			// NOTE: In .NET we keep , MyAssembly to use in the getType(String) method
			// preparedType = preparedType.replaceAll("\\[(.+?), Version=.+?\\]", "\\[$1\\]");
			// preparedType = preparedType.replaceAll("\\[(.+?), .+?\\]", "\\[$1\\]");
			preparedType = preparedType.replaceAll("(.+?)`.+?$", "$1");
		}

		// Handle non generic types
		if (preparedType.contains("PublicKeyToken"))
			// NOTE: In .NET we keep , MyAssembly to use in the getType(String) method
			// preparedType = preparedType.replaceAll("(.+?), Version=.+?$", "$1");
			preparedType = preparedType.replaceAll("(.+?), .+?$", "$1");

		// TODO: For WinRT this is most likely broken!
		// TODO: Debug this when we need it
		preparedType = preparedType.replace(", Microsoft.Xna.Framework.Graphics", String.format(", %s", _assemblyName));
		preparedType = preparedType.replace(", Microsoft.Xna.Framework.Video", String.format(", %s", _assemblyName));
		preparedType = preparedType.replace(", Microsoft.Xna.Framework", String.format(", %s", _assemblyName));

		return preparedType;
	}
	
	// NOTE: Added this method to take care of parameters for parameterized types
	// i.e.: List<Rectangle>
	public static String prepareParameter(String parameter)
	{
		String preparedParameter = parameter;

		if (parameter.contains("["))
		{
			// Needed to support nested types
			int count = parameter.split("\\[\\[").length - 1;
	
			for (int i = 0; i < count; ++i)
			{
				preparedParameter = preparedParameter.replaceAll(".+?\\[\\[(.+?), .+?\\]\\]", "$1");
			}
	
			// Handle non generic types
			if (preparedParameter.contains("PublicKeyToken"))
				preparedParameter = preparedParameter.replaceAll("(.+?), .+?$", "$1");
		}
		else
		{
			preparedParameter = null;
		}

		return preparedParameter;
	}
	
	// NOTE: Added this method to take care of the mapKey
	// This is needed because of Java's type erasure.
	// i.e. List<Rectangle> becomes List so there is no difference
	// between List<Rectangle> and List<Vector3>
	public static String prepareMapKey(String mapKey)
	{
		String preparedMapKey = mapKey;

		// Needed to support nested types
		int count = mapKey.split("\\[\\[").length - 1;
		
		for (int i = 0; i < count; ++i)
		{
			preparedMapKey = preparedMapKey.replaceAll("\\[(.+?), .+?\\]", "\\[$1\\]");
		}
	
		// Handle non generic types
		if (preparedMapKey.contains("PublicKeyToken"))
			preparedMapKey = preparedMapKey.replaceAll("(.+?), .+?$", "$1");

		return preparedMapKey;
	}

	// Static map of type names to creation functions. Required as iOS requires all types at compile
	// time
	private static Map<String, Supplier<ContentTypeReader<?>>> typeCreators = new HashMap<String, Supplier<ContentTypeReader<?>>>();

	// / <summary>
	// / Adds the type creator.
	// / </summary>
	// / <param name='typeString'>
	// / Type string.
	// / </param>
	// / <param name='createFunction'>
	// / Create function.
	// / </param>
	public static void addTypeCreator(String typeString, Supplier<ContentTypeReader<?>> createFunction)
	{
		if (!typeCreators.containsKey(typeString))
			typeCreators.put(typeString, createFunction);
	}

	public static void clearTypeCreators()
	{
		typeCreators.clear();
	}

}
