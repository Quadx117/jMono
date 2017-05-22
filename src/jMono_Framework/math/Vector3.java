package jMono_Framework.math;

// C# struct
/**
 * Defines a vector with three float components.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft)
 *
 */
public class Vector3
{
	// TODO: Finish comments
	private static Vector3 zero = new Vector3(0f, 0f, 0f);
	private static Vector3 one = new Vector3(1f, 1f, 1f);
	private static Vector3 unitX = new Vector3(1f, 0f, 0f);
	private static Vector3 unitY = new Vector3(0f, 1f, 0f);
	private static Vector3 unitZ = new Vector3(0f, 0f, 1f);
	private static Vector3 up = new Vector3(0f, 1f, 0f);
	private static Vector3 down = new Vector3(0f, -1f, 0f);
	private static Vector3 right = new Vector3(1f, 0f, 0f);
	private static Vector3 left = new Vector3(-1f, 0f, 0f);
	private static Vector3 forward = new Vector3(0f, 0f, -1f);
	private static Vector3 backward = new Vector3(0f, 0f, 1f);

	/**
	 * The x coordinate of this {@code Vector3}.
	 */
	public float x;
	
	/**
	 * The y coordinate of this {@code Vector3}.
	 */
	public float y;
	
	/**
	 * The z coordinate of this {@code Vector3}.
	 */
	public float z;

	/**
	 * Returns a {@code Vector3} with components 0, 0, 0.
	 * 
	 * @return A {@code Vector3} with components 0, 0, 0.
	 */
	public static Vector3 zero()
	{
		return new Vector3(zero);
	}

	/**
	 * Returns a {@code Vector3} with components 1, 1, 1.
	 * 
	 * @return A {@code Vector3} with components 1, 1, 1.
	 */
	public static Vector3 one()
	{
		return new Vector3(one);
	}

	/**
	 * Returns a {@code Vector3} with components 1, 0, 0.
	 * 
	 * @return A {@code Vector3} with components 1, 0, 0.
	 */
	public static Vector3 unitX()
	{
		return new Vector3(unitX);
	}

	/**
	 * Returns a {@code Vector3} with components 0, 1, 0.
	 * 
	 * @return A {@code Vector3} with components 0, 1, 0.
	 */
	public static Vector3 unitY()
	{
		return new Vector3(unitY);
	}

	/**
	 * Returns a {@code Vector3} with components 0, 0, 1.
	 * 
	 * @return A {@code Vector3} with components 0, 0, 1.
	 */
	public static Vector3 unitZ()
	{
		return new Vector3(unitZ);
	}

	/**
	 * Returns a {@code Vector3} with components 0, 1, 0.
	 * 
	 * @return A {@code Vector3} with components 0, 1, 0.
	 */
	public static Vector3 up()
	{
		return new Vector3(up);
	}

	/**
	 * Returns a {@code Vector3} with components 0, -1, 0.
	 * 
	 * @return A {@code Vector3} with components 0, -1, 0.
	 */
	public static Vector3 down()
	{
		return new Vector3(down);
	}

	/**
	 * Returns a {@code Vector3} with components 1, 0, 0.
	 * 
	 * @return A {@code Vector3} with components 1, 0, 0.
	 */
	public static Vector3 right()
	{
		return new Vector3(right);
	}

	/**
	 * Returns a {@code Vector3} with components -1, 0, 0.
	 * 
	 * @return A {@code Vector3} with components -1, 0, 0.
	 */
	public static Vector3 left()
	{
		return new Vector3(left);
	}

	/**
	 * Returns a {@code Vector3} with components 0, 0, -1.
	 * 
	 * @return A {@code Vector3} with components 0, 0, -1.
	 */
	public static Vector3 forward()
	{
		return new Vector3(forward);
	}

	/**
	 * Returns a {@code Vector3} with components 0, 0, 1.
	 * 
	 * @return Returns a {@code Vector3} with components 0, 0, 1.
	 */
	public static Vector3 backward()
	{
		return new Vector3(backward);
	}

	public String debugDisplayString()
	{
		return (this.x + "  " + this.y + "  " + this.z);
	}
	
