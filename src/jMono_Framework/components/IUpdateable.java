package jMono_Framework.components;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.time.GameTime;

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
