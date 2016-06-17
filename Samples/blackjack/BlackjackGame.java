package blackjack;

import jMono_Framework.Game;
import jMono_Framework.GraphicsDeviceManager;
import jMono_Framework.Rectangle;
import jMono_Framework.time.GameTime;
import blackjack.cardsFramework.screenManager.ScreenManager;
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

	/** A FPS counter */
	private FPS_Counter fpsCounter;

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

		setMouseVisible(true);
		// getComponents().add(new GamerServicesComponent(this));

		// TODO: Handle sound
		// Initialize sound system
		// AudioManager.Initialize(this);
		
		fpsCounter = new FPS_Counter(this);
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
		// TODO: handle sound
		// AudioManager.loadSounds();

		super.loadContent();
	}

	// TODO: Get rid of this update call once the game is finished
	@Override
	public void update(GameTime gameTime)
	{
		super.update(gameTime);

		fpsCounter.update(gameTime);
	}

	// TODO: Get rid of this draw call once the game is finished
	@Override
	protected void draw(GameTime gameTime)
	{
		super.draw(gameTime);

		fpsCounter.draw(gameTime);
	}
}
