package shooter;

/**
 * This class is the main entry point of the program. It contains
 * the main method to launch the game.
 */
public class Main
{
	public static void main(String[] args)
	{
		try (ShooterGame game = new ShooterGame())
		{
			game.run();
		}
	}
}

