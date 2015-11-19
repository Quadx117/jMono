package gameCore;

import gameCore.dotNet.events.EventArgs;

/**
 * The arguments to the {@link GraphicsDeviceManager.PreparingDeviceSettings} event.
 * 
 * @author Eric
 *
 */
public class PreparingDeviceSettingsEventArgs extends EventArgs
{
	/**
	 * Create a new instance of the event.
	 * 
	 * @param graphicsDeviceInformation
	 *        The default settings to be used in device creation.
	 */
	public PreparingDeviceSettingsEventArgs(GraphicsDeviceInformation graphicsDeviceInformation)
	{
		this.graphicsDeviceInformation = graphicsDeviceInformation;
	}

	/**
	 * The default settings that will be used in device creation.
	 */
	private GraphicsDeviceInformation graphicsDeviceInformation;

	/**
	 * Gets the default settings that will be used in device creation.
	 * 
	 * @return The default settings that will be used in device creation.
	 */
	public GraphicsDeviceInformation getGraphicsDeviceInformation()
	{
		return graphicsDeviceInformation;
	}
}
