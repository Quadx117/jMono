package blackjack.cardsFramework.UI;

import jMono_Framework.Rectangle;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.time.GameTime;

public class ScaleGameComponentAnimation extends AnimatedGameComponentAnimation
{
	float percent = 0f;
	float beginFactor;
	float factorDelta;

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param beginFactor
	 *        The initial scale factor.
	 * @param endFactor
	 *        The eventual scale factor.
	 */
	public ScaleGameComponentAnimation(float beginFactor, float endFactor)
	{
		this.beginFactor = beginFactor;
		factorDelta = endFactor - beginFactor;
	}

	/**
	 * Runs the scaling animation.
	 * 
	 * @param gameTime
	 *        Game time information.
	 */
	@Override
	public void run(GameTime gameTime)
	{
		Texture2D texture;
		if (isStarted())
		{
			texture = component.currentFrame;
			if (texture != null)
			{
				// Calculate the completion percent of animation
				percent += (float) (gameTime.getElapsedGameTime().getTotalSeconds() / duration.getTotalSeconds());

				// Inflate the component with an increasing delta. The eventual
				// delta will have the component scale to the specified target
				// scaling factor.
				Rectangle bounds = texture.getBounds();
				bounds.x = (int) component.currentPosition.x;
				bounds.y = (int) component.currentPosition.y;
				float currentFactor = beginFactor + factorDelta * percent - 1;
				bounds.inflate((int) (bounds.width * currentFactor), (int) (bounds.height * currentFactor));
				component.currentDestination = new Rectangle(bounds);
			}
		}
	}
}
