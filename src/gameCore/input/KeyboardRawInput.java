package gameCore.input;

import gameCore.dotNet.events.Event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Gets the raw keyboard inputs and change the key value to the same as in .NET
 * 
 * @author Eric Perron
 *
 */
public class KeyboardRawInput implements KeyListener
{
	public static Event<KeyboardInputEventArgs> keyboardInput = new Event<KeyboardInputEventArgs>();

	@Override
	public void keyPressed(KeyEvent e)
	{
//		 System.out.println("------------START-------------");
//		 System.out.println("getKeyChar():         " + e.getKeyChar());
//		 System.out.println("getModifiers():       " + e.getModifiers());
//		 System.out.println("getExtendedKeyCode(): " + e.getExtendedKeyCode());
//		 System.out.println("getKeyLocation():     " + e.getKeyLocation());

		int key = getKeyValue(e);
		int keyLocation = getKeyLocation(e);
		KeyState state = KeyState.Down;
		keyboardInput.handleEvent(null, new KeyboardInputEventArgs(key, keyLocation, state));
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int key = getKeyValue(e);
		int keyLocation = getKeyLocation(e);
		KeyState state = KeyState.Up;
		keyboardInput.handleEvent(null, new KeyboardInputEventArgs(key, keyLocation, state));
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	// Helper method to convert Java key values to .NET key values
	private int getKeyValue(KeyEvent e)
	{
		// change the key if not the same as .NET
		switch (e.getExtendedKeyCode())
		{
			case KeyEvent.VK_NUMBER_SIGN:
				return 222;
			case KeyEvent.VK_MINUS:
				return 189;
			case KeyEvent.VK_EQUALS:
				return 187;
			case KeyEvent.VK_DEAD_CIRCUMFLEX:
				return 219;
			case KeyEvent.VK_DEAD_CEDILLA:
				return 221;
			case KeyEvent.VK_LESS:
				return 220;
			case KeyEvent.VK_SEMICOLON:
				return 186;
			case KeyEvent.VK_DEAD_GRAVE:
				return 192;
			case KeyEvent.VK_ENTER:
				return 13;
			case KeyEvent.VK_COMMA:
				return 188;
			case KeyEvent.VK_PERIOD:
				return 190;
			case 16777449:
				return 191;
			case KeyEvent.VK_CONTEXT_MENU:
				return 93;
			default:
				return e.getExtendedKeyCode();
		}
	}

	// Helper method to get the key location
	private int getKeyLocation(KeyEvent e)
	{
		switch (e.getExtendedKeyCode())
		{
			case KeyEvent.VK_SHIFT:
				return e.getKeyLocation();
			case KeyEvent.VK_CONTROL:
				return e.getKeyLocation();
			case KeyEvent.VK_WINDOWS:
				return e.getKeyLocation();
			case KeyEvent.VK_ALT:
				return e.getKeyLocation();
			default:
				return KeyEvent.KEY_LOCATION_UNKNOWN;
		}
	}
}
