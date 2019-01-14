package jMono_Framework.graphics;

import jMono_Framework.Rectangle;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpriteFont
{
    static class Errors
    {
        public static final String TextContainsUnresolvableCharacters =
                "Text contains characters that cannot be resolved by this SpriteFont.";
    }

    private final Map<Character, Glyph> _glyphs;

    private final Texture2D _texture;

    protected Map<Character, Glyph> glyphs()
    {
        return _glyphs;
    }

    // NOTE(Eric): Don't need this since Java's Map cannot take a specified compare function.
    // If it was really needed, we would have to override the equals method of the key class using a
    // wrapper class.
    // public class CharComparer // : IEqualityComparer<char>
    // {
    // public boolean equals(char x, char y)
    // {
    // return x == y;
    // }

    // public int hashCode(char b)
    // {
    // return (b | (b << 16));
    // }

    // public static final CharComparer Default = new CharComparer();
    // }

    public SpriteFont(Texture2D texture, List<Rectangle> glyphBounds, List<Rectangle> cropping, List<Character> characters,
                      int lineSpacing, float spacing, List<Vector3> kerning, Character defaultCharacter)
    {
        this._characters = new ArrayList<Character>(characters);
        _texture = texture;
        this.lineSpacing = lineSpacing;
        this.spacing = spacing;
        this.defaultCharacter = defaultCharacter;

        _glyphs = new HashMap<Character, Glyph>(characters.size());

        for (int i = 0; i < characters.size(); ++i)
        {
            Glyph glyph = new Glyph();
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

    /**
     * Returns the texture that this SpriteFont draws from.
     * <p>
     * Can be used to implement custom rendering of a SpriteFont.
     * 
     * @return The texture that this SpriteFont draws from.
     */
    public Texture2D getTexture()
    {
        return _texture;
    }

    /**
     * Returns a copy of the dictionary containing the glyphs in this SpriteFont.
     * <p>
     * Can be used to calculate character bounds when implementing custom SpriteFont rendering.
     * 
     * @return A new Map containing all of the glyphs in this SpriteFont.
     */
    public Map<Character, Glyph> getGlyphs()
    {
        return new HashMap<Character, Glyph>(_glyphs);
    }

    private List<Character> _characters;

    /**
     * Returns a collection of the characters in the font.
     * 
     * @return A collection of the characters in the font.
     */
    public List<Character> getCharacters()
    {
        return Collections.unmodifiableList(_characters);
    }

    /**
     * Gets or sets the character that will be substituted when a given character is not included in
     * the font.
     */
    public Character defaultCharacter;

    /**
     * Gets or sets the line spacing (the distance from baseline to baseline) of the font.
     */
    public int lineSpacing;

    /**
     * Gets or sets the spacing (tracking) between characters in the font.
     */
    public float spacing;

    /**
     * Returns the size of a string when rendered in this font.
     * 
     * @param text
     *        The text to measure.
     * @return The size, in pixels, of 'text' when rendered in this font.
     */
    public Vector2 measureString(String text)
    {
        CharacterSource source = new CharacterSource(text);
        Vector2 size = new Vector2();
        measureString(source, size);
        return size;
    }

    /**
     * Returns the size of the contents of a StringBuilder when rendered in this font.
     * 
     * @param text
     *        The text to measure.
     * @return The size, in pixels, of 'text' when rendered in this font.
     */
    public Vector2 measureString(StringBuilder text)
    {
        CharacterSource source = new CharacterSource(text);
        Vector2 size = new Vector2();
        measureString(source, size);
        return size;
    }

    void measureString(final CharacterSource text, Vector2 size)
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

        Glyph currentGlyph = new Glyph();
        Vector2 offset = Vector2.Zero();
        boolean firstGlyphOfLine = true;

        for (int i = 0; i < text.length; ++i)
        {
            char c = text.getChar(i);

            if (c == '\r')
                continue;

            if (c == '\n')
            {
                finalLineHeight = lineSpacing;

                offset.x = 0;
                offset.y += lineSpacing;
                firstGlyphOfLine = true;
                continue;
            }

            currentGlyph = _glyphs.get(c);
            if (currentGlyph == null)
            {
                if (defaultGlyph == null)
                    throw new IllegalArgumentException("text: " + SpriteFont.Errors.TextContainsUnresolvableCharacters);

                currentGlyph = defaultGlyph;
            }

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
                offset.x += spacing + currentGlyph.leftSideBearing;
            }

            offset.x += currentGlyph.width;

            float proposedWidth = offset.x + Math.max(currentGlyph.rightSideBearing, 0);
            if (proposedWidth > width)
                width = proposedWidth;

            offset.x += currentGlyph.rightSideBearing;

            if (currentGlyph.cropping.height > finalLineHeight)
                finalLineHeight = currentGlyph.cropping.height;
        }

        size.x = width;
        size.y = offset.y + finalLineHeight;
    }

    // C# struct
    // TODO(Eric): Since this is a struct, should I add a no-argument constructor ?
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
     * Class that defines the spacing, kerning, and bounds of a character.
     * <p>
     * Provides the data necessary to implement custom SpriteFont rendering.
     * 
     * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
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
         * The amount of space between the left side of the character and its first pixel in the X
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

        // TODO(Eric): Do I want this static method and make the inner class Static ?
        // It only returns a new Glyph()
        // public static final Glyph getEmpty() { return new Glyph(); }

        // NOTE(Eric): I added this but it's not used anywhere
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

        // NOTE(Eric): I added this but it's not used anywhere
        @Override
        public int hashCode()
        {
            return (character | (character << 16));
        }

        @Override
        public String toString()
        {
            return "CharacterIndex=" + character + ", Glyph=" + boundsInTexture + ", Cropping=" + cropping + ", Kerning=" + leftSideBearing + "," + width + "," + rightSideBearing;
        }
    }

}
