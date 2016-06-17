package jMono_Framework.graphics;

/**
 * Defines sprite visual options for mirroring.
 * 
 * @author Eric
 *
 */
public enum SpriteEffects
{
	/**
	 * No options specified.
	 */
	None(0),

	/**
	 * Render the sprite reversed along the X axis.
	 */
	FlipHorizontally(1),

	/**
	 * Render the sprite reversed along the Y axis.
	 */
	FlipVertically(2);

	// NOTE: This is for flags
	private final int value;

	private SpriteEffects(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
