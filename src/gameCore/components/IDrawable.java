package gameCore.components;

import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public interface IDrawable
{
	int getDrawOrder();

	boolean isVisible();

	Event<EventArgs> getDrawOrderChanged();
	Event<EventArgs> getVisibleChanged();

	void draw(GameTime gameTime);

}
