package blackjack;

/**
 * This class is the main entry point of the program. It contains
 * the main method to launch the game.
 */
public class Main
{
	public static void main(String[] args)
	{
		try (BlackjackGame game = new BlackjackGame())
		{
			game.run();
		}
	}
}

// TODO: hitting "esc" then pressing the "Quit" button wile playing the game doesn't work