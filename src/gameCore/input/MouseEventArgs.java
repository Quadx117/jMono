package gameCore.input;

import gameCore.Point;
import gameCore.dotNet.events.EventArgs;

/**
 * Provides data for the {@code MouseUp}, {@code MouseDown}, and {@code MouseMove} events.
 * 
 * @author Eric Perron
 *
 */
public class MouseEventArgs extends EventArgs
{
	private MouseButtons button;
	private int clicks;
	private int x;
	private int y;
	private int delta;

	/**
	 * Initializes a new instance of the {@link MouseEventArgs} class.
	 * 
	 * @param button
	 *        One of the {@link MouseButtons} values that indicate which mouse button was pressed.
	 * @param clicks
	 *        The number of times a mouse button was pressed.
	 * @param x
	 *        The x-coordinate of a mouse click, in pixels.
	 * @param y
	 *        The y-coordinate of a mouse click, in pixels.
	 * @param delta
	 *        A signed count of the number of detents the wheel has rotated.
	 */
	public MouseEventArgs(MouseButtons button, int clicks, int x, int y, int delta)
	{
		this.button = button;
		this.clicks = clicks;
		this.x = x;
		this.y = y;
		this.delta = delta;
	}

	/**
	 * Gets which mouse button was pressed.
	 * 
	 * @return The mouse button that was pressed.
	 */
	public MouseButtons getButton()
	{
		return button;
	}

	/**
	 * Gets the number of times the mouse button was pressed and released.
	 * 
	 * @return The number of times the mouse button was pressed and released.
	 */
	public int getClicks()
	{
		return clicks;
	}

	/**
	 * Gets the x-coordinate of the mouse during the generating mouse event.
	 * 
	 * @return The x-coordinate of the mouse during the generating mouse event.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Gets the y-coordinate of the mouse during the generating mouse event.
	 * 
	 * @return The y-coordinate of the mouse during the generating mouse event.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Gets the location of the mouse during the generating mouse event.
	 * 
	 * @return The location of the mouse during the generating mouse event.
	 */
	public Point getLocation()
	{
		return new Point(x, y);
	}

	/**
	 * Gets a signed count of the number of detents the mouse wheel has rotated, multiplied by the
	 * WHEEL_DELTA constant. A detent is one notch of the mouse wheel.
	 * 
	 * @return The number of detents the mouse wheel has rotated.
	 */
	public int getDelta()
	{
		return delta;
	}
}
