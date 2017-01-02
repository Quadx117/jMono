package blackjack.misc;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteEffects;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

/**
 * Used to simulate a cursor on the Xbox.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class InputHelper extends DrawableGameComponent
{
	public boolean isEscape;
	public boolean isPressed;

	private Vector2 drawPosition;
	private Texture2D texture;
	private SpriteBatch spriteBatch;
	private float maxVelocity;

	public InputHelper(Game game)
	{
		super(game);
		texture = game.getContent().load("images/GamePadCursor", Texture2D.class);
		spriteBatch = new SpriteBatch(game.getGraphicsDevice());
		maxVelocity = (float) (game.getGraphicsDevice().getViewport().getWidth() +
                			   game.getGraphicsDevice().getViewport().getHeight()) / 3000f;

		drawPosition = new Vector2(game.getGraphicsDevice().getViewport().getWidth() / 2,
                				   game.getGraphicsDevice().getViewport().getHeight() / 2);
	}

	public Vector2 getPointPosition()
    {
            return new Vector2(texture.getWidth() / 2f,
            				   texture.getHeight() / 2f).add(drawPosition);
    }
	
	/**
	 * Updates itself.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		// GamePadState gamePadState = GamePad.GetState(PlayerIndex.One);

		// isPressed = gamePadState.Buttons.A == ButtonState.Pressed;

		// isEscape = gamePadState.Buttons.Back == ButtonState.Pressed;

		// drawPosition += gamePadState.ThumbSticks.Left * new Vector2(1, -1) *
		// gameTime.getElapsedGameTime().getMilliseconds() * maxVelocity;

		// drawPosition = Vector2.clamp(drawPosition, Vector2.Zero, new
		// Vector2(Game.GraphicsDevice.Viewport.Width,
		// Game.GraphicsDevice.Viewport.Height) - new
		// Vector2(sprite.Bounds.Width, sprite.Bounds.Height));
	}

	/**
	 * Draws cursor.
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		spriteBatch.begin();
		spriteBatch.draw(texture, drawPosition, null, Color.White, 0,
						 Vector2.Zero(), 1, SpriteEffects.None, 0);
		spriteBatch.end();
	}

}
