package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines options for filling the primitive.
 * 
 * @author Eric
 *
 */
public enum FillMode
{
	/**
	 * Draw solid faces for each primitive.
	 */
	Solid,

	/**
	 * Draw lines for each primitive.
	 */
	WireFrame;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, FillMode> map = new HashMap<Integer, FillMode>();

	static
	{
		for (FillMode fillMode : FillMode.values())
		{
			map.put(fillMode.ordinal(), fillMode);
		}
	}

	public static FillMode valueOf(int fillMode)
	{
		return map.get(fillMode);
	}
}
