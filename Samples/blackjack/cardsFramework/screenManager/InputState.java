package blackjack.cardsFramework.screenManager;

import jMono_Framework.input.Keyboard;
import jMono_Framework.input.KeyboardState;
import jMono_Framework.input.Keys;

/**
 * Helper class for reading input from keyboard, gamepad, and touch input. This
 * class tracks both the current and previous state of the input devices, and
 * implements query methods for high level input actions such as "move up through
 * the menu" or "pause the game".
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class InputState
{

	public final int MAX_INPUTS = 4;

	public KeyboardState currentKeyboardStates;
	// public KeyboardState[] CurrentKeyboardStates;
	// public GamePadState[] CurrentGamePadStates;

	public KeyboardState lastKeyboardStates;

	// public KeyboardState[] LastKeyboardStates;
	// public GamePadState[] LastGamePadStates;

	// public boolean[] gamePadWasConnected;

	// #if WINDOWS_PHONE
	// public TouchCollection TouchState;

	// public readonly List<GestureSample> Gestures = new List<GestureSample>();
	// #endif

	/**
	 * Constructs a new input state.
	 */
	public InputState()
	{
		currentKeyboardStates = new KeyboardState();
		// CurrentKeyboardStates = new KeyboardState[MAX_INPUTS];
		// CurrentGamePadStates = new GamePadState[MAX_INPUTS];

		lastKeyboardStates = new KeyboardState();
		// LastKeyboardStates = new KeyboardState[MAX_INPUTS];
		// LastGamePadStates = new GamePadState[MAX_INPUTS];

		// gamePadWasConnected = new boolean[MAX_INPUTS];
	}

	// / <summary>
	// / Reads the latest state of the keyboard and gamepad.
	// / </summary>

	public void update()
	{
		lastKeyboardStates = currentKeyboardStates;
		currentKeyboardStates = Keyboard.getState();
		
/*		for (int i = 0; i < MAX_INPUTS; ++i)
		{
			LastKeyboardStates[i] = CurrentKeyboardStates[i];
			LastGamePadStates[i] = CurrentGamePadStates[i];

			CurrentKeyboardStates[i] = Keyboard.GetState((PlayerIndex) i);
			CurrentGamePadStates[i] = GamePad.GetState((PlayerIndex) i);

			// Keep track of whether a gamepad has ever been
			// connected, so we can detect if it is unplugged.
			if (CurrentGamePadStates[i].IsConnected)
			{
				GamePadWasConnected[i] = true;
			}
		}
#if WINDOWS_PHONE
		TouchState = TouchPanel.GetState();

		Gestures.Clear();
		while (TouchPanel.IsGestureAvailable)
		{
			Gestures.Add(TouchPanel.ReadGesture());
		}
#endif
*/
	}

	/**
	 * Helper for checking if a key was newly pressed during this update.
	 * 
	 * @param key
	 *        The key that we want to check for.
	 * @return Whether or not the specified key was newly pressed.
	 */
	public boolean isNewKeyPress(Keys key)
	{
		// if (controllingPlayer != null)
		// {
			// Read input from the specified player.
		//	playerIndex = controllingPlayer.Value;

		//	int i = (int)playerIndex;

		return (currentKeyboardStates.isKeyDown(key) &&
			    lastKeyboardStates.isKeyUp(key));
		// }
        // else
        // {
            // Accept input from any player.
        //  return (IsNewKeyPress(key, PlayerIndex.One, out playerIndex) ||
        //            IsNewKeyPress(key, PlayerIndex.Two, out playerIndex) ||
        //            IsNewKeyPress(key, PlayerIndex.Three, out playerIndex) ||
        //            IsNewKeyPress(key, PlayerIndex.Four, out playerIndex));
		// }
	}

	/// <summary>
    /// Helper for checking if a button was newly pressed during this update.
    /// The controllingPlayer parameter specifies which player to read input for.
    /// If this is null, it will accept input from any player. When a button press
    /// is detected, the output playerIndex reports which player pressed it.
    /// </summary>
/*    public boolean isNewButtonPress(Buttons button, PlayerIndex? controllingPlayer,
                                                 out PlayerIndex playerIndex)
    {
        if (controllingPlayer.HasValue)
        {
            // Read input from the specified player.
            playerIndex = controllingPlayer.Value;

            int i = (int)playerIndex;

            return (CurrentGamePadStates[i].IsButtonDown(button) &&
                    LastGamePadStates[i].IsButtonUp(button));
        }
        else
        {
            // Accept input from any player.
            return (IsNewButtonPress(button, PlayerIndex.One, out playerIndex) ||
                    IsNewButtonPress(button, PlayerIndex.Two, out playerIndex) ||
                    IsNewButtonPress(button, PlayerIndex.Three, out playerIndex) ||
                    IsNewButtonPress(button, PlayerIndex.Four, out playerIndex));
        }
    }
*/

	/**
	 * Checks for a "menu select" input action.
	 * 
	 * @return
	 */
	public boolean isMenuSelect()
	{
		return isNewKeyPress(Keys.Space) || isNewKeyPress(Keys.Enter);
			// isNewButtonPress(Buttons.A, controllingPlayer, out playerIndex) ||
			// isNewButtonPress(Buttons.Start, controllingPlayer, out playerIndex);
	}

	/**
	 * Checks for a "menu cancel" input action.
	 * 
	 * @return
	 */
	public boolean isMenuCancel()
	{
		return isNewKeyPress(Keys.Escape);
			// isNewButtonPress(Buttons.B, controllingPlayer, out playerIndex) ||
			// isNewButtonPress(Buttons.Back, controllingPlayer, out playerIndex);
	}

	/**
	 * Checks for a "menu up" input action.
	 * 
	 * @return
	 */
	public boolean isMenuUp()
	{

		// PlayerIndex playerIndex;

		return isNewKeyPress(Keys.Up) || isNewKeyPress(Keys.Left);
			// isNewButtonPress(Buttons.DPadLeft, controllingPlayer, out playerIndex) ||
			// isNewButtonPress(Buttons.LeftThumbstickLeft, controllingPlayer, out playerIndex);
	}

	/**
	 * Checks for a "menu down" input action.
	 * 
	 * @return
	 */
	public boolean isMenuDown()
	{
		// PlayerIndex playerIndex;

		return isNewKeyPress(Keys.Down) || isNewKeyPress(Keys.Right);
			// IsNewButtonPress(Buttons.DPadRight, controllingPlayer, out playerIndex) ||
			// IsNewButtonPress(Buttons.LeftThumbstickRight, controllingPlayer, out playerIndex);
	}

	/**
	 * Checks for a "pause the game" input action.
	 * 
	 * @return
	 */
	public boolean isPauseGame()
	{
		// PlayerIndex playerIndex;

		return isNewKeyPress(Keys.Escape);
			// isNewButtonPress(Buttons.Back, controllingPlayer, out playerIndex) ||
			// isNewButtonPress(Buttons.Start, controllingPlayer, out playerIndex);
	}
}
