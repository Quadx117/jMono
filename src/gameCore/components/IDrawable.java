package gameCore.components;

import gameCore.Game;
import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.time.GameTime;

/**
 * 
 * @author Eric Perron (inspired by XNA framework from Microsoft)
 * 
 */
public abstract class IDrawable extends GameComponent
{ // extends IVisibleChanged, IDrawOrderChanged {

	public IDrawable(Game game)
	{
		super(game);
	}

	public abstract int getDrawOrder();

	public abstract boolean isVisible();

	// TODO: take care of the event handling
	// TODO: This is busted. Need to find a way to make this work for the Game class.
	public EventHandler<EventArgs> drawOrderChanged;
	public EventHandler<EventArgs> visibleChanged;

	public abstract void draw(GameTime gameTime);

}
