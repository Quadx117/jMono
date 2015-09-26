package gameCore.components;

import gameCore.Game;
import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.time.GameTime;

// public class GameComponent implements IGameComponent, IUpdateable, Comparable<GameComponent>,
// AutoCloseable {
public class GameComponent extends IUpdateable implements IGameComponent, Comparable<GameComponent>, AutoCloseable
{

	/**
	 * A reference to the main game object
	 */
	protected Game game;
	private boolean isEnabled = true;
	private int updateOrder;

	public Game getGame() { return this.game; }

	public boolean isEnabled() { return isEnabled; }

	// public EnabledChangedEvent setEnabled(boolean value) {
	public void setEnabled(boolean value)
	{
		// EnabledChangedEvent e = null;
		if (isEnabled != value)
		{
			isEnabled = value;

			if (this.enabledChanged != null)
				this.enabledChanged.accept(this, EventArgs.Empty);
			// e = new EnabledChangedEvent(this, null);
			onEnabledChanged(this, null);
		}
		// return e;
	}

	public int getUpdateOrder() { return updateOrder; }

	// public UpdateOrderChangedEvent setUpdateOrder(int value) {
	public void setUpdateOrder(int value)
	{
		// UpdateOrderChangedEvent e = null;
		if (updateOrder != value)
		{
			updateOrder = value;

			if (this.updateOrderChanged != null)
				this.updateOrderChanged.accept(this, EventArgs.Empty);
			// e = new UpdateOrderChangedEvent(this, null);
			onUpdateOrderChanged(this, null);
		}
		// return e;
	}

	// TODO: take care of event handling
	public EventHandler<EventArgs> enabledChanged;
	public EventHandler<EventArgs> updateOrderChanged;

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

	// TODO: EVENTS
	protected void onUpdateOrderChanged(Object sender, EventArgs args) {}

	protected void onEnabledChanged(Object sender, EventArgs args) {}

	// protected UpdateOrderChangedEvent onUpdateOrderChanged(Object sender, Object args) {
	// return new UpdateOrderChangedEvent (sender, args);
	// }

	// protected EnabledChangedEvent onEnabledChanged(Object sender, Object args) {
	// return new EnabledChangedEvent(sender, args);
	// }
	
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

	/*
	 * @Override
	 * public EnabledChangedEvent enabledChanged(Object source) {
	 * // TODO: Need to revisit all the Event code to see if it works as expected
	 * return null;
	 * }
	 * 
	 * @Override
	 * public UpdateOrderChangedEvent updateOrderChanged(Object source, Object args) {
	 * // TODO: Need to revisit all the Event code to see if it works as expected
	 * return null;
	 * }
	 */
}
