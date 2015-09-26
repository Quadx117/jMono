package gameCore.time;

/**
 * Snapshot of the game timing state expressed in values that can be used by
 * variable-step (real time) or fixed-step (game time) games.
 */
public class GameTime
{

	/** The amount of game time since the start of the game. */
	public TimeSpan totalGameTime;

	/** The amount of elapsed game time since the last update. */
	public TimeSpan elapsedGameTime;

	/**
	 * Value indicating that the game loop is taking longer than its
	 * targetElapsedTime. In this case, the game loop can be considered to be
	 * running too slowly and should do something to "catch up."
	 */
	public boolean isRunningSlowly;

	/**
	 * Creates a new instance of GameTime with it's members initialized to {@link TimeSpan.ZERO}.
	 * This constructor should be used for the games main
	 * game time.
	 */
	public GameTime()
	{
		this(TimeSpan.ZERO, TimeSpan.ZERO);
	}

	/**
	 * Creates a new instance of GameTime with the specified values.
	 * 
	 * @param totalGameTime
	 *        The value used to set the initial totalGameTime for this
	 *        GameTime.
	 * @param elapsedGameTime
	 *        The value used to set the initial elapsedGameTime for this
	 *        GameTime.
	 */
	public GameTime(TimeSpan totalGameTime, TimeSpan elapsedGameTime)
	{
		this.totalGameTime = totalGameTime;
		this.elapsedGameTime = elapsedGameTime;
		isRunningSlowly = false;
	}

	/**
	 * Creates a new instance of GameTime with the specified values.
	 * 
	 * @param totalGameTime
	 *        The value used to set the initial totalGameTime for this
	 *        GameTime.
	 * @param elapsedGameTime
	 *        The value used to set the initial elapsedGameTime for this
	 *        GameTime.
	 * @param isRunningSlowly
	 *        The value used to set the initial isRunningSlowly for this
	 *        GameTime.
	 */
	public GameTime(TimeSpan totalRealTime, TimeSpan elapsedRealTime, boolean isRunningSlowly)
	{
		this.totalGameTime = totalRealTime;
		this.elapsedGameTime = elapsedRealTime;
		this.isRunningSlowly = isRunningSlowly;
	}

	// TODO : Validate if we keep this method and if I can use it in GameCore
	/**
	 * Sets elapsedGameTime the specified value and update totalGameTime
	 * accordingly.
	 * 
	 * @param elapsedTicks
	 *        The value to set elapsedGameTime and to add to totalGameTime
	 */
	public void update(long elapsedTicks)
	{
		try
		{
			elapsedGameTime.setTimeSpan(elapsedTicks);
			totalGameTime.add(elapsedGameTime);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// TODO: Finish comments
	// TODO: Do I keep these since the fields are public
	// ++++++++++ GETTERS ++++++++++ //
	public TimeSpan getElapsedGameTime()
	{
		return elapsedGameTime;
	}

	public TimeSpan getTotalGameTime()
	{
		return totalGameTime;
	}

	public boolean isRunningSlowly()
	{
		return isRunningSlowly;
	}

	// ++++++++++ SETTERS ++++++++++ //
	public void setElapsedGameTime(TimeSpan ts)
	{
		elapsedGameTime = ts;
	}

	public void setTotalGameTime(TimeSpan ts)
	{
		totalGameTime = ts;
	}

	public void setIsRunningSlowly(boolean value)
	{
		isRunningSlowly = value;
	}

}
