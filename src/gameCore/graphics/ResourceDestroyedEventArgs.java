package gameCore.graphics;

import gameCore.dotNet.events.EventArgs;

public class ResourceDestroyedEventArgs extends EventArgs {

	// public string Name { get; internal set; }
	/**
	 * The name of the destroyed resource.
	 */
	public String name;

	// public Object Tag { get; internal set; }
	/**
	 * The resource manager tag of the destroyed resource.
	 */
	public Object Tag;
}
