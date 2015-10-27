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

// TODO: Mouse Inputs
// TODO: Sound
// TODO: Finish validating Java keycode vs .NET (Keys.java)
// TODO: add Font anti-aliasing to software renderer ?
// TODO: frame doesn't have keyboard focus at start up
// TODO: Should probably create a separate git repo for the Shooter game code.
