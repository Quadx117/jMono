package blackjack.screenManager;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteEffects;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

/**
 * Helper class that represents a single entry in a MenuScreen. By default this just
 * draws the entry text string, but it can be customized to display menu entries
 * in different ways. This also provides an event that will be raised when the
 * menu entry is selected.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class MenuEntry
{
	/**
	 * The text rendered for this entry.
	 */
	private String text;

	/**
	 * Tracks a fading selection effect on the entry.
	 * 
	 * <p>
	 * The entries transition out of the selection effect when they are deselected.
	 */
	protected float selectionFade;

	protected Rectangle destination = new Rectangle();

	/**
	 * Gets the text of this menu entry.
	 * 
	 * @return The text of this menu entry.
	 */
	public String getText() { return text; }

	/**
	 * Sets the text of this menu entry.
	 * 
	 * @param value
	 *        The new text value.
	 */
	public void setText(String value) { text = value; }

	/**
	 * Gets the position at which to draw this menu entry.
	 * 
	 * @return The position of this menu entry.
	 */
	public Rectangle getDestination() { return destination; }

	/**
	 * Sets the position at which to draw this menu entry.
	 * 
	 * @param value
	 *        The new destination value.
	 */
	public void setDestination(Rectangle value) { destination = new Rectangle(value); }

	/**
	 * Scale factor. Default value is 1.0f, that is the actual size (no
	 * scaling).
	 */
	public float scale;

	/**
	 * Specifies the angle (in radians) to rotate the text about its center.
	 * Default value is 0.0f, that is no rotation.
	 */
	public float rotation;

	/**
	 * Event raised when the menu entry is selected.
	 */
	public Event<EventArgs> selected = new Event<EventArgs>();

	/**
	 * Method for raising the selected event from the menuEvent class.
	 * 
	 * @return The event that was created by this method.
	 */
	protected void onSelectEntry()
	{
		if (selected != null)
			selected.handleEvent(this, EventArgs.Empty);
	}

	/**
	 * Constructs a new menu entry with the specified text.
	 * 
	 * @param text
	 *        The text for this MenuEntry.
	 */
	public MenuEntry(String text)
	{
		this.text = text;

		scale = 1.0f;
		rotation = 0.0f;
	}

	/**
	 * Updates the menu entry.
	 * 
	 * @param screen
	 *        The screen we are rendering.
	 * @param isSelected
	 *        {@code true} if this screen is selected.
	 * @param gameTime
	 *        an instance of the GameTime object.
	 */
	public void update(MenuScreen screen, boolean isSelected, GameTime gameTime)
	{
		// there is no such thing as a selected item on Windows Phone, so we always
		// force isSelected to be false
// #if WINDOWS_PHONE
		// isSelected = false;
// #endif

		// When the menu selection changes, entries gradually fade between
		// their selected and deselected appearance, rather than instantly
		// popping to the new state.
		float fadeSpeed = (float) gameTime.getElapsedGameTime().getTotalSeconds() * 4;

		if (isSelected)
		{
			selectionFade = Math.min(selectionFade + fadeSpeed, 1);
		}
		else
		{
			selectionFade = Math.max(selectionFade - fadeSpeed, 0);
		}
	}

	/**
	 * Draws the menu entry. This can be overridden to customize the appearance.
	 * 
	 * @param screen
	 *        The screen we are rendering.
	 * @param isSelected
	 *        {@code true} if this screen is selected.
	 * @param gameTime
	 *        an instance of the GameTime object.
	 */
	public void draw(MenuScreen screen, boolean isSelected, GameTime gameTime)
	{
		Color textColor = isSelected ? Color.White() : Color.Black();
		Color tintColor = isSelected ? Color.White() : Color.Gray();

// #if WINDOWS_PHONE
		// there is no such thing as a selected item on Windows Phone, so we always
		// force isSelected to be false

		// isSelected = false;
		// tintColor = Color.White;
		// textColor = Color.Black;
// #endif

		// Draw text, centered on the middle of each line.
		ScreenManager screenManager = screen.screenManager;
		SpriteBatch spriteBatch = screenManager.getSpriteBatch();
		SpriteFont font = screenManager.getFont();

		spriteBatch.draw(screenManager.getButtonBackground(), destination, tintColor);

		spriteBatch.drawString(font, text, getTextPosition(screen),
				textColor, rotation, Vector2.Zero(), scale, SpriteEffects.None, 0);
	}

	/**
	 * Queries how high this menu entry is when rendered in a specific font.
	 * Useful for centering on the screen.
	 * 
	 * @param screen
	 *        The screen we are rendering.
	 * @return The height of this menu entry.
	 */
	public int getHeight(MenuScreen screen)
	{
		return screen.screenManager.getFont().lineSpacing;
	}

	/**
	 * Queries how wide the entry is, used for centering on the screen.
	 * 
	 * @param screen
	 *        The screen we are rendering.
	 * @return the width in pixels.
	 */
	public int getWidth(MenuScreen screen)
	{
		return (int) screen.screenManager.getFont().measureString(text).x;
	}

	private Vector2 getTextPosition(MenuScreen screen)
	{
		Vector2 textPosition = Vector2.Zero();
		if (scale == 1f)
		{
			textPosition = new Vector2((int) destination.x + destination.width / 2 - getWidth(screen) / 2,
									   (int) destination.y);
		}
		else
		{
			textPosition = new Vector2(
					(int) destination.x + (destination.width / 2 - ((getWidth(screen) / 2) * scale)),
					(int) destination.y + (getHeight(screen) - getHeight(screen) * scale) / 2);
		}

		return textPosition;
	}
}
