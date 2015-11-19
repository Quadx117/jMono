package gameCore.dotNet.events;

public interface EventHandler<TEventArgs extends EventArgs>
{
	void handleEvent(Object sender, TEventArgs e);
}
