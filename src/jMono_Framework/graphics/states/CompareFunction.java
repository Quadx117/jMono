package jMono_Framework.graphics.states;

import java.util.HashMap;
import java.util.Map;

/**
 * The comparison function used for depth, stencil, and alpha tests.
 * 
 * @author Eric
 *
 */
public enum CompareFunction
{
	/**
	 * Always passes the test.
	 */
	Always,

	/**
	 * Never passes the test.
	 */
	Never,

	/**
	 * Passes the test when the new pixel value is less than current pixel value.
	 */
	Less,

	/**
	 * Passes the test when the new pixel value is less than or equal to current pixel value.
	 */
	LessEqual,

	/**
	 * Passes the test when the new pixel value is equal to current pixel value.
	 */
	Equal,

	/**
	 * Passes the test when the new pixel value is greater than or equal to current pixel value.
	 */
	GreaterEqual,

	/**
	 * Passes the test when the new pixel value is greater than current pixel value.
	 */
	Greater,

	/**
	 * Passes the test when the new pixel value does not equal to current pixel value.
	 */
	NotEqual;

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, CompareFunction> map = new HashMap<Integer, CompareFunction>();

	static
	{
		for (CompareFunction compareFunction : CompareFunction.values())
		{
			map.put(compareFunction.ordinal(), compareFunction);
		}
	}

	public static CompareFunction valueOf(int compareFunction)
	{
		return map.get(compareFunction);
	}
}
