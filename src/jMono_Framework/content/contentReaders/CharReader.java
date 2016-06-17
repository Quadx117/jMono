package jMono_Framework.content.contentReaders;

import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;

public class CharReader extends ContentTypeReader<Character>
{
	public CharReader()
	{
		super(Character.class);
	}

	@Override
	protected Character read(ContentReader input, Character existingInstance)
	{
		return input.readChar();
	}
}
