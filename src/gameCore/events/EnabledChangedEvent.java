package gameCore.events;

import java.util.EventObject;

public class EnabledChangedEvent extends EventObject implements IEnabledChanged {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	private Object args;
	
	public EnabledChangedEvent(Object source, Object args) {
		super(source);
		this.args = args;
	}

	@Override
	public EnabledChangedEvent enabledChanged(Object source) {
		return new EnabledChangedEvent(source, null);
	}
	
	public Object getArgs() {
		return args;
	}

}
