package gameCore.events;

import java.util.EventObject;

public class DrawOrderChangedEvent extends EventObject implements IDrawOrderChanged {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	private Object args;

	private DrawOrderChangedEvent(Object source, Object args) {
		super(source);
		this.args = args;
	}

	@Override
	public DrawOrderChangedEvent drawOrderChanged(Object source, Object args) {
		return new DrawOrderChangedEvent(source, args);
	}

	public Object getArgs() {
		return args;
	}

}
