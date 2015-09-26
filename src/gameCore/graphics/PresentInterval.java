package gameCore.graphics;

/**
 * Defines how {@link GraphicsDevice.Present} updates the game window.
 * 
 * @author Eric
 *
 */
public enum PresentInterval
{
	/**
	 * Equivalent to <see cref="PresentInterval.One"/>.
	 */
	Default,

	/**
	 * The driver waits for the vertical retrace period, before updating window client area. Present
	 * operations are not affected more frequently than the screen refresh rate.
	 */
	One,

	/**
	 * The driver waits for the vertical retrace period, before updating window client area. Present
	 * operations are not affected more frequently than every second screen refresh.
	 */
	Two,

	/**
	 * The driver updates the window client area immediately. Present operations might be affected
	 * immediately. There is no limit for frame rate.
	 */
	Immediate;

	// NOTE: This method is from the file GraphicsExtension.cs
	/**
	 * Converts {@link PresentInterval} to OpenGL swap interval.
	 * 
	 * @return A value according to EXT_swap_control
	 */
	public int getSwapInterval()
	{
		// See http://www.opengl.org/registry/specs/EXT/swap_control.txt
		// and https://www.opengl.org/registry/specs/EXT/glx_swap_control_tear.txt
		// OpenTK checks for EXT_swap_control_tear:
		// if supported, a swap interval of -1 enables adaptive vsync;
		// otherwise -1 is converted to 1 (vsync enabled.)

		switch (this)
		{

			case Immediate:
				return 0;
			case One:
				return 1;
			case Two:
				return 2;
			case Default:
			default:
				return -1;
		}
	}
}
