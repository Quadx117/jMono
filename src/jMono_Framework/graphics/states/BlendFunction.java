package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a function for color blending.
 * 
 * @author Eric
 *
 */
public enum BlendFunction
{
	/**
	 * The function will adds destination to the source. (srcColor * srcBlend) + (destColor *
	 * destBlend)
	 */
	Add,

	/**
	 * The function will subtracts destination from source. (srcColor * srcBlend) − (destColor *
	 * destBlend)
	 */
	Subtract,

	/**
	 * The function will subtracts source from destination. (destColor * destBlend) - (srcColor *
	 * srcBlend)
	 */
	ReverseSubtract,

	/**
	 * The function will extracts minimum of the source and destination. min((srcColor *
	 * srcBlend),(destColor * destBlend))
	 */
	Max,

	/**
	 * The function will extracts maximum of the source and destination. max((srcColor *
	 * srcBlend),(destColor * destBlend))
	 */
	Min;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, BlendFunction> map = new HashMap<Integer, BlendFunction>();

	static
	{
		for (BlendFunction blendFunction : BlendFunction.values())
		{
			map.put(blendFunction.ordinal(), blendFunction);
		}
	}

	public static BlendFunction valueOf(int blendFunction)
	{
		return map.get(blendFunction);
	}
}
