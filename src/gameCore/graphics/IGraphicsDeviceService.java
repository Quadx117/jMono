package gameCore.graphics;

import gameCore.events.EventArgs;
import gameCore.events.EventHandler;

public abstract class IGraphicsDeviceService {

	public abstract GraphicsDevice getGraphicsDevice();

	protected EventHandler<EventArgs> deviceCreated;
	protected EventHandler<EventArgs> deviceDisposing;
	protected EventHandler<EventArgs> deviceReset;
	protected EventHandler<EventArgs> deviceResetting;
}
