package gameCore.dotNet.events;

import java.util.ArrayList;
import java.util.List;

public class Event<TEventArgs extends EventArgs> implements EventHandler<TEventArgs>
{
	List<EventHandler<TEventArgs>> list = new ArrayList<EventHandler<TEventArgs>>();

	public void add(EventHandler<TEventArgs> handler)
	{
		list.add(handler);
	}

	public void remove(EventHandler<TEventArgs> handler)
	{
		list.remove(handler);
	}

	public void handleEvent(Object sender, TEventArgs e)
	{
		for (int i = 0; i < list.size(); ++i)
		{
			list.get(i).handleEvent(sender, e);
		}
	}
}
