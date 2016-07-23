package blackjack.screens;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.input.ButtonState;
import jMono_Framework.input.Mouse;
import jMono_Framework.input.MouseState;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;
import blackjack.screenManager.GameScreen;

public class InstructionScreen extends GameplayScreen
{
	Texture2D background;
	SpriteFont font;
	GameplayScreen gameplayScreen;
	String theme;
	boolean isExit = false;
	boolean isExited = false;

	public InstructionScreen(String theme)
	{
		super("");
		transitionOnTime = TimeSpan.fromSeconds(0.0);
		transitionOffTime = TimeSpan.fromSeconds(0.5);

		this.theme = theme;
// #if WINDOWS_PHONE
		// EnabledGestures = GestureType.Tap;
// #endif
	}

	/**
	 * Load the screen resources.
	 */
	@Override
	public void loadContent()
	{
		background = load("images/instructions", Texture2D.class);
		font = load("fonts/menuFont_2", SpriteFont.class);

		// Create a new instance of the gameplay screen
		gameplayScreen = new GameplayScreen(theme);
	}

	/**
	 * Exit the screen after a tap or click.
	 * 
	 * @param mouseState
	 */
	private void handleInput(MouseState mouseState) // , GamePadState padState)
	{
		if (!isExit)
		{
// #if WINDOWS_PHONE
		// if (ScreenManager.input.Gestures.Count > 0 &&
		// ScreenManager.input.Gestures[0].GestureType == GestureType.Tap)
		// {
		// isExit = true;
		// }
// #else
			// PlayerIndex result;
			if (mouseState.getLeftButton() == ButtonState.Pressed)
			{
				isExit = true;
			}
			// TODO: X-Box controller
			// else if (screenManager.input.IsNewButtonPress(Buttons.A, null, out result) ||
			// screenManager.input.IsNewButtonPress(Buttons.Start, null, out result))
			// {
			// isExit = true;
			// }
// #endif
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(GameTime gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		if (isExit && !isExited)
		{
			// Move on to the gameplay screen
			for (GameScreen screen : screenManager.getScreens())
				screen.exitScreen();

			gameplayScreen.setScreenManager(screenManager);
			screenManager.addScreen(gameplayScreen);
			isExited = true;
		}

		// HandleInput(Mouse.GetState(), GamePad.GetState(PlayerIndex.One));
		handleInput(Mouse.getState());

		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		SpriteBatch spriteBatch = screenManager.getSpriteBatch();

		spriteBatch.begin();

		// Draw Background
		spriteBatch.draw(background, screenManager.getGraphicsDevice().getViewport().getBounds(),
				Color.multiply(Color.White, getTransitionAlpha()));

		if (isExit)
		{
			Rectangle safeArea = screenManager.getSafeArea();
			String text = "Loading...";
			Vector2 measure = font.measureString(text);
			Vector2 textPosition = new Vector2(safeArea.getCenter().x - measure.x / 2,
											   safeArea.getCenter().y - measure.y / 2);
			spriteBatch.drawString(font, text, textPosition, Color.Black);
		}

		spriteBatch.end();
		super.draw(gameTime);
	}
}
