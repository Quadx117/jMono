package gameCore;

import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.input.MouseEventArgs;

import javax.swing.JFrame;

public class JFrameGameFrame extends JFrame
{
	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	// Mouse events
	Event<MouseEventArgs> mouseWheel = new Event<MouseEventArgs>();
	Event<EventArgs> mouseEnter = new Event<EventArgs>();
	Event<EventArgs> mouseLeave = new Event<EventArgs>();

	// Keyboard event
	Event<EventArgs> keyPress = new Event<EventArgs>();

	// Frame events
	Event<EventArgs> activated = new Event<EventArgs>();
	Event<EventArgs> deactivate = new Event<EventArgs>();
	Event<EventArgs> clientSizeChanged = new Event<EventArgs>();
}
