package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a culling mode for faces in rasterization process.
 * 
 * @author Eric
 *
 */
public enum CullMode
{
	/**
	 * Do not cull faces.
	 */
	None,

	/**
	 * Cull faces with clockwise order.
	 */
	CullClockwiseFace,

	/**
	 * Cull faces with counter clockwise order.
	 */
	CullCounterClockwiseFace;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, CullMode> map = new HashMap<Integer, CullMode>();

	static
	{
		for (CullMode cullMode : CullMode.values())
		{
			map.put(cullMode.ordinal(), cullMode);
		}
	}

	public static CullMode valueOf(int cullMode)
	{
		return map.get(cullMode);
	}
}
