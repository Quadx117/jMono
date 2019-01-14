package jMono_Framework.graphics;

/**
 * Describes the status of the {@link GraphicsDevice}.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
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
