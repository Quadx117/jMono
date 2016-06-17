package jMono_Framework;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;

import javax.swing.JFrame;

public class JFrameGameFrame extends JFrame
{
	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	// Keyboard event
	Event<EventArgs> keyPress = new Event<EventArgs>();

	// Frame events
	Event<EventArgs> activated = new Event<EventArgs>();
	Event<EventArgs> deactivate = new Event<EventArgs>();
	Event<EventArgs> clientSizeChanged = new Event<EventArgs>();
}
