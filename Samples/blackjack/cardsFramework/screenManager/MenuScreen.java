package blackjack.cardsFramework.screenManager;

import jMono_Framework.Color;
import jMono_Framework.Point;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteEffects;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.input.ButtonState;
import jMono_Framework.input.Keys;
import jMono_Framework.input.Mouse;
import jMono_Framework.input.MouseState;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for screens that contain a menu of options. The user can move up
 * and down to select an entry, or cancel to back out of the screen.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public abstract class MenuScreen extends GameScreen
{
	/**
	 * the number of pixels to pad above and below menu entries for touch input
	 */
	protected final int menuEntryPadding = 35;

	/**
	 * List of menu entries
	 */
	protected List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();

	/**
	 * The index of the selected menu entry. Default value is 0.
	 */
	protected int selectedEntry = 0;

	/**
	 * The title for this menu.
	 */
	protected String menuTitle;

// #if WINDOWS
	/**
	 * Determines whether a mouse button is down.
	 */
	protected boolean isMouseDown = false;
// #endif

	protected Rectangle bounds;

	/**
	 * Gets the list of menu entries, so derived classes can add or change the
	 * menu contents.
	 * 
	 * @return the list of menu entries.
	 */
	protected List<MenuEntry> getMenuEntries()
	{
		return menuEntries;
	}
	
	/**
	 * Construct a new {@link MenuScreen} with the specified title.
	 * 
	 * @param menuTitle
	 *        The title for this new menu screen.
	 */
	public MenuScreen(String menuTitle)
	{
// #if WINDOWS_PHONE
        // menus generally only need Tap for menu selection
		// EnabledGestures = GestureType.Tap;
// #endif
		
		this.menuTitle = menuTitle;
		
		transitionOnTime = TimeSpan.fromSeconds(0.5);
		transitionOffTime = TimeSpan.fromSeconds(0.5);
	}

	/**
	 * Allows the screen to create the hit bounds for touch screens for a
	 * particular menu entry.
	 * 
	 * @param entry
	 * 			The {@link MenuEntry} for which to get the hit bounds
	 * 
	 * @return A {@code Rectangle} that represents the size of the hit bounds.
	 */
	protected Rectangle getMenuEntryHitBounds(MenuEntry entry)
	{
		// The hit bounds are the entire width of the screen, and the height of
		// the entry with some additional padding above and below.
		return new Rectangle(0,
							 (int) entry.getDestination().y - menuEntryPadding,
							 screenManager.getGraphicsDevice().getViewport().getWidth(),
							 entry.getHeight(this) + (menuEntryPadding * 2));
	}

	/**
	 * Responds to user input, changing the selected entry and accepting or
	 * canceling the menu.
	 */
	@Override
	protected void handleInput(InputState input)
	{
		// we cancel the current menu screen if the user presses the back button
		// PlayerIndex player;
		// if (input.IsNewButtonPress(Buttons.Back, ControllingPlayer, out player))
		// {
			// onCancel(player);
		// }
// #if WINDOWS
		// Take care of Keyboard input
		if (input.isMenuUp())
		{
			--selectedEntry;
			
			if (selectedEntry < 0)
				selectedEntry = menuEntries.size() - 1;
		}
		else if (input.isMenuDown())
		{
			++selectedEntry;
			
			if (selectedEntry >= menuEntries.size())
				selectedEntry = 0;
		}
		else if (input.isNewKeyPress(Keys.Enter) ||
				 input.isNewKeyPress(Keys.Space))
		{
			onSelectEntry(selectedEntry);
		}
		// TODO: Validate if this is needed (not part of original code)
		else if (input.isMenuCancel())
		{
			onCancel();
		}
		
		MouseState state = Mouse.getState();
		if (state.getLeftButton() == ButtonState.Released)
		{
			if (isMouseDown)
			{
				isMouseDown = false;
				// convert the position to a Point that we can test against a Rectangle
				Point clickLocation = new Point(state.getX(), state.getY());

				// iterate the entries to see if any were tapped
				for (int i = 0; i < menuEntries.size(); ++i)
				{
					MenuEntry menuEntry = menuEntries.get(i);

					if (menuEntry.destination.contains(clickLocation))
					{
						// Select the entry.
						onSelectEntry(i);
					}
				}
			}
		}
		else if (state.getLeftButton() == ButtonState.Pressed)
		{
			isMouseDown = true;

		// convert the position to a Point that we can test against a Rectangle
		Point clickLocation = new Point(state.getX(), state.getY());

		// iterate the entries to see if any were tapped
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			MenuEntry menuEntry = menuEntries.get(i);

			if (menuEntry.destination.contains(clickLocation))
				selectedEntry = i;
		}
	}
		
/*
#elif XBOX
            // Take care of Gamepad input
            if (input.IsMenuUp(ControllingPlayer))
            {
                selectedEntry--;

                if (selectedEntry < 0)
                    selectedEntry = menuEntries.Count - 1;
            }
            else if (input.IsMenuDown(ControllingPlayer))
            {
                selectedEntry++;

                if (selectedEntry >= menuEntries.Count)
                    selectedEntry = 0;
            }
            else if (input.IsNewButtonPress(Buttons.A, ControllingPlayer, out player))
                OnSelectEntry(selectedEntry, player);

#elif WINDOWS_PHONE
            // look for any taps that occurred and select any entries that were tapped
            foreach (GestureSample gesture in input.Gestures)
            {
                if (gesture.GestureType == GestureType.Tap)
                {
                    // convert the position to a Point that we can test against a Rectangle
                    Point tapLocation = new Point((int)gesture.Position.X, (int)gesture.Position.Y);

                    // iterate the entries to see if any were tapped
                    for (int i = 0; i < menuEntries.Count; i++)
                    {
                        MenuEntry menuEntry = menuEntries[i];

                        if (menuEntry.Destination.Contains(tapLocation))
                        {
                            // Select the entry. since gestures are only available on Windows Phone,
                            // we can safely pass PlayerIndex.One to all entries since there is only
                            // one player on Windows Phone.
                            OnSelectEntry(i, PlayerIndex.One);
                        }
                    }
                }
            }
#endif
 */
	}

	/**
	 * Handler for when the user has chosen a menu entry.
	 * 
	 * @param entryIndex
	 */
	protected void onSelectEntry(int entryIndex)
	{
		menuEntries.get(entryIndex).onSelectEntry();
	}

	/**
	 * Handler for when the user has cancelled the menu.
	 */
	protected void onCancel()
	{
		exitScreen();
	}

	/**
	 * Helper overload makes it easy to use OnCancel as a MenuEntry event handler.
	 * 
	 * @param sender
	 * @param e
	 */
    protected void onCancel(Object sender, EventArgs e)
    {
        onCancel();
    }
    
	@Override
	public void loadContent()
	{
		bounds = screenManager.getSafeArea();
		super.loadContent();
	}

	/**
	 * Allows the screen the chance to position the menu entries. By default all
	 * menu entries are lined up in a vertical list, centered on the screen.
	 */
	protected void updateMenuEntryLocations()
	{
		// Make the menu slide into place during transitions, using a
		// power curve to make things look more interesting (this makes
		// the movement slow down as it nears the end).
		float transitionOffset = (float) Math.pow(transitionPosition, 2);

		// start at y = screenHeight / 2; each X value is generated per entry
		Vector2 position = new Vector2(0.0f,
									   screenManager.getGame().getWindow().clientBounds.height / 2
									   - (menuEntries.get(0).getHeight(this) +
										(menuEntryPadding * 2) * menuEntries.size()));

		// update each menu entry's location in turn
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			MenuEntry menuEntry = menuEntries.get(i);

			// each entry is to be centered horizontally
			position.x = screenManager.getGraphicsDevice().getViewport().getWidth() / 2 - menuEntry.getWidth(this) / 2;

			if (screenState == ScreenState.TRANSITION_ON)
			{
				position.x -= transitionOffset * 256;
			}
			else
			{
				position.x += transitionOffset * 512;
			}

			// move down for the next entry the size of this entry plus our
			// padding
			position.y += menuEntry.getHeight(this) + (menuEntryPadding * 2);
		}
	}

	/**
	 * Updates the menu.
	 */
	@Override
	protected void update(GameTime gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

		// Update each nested MenuEntry object.
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			boolean isSelected = isActive() && (i == selectedEntry);
			updateMenuEntryDestination();
			menuEntries.get(i).update(this, isSelected, gameTime);
		}
	}

	/**
	 * Draws the menu.
	 */
	@Override
	protected void draw(GameTime gameTime)
	{
		// NOTE: Already commented out in the cards framework
		// make sure our entries are in the right place before we draw them
		// updateMenuEntryLocations();

		GraphicsDevice graphics = screenManager.getGraphicsDevice();
		SpriteBatch spriteBatch = screenManager.getSpriteBatch();
		SpriteFont font = screenManager.getFont();

		spriteBatch.begin();

		// Draw each menu entry in turn.
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			MenuEntry menuEntry = menuEntries.get(i);
			
			boolean isSelected = isActive() && (i == selectedEntry);
			
			menuEntry.draw(this, isSelected, gameTime);
		}

		// Make the menu slide into place during transitions, using a
        // power curve to make things look more interesting (this makes
        // the movement slow down as it nears the end).
		float transitionOffset = (float)Math.pow(transitionPosition, 2);

		// Draw the menu title centered on the screen
		Vector2 titlePosition = new Vector2(graphics.getViewport().getWidth() / 2, 375);
		Vector2 titleOrigin = font.measureString(menuTitle).divide(2);
		Color titleColor = Color.multiply(new Color(192, 192, 192), getTransitionAlpha());
		float titleScale = 1.25f;

		titlePosition.y -= transitionOffset * 100;

		spriteBatch.drawString(font, menuTitle, titlePosition, titleColor, 0,
                               titleOrigin, titleScale, SpriteEffects.None, 0);

        spriteBatch.end();
	}

	/**
	 * Centers our menuEntries horizontally.
	 */
	protected void updateMenuEntryDestination()
	{
		Rectangle bounds = screenManager.getSafeArea();

		Rectangle textureSize = screenManager.getButtonBackground().getBounds();

		// Add 2 so we get an empty space the same size as a button on each far
		// side (let and right) so it looks centered.
		int xStep = (int) (bounds.width / (menuEntries.size() + 2));

		// Find the largest text from our menuEntries
		int maxWidth = 0;
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			int width = (int) menuEntries.get(i).getWidth(this);
			if (width > maxWidth)
			{
				maxWidth = width;
			}
		}
		// add 10 pixels of padding on each side of the text between the
		// buttonBackground and the text itself
		maxWidth += 20;

		// NOTE: This was hardcoded as 50 in the original code
		int height = menuEntries.get(0).getHeight(this) + 11;
		
		// set each menuEntry's destination.
		for (int i = 0; i < menuEntries.size(); ++i)
		{
			menuEntries.get(i).destination = 
					new Rectangle(bounds.left() + (xStep - textureSize.width) / 2 + (i + 1) * xStep,
								  bounds.bottom() - textureSize.height * 2, maxWidth, height);
		}
	}

}
