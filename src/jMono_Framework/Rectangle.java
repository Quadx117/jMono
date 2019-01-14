package jMono_Framework;

import jMono_Framework.math.Vector2;

// C# struct
/**
 * Describes a 2D-rectangle.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Rectangle
{
    // #region Private Fields

    // private static Rectangle emptyRectangle = new Rectangle();

    // #endregion

    // #region Public Fields

    /**
     * The x coordinate of the top-left corner of this {@link Rectangle}.
     */
    public int x;

    /**
     * The y coordinate of the top-left corner of this {@link Rectangle}.
     */
    public int y;

    /**
     * The width of this {@link Rectangle}.
     */
    public int width;

    /**
     * The height of this {@link Rectangle}.
     */
    public int height;

    // #endregion

    // #region Public Properties

    /**
     * Returns a {@link Rectangle} with x=0, y=0, width=0, height=0.
     * 
     * @return A {@code Rectangle} with x=0, y=0, width=0, height=0.
     */
    public static Rectangle empty()
    {
        return new Rectangle();
    }

    /**
     * Returns the x coordinate of the left edge of this {@link Rectangle}.
     * 
     * @return The x coordinate of the left edge of this {@link Rectangle}.
     */
    public int left()
    {
        return this.x;
    }

    /**
     * Returns the x coordinate of the right edge of this {@link Rectangle}.
     * 
     * @return The x coordinate of the right edge of this {@link Rectangle}.
     */
    public int right()
    {
        return (this.x + this.width);
    }

    /**
     * Returns the y coordinate of the top edge of this {@link Rectangle}.
     * 
     * @return The y coordinate of the top edge of this {@link Rectangle}.
     */
    public int top()
    {
        return this.y;
    }

    /**
     * Returns the y coordinate of the bottom edge of this {@link Rectangle}.
     * 
     * @return
     */
    public int bottom()
    {
        return (this.y + this.height);
    }

    /**
     * Whether or not this {@link Rectangle} has a {@link #width} and {@link #height} of 0, and a
     * {@link #location()} of (0, 0).
     * 
     * @return {@code true} if all components of this {@code Rectangle} are equal to 0;
     *         {@code false} otherwise.
     */
    public boolean isEmpty()
    {
        return ((((this.width == 0) && (this.height == 0)) && (this.x == 0)) && (this.y == 0));
    }

    /**
     * Returns the top-left coordinates of this {@link Rectangle}.
     * 
     * @return The top-left coordinates of this {@link Rectangle}.
     */
    public Point getLocation()
    {
        return new Point(this.x, this.y);
    }

    public void setLocation(Point value)
    {
        x = value.x;
        y = value.y;
    }

    /**
     * Returns the width-height coordinates of this {@link Rectangle}.
     * 
     * @return The width-height coordinates of this {@link Rectangle}.
     */
    public Point getSize()
    {
        return new Point(this.width, this.height);
    }

    public void setSize(Point value)
    {
        width = value.x;
        height = value.y;
    }

    /**
     * Returns a {@link Point} located in the center of this {@link Rectangle}.
     * <p>
     * If {@link #width} or {@link #height} is an odd number, the center point will be rounded down.
     * 
     * @return A {@code Point} located in the center of this {@code Rectangle}.
     */
    public Point getCenter()
    {
        return new Point(this.x + (this.width / 2), this.y + (this.height / 2));
    }

    // #endregion

    // #region Internal Properties

    protected String debugDisplayString()
    {
        return this.x + "  " +
               this.y + "  " +
               this.width + "  " +
               this.height;
    }

    // #endregion

    // #region Constructors

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    public Rectangle()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    // NOTE(Eric): added this utility constructor since struct behaves differently than class in C#
    public Rectangle(Rectangle rect)
    {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    /**
     * Creates a new instance of {@link Rectangle} struct, with the specified position, width, and
     * height.
     * 
     * @param x
     *        The x coordinate of the top-left corner of the created {@link Rectangle}.
     * @param y
     *        The y coordinate of the top-left corner of the created {@link Rectangle}.
     * @param width
     *        The width of the created {@link Rectangle}.
     * @param height
     *        The height of the created {@link Rectangle}.
     */
    public Rectangle(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new instance of {@link Rectangle} struct, with the specified location and size.
     * 
     * @param location
     *        The x and y coordinates of the top-left corner of the created {@link Rectangle}.
     * @param size
     *        The width and height of the created {@link Rectangle}.
     */
    public Rectangle(Point location, Point size)
    {
        this.x = location.x;
        this.y = location.y;
        this.width = size.x;
        this.height = size.y;
    }

    // #endregion

    // #region Public Methods

    /**
     * Gets whether or not the provided coordinates lie within the bounds of this {@link Rectangle}.
     * 
     * @param x
     *        The x coordinate of the point to check for containment.
     * @param y
     *        The y coordinate of the point to check for containment.
     * @return {@link true} if the provided coordinates lie inside this {@link Rectangle}; {@link
     *         false} otherwise.
     */
    public boolean contains(int x, int y)
    {
        return ((((this.x <= x) && (x < (this.x + this.width))) && (this.y <= y)) && (y < (this.y + this.height)));
    }

    /**
     * Gets whether or not the provided coordinates lie within the bounds of this {@link Rectangle}.
     * 
     * @param x
     *        The x coordinate of the point to check for containment.
     * @param y
     *        The y coordinate of the point to check for containment.
     * @return {@link true} if the provided coordinates lie inside this {@link Rectangle}; {@link
     *         false} otherwise.
     */
    public boolean contains(float x, float y)
    {
        return ((((this.x <= x) && (x < (this.x + this.width))) && (this.y <= y)) && (y < (this.y + this.height)));
    }

    /**
     * Gets whether or not the provided {@link Point} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The coordinates to check for inclusion in this {@link Rectangle}.
     * @return {@link true} if the provided {@link Point} lies inside this {@link Rectangle};
     *         {@link false} otherwise.
     */
    public boolean contains(Point value)
    {
        return ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not the provided {@link Point} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The coordinates to check for inclusion in this {@link Rectangle}.
     * @param result
     *        {@link true} if the provided {@link Point} lies inside this {@link Rectangle}; {@link
     *        false} otherwise. As an output parameter.
     */
    // public void contains(final Point value, boolean result)
    // {
    // result = ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y))
    // && (value.y < (this.y + this.height)));
    // }

    /**
     * Gets whether or not the provided {@link Vector2} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The coordinates to check for inclusion in this {@link Rectangle}.
     * @return {@link true} if the provided {@link Vector2} lies inside this {@link Rectangle};
     *         {@link false} otherwise.
     */
    public boolean contains(Vector2 value)
    {
        return ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
    }

    /**
     * Gets whether or not the provided {@link Vector2} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The coordinates to check for inclusion in this {@link Rectangle}.
     * @param result
     *        {@link true} if the provided {@link Vector2} lies inside this {@link Rectangle};
     *        {@link false} otherwise. As an output parameter.
     */
    public void contains(final Vector2 value, boolean result)
    {
        result = ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
    }

    /**
     * Gets whether or not the provided {@link Rectangle} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The {@link Rectangle} to check for inclusion in this {@link Rectangle}.
     * @return {@link true} if the provided {@link Rectangle}'s bounds lie entirely inside this
     *         {@link Rectangle}; {@link false} otherwise.
     */
    public boolean contains(Rectangle value)
    {
        return ((((this.x <= value.x) && ((value.x + value.width) <= (this.x + this.width))) && (this.y <= value.y)) && ((value.y + value.height) <= (this.y + this.height)));
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not the provided {@link Rectangle} lies within the bounds of this
     * {@link Rectangle}.
     * 
     * @param value
     *        The {@link Rectangle} to check for inclusion in this {@link Rectangle}.
     * @param result
     *        {@link true} if the provided {@link Rectangle}'s bounds lie entirely inside this
     *        {@link Rectangle}; {@link false} otherwise. As an output parameter.
     */
    // public void contains(final Rectangle value, boolean result)
    // {
    // result = ((((this.x <= value.x) && ((value.x + value.width) <= (this.x + this.width))) &&
    // (this.y <= value.y)) && ((value.y + value.height) <= (this.y + this.height)));
    // }

    /**
     * Indicates whether some other {@link Object} is "equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
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
        return this.equals((Rectangle) obj);
    }

    /**
     * Compares whether current instance is equal to specified {@link Rectangle}.
     * 
     * @param other
     *        The {@link Rectangle} to compare.
     * @return {@link true} if the instances are equal; {@link false} otherwise.
     */
    public boolean equals(Rectangle other)
    {
        return ((this.x == other.x) && (this.y == other.y) && (this.width == other.width) && (this.height == other.height));
    }

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
     * @return {@code false} if this {@code Object} is the same as the obj argument; {@code true}
     *         otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    /**
     * Gets the hash code of this {@link Rectangle}.
     * 
     * @return Hash code of this {@link Rectangle}.
     */
    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 23 + x;
        hash = hash * 23 + y;
        hash = hash * 23 + width;
        hash = hash * 23 + height;
        return hash;
    }

    /**
     * Adjusts the edges of this {@link Rectangle} by specified horizontal and vertical amounts.
     * 
     * @param horizontalAmount
     *        Value to adjust the left and right edges.
     * @param verticalAmount
     *        Value to adjust the top and bottom edges.
     */
    public void inflate(int horizontalAmount, int verticalAmount)
    {
        x -= horizontalAmount;
        y -= verticalAmount;
        width += horizontalAmount * 2;
        height += verticalAmount * 2;
    }

    /**
     * Adjusts the edges of this {@link Rectangle} by specified horizontal and vertical amounts.
     * 
     * @param horizontalAmount
     *        Value to adjust the left and right edges.
     * @param verticalAmount
     *        Value to adjust the top and bottom edges.
     */
    public void inflate(float horizontalAmount, float verticalAmount)
    {
        x -= (int) horizontalAmount;
        y -= (int) verticalAmount;
        width += (int) horizontalAmount * 2;
        height += (int) verticalAmount * 2;
    }

    /**
     * Gets whether or not the other {@link Rectangle} intersects with this rectangle.
     * 
     * @param value
     *        The other rectangle for testing.
     * @return {@link true} if other {@link Rectangle} intersects with this rectangle; {@link false}
     *         otherwise.
     */
    public boolean intersects(Rectangle value)
    {
        return value.left() < right() &&
               left() < value.right() &&
               value.top() < bottom() &&
               top() < value.bottom();
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not the other {@link Rectangle} intersects with this rectangle.
     * 
     * @param value
     *        The other rectangle for testing.
     * @param result
     *        {@link true} if other {@link Rectangle} intersects with this rectangle; {@link false}
     *        otherwise. As an output parameter.
     */
    // public void intersects(final Rectangle value, boolean result)
    // {
    // result = value.left() < right() &&
    // left() < value.right() &&
    // value.top() < bottom() &&
    // top() < value.bottom();
    // }

    /**
     * Creates a new {@link Rectangle} that contains overlapping region of two other rectangles.
     * 
     * @param value1
     *        The first {@link Rectangle}.
     * @param value2
     *        The second {@link Rectangle}.
     * @return Overlapping region of the two rectangles.
     */
    public static Rectangle intersect(Rectangle value1, Rectangle value2)
    {
        Rectangle rectangle = new Rectangle();
        intersect(value1, value2, rectangle);
        return rectangle;
    }

    /**
     * Creates a new {@link Rectangle} that contains overlapping region of two other rectangles.
     * 
     * @param value1
     *        The first {@link Rectangle}.
     * @param value2
     *        The second {@link Rectangle}.
     * @param result
     *        Overlapping region of the two rectangles as an output parameter.
     */
    public static void intersect(final Rectangle value1, final Rectangle value2, Rectangle result)
    {
        if (value1.intersects(value2))
        {
            int right_side = Math.min(value1.x + value1.width, value2.x + value2.width);
            int left_side = Math.max(value1.x, value2.x);
            int top_side = Math.max(value1.y, value2.y);
            int bottom_side = Math.min(value1.y + value1.height, value2.y + value2.height);
            result.x = left_side;
            result.y = top_side;
            result.width = right_side - left_side;
            result.height = bottom_side - top_side;
        }
        else
        {
            result.x = 0;
            result.y = 0;
            result.width = 0;
            result.height = 0;
        }
    }

    /**
     * Changes the {@link #location()} of this {@link Rectangle}.
     * 
     * @param offsetX
     *        The x coordinate to add to this {@link Rectangle}.
     * @param offsetY
     *        The y coordinate to add to this {@link Rectangle}.
     */
    public void offset(int offsetX, int offsetY)
    {
        x += offsetX;
        y += offsetY;
    }

    /**
     * Changes the {@link #location()} of this {@link Rectangle}.
     * 
     * @param offsetX
     *        The x coordinate to add to this {@link Rectangle}.
     * @param offsetY
     *        The y coordinate to add to this {@link Rectangle}.
     */
    public void offset(float offsetX, float offsetY)
    {
        x += (int) offsetX;
        y += (int) offsetY;
    }

    /**
     * Changes the {@link #location()} of this {@link Rectangle}.
     * 
     * @param amount
     *        The x and y components to add to this {@link Rectangle}.
     */
    public void offset(Point amount)
    {
        x += amount.x;
        y += amount.y;
    }

    /**
     * Changes the {@link #location()} of this {@link Rectangle}.
     * 
     * @param amount
     *        The x and y components to add to this {@link Rectangle}.
     */
    public void offset(Vector2 amount)
    {
        x += (int) amount.x;
        y += (int) amount.y;
    }

    /**
     * Returns a {@link String} representation of this {@link Rectangle} in the format:<br/>
     * {X:[{@link #x} Y:[{@link #y}] Width:[{@link #width}] Height:[{@link #height}]}
     * 
     * @return {@link String} representation of this {@link Rectangle}.
     */
    @Override
    public String toString()
    {
        return "{X:" + x + " Y:" + y + " Width:" + width + " Height:" + height + "}";
    }

    /**
     * Creates a new {@link Rectangle} that completely contains two other rectangles.
     * 
     * @param value1
     *        The first {@link Rectangle}.
     * @param value2
     *        The second {@link Rectangle}.
     * @return The union of the two rectangles.
     */
    public static Rectangle union(Rectangle value1, Rectangle value2)
    {
        int x = Math.min(value1.x, value2.x);
        int y = Math.min(value1.y, value2.y);
        return new Rectangle(x, y,
                             Math.max(value1.right(), value2.right()) - x,
                             Math.max(value1.bottom(), value2.bottom()) - y);
    }

    /**
     * Creates a new {@link Rectangle} that completely contains two other rectangles.
     * 
     * @param value1
     *        The first {@link Rectangle}.
     * @param value2
     *        The second {@link Rectangle}.
     * @param result
     *        The union of the two rectangles as an output parameter.
     */
    public static void union(final Rectangle value1, final Rectangle value2, Rectangle result)
    {
        result.x = Math.min(value1.x, value2.x);
        result.y = Math.min(value1.y, value2.y);
        result.width = Math.max(value1.right(), value2.right()) - result.x;
        result.height = Math.max(value1.bottom(), value2.bottom()) - result.y;
    }

    // #endregion
}
