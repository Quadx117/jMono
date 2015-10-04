package gameCore.content;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import gameCore.BoundingSphere;
import gameCore.Color;
import gameCore.dotNet.BinaryReader;
import gameCore.graphics.GraphicsDevice;
import gameCore.math.Matrix;
import gameCore.math.Quaternion;
import gameCore.math.Vector2;
import gameCore.math.Vector3;
import gameCore.math.Vector4;
import gameCore.utilities.FileHelpers;
import gameCore.utilities.ReflectionHelpers;
import gameCore.utilities.StringHelpers;

public class ContentReader extends BinaryReader
{
	private ContentManager contentManager;
    private Consumer<AutoCloseable> recordDisposableObject;
    private ContentTypeReaderManager typeReaderManager;
    private GraphicsDevice graphicsDevice;
    private String assetName;
    private List<Map.Entry<Integer, Consumer<Object>>> sharedResourceFixups;
    private ContentTypeReader[] typeReaders;
	protected int version;
	protected int sharedResourceCount;

    protected ContentTypeReader[] getTypeReaders()
    {
    	return typeReaders;
    }

    public GraphicsDevice getGraphicsDevice()
    {
    	return this.graphicsDevice;
    }

    protected ContentReader(ContentManager manager, InputStream stream, GraphicsDevice graphicsDevice, String assetName, int version, Consumer<AutoCloseable> recordDisposableObject)
    {
    	super(stream);
        this.graphicsDevice = graphicsDevice;
        this.recordDisposableObject = recordDisposableObject;
        this.contentManager = manager;
        this.assetName = assetName;
		this.version = version;
    }

    public ContentManager getContentManager()
    {
    	return contentManager;
    }
    
    public String getAssetName()
    {
    	return assetName;
    }

    protected <T> Object readAsset()
    {
        initializeTypeReaders();

        // Read primary object
        Object result = readObject();

        // Read shared resources
        readSharedResources();
        
        return result;
    }

    protected void initializeTypeReaders()
    {
        typeReaderManager = new ContentTypeReaderManager();
		typeReaders = typeReaderManager.loadAssetReaders(this);
        sharedResourceCount = read7BitEncodedInt();
        sharedResourceFixups = new ArrayList<Map.Entry<Integer, Consumer<Object>>>();
    }

    protected void readSharedResources() throws ContentLoadException
    {
        if (sharedResourceCount <= 0)
            return;

        @SuppressWarnings("unchecked")
		Consumer<Object>[] sharedResources = new Consumer[sharedResourceCount];
        for (int i = 0; i < sharedResourceCount; ++i)
            sharedResources[i] = innerReadObject(null);

        // Fixup shared resources by calling each registered action
        for (Entry<Integer, Consumer<Object>> fixup : sharedResourceFixups)
            fixup.setValue(sharedResources[fixup.getKey()]);
    }

    // TODO: test to see if works.
    public <T> T readExternalReference()
    {
        String externalReference = readString();

        if (!StringHelpers.isNullOrEmpty(externalReference))
        {
            return contentManager.load(FileHelpers.resolveRelativePath(assetName, externalReference));
        }

        // return default(T);
        return null;
    }

    public Matrix readMatrix()
    {
        Matrix result = new Matrix();
        result.M11 = readSingle();
        result.M12 = readSingle();
        result.M13 = readSingle();
        result.M14 = readSingle(); 
        result.M21 = readSingle();
        result.M22 = readSingle();
        result.M23 = readSingle();
        result.M24 = readSingle();
        result.M31 = readSingle();
        result.M32 = readSingle();
        result.M33 = readSingle();
        result.M34 = readSingle();
        result.M41 = readSingle();
        result.M42 = readSingle();
        result.M43 = readSingle();
        result.M44 = readSingle();
        return result;
    }
        
    private <T> void recordDisposable(T result)
    {
    	// TODO: Should I us As.as here ?
    	AutoCloseable disposable = (AutoCloseable) result;
        if (disposable == null)
            return;

        if (recordDisposableObject != null)
            recordDisposableObject.accept(disposable);
        else
            contentManager.recordDisposable(disposable);
    }

