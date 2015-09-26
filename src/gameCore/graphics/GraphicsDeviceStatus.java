package gameCore.graphics;

/**
 * Describes the status of the {@link GraphicsDevice}.
 * 
 * @author Eric
 *
 */
public enum GraphicsDeviceStatus
{
	/**
	 * The device is normal.
	 */
	Normal,
	
	/**
	 * The device has been lost.
	 */
	Lost,
	
	/**
	 * The device has not been reset.
	 */
	NotReset
}
