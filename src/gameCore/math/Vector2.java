package gameCore.math;

/**
 * Defines a vector with two float components.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft)
 * 
 */
public class Vector2 // implements IEquatable<Vector2>
{

	// TODO : Do I keep those private static fields ?
	private static final Vector2 zeroVector = new Vector2(0.0f, 0.0f);
	private static final Vector2 unitVector = new Vector2(1.0f, 1.0f);
	private static final Vector2 unitXVector = new Vector2(1.0f, 0.0f);
	private static final Vector2 unitYVector = new Vector2(0.0f, 1.0f);

	// TODO: Change all these like in Vector3
	/**
	 * Returns a Vector2 with all of its components set to zero.
	 */
	public static final Vector2 ZERO = zeroVector;

	/**
	 * Returns a Vector2 with all of its components set to one.
	 */
	public static final Vector2 ONE = unitVector;

	/**
	 * Returns the unit vector for the x-axis, that is a Vector2 with its x
	 * component set to one and all the other components set to zero (1, 0).
	 */
	public static final Vector2 UNIT_X = unitXVector;

	/**
	 * Returns the unit vector for the y-axis, that is a Vector2 with its y
	 * component set to one and all the other components set to zero (0, 1).
	 */
	public static final Vector2 UNIT_Y = unitYVector;

	/**
	 * The x component of this Vector.
	 */
	public float x;

	/**
	 * The y component of this Vector.
	 */
	public float y;

	protected String debugDisplayString()
	{
		return (this.x + "  " + this.y);
	}

	/**
	 * Initializes a new instance of Vector2 with both of its component set to
	 * 0.0f.
	 */
	public Vector2()
	{
		x = y = 0.0f;
	}

	/**
	 * Initializes a new instance of Vector2 with both of its component set to
	 * the specified value.
	 * 
	 * @param value
	 *        The value to initialize both components to.
	 */
	public Vector2(float value)
	{
		this.x = value;
		this.y = value;
	}

	/**
	 * Initializes a new instance of Vector2 with its component set to the
	 * specified values.
	 * 
	 * @param x
	 *        Initial value for the x-component of the vector.
	 * @param y
	 *        Initial value for the y-component of the vector.
	 */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Initializes a new instance of Vector2 with both of its component set to
	 * the same values as the specified vector.
	 * 
	 * @param vector
	 *        The vector used to set the components of this vector.
	 */
	public Vector2(Vector2 vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
	}