    public <T> T readObject()
    {
        // return readObject(default(T));
    	return readObject2(null);
    }

    public <T> T readObject(ContentTypeReader typeReader)
    {
        // T result = (T)typeReader.read(this, default(T));
    	T result = (T)typeReader.read(this, null);
        recordDisposable(result);
        return result;
    }

    // TODO: Need to find a way to reslove this issue since Java doesn't resolve to the right
    //		 method when passing null. It defaults to the previous method and crash.
    //		 For now I changed the method name to force it through the right one.
    public <T> T readObject2(T existingInstance)
    {
        return innerReadObject(existingInstance);
    }

    private <T> T innerReadObject(T existingInstance)
    {
        int typeReaderIndex = read7BitEncodedInt();
        if (typeReaderIndex == 0)
            return existingInstance;

        if (typeReaderIndex > typeReaders.length)
            throw new ContentLoadException("Incorrect type reader index found!");

        ContentTypeReader typeReader = typeReaders[typeReaderIndex - 1];
        T result = (T) typeReader.read(this, existingInstance);

        recordDisposable(result);

        return result;
    }

    public <T> T readObject(ContentTypeReader typeReader, T existingInstance)
    {
        if (!ReflectionHelpers.isValueType(typeReader.getTargetType()))
            return readObject2(existingInstance);

        T result = (T)typeReader.read(this, existingInstance);

        recordDisposable(result);

        return result;
    }

    public Quaternion readQuaternion()
    {
        Quaternion result = new Quaternion();
        result.x = readSingle();
        result.y = readSingle();
        result.z = readSingle();
        result.w = readSingle();
        return result;
    }

    public <T> T readRawObject()
    {
		// return (T)readRawObject(default(T));
    	return (T)readRawObject(null);
    }

    public <T> T readRawObject(ContentTypeReader typeReader)
    {
    	// return (T)readRawObject(typeReader, default(T));
        return (T)readRawObject(typeReader, null);
    }

    public <T> T readRawObject(T existingInstance)
    {
        Class<?> objectType = typeof(T);
        for(ContentTypeReader typeReader : typeReaders)
        {
            if(typeReader.getTargetType().equals(objectType))
                return (T)readRawObject(typeReader,existingInstance);
        }
        throw new IllegalArgumentException("Unsuported type");
    }

    @SuppressWarnings("unchecked")
	public <T> T readRawObject(ContentTypeReader typeReader, T existingInstance)
    {
        return (T)typeReader.read(this, existingInstance);
    }

    public <T> void readSharedResource(Consumer<T> fixup)
    {
        int index = read7BitEncodedInt();
        if (index > 0)
        {
            sharedResourceFixups.add(new AbstractMap.SimpleEntry<Integer, Consumer<Object>>(index - 1, Consumer(Object v)
                {
                    if (!(v instanceof T))
                    {
                        throw new ContentLoadException(String.Format("Error loading shared resource. Expected type {0}, received type {1}", typeof(T).Name, v.getClass().getSimpleName()));
                    }
                    fixup.accept((T)v);
                }));
        }
    }

    public Vector2 readVector2()
    {
        Vector2 result = new Vector2();
        result.x = readSingle();
        result.y = readSingle();
        return result;
    }

    public Vector3 readVector3()
    {
        Vector3 result = new Vector3();
        result.x = readSingle();
        result.y = readSingle();
        result.z = readSingle();
        return result;
    }

    public Vector4 readVector4()
    {
        Vector4 result = new Vector4();
        result.x = readSingle();
        result.y = readSingle();
        result.z = readSingle();
        result.w = readSingle();
        return result;
    }

    public Color readColor()
    {
        Color result = new Color();
        result.setRed(readByte());
        result.setGreen(readByte());
        result.setBlue(readByte());
        result.setAlpha(readByte());
        return result;
    }

    protected int read7BitEncodedInt()
    {
        return super.read7BitEncodedInt();
    }
	
	protected BoundingSphere readBoundingSphere()
	{
		Vector3 position = readVector3();
        float radius = readSingle();
        return new BoundingSphere(position, radius);
	}
}
