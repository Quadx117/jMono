package blackjack.cardsFramework.UI;

import jMono_Framework.Rectangle;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.time.GameTime;

public class FlipGameComponentAnimation extends AnimatedGameComponentAnimation
{
	protected int percent = 0;
	public boolean isFromFaceDownToFaceUp = true;

	/**
	 * Runs the flip animation, which makes the component appear as if it has
	 * been flipped.
	 */
	@Override
	public void run(GameTime gameTime)
	{
		Texture2D texture;
		if (isStarted())
		{
			if (isDone())
			{
				// Finish the animation
				component.isFaceDown = !isFromFaceDownToFaceUp;
				component.currentDestination = null;
			}
			else
			{
				texture = component.currentFrame;
				if (texture != null)
				{
					// Calculate the completion percent of the animation
					percent += (int) (((gameTime.getElapsedGameTime().getTotalMilliseconds() / (duration
							.getTotalMilliseconds() / animationCycles)) * 100));

					if (percent >= 100)
					{
						percent = 0;
					}

					int currentPercent;
					if (percent < 50)
					{
						// On the first half of the animation the component is
						// on its initial size
						currentPercent = percent;
						component.isFaceDown = isFromFaceDownToFaceUp;
					}
					else
					{
						// On the second half of the animation the component is
						// flipped
						currentPercent = 100 - percent;
						component.isFaceDown = !isFromFaceDownToFaceUp;
					}
					// Shrink and widen the component to look like it is
					// flipping
					component.currentDestination =
							new Rectangle((int) (component.currentPosition.x + texture.getWidth() * currentPercent / 100),
										  (int) component.currentPosition.y,
										  (int) (texture.getWidth() - texture.getWidth() * currentPercent / 100 * 2),
										  texture.getHeight());

				}
			}
		}
	}
}
