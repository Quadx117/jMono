package gameCore;

import gameCore.math.Vector2;

// TODO: Look at java.awt.Point and it's parent classes and C# Point struct
// and see if there are methods that I should implement.
// C# struct
/**
 * Describes a 2D-point.
 * 
 * @author Eric Perron
 *
 */
public class Point
{
	private static final Point zeroPoint = new Point();

	/**
	 * The x coordinate of this {@link Point}.
	 */
	public int x;

	/**
	 * The y coordinate of this {@link Point}.
	 */
	public int y;

	/**
	 * Returns a {@link Point} with coordinates 0, 0.
	 * 
	 * @return
	 */
	public static Point zero()
	{
		return zeroPoint;
	}

	protected String debugDisplayString()
	{
		return this.x + "  " +
				this.y;
	}

	// Note: Added this since it is provided by default for struct in C#
	public Point()
	{
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Constructs a point with X and Y from two values.
	 * 
	 * @param x
	 *        The x coordinate in 2d-space.
	 * @param y
	 *        The y coordinate in 2d-space.
	 */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a point with X and Y set to the same value.
	 * 
	 * @param value
	 *        The x and y coordinates in 2d-space.
	 */
	public Point(int value)
	{
		this.x = value;
		this.y = value;
	}

	/**
	 * Adds two points.
	 * 
	 * @param value1
	 *        Source {@link Point} on the left of the add sign.
	 * @param value2
	 *        Source {@link Point} on the right of the add sign.
	 * @return Sum of the points.
	 */
	public static Point add(Point value1, Point value2)
	{
		return new Point(value1.x + value2.x, value1.y + value2.y);
	}

	/**
	 * Subtracts a {@link Point} from a {@link Point}.
	 * 
	 * @param value1
	 *        Source {@link Point} on the left of the sub sign.
	 * @param value2
	 *        Source {@link Point} on the right of the sub sign.
	 * @return Result of the subtraction.
	 */
	public static Point subtract(Point value1, Point value2)
	{
		return new Point(value1.x - value2.x, value1.y - value2.y);
	}

	/**
	 * Multiplies the components of two points by each other.
	 * 
	 * @param value1
	 *        Source {@link Point} on the left of the multiply sign.
	 * @param value2
	 *        Source {@link Point} on the right of the multiply sign.
	 * @return Result of the multiplication.
	 */
	public static Point multiply(Point value1, Point value2)
	{
		return new Point(value1.x * value2.x, value1.y * value2.y);
	}

	/**
	 * Divides the components of a {@link Point} by the components of another {@link Point}.
	 * 
	 * @param source
	 *        Source {@link Point} on the left of the div sign.
	 * @param divisor
	 *        Divisor {@link Point} on the right of the div sign.
	 * @return The result of dividing the points.
	 */
	public static Point divide(Point source, Point divisor)
	{
		return new Point(source.x / divisor.x, source.y / divisor.y);
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
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
		return this.equals((Point) obj);
	}

	// Helper method
	private boolean equals(Point other)
	{
		return ((x == other.x) && (y == other.y));
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
	 * Gets the hash code of this {@link Point}.
	 * 
	 * @return Hash code of this {@link Point}.
	 */
	@Override
	public int hashCode()
	{
		return x ^ y;
	}

	/**
	 * Returns a {@link String} representation of this {@link Point} in the format:
	 * {X:[<see cref="X"/>] Y:[<see cref="Y"/>]}
	 * 
	 * @return {@link String} representation of this {@link Point}.
	 */
	@Override
	public String toString()
	{
		return "{X:" + x + " Y:" + y + "}";
	}

	/**
	 * Gets a {@link Vector2} representation for this object.
	 * 
	 * @return A {@link Vector2} representation for this object.
	 */
	public Vector2 ToVector2()
	{
		return new Vector2(x, y);
	}
}
