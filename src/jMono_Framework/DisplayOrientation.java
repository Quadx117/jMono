package jMono_Framework;

/**
 * Defines the orientation of the display.
 * 
 * @author Eric
 *
 */
public enum DisplayOrientation
{
	/**
	 * The default orientation.
	 */
	Default(0),

	/**
	 * The display is rotated counterclockwise into a landscape orientation. Width is greater than
	 * height.
	 */
	LandscapeLeft(1),

	/**
	 * The display is rotated clockwise into a landscape orientation. Width is greater than height.
	 */
	LandscapeRight(2),

	/**
	 * The display is rotated as portrait, where height is greater than width.
	 */
	Portrait(4),

	/**
	 * The display is rotated as inverted portrait, where height is greater than width.
	 */
	PortraitDown(8),

	/**
	 * Unknown display orientation.
	 */
	Unknown(16);

	// NOTE: This is for flags
	private final int value;

	private DisplayOrientation(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
