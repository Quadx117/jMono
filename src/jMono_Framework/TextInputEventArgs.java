package jMono_Framework;

import jMono_Framework.dotNet.events.EventArgs;

/**
 * This class is used for the game window's TextInput event as EventArgs.
 * 
 * @author Eric
 *
 */
public class TextInputEventArgs extends EventArgs
{

	char character;

	public TextInputEventArgs(char character)
	{
		this.character = character;
	}

	public char getCharacter()
	{
		return character;
	}
}
