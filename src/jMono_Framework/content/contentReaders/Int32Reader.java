package jMono_Framework.content.contentReaders;

import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;

public class Int32Reader extends ContentTypeReader<Integer>
{
	public Int32Reader()
	{
		super(Integer.class);
	}

	@Override
	protected Integer read(ContentReader input, Integer existingInstance)
	{
		return input.readInt32();
	}
}
