package gameCore.components;

import gameCore.events.EventHandler;

import java.util.ArrayList;

public class GameComponentCollection extends ArrayList<IGameComponent>
{

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Event that is triggered when a {@link GameComponent} is added to this
	 * {@code GameComponentCollection}.
	 */
	public EventHandler<GameComponentCollectionEventArgs> componentAdded;

	/**
	 * Event that is triggered when a {@link GameComponent} is removed from this
	 * {@code GameComponentCollection}.
	 */
	public EventHandler<GameComponentCollectionEventArgs> componentRemoved;

	/**
	 * Removes every {@link GameComponent} from this {@code GameComponentCollection}.
	 * Triggers {@link #onComponentRemoved()} once for each {@link GameComponent} removed.
	 */
	@Override
	public void clear()
	{
		for (int i = 0; i < super.size(); ++i)
		{
			this.onComponentRemoved(new GameComponentCollectionEventArgs(super.get(i)));
		}
		super.clear();
	}

	// TODO: This is not part of the original code , yet it worked and I don't know how.
	@Override
	public boolean add(IGameComponent item)
	{
		if (super.indexOf(item) != -1)
		{
			throw new IllegalArgumentException("Cannot Add Same Component Multiple Times");
		}

		boolean result = super.add(item);

		if (item != null)
		{
			this.onComponentAdded(new GameComponentCollectionEventArgs(item));
		}

		return result;
	}

	@Override
	public void add(int index, IGameComponent item) throws IllegalArgumentException
	{
		if (super.indexOf(item) != -1)
		{
			throw new IllegalArgumentException("Cannot Add Same Component Multiple Times");
		}
		super.add(index, item);
		if (item != null)
		{
			this.onComponentAdded(new GameComponentCollectionEventArgs(item));
		}
	}

	private void onComponentAdded(GameComponentCollectionEventArgs eventArgs)
	{
		if (this.componentAdded != null)
		{
			this.componentAdded.accept(this, eventArgs);
		}
	}

	private void onComponentRemoved(GameComponentCollectionEventArgs eventArgs)
	{
		if (this.componentRemoved != null)
		{
			this.componentRemoved.accept(this, eventArgs);
		}
	}

	@Override
	public IGameComponent remove(int index)
	{
		IGameComponent gameComponent = super.remove(index);
		if (gameComponent != null)
		{
			this.onComponentRemoved(new GameComponentCollectionEventArgs(gameComponent));
		}
		return gameComponent;
	}

	/**
	 * This operation is not supported.
	 */
	@Override
	public IGameComponent set(int index, IGameComponent item) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
