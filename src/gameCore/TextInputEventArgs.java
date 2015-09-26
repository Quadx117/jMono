package gameCore;

import gameCore.events.EventArgs;

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
