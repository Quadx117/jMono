package gameCore.events;

import java.util.EventObject;

public class VisibleChangedEvent extends EventObject implements IVisibleChanged {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	private Object args;

	private VisibleChangedEvent(Object source, Object args) {
		super(source);
		this.args = args;
	}

	@Override
	public VisibleChangedEvent visibleChanged(Object source) {
		return new VisibleChangedEvent(source, null);
	}

	public Object getArgs() {
		return args;
	}

}
