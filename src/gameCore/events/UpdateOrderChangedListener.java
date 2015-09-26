package gameCore.events;

import java.util.EventListener;

public interface UpdateOrderChangedListener extends EventListener {

	public void updateOrderChanged(UpdateOrderChangedEvent e);
}
