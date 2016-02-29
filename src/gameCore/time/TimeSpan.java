package gameCore.time;

// C# struct

/**
 * A TimeSpan object represents a time interval (duration of time or elapsed
 * time) that is measured as a positive or negative number of days, hours,
 * minutes, seconds, and fractions of a second. The TimeSpan structure can also
 * be used to represent the time of day, but only if the time is unrelated to a
 * particular date. Otherwise, the DateTime or DateTimeOffset structure should
 * be used instead. (For more information about using the TimeSpan structure to
 * reflect the time of day, see Choosing Between DateTime, DateTimeOffset, and
 * TimeZoneInfo.)
 * 
 * The largest unit of time that the TimeSpan structure uses to measure duration
 * is a day. Time intervals are measured in days for consistency, because the
 * number of days in larger units of time, such as months and years, varies. For
 * instance, a month can be between 28 and 31 days, while a year can contain 365
 * or 364 days. A decade can have between 1 and 3 leap years, depending on when
 * you map the TimeSpan into the calendar. This is why we do not provide Years()
 * or Months().
 * 
 * The value of a TimeSpan object is the number of ticks that equal the
 * represented time interval. A tick is equal to 100 nanoseconds, or one
 * ten-millionth of a second. The value of a TimeSpan object can be negative or
 * positive and can range from TimeSpan.MIN_VALUE to TimeSpan.MAX_VALUE.
 */
public class TimeSpan implements Comparable<Object>
{
	// TODO : Add missing methods (Parse, TryParse, TimeSpan.Equality, TimeSpan.UnaryNegation, etc)
	// TODO : Re-calibrate to work in nanoseconds and test it before and after

	/**
	 * Represents the number of ticks in 1 millisecond. This field is constant.
	 * The value of this constant is 10 thousand; that is, 10,000.
	 */
	public static final long TICKS_PER_MILLISECOND = 10000L;
	private final double MILLISECONDS_PER_TICK = 1.0 / TICKS_PER_MILLISECOND;

	// private final long TICKS_PER_TENTH_SECOND = TICKS_PER_MILLISECOND * 100L;

	/**
	 * Represents the number of ticks in 1 second.
	 * The value of this constant is 10 million; that is, 10,000,000.
	 */
	public static final long TICKS_PER_SECOND = TICKS_PER_MILLISECOND * 1000L;
	private final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

	/**
	 * Represents the number of ticks in 1 minute. This field is constant.
	 * The value of this constant is 600 million; that is, 600,000,000.
	 */
	public static final long TICKS_PER_MINUTE = TICKS_PER_SECOND * 60L;
	private final double MINUTES_PER_TICK = 1.0 / TICKS_PER_MINUTE;

	/**
	 * Represents the number of ticks in 1 hour. This field is constant.
	 * The value of this constant is 36 billion; that is, 36,000,000,000.
	 */
	public static final long TICKS_PER_HOUR = TICKS_PER_MINUTE * 60L;
	private final double HOURS_PER_TICK = 1.0 / TICKS_PER_HOUR;

	/**
	 * Represents the number of ticks in 1 day. This field is constant.
	 * The value of this constant is 864 billion; that is, 864,000,000,000.
	 */
	public static final long TICKS_PER_DAY = TICKS_PER_HOUR * 24L;
	private final double DAYS_PER_TICK = 1.0 / TICKS_PER_DAY;

	private final long MAX_MILLISECONDS = Long.MAX_VALUE / TICKS_PER_MILLISECOND;
	private final long MIN_MILLISECONDS = Long.MIN_VALUE / TICKS_PER_MILLISECOND;

	/**
	 * Represents the zero TimeSpan value. This field is read-only.
	 * 
	 * <p>
	 * Because the value of the Zero field is a TimeSpan object that represents a zero time value,
	 * you can compare it with other TimeSpan objects to determine whether the latter represent
	 * positive, non-zero, or negative time intervals. You can also use this field to initialize a
	 * TimeSpan object to a zero time value.
	 */
	public static final TimeSpan ZERO = new TimeSpan(0);

	/**
	 * Represents the maximum TimeSpan value. This field is read-only.
	 * 
	 * The value of this field is equivalent to Long.MAX_VALUE ticks. The string
	 * representation of this value is positive 10675199.02:48:05.4775807, or
	 * slightly more than 10,675,199 days.
	 */
	public static final TimeSpan MAX_VALUE = new TimeSpan(Long.MAX_VALUE);

	/**
	 * Represents the minimum TimeSpan value. This field is read-only.
	 * 
	 * The value of this field is equivalent to Long.MIN_VALUE ticks. The string
	 * representation of this value is negative 10675199.02:48:05.4775808, or
	 * slightly more than negative 10,675,199 days.
	 */
	public static final TimeSpan MIN_VALUE = new TimeSpan(Long.MIN_VALUE);

