package jMono_Framework;

import jMono_Framework.math.Vector2;

// C# struct
/**
 * Describes a 2D-point.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Point
{
    // #region Private Fields

    // private static final Point zeroPoint = new Point();

    // #endregion

    // #region Public Fields

    /**
     * The x coordinate of this {@link Point}.
     */
    public int x;

    /**
     * The y coordinate of this {@link Point}.
     */
    public int y;

    // #endregion

    // #region Properties

    /**
     * Returns a {@link Point} with coordinates 0, 0.
     * 
     * @return
     */
    public static Point zero()
    {
        return new Point();
    }

    // #endregion

    // #region Internal Properties

    protected String debugDisplayString()
    {
        return this.x + "  " + this.y;
    }

    // #endregion

    // #region Constructors

    // NOTE(Eric): Added this since it is provided by default for struct in C#
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

    // #endregion

    // #region Operators

    /**
     * Adds a {@link Point} to the current instance.
     * 
     * @param other
     *        The other {@link Point} to add to this instance.
     * @return Sum of the points.
     */
    public Point add(Point other)
    {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    /**
     * Subtracts a {@link Point} from the current instance.
     * 
     * @param other
     *        the other {@link Point} to subtract from this instance.
     * @return Result of the subtraction.
     */
    public Point subtract(Point other)
    {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    /**
     * Multiplies the components of the current instance by the components of the specified
     * {@link Point}.
     * 
     * @param other
     *        The other {@link Point} used to multiply with the current instance.
     * @return Result of the multiplication.
     */
    public Point multiply(Point other)
    {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    /**
     * Divides the components of the current instance by the components of the specified
     * {@link Point}.
     * 
     * @param divisor
     *        Divisor {@link Point} on the right of the div sign.
     * @return The result of dividing the points.
     */
    public Point divide(Point divisor)
    {
        this.x /= divisor.x;
        this.y /= divisor.y;
        return this;
    }

    // #endregion

    // #region Public methods

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

    // #endregion

    // #region Public methods

    /**
     * Indicates whether some other {@link Object} is "equal to" this one.
     * 
     * @param obj
     *        The reference {@code Object} with which to compare.
     * @return {@code true} if this {@code Object} is the same as the obj argument; {@code false}
     *         otherwise.
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

    /**
     * Indicates whether the current instance is equal to the specified {@link Point}.
     * 
     * @param other
     *        The {@code Point} to compare.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(Point other)
    {
        return ((x == other.x) && (y == other.y));
    }

    /**
     * Indicates whether some other object is "not equal to" this one.
     * 
     * @param obj
     *        the reference object with which to compare.
     * @return {@code false} if this object is the same as the obj argument; {@code true} otherwise.
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
        int hash = 17;
        hash = hash * 23 + x;
        hash = hash * 23 + y;
        return hash;
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

    // #endregion
}
