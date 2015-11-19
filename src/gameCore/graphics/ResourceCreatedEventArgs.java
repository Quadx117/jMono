package gameCore.graphics;

import gameCore.dotNet.events.EventArgs;

public class ResourceCreatedEventArgs extends EventArgs {

	// public Object Resource { get; internal set; }
	/**
	 * The newly created resource object.
	 */
	public Object resource;
}
