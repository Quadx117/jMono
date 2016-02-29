package gameCore.math;

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

	public float x;
	public float y;
	public float z;

	// / <summary>
	// / Returns a <see>Vector3</see> with components 0, 0, 0.
	// / </summary>
	public static Vector3 zero()
	{
		return new Vector3(zero);
	}

	// / <summary>
	// / Returns a <see>Vector3</see> with components 1, 1, 1.
	// / </summary>
	public static Vector3 one()
	{
		return new Vector3(one);
	}

	// / <summary>
	// / Returns a <see>Vector3</see> with components 1, 0, 0.
	// / </summary>
	public static Vector3 unitX()
	{
		return new Vector3(unitX);
	}

	// / <summary>
	// / Returns a <see>Vector3</see> with components 0, 1, 0.
	// / </summary>
	public static Vector3 unitY()
	{
		return new Vector3(unitY);
	}

	// / <summary>
	// / Returns a <see>Vector3</see> with components 0, 0, 1.
	// / </summary>
	public static Vector3 unitZ()
	{
		return new Vector3(unitZ);
	}

	public static Vector3 up()
	{
		return new Vector3(up);
	}

	public static Vector3 down()
	{
		return new Vector3(down);
	}

	public static Vector3 right()
	{
		return new Vector3(right);
	}

	public static Vector3 left()
	{
		return new Vector3(left);
	}

	public static Vector3 forward()
	{
		return new Vector3(forward);
	}

	public static Vector3 backward()
	{
		return new Vector3(backward);
	}

	// Note: Added this since it is provided by default for struct in C#
	public Vector3()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}

	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(float value)
	{
		this.x = value;
		this.y = value;
		this.z = value;
	}

	public Vector3(Vector3 value)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
	}

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
	 *        The first vector to add.
	 * @param value2
	 *        The second vector to add.
	 * @return The result of the vector addition.
	 */
	public static Vector3 add(Vector3 value1, Vector3 value2)
	{
		value1.x += value2.x;
		value1.y += value2.y;
		value1.z += value2.z;
		return value1;
	}

	/**
	 * Performs vector addition on {@code value1} and {@code value2}, storing the result of the
	 * addition in {@code result}.
	 * 
	 * @param value1
	 *        The first vector to add.
	 * @param value2
	 *        The second vector to add.
	 * @param result
	 *        The result of the vector addition.
	 */
	public static void add(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x + value2.x;
		result.y = value1.y + value2.y;
		result.z = value1.z + value2.z;
	}

	public static Vector3 barycentric(Vector3 value1, Vector3 value2, Vector3 value3, float amount1, float amount2)
	{
		return new Vector3(MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
				MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2), MathHelper.barycentric(
						value1.z, value2.z, value3.z, amount1, amount2));
	}

	public static void barycentric(final Vector3 value1, final Vector3 value2, final Vector3 value3, float amount1,
			float amount2, Vector3 result)
	{
		result.x = MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2);
		result.y = MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2);
		result.z = MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2);
	}

	public static Vector3 catmullRom(Vector3 value1, Vector3 value2, Vector3 value3, Vector3 value4, float amount)
	{
		return new Vector3(MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
				MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount), MathHelper.catmullRom(value1.z,
						value2.z, value3.z, value4.z, amount));
	}

	public static void catmullRom(final Vector3 value1, final Vector3 value2, final Vector3 value3,
			final Vector3 value4, float amount, Vector3 result)
	{
		result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
		result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
		result.z = MathHelper.catmullRom(value1.z, value2.z, value3.z, value4.z, amount);
	}

	public static Vector3 clamp(Vector3 value1, Vector3 min, Vector3 max)
	{
		return new Vector3(MathHelper.clamp(value1.x, min.x, max.x), MathHelper.clamp(value1.y, min.y, max.y),
				MathHelper.clamp(value1.z, min.z, max.z));
	}

	public static void clamp(final Vector3 value1, final Vector3 min, final Vector3 max, Vector3 result)
	{
		result.x = MathHelper.clamp(value1.x, min.x, max.x);
		result.y = MathHelper.clamp(value1.y, min.y, max.y);
		result.z = MathHelper.clamp(value1.z, min.z, max.z);
	}

	public static Vector3 cross(Vector3 vector1, Vector3 vector2)
	{
		cross(vector1, vector2, vector1);
		return vector1;
	}

	public static void cross(final Vector3 vector1, final Vector3 vector2, Vector3 result)
	{
		float x = vector1.y * vector2.z - vector2.y * vector1.z;
		float y = -(vector1.x * vector2.z - vector2.x * vector1.z);
		float z = vector1.x * vector2.y - vector2.x * vector1.y;
		result.x = x;
		result.y = y;
		result.z = z;
	}

	// Original code with out parameters on primitive date types. This could have been emulated in
	// Java
	// using a Wrapper class aroud the primitive
	// see :
	// http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
	// public static float distance(Vector3 vector1, Vector3 vector2)
	// {
	// float result;
	// distanceSquared(vector1, vector2, result);
	// return (float)Math.sqrt(result);
	// }
	//
	// public static void distance(final Vector3 value1, final Vector3 value2, float result)
	// {
	// distanceSquared(value1, value2, result);
	// result = (float)Math.sqrt(result);
	// }
	//
	// public static float distanceSquared(Vector3 value1, Vector3 value2)
	// {
	// float result;
	// distanceSquared(value1, value2, result);
	// return result;
	// }
	//
	// public static void distanceSquared(final Vector3 value1, final Vector3 value2, float
	// result)
	// {
	// result = (value1.x - value2.x) * (value1.x - value2.x) +
	// (value1.y - value2.y) * (value1.y - value2.y) +
	// (value1.z - value2.z) * (value1.z - value2.z);
	// }

	public static float distance(Vector3 vector1, Vector3 vector2)
	{
		float result = distanceSquared(vector1, vector2);
		return (float) Math.sqrt(result);
	}

	public static float distanceSquared(final Vector3 value1, final Vector3 value2)
	{
		return (value1.x - value2.x) * (value1.x - value2.x) + (value1.y - value2.y) * (value1.y - value2.y)
				+ (value1.z - value2.z) * (value1.z - value2.z);
	}

	public static Vector3 divide(Vector3 value1, Vector3 value2)
	{
		value1.x /= value2.x;
		value1.y /= value2.y;
		value1.z /= value2.z;
		return value1;
	}

	public static Vector3 divide(Vector3 value1, float value2)
	{
		float factor = 1 / value2;
		value1.x *= factor;
		value1.y *= factor;
		value1.z *= factor;
		return value1;
	}

	public static void divide(final Vector3 value1, float divisor, Vector3 result)
	{
		float factor = 1 / divisor;
		result.x = value1.x * factor;
		result.y = value1.y * factor;
		result.z = value1.z * factor;
	}

	public static void divide(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x / value2.x;
		result.y = value1.y / value2.y;
		result.z = value1.z / value2.z;
	}

	public static float dot(Vector3 vector1, Vector3 vector2)
	{
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	}

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
     * @see java.lang.Object#equals(Object)
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

	// Helper method
	private boolean equals(Vector3 other)
	{
		return this.x == other.x && this.y == other.y && this.z == other.z;
	}

	/**
	 * Indicates whether some other object is "not equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see java.lang.Object#equals(Object)
	 */
	public boolean notEquals(Object obj)
	{
		return !this.equals(obj);
	}
	
	@Override
	public int hashCode()
	{
		return (int) (this.x + this.y + this.z);
	}

	public static Vector3 hermite(Vector3 value1, Vector3 tangent1, Vector3 value2, Vector3 tangent2, float amount)
	{
		Vector3 result = new Vector3(1f);
		hermite(value1, tangent1, value2, tangent2, amount, result);
		return result;
	}

	public static void hermite(final Vector3 value1, final Vector3 tangent1, final Vector3 value2,
			final Vector3 tangent2, float amount, Vector3 result)
	{
		result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
		result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
		result.z = MathHelper.hermite(value1.z, tangent1.z, value2.z, tangent2.z, amount);
	}

	public float length()
	{
		float result = distanceSquared(this, zero);
		return (float) Math.sqrt(result);
	}

	public float lengthSquared()
	{
		float result = distanceSquared(this, zero);
		return result;
	}

	public static Vector3 Lerp(Vector3 value1, Vector3 value2, float amount)
	{
		return new Vector3(MathHelper.lerp(value1.x, value2.x, amount), MathHelper.lerp(value1.y, value2.y, amount),
				MathHelper.lerp(value1.z, value2.z, amount));
	}

	public static void lerp(final Vector3 value1, final Vector3 value2, float amount, Vector3 result)
	{
		result.x = MathHelper.lerp(value1.x, value2.x, amount);
		result.y = MathHelper.lerp(value1.y, value2.y, amount);
		result.z = MathHelper.lerp(value1.z, value2.z, amount);
	}

	public static Vector3 max(Vector3 value1, Vector3 value2)
	{
		return new Vector3(MathHelper.max(value1.x, value2.x), MathHelper.max(value1.y, value2.y), MathHelper.max(
				value1.z, value2.z));
	}

	public static void max(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = MathHelper.max(value1.x, value2.x);
		result.y = MathHelper.max(value1.y, value2.y);
		result.z = MathHelper.max(value1.z, value2.z);
	}

	public static Vector3 min(Vector3 value1, Vector3 value2)
	{
		return new Vector3(MathHelper.min(value1.x, value2.x), MathHelper.min(value1.y, value2.y), MathHelper.min(
				value1.z, value2.z));
	}

	public static void min(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = MathHelper.min(value1.x, value2.x);
		result.y = MathHelper.min(value1.y, value2.y);
		result.z = MathHelper.min(value1.z, value2.z);
	}

	public static Vector3 multiply(Vector3 value1, Vector3 value2)
	{
		value1.x *= value2.x;
		value1.y *= value2.y;
		value1.z *= value2.z;
		return value1;
	}

	public static Vector3 multiply(Vector3 value1, float scaleFactor)
	{
		value1.x *= scaleFactor;
		value1.y *= scaleFactor;
		value1.z *= scaleFactor;
		return value1;
	}

	public static void multiply(final Vector3 value1, float scaleFactor, Vector3 result)
	{
		result.x = value1.x * scaleFactor;
		result.y = value1.y * scaleFactor;
		result.z = value1.z * scaleFactor;
	}

	public static void multiply(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x * value2.x;
		result.y = value1.y * value2.y;
		result.z = value1.z * value2.z;
	}

	/**
	 * Returns a <see>Vector3</see> pointing in the opposite direction of {@code value}.
	 * 
	 * @param value
	 *        The vector to negate.
	 * @return The vector negation of <paramfinal name="value"/>.
	 */
	public static Vector3 negate(Vector3 value)
	{
		value = new Vector3(-value.x, -value.y, -value.z);
		return value;
	}

	/**
	 * Stores a <see>Vector3</see> pointing in the opposite direction of {@code value} in
	 * {@code result}.
	 * 
	 * @param value
	 *        The vector to negate.
	 * @param result
	 *        The vector that the negation of <paramfinal name="value"/> will be stored in.
	 */
	public static void negate(final Vector3 value, Vector3 result)
	{
		result.x = -value.x;
		result.y = -value.y;
		result.z = -value.z;
	}

	public void normalize()
	{
		normalize(this, this);
	}

	public static Vector3 normalize(Vector3 vector)
	{
		normalize(vector, vector);
		return vector;
	}

	public static void normalize(final Vector3 value, Vector3 result)
	{
		float factor = distance(value, zero);
		factor = 1f / factor;
		result.x = value.x * factor;
		result.y = value.y * factor;
		result.z = value.z * factor;
	}

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

	public static Vector3 smoothStep(Vector3 value1, Vector3 value2, float amount)
	{
		return new Vector3(MathHelper.smoothStep(value1.x, value2.x, amount), MathHelper.smoothStep(value1.y,
				value2.y, amount), MathHelper.smoothStep(value1.z, value2.z, amount));
	}

	public static void smoothStep(final Vector3 value1, final Vector3 value2, float amount, Vector3 result)
	{
		result.x = MathHelper.smoothStep(value1.x, value2.x, amount);
		result.y = MathHelper.smoothStep(value1.y, value2.y, amount);
		result.z = MathHelper.smoothStep(value1.z, value2.z, amount);
	}

	// / <summary>
	// / Performs vector subtraction on <paramfinal name="value1"/> and <paramfinal name="value2"/>.
	// / </summary>
	// / <param name="value1">The vector to be subtracted from.</param>
	// / <param name="value2">The vector to be subtracted from <paramfinal name="value1"/>.</param>
	// / <returns>The result of the vector subtraction.</returns>
	public static Vector3 subtract(Vector3 value1, Vector3 value2)
	{
		value1.x -= value2.x;
		value1.y -= value2.y;
		value1.z -= value2.z;
		return value1;
	}

	// / <summary>
	// / Performs vector subtraction on <paramfinal name="value1"/> and <paramfinal name="value2"/>.
	// / </summary>
	// / <param name="value1">The vector to be subtracted from.</param>
	// / <param name="value2">The vector to be subtracted from <paramfinal name="value1"/>.</param>
	// / <param name="result">The result of the vector subtraction.</param>
	public static void subtract(final Vector3 value1, final Vector3 value2, Vector3 result)
	{
		result.x = value1.x - value2.x;
		result.y = value1.y - value2.y;
		result.z = value1.z - value2.z;
	}

	public String debugDisplayString()
	{
		return (this.x + "  " + this.y + "  " + this.z);
	}

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

	public static Vector3 transform(Vector3 position, Matrix matrix)
	{
		transform(position, matrix, position);
		return position;
	}

	public static void transform(final Vector3 position, final Matrix matrix, Vector3 result)
	{
		float x = (position.x * matrix.M11) + (position.y * matrix.M21) + (position.z * matrix.M31) + matrix.M41;
		float y = (position.x * matrix.M12) + (position.y * matrix.M22) + (position.z * matrix.M32) + matrix.M42;
		float z = (position.x * matrix.M13) + (position.y * matrix.M23) + (position.z * matrix.M33) + matrix.M43;
		result.x = x;
		result.y = y;
		result.z = z;
	}

	public static void transform(Vector3[] sourceArray, final Matrix matrix, Vector3[] destinationArray)
	{
		// TODO: Assert
		// Debug.Assert(destinationArray.Length >= sourceArray.length,
		// "The destination array is smaller than the source array.");

		// TODO: Are there options on some platforms to implement a vectorized version of this?

		for (int i = 0; i < sourceArray.length; ++i)
		{
			Vector3 position = sourceArray[i];
			destinationArray[i] = new Vector3((position.x * matrix.M11) + (position.y * matrix.M21)
					+ (position.z * matrix.M31) + matrix.M41, (position.x * matrix.M12) + (position.y * matrix.M22)
					+ (position.z * matrix.M32) + matrix.M42, (position.x * matrix.M13) + (position.y * matrix.M23)
					+ (position.z * matrix.M33) + matrix.M43);
		}
	}

	public static void transform(Vector3[] sourceArray, int sourceIndex, final Matrix matrix,
			Vector3[] destinationArray, int destinationIndex, int length)
	{
		// TODO: Assert
		// Debug.Assert(sourceArray.length - sourceIndex >= length,
		// "The source array is too small for the given sourceIndex and length.");
		// Debug.Assert(destinationArray.length - destinationIndex >= length,
		// "The destination array is too small for the given destinationIndex and length.");

		// TODO: Are there options on some platforms to implement a vectorized version of this?

		for (int i = 0; i < length; i++)
		{
			Vector3 position = sourceArray[sourceIndex + i];
			destinationArray[destinationIndex + i] = new Vector3((position.x * matrix.M11) + (position.y * matrix.M21)
					+ (position.z * matrix.M31) + matrix.M41, (position.x * matrix.M12) + (position.y * matrix.M22)
					+ (position.z * matrix.M32) + matrix.M42, (position.x * matrix.M13) + (position.y * matrix.M23)
					+ (position.z * matrix.M33) + matrix.M43);
		}
	}

	// / <summary>
	// / Transforms a vector by a quaternion rotation.
	// / </summary>
	// / <param name="vec">The vector to transform.</param>
	// / <param name="quat">The quaternion to rotate the vector by.</param>
	// / <returns>The result of the operation.</returns>
	public static Vector3 transform(Vector3 vec, Quaternion quat)
	{
		Vector3 result = Vector3.one();
		transform(vec, quat, result);
		return result;
	}

	// /// <summary>
	// /// Transforms a vector by a quaternion rotation.
	// /// </summary>
	// /// <param name="vec">The vector to transform.</param>
	// /// <param name="quat">The quaternion to rotate the vector by.</param>
	// /// <param name="result">The result of the operation.</param>
	// public static void Transform(final Vector3 vec, final Quaternion quat, Vector3 result)
	// {
	// // Taken from the OpentTK implementation of Vector3
	// // Since vec.W == 0, we can optimize quat * vec * quat^-1 as follows:
	// // vec + 2.0 * cross(quat.xyz, cross(quat.xyz, vec) + quat.w * vec)
	// Vector3 xyz = quat.xyz, temp, temp2;
	// Vector3.Cross(final xyz, final vec, temp);
	// Vector3.Multiply(final vec, quat.W, temp2);
	// Vector3.Add(final temp, final temp2, temp);
	// Vector3.Cross(final xyz, final temp, temp);
	// Vector3.Multiply(final temp, 2, temp);
	// Vector3.Add(final vec, final temp, result);
	// }

	// / <summary>
	// / Transforms a vector by a quaternion rotation.
	// / </summary>
	// / <param name="value">The vector to transform.</param>
	// / <param name="rotation">The quaternion to rotate the vector by.</param>
	// / <param name="result">The result of the operation.</param>
	public static void transform(final Vector3 value, final Quaternion rotation, Vector3 result)
	{
		float x = 2 * (rotation.y * value.z - rotation.z * value.y);
		float y = 2 * (rotation.z * value.x - rotation.x * value.z);
		float z = 2 * (rotation.x * value.y - rotation.y * value.x);

		result.x = value.x + x * rotation.w + (rotation.y * z - rotation.z * y);
		result.y = value.y + y * rotation.w + (rotation.z * x - rotation.x * z);
		result.z = value.z + z * rotation.w + (rotation.x * y - rotation.y * x);
	}

	// / <summary>
	// / Transforms an array of vectors by a quaternion rotation.
	// / </summary>
	// / <param name="sourceArray">The vectors to transform</param>
	// / <param name="rotation">The quaternion to rotate the vector by.</param>
	// / <param name="destinationArray">The result of the operation.</param>
	public static void Transform(Vector3[] sourceArray, final Quaternion rotation, Vector3[] destinationArray)
	{
		// TODO: Assert
		// Debug.Assert(destinationArray.length >= sourceArray.Length,
		// "The destination array is smaller than the source array.");

		// TODO: Are there options on some platforms to implement a vectorized version of this?

		for (int i = 0; i < sourceArray.length; i++)
		{
			Vector3 position = sourceArray[i];

			float x = 2 * (rotation.y * position.z - rotation.z * position.y);
			float y = 2 * (rotation.z * position.x - rotation.x * position.z);
			float z = 2 * (rotation.x * position.y - rotation.y * position.x);

			destinationArray[i] = new Vector3(position.x + x * rotation.w + (rotation.y * z - rotation.z * y),
					position.y + y * rotation.w + (rotation.z * x - rotation.x * z), position.z + z * rotation.w
							+ (rotation.x * y - rotation.y * x));
		}
	}

	// / <summary>
	// / Transforms an array of vectors within a given range by a quaternion rotation.
	// / </summary>
	// / <param name="sourceArray">The vectors to transform.</param>
	// / <param name="sourceIndex">The starting index in the source array.</param>
	// / <param name="rotation">The quaternion to rotate the vector by.</param>
	// / <param name="destinationArray">The array to store the result of the operation.</param>
	// / <param name="destinationIndex">The starting index in the destination array.</param>
	// / <param name="length">The number of vectors to transform.</param>
	public static void transform(Vector3[] sourceArray, int sourceIndex, final Quaternion rotation,
			Vector3[] destinationArray, int destinationIndex, int length)
	{
		// TODO: Assert
		// Debug.Assert(sourceArray.length - sourceIndex >= length,
		// "The source array is too small for the given sourceIndex and length.");
		// Debug.Assert(destinationArray.length - destinationIndex >= length,
		// "The destination array is too small for the given destinationIndex and length.");

		// TODO: Are there options on some platforms to implement a vectorized version of this?

		for (int i = 0; i < length; i++)
		{
			Vector3 position = sourceArray[sourceIndex + i];

			float x = 2 * (rotation.y * position.z - rotation.z * position.y);
			float y = 2 * (rotation.z * position.x - rotation.x * position.z);
			float z = 2 * (rotation.x * position.y - rotation.y * position.x);

			destinationArray[destinationIndex + i] = new Vector3(position.x + x * rotation.w
					+ (rotation.y * z - rotation.z * y), position.y + y * rotation.w
					+ (rotation.z * x - rotation.x * z), position.z + z * rotation.w
					+ (rotation.x * y - rotation.y * x));
		}
	}

	public static Vector3 transformNormal(Vector3 normal, Matrix matrix)
	{
		transformNormal(normal, matrix, normal);
		return normal;
	}

	public static void transformNormal(final Vector3 normal, final Matrix matrix, Vector3 result)
	{
		float x = (normal.x * matrix.M11) + (normal.y * matrix.M21) + (normal.z * matrix.M31);
		float y = (normal.x * matrix.M12) + (normal.y * matrix.M22) + (normal.z * matrix.M32);
		float z = (normal.x * matrix.M13) + (normal.y * matrix.M23) + (normal.z * matrix.M33);
		result.x = x;
		result.y = y;
		result.z = z;
	}

	public static void transformNormal(Vector3[] sourceArray, final Matrix matrix, Vector3[] destinationArray)
	{
		// TODO: Assert
		// Debug.Assert(destinationArray.length >= sourceArray.length,
		// "The destination array is smaller than the source array.");

		for (int i = 0; i < sourceArray.length; ++i)
		{
			Vector3 normal = sourceArray[i];
			destinationArray[i] = new Vector3((normal.x * matrix.M11) + (normal.y * matrix.M21)
					+ (normal.z * matrix.M31), (normal.x * matrix.M12) + (normal.y * matrix.M22)
					+ (normal.z * matrix.M32), (normal.x * matrix.M13) + (normal.y * matrix.M23)
					+ (normal.z * matrix.M33));
		}
	}

	// TODO: Turn some of those operators into methods like equals and not equals
	// public static bool operator ==(Vector3 value1, Vector3 value2)
	// {
	// return value1.x == value2.x
	// && value1.y == value2.y
	// && value1.z == value2.z;
	// }
	//
	// public static bool operator !=(Vector3 value1, Vector3 value2)
	// {
	// return !(value1 == value2);
	// }
	//
	// public static Vector3 operator +(Vector3 value1, Vector3 value2)
	// {
	// value1.x += value2.x;
	// value1.y += value2.y;
	// value1.z += value2.z;
	// return value1;
	// }
	//
	// public static Vector3 operator -(Vector3 value)
	// {
	// value = new Vector3(-value.x, -value.y, -value.z);
	// return value;
	// }
	//
	// public static Vector3 operator -(Vector3 value1, Vector3 value2)
	// {
	// value1.x -= value2.x;
	// value1.y -= value2.y;
	// value1.z -= value2.z;
	// return value1;
	// }
	//
	// public static Vector3 operator *(Vector3 value1, Vector3 value2)
	// {
	// value1.x *= value2.x;
	// value1.y *= value2.y;
	// value1.z *= value2.z;
	// return value1;
	// }
	//
	// public static Vector3 operator *(Vector3 value, float scaleFactor)
	// {
	// value.x *= scaleFactor;
	// value.y *= scaleFactor;
	// value.z *= scaleFactor;
	// return value;
	// }
	//
	// public static Vector3 operator *(float scaleFactor, Vector3 value)
	// {
	// value.x *= scaleFactor;
	// value.y *= scaleFactor;
	// value.z *= scaleFactor;
	// return value;
	// }
	//
	// public static Vector3 operator /(Vector3 value1, Vector3 value2)
	// {
	// value1.x /= value2.x;
	// value1.y /= value2.y;
	// value1.z /= value2.z;
	// return value1;
	// }
	//
	// public static Vector3 operator /(Vector3 value, float divider)
	// {
	// float factor = 1 / divider;
	// value.x *= factor;
	// value.y *= factor;
	// value.z *= factor;
	// return value;
	// }
}
