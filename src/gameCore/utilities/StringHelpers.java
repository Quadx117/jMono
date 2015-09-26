package gameCore.utilities;

/**
 * This class contains helper methods that can be used on Strings.
 * These methods are part of the String class in .NET
 * 
 * @author Eric
 *
 */
public class StringHelpers
{
	/**
	 * Make sure we can't instantiate this class.
	 */
	private StringHelpers() {}
	
	/**
	 * Whether or not the specified {@code String} is null or empty.
	 * 
	 * @param s
	 *        the string to test.
	 * @return {@code true} if the String is null or empty, {@code false} otherwise.
	 */
	static public boolean isNullOrEmpty(String s)
	{
		return s == null || s.isEmpty();
	}
}
