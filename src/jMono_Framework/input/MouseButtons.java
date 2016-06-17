package jMono_Framework.input;

/**
 * Specifies constants that define which mouse button was pressed.
 * 
 * @author Eric Perron
 *
 */
public enum MouseButtons
{
	/**
	 * No mouse button was pressed.
	 */
	None(0),

	/**
	 * The left mouse button was pressed.
	 */
	Left(1),

	/**
	 * The right mouse button was pressed.
	 */
	Right(2),

	/**
	 * The middle mouse button was pressed.
	 */
	Middle(4),

	/**
	 * The first XButton was pressed.
	 */
	XButton1(8),

	/**
	 * The second XButton was pressed.
	 */
	XButton2(16);

	// NOTE: This is for flags
	private final int value;

	private MouseButtons(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