	/**
	 * Unit used to represent a TimeSpan.
	 * 
	 * <p>
	 * A single tick represents one hundred nanoseconds or one ten-millionth of a second.
	 * There are 10,000 ticks in a millisecond.
	 */
	private long ticks;

	// ++++++++++ CONSTRUCTORS ++++++++++ //

	// Note: Added this since it is provided by default for struct in C#
	public TimeSpan()
	{
		this.ticks = 0L;
	}

	/**
	 * Initializes a new instance of the TimeSpan structure to the specified
	 * number of ticks.
	 * 
	 * <p>
	 * A single tick represents one hundred nanoseconds or one ten-millionth of a second.
	 * There are 10,000 ticks in a millisecond.
	 * 
	 * @param ticks
	 *        The value used to set the number of ticks for this TimeSpan.
	 *        One tick represents 100-nanosecond.
	 */
	public TimeSpan(long ticks)
	{
		this.ticks = ticks;
	}

	/**
	 * Initializes a new instance of the TimeSpan structure to a specified
	 * number of hours, minutes, and seconds.
	 * 
	 * <p>
	 * The specified hours, minutes, and seconds are converted to ticks, and
	 * that value initializes this instance.
	 * 
	 * @param hours
	 *        The value used to set the hours for this TimeSpan.
	 * @param minutes
	 *        The value used to set the minutes for this TimeSpan.
	 * @param seconds
	 *        The value used to set the seconds for this TimeSpan.
	 * 
	 * @throws IllegalArgumentException
	 *         If the parameters specify a TimeSpan value less than
	 *         TimeSpan.MIN_VALUE or greater than TimeSpan.MAX_VALUE.
	 */
	public TimeSpan(int hours, int minutes, int seconds) throws IllegalArgumentException
	{
		this(0, hours, minutes, seconds, 0);
	}

	/**
	 * Initializes a new instance of the TimeSpan structure to a specified
	 * number of days, hours, minutes, and seconds.
	 * 
	 * <p>
	 * The specified days, hours, minutes, and seconds are converted to ticks,
	 * and that value initializes this instance.
	 * 
	 * @param days
	 *        The value used to set the days for this TimeSpan.
	 * @param hours
	 *        The value used to set the hours for this TimeSpan.
	 * @param minutes
	 *        The value used to set the minutes for this TimeSpan.
	 * @param seconds
	 *        The value used to set the seconds for this TimeSpan.
	 * 
	 * @throws IllegalArgumentException
	 *         If the parameters specify a TimeSpan value less than
	 *         TimeSpan.MIN_VALUE or greater than TimeSpan.MAX_VALUE.
	 */
	public TimeSpan(int days, int hours, int minutes, int seconds) throws IllegalArgumentException
	{
		this(days, hours, minutes, seconds, 0);
	}

	/**
	 * Initializes a new instance of the TimeSpan structure to a specified
	 * number of days, hours, minutes, seconds, and milliseconds.
	 * 
	 * <p>
	 * The specified days, hours, minutes, seconds, and milliseconds are converted to ticks,
	 * and that value initializes this instance.
	 * 
	 * @param days
	 *        The value used to set the days for this TimeSpan.
	 * @param hours
	 *        The value used to set the hours for this TimeSpan.
	 * @param minutes
	 *        The value used to set the minutes for this TimeSpan.
	 * @param seconds
	 *        The value used to set the seconds for this TimeSpan.
	 * @param milliseconds
	 *        The value used to set the milliseconds for this TimeSpan.
	 * @throws IllegalArgumentException
	 *         If the parameters specify a TimeSpan value less than
	 *         TimeSpan.MIN_VALUE or greater than TimeSpan.MAX_VALUE.
	 */
	public TimeSpan(int days, int hours, int minutes, int seconds, int milliseconds) throws IllegalArgumentException
	{
		long totalMilliSeconds = ((long) days * 3600 * 24 + (long) hours * 3600 + (long) minutes * 60 + seconds) * 1000
				+ milliseconds;
		if (totalMilliSeconds > MAX_MILLISECONDS || totalMilliSeconds < MIN_MILLISECONDS)
			throw new IllegalArgumentException("Overflow: TimeSpan too long");
		ticks = (long) totalMilliSeconds * TICKS_PER_MILLISECOND;
	}

	/**
	 * Initializes a new instance of the TimeSpan structure to the same value as
	 * the specified TimeSpan.
	 * 
	 * @param ts
	 *        The TimeSpan used to create this TimeSpan.
	 */
	public TimeSpan(TimeSpan ts)
	{
		this.ticks = ts.getTicks();
	}

	// ++++++++++ STATIC METHODS ++++++++++ //

