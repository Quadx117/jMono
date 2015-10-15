package gameCore.content.contentReaders;

import gameCore.content.ContentReader;
import gameCore.content.ContentTypeReader;

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
