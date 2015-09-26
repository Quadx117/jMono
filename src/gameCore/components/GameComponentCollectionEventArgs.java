package gameCore.components;

import gameCore.events.EventArgs;

public class GameComponentCollectionEventArgs extends EventArgs
{

	private IGameComponent gameComponent;

	public GameComponentCollectionEventArgs(IGameComponent gameComponent)
	{
		this.gameComponent = gameComponent;
	}

	public IGameComponent getGameComponent()
	{
		return gameComponent;
	}

}
