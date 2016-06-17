package jMono_Framework.dotNet.events;

public interface EventHandler<TEventArgs extends EventArgs>
{
	void handleEvent(Object sender, TEventArgs e);
}
