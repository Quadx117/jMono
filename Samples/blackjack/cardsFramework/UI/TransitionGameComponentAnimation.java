package blackjack.cardsFramework.UI;

import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

/**
 * An animation which moves a component from one point to the other.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public class TransitionGameComponentAnimation extends AnimatedGameComponentAnimation
{
	private Vector2 sourcePosition;
	private Vector2 positionDelta;
	private float percent = 0;
	private Vector2 destinationPosition;

	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param sourcePosition
	 *        The source position.
	 * @param destinationPosition
	 *        The destination position.
	 */
	public TransitionGameComponentAnimation(Vector2 sourcePosition, Vector2 destinationPosition)
	{
		this.destinationPosition = new Vector2(destinationPosition);
		this.sourcePosition = new Vector2(sourcePosition);
		positionDelta = Vector2.subtract(destinationPosition, sourcePosition);
	}

	/**
	 * Runs the transition animation.
	 * 
	 * @param gameTime
	 *        Game time information.
	 */
	@Override
	public void run(GameTime gameTime)
	{
		if (isStarted())
		{
			// Calculate the animation's completion percentage.
			percent += (float) (gameTime.getElapsedGameTime().getTotalSeconds() / duration.getTotalSeconds());

			// Move the component towards the destination as the animation progresses
			component.currentPosition = Vector2.add(sourcePosition, Vector2.multiply(positionDelta, percent));

			if (isDone())
			{
				component.currentPosition = new Vector2(destinationPosition);
			}
		}
	}
}