	// Note: Added this since it is provided by default for struct in C#
	/**
	 * Constructs a 3d vector with X, Y and Z set to 0.
	 */
	public Vector3()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}

	/**
	 * Constructs a 3d vector with X, Y and Z from three values.
	 * 
	 * @param x
	 *        The x coordinate in 3d-space.
	 * @param y
	 *        The y coordinate in 3d-space.
	 * @param z
	 *        The z coordinate in 3d-space.
	 */
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a 3d vector with X, Y and Z set to the same value.
	 * 
	 * @param value
	 *        The x, y and z coordinates in 3d-space.
	 */
	public Vector3(float value)
	{
		this.x = value;
		this.y = value;
		this.z = value;
	}

	/**
	 * Constructs a 3d vector with X, Y and Z set to the same value as the supplied {@code Vector3}.
	 * 
	 * @param value
	 *        Source {@code Vector3}.
	 */
	public Vector3(Vector3 value)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
	}

	/**
	 * Constructs a 3d vector with X, Y from {@link Vector2} and Z from a scalar.
	 * 
	 * @param value
	 *        The x and y coordinates in 3d-space.
	 * @param z
	 *        The z coordinate in 3d-space.
	 */
	public Vector3(Vector2 value, float z)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = z;
	}

	/**
	 * Performs vector addition on {@code value1} and {@code value2}.
	 * 
	 * @param value1
	 *        The first {@code Vector3} to add.
	 * @param value2
	 *        The second {@code Vector3} to add.
	 * @return The result of the vector addition.
	 */
	public static Vector3 add(Vector3 value1, Vector3 value2)
	{
		Vector3 result = new Vector3(value1);
		result.x += value2.x;
		result.y += value2.y;
		result.z += value2.z;
		return result;
	}

	/**
	 * Performs vector addition on {@code value1} and {@code value2}, storing the result of the
	 * addition in {@code result}.
	 * 
	 * @param value1
	 *        The first {@code Vector3} to add.
	 * @param value2
	 *        The second {@code Vector3} to add.
	 * @param result
	 *        The result of the vector addition.
	 */
	public static void add(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x + value2.x;
		result.y = value1.y + value2.y;
		result.z = value1.z + value2.z;
	}

	/**
	 * Creates a new {@code Vector3} that contains the cartesian coordinates of a vector specified in barycentric coordinates and relative to 3d-triangle.
	 * 
	 * @param value1
	 *        The first {@code Vector3} of 3d-triangle.
	 * @param value2
	 *        The second {@code Vector3} of 3d-triangle.
	 * @param value3
	 *        The third {@code Vector3} of 3d-triangle.
	 * @param amount1
	 *        Barycentric scalar {@code b2} which represents a weighting factor towards second {@code Vector3} of 3d-triangle.
	 * @param amount2
	 *        Barycentric scalar {@code b3} which represents a weighting factor towards third {@code Vector3} of 3d-triangle.
	 * @return The cartesian translation of barycentric coordinates.
	 */
	public static Vector3 barycentric(Vector3 value1, Vector3 value2, Vector3 value3, float amount1, float amount2)
	{
		return new Vector3(
				MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
				MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2),
				MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2));
	}

	/**
	 * Creates a new {@code Vector3} that contains the cartesian coordinates of a vector specified in barycentric coordinates and relative to 3d-triangle.
	 * 
	 * @param value1
	 *        The first {@code Vector3} of 3d-triangle.
	 * @param value2
	 *        The second {@code Vector3} of 3d-triangle.
	 * @param value3
	 *        The third {@code Vector3} of 3d-triangle.
	 * @param amount1
	 *        Barycentric scalar {@code b2} which represents a weighting factor towards second {@code Vector3} of 3d-triangle.
	 * @param amount2
	 *        Barycentric scalar {@code b3} which represents a weighting factor towards third {@code Vector3} of 3d-triangle.
	 * @param result
	 *        The cartesian translation of barycentric coordinates as an output parameter.
	 */
	public static void barycentric(final Vector3 value1, final Vector3 value2, final Vector3 value3, float amount1,
			float amount2, Vector3 result)
	{
		result.x = MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2);
		result.y = MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2);
		result.z = MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2);
	}

	/**
	 * Creates a new {@code Vector3} that contains CatmullRom interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3} in interpolation.
	 * @param value2
	 *        The second {@code Vector3} in interpolation.
	 * @param value3
	 *        The third {@code Vector3} in interpolation.
	 * @param value4
	 *        The fourth {@code Vector3} in interpolation.
	 * @param amount
	 *        Weighting factor.
	 * @return The result of CatmullRom interpolation.
	 */
	public static Vector3 catmullRom(Vector3 value1, Vector3 value2, Vector3 value3, Vector3 value4, float amount)
	{
		return new Vector3(MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
				MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount), MathHelper.catmullRom(value1.z,
						value2.z, value3.z, value4.z, amount));
	}

	/**
	 * Creates a new {@code Vector3} that contains CatmullRom interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3} in interpolation.
	 * @param value2
	 *        The second {@code Vector3} in interpolation.
	 * @param value3
	 *        The third {@code Vector3} in interpolation.
	 * @param value4
	 *        The fourth {@code Vector3} in interpolation.
	 * @param amount
	 *        Weighting factor.
	 * @param result The result of CatmullRom interpolation as an output parameter.
	 */
	public static void catmullRom(final Vector3 value1, final Vector3 value2, final Vector3 value3,
			final Vector3 value4, float amount, Vector3 result)
	{
		result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
		result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
		result.z = MathHelper.catmullRom(value1.z, value2.z, value3.z, value4.z, amount);
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
	 * @return The clamped value.
	 */
	public static Vector3 clamp(Vector3 value1, Vector3 min, Vector3 max)
	{
		return new Vector3(MathHelper.clamp(value1.x, min.x, max.x), MathHelper.clamp(value1.y, min.y, max.y),
				MathHelper.clamp(value1.z, min.z, max.z));
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
	public static void clamp(final Vector3 value1, final Vector3 min, final Vector3 max, Vector3 result)
	{
		result.x = MathHelper.clamp(value1.x, min.x, max.x);
		result.y = MathHelper.clamp(value1.y, min.y, max.y);
		result.z = MathHelper.clamp(value1.z, min.z, max.z);
	}

	/**
	 * Computes the cross product of two vectors.
	 * 
	 * @param vector1
	 *        The first {@code Vector3}.
	 * @param vector2
	 *        The second {@code Vector3}.
	 * @return The cross product of two vectors.
	 */
	public static Vector3 cross(Vector3 vector1, Vector3 vector2)
	{
		cross(vector1, vector2, vector1);
		return vector1;
	}

	/**
	 * Computes the cross product of two vectors.
	 * 
	 * @param vector1
	 *        The first {@code Vector3}.
	 * @param vector2
	 *        The second {@code Vector3}.
	 * @param result
	 *        The cross product of two vectors as an output parameter.
	 */
	public static void cross(final Vector3 vector1, final Vector3 vector2, Vector3 result)
	{
		float x = vector1.y * vector2.z - vector2.y * vector1.z;
		float y = -(vector1.x * vector2.z - vector2.x * vector1.z);
		float z = vector1.x * vector2.y - vector2.x * vector1.y;
		result.x = x;
		result.y = y;
		result.z = z;
	}

	/**
	 * Returns the distance between two vectors.
	 * @param vector1
	 *        The first {@code Vector3}.
	 * @param vector2
	 *        The second {@code Vector3}.
	 * @return The distance between two vectors.
	 */
	public static float distance(Vector3 vector1, Vector3 vector2)
	{
		float result = distanceSquared(vector1, vector2);
		return (float) Math.sqrt(result);
	}

	// Original code with out parameters on primitive date types. This could be emulated in Java
	// using a Wrapper class aroud the primitive
	// see : http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
	/// <summary>
    /// Returns the distance between two vectors.
    /// </summary>
    /// <param name="value1">The first {@code Vector3}.</param>
    /// <param name="value2">The second {@code Vector3}.</param>
    /// <param name="result">The distance between two vectors as an output parameter.</param>
	// public static void distance(final Vector3 value1, final Vector3 value2, float result)
	// {
	// distanceSquared(value1, value2, result);
	// result = (float)Math.sqrt(result);
	// }

	/**
	 * Returns the squared distance between two vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @return The squared distance between two vectors.
	 */
	public static float distanceSquared(final Vector3 value1, final Vector3 value2)
	{
		return (value1.x - value2.x) * (value1.x - value2.x) +
			   (value1.y - value2.y) * (value1.y - value2.y) +
			   (value1.z - value2.z) * (value1.z - value2.z);
	}

	/// <summary>
    /// Returns the squared distance between two vectors.
    /// </summary>
    /// <param name="value1">The first {@code Vector3}.</param>
    /// <param name="value2">The second {@code Vector3}.</param>
    /// <param name="result">The squared distance between two vectors as an output parameter.</param>
	// public static void distanceSquared(final Vector3 value1, final Vector3 value2, float
	// result)
	// {
	// result = (value1.x - value2.x) * (value1.x - value2.x) +
	// (value1.y - value2.y) * (value1.y - value2.y) +
	// (value1.z - value2.z) * (value1.z - value2.z);
	// }

	/**
	 * Divides the components of a {@code Vector3} by the components of another {@code Vector3}.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Divisor {@code Vector3}.
	 * @return The result of dividing the vectors.
	 */
	public static Vector3 divide(Vector3 value1, Vector3 value2)
	{
		Vector3 result = new Vector3(value1);
		result.x /= value2.x;
		result.y /= value2.y;
		result.z /= value2.z;
		return result;
	}

	/**
	 * Divides the components of a {@code Vector3} by a scalar.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param divisor
	 *        Divisor scalar.
	 * @return The result of dividing a vector by a scalar.
	 */
	public static Vector3 divide(Vector3 value1, float divisor)
	{
		float factor = 1 / divisor;
		Vector3 result = new Vector3(value1);
		result.x *= factor;
		result.y *= factor;
		result.z *= factor;
		return result;
	}

	/**
	 * Divides the components of a {@code Vector3} by a scalar.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param divisor
	 *        Divisor scalar.
	 * @param result
	 *        The result of dividing a vector by a scalar as an output parameter.
	 */
	public static void divide(final Vector3 value1, float divisor, Vector3 result)
	{
		float factor = 1 / divisor;
		result.x = value1.x * factor;
		result.y = value1.y * factor;
		result.z = value1.z * factor;
	}

	/**
	 * Divides the components of a {@code Vector3} by the components of another {@code Vector3}.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Divisor {@code Vector3}.
	 * @param result
	 *        The result of dividing the vectors as an output parameter.
	 */
	public static void divide(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x / value2.x;
		result.y = value1.y / value2.y;
		result.z = value1.z / value2.z;
	}

	/**
	 * Returns a dot product of two vectors.
	 * 
	 * @param vector1
	 *        The first {@code Vector3}.
	 * @param vector2
	 *        The second {@code Vector3}.
	 * @return The dot product of two vectors.
	 */
	public static float dot(Vector3 vector1, Vector3 vector2)
	{
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	}

	/// <summary>
    /// Returns a dot product of two vectors.
    /// </summary>
    /// <param name="value1">The first {@code Vector3}.</param>
    /// <param name="value2">The second {@code Vector3}.</param>
    /// <param name="result">The dot product of two vectors as an output parameter.</param>
	// public static void dot(final Vector3 vector1, final Vector3 vector2, float result)
	// {
	// result = vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	// }

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
		return this.equals((Vector3) obj);
	}

	// TODO: Should I make this method private so we always use the other one ?
	/**
	 * Compares whether current instance is equal to specified {@code Vector3}.
	 * 
	 * @param other
	 *        The {@code Vector3} to compare.
	 * @return {@code true} if the instances are equal; {@code false} otherwise.
	 */
	public boolean equals(Vector3 other)
	{
		return this.x == other.x &&
			   this.y == other.y &&
			   this.z == other.z;
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
	 * Returns the hash code of this {@code Vector3}.
	 */
	@Override
	public int hashCode()
	{
		return (int) (this.x + this.y + this.z);
	}

	/**
	 * Creates a new {@code Vector3} that contains hermite spline interpolation.
	 * 
	 * @param value1
	 *        The first position {@code Vector3}.
	 * @param tangent1
	 *        The first tangent {@code Vector3}.
	 * @param value2
	 *        The second position {@code Vector3}.
	 * @param tangent2
	 *        The second tangent {@code Vector3}.
	 * @param amount
	 *        Weighting factor.
	 * @return The hermite spline interpolation vector.
	 */
	public static Vector3 hermite(Vector3 value1, Vector3 tangent1, Vector3 value2, Vector3 tangent2, float amount)
	{
		return new Vector3(MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount),
						   MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount),
						   MathHelper.hermite(value1.z, tangent1.z, value2.z, tangent2.z, amount));
	}

	/**
	 * Creates a new {@code Vector3} that contains hermite spline interpolation.
	 * 
	 * @param value1
	 *        The first position {@code Vector3}.
	 * @param tangent1
	 *        The first tangent {@code Vector3}.
	 * @param value2
	 *        The second position {@code Vector3}.
	 * @param tangent2
	 *        The second tangent {@code Vector3}.
	 * @param amount
	 *        Weighting factor.
	 * @param result
	 *        The hermite spline interpolation vector as an output parameter.
	 */
	public static void hermite(final Vector3 value1, final Vector3 tangent1, final Vector3 value2,
			final Vector3 tangent2, float amount, Vector3 result)
	{
		result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
		result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
		result.z = MathHelper.hermite(value1.z, tangent1.z, value2.z, tangent2.z, amount);
	}

	/**
	 * Returns the length of this {@code Vector3}.
	 * 
	 * @return The length of this {@code Vector3}.
	 */
	public float length()
	{
		float result = distanceSquared(this, zero);
		return (float) Math.sqrt(result);
	}

	/**
	 * Returns the squared length of this {@code Vector3}.
	 * 
	 * @return The squared length of this {@code Vector3}.
	 */
	public float lengthSquared()
	{
		return distanceSquared(this, zero);
	}

	/**
	 * Creates a new {@code Vector3} that contains linear interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @param amount
	 *        Weighting value(between 0.0 and 1.0).
	 * @return The result of linear interpolation of the specified vectors.
	 */
	public static Vector3 lerp(Vector3 value1, Vector3 value2, float amount)
	{
		return new Vector3(
				MathHelper.lerp(value1.x, value2.x, amount),
				MathHelper.lerp(value1.y, value2.y, amount),
				MathHelper.lerp(value1.z, value2.z, amount));
	}

	/**
	 * Creates a new {@code Vector3} that contains linear interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @param amount
	 *        Weighting value(between 0.0 and 1.0).
	 * @param result
	 *        The result of linear interpolation of the specified vectors as an output parameter.
	 */
	public static void lerp(final Vector3 value1, final Vector3 value2, float amount, Vector3 result)
	{
		result.x = MathHelper.lerp(value1.x, value2.x, amount);
		result.y = MathHelper.lerp(value1.y, value2.y, amount);
		result.z = MathHelper.lerp(value1.z, value2.z, amount);
	}

	/**
	 * Creates a new {@code Vector3} that contains a maximal values from the two vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @return The {@code Vector3} with maximal values from the two vectors.
	 */
	public static Vector3 max(Vector3 value1, Vector3 value2)
	{
		return new Vector3(MathHelper.max(value1.x, value2.x), MathHelper.max(value1.y, value2.y), MathHelper.max(
				value1.z, value2.z));
	}

	/**
	 * Creates a new {@code Vector3} that contains a maximal values from the two vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @param result
	 *        The {@code Vector3} with maximal values from the two vectors as an output parameter.
	 */
	public static void max(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = MathHelper.max(value1.x, value2.x);
		result.y = MathHelper.max(value1.y, value2.y);
		result.z = MathHelper.max(value1.z, value2.z);
	}

	/**
	 * Creates a new {@code Vector3} that contains a minimal values from the two vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @return The {@code Vector3} with minimal values from the two vectors.
	 */
	public static Vector3 min(Vector3 value1, Vector3 value2)
	{
		return new Vector3(
				MathHelper.min(value1.x, value2.x),
				MathHelper.min(value1.y, value2.y),
				MathHelper.min(value1.z, value2.z));
	}

	/**
	 * Creates a new {@code Vector3} that contains a minimal values from the two vectors.
	 * 
	 * @param value1
	 *        The first {@code Vector3}.
	 * @param value2
	 *        The second {@code Vector3}.
	 * @param result
	 *        The {@code Vector3} with minimal values from the two vectors as an output parameter.
	 */
	public static void min(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = MathHelper.min(value1.x, value2.x);
		result.y = MathHelper.min(value1.y, value2.y);
		result.z = MathHelper.min(value1.z, value2.z);
	}

	/**
	 * Creates a new {@code Vector3} that contains a multiplication of two vectors.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Source {@code Vector3}.
	 * @return The result of the vector multiplication.
	 */
	public static Vector3 multiply(Vector3 value1, Vector3 value2)
	{
		Vector3 result = new Vector3(value1);
		result.x *= value2.x;
		result.y *= value2.y;
		result.z *= value2.z;
		return result;
	}

	/**
	 * Creates a new {@code Vector3} that contains a multiplication of {@code Vector3} and a scalar.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param scaleFactor
	 *        Scalar value.
	 * @return The result of the vector multiplication with a scalar.
	 */
	public static Vector3 multiply(Vector3 value1, float scaleFactor)
	{
		Vector3 result = new Vector3(value1);
		result.x *= scaleFactor;
		result.y *= scaleFactor;
		result.z *= scaleFactor;
		return result;
	}

	/**
	 * Creates a new {@code Vector3} that contains a multiplication of {@code Vector3} and a scalar.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param scaleFactor
	 *        Scalar value.
	 * @param result
	 *        The result of the multiplication with a scalar as an output parameter.
	 */
	public static void multiply(final Vector3 value1, float scaleFactor, Vector3 result)
	{
		result.x = value1.x * scaleFactor;
		result.y = value1.y * scaleFactor;
		result.z = value1.z * scaleFactor;
	}

	/**
	 * Creates a new {@code Vector3} that contains a multiplication of two vectors.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Source {@code Vector3}.
	 * @param result
	 *        The result of the vector multiplication as an output parameter.
	 */
	public static void multiply(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x * value2.x;
		result.y = value1.y * value2.y;
		result.z = value1.z * value2.z;
	}

	/**
	 * Returns a {@code Vector3} pointing in the opposite direction of {@code value}.
	 * 
	 * @param value
	 *        The {@code Vector3} to negate.
	 * @return The vector negation of {@code value}.
	 */
	public static Vector3 negate(Vector3 value)
	{
		return new Vector3(-value.x, -value.y, -value.z);
	}

	/**
	 * Stores a {@code Vector3} pointing in the opposite direction of {@code value} in {@code result}.
	 * 
	 * @param value
	 *        The vector to negate.
	 * @param result
	 *        The vector that the negation of {@code value} will be stored in.
	 */
	public static void negate(final Vector3 value, Vector3 result)
	{
		result.x = -value.x;
		result.y = -value.y;
		result.z = -value.z;
	}

	/**
	 * Turns this {@code Vector3} to a unit vector with the same direction.
	 */
	public void normalize()
	{
		normalize(this, this);
	}

	/**
	 * Creates a new {@code Vector3} that contains a normalized values from another {@code Vector3}.
	 * 
	 * @param vector
	 *        Source {@code Vector3}.
	 * @return Unit vector.
	 */
	public static Vector3 normalize(Vector3 vector)
	{
		float val = 1.0f / (float)Math.sqrt((vector.x * vector.x) + (vector.y * vector.y) + (vector.z * vector.z));
		return new Vector3(vector.x * val, vector.y * val, vector.z * val);
	}

	/**
	 * Creates a new {@code Vector3} that contains a normalized values from another {@code Vector3}.
	 * 
	 * @param value
	 *        Source {@code Vector3}.
	 * @param result
	 *        Unit vector as an output parameter.
	 */
	public static void normalize(final Vector3 value, Vector3 result)
	{
		float factor = distance(value, zero);
		factor = 1f / factor;
		result.x = value.x * factor;
		result.y = value.y * factor;
		result.z = value.z * factor;
	}

	/**
	 * Creates a new {@code Vector3} that contains reflect vector of the given vector and normal.
	 * 
	 * @param vector
	 *        Source {@code Vector3}.
	 * @param normal
	 *        Reflection normal.
	 * @return Reflected vector.
	 */
	public static Vector3 reflect(Vector3 vector, Vector3 normal)
	{
		// I is the original array
		// N is the normal of the incident plane
		// R = I - (2 * N * ( DotProduct[ I,N] ))
		Vector3 reflectedVector = new Vector3(1f);
		// inline the dotProduct here instead of calling method
		float dotProduct = ((vector.x * normal.x) + (vector.y * normal.y)) + (vector.z * normal.z);
		reflectedVector.x = vector.x - (2.0f * normal.x) * dotProduct;
		reflectedVector.y = vector.y - (2.0f * normal.y) * dotProduct;
		reflectedVector.z = vector.z - (2.0f * normal.z) * dotProduct;

		return reflectedVector;
	}

	/**
	 * Creates a new {@code Vector3} that contains reflect vector of the given vector and normal.
	 * 
	 * @param vector
	 *        Source {@code Vector3}.
	 * @param normal
	 *        Reflection normal.
	 * @param result
	 *        Reflected vector as an output parameter.
	 */
	public static void reflect(final Vector3 vector, final Vector3 normal, Vector3 result)
	{
		// I is the original array
		// N is the normal of the incident plane
		// R = I - (2 * N * ( DotProduct[ I,N] ))

		// inline the dotProduct here instead of calling method
		float dotProduct = ((vector.x * normal.x) + (vector.y * normal.y)) + (vector.z * normal.z);
		result.x = vector.x - (2.0f * normal.x) * dotProduct;
		result.y = vector.y - (2.0f * normal.y) * dotProduct;
		result.z = vector.z - (2.0f * normal.z) * dotProduct;
	}

	/**
	 * Creates a new {@code Vector3} that contains cubic interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Source {@code Vector3}.
	 * @param amount
	 *        Weighting value.
	 * @return Cubic interpolation of the specified vectors.
	 */
	public static Vector3 smoothStep(Vector3 value1, Vector3 value2, float amount)
	{
		return new Vector3(MathHelper.smoothStep(value1.x, value2.x, amount), MathHelper.smoothStep(value1.y,
				value2.y, amount), MathHelper.smoothStep(value1.z, value2.z, amount));
	}

	/**
	 * Creates a new {@code Vector3} that contains cubic interpolation of the specified vectors.
	 * 
	 * @param value1
	 *        Source {@code Vector3}.
	 * @param value2
	 *        Source {@code Vector3}.
	 * @param amount
	 *        Weighting value.
	 * @param result
	 *        Cubic interpolation of the specified vectors as an output parameter.
	 */
	public static void smoothStep(final Vector3 value1, final Vector3 value2, float amount, Vector3 result)
	{
		result.x = MathHelper.smoothStep(value1.x, value2.x, amount);
		result.y = MathHelper.smoothStep(value1.y, value2.y, amount);
		result.z = MathHelper.smoothStep(value1.z, value2.z, amount);
	}

	/**
	 * Performs vector subtraction on {@code value1} and {@code value2}.
	 * 
	 * @param value1
	 *        The vector to be subtracted from.
	 * @param value2
	 *        The vector to be subtracted from {@code value1}.
	 * @return The result of the vector subtraction.
	 */
	public static Vector3 subtract(Vector3 value1, Vector3 value2)
	{
		Vector3 result = new Vector3(value1);
		result.x -= value2.x;
		result.y -= value2.y;
		result.z -= value2.z;
		return result;
	}

	/**
	 * Performs vector subtraction on {@code value1} and {@code value2}.
	 * 
	 * @param value1
	 *        The {@code Vector3} to be subtracted from.
	 * @param value2
	 *        The {@code Vector3} to be subtracted from {@code value1}.
	 * @param result
	 *        The result of the vector subtraction.
	 */
	public static void subtract(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x - value2.x;
		result.y = value1.y - value2.y;
		result.z = value1.z - value2.z;
	}

	/**
	 * Returns a {@link String} representation of this {@code Vector3} in the format:
	 * {X:{@link #x} Y:{@link #y} Z:{@link #z}}
	 * 
	 * @return A {@link String} representation of this {@code Vector3}.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(32);
		sb.append("{X:");
		sb.append(this.x);
		sb.append(" Y:");
		sb.append(this.y);
		sb.append(" Z:");
		sb.append(this.z);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Creates a new {@code Vector3} that contains a transformation of 3d-vector by the specified {@link Matrix}.
	 * 
	 * @param position
	 *        Source {@code Vector3}.
	 * @param matrix
	 *        The transformation {@link Matrix}.
	 * @return Transformed {@code Vector3}.
	 */
	public static Vector3 transform(Vector3 position, Matrix matrix)
	{
		transform(position, matrix, position);
		return position;
	}

	/**
	 * Creates a new {@code Vector3} that contains a transformation of 3d-vector by the specified {@link Matrix}.
	 * 
	 * @param position
	 *        Source {@code Vector3}.
	 * @param matrix
	 *        The transformation {@link Matrix}.
	 * @param result
	 *        Transformed {@code Vector3} as an output parameter.
	 */
	public static void transform(final Vector3 position, final Matrix matrix, Vector3 result)
	{
		float x = (position.x * matrix.M11) + (position.y * matrix.M21) + (position.z * matrix.M31) + matrix.M41;
		float y = (position.x * matrix.M12) + (position.y * matrix.M22) + (position.z * matrix.M32) + matrix.M42;
		float z = (position.x * matrix.M13) + (position.y * matrix.M23) + (position.z * matrix.M33) + matrix.M43;
		result.x = x;
		result.y = y;
		result.z = z;
	}

	/**
	 * Creates a new {@code Vector3} that contains a transformation of 3d-vector by the specified {@link Quaternion}, representing the rotation.
	 * 
	 * @param value
	 *        Source {@code Vector3}.
	 * @param rotation
	 *        The {@link Quaternion} which contains rotation transformation.
	 * @return Transformed {@code Vector3}.
	 */
    public static Vector3 transform(Vector3 value, Quaternion rotation)
    {
        Vector3 result = null;
        transform(value, rotation, result);
        return result;
    }

    /**
     * Creates a new {@code Vector3} that contains a transformation of 3d-vector by the specified {@link Quaternion}, representing the rotation.
     * 
     * @param value
     *        Source {@code Vector3}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param result
     *        Transformed {@code Vector3} as an output parameter.
     */
    public static void transform(final Vector3 value, final Quaternion rotation, Vector3 result)
    {
        float x = 2 * (rotation.y * value.z - rotation.z * value.y);
        float y = 2 * (rotation.z * value.x - rotation.x * value.z);
        float z = 2 * (rotation.x * value.y - rotation.y * value.x);

        result.x = value.x + x * rotation.w + (rotation.y * z - rotation.z * y);
        result.y = value.y + y * rotation.w + (rotation.z * x - rotation.x * z);
        result.z = value.z + z * rotation.w + (rotation.x * y - rotation.y * x);
    }

    /**
     * Apply transformation on vectors within array of {@code Vector3} by the specified {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param sourceIndex
     *        The starting index of transformation in the source array.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param destinationArray
     *        Destination array.
     * @param destinationIndex
     *        The starting index in the destination array, where the first {@code Vector3} should be written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector3[] sourceArray, int sourceIndex, final Matrix matrix, Vector3[] destinationArray, int destinationIndex, int length)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (sourceArray.length < sourceIndex + length)
            throw new IllegalArgumentException("Source array length is lesser than sourceIndex + length");
        if (destinationArray.length < destinationIndex + length)
            throw new IllegalArgumentException("Destination array length is lesser than destinationIndex + length");

        // TODO: Are there options on some platforms to implement a vectorized version of this?

        for (int i = 0; i < length; ++i)
        {
            Vector3 position = sourceArray[sourceIndex + i];
            destinationArray[destinationIndex + i] =
                new Vector3(
                    (position.x * matrix.M11) + (position.y * matrix.M21) + (position.z * matrix.M31) + matrix.M41,
                    (position.x * matrix.M12) + (position.y * matrix.M22) + (position.z * matrix.M32) + matrix.M42,
                    (position.x * matrix.M13) + (position.y * matrix.M23) + (position.z * matrix.M33) + matrix.M43);
        }
    }

    /**
     * Apply transformation on vectors within array of {@code Vector3} by the specified {@link Quaternion} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param sourceIndex
     *        The starting index of transformation in the source array.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param destinationArray
     *        Destination array.
     * @param destinationIndex
     *        The starting index in the destination array, where the first {@code Vector3} should be written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector3[] sourceArray, int sourceIndex, final Quaternion rotation, Vector3[] destinationArray, int destinationIndex, int length)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (sourceArray.length < sourceIndex + length)
            throw new IllegalArgumentException("Source array length is lesser than sourceIndex + length");
        if (destinationArray.length < destinationIndex + length)
            throw new IllegalArgumentException("Destination array length is lesser than destinationIndex + length");

        // TODO: Are there options on some platforms to implement a vectorized version of this?

        for (int i = 0; i < length; ++i)
        {
            Vector3 position = sourceArray[sourceIndex + i];

            float x = 2 * (rotation.y * position.z - rotation.z * position.y);
            float y = 2 * (rotation.z * position.x - rotation.x * position.z);
            float z = 2 * (rotation.x * position.y - rotation.y * position.x);

            destinationArray[destinationIndex + i] =
                new Vector3(
                    position.x + x * rotation.w + (rotation.y * z - rotation.z * y),
                    position.y + y * rotation.w + (rotation.z * x - rotation.x * z),
                    position.z + z * rotation.w+ (rotation.x * y - rotation.y * x));
        }
    }

    /**
     * Apply transformation on all vectors within array of {@code Vector3} by the specified {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector3[] sourceArray, final Matrix matrix, Vector3[] destinationArray)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        // TODO: Are there options on some platforms to implement a vectorized version of this?

        for (int i = 0; i < sourceArray.length; ++i)
        {
            Vector3 position = sourceArray[i];                
            destinationArray[i] =
                new Vector3(
                    (position.x*matrix.M11) + (position.y*matrix.M21) + (position.z*matrix.M31) + matrix.M41,
                    (position.x*matrix.M12) + (position.y*matrix.M22) + (position.z*matrix.M32) + matrix.M42,
                    (position.x*matrix.M13) + (position.y*matrix.M23) + (position.z*matrix.M33) + matrix.M43);
        }
    }

    /**
     * Apply transformation on all vectors within array of {@code Vector3} by the specified {@link Quaternion} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector3[] sourceArray, final Quaternion rotation, Vector3[] destinationArray)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        // TODO: Are there options on some platforms to implement a vectorized version of this?

        for (int i = 0; i < sourceArray.length; ++i)
        {
            Vector3 position = sourceArray[i];

            float x = 2 * (rotation.y * position.z - rotation.z * position.y);
            float y = 2 * (rotation.z * position.x - rotation.x * position.z);
            float z = 2 * (rotation.x * position.y - rotation.y * position.x);

            destinationArray[i] =
                new Vector3(
                    position.x + x * rotation.w + (rotation.y * z - rotation.z * y),
                    position.y + y * rotation.w + (rotation.z * x - rotation.x * z),
                    position.z + z * rotation.w + (rotation.x * y - rotation.y * x));
        }
    }

    /**
     * Creates a new {@code Vector3} that contains a transformation of the specified normal by the specified {@link Matrix}.
     * 
     * @param normal
     *        Source {@code Vector3} which represents a normal vector.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @return Transformed normal.
     */
	public static Vector3 transformNormal(Vector3 normal, Matrix matrix)
	{
		transformNormal(normal, matrix, normal);
		return normal;
	}

	/**
	 * Creates a new {@code Vector3} that contains a transformation of the specified normal by the specified {@link Matrix}.
	 * 
	 * @param normal
	 *        Source {@code Vector3} which represents a normal vector.
	 * @param matrix
	 *        The transformation {@link Matrix}.
	 * @param result
	 *        Transformed normal as an output parameter.
	 */
	public static void transformNormal(final Vector3 normal, final Matrix matrix, Vector3 result)
	{
		float x = (normal.x * matrix.M11) + (normal.y * matrix.M21) + (normal.z * matrix.M31);
		float y = (normal.x * matrix.M12) + (normal.y * matrix.M22) + (normal.z * matrix.M32);
		float z = (normal.x * matrix.M13) + (normal.y * matrix.M23) + (normal.z * matrix.M33);
		result.x = x;
		result.y = y;
		result.z = z;
	}

	/**
	 * Apply transformation on normals within array of {@code Vector3} by the specified {@link Matrix} and places the results in an another array.
	 * 
	 * @param sourceArray
	 *       Source array.
	 * @param sourceIndex
	 *        The starting index of transformation in the source array.
	 * @param matrix
	 *        The transformation {@link Matrix}.
	 * @param destinationArray
	 *        Destination array.
	 * @param destinationIndex
	 *        The starting index in the destination array, where the first {@code Vector3} should be written.
	 * @param length
	 *        The number of normals to be transformed.
	 */
    public static void transformNormal(Vector3[] sourceArray,
     int sourceIndex,
     final Matrix matrix,
     Vector3[] destinationArray,
     int destinationIndex,
     int length)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if(sourceArray.length < sourceIndex + length)
            throw new IllegalArgumentException("Source array length is lesser than sourceIndex + length");
        if (destinationArray.length < destinationIndex + length)
            throw new IllegalArgumentException("Destination array length is lesser than destinationIndex + length");

        for (int x = 0; x < length; x++)
        {
            Vector3 normal = sourceArray[sourceIndex + x];

            destinationArray[destinationIndex + x] =
                 new Vector3(
                    (normal.x * matrix.M11) + (normal.y * matrix.M21) + (normal.z * matrix.M31),
                    (normal.x * matrix.M12) + (normal.y * matrix.M22) + (normal.z * matrix.M32),
                    (normal.x * matrix.M13) + (normal.y * matrix.M23) + (normal.z * matrix.M33));
        }
    }

    /**
     * Apply transformation on all normals within array of {@code Vector3} by the specified {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param destinationArray
     *        Destination array.
     */
    public static void transformNormal(Vector3[] sourceArray, final Matrix matrix, Vector3[] destinationArray)
    {
        if(sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        for (int i = 0; i < sourceArray.length; i++)
        {
        	Vector3 normal = sourceArray[i];

            destinationArray[i] =
                new Vector3(
                    (normal.x*matrix.M11) + (normal.y*matrix.M21) + (normal.z*matrix.M31),
                    (normal.x*matrix.M12) + (normal.y*matrix.M22) + (normal.z*matrix.M32),
                    (normal.x*matrix.M13) + (normal.y*matrix.M23) + (normal.z*matrix.M33));
        }
    }

	// TODO: Turn some of those operators into methods like equals and not equals
	/// <summary>
    /// Compares whether two {@code Vector3} instances are equal.
    /// </summary>
    /// <param name="value1">{@code Vector3} instance on the left of the equal sign.</param>
    /// <param name="value2">{@code Vector3} instance on the right of the equal sign.</param>
    /// <returns><c>true</c> if the instances are equal; <c>false</c> otherwise.</returns>
//    public static bool operator ==(Vector3 value1, Vector3 value2)
//    {
//        return value1.X == value2.X
//            && value1.Y == value2.Y
//            && value1.Z == value2.Z;
//    }

    /// <summary>
    /// Compares whether two {@code Vector3} instances are not equal.
    /// </summary>
    /// <param name="value1">{@code Vector3} instance on the left of the not equal sign.</param>
    /// <param name="value2">{@code Vector3} instance on the right of the not equal sign.</param>
    /// <returns><c>true</c> if the instances are not equal; <c>false</c> otherwise.</returns>	
//    public static bool operator !=(Vector3 value1, Vector3 value2)
//    {
//        return !(value1 == value2);
//    }

    /**
     * Adds the supplied {@code Vector3} to this {@code Vector3}.
     * 
     * @param other
     *        The other {@code Vector3} to add to this {@code Vector3}.
     * @return The result of the addition applied to this vector.
     */
    public Vector3 add(Vector3 other)
	{
    	this.x += other.x;
    	this.y += other.y;
    	this.z += other.z;
    	return this;
    }

    /// <summary>
    /// Inverts values in the specified {@code Vector3}.
    /// </summary>
    /// <param name="value">Source {@code Vector3} on the right of the sub sign.</param>
    /// <returns>Result of the inversion.</returns>
//    public static Vector3 operator -(Vector3 value)
//    {
//        value = new Vector3(-value.X, -value.Y, -value.Z);
//        return value;
//    }

    /**
     * Subtracts the supplied {@code Vector3} to this {@code Vector3}.
     * 
     * @param other
     *        The other {@code Vector3} to subtract to this vector.
     * @return The result of the subtraction applied to this vector.
     */
    public Vector3 subtract(Vector3 other)
    {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    /**
     * Multiplies the components of this {@code Vector3} by the components of the supplied {@code Vector3}.
     * 
     * @param other
     *        The other {@code Vector3} to multiply to this vector.
     * @return The result of the multiplication applied to this vector.
     */
	public Vector3 multiply(Vector3 other)
	{
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
		return this;
	}

	/**
	 * Multiplies the components of this {@code Vector3} by a scalar.
	 * 
	 * @param scaleFactor
	 *        Scalar value used to multiply each component of this {@code Vector3}.
	 * @return The result of the vector multiplication with a scalar.
	 */
	public Vector3 multiply(float scaleFactor)
	{
		this.x *= scaleFactor;
		this.y *= scaleFactor;
		this.z *= scaleFactor;
		return this;
	}

    /// <summary>
    /// Multiplies the components of vector by a scalar.
    /// </summary>
    /// <param name="scaleFactor">Scalar value on the left of the mul sign.</param>
    /// <param name="value">Source {@code Vector3} on the right of the mul sign.</param>
    /// <returns>Result of the vector multiplication with a scalar.</returns>
//    public static Vector3 operator *(float scaleFactor, Vector3 value)
//    {
//        value.X *= scaleFactor;
//        value.Y *= scaleFactor;
//        value.Z *= scaleFactor;
//        return value;
//    }

	 /**
     * Divides the components of this {@code Vector3} by the components of the supplied {@code Vector3}.
     * 
     * @param other
     *        The other {@code Vector3} to divide to this vector.
     * @return The result of the division applied to this vector.
     */
	public Vector3 divide(Vector3 other)
	{
		this.x /= other.x;
		this.y /= other.y;
		this.z /= other.z;
		return this;
	}

	/**
	 * Divides the components of this {@code Vector3} by a scalar.
	 * 
	 * @param divisor
	 *        Scalar value used to divide each component of this {@code Vector3}.
	 * @return The result of the vector division by a scalar.
	 */
    public Vector3 divide(float divisor)
    {
        float factor = 1 / divisor;
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }
}
