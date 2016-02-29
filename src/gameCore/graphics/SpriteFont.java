package gameCore.graphics;

import gameCore.Color;
import gameCore.Rectangle;
import gameCore.math.Matrix;
import gameCore.math.Vector2;
import gameCore.math.Vector3;
import gameCore.math.Vector4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpriteFont
{
	static class Errors
	{
		public static final String TextContainsUnresolvableCharacters = "Text contains characters that cannot be resolved by this SpriteFont.";
	}

	// NOTE: Moved this into Glyph since Java's Map handle this Differently than .NET's Dictionary.
//	public class CharComparer // : IEqualityComparer<char>
//	{
//		public boolean equals(char x, char y)
//		{
//			return x == y;
//		}

//		public int hashCode(char b)
//		{
//			return (b | (b << 16));
//		}
//	}

	private Map<Character, Glyph> _glyphs;

	private Texture2D _texture;

	// TODO: Do I need to make a wrapper class for Character ?
	public SpriteFont(Texture2D texture, List<Rectangle> glyphBounds, List<Rectangle> cropping,
			List<Character> characters, int lineSpacing, float spacing, List<Vector3> kerning,
			Character defaultCharacter)
	{
		this.characters = new ArrayList<Character>(characters);
		_texture = texture;
		this.lineSpacing = lineSpacing;
		this.spacing = spacing;
		this.defaultCharacter = defaultCharacter;

		_glyphs = new HashMap<Character, Glyph>(characters.size());

		for (int i = 0; i < characters.size(); ++i)
		{
			Glyph glyph = new Glyph();
			// TODO: Should I create a copy of the rectangle instead (struct)
			glyph.boundsInTexture = glyphBounds.get(i);
			glyph.cropping = cropping.get(i);
			glyph.character = characters.get(i);

			glyph.leftSideBearing = kerning.get(i).x;
			glyph.width = kerning.get(i).y;
			glyph.rightSideBearing = kerning.get(i).z;

			glyph.widthIncludingBearings = kerning.get(i).x + kerning.get(i).y + kerning.get(i).z;
			_glyphs.put(glyph.character, glyph);
		}
	}

	// / <summary>
	// / Gets the texture that this SpriteFont draws from.
	// / </summary>
	// / <remarks>Can be used to implement custom rendering of a SpriteFont</remarks>
	public Texture2D getTexture()
	{
		return _texture;
	}

	// / <summary>
	// / Returns a copy of the dictionary containing the glyphs in this SpriteFont.
	// / </summary>
	// / <returns>A new Dictionary containing all of the glyphs inthis SpriteFont</returns>
	// / <remarks>Can be used to calculate character bounds when implementing custom SpriteFont
	// rendering.</remarks>
	public Map<Character, Glyph> getGlyphs()
	{
		return new HashMap<Character, Glyph>(_glyphs);
	}

	// / <summary>
	// / Gets a collection of the characters in the font.
	// / </summary>
	private List<Character> characters;

	public List<Character> getCharacters()
	{
		return characters;
	}

	// / <summary>
	// / Gets or sets the character that will be substituted when a
	// / given character is not included in the font.
	// / </summary>
	public Character defaultCharacter;

	// / <summary>
	// / Gets or sets the line spacing (the distance from baseline
	// / to baseline) of the font.
	// / </summary>
	public int lineSpacing;

	// / <summary>
	// / Gets or sets the spacing (tracking) between characters in
	// / the font.
	// / </summary>
	public float spacing;

	// / <summary>
	// / Returns the size of a string when rendered in this font.
	// / </summary>
	// / <param name="text">The text to measure.</param>
	// / <returns>The size, in pixels, of 'text' when rendered in
	// / this font.</returns>
	public Vector2 measureString(String text)
	{
		CharacterSource source = new CharacterSource(text);
		Vector2 size = new Vector2();
		measureString(source, size);
		return size;
	}

	// / <summary>
	// / Returns the size of the contents of a StringBuilder when
	// / rendered in this font.
	// / </summary>
	// / <param name="text">The text to measure.</param>
	// / <returns>The size, in pixels, of 'text' when rendered in
	// / this font.</returns>
	public Vector2 measureString(StringBuilder text)
	{
		CharacterSource source = new CharacterSource(text);
		Vector2 size = new Vector2();
		measureString(source, size);
		return size;
	}

	private void measureString(final CharacterSource text, Vector2 size)
	{
		if (text.length == 0)
		{
			size = new Vector2();
			return;
		}

		// Get the default glyph here once.
		Glyph defaultGlyph = null;
		if (defaultCharacter != null)
			defaultGlyph = _glyphs.get(defaultCharacter.charValue());

		float width = 0.0f;
		float finalLineHeight = (float) lineSpacing;
		int fullLineCount = 0;
		Glyph currentGlyph = new Glyph();
		Vector2 offset = new Vector2(Vector2.ZERO);
		boolean hasCurrentGlyph = false;
		boolean firstGlyphOfLine = true;

		for (int i = 0; i < text.length; ++i)
		{
			char c = text.getChar(i);
			if (c == '\r')
			{
				hasCurrentGlyph = false;
				continue;
			}

			if (c == '\n')
			{
				++fullLineCount;
				finalLineHeight = lineSpacing;

				offset.x = 0;
				offset.y = lineSpacing * fullLineCount;
				hasCurrentGlyph = false;
				firstGlyphOfLine = true;
				continue;
			}

			if (hasCurrentGlyph)
			{
				offset.x += spacing;

				// The first character on a line might have a negative left side bearing.
				// In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
				// so that text does not hang off the left side of its rectangle.
				if (firstGlyphOfLine)
				{
					offset.x = Math.max(offset.x + Math.abs(currentGlyph.leftSideBearing), 0);
					firstGlyphOfLine = false;
				}
				else
				{
					offset.x += currentGlyph.leftSideBearing;
				}

				offset.x += currentGlyph.width + currentGlyph.rightSideBearing;
			}

			currentGlyph = _glyphs.get(c);
			if (currentGlyph == null)
			{
				if (defaultGlyph == null)
					throw new IllegalArgumentException("text: " + Errors.TextContainsUnresolvableCharacters);

				currentGlyph = defaultGlyph;
			}
			hasCurrentGlyph = true;

			float proposedWidth = offset.x + currentGlyph.widthIncludingBearings + spacing;
			if (proposedWidth > width)
				width = proposedWidth;

			if (currentGlyph.cropping.height > finalLineHeight)
				finalLineHeight = currentGlyph.cropping.height;
		}

		size.x = width;
		size.y = fullLineCount * lineSpacing + finalLineHeight;
	}

	// TODO: test to see if it works
	protected void drawInto(SpriteBatch spriteBatch, final CharacterSource text, Vector2 position, Color color,
			float rotation, Vector2 origin, Vector2 scale, SpriteEffects effect, float depth)
	{
		Vector2 flipAdjustment = new Vector2();

		boolean flippedVert = (effect.getValue() & SpriteEffects.FlipVertically.getValue()) == SpriteEffects.FlipVertically
				.getValue();
		boolean flippedHorz = (effect.getValue() & SpriteEffects.FlipHorizontally.getValue()) == SpriteEffects.FlipHorizontally
				.getValue();

		if (flippedVert || flippedHorz)
		{
			Vector2 size = new Vector2();
			measureString(text, size);

			if (flippedHorz)
			{
				origin.x *= -1;
				flipAdjustment.x = -size.x;
			}

			if (flippedVert)
			{
				origin.y *= -1;
				flipAdjustment.y = lineSpacing - size.y;
			}
		}

		// TODO: This looks excessive... i suspect we could do most
		// of this with simple vector math and avoid this much matrix work.

		Matrix transformation = new Matrix();
		Matrix temp = new Matrix();
		Matrix.createTranslation(-origin.x, -origin.y, 0f, transformation);
		Matrix.createScale((flippedHorz ? -scale.x : scale.x), (flippedVert ? -scale.y : scale.y), 1f, temp);
		Matrix.multiply(transformation, temp, transformation);
		Matrix.createTranslation(flipAdjustment.x, flipAdjustment.y, 0, temp);
		Matrix.multiply(temp, transformation, transformation);
		Matrix.createRotationZ(rotation, temp);
		Matrix.multiply(transformation, temp, transformation);
		Matrix.createTranslation(position.x, position.y, 0f, temp);
		Matrix.multiply(transformation, temp, transformation);

		// Get the default glyph here once.
		Glyph defaultGlyph = null;
		if (defaultCharacter != null)
			defaultGlyph = _glyphs.get(defaultCharacter.charValue());

		Glyph currentGlyph = new Glyph();
		Vector2 offset = new Vector2();
		boolean hasCurrentGlyph = false;
		boolean firstGlyphOfLine = true;

		for (int i = 0; i < text.length; ++i)
		{
			char c = text.getChar(i);
			if (c == '\r')
			{
				hasCurrentGlyph = false;
				continue;
			}

			if (c == '\n')
			{
				offset.x = 0;
				offset.y += lineSpacing;
				hasCurrentGlyph = false;
				firstGlyphOfLine = true;
				continue;
			}

			if (hasCurrentGlyph)
			{
				offset.x += spacing + currentGlyph.width + currentGlyph.rightSideBearing;
			}

			currentGlyph = _glyphs.get(c);
			if (currentGlyph == null)
			{
				if (defaultGlyph == null)
					throw new IllegalArgumentException("text: " + Errors.TextContainsUnresolvableCharacters);

				currentGlyph = defaultGlyph;
			}
			hasCurrentGlyph = true;

			// The first character on a line might have a negative left side bearing.
			// In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
			// so that text does not hang off the left side of its rectangle.
			if (firstGlyphOfLine)
			{
				offset.x = Math.max(currentGlyph.leftSideBearing, 0);
				firstGlyphOfLine = false;
			}
			else
			{
				offset.x += currentGlyph.leftSideBearing;
			}

			Vector2 p = new Vector2(offset);

			if (flippedHorz)
				p.x += currentGlyph.boundsInTexture.width;
			p.x += currentGlyph.cropping.x;

			if (flippedVert)
				p.y += currentGlyph.boundsInTexture.height - lineSpacing;
			p.y += currentGlyph.cropping.y;

			Vector2.transform(p, transformation, p);

			Vector4 destRect = new Vector4(p.x, p.y,
					currentGlyph.boundsInTexture.width * scale.x,
					currentGlyph.boundsInTexture.height * scale.y);

			spriteBatch.drawInternal(
					_texture, destRect, currentGlyph.boundsInTexture,
					color, rotation, Vector2.ZERO, effect, depth, false);
		}

		// We need to flush if we're using Immediate sort mode.
		spriteBatch.flushIfNeeded();
	}

	// C# struct
	// TODO: Since this is a struct, should I add a no-argument constructor ?
	protected class CharacterSource
	{
		private final String _string;
		private final StringBuilder _builder;

		public CharacterSource(String s)
		{
			_string = s;
			_builder = null;
			length = s.length();
		}

		public CharacterSource(StringBuilder builder)
		{
			_builder = builder;
			_string = null;
			length = _builder.length();
		}

		public final int length;

		public char getChar(int index)
		{
			if (_string != null)
				return _string.charAt(index);
			return _builder.charAt(index);
		}
	}

	// C# struct
	/**
	 * Class that defines the spacing, Kerning, and bounds of a character.
	 * <p>
	 * Provides the data necessary to implement custom SpriteFont rendering.
	 * 
	 * @author Eric Perron
	 *
	 */
	class Glyph
	{
		/**
		 * The char associated with this glyph.
		 */
		public char character;
		/**
		 * Rectangle in the font texture where this letter exists.
		 */
		public Rectangle boundsInTexture = new Rectangle();
		/**
		 * Cropping applied to the BoundsInTexture to calculate the bounds of the actual character.
		 */
		public Rectangle cropping = new Rectangle();
		/**
		 * The amount of space between the left side ofthe character and its first pixel in the X
		 * dimension.
		 */
		public float leftSideBearing;
		/**
		 * The amount of space between the right side of the character and its last pixel in the X
		 * dimension.
		 */
		public float rightSideBearing;
		/**
		 * Width of the character before kerning is applied.
		 */
		public float width;
		/**
		 * Width of the character before kerning is applied.
		 */
		public float widthIncludingBearings;

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
			{
				return false;
			}
			if (obj.getClass() != this.getClass())
			{
				return false;
			}
			return (this.character == ((Glyph) obj).character);
		}

		@Override
		public int hashCode()
		{
			return (character | (character << 16));
		}
		
		@Override
		public String toString()
		{
			return "CharacterIndex=" + character + ", Glyph=" + boundsInTexture + ", Cropping=" + cropping
					+ ", Kerning="
					+ leftSideBearing + "," + width + "," + rightSideBearing;
		}
	}

}
