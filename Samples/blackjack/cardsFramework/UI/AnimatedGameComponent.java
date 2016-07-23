package blackjack.cardsFramework.UI;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.Rectangle;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.List;

import blackjack.cardsFramework.game.CardsGame;

/**
 * A game component.
 * Enable variable display while managing and displaying a set
 * of {@link "AnimatedGameComponentAnimation"} Animations.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class AnimatedGameComponent extends DrawableGameComponent
{
	public Texture2D currentFrame;
	public Rectangle currentSegment = null;			// nullable struct in C#
	public String text;
	public Color textColor = new Color();
	public boolean isFaceDown = true;
	public Vector2 currentPosition = new Vector2();
	public Rectangle currentDestination = null;		// nullable struct in C#

	private List<AnimatedGameComponentAnimation> runningAnimations = new ArrayList<AnimatedGameComponentAnimation>();;

	/**
	 * Returns whether or not an animation belonging to the component is
	 * running.
	 * 
	 * @return {@code true} if an animation belonging to the component is
	 *         running, {@code false} otherwise.
	 */
	public boolean isAnimating()
	{
		return runningAnimations.size() > 0;
	}

	protected CardsGame cardGame;
	public CardsGame getCardGame() { return cardGame; }
	
	/**
	 * Initializes a new instance of the class, using black text color.
	 * 
	 * @param game
	 *        The associated game class.
	 */
	public AnimatedGameComponent(Game game)
	{
		super(game);
		textColor = Color.Black;
	}

	/**
	 * Initializes a new instance of the class, using black text color.
	 * 
	 * @param game
	 *        The associated game class.
	 * @param currentFrame
	 *        The texture serving as the current frame to display as the
	 *        component.
	 */
	public AnimatedGameComponent(Game game, Texture2D currentFrame)
	{
		this(game);
		this.currentFrame = currentFrame;
	}

	/**
	 * Initializes a new instance of the class, using black text color.
	 * 
	 * @param cardGame
	 *        The associated card game.
	 * @param currentFrame
	 *        The texture serving as the current frame to display as the
	 *        component.
	 */
	public AnimatedGameComponent(CardsGame cardGame, Texture2D currentFrame)
	{
		this(cardGame.game);
		this.cardGame = cardGame;
		this.currentFrame = currentFrame;
	}

	/**
	 * Keeps track of the component's animations.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		super.update(gameTime);

		for (int animationIndex = 0; animationIndex < runningAnimations.size(); ++animationIndex)
		{
			runningAnimations.get(animationIndex).accumulateElapsedTime(gameTime.getElapsedGameTime());
			runningAnimations.get(animationIndex).run(gameTime);
			if (runningAnimations.get(animationIndex).isDone())
			{
				runningAnimations.remove(animationIndex);
				--animationIndex;
			}
		}
	}

	/**
	 * Draws the animated component and its associated text, if it exists, at
	 * the object's set destination. If a destination is not set, its initial
	 * position is used.
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		super.draw(gameTime);

		SpriteBatch spriteBatch;
		
		if (cardGame != null)
		{
			spriteBatch = cardGame.spriteBatch;
		}
		else
		{
			spriteBatch = new SpriteBatch(getGame().getGraphicsDevice());
		}
		
		spriteBatch.begin();

		// Draw at the destination if one is set
		if (currentDestination != null)
		{
			if (currentFrame != null)
			{
				spriteBatch.draw(currentFrame, currentDestination, currentSegment, Color.White);
				if (text != null)
				{
					Vector2 size = cardGame.font.measureString(text);
					Vector2 textPosition = new Vector2(currentDestination.x + currentDestination.width / 2 - size.x / 2,
													   currentDestination.y + currentDestination.height / 2 - size.y / 2);

					spriteBatch.drawString(cardGame.font, text, textPosition, textColor);
                }
			}
		}
		// Draw at the component's position if there is no destination
		else
		{
			if (currentFrame != null)
			{
				spriteBatch.draw(currentFrame, currentPosition, currentSegment, Color.White);
				if (text != null)
				{
					Vector2 size = cardGame.font.measureString(text);
					Vector2 textPosition = new Vector2(currentPosition.x + currentFrame.getBounds().width / 2 - size.x / 2,
													   currentPosition.y + currentFrame.getBounds().height / 2 - size.y / 2);

					spriteBatch.drawString(cardGame.font, text, textPosition, textColor);
                }
			}
		}
		spriteBatch.end();
	}

	/**
	 * Adds an animation to the animated component.
	 * 
	 * @param animation
	 *        The animation to add.
	 */
	public void addAnimation(AnimatedGameComponentAnimation animation)
	{
		animation.component = this;
		runningAnimations.add(animation);
	}

	/**
	 * Calculate the estimated time at which the longest lasting animation
	 * currently managed will complete.
	 * 
	 * @return The estimated time for animation complete.
	 */
	public TimeSpan estimatedTimeForAnimationsCompletion()
	{
		TimeSpan result = new TimeSpan(TimeSpan.ZERO);

		if (isAnimating())
		{
			for (int animationIndex = 0; animationIndex < runningAnimations.size(); ++animationIndex)
			{
				if (runningAnimations.get(animationIndex).getEstimatedTimeForAnimationCompletion().getTicks() > result.getTicks())
				{
					result = runningAnimations.get(animationIndex).getEstimatedTimeForAnimationCompletion();
				}
			}
		}

		return result;
	}
}