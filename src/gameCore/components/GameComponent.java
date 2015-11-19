package gameCore.components;

import gameCore.Game;
import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.time.GameTime;

public class GameComponent implements IGameComponent, IUpdateable, Comparable<GameComponent>, AutoCloseable
{
	private boolean isEnabled = true;
	private int updateOrder;

	/**
	 * A reference to the main game object
	 */
	public Game getGame() { return this.game; }
	protected Game game;

	public boolean isEnabled() { return isEnabled; }

	public void setEnabled(boolean value)
	{
		if (isEnabled != value)
		{
			isEnabled = value;

			if (this.enabledChanged != null)
				this.enabledChanged.handleEvent(this, EventArgs.Empty);
			onEnabledChanged(this, null);
		}
	}

	public int getUpdateOrder() { return updateOrder; }

	public void setUpdateOrder(int value)
	{
		if (updateOrder != value)
		{
			updateOrder = value;

			if (this.updateOrderChanged != null)
				this.updateOrderChanged.handleEvent(this, EventArgs.Empty);
			onUpdateOrderChanged(this, null);
		}
	}

	public Event<EventArgs> enabledChanged = new Event<EventArgs>();
	@Override
	public Event<EventArgs> getEnabledChanged() { return enabledChanged; }
	
	public Event<EventArgs> updateOrderChanged = new Event<EventArgs>();
	@Override
	public Event<EventArgs> getUpdateOrderChanged() { return updateOrderChanged; }
	
	public GameComponent(Game game)
	{
		this.game = game;
	}

	@Override
	public void finalize()
	{
		dispose(false);
	}

	/**
	 * Initialize the component.
	 */
	public void initialize() {}

	/**
	 * Updates the component.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 */
	public void update(GameTime gameTime) {}

	protected void onUpdateOrderChanged(Object sender, EventArgs args) {}

	protected void onEnabledChanged(Object sender, EventArgs args) {}

	/**
	 * Shuts down the component.
	 * 
	 * @param disposing
	 */
	protected void dispose(boolean disposing) {}

	/**
	 * Shuts down the component.
	 */
	@Override
	public void close()
	{
		dispose(true);
		// GC.SuppressFinalize(this);
	}

	public int compareTo(GameComponent other)
	{
		return other.getUpdateOrder() - this.updateOrder;
	}
}
