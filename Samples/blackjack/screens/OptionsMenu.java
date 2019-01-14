package blackjack.screens;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

import java.util.HashMap;

import blackjack.cardsFramework.UI.AnimatedGameComponent;
import blackjack.screenManager.MenuEntry;
import blackjack.screenManager.MenuScreen;

public class OptionsMenu extends MenuScreen
{
	HashMap<String, Texture2D> themes = new HashMap<String, Texture2D>();
	AnimatedGameComponent card;
	Texture2D background;
	Rectangle safeArea;

	/**
	 * Initializes a new instance of the screen.
	 */
	public OptionsMenu()
	{
		super("");
	}

	/**
	 * Loads content required by the screen, and initializes the displayed menu.
	 */
	@Override
	public void loadContent()
	{
		safeArea = screenManager.getSafeArea();

		// Create our menu entries.
		MenuEntry themeGameMenuEntry = new MenuEntry("Deck");
		MenuEntry returnMenuEntry = new MenuEntry("Return");

		// Hook up menu event handlers.
		themeGameMenuEntry.selected.add(this::themeGameMenuEntrySelected);
		returnMenuEntry.selected.add(this::onCancel);

		// Add entries to the menu.
		menuEntries.add(themeGameMenuEntry);
		menuEntries.add(returnMenuEntry);

		themes.put("Red", screenManager.getGame().getContent().load(
				"images/Cards/cardBack_Red", Texture2D.class));
		themes.put("Blue", screenManager.getGame().getContent().load(
				"images/Cards/cardBack_Blue", Texture2D.class));
		background = screenManager.getGame().getContent().load(
				"images/UI/table", Texture2D.class);

		card = new AnimatedGameComponent(screenManager.getGame(),
				themes.get(MainMenuScreen.theme));
		card.currentPosition = new Vector2(safeArea.getCenter().x, safeArea.getCenter().y - 50);

		// Make the card draw itself from the DrawableGameComponents list in the Game class
		screenManager.getGame().getComponents().add(card);

		super.loadContent();

	}

	/**
	 * Respond to "Theme" Item Selection.
	 * 
	 * @param sender
	 * @param e
	 */
	void themeGameMenuEntrySelected(Object sender, EventArgs e)
	{
		if (MainMenuScreen.theme.equals("Red"))
		{
			MainMenuScreen.theme = "Blue";
		}
		else
		{
			MainMenuScreen.theme = "Red";
		}
		card.currentFrame = themes.get(MainMenuScreen.theme);
	}

	/**
	 * Respond to "Return" Item Selection
	 */
	@Override
	protected void onCancel()
	{
		screenManager.getGame().getComponents().remove(card);
		exitScreen();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		screenManager.getSpriteBatch().begin();

		// Draw the background
		screenManager.getSpriteBatch().draw(background, screenManager.getGraphicsDevice().getViewport().getBounds(),
				Color.multiply(Color.White(), getTransitionAlpha()));

		screenManager.getSpriteBatch().end();

		super.draw(gameTime);
	}

}