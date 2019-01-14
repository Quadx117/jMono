package blackjack.UI;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.input.ButtonState;
import jMono_Framework.input.Mouse;
import jMono_Framework.input.MouseState;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import blackjack.cardsFramework.UI.AnimatedGameComponent;
import blackjack.cardsFramework.game.CardsGame;
import blackjack.misc.InputHelper;
import blackjack.screenManager.InputState;

public class Button extends AnimatedGameComponent
{
	private boolean isKeyDown = false;
	private boolean isPressed = false;
	private SpriteBatch spriteBatch;

	public Texture2D regularTexture;
	public Texture2D pressedTexture;
	public SpriteFont font;
	public Rectangle bounds = new Rectangle();

	private String regularTextureText;
	private String pressedTextureText;

	public Event<EventArgs> click = new Event<EventArgs>();

	// Used only for touch screens
	private InputState input;

	private InputHelper inputHelper;

	/**
	 * Creates a new instance of the {@code Button} class.
	 * Texture names are relative to the "images" content folder.
	 * 
	 * @param regularTextureText
	 *        The name of the button's texture.
	 * @param pressedTextureText
	 *        The name of the texture to display when the button is pressed.
	 * @param input
	 *        A {@link InputState} object which can be used to retrieve user input.
	 * @param cardGame
	 *        The associated card game.
	 */
	public Button(String regularTextureText, String pressedTextureText, InputState input, CardsGame cardGame)
	{
		super(cardGame, null);
		this.input = input;
		this.regularTextureText = regularTextureText;
		this.pressedTextureText = pressedTextureText;
	}

	/**
	 * Initializes the button.
	 */
	@Override
	public void initialize()
	{
// #if WINDOWS_PHONE
		// Enable tab gesture
		// TouchPanel.EnabledGestures = GestureType.Tap;
// #endif
		// Get Xbox cursor
		inputHelper = null;
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			if (game.getComponents().get(componentIndex) instanceof InputHelper)
			{
				inputHelper = (InputHelper) game.getComponents().get(componentIndex);
				break;
			}
		}

		spriteBatch = new SpriteBatch(getGame().getGraphicsDevice());

		super.initialize();
	}

	/**
	 * Loads the content required by the button.
	 */
	@Override
	protected void loadContent()
	{
		if (regularTextureText != null)
		{
			regularTexture = getGame().getContent().load("images/" + regularTextureText, Texture2D.class);
		}
		if (pressedTextureText != null)
		{
			pressedTexture = getGame().getContent().load("images/" + pressedTextureText, Texture2D.class);
		}

		super.loadContent();
	}

	/**
	 * Performs update logic for the button.
	 * 
	 * @param gameTime
	 *        The time that has passed since the last call to this method.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		if (regularTexture != null)
		{
			handleInput(Mouse.getState());
		}

		super.update(gameTime);
	}

	/**
	 * Handle the input of adding chip on all platform.
	 * 
	 * @param mouseState
	 *        Mouse input information.
	 */
	private void handleInput(MouseState mouseState)
	{
		boolean pressed = false;
		Vector2 position = new Vector2(Vector2.Zero());

// #if WINDOWS_PHONE
		// if ((input.Gestures.Count > 0) && input.Gestures[0].GestureType == GestureType.Tap)
		// {
		// pressed = true;
		// position = input.Gestures[0].Position;
		// }
// #else
		
		if (mouseState.getLeftButton() == ButtonState.Pressed)
		{
			pressed = true;
			position = new Vector2(mouseState.getX(), mouseState.getY());
		}
		else if (inputHelper.isPressed)
		{
			pressed = true;
			position = inputHelper.getPointPosition();
		}
		else
		{
			if (isPressed)
			{
				if (intersectWith(new Vector2(mouseState.getX(), mouseState.getY())) ||
					intersectWith(inputHelper.getPointPosition()))
				{
					fireClick();
					isPressed = false;
				}
				else
				{
					isPressed = false;
				}
			}
		
			isKeyDown = false;
		}
// #endif
		
		if (pressed)
		{
			if (!isKeyDown)
			{
				if (intersectWith(position))
				{
					isPressed = true;
// #if WINDOWS_PHONE
					// FireClick();
					// isPressed = false;
// #endif
				}
				isKeyDown = true;
			}
		}
		else
		{
			isKeyDown = false;
		}
	}
		
	/**
	 * Checks if the button intersects with a specified position.
	 * 
	 * @param position
	 *        The position to check intersection against.
	 * @return True if the position intersects with the button, false otherwise.
	 */
	private boolean intersectWith(Vector2 position)
	{
		Rectangle touchTap = new Rectangle((int) position.x - 1, (int) position.y - 1, 2, 2);
		return bounds.intersects(touchTap);
	}

	/**
	 * Fires the button's click event.
	 */
	public void fireClick()
	{
		if (click != null)
		{
			click.handleEvent(this, EventArgs.Empty);
		}
	}

	/**
	 * Draws the button.
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		spriteBatch.begin();

		spriteBatch.draw(isPressed ? pressedTexture : regularTexture, bounds, Color.White());
		if (font != null)
		{
			Vector2 textPosition = font.measureString(text);
			textPosition = new Vector2(bounds.width - textPosition.x,
					bounds.height - textPosition.y);
			textPosition.divide(2);
			textPosition.x += bounds.x;
			textPosition.y += bounds.y;
			spriteBatch.drawString(font, text, textPosition, Color.White());
		}

		spriteBatch.end();

		super.draw(gameTime);
	}

	@Override
	protected void dispose(boolean disposing)
	{
		click = null;
		super.dispose(disposing);
	}

	// ++++++++++ GETTERS ++++++++++ //

	public SpriteFont getFont() { return font; }

	public Rectangle getBounds() { return bounds; }

}
