package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines filtering types for texture sampler.
 * 
 * @author Eric
 *
 */
public enum TextureFilter
{
	/**
	 * Use linear filtering.
	 */
	Linear,

	/**
	 * Use point filtering.
	 */
	Point,

	/**
	 * Use anisotropic filtering.
	 */
	Anisotropic,

	/**
	 * Use linear filtering to shrink or expand, and point filtering between mipmap levels (min).
	 */
	LinearMipPoint,

	/**
	 * Use point filtering to shrink (minify) or expand (magnify), and linear filtering between
	 * mipmap levels.
	 */
	PointMipLinear,

	/**
	 * Use linear filtering to shrink, point filtering to expand, and linear filtering between
	 * mipmap levels.
	 */
	MinLinearMagPointMipLinear,

	/**
	 * Use linear filtering to shrink, point filtering to expand, and point filtering between mipmap
	 * levels.
	 */
	MinLinearMagPointMipPoint,

	/**
	 * Use point filtering to shrink, linear filtering to expand, and linear filtering between
	 * mipmap levels.
	 */
	MinPointMagLinearMipLinear,

	/**
	 * Use point filtering to shrink, linear filtering to expand, and point filtering between mipmap
	 * levels.
	 */
	MinPointMagLinearMipPoint;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, TextureFilter> map = new HashMap<Integer, TextureFilter>();

	static
	{
		for (TextureFilter textureFilter : TextureFilter.values())
		{
			map.put(textureFilter.ordinal(), textureFilter);
		}
	}

	public static TextureFilter valueOf(int textureFilter)
	{
		return map.get(textureFilter);
	}
}
