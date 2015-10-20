package gameCore.input;

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

    /// <summary>
    /// Returns the current keyboard state.
    /// </summary>
    /// <returns>Current keyboard state.</returns>
	public static KeyboardState getState()
	{
        return new KeyboardState(_keys);
	}
	
    public static void setKeys(List<Keys> keys)
    {
        _keys = keys;
    }
}