	public Vector2 add(Vector2 other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Vector2 add(float value)
	{
		this.x += value;
		this.y += value;
		return this;
	}

	public Vector2 subtract(Vector2 other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Vector2 subtract(float value)
	{
		this.x -= value;
		this.y -= value;
		return this;
	}

	public Vector2 multiply(Vector2 other)
	{
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}

	public Vector2 multiply(float value)
	{
		this.x *= value;
		this.y *= value;
		return this;
	}

	public Vector2 divide(Vector2 other) throws IllegalArgumentException
	{
		if (other.x == 0 || other.y == 0)
			throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= other.x;
		this.y /= other.y;
		return this;
	}

	public Vector2 divide(float value) throws IllegalArgumentException
	{
		if (value == 0)
			throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= value;
		this.y /= value;
		return this;
	}
	
	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains hermite spline interpolation.
	/// </summary>
	/// <param name="value1">The first position vector.</param>
	/// <param name="tangent1">The first tangent vector.</param>
	/// <param name="value2">The second position vector.</param>
	/// <param name="tangent2">The second tangent vector.</param>
	/// <param name="amount">Weighting factor.</param>
	/// <returns>The hermite spline interpolation vector.</returns>
	public static Vector2 hermite(Vector2 value1, Vector2 tangent1, Vector2 value2, Vector2 tangent2, float amount)
	{
		return new Vector2(MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount),	//
        				   MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount));
	}

	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains hermite spline interpolation.
	/// </summary>
	/// <param name="value1">The first position vector.</param>
	/// <param name="tangent1">The first tangent vector.</param>
	/// <param name="value2">The second position vector.</param>
	/// <param name="tangent2">The second tangent vector.</param>
	/// <param name="amount">Weighting factor.</param>
	/// <param name="result">The hermite spline interpolation vector as an output parameter.</param>
	public static void hermite(final Vector2 value1, final Vector2 tangent1, final Vector2 value2, final Vector2 tangent2, float amount, Vector2 result)
	{
		result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
		result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
	}

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	/// <summary>
	/// Returns the squared length of this <see cref="Vector2"/>.
	/// </summary>
	/// <returns>The squared length of this <see cref="Vector2"/>.</returns>
	public float LengthSquared()
	{
		return (x * x) + (y * y);
	}
	
	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains linear interpolation of the specified vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <param name="amount">Weighting value(between 0.0 and 1.0).</param>
	/// <returns>The result of linear interpolation of the specified vectors.</returns>
	public static Vector2 lerp(Vector2 value1, Vector2 value2, float amount)
	{
		return new Vector2(	//
				MathHelper.lerp(value1.x, value2.x, amount),	//
				MathHelper.lerp(value1.y, value2.y, amount));
	}

	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains linear interpolation of the specified vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <param name="amount">Weighting value(between 0.0 and 1.0).</param>
	/// <param name="result">The result of linear interpolation of the specified vectors as an output parameter.</param>
	public static void lerp(final Vector2 value1, final Vector2 value2, float amount, Vector2 result)
	{
		result.x = MathHelper.lerp(value1.x, value2.x, amount);
		result.y = MathHelper.lerp(value1.y, value2.y, amount);
	}
    
	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains a maximal values from the two vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <returns>The <see cref="Vector2"/> with maximal values from the two vectors.</returns>
	public static Vector2 max(Vector2 value1, Vector2 value2)
	{
		return new Vector2(value1.x > value2.x ? value1.x : value2.x,
						   value1.y > value2.y ? value1.y : value2.y);
	}

	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains a maximal values from the two vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <param name="result">The <see cref="Vector2"/> with maximal values from the two vectors as an output parameter.</param>
	public static void max(final Vector2 value1, final Vector2 value2, Vector2 result)
	{
		result.x = value1.x > value2.x ? value1.x : value2.x;
		result.y = value1.y > value2.y ? value1.y : value2.y;
	}

	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains a minimal values from the two vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <returns>The <see cref="Vector2"/> with minimal values from the two vectors.</returns>
	public static Vector2 min(Vector2 value1, Vector2 value2)
	{
		return new Vector2(value1.x < value2.x ? value1.x : value2.x,
						   value1.y < value2.y ? value1.y : value2.y);
	}

	/// <summary>
	/// Creates a new <see cref="Vector2"/> that contains a minimal values from the two vectors.
	/// </summary>
	/// <param name="value1">The first vector.</param>
	/// <param name="value2">The second vector.</param>
	/// <param name="result">The <see cref="Vector2"/> with minimal values from the two vectors as an output parameter.</param>
	public static void min(final Vector2 value1, final Vector2 value2, Vector2 result)
	{
		result.x = value1.x < value2.x ? value1.x : value2.x;
		result.y = value1.y < value2.y ? value1.y : value2.y;
	}
	
	public float dotProduct(Vector2 other)
	{
		return x * other.getX() + y * other.getY();
	}

	public Vector2 normalize()
	{
		float length = length();
		x /= length;
		y /= length;
		return this;
	}

	public Vector2 rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	// ++++++++++ Static methods ++++++++++ //

	/**
	 * Compares whether two {@link Vector2} instances are equal.
	 * 
	 * @param value1
	 *        {@link Vector2} instance on the left of the equal sign.
	 * @param value2
	 *        {@link Vector2} instance on the right of the equal sign.
	 * @return {@code true} if the instances are equal; {@code false} otherwise.
	 */
	public static boolean equals(Vector2 value1, Vector2 value2)
	{
		return value1.x == value2.x && value1.y == value2.y;
	}

