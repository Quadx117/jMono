package jMono_Framework.graphics;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;

public interface IGraphicsDeviceService
{
	GraphicsDevice getGraphicsDevice();

	Event<EventArgs> getDeviceCreated();
	Event<EventArgs> getDeviceDisposing();
	Event<EventArgs> getDeviceReset();
	Event<EventArgs> getDeviceResetting();
}
