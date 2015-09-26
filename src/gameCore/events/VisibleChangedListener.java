package gameCore.events;

import java.util.EventListener;

public interface VisibleChangedListener extends EventListener {

	public void visibleChanged(VisibleChangedEvent e);
}
