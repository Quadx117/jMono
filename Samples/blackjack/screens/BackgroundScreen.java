package blackjack.screens;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;
import blackjack.screenManager.GameScreen;

/**
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class BackgroundScreen extends GameScreen
{
	Texture2D background;
	Rectangle safeArea;

	/**
	 * Initializes a new instance of the screen.
	 */
	public BackgroundScreen()
	{
		transitionOnTime = TimeSpan.fromSeconds(0.0);
		transitionOffTime = TimeSpan.fromSeconds(0.5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadContent()
	{
		background = screenManager.getGame().getContent().load("images/titlescreen", Texture2D.class);
		safeArea = screenManager.getGame().getGraphicsDevice().getViewport().getTitleSafeArea();
		super.loadContent();
	}

	/**
	 * Allows the screen to run logic, such as updating the transition position.
	 * Unlike HandleInput, this method is called regardless of whether the
	 * screen is active, hidden, or in the middle of a transition.
	 * 
	 * @param gameTime
	 * @param otherScreenHasFocus
	 * @param coveredByOtherScreen
	 */
	@Override
	public void update(GameTime gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		super.update(gameTime, otherScreenHasFocus, false);
	}

	/**
	 * This is called when the screen should draw itself.
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		screenManager.getSpriteBatch().begin();

		screenManager.getSpriteBatch().draw(background,
											screenManager.getGraphicsDevice().getViewport().getBounds(),
											Color.multiply(Color.White(), getTransitionAlpha()));

		screenManager.getSpriteBatch().end();

		super.draw(gameTime);
	}
}