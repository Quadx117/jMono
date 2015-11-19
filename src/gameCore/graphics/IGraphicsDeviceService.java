package gameCore.graphics;

import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;

public interface IGraphicsDeviceService
{
	GraphicsDevice getGraphicsDevice();

	Event<EventArgs> getDeviceCreated();
	Event<EventArgs> getDeviceDisposing();
	Event<EventArgs> getDeviceReset();
	Event<EventArgs> getDeviceResetting();
}
