package gameCore.components;

import gameCore.Game;
import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.graphics.GraphicsDevice;
import gameCore.time.GameTime;

import java.awt.Graphics;

// public class DrawableGameComponent extends GameComponent implements IDrawable {
public class DrawableGameComponent extends IDrawable
{

	private boolean isInitialized;
	private int drawOrder;
	private boolean isVisible = true;

	public GraphicsDevice getGraphicsDevice()
	{
		return this.getGame().getGraphicsDevice();
	}

	public int getDrawOrder()
	{
		return drawOrder;
	}

	public void setDrawOrder(int value)
	{
		if (drawOrder != value)
		{
			drawOrder = value;

			if (drawOrderChanged != null)
			{
				drawOrderChanged.accept(this, null);
			}
			onDrawOrderChanged(this, null);
		}
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean value)
	{
		if (isVisible != value)
		{
			isVisible = value;

			if (visibleChanged != null)
			{
				visibleChanged.accept(this, EventArgs.Empty);
			}
			onVisibleChanged(this, EventArgs.Empty);
		}
	}

	// TODO: take care of event handling
	public EventHandler<EventArgs> drawOrderChanged;
	public EventHandler<EventArgs> visibleChanged;

	public DrawableGameComponent(Game game)
	{
		super(game);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize()
	{
		if (!isInitialized)
		{
			isInitialized = true;
			loadContent();
		}
	}

	/**
	 * Load component content.
	 */
	protected void loadContent() {}

	/**
	 * Unload component content.
	 */
	protected void unloadContent() {}

	/**
	 * Draw the component to the screen.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 */
	public void draw(GameTime gameTime) {}

	/**
	 * Draw the text of the component to the screen.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 * @param g
	 *        The {@code Graphics} object used for rendering text.
	 */
	public void drawText(GameTime gameTime, Graphics g) {}

	protected void onVisibleChanged(Object sender, EventArgs args) {}

	protected void onDrawOrderChanged(Object sender, EventArgs args) {}

	/*
	 * @Override
	 * public VisibleChangedEvent visibleChanged(Object source) {
	 * // TODO: Need to revisit all the Event code to see if it works as expected
	 * return null;
	 * }
	 * 
	 * @Override
	 * public DrawOrderChangedEvent drawOrderChanged(Object source, Object args) {
	 * // TODO: Need to revisit all the Event code to see if it works as expected
	 * return null;
	 * }
	 */

}
