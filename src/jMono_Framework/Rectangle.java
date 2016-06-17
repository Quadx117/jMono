package jMono_Framework;

import jMono_Framework.math.Vector2;

// C# struct
/**
 * Describes a 2D-rectangle.
 * 
 * @author Eric Perron
 *
 */
public class Rectangle
{
	private static Rectangle emptyRectangle = new Rectangle();

	// / <summary>
	// / The x coordinate of the top-left corner of this <see cref="Rectangle"/>.
	// / </summary>
	public int x;

	// / <summary>
	// / The y coordinate of the top-left corner of this <see cref="Rectangle"/>.
	// / </summary>
	public int y;

	// / <summary>
	// / The width of this <see cref="Rectangle"/>.
	// / </summary>
	public int width;

	// / <summary>
	// / The height of this <see cref="Rectangle"/>.
	// / </summary>
	public int height;

	// / <summary>
	// / Returns a <see cref="Rectangle"/> with X=0, Y=0, Width=0, Height=0.
	// / </summary>
	public static Rectangle empty()
	{
		return emptyRectangle;
	}

	// / <summary>
	// / Returns the x coordinate of the left edge of this <see cref="Rectangle"/>.
	// / </summary>
	public int left()
	{
		return this.x;
	}

	// / <summary>
	// / Returns the x coordinate of the right edge of this <see cref="Rectangle"/>.
	// / </summary>
	public int right()
	{
		return (this.x + this.width);
	}

	// / <summary>
	// / Returns the y coordinate of the top edge of this <see cref="Rectangle"/>.
	// / </summary>
	public int top()
	{
		return this.y;
	}

	// / <summary>
	// / Returns the y coordinate of the bottom edge of this <see cref="Rectangle"/>.
	// / </summary>
	public int bottom()
	{
		return (this.y + this.height);
	}

	// / <summary>
	// / Whether or not this <see cref="Rectangle"/> has a <see cref="Width"/> and
	// / <see cref="Height"/> of 0, and a <see cref="Location"/> of (0, 0).
	// / </summary>
	public boolean isEmpty()
	{
		return ((((this.width == 0) && (this.height == 0)) && (this.x == 0)) && (this.y == 0));
	}

	// / <summary>
	// / The top-left coordinates of this <see cref="Rectangle"/>.
	// / </summary>
	public Point getLocation()
	{
		return new Point(this.x, this.y);
	}

	public void setLocation(Point value)
	{
		x = value.x;
		y = value.y;
	}

	// / <summary>
	// / The width-height coordinates of this <see cref="Rectangle"/>.
	// / </summary>
	public Point getSize()
	{
		return new Point(this.width, this.height);
	}

	public void setSize(Point value)
	{
		width = value.x;
		height = value.y;
	}

	// / <summary>
	// / A <see cref="Point"/> located in the center of this <see cref="Rectangle"/>.
	// / </summary>
	// / <remarks>
	// / If <see cref="Width"/> or <see cref="Height"/> is an odd number,
	// / the center point will be rounded down.
	// / </remarks>
	public Point getCenter()
	{
		return new Point(this.x + (this.width / 2), this.y + (this.height / 2));
	}

	protected String debugDisplayString()
	{
		return this.x + "  " +
				this.y + "  " +
				this.width + "  " +
				this.height;
	}

