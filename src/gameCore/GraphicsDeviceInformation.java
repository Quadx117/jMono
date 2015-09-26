package gameCore;

import gameCore.graphics.GraphicsAdapter;
import gameCore.graphics.GraphicsProfile;
import gameCore.graphics.PresentationParameters;

/**
 * The settings used in creation of the graphics device
 * See {@link GraphicsDeviceManager.preparingDeviceSettings}.
 * 
 * @author Eric
 *
 */
public class GraphicsDeviceInformation {

	/**
	 * The graphics adapter on which the graphics device will be created.
	 * This is only valid on desktop systems where multiple graphics adapters are possible.
	 * Defaults to {@link #GraphicsAdapter.getDefaultAdapter()}.
	 */
	public GraphicsAdapter adapter;

	/**
	 * The requested graphics device feature set.
	 */
	public GraphicsProfile graphicsProfile;

	/**
	 * The settings that define how graphics will be presented to the display.
	 */
	public PresentationParameters presentationParameters;
}
