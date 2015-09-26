package gameCore.events;

import java.util.EventObject;

public class UpdateOderChangedEvent extends EventObject implements IUpdateOrderChanged {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	private UpdateOderChangedEvent(Object source, Object args) {
		super(source);
	}

	@Override
	public UpdateOrderChangedEvent updateOrderChanged(Object source, Object args) {
		return new UpdateOrderChangedEvent(source, args);
	}

}
