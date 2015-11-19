package gameCore.components;

import gameCore.Game;
import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.graphics.GraphicsDevice;
import gameCore.time.GameTime;

import java.awt.Graphics;

public class DrawableGameComponent extends GameComponent implements IDrawable
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
				drawOrderChanged.handleEvent(this, null);
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
				visibleChanged.handleEvent(this, EventArgs.Empty);
			}
			onVisibleChanged(this, EventArgs.Empty);
		}
	}

	public Event<EventArgs> drawOrderChanged = new Event<EventArgs>();
	@Override
	public Event<EventArgs> getDrawOrderChanged() { return drawOrderChanged; }

	public Event<EventArgs> visibleChanged = new Event<EventArgs>();
	@Override
	public Event<EventArgs> getVisibleChanged() { return visibleChanged; }

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
}
