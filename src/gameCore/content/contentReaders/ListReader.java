package gameCore.content.contentReaders;

import gameCore.content.ContentReader;
import gameCore.content.ContentTypeReader;
import gameCore.content.ContentTypeReaderManager;
import gameCore.utilities.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

public class ListReader<T> extends ContentTypeReader<List<T>>
{
	ContentTypeReader<?> elementReader;

    public ListReader(Class<?> type)
    {
    	super(List.class);
		this.type = type;
    }

    @Override
    protected void initialize(ContentTypeReaderManager manager)
    {
		Class<?> readerType = type;
		elementReader = manager.getTypeReader(readerType);
    }

    @Override
    public boolean canDeserializeIntoExistingObject()
    {
        return true;
    }

    @Override
    protected List<T> read(ContentReader input, List<T> existingInstance)
    {
        int count = input.readInt32();
        List<T> list = existingInstance;
        if (list == null) list = new ArrayList<T>(count);
        for (int i = 0; i < count; ++i)
        {
			if (ReflectionHelpers.isValueType(type))
			{
            	list.add(input.readObject(elementReader));
			}
			else
			{
                int readerType = input.read7BitEncodedInt();
            	list.add(readerType > 0 ? input.readObject(input.getTypeReaders()[readerType - 1]) : null);
			}
        }
        return list;
    }
    
    // NOTE: Added this to resolve typeof(T) problem in Java
    Class<?> type;
}