	// Note: Added this since it is provided by default for struct in C#
	public Rectangle()
	{
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	// / <summary>
	// / Creates a new instance of <see cref="Rectangle"/> struct, with the specified
	// / position, width, and height.
	// / </summary>
	// / <param name="x">The x coordinate of the top-left corner of the created <see
	// cref="Rectangle"/>.</param>
	// / <param name="y">The y coordinate of the top-left corner of the created <see
	// cref="Rectangle"/>.</param>
	// / <param name="width">The width of the created <see cref="Rectangle"/>.</param>
	// / <param name="height">The height of the created <see cref="Rectangle"/>.</param>
	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// / <summary>
	// / Creates a new instance of <see cref="Rectangle"/> struct, with the specified
	// / location and size.
	// / </summary>
	// / <param name="location">The x and y coordinates of the top-left corner of the created <see
	// cref="Rectangle"/>.</param>
	// / <param name="size">The width and height of the created <see cref="Rectangle"/>.</param>
	public Rectangle(Point location, Point size)
	{
		this.x = location.x;
		this.y = location.y;
		this.width = size.x;
		this.height = size.y;
	}
	
	// NOTE: added this utility method since struct behaves differently than class in C#
	public Rectangle(Rectangle rect)
	{
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
	}

	// / <summary>
	// / Gets whether or not the provided coordinates lie within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="x">The x coordinate of the point to check for containment.</param>
	// / <param name="y">The y coordinate of the point to check for containment.</param>
	// / <returns><c>true</c> if the provided coordinates lie inside this <see cref="Rectangle"/>;
	// <c>false</c> otherwise.</returns>
	public boolean contains(int x, int y)
	{
		return ((((this.x <= x) && (x < (this.x + this.width))) && (this.y <= y)) && (y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided coordinates lie within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="x">The x coordinate of the point to check for containment.</param>
	// / <param name="y">The y coordinate of the point to check for containment.</param>
	// / <returns><c>true</c> if the provided coordinates lie inside this <see cref="Rectangle"/>;
	// <c>false</c> otherwise.</returns>
	public boolean contains(float x, float y)
	{
		return ((((this.x <= x) && (x < (this.x + this.width))) && (this.y <= y)) && (y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Point"/> lies within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The coordinates to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <returns><c>true</c> if the provided <see cref="Point"/> lies inside this <see
	// cref="Rectangle"/>; <c>false</c> otherwise.</returns>
	public boolean contains(Point value)
	{
		return ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Point"/> lies within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The coordinates to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <param name="result"><c>true</c> if the provided <see cref="Point"/> lies inside this <see
	// cref="Rectangle"/>; <c>false</c> otherwise. As an output parameter.</param>
	public void contains(final Point value, boolean result)
	{
		result = ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Vector2"/> lies within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The coordinates to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <returns><c>true</c> if the provided <see cref="Vector2"/> lies inside this <see
	// cref="Rectangle"/>; <c>false</c> otherwise.</returns>
	public boolean contains(Vector2 value)
	{
		return ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Vector2"/> lies within the bounds of this <see
	// cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The coordinates to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <param name="result"><c>true</c> if the provided <see cref="Vector2"/> lies inside this
	// <see cref="Rectangle"/>; <c>false</c> otherwise. As an output parameter.</param>
	public void contains(final Vector2 value, boolean result)
	{
		result = ((((this.x <= value.x) && (value.x < (this.x + this.width))) && (this.y <= value.y)) && (value.y < (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Rectangle"/> lies within the bounds of this
	// <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The <see cref="Rectangle"/> to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <returns><c>true</c> if the provided <see cref="Rectangle"/>'s bounds lie entirely inside
	// this <see cref="Rectangle"/>; <c>false</c> otherwise.</returns>
	public boolean contains(Rectangle value)
	{
		return ((((this.x <= value.x) && ((value.x + value.width) <= (this.x + this.width))) && (this.y <= value.y)) && ((value.y + value.height) <= (this.y + this.height)));
	}

	// / <summary>
	// / Gets whether or not the provided <see cref="Rectangle"/> lies within the bounds of this
	// <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="value">The <see cref="Rectangle"/> to check for inclusion in this <see
	// cref="Rectangle"/>.</param>
	// / <param name="result"><c>true</c> if the provided <see cref="Rectangle"/>'s bounds lie
	// entirely inside this <see cref="Rectangle"/>; <c>false</c> otherwise. As an output
	// parameter.</param>
	// TODO: If I need this method I will need to create my own wrapper to make it work
	public void contains(final Rectangle value, boolean result)
	{
		result = ((((this.x <= value.x) && ((value.x + value.width) <= (this.x + this.width))) && (this.y <= value.y)) && ((value.y + value.height) <= (this.y + this.height)));
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
		return this.equals((Rectangle) obj);
	}

	// Helper method
	private boolean equals(Rectangle other)
	{
		return ((this.x == other.x) && (this.y == other.y) && (this.width == other.width) && (this.height == other.height));
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
		
	// / <summary>
	// / Gets the hash code of this <see cref="Rectangle"/>.
	// / </summary>
	// / <returns>Hash code of this <see cref="Rectangle"/>.</returns>
	@Override
	public int hashCode()
	{
		return (x ^ y ^ width ^ height);
	}

	// / <summary>
	// / Adjusts the edges of this <see cref="Rectangle"/> by specified horizontal and vertical
	// amounts.
	// / </summary>
	// / <param name="horizontalAmount">Value to adjust the left and right edges.</param>
	// / <param name="verticalAmount">Value to adjust the top and bottom edges.</param>
	public void inflate(int horizontalAmount, int verticalAmount)
	{
		x -= horizontalAmount;
		y -= verticalAmount;
		width += horizontalAmount * 2;
		height += verticalAmount * 2;
	}

	// / <summary>
	// / Adjusts the edges of this <see cref="Rectangle"/> by specified horizontal and vertical
	// amounts.
	// / </summary>
	// / <param name="horizontalAmount">Value to adjust the left and right edges.</param>
	// / <param name="verticalAmount">Value to adjust the top and bottom edges.</param>
	public void inflate(float horizontalAmount, float verticalAmount)
	{
		x -= (int) horizontalAmount;
		y -= (int) verticalAmount;
		width += (int) horizontalAmount * 2;
		height += (int) verticalAmount * 2;
	}

	// / <summary>
	// / Gets whether or not the other <see cref="Rectangle"/> intersects with this rectangle.
	// / </summary>
	// / <param name="value">The other rectangle for testing.</param>
	// / <returns><c>true</c> if other <see cref="Rectangle"/> intersects with this rectangle;
	// <c>false</c> otherwise.</returns>
	public boolean intersects(Rectangle value)
	{
		return value.left() < right() &&
				left() < value.right() &&
				value.top() < bottom() &&
				top() < value.bottom();
	}

	// / <summary>
	// / Gets whether or not the other <see cref="Rectangle"/> intersects with this rectangle.
	// / </summary>
	// / <param name="value">The other rectangle for testing.</param>
	// / <param name="result"><c>true</c> if other <see cref="Rectangle"/> intersects with this
	// rectangle; <c>false</c> otherwise. As an output parameter.</param>
	public void intersects(final Rectangle value, boolean result)
	{
		result = value.left() < right() &&
				left() < value.right() &&
				value.top() < bottom() &&
				top() < value.bottom();
	}

	// / <summary>
	// / Creates a new <see cref="Rectangle"/> that contains overlapping region of two other
	// rectangles.
	// / </summary>
	// / <param name="value1">The first <see cref="Rectangle"/>.</param>
	// / <param name="value2">The second <see cref="Rectangle"/>.</param>
	// / <returns>Overlapping region of the two rectangles.</returns>
	public static Rectangle intersect(Rectangle value1, Rectangle value2)
	{
		Rectangle rectangle = null;
		intersect(value1, value2, rectangle);
		return rectangle;
	}

	// / <summary>
	// / Creates a new <see cref="Rectangle"/> that contains overlapping region of two other
	// rectangles.
	// / </summary>
	// / <param name="value1">The first <see cref="Rectangle"/>.</param>
	// / <param name="value2">The second <see cref="Rectangle"/>.</param>
	// / <param name="result">Overlapping region of the two rectangles as an output
	// parameter.</param>
	public static void intersect(final Rectangle value1, final Rectangle value2, Rectangle result)
	{
		if (value1.intersects(value2))
		{
			int right_side = Math.min(value1.x + value1.width, value2.x + value2.width);
			int left_side = Math.max(value1.x, value2.x);
			int top_side = Math.max(value1.y, value2.y);
			int bottom_side = Math.min(value1.y + value1.height, value2.y + value2.height);
			result = new Rectangle(left_side, top_side, right_side - left_side, bottom_side - top_side);
		}
		else
		{
			result = new Rectangle(0, 0, 0, 0);
		}
	}

	// / <summary>
	// / Changes the <see cref="Location"/> of this <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="offsetX">The x coordinate to add to this <see cref="Rectangle"/>.</param>
	// / <param name="offsetY">The y coordinate to add to this <see cref="Rectangle"/>.</param>
	public void offset(int offsetX, int offsetY)
	{
		x += offsetX;
		y += offsetY;
	}

	// / <summary>
	// / Changes the <see cref="Location"/> of this <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="offsetX">The x coordinate to add to this <see cref="Rectangle"/>.</param>
	// / <param name="offsetY">The y coordinate to add to this <see cref="Rectangle"/>.</param>
	public void offset(float offsetX, float offsetY)
	{
		x += (int) offsetX;
		y += (int) offsetY;
	}

	// / <summary>
	// / Changes the <see cref="Location"/> of this <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="amount">The x and y components to add to this <see cref="Rectangle"/>.</param>
	public void offset(Point amount)
	{
		x += amount.x;
		y += amount.y;
	}

	// / <summary>
	// / Changes the <see cref="Location"/> of this <see cref="Rectangle"/>.
	// / </summary>
	// / <param name="amount">The x and y components to add to this <see cref="Rectangle"/>.</param>
	public void offset(Vector2 amount)
	{
		x += (int) amount.x;
		y += (int) amount.y;
	}

	// / <summary>
	// / Returns a <see cref="String"/> representation of this <see cref="Rectangle"/> in the
	// format:
	// / {X:[<see cref="X"/>] Y:[<see cref="Y"/>] Width:[<see cref="Width"/>] Height:[<see
	// cref="Height"/>]}
	// / </summary>
	// / <returns><see cref="String"/> representation of this <see cref="Rectangle"/>.</returns>
	@Override
	public String toString()
	{
		return "{X:" + x + " Y:" + y + " Width:" + width + " Height:" + height + "}";
	}

	// / <summary>
	// / Creates a new <see cref="Rectangle"/> that completely contains two other rectangles.
	// / </summary>
	// / <param name="value1">The first <see cref="Rectangle"/>.</param>
	// / <param name="value2">The second <see cref="Rectangle"/>.</param>
	// / <returns>The union of the two rectangles.</returns>
	public static Rectangle union(Rectangle value1, Rectangle value2)
	{
		int x = Math.min(value1.x, value2.x);
		int y = Math.min(value1.y, value2.y);
		return new Rectangle(x, y,
				Math.max(value1.right(), value2.right()) - x,
				Math.max(value1.bottom(), value2.bottom()) - y);
	}

	// / <summary>
	// / Creates a new <see cref="Rectangle"/> that completely contains two other rectangles.
	// / </summary>
	// / <param name="value1">The first <see cref="Rectangle"/>.</param>
	// / <param name="value2">The second <see cref="Rectangle"/>.</param>
	// / <param name="result">The union of the two rectangles as an output parameter.</param>
	public static void union(final Rectangle value1, final Rectangle value2, Rectangle result)
	{
		result.x = Math.min(value1.x, value2.x);
		result.y = Math.min(value1.y, value2.y);
		result.width = Math.max(value1.right(), value2.right()) - result.x;
		result.height = Math.max(value1.bottom(), value2.bottom()) - result.y;
	}
	
}
