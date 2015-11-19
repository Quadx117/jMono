package gameCore.dotNet.events;

/**
 * The base class for all event classes.
 * 
 * @author Eric
 *
 */
public class EventArgs {

	public static EventArgs Empty = new EventArgs();

	public EventArgs() {}
}

/*
 * [Serializable]
 * [System.Runtime.InteropServices.ComVisible(true)]
 * public class EventArgs {
 * public static readonly EventArgs Empty = new EventArgs();
 * 
 * public EventArgs()
 * {
 * }
 * }
 */