package gameCore.components;

import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public abstract class IUpdateable
{ // extends IEnabledChanged, IUpdateOrderChanged {

	public abstract void update(GameTime gameTime);

	// TODO: take care of the event handling
	public EventHandler<EventArgs> enabledChanged;
	public EventHandler<EventArgs> updateOrderChanged;

	public abstract boolean isEnabled();

	public abstract int getUpdateOrder();


}
