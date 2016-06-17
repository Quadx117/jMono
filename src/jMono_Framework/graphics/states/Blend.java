package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a blend mode.
 * 
 * @author Eric
 *
 */
public enum Blend
{
	/**
	 * Each component of the color is multiplied by (1, 1, 1, 1).
	 */
	One,

	/**
	 * Each component of the color is multiplied by (0, 0, 0, 0).
	 */
	Zero,

	/**
	 * Each component of the color is multiplied by the source color.
	 * This can be represented as (Rs, Gs, Bs, As), where R, G, B, and A
	 * respectively stand for the red, green, blue, and alpha source values.
	 */
	SourceColor,

	/**
	 * Each component of the color is multiplied by the inverse of the source color.
	 * This can be represented as (1 - Rs, 1 - Gs, 1 - Bs, 1 - As) where R, G, B, and A
	 * respectively stand for the red, green, blue, and alpha destination values.
	 */
	InverseSourceColor,

	/**
	 * Each component of the color is multiplied by the alpha value of the source.
	 * This can be represented as (As, As, As, As), where As is the alpha source value.
	 */
	SourceAlpha,

	/**
	 * Each component of the color is multiplied by the inverse of the alpha value of the source.
	 * This can be represented as (1 - As, 1 - As, 1 - As, 1 - As), where As is the alpha
	 * destination value.
	 */
	InverseSourceAlpha,

	/**
	 * Each component color is multiplied by the destination color.
	 * This can be represented as (Rd, Gd, Bd, Ad), where R, G, B, and A
	 * respectively stand for red, green, blue, and alpha destination values.
	 */
	DestinationColor,

	/**
	 * Each component of the color is multiplied by the inverse of the destination color.
	 * This can be represented as (1 - Rd, 1 - Gd, 1 - Bd, 1 - Ad), where Rd, Gd, Bd, and Ad
	 * respectively stand for the red, green, blue, and alpha destination values.
	 */
	InverseDestinationColor,

	/**
	 * Each component of the color is multiplied by the alpha value of the destination.
	 * This can be represented as (Ad, Ad, Ad, Ad), where Ad is the destination alpha value.
	 */
	DestinationAlpha,

	/**
	 * Each component of the color is multiplied by the inverse of the alpha value of the source.
	 * This can be represented as (1 - As, 1 - As, 1 - As, 1 - As), where As is the alpha
	 * destination value.
	 */
	InverseDestinationAlpha,

	/**
	 * Each component of the color is multiplied by a constant set in BlendFactor.
	 */
	BlendFactor,

	/**
	 * Each component of the color is multiplied by the inverse of a constant set in BlendFactor.
	 */
	InverseBlendFactor,

	/**
	 * Each component of the color is multiplied by either the alpha of the source color, or the
	 * inverse of the alpha of the source color, whichever is greater.
	 * This can be represented as (f, f, f, 1), where f = min(A, 1 - Ad).
	 */
	SourceAlphaSaturation;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, Blend> map = new HashMap<Integer, Blend>();

	static
	{
		for (Blend blend : Blend.values())
		{
			map.put(blend.ordinal(), blend);
		}
	}

	public static Blend valueOf(int blend)
	{
		return map.get(blend);
	}
}
