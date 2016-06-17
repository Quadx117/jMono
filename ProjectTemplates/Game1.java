package game;

import gameCore.Color;
import gameCore.Game;
import gameCore.GraphicsDeviceManager;
import gameCore.graphics.SpriteBatch;
import gameCore.input.Keyboard;
import gameCore.input.KeyboardState;
import gameCore.input.Keys;
import gameCore.time.GameTime;

import javax.swing.UIManager;

public class Game1 extends Game
{
	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	GraphicsDeviceManager graphics;
	SpriteBatch spriteBatch;

	// Keyboard states used to determine key presses
	KeyboardState currentKeyboardState = new KeyboardState();
	KeyboardState previousKeyboardState = new KeyboardState();
		
	public Game1()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		graphics = new GraphicsDeviceManager(this);
		getContent().setRootDirectory("Content");
	}

	protected void initialize()
	{
		// TODO: Add your initialization logic here

		super.initialize();
	}

	protected void loadContent()
	{
		// Create a new SpriteBatch, which can be used to draw textures.
		spriteBatch = new SpriteBatch(getGraphicsDevice());

		// TODO: use this.Content to load your game content here
	}

	protected void unloadContent()
	{
		// TODO: Unload any non ContentManager content here
	}

	protected void update(GameTime gameTime)
	{
		// Save the previous state of the Keyboard and GamePad so we can determine single key/button presses
		previousKeyboardState = currentKeyboardState;

		// Read the current state of the Keyboard and GamePad and store it
		// currentGamePadState = GamePad.GetState(PlayerIndex.One);
		currentKeyboardState = Keyboard.getState();

		// Allows the game to exit
		// if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed ||
		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Escape))
			this.exit();

		// TODO: Add your update logic here

		super.update(gameTime);
	}

	protected void draw(GameTime gameTime)
	{
		getGraphicsDevice().clear(Color.CornflowerBlue);

		// TODO: Add your drawing code here

		super.draw(gameTime);
	}

}
