package blackjack.cardsFramework.UI;

import jMono_Framework.Rectangle;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

/**
 * A "typical" animation that consists of alternating between a set of frames.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 */
public class FramesetGameComponentAnimation extends AnimatedGameComponentAnimation
{
	private Texture2D framesTexture;
	private int numberOfFrames;
	private int numberOfFramePerRow;
	private Vector2 frameSize;

	private double percent = 0.00;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param framesTexture
	 *        The frames texture (animation sheet).
	 * @param numberOfFrames
	 *        The number of frames in the sheet.
	 * @param numberOfFramesPerRow
	 *        The number of frames per row.
	 * @param frameSize
	 *        Size of the frame.
	 */
	public FramesetGameComponentAnimation(Texture2D framesTexture, int numberOfFrames, int numberOfFramesPerRow,
			Vector2 frameSize)
	{
		this.framesTexture = framesTexture;
		this.numberOfFrames = numberOfFrames;
		this.numberOfFramePerRow = numberOfFramesPerRow;
		this.frameSize = new Vector2(frameSize);
	}

	/**
	 * Runs the frame set animation.
	 * 
	 */
	@Override
	public void run(GameTime gameTime)
	{
		if (isStarted())
		{
			// Calculate the completion percent of the animation
			percent += (((gameTime.getElapsedGameTime().getTotalMilliseconds() / (duration.getTotalMilliseconds() / animationCycles)) * 100));

			if (percent >= 100)
			{
				percent = 0;
			}

			// Calculate the current frame index
			int animationIndex = (int) (numberOfFrames * percent / 100);
			component.currentSegment =
					new Rectangle(
							(int) frameSize.x * (animationIndex % numberOfFramePerRow),
							(int) frameSize.y * (animationIndex / numberOfFramePerRow),
							(int) frameSize.x,
							(int) frameSize.y);
			component.currentFrame = framesTexture;

		}
		else
		{
			component.currentFrame = null;
			component.currentSegment = null;
		}
		super.run(gameTime);
	}
}