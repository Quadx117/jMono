package gameCore.input;

import gameCore.dotNet.events.EventArgs;

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

	public MouseButtons getButton()
	{
		return button;
	}

	public int getClicks()
	{
		return clicks;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getDelta()
	{
		return delta;
	}
}
