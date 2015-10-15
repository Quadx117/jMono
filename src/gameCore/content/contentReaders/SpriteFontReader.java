package gameCore.content.contentReaders;

import gameCore.Rectangle;
import gameCore.content.ContentReader;
import gameCore.content.ContentTypeReader;
import gameCore.graphics.SpriteFont;
import gameCore.graphics.Texture2D;
import gameCore.math.Vector3;

import java.util.List;

public class SpriteFontReader extends ContentTypeReader<SpriteFont>
{
	public SpriteFontReader()
	{
		super(SpriteFont.class);
		// Do nothing
	}

	static String[] supportedExtensions = new String[] { ".spritefont" };

	public static String normalize(String fileName)
	{
		return normalize(fileName, supportedExtensions);
	}

	@Override
	protected SpriteFont read(ContentReader input, SpriteFont existingInstance)
	{
		if (existingInstance != null)
		{
			// Read the texture into the existing texture instance
			// input.readObject<Texture2D>(existingInstance.getTexture());
			input.readObject2(existingInstance.getTexture());

			// discard the rest of the SpriteFont data as we are only reloading GPU resources for
			// now
			// input.readObject<List<Rectangle>>();
			// input.readObject<List<Rectangle>>();
			// input.readObject<List<char>>();
			input.readObject();
			input.readObject();
			input.readObject();

			input.readInt32();
			input.readSingle();
			// input.readObject<List<Vector3>>();
			input.readObject();
			if (input.readBoolean())
			{
				input.readChar();
			}

			return existingInstance;
		}
		else
		{
			// Create a fresh SpriteFont instance
			// Texture2D texture = input.readObject<Texture2D>();
			// List<Rectangle> glyphs = input.readObject<List<Rectangle>>();
			// List<Rectangle> cropping = input.readObject<List<Rectangle>>();
			// List<Character> charMap = input.ReadObject<List<char>>();
			Texture2D texture = input.readObject();
			List<Rectangle> glyphs = input.readObject();
			List<Rectangle> cropping = input.readObject();
			List<Character> charMap = input.readObject();
			int lineSpacing = input.readInt32();
			float spacing = input.readSingle();
			List<Vector3> kerning = input.readObject();
			Character defaultCharacter = null;
			if (input.readBoolean())
			{
				defaultCharacter = new Character(input.readChar());
			}
			return new SpriteFont(texture, glyphs, cropping, charMap, lineSpacing, spacing, kerning, defaultCharacter);
		}
	}
}
