package gameCore.events;

import java.util.EventListener;

public interface EnabledChangedListener extends EventListener {
	
	public void enabledChanged(EnabledChangedEvent e);
}
