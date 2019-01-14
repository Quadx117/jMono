package jMono_Framework;

import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.input.Keys;

/**
 * This class is used for the game window's TextInput event as EventArgs.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class TextInputEventArgs extends EventArgs
{
    char character;

    public TextInputEventArgs(char character)
    {
        this(character, Keys.None);
    }

    public TextInputEventArgs(char character, Keys key)
    {
        this.character = character;
        this.key = key;
    }

    public char getCharacter()
    {
        return character;
    }

    private Keys key;

    public Keys getKey()
    {
        return key;
    }
}
