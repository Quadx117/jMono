package gameCore.input;

import gameCore.events.EventArgs;

public class KeyboardInputEventArgs extends EventArgs
{
	public KeyboardInputEventArgs(int key, int keyLocation, KeyState state)
	{
		this.key = key;
		this.keyLocation = keyLocation;
		this.state = state;
	}

	public int key;

	public int keyLocation;

	public KeyState state;

}
