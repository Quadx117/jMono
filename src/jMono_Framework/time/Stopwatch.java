package jMono_Framework.time;

/**
 * Provides a set of methods and properties that you can use to accurately
 * measure elapsed time. This class uses high-resolution performance counter.
 * 
 * <p>
 * Conceals use of {@code System.nanoTime()}, improving the readability of
 * application code and reducing the likelihood of calculation errors.
 * 
 * <p>
 * Note that this object is not designed to be thread-safe and does not use
 * synchronization.
 * 
 * <p>
 * We use a tick count of 100 nanoseconds to extend the length that can be
 * stored in a long variable while keeping enough precision. TimeSpan is also
 * configured with a 100 tick count.
 */
public class Stopwatch {
	// 100 nanoseconds per tick.
	private final long NANOSECONDS_PER_TICK = 100L;
	// 1000000 nanoseconds per millisecond.
	private final long NANOS_PER_MILLISECOND = 1000000L;

	private long elapsedNanoSeconds;
	private long startTimeStamp;
	private boolean isRunning;

	public Stopwatch() {
		reset();
	}

	public void start() {
		// Calling start on a running Stopwatch throws an error.
		if (isRunning) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		} else {
			startTimeStamp = System.nanoTime();
			isRunning = true;
		}
	}

	/** Creates a new instance of this class and starts the Stopwatch */
	public static Stopwatch startNew() {
		Stopwatch s = new Stopwatch();
		s.start();
		return s;
	}

	public void stop() {
		// Calling stop on a stopped Stopwatch throws an error.
		if (!isRunning) {
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		} else {
			long endTimeStamp = System.nanoTime();
			long elapsedThisPeriod = endTimeStamp - startTimeStamp;
			elapsedNanoSeconds += elapsedThisPeriod;
			isRunning = false;
		}
	}

	public void reset() {
		elapsedNanoSeconds = 0;
		isRunning = false;
		startTimeStamp = 0;
	}

	// ++++++++++ GETTERS ++++++++++ //
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Returns a {@code TimeSpan} representing the elapsed time of this
	 * Stopwatch since the last start time.
	 * 
	 * @return a {@code TimeSpan} representing the elapsed time since the last
	 *         start time.
	 */
	public TimeSpan getElapsed() {
		return new TimeSpan(getElapsedTicks());
	}

	/**
	 * Returns the number of elapsed ticks of this Stopwatch since the last
	 * start time. There is 100 nanoseconds per tick.
	 * 
	 * @return the number of elapsed ticks since the last start time.
	 */
	public long getElapsedTicks() {
		return getElapsedNanoseconds() / NANOSECONDS_PER_TICK;
	}

	/**
	 * Returns the number of elapsed milliseconds of this Stopwatch since the
	 * last start time.
	 * 
	 * @return the number of elapsed milliseconds since the last start time.
	 */

	public long getElapsedMilliseconds() {
		return getElapsedNanoseconds() / NANOS_PER_MILLISECOND;
	}

	/**
	 * Returns the number of elapsed nanoseconds of this Stopwatch since the
	 * last start time.
	 * 
	 * @return the number of elapsed nanoseconds since the last start time.
	 */
	public long getElapsedNanoseconds() {
		long elapsedNanos = elapsedNanoSeconds;

		if (isRunning) {
			// If the StopWatch is running, add elapsed time since the Stopwatch
			// last started time.
			long currentTimeStamp = System.nanoTime();
			long elapsedUntilNow = currentTimeStamp - startTimeStamp;
			elapsedNanos += elapsedUntilNow;
		}
		return elapsedNanos;
	}

}