	// TODO : Do I want to keep this method here or create a DateTime class (see
	// MSDN)
	/**
	 * Returns a TimeSpan that represents the current system time accurate to
	 * the nearest millisecond.
	 * 
	 * @return A TimeSpan that represents the current system
	 */
	public static TimeSpan now()
	{
		return new TimeSpan(System.currentTimeMillis() * TimeSpan.TICKS_PER_MILLISECOND);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of days, where the
	 * specification is accurate to the nearest millisecond.
	 * 
	 * <p>
	 * The value parameter is converted to milliseconds, which is converted to ticks, and that
	 * number of ticks is used to initialize the new TimeSpan. Therefore, value will only be
	 * considered accurate to the nearest millisecond. Note that, because of the loss of precision
	 * of the Double data type, this conversion can cause an OverflowException for values that are
	 * near to but still in the range of either MIN_VALUE or MAX_VALUE. For example, this causes an
	 * OverflowException in the following attempt to instantiate a TimeSpan object.
	 * 
	 * {@code
	 * // The following throws an OverflowException at runtime
	 * TimeSpan maxSpan = TimeSpan.fromDays(TimeSpan.MAX_VALUE.getTotalDays);
	 * }
	 * 
	 * @param value
	 *        A number of days, accurate to the nearest millisecond.
	 * @return A TimeSpan that represents the specified value.
	 * @throws IllegalArgumentException
	 *         If value is equal to Double.NaN or value is less than
	 *         MIN_VALUE or greater than MAX_VALUE or value is
	 *         Double.POSITIVE_INFINITY or value is
	 *         Double.NEGATIVE_INFINITY.
	 */
	public static TimeSpan fromDays(double value) throws IllegalArgumentException
	{
		return interval(value, MILLIS_PER_DAY);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of hours, where the
	 * specification is accurate to the nearest millisecond.
	 * 
	 * <p>
	 * The value parameter is converted to milliseconds, which is converted to ticks, and that
	 * number of ticks is used to initialize the new TimeSpan. Therefore, value will only be
	 * considered accurate to the nearest millisecond. Note that, because of the loss of precision
	 * of the Double data type, this conversion can cause an OverflowException for values that are
	 * near to but still in the range of either MIN_VALUE or MAX_VALUE. For example, this causes an
	 * OverflowException in the following attempt to instantiate a TimeSpan object.
	 * 
	 * {@code
	 * // The following throws an OverflowException at runtime
	 * TimeSpan maxSpan = TimeSpan.fromHours(TimeSpan.MAX_VALUE.getTotalHours);
	 * }
	 * 
	 * @param value
	 *        A number of hours, accurate to the nearest millisecond.
	 * @return A TimeSpan that represents the specified value.
	 * @throws IllegalArgumentException
	 *         If value is equal to Double.NaN or value is less than
	 *         MIN_VALUE or greater than MAX_VALUE or value is
	 *         Double.POSITIVE_INFINITY or value is
	 *         Double.NEGATIVE_INFINITY.
	 */
	public static TimeSpan fromHours(double value) throws IllegalArgumentException
	{
		return interval(value, MILLIS_PER_HOUR);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of minutes, where
	 * the
	 * specification is accurate to the nearest millisecond.
	 * 
	 * <p>
	 * The value parameter is converted to milliseconds, which is converted to ticks, and that
	 * number of ticks is used to initialize the new TimeSpan. Therefore, value will only be
	 * considered accurate to the nearest millisecond. Note that, because of the loss of precision
	 * of the Double data type, this conversion can cause an OverflowException for values that are
	 * near to but still in the range of either MIN_VALUE or MAX_VALUE. For example, this causes an
	 * OverflowException in the following attempt to instantiate a TimeSpan object.
	 * 
	 * {@code
	 * // The following throws an OverflowException at runtime
	 * TimeSpan maxSpan = TimeSpan.fromMinutes(TimeSpan.MAX_VALUE.getTotalMinutes);
	 * }
	 * 
	 * @param value
	 *        A number of minutes, accurate to the nearest millisecond.
	 * @return A TimeSpan that represents the specified value.
	 * @throws IllegalArgumentException
	 *         If value is equal to Double.NaN or value is less than
	 *         MIN_VALUE or greater than MAX_VALUE or value is
	 *         Double.POSITIVE_INFINITY or value is
	 *         Double.NEGATIVE_INFINITY.
	 */
	public static TimeSpan fromMinutes(double value) throws IllegalArgumentException
	{
		return interval(value, MILLIS_PER_MINUTE);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of seconds, where
	 * the specification is accurate to the nearest millisecond.
	 * 
	 * <p>
	 * The value parameter is converted to milliseconds, which is converted to ticks, and that
	 * number of ticks is used to initialize the new TimeSpan. Therefore, value will only be
	 * considered accurate to the nearest millisecond. Note that, because of the loss of precision
	 * of the Double data type, this conversion can cause an OverflowException for values that are
	 * near to but still in the range of either MIN_VALUE or MAX_VALUE. For example, this causes an
	 * OverflowException in the following attempt to instantiate a TimeSpan object.
	 * 
	 * {@code
	 * // The following throws an OverflowException at runtime
	 * TimeSpan maxSpan = TimeSpan.fromSeconds(TimeSpan.MAX_VALUE.getTotalSeconds);
	 * }
	 * 
	 * @param value
	 *        A number of seconds, accurate to the nearest millisecond.
	 * @return A TimeSpan that represents the specified value.
	 * @throws IllegalArgumentException
	 *         If value is equal to Double.NaN or value is less than
	 *         MIN_VALUE or greater than MAX_VALUE or value is
	 *         Double.POSITIVE_INFINITY or value is
	 *         Double.NEGATIVE_INFINITY.
	 */
	public static TimeSpan fromSeconds(double value) throws IllegalArgumentException
	{
		return interval(value, MILLIS_PER_SECOND);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of milliseconds.
	 * 
	 * <p>
	 * The value parameter is converted to ticks, and that number of ticks is used to initialize the
	 * new TimeSpan. Therefore, value will only be considered accurate to the nearest millisecond.
	 * Note that, because of the loss of precision of the Double data type, this conversion can
	 * cause an OverflowException for values that are near to but still in the range of either
	 * MIN_VALUE or MAX_VALUE. For example, this causes an OverflowException in the following
	 * attempt to instantiate a TimeSpan object.
	 * 
	 * {@code
	 * // The following throws an OverflowException at runtime
	 * TimeSpan maxSpan = TimeSpan.fromMilliseconds(TimeSpan.MAX_VALUE.getTotalMilliseconds);
	 * }
	 * 
	 * @param value
	 *        A number of milliseconds.
	 * @return A TimeSpan that represents the specified value.
	 * @throws IllegalArgumentException
	 *         If value is equal to Double.NaN or value is less than
	 *         MIN_VALUE or greater than MAX_VALUE or value is
	 *         Double.POSITIVE_INFINITY or value is
	 *         Double.NEGATIVE_INFINITY.
	 */
	public static TimeSpan fromMilliseconds(double value) throws IllegalArgumentException
	{
		return interval(value, 1);
	}

	/**
	 * Returns a TimeSpan that represents a specified time, where the
	 * specification is in units of ticks.
	 * 
	 * <p>
	 * This is a convenience method with the same behavior as the TimeSpan.TimeSpan(long) constructor.
	 * A single tick represents one hundred nanoseconds or one ten-millionth of a second.
	 * There are 10,000 ticks in a millisecond.
	 * 
	 * @param value
	 *        A number of ticks that represent a time.
	 * @return A TimeSpan that represents the specified value.
	 */
	public static TimeSpan fromTicks(long value)
	{
		return new TimeSpan(value);
	}

	// TODO : Finish comments
	/**
	 * Internal private helper method used to create a new TimeSpan
	 * 
	 * @param value
	 * @param scale
	 * @return A new TimeSpan object representing the specified interval
	 * @throws IllegalArgumentException
	 *         If the specified value is not a number {@code Double.NaN} or
	 *         if the resulting interval created an overflow.
	 */
	private static TimeSpan interval(double value, int scale) throws IllegalArgumentException
	{
		if (Double.isNaN(value))
			throw new IllegalArgumentException("Argument cannot be NaN");
		double tmp = value * scale;
		double millis = tmp + (value >= 0 ? 0.5 : -0.5);
		if ((millis > Long.MAX_VALUE / TICKS_PER_MILLISECOND) || (millis < Long.MIN_VALUE / TICKS_PER_MILLISECOND))
			throw new IllegalArgumentException("Overflow: TimeSpan too long");
		return new TimeSpan((long) millis * TICKS_PER_MILLISECOND);
	}

	/**
	 * Used when calling the internal private helper method
	 */
	private static final int MILLIS_PER_SECOND = 1000;
	private static final int MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;
	private static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
	private static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

	/**
	 * Adds two specified TimeSpan instances.
	 * 
	 * @param ts1
	 *        The first time interval to add.
	 * @param ts2
	 *        The second time interval to add.
	 * @return A TimeSpan object whose value is the sum of the values of t1 and
	 *         t2.
	 * @throws Exception
	 *         If the resulting TimeSpan is less than MIN_VALUE or greater
	 *         than MAX_VALUE.
	 */
	public static TimeSpan add(TimeSpan ts1, TimeSpan ts2) throws Exception
	{
		long result = ts1.ticks + ts2.ticks;
		// Overflow if signs of operands was identical and result's sign was
		// opposite.
		// >> 63 gives the sign bit (either 64 1's or 64 0's).
		if ((ts1.ticks >> 63 == ts2.ticks >> 63) && (ts1.ticks >> 63 != result >> 63))
			throw new Exception("Overflow: TimeSpan too long");
		return new TimeSpan(result);
	}

	/**
	 * Subtracts a specified TimeSpan from another specified TimeSpan.
	 * 
	 * @param ts1
	 *        The minuend.
	 * @param ts2
	 *        The subtrahend.
	 * @return A TimeSpan object whose value is the result of the value of t1
	 *         minus the value of t2.
	 * @throws Exception
	 *         If the resulting TimeSpan is less than MIN_VALUE or greater
	 *         than MAX_VALUE.
	 */
	public static TimeSpan subtract(TimeSpan ts1, TimeSpan ts2) throws Exception
	{
		long result = ts1.ticks - ts2.ticks;
		// Overflow if signs of operands was different and result's
		// sign was opposite from the first argument's sign.
		// >> 63 gives the sign bit (either 64 1's or 64 0's).
		if ((ts1.ticks >> 63 != ts2.ticks >> 63) && (ts1.ticks >> 63 != result >> 63))
			throw new Exception("Overflow: TimeSpan too long");
		return new TimeSpan(result);
	}

	/**
	 * Compares two TimeSpan values and returns an integer that indicates
	 * whether the first value is shorter than, equal to, or longer than the
	 * second value.
	 * 
	 * @param t1
	 *        The first time interval to compare.
	 * @param t2
	 *        The second time interval to compare.
	 * @return -1 if t1 is shorter than t2, 0 if t1 is equal to t2 or 1 if t1 is
	 *         longer than t2.
	 */
	public static int compare(TimeSpan t1, TimeSpan t2)
	{
		if (t1.ticks > t2.ticks)
			return 1;
		if (t1.ticks < t2.ticks)
			return -1;
		return 0;
	}

	// ++++++++++ NON-STATIC METHODS ++++++++++ //

	/*
	 * Original method
	 * Returns a new TimeSpan object whose value is the sum of the specified
	 * TimeSpan object and this instance.
	 * 
	 * <p>
	 * The return value must be between TimeSpan.MIN_VALUE and
	 * TimeSpan.MAX_VALUE;
	 * otherwise, an exception is thrown.
	 * 
	 * The return value is a new TimeSpan; the original TimeSpan is not
	 * modified.
	 * 
	 * @param ts
	 * The time interval to add.
	 * 
	 * @return A new TimeSpan object that represents the value of this instance
	 * plus the value of ts.
	 * 
	 * @throws Exception
	 * If the resulting TimeSpan is less than TimeSpan.MIN_VALUE or
	 * greater than TimeSpan.MAX_VALUE.
	 * public TimeSpan add(TimeSpan ts) throws Exception {
	 * long result = ticks + ts.ticks;
	 * // Overflow if signs of operands was identical and result's sign was
	 * // opposite.
	 * // >> 63 gives the sign bit (either 64 1's or 64 0's).
	 * if ((ticks >> 63 == ts.ticks >> 63) && (ticks >> 63 != result >> 63))
	 * throw new Exception("Overflow: TimeSpan too long");
	 * return new TimeSpan(result);
	 * }
	 */

	/**
	 * Adds the time interval specified by the TimeSpan object to the time
	 * interval of this instance.
	 * 
	 * <p>
	 * The resulting value must be between TimeSpan.MIN_VALUE and TimeSpan.MAX_VALUE;
	 * otherwise, an exception is thrown.
	 * 
	 * @param ts
	 *        The time interval to add to this instance.
	 * @throws Exception
	 *         If the resulting TimeSpan is less than TimeSpan.MIN_VALUE or
	 *         greater than TimeSpan.MAX_VALUE.
	 */
	public void add(TimeSpan ts) throws Exception
	{
		long result = ticks + ts.ticks;
		// Overflow if signs of operands was identical and result's sign was
		// opposite.
		// >> 63 gives the sign bit (either 64 1's or 64 0's).
		if ((ticks >> 63 == ts.ticks >> 63) && (ticks >> 63 != result >> 63))
			throw new Exception("Overflow: TimeSpan too long");
		ticks = result;
	}

	/*
	 * Original method
	 * Returns a new TimeSpan object whose value is the difference the specified
	 * TimeSpan object and this instance.
	 * 
	 * <p>
	 * The return value must be between TimeSpan.MIN_VALUE and
	 * TimeSpan.MAX_VALUE;
	 * otherwise, an exception is thrown.
	 * 
	 * The return value is a new TimeSpan; the original TimeSpan is not
	 * modified.
	 * 
	 * @param ts
	 * The time interval to be subtracted.
	 * 
	 * @return A new TimeSpan object whose value is the result of the value of
	 * this instance minus the value of ts.
	 * 
	 * @throws Exception
	 * If the resulting TimeSpan is less than TimeSpan.MIN_VALUE or
	 * greater than TimeSpan.MAX_VALUE.
	 * public TimeSpan subtract(TimeSpan ts) throws Exception {
	 * long result = ticks - ts.ticks;
	 * // Overflow if signs of operands was different and result's
	 * // sign was opposite from the first argument's sign.
	 * // >> 63 gives the sign bit (either 64 1's or 64 0's).
	 * if ((ticks >> 63 != ts.ticks >> 63) && (ticks >> 63 != result >> 63))
	 * throw new Exception("Overflow: TimeSpan too long");
	 * return new TimeSpan(result);
	 * }
	 */

	/**
	 * Subtracts the time interval specified by the TimeSpan object to the time
	 * interval of this instance.
	 * 
	 * <p>
	 * The resulting value must be between TimeSpan.MIN_VALUE and TimeSpan.MAX_VALUE;
	 * otherwise, an exception is thrown.
	 * 
	 * @param ts
	 *        The time interval to be subtracted to this instance.
	 * @throws Exception
	 *         If the resulting TimeSpan is less than TimeSpan.MIN_VALUE or
	 *         greater than TimeSpan.MAX_VALUE.
	 */
	public void subtract(TimeSpan ts) throws Exception
	{
		long result = ticks - ts.ticks;
		// Overflow if signs of operands was different and result's
		// sign was opposite from the first argument's sign.
		// >> 63 gives the sign bit (either 64 1's or 64 0's).
		if ((ticks >> 63 != ts.ticks >> 63) && (ticks >> 63 != result >> 63))
			throw new Exception("Overflow: TimeSpan too long");
		ticks = result;
	}

	/*
	 * Original method
	 * 
	 * Returns a new TimeSpan object whose value is the negated value of this
	 * instance.
	 * 
	 * @return A new TimeSpan object with the same numeric value as this
	 * instance, but with the opposite sign.
	 * 
	 * @throws Exception
	 * If the negated value of this instance cannot be represented
	 * by a TimeSpan; that is, the value of this instance is
	 * MIN_VALUE.
	 * 
	 * public TimeSpan negate() throws Exception {
	 * if (ticks == TimeSpan.MIN_VALUE.ticks) throw new
	 * Exception("Overflow_NegateTwosCompNum");
	 * return new TimeSpan(-ticks);
	 * }
	 */

	/**
	 * Negates the value of this instance, that is, flip the sign.
	 * 
	 * @throws Exception
	 *         If the negated value of this instance cannot be represented
	 *         by a TimeSpan; that is, the value of this instance is
	 *         MIN_VALUE.
	 */
	public void negate() throws Exception
	{
		if (ticks == TimeSpan.MIN_VALUE.ticks)
			throw new Exception("Overflow: Negate Two's Complement Number is too big");
		ticks = -ticks;
	}

	/**
	 * Returns a new TimeSpan object whose value is the absolute value of the
	 * current TimeSpan object.
	 * 
	 * @return A new object whose value is the absolute value of the current
	 *         TimeSpan object.
	 * @throws Exception
	 *         If the value of this instance is TimeSpan.MIN_VALUE.
	 */
	public TimeSpan getDuration() throws Exception
	{
		if (ticks == TimeSpan.MIN_VALUE.ticks)
			throw new Exception("Overflow_Duration");
		return new TimeSpan(ticks >= 0 ? ticks : -ticks);
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *        The other object to compare with this instance.
	 * @return {@code true} if value is a TimeSpan object that represents the
	 *         same time interval as the current TimeSpan; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj.getClass() != this.getClass())
		{
			return false;
		}
		return this.equals((TimeSpan) obj);
	}

	// Helper method
	private boolean equals(TimeSpan other)
	{
		return this.ticks == other.ticks;
	}

	/**
	 * Indicates whether some other object is "not equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see #equals(Object)
	 */
	public boolean notEquals(Object obj)
	{
		return !this.equals(obj);
	}
	
	/**
	 * Compares this instance to a specified object and returns an integer that
	 * indicates whether this instance is shorter than, equal to, or longer than
	 * the specified object.
	 * 
	 * <p>
	 * Any instance of TimeSpan, regardless of its value, is considered greater than null.
	 * 
	 * The value parameter must be an instance of TimeSpan or null; otherwise, an exception is
	 * thrown.
	 * 
	 * @param other
	 *        The other object to compare with this instance, or null.
	 * @return -1 if t1 is shorter than t2, 0 if t1 is equal to t2 or 1 if t1 is
	 *         longer than t2.
	 * @throws IllegalArgumentException
	 *         If other is not a TimeSpan.
	 */
	@Override
	public int compareTo(Object other) throws IllegalArgumentException
	{
		if (other == null)
			return 1;
		if (!(other instanceof TimeSpan))
			throw new IllegalArgumentException("Argument must be TimeSpan");
		long t = ((TimeSpan) other).ticks;
		if (ticks > t)
			return 1;
		if (ticks < t)
			return -1;
		return 0;
	}

	/**
	 * Converts the value of the current TimeSpan object to its equivalent
	 * string representation.
	 * 
	 * <p>
	 * The returned string has the following format:
	 * 
	 * {@code [-][d.]hh:mm:ss[.fffffff]}
	 * 
	 * Elements in square brackets ([ and ]) may not be included in the returned string. Colons and
	 * periods (: and .) are literal characters. The non-literal elements are listed here :
	 * 
	 * "-" A minus sign, which indicates a negative time interval. No sign is included for a
	 * positive time span.
	 * 
	 * "d" The number of days in the time interval. This element is omitted if the time interval is
	 * less than one day.
	 * 
	 * "hh" The number of hours in the time interval, ranging from 0 to 23.
	 * 
	 * "mm" The number of minutes in the time interval, ranging from 0 to 59.
	 * 
	 * "ss" The number of seconds in the time interval, ranging from 0 to 59.
	 * 
	 * "fffffff" Fractional seconds in the time interval. This element is omitted if the time
	 * interval does not include fractional seconds. If present, fractional seconds are always
	 * expressed using seven decimal digits.
	 * 
	 * @return The string representation of the current TimeSpan value.
	 */
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		if (getDays() >= 1 || getDays() <= -1)
			str.append(String.format("%02d.", getDays()));
		str.append(String.format("%02d:", getHours()));
		str.append(String.format("%02d:", getMinutes()));
		str.append(String.format("%02d", getSeconds()));
		if (getMilliseconds() >= 1)
			str.append(String.format(".%d%s", getMilliseconds(),
					TRAILING_ZEROS.substring(Integer.toString(getMilliseconds()).length())));
		return str.toString();
	}

	/**
	 * Used in the toString method to display a fixed number of decimals for the
	 * milliseconds component.
	 */
	private final String TRAILING_ZEROS = "0000000";

	// ----------------------------------------------//
	// TODO : Validate and adjust name to java's naming convention
	public boolean greaterThan(TimeSpan other)
	{
		return this.ticks > other.ticks;
	}

	public boolean greaterThanOrEqual(TimeSpan other)
	{
		return this.ticks >= other.ticks;
	}

	public boolean lessThan(TimeSpan other)
	{
		return this.ticks < other.ticks;
	}

	public boolean lessThanOrEqual(TimeSpan other)
	{
		return this.ticks <= other.ticks;
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Gets the days component of the time interval represented by the current
	 * TimeSpan structure.
	 * 
	 * <p>
	 * A TimeSpan value can be represented as [-]d.hh:mm:ss.ff, where the optional minus sign
	 * indicates a negative time interval, the d component is days, hh is hours as measured on a
	 * 24-hour clock, mm is minutes, ss is seconds, and ff is fractions of a second. The returned
	 * value is the day component, d.
	 * 
	 * <p>
	 * The returned value represents whole days, whereas the value returned by getTotalDays()
	 * represents whole and fractional days.
	 * 
	 * @return The day component of this instance. The return value can be
	 *         positive or negative.
	 */
	public int getDays()
	{
		return (int) (ticks / TICKS_PER_DAY);
	}

	/**
	 * Gets the hours component of the time interval represented by the current
	 * TimeSpan structure.
	 * 
	 * <p>
	 * A TimeSpan value can be represented as [-]d.hh:mm:ss.ff, where the optional minus sign
	 * indicates a negative time interval, the d component is days, hh is hours as measured on a
	 * 24-hour clock, mm is minutes, ss is seconds, and ff is fractions of a second. The returned
	 * value is the hours component, hh.
	 * 
	 * <p>
	 * The returned value represents whole hours, whereas the value returned by getTotalHours()
	 * represents whole and fractional hours.
	 * 
	 * @return The hour component of the current TimeSpan structure. The return
	 *         value ranges from -23 through 23.
	 */
	public int getHours()
	{
		return (int) ((ticks / TICKS_PER_HOUR) % 24);
	}

	/**
	 * Gets the minutes component of the time interval represented by the
	 * current TimeSpan structure.
	 * 
	 * <p>
	 * A TimeSpan value can be represented as [-]d.hh:mm:ss.ff, where the optional minus sign
	 * indicates a negative time interval, the d component is days, hh is hours as measured on a
	 * 24-hour clock, mm is minutes, ss is seconds, and ff is fractions of a second. The returned
	 * value is the minute component, mm.
	 * 
	 * <p>
	 * The returned value represents whole minutes, whereas the value returned by getTotalMinutes()
	 * represents whole and fractional minutes.
	 * 
	 * @return The minute component of the current TimeSpan structure. The
	 *         return value ranges from -59 through 59.
	 */
	public int getMinutes()
	{
		return (int) ((ticks / TICKS_PER_MINUTE) % 60);
	}

	/**
	 * Gets the seconds component of the time interval represented by the
	 * current TimeSpan structure.
	 * 
	 * <p>
	 * A TimeSpan value can be represented as [-]d.hh:mm:ss.ff, where the optional minus sign
	 * indicates a negative time interval, the d component is days, hh is hours as measured on a
	 * 24-hour clock, mm is minutes, ss is seconds, and ff is fractions of a second. The returned
	 * value is the seconds component, ss.
	 * 
	 * <p>
	 * The returned value represents whole seconds, whereas the value returned by getTotalSeconds()
	 * represents whole and fractional seconds.
	 * 
	 * @return The second component of the current TimeSpan structure. The
	 *         return value ranges from -59 through 59.
	 */
	public int getSeconds()
	{
		return (int) ((ticks / TICKS_PER_SECOND) % 60);
	}

	/**
	 * Gets the milliseconds component of the time interval represented by the
	 * current TimeSpan structure.
	 * 
	 * <p>
	 * A TimeSpan value can be represented as [-]d.hh:mm:ss.ff, where the optional minus sign
	 * indicates a negative time interval, the d component is days, hh is hours as measured on a
	 * 24-hour clock, mm is minutes, ss is seconds, and ff is fractions of a second. The returned
	 * value is the fractional second component, ff.
	 * 
	 * <p>
	 * The returned value represents whole milliseconds, whereas the value returned by
	 * getTotalMilliseconds represents whole and fractional milliseconds.
	 * 
	 * @return The millisecond component of the current TimeSpan structure. The
	 *         return value ranges from -999 through 999.
	 */
	public int getMilliseconds()
	{
		return (int) ((ticks / TICKS_PER_MILLISECOND) % 1000);
	}

	/**
	 * Gets the number of ticks that represent the value of the current TimeSpan
	 * structure.
	 * 
	 * <p>
	 * The smallest unit of time is the tick, which is equal to 100 nanoseconds or one ten-millionth
	 * of a second. There are 10,000 ticks in a millisecond. The returned value can be negative or
	 * positive to represent a negative or positive time interval.
	 * 
	 * @return The number of ticks contained in this instance.
	 */
	public long getTicks()
	{
		return ticks;
	}

	/**
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional days.
	 * 
	 * <p>
	 * This method converts the value of this instance from ticks to days. This number might include
	 * whole and fractional days.
	 * 
	 * <p>
	 * The returned value represents whole and fractional days, whereas the value returned by
	 * getDays() represents whole days.
	 * 
	 * @return The total number of days represented by this instance.
	 */
	public double getTotalDays()
	{
		return ((double) ticks) * DAYS_PER_TICK;
	}

	/**
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional hours.
	 * 
	 * <p>
	 * This method converts the value of this instance from ticks to hours. This number might
	 * include whole and fractional hours.
	 * 
	 * <p>
	 * The returned value represents whole and fractional hours, whereas the value returned by
	 * getTotalHours() represents whole hours.
	 * 
	 * @return The total number of hours represented by this instance.
	 */
	public double getTotalHours()
	{
		return (double) ticks * HOURS_PER_TICK;
	}

	/**
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional minutes.
	 * 
	 * <p>
	 * This method converts the value of this instance from ticks to minutes. This number might
	 * include whole and fractional minutes.
	 * 
	 * <p>
	 * The returned value represents whole and fractional minutes, whereas the value returned by
	 * getTotalMinutes() represents whole minutes.
	 * 
	 * @return The total number of minutes represented by this instance.
	 */
	public double getTotalMinutes()
	{
		return (double) ticks * MINUTES_PER_TICK;
	}

	/**
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional seconds.
	 * 
	 * <p>
	 * This method converts the value of this instance from ticks to seconds. This number might
	 * include whole and fractional seconds.
	 * 
	 * <p>
	 * The returned value represents whole and fractional seconds, whereas the value returned by
	 * getTotalSeconds() represents whole seconds.
	 * 
	 * @return The total number of seconds represented by this instance.
	 */
	public double getTotalSeconds()
	{
		return (double) ticks * SECONDS_PER_TICK;
	}

	/**
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional milliseconds.
	 * 
	 * <p>
	 * This method converts the value of this instance from ticks to milliseconds. This number might
	 * include whole and fractional milliseconds.
	 * 
	 * <p>
	 * The returned value represents whole and fractional milliseconds, whereas the value returned
	 * by getTotalMilliseconds() represents whole milliseconds.
	 * 
	 * @return The total number of milliseconds represented by this instance.
	 */
	public double getTotalMilliseconds()
	{
		double temp = (double) ticks * MILLISECONDS_PER_TICK;
		if (temp > MAX_MILLISECONDS)
			return (double) MAX_MILLISECONDS;

		if (temp < MIN_MILLISECONDS)
			return (double) MIN_MILLISECONDS;

		return temp;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * Set this TimeSpan's tick to the specified TimeSpan's tick value.
	 * 
	 * @param TimeSpan
	 *        The new tick value for this TimeSpan.
	 */
	public void setTimeSpan(TimeSpan ts)
	{
		this.ticks = ts.ticks;
	}

	/**
	 * Set this TimeSpan's tick to the specified value.
	 * 
	 * @param ticks
	 *        the new tick value for this TimeSpan.
	 */
	public void setTimeSpan(long ticks)
	{
		this.ticks = ticks;
	}

}