package blackjack;

import jMono_Framework.Game;
import jMono_Framework.GraphicsDeviceManager;
import jMono_Framework.Rectangle;
import blackjack.misc.AudioManager;
import blackjack.screenManager.ScreenManager;
import blackjack.screens.BackgroundScreen;
import blackjack.screens.MainMenuScreen;

/** This class contains the main methods of our game */
public class BlackjackGame extends Game
{
	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	GraphicsDeviceManager graphics;
	ScreenManager screenManager;

	public static float HEIGHT_SCALE = 1.0f;
	public static float WIDTH_SCALE = 1.0f;

	/**
	 * Initializes a new instance of the game.
	 */
	public BlackjackGame()
	{
		graphics = new GraphicsDeviceManager(this);

		getContent().setRootDirectory("Content");

		screenManager = new ScreenManager(this);

		screenManager.addScreen(new BackgroundScreen());
		screenManager.addScreen(new MainMenuScreen());

		getComponents().add(screenManager);

// #if WINDOWS
		setMouseVisible(true);
// #elif WINDOWS_PHONE
		// Frame rate is 30 fps by default for Windows Phone.
//        setTargetElapsedTime(TimeSpan.fromTicks(333333));
//        graphics.setIsFullScreen(true);
// #else
		// getComponents().add(new GamerServicesComponent(this));
// #endif

		// Initialize sound system
		AudioManager.initialize(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize()
	{
		super.initialize();

// #if XBOX
        // graphics.PreferredBackBufferHeight = graphics.GraphicsDevice.DisplayMode.Height;
        // graphics.PreferredBackBufferWidth = graphics.GraphicsDevice.DisplayMode.Width; 
// #elif WINDOWS
        graphics.setPreferredBackBufferHeight(480);
        graphics.setPreferredBackBufferWidth(800);; 
// #endif   
		graphics.applyChanges();

		Rectangle bounds = graphics.getGraphicsDevice().getViewport().getTitleSafeArea();
		HEIGHT_SCALE = bounds.height / 480.0f;
		WIDTH_SCALE = bounds.width / 800.0f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadContent()
	{
		AudioManager.loadSounds();

		super.loadContent();
	}
}
