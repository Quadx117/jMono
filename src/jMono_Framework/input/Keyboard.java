package jMono_Framework.input;

import java.util.List;

/**
 * Allows getting keystrokes from keyboard.
 * 
 * @author Eric Perron
 *
 */
public class Keyboard
{
	static List<Keys> _keys;

	/**
	 * Returns the current keyboard state.
	 * 
	 * @return Current keyboard state.
	 */
	public static KeyboardState getState()
	{
		return new KeyboardState(_keys);
	}

	public static void setKeys(List<Keys> keys)
	{
		_keys = keys;
	}
}
