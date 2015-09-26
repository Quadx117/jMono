package gameCore.events;

import java.util.EventObject;

public class UpdateOrderChangedEvent extends EventObject implements IUpdateOrderChanged {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	private Object args;

	public UpdateOrderChangedEvent(Object source, Object args) {
		super(source);
		this.args = args;
	}
	
	@Override
	public UpdateOrderChangedEvent updateOrderChanged(Object source, Object args) {
		return new UpdateOrderChangedEvent(source, args);
	}
	
	public Object getArgs() {
		return args;
	}

}
