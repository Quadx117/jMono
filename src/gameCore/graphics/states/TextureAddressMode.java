package gameCore.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines modes for addressing texels using texture coordinates that are outside of the range of
 * 0.0 to 1.0.
 * 
 * @author Eric
 *
 */
public enum TextureAddressMode
{
	/**
	 * Texels outside range will form the tile at every integer junction.
	 */
	Wrap,

	/**
	 * Texels outside range will be set to color of 0.0 or 1.0 texel.
	 */
	Clamp,

	/**
	 * Same as <see cref="TextureAddressMode.Wrap"/> but tiles will also flipped at every integer
	 * junction.
	 */
	Mirror,

	/**
	 * Texels outside range will be set to the border color.
	 */
	Border;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, TextureAddressMode> map = new HashMap<Integer, TextureAddressMode>();

	static
	{
		for (TextureAddressMode textureAddressMode : TextureAddressMode.values())
		{
			map.put(textureAddressMode.ordinal(), textureAddressMode);
		}
	}

	public static TextureAddressMode valueOf(int textureAddressMode)
	{
		return map.get(textureAddressMode);
	}
}
