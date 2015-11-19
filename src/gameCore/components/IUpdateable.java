package gameCore.components;

import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public interface IUpdateable
{
	void update(GameTime gameTime);

	Event<EventArgs> getEnabledChanged();
	Event<EventArgs> getUpdateOrderChanged();

	boolean isEnabled();

	int getUpdateOrder();

}
