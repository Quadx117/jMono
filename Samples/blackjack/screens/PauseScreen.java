package blackjack.screens;

import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.EventArgs;

import java.util.ArrayList;
import java.util.List;

import blackjack.cardsFramework.screenManager.GameScreen;
import blackjack.cardsFramework.screenManager.MenuEntry;
import blackjack.cardsFramework.screenManager.MenuScreen;
import blackjack.cardsFramework.screenManager.ScreenManager;

/**
 * 
 * @author Eric Perron
 *
 */
public class PauseScreen extends MenuScreen
{
	/**
	 * Initializes a new instance of the screen.
	 */
	public PauseScreen()
	{
		super("");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadContent()
	{
		// Create our menu entries.
		MenuEntry returnGameMenuEntry = new MenuEntry("Back");
		MenuEntry exitMenuEntry = new MenuEntry("Quit");

		// Hook up menu event handlers.
		returnGameMenuEntry.selected.add(this::returnGameMenuEntrySelected);
		exitMenuEntry.selected.add(this::onCancel);

		// Add entries to the menu.
		menuEntries.add(returnGameMenuEntry);
		menuEntries.add(exitMenuEntry);

		super.loadContent();
	}

	/**
	 * Respond to "Return" Item Selection
	 * 
	 * @param sender
	 * @param e
	 */
	void returnGameMenuEntrySelected(Object sender, EventArgs e)
	{
		GameScreen[] screens = screenManager.getScreens();
		GameplayScreen gameplayScreen = null;
		List<GameScreen> res = new ArrayList<GameScreen>();

		for (int screenIndex = 0; screenIndex < screens.length; ++screenIndex)
		{
			if (screens[screenIndex] instanceof GameplayScreen)
			{
				gameplayScreen = (GameplayScreen) screens[screenIndex];
			}
			else
			{
				res.add(screens[screenIndex]);
			}
		}

		for (GameScreen screen : res)
			screen.exitScreen();

		gameplayScreen.returnFromPause();
	}

	/**
	 * Respond to "Quit Game" Item Selection
	 */
	@Override
	protected void onCancel()
	{
		for (int componentIndex = 0; componentIndex < screenManager.getGame().getComponents().size(); ++componentIndex)
		{
			if (!(screenManager.getGame().getComponents().get(componentIndex) instanceof ScreenManager))
			{
				if (screenManager.getGame().getComponents().get(componentIndex) instanceof DrawableGameComponent)
				{
					try
					{
						(As.as(screenManager.getGame().getComponents().get(componentIndex), AutoCloseable.class)).close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					--componentIndex;
				}
				else
				{
					screenManager.getGame().getComponents().remove(componentIndex);
					componentIndex--;
				}
			}
		}

		for (GameScreen screen : screenManager.getScreens())
			screen.exitScreen();

		screenManager.addScreen(new BackgroundScreen());
		screenManager.addScreen(new MainMenuScreen());
	}
}
