package blackjack.screens;

import jMono_Framework.dotNet.events.EventArgs;
import blackjack.screenManager.GameScreen;
import blackjack.screenManager.MenuEntry;
import blackjack.screenManager.MenuScreen;

/**
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class MainMenuScreen extends MenuScreen
{
	public static String theme = "Red";

	/**
	 * Initializes a new instance of the Main Menu.
	 */
	public MainMenuScreen()
	{
		super("");
	}

	@Override
	public void loadContent()
	{
		// Create our menu entries.
		MenuEntry startGameMenuEntry = new MenuEntry("Play");
		MenuEntry themeGameMenuEntry = new MenuEntry("Theme");
		MenuEntry exitMenuEntry = new MenuEntry("Exit");

		// Hook up menu event handlers.
		startGameMenuEntry.selected.add(this::startGameMenuEntrySelected);
		themeGameMenuEntry.selected.add(this::themeGameMenuEntrySelected);
		exitMenuEntry.selected.add(this::onCancel);

		// Add entries to the menu.
		menuEntries.add(startGameMenuEntry);
		menuEntries.add(themeGameMenuEntry);
		menuEntries.add(exitMenuEntry);

		super.loadContent();
	}

	/**
	 * Respond to "Play" Item Selection
	 * 
	 * @param sender
	 * @param e
	 */
	void startGameMenuEntrySelected(Object sender, EventArgs e)
	{
		for (GameScreen screen : screenManager.getScreens())
		{
			screen.exitScreen();
		}
		screenManager.addScreen(new GameplayScreen(theme));
	}

	/**
	 * Respond to "Theme" Item Selection
	 * 
	 * @param sender
	 * @param e
	 */
	void themeGameMenuEntrySelected(Object sender, EventArgs e)
	{
		screenManager.addScreen(new OptionsMenu());
	}

	/**
	 * Respond to "Exit" Item Selection
	 */
	// protected void onCancel() {
	protected void onCancel(Object sender, EventArgs e)
	{
		screenManager.getGame().exit();
	}

}