	/**
	 * Compares whether two {@link Vector2} instances are not equal.
	 * 
	 * @param value1
	 *        {@link Vector2} instance on the left of the equal sign.
	 * @param value2
	 *        {@link Vector2} instance on the right of the equal sign.
	 * @return {@code true} if the instances are not equal; {@code false} otherwise.
	 */
	public static boolean notEquals(Vector2 value1, Vector2 value2)
	{
		return value1.x != value2.x || value1.y != value2.y;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof Vector2))
			return false;
		Vector2 vec = (Vector2) object;
		if (vec.getX() == this.getX() && vec.getY() == this.getY())
			return true;
		return false;
	}

	/**
	 * Inverts values in the specified {@link Vector2}.
	 * 
	 * @param value
	 *        Source {@link Vector2} on the right of the sub sign.
	 * @return Result of the inversion.
	 */
	public static Vector2 negate(Vector2 value)
	{
		return new Vector2(-value.x, -value.y);
	}

	public static Vector2 add(Vector2 v0, Vector2 v1)
	{
		return new Vector2(v0.getX() + v1.getX(), v0.getY() + v1.getY());
	}

	public static Vector2 subtract(Vector2 v0, Vector2 v1)
	{
		return new Vector2(v0.getX() - v1.getX(), v0.getY() - v1.getY());
	}

	public static Vector2 multiply(Vector2 vector, float value)
	{
		return new Vector2((float) (vector.getX() * value), (float) (vector.getY() * value));
	}

	public static Vector2 multiply(Vector2 vector1, Vector2 vector2)
	{
		return new Vector2((vector1.getX() * vector2.getX()), (vector1.getY() * vector2.getY()));
	}

	/**
	 * Divides the components of a {@link Vector2} by the components of another {@link Vector2}.
	 * 
	 * @param value1
	 *        Source {@link Vector2} on the left of the div sign.
	 * @param value2
	 *        Divisor {@link Vector2} on the right of the div sign.
	 * @return The result of dividing the vectors.
	 */
	public static Vector2 divide(Vector2 value1, Vector2 value2)
	{
		return new Vector2(value1.x / value2.x, value1.y /= value2.y);
	}

	public static Vector2 divide(Vector2 vector, float value)
	{
		return new Vector2((float) (vector.getX() / value), (float) (vector.getY() / value));
	}

	/**
	 * Creates a new {@link Vector2} that contains the cartesian coordinates of a vector
	 * specified in barycentric coordinates and relative to 2d-triangle.
	 * 
	 * @param value1
	 *        The first vector of 2d-triangle.
	 * @param value2
	 *        The second vector of 2d-triangle.
	 * @param value3
	 *        The third vector of 2d-triangle.
	 * @param amount1
	 *        Barycentric scalar <c>b2</c> which represents a weighting factor towards second vector
	 *        of 2d-triangle.
	 * @param amount2
	 *        Barycentric scalar <c>b3</c> which represents a weighting factor towards third vector
	 *        of 2d-triangle.
	 * @return A cartesian translation of barycentric coordinates.
	 */
	public static Vector2 barycentric(Vector2 value1, Vector2 value2, Vector2 value3, float amount1, float amount2)
	{
		return new Vector2(MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
				MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2));
	}

	/**
	 * Creates a new {@link Vector2} that contains the cartesian coordinates of a vector
	 * specified in barycentric coordinates and relative to 2d-triangle.
	 * 
	 * @param value1
	 *        The first vector of 2d-triangle.
	 * @param value2
	 *        The second vector of 2d-triangle.
	 * @param value3
	 *        The third vector of 2d-triangle.
	 * @param amount1
	 *        Barycentric scalar <c>b2</c> which represents a weighting factor towards second vector
	 *        of 2d-triangle.
	 * @param amount2
	 *        Barycentric scalar <c>b3</c> which represents a weighting factor towards third vector
	 *        of 2d-triangle.
	 * @param result
	 *        A cartesian translation of barycentric coordinates as an output parameter.
	 */
	public static void barycentric(final Vector2 value1, final Vector2 value2, final Vector2 value3, float amount1,
			float amount2, Vector2 result)
	{
		result.x = MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2);
		result.y = MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2);
	}

	/**
	 * Creates a new {@link Vector2} that contains CatmullRom interpolation of the specified
	 * vectors.
	 * 
	 * @param value1
	 *        The first vector in interpolation.
	 * @param value2
	 *        The second vector in interpolation.
	 * @param value3
	 *        The third vector in interpolation.
	 * @param value4
	 *        The fourth vector in interpolation.
	 * @param amount
	 *        Weighting factor.
	 * @return The result of CatmullRom interpolation.
	 */
	public static Vector2 catmullRom(Vector2 value1, Vector2 value2, Vector2 value3, Vector2 value4, float amount)
	{
		return new Vector2(MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
				MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount));
	}

	/**
	 * Creates a new {@link Vector2} that contains CatmullRom interpolation of the specified
	 * vectors.
	 * 
	 * @param value1
	 *        The first vector in interpolation.
	 * @param value2
	 *        The second vector in interpolation.
	 * @param value3
	 *        The third vector in interpolation.
	 * @param value4
	 *        The fourth vector in interpolation.
	 * @param amount
	 *        Weighting factor.
	 * @param result
	 *        The result of CatmullRom interpolation as an output parameter.
	 */
	public static void catmullRom(final Vector2 value1, final Vector2 value2, final Vector2 value3,
			final Vector2 value4, float amount, Vector2 result)
	{
		result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
		result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
	}

	// / <summary>
	// / Clamps the specified value within a range.
	// / </summary>
	// / <param name="value1">The value to clamp.</param>
	// / <param name="min">The min value.</param>
	// / <param name="max">The max value.</param>
	// / <returns>The clamped value.</returns>
	public static Vector2 clamp(Vector2 value1, Vector2 min, Vector2 max)
	{
		return new Vector2(MathHelper.clamp(value1.x, min.x, max.x), MathHelper.clamp(value1.y, min.y, max.y));
	}

	/**
	 * Clamps the specified value within a range.
	 * 
	 * @param value1
	 *        The value to clamp.
	 * @param min
	 *        The min value.
	 * @param max
	 *        The max value.
	 * @param result
	 *        The clamped value as an output parameter.
	 */
	public static void clamp(final Vector2 value1, final Vector2 min, final Vector2 max, Vector2 result)
	{
		result.x = MathHelper.clamp(value1.x, min.x, max.x);
		result.y = MathHelper.clamp(value1.y, min.y, max.y);
	}

	/**
	 * Returns the distance between two vectors.
	 * 
	 * @param value1
	 *        The first {@link Vector2}.
	 * @param value2
	 *        The second {@link Vector2}.
	 * @return The distance between two vectors.
	 */
	public static float distance(Vector2 value1, Vector2 value2)
	{
		float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
		return (float) Math.sqrt((v1 * v1) + (v2 * v2));
	}

	/**
	 * Returns the distance between two vectors.
	 * 
	 * @param value1
	 *        The first {@link Vector2}.
	 * @param value2
	 *        The second {@link Vector2}.
	 * @param result
	 *        The distance between two vectors as an output parameter.
	 */
	public static void distance(final Vector2 value1, final Vector2 value2, float result)
	{
		float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
		result = (float) Math.sqrt((v1 * v1) + (v2 * v2));
	}

	/**
	 * Returns the squared distance between two vectors.
	 * 
	 * @param value1
	 *        The first {@link Vector2}.
	 * @param value2
	 *        The second {@link Vector2}.
	 * @return The squared distance between two vectors.
	 */
	public static float distanceSquared(Vector2 value1, Vector2 value2)
	{
		float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
		return (v1 * v1) + (v2 * v2);
	}

	/**
	 * Returns the squared distance between two vectors.
	 * 
	 * @param value1
	 *        The first {@link Vector2}.
	 * @param value2
	 *        The second {@link Vector2}.
	 * @param result
	 *        The squared distance between two vectors as an output parameter.
	 */
	public static void distanceSquared(final Vector2 value1, final Vector2 value2, float result)
	{
		float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
		result = (v1 * v1) + (v2 * v2);
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * 
	 * @return
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * 
	 * @return
	 */
	public float getY()
	{
		return y;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * 
	 * @param x
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * 
	 * @param vector2
	 */
	public void set(Vector2 vector2)
	{
		set(vector2.x, vector2.y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
