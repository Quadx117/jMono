package blackjack.cardsFramework.UI;

import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.function.Consumer;

/**
 * Represents an animation that can alter an animated component.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class AnimatedGameComponentAnimation
{
	protected TimeSpan elapsed;

	protected AnimatedGameComponent component;
	public AnimatedGameComponent getComponent() { return component; }

	/**
	 * An action to perform before the animation begins.
	 */
	public Consumer<Object> performBeforeStart;
	public Object performBeforSartArgs;

	/**
	 * An action to perform once the animation is complete.
	 */
	public Consumer<Object> performWhenDone;
	public Object performWhenDoneArgs;

	protected int animationCycles = 1;
	public int getAnimationCycles() { return animationCycles; }
	public void setAnimationCycles(int value)
	{
		if (value > 0)
		{
			animationCycles = value;
		}
	}

	public TimeSpan startTime;
	public TimeSpan duration;

	// Returns the time at which the animation is estimated to end.
	public TimeSpan getEstimatedTimeForAnimationCompletion()
	{
		if (isStarted)
		{
			return new TimeSpan(duration.getTicks() - elapsed.getTicks());
		}
		else
		{
			return new TimeSpan(startTime.getTicks() - TimeSpan.now().getTicks() + duration.getTicks());
		}
	}

	public boolean isLooped;

	private boolean isDone = false;

	private boolean isStarted = false;

	/**
	 * Initializes a new instance of the class. By default, an animation starts
	 * immediately and has a duration of 150 milliseconds.
	 */
	public AnimatedGameComponentAnimation()
	{
		elapsed = new TimeSpan(0);
		startTime = TimeSpan.now();
		duration = TimeSpan.fromMilliseconds(150);
	}

	/**
	 * Check whether or not the animation is done playing. Looped animations
	 * never finish playing.
	 * 
	 * @return Whether or not the animation is done playing.
	 */
	public boolean isDone()
	{
		if (!isDone)
		{
			isDone = !isLooped && (elapsed.getTicks() >= duration.getTicks());
			if (isDone && performWhenDone != null)
			{
				performWhenDone.accept(performWhenDoneArgs);
				performWhenDone = null;
			}
		}
		return isDone;
	}

	/**
	 * Returns whether or not the animation is started. As a side-effect, starts
	 * the animation if it is not started and it is time for it to start.
	 * 
	 * @return Whether or not the animation is started.
	 */
	public boolean isStarted()
	{
		if (!isStarted)
		{
			if (startTime.getTicks() <= TimeSpan.now().getTicks())
			{
				if (performBeforeStart != null)
				{
					performBeforeStart.accept(performBeforSartArgs);
					performBeforeStart = null;
				}
				startTime = TimeSpan.now();
				isStarted = true;
			}
		}
		return isStarted;
	}

	/**
	 * Increases the amount of elapsed time as seen by the animation, but only
	 * if the animation is started.
	 * 
	 * @param elapsedTime
	 *        The timespan by which to incerase the animation's elapsed
	 *        time.
	 */
	protected void accumulateElapsedTime(TimeSpan elapsedTime)
	{
		if (isStarted)
		{
			try
			{
				elapsed.add(elapsedTime);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Runs the animation.
	 * 
	 * @param gameTime
	 *        Game time information.
	 */
	public void run(GameTime gameTime)
	{
		isStarted = isStarted();
	}
}
