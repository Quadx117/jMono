package jMono_Framework.input;

import java.util.List;

/**
 * Allows getting keystrokes from keyboard.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Keyboard
{
    /**
     * Returns the current keyboard state.
     * 
     * @return Current keyboard state.
     */
    public static KeyboardState getState()
    {
        return platformGetState();
    }

    // @formatter:off
    //  #######################################################################
    //  #                         Keyboard.Default.cs                         #
    //  #######################################################################
    // @formatter:on

    static List<Keys> _keys;

    private static KeyboardState platformGetState()
    {
        return new KeyboardState(_keys);
    }

    public static void setKeys(List<Keys> keys)
    {
        _keys = keys;
    }

    // @formatter:off
    //  #######################################################################
    //  #                         Keyboard.Windows.cs                         #
    //  #######################################################################
    // @formatter:on

    // TODO(Eric): Java specific code (see Keyboard.Windows.cs if needed)
}
