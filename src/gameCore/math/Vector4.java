package gameCore.math;

// C# struct
/**
 * Defines a vector with four float components.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft)
 *
 */
public class Vector4
{

	private static Vector4 zero = new Vector4(0f, 0f, 0f, 0f);
	private static Vector4 one = new Vector4(1f, 1f, 1f, 1f);
	private static Vector4 unitX = new Vector4(1f, 0f, 0f, 0f);
	private static Vector4 unitY = new Vector4(0f, 1f, 0f, 0f);
	private static Vector4 unitZ = new Vector4(0f, 0f, 1f, 0f);
	private static Vector4 unitW = new Vector4(0f, 0f, 0f, 1f);

	public float x;

	public float y;

	public float z;

	public float w;

	// / <summary>
	// / Returns a <see>Vector4</see> with components 0, 0, 0, 0.
	// / </summary>
	public static Vector4 zero()
	{
		return new Vector4(zero);
	}

	// / <summary>
	// / Returns a <see>Vector4</see> with components 1, 1, 1, 1.
	// / </summary>
	public static Vector4 one()
	{
		return new Vector4(one);
	}

	// / <summary>
	// / Returns a <see>Vector4</see> with components 1, 0, 0, 0.
	// / </summary>
	public static Vector4 unitX()
	{
		return new Vector4(unitX);
	}

	// / <summary>
	// / Returns a <see>Vector4</see> with components 0, 1, 0, 0.
	// / </summary>
	public static Vector4 unitY()
	{
		return new Vector4(unitY);
	}

	// / <summary>
	// / Returns a <see>Vector4</see> with components 0, 0, 1, 0.
	// / </summary>
	public static Vector4 unitZ()
	{
		return new Vector4(unitZ);
	}

	// / <summary>
	// / Returns a <see>Vector4</see> with components 0, 0, 0, 1.
	// / </summary>
	public static Vector4 unitW()
	{
		return new Vector4(unitW);
	}

	// Note: Added this since it is provided by default for struct in C#
	public Vector4()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
		this.w = 0.0f;
	}

	public Vector4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4(Vector2 value, float z, float w)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = z;
		this.w = w;
	}

	public Vector4(Vector3 value, float w)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
		this.w = w;
	}

	public Vector4(Vector4 value)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
		this.w = value.w;
	}

	public Vector4(float value)
	{
		this.x = value;
		this.y = value;
		this.z = value;
		this.w = value;
	}

	// / <summary>
	// / Performs vector addition on <paramfinal name="value1"/> and <paramfinal name="value2"/>.
	// / </summary>
	// / <param name="value1">The first vector to add.</param>
	// / <param name="value2">The second vector to add.</param>
	// / <returns>The result of the vector addition.</returns>
	public static Vector4 add(Vector4 value1, Vector4 value2)
	{
		value1.x += value2.x;
		value1.y += value2.y;
		value1.z += value2.z;
		value1.w += value2.w;
		return value1;
	}

	// / <summary>
	// / Performs vector addition on <paramfinal name="value1"/> and
	// / <paramfinal name="value2"/>, storing the result of the
	// / addition in <paramfinal name="result"/>.
	// / </summary>
	// / <param name="value1">The first vector to add.</param>
	// / <param name="value2">The second vector to add.</param>
	// / <param name="result">The result of the vector addition.</param>
	public static void add(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.x = value1.x + value2.x;
		result.y = value1.y + value2.y;
		result.z = value1.z + value2.z;
		result.w = value1.w + value2.w;
	}

	public static Vector4 barycentric(Vector4 value1, Vector4 value2, Vector4 value3, float amount1, float amount2)
	{
		return new Vector4(MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
				MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2), MathHelper.barycentric(
						value1.z, value2.z, value3.z, amount1, amount2), MathHelper.barycentric(value1.w, value2.w,
						value3.w, amount1, amount2));
	}

	public static void barycentric(final Vector4 value1, final Vector4 value2, final Vector4 value3, float amount1,
			float amount2, Vector4 result)
	{
		result.x = MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2);
		result.y = MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2);
		result.z = MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2);
		result.w = MathHelper.barycentric(value1.w, value2.w, value3.w, amount1, amount2);
	}

	public static Vector4 catmullRom(Vector4 value1, Vector4 value2, Vector4 value3, Vector4 value4, float amount)
	{
		return new Vector4(MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
				MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount), MathHelper.catmullRom(value1.z,
						value2.z, value3.z, value4.z, amount), MathHelper.catmullRom(value1.w, value2.w, value3.w,
						value4.w, amount));
	}

	public static void catmullRom(final Vector4 value1, final Vector4 value2, final Vector4 value3,
			final Vector4 value4, float amount, Vector4 result)
	{
		result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
		result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
		result.z = MathHelper.catmullRom(value1.z, value2.z, value3.z, value4.z, amount);
		result.w = MathHelper.catmullRom(value1.w, value2.w, value3.w, value4.w, amount);
	}

	public static Vector4 clamp(Vector4 value1, Vector4 min, Vector4 max)
	{
		return new Vector4(MathHelper.clamp(value1.x, min.x, max.x), MathHelper.clamp(value1.y, min.y, max.y),
				MathHelper.clamp(value1.z, min.z, max.z), MathHelper.clamp(value1.w, min.w, max.w));
	}

	public static void clamp(final Vector4 value1, final Vector4 min, final Vector4 max, Vector4 result)
	{
		result.x = MathHelper.clamp(value1.x, min.x, max.x);
		result.y = MathHelper.clamp(value1.y, min.y, max.y);
		result.z = MathHelper.clamp(value1.z, min.z, max.z);
		result.w = MathHelper.clamp(value1.w, min.w, max.w);
	}

	public static float distance(Vector4 value1, Vector4 value2)
	{
		return (float) Math.sqrt(distanceSquared(value1, value2));
	}

	// public static void distance(final Vector4 value1, final Vector4 value2, float result) {
	// result = (float)Math.sqrt(DistanceSquared(value1, value2));
	// }

	public static float distanceSquared(Vector4 value1, Vector4 value2)
	{
		float result = (value1.w - value2.w) * (value1.w - value2.w) + (value1.x - value2.x) * (value1.x - value2.x)
				+ (value1.y - value2.y) * (value1.y - value2.y) + (value1.z - value2.z) * (value1.z - value2.z);
		return result;
	}

	// public static void distanceSquared(final Vector4 value1, final Vector4 value2, float
	// result) {
	// result = (value1.W - value2.W) * (value1.W - value2.W) +
	// (value1.X - value2.X) * (value1.X - value2.X) +
	// (value1.Y - value2.Y) * (value1.Y - value2.Y) +
	// (value1.Z - value2.Z) * (value1.Z - value2.Z);
	// }

	public static Vector4 divide(Vector4 value1, Vector4 value2)
	{
		value1.w /= value2.w;
		value1.x /= value2.x;
		value1.y /= value2.y;
		value1.z /= value2.z;
		return value1;
	}

	public static Vector4 divide(Vector4 value1, float divider)
	{
		float factor = 1f / divider;
		value1.w *= factor;
		value1.x *= factor;
		value1.y *= factor;
		value1.z *= factor;
		return value1;
	}

	public static void divide(final Vector4 value1, float divider, Vector4 result)
	{
		float factor = 1f / divider;
		result.w = value1.w * factor;
		result.x = value1.x * factor;
		result.y = value1.y * factor;
		result.z = value1.z * factor;
	}

	public static void divide(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.w = value1.w / value2.w;
		result.x = value1.x / value2.x;
		result.y = value1.y / value2.y;
		result.z = value1.z / value2.z;
	}

	public static float dot(Vector4 vector1, Vector4 vector2)
	{
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z + vector1.w * vector2.w;
	}

	public static void dot(final Vector4 vector1, final Vector4 vector2, float result)
	{
		result = vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z + vector1.w * vector2.w;
	}

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Vector4) ? this.equals((Vector4) obj) : false;
	}

	public boolean equals(Vector4 other)
	{
		return this.w == other.w && this.x == other.x && this.y == other.y && this.z == other.z;
	}

	@Override
	public int hashCode()
	{
		return (int) (this.w + this.x + this.y + this.y);
	}

	public static Vector4 hermite(Vector4 value1, Vector4 tangent1, Vector4 value2, Vector4 tangent2, float amount)
	{
		Vector4 result = new Vector4();
		hermite(value1, tangent1, value2, tangent2, amount, result);
		return result;
	}

	public static void hermite(final Vector4 value1, final Vector4 tangent1, final Vector4 value2,
			final Vector4 tangent2, float amount, Vector4 result)
	{
		result.w = MathHelper.hermite(value1.w, tangent1.w, value2.w, tangent2.w, amount);
		result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
		result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
		result.z = MathHelper.hermite(value1.z, tangent1.z, value2.z, tangent2.z, amount);
	}

	public float length()
	{
		float result = distanceSquared(this, Vector4.zero());
		return (float) Math.sqrt(result);
	}

	public float lengthSquared()
	{
		float result = distanceSquared(this, Vector4.zero());
		return result;
	}

	public static Vector4 lerp(Vector4 value1, Vector4 value2, float amount)
	{
		return new Vector4(MathHelper.lerp(value1.x, value2.x, amount), MathHelper.lerp(value1.y, value2.y, amount),
				MathHelper.lerp(value1.z, value2.z, amount), MathHelper.lerp(value1.w, value2.w, amount));
	}

	public static void lerp(final Vector4 value1, final Vector4 value2, float amount, Vector4 result)
	{
		result.x = MathHelper.lerp(value1.x, value2.x, amount);
		result.y = MathHelper.lerp(value1.y, value2.y, amount);
		result.z = MathHelper.lerp(value1.z, value2.z, amount);
		result.w = MathHelper.lerp(value1.w, value2.w, amount);
	}

	public static Vector4 max(Vector4 value1, Vector4 value2)
	{
		return new Vector4(MathHelper.max(value1.x, value2.x), MathHelper.max(value1.y, value2.y), MathHelper.max(
				value1.z, value2.z), MathHelper.max(value1.w, value2.w));
	}

	public static void max(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.x = MathHelper.max(value1.x, value2.x);
		result.y = MathHelper.max(value1.y, value2.y);
		result.z = MathHelper.max(value1.z, value2.z);
		result.w = MathHelper.max(value1.w, value2.w);
	}

	public static Vector4 min(Vector4 value1, Vector4 value2)
	{
		return new Vector4(MathHelper.min(value1.x, value2.x), MathHelper.min(value1.y, value2.y), MathHelper.min(
				value1.z, value2.z), MathHelper.min(value1.w, value2.w));
	}

	public static void min(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.x = MathHelper.min(value1.x, value2.x);
		result.y = MathHelper.min(value1.y, value2.y);
		result.z = MathHelper.min(value1.z, value2.z);
		result.w = MathHelper.min(value1.w, value2.w);
	}

	public static Vector4 multiply(Vector4 value1, Vector4 value2)
	{
		value1.w *= value2.w;
		value1.x *= value2.x;
		value1.y *= value2.y;
		value1.z *= value2.z;
		return value1;
	}

	public static Vector4 multiply(Vector4 value1, float scaleFactor)
	{
		value1.w *= scaleFactor;
		value1.x *= scaleFactor;
		value1.y *= scaleFactor;
		value1.z *= scaleFactor;
		return value1;
	}

	public static void multiply(final Vector4 value1, float scaleFactor, Vector4 result)
	{
		result.w = value1.w * scaleFactor;
		result.x = value1.x * scaleFactor;
		result.y = value1.y * scaleFactor;
		result.z = value1.z * scaleFactor;
	}

	public static void multiply(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.w = value1.w * value2.w;
		result.x = value1.x * value2.x;
		result.y = value1.y * value2.y;
		result.z = value1.z * value2.z;
	}

	public static Vector4 negate(Vector4 value)
	{
		Vector4 result = new Vector4(-value.x, -value.y, -value.z, -value.w);
		return result;
	}

	public static void negate(final Vector4 value, Vector4 result)
	{
		result.x = -value.x;
		result.y = -value.y;
		result.z = -value.z;
		result.w = -value.w;
	}

	public void normalize()
	{
		normalize(this, this);
	}

	public static Vector4 normalize(Vector4 vector)
	{
		normalize(vector, vector);
		return vector;
	}

	public static void normalize(final Vector4 vector, Vector4 result)
	{
		float factor = distanceSquared(vector, zero);
		factor = 1f / (float) Math.sqrt(factor);

		result.w = vector.w * factor;
		result.x = vector.x * factor;
		result.y = vector.y * factor;
		result.z = vector.z * factor;
	}

	public static Vector4 smoothStep(Vector4 value1, Vector4 value2, float amount)
	{
		return new Vector4(MathHelper.smoothStep(value1.x, value2.x, amount), MathHelper.smoothStep(value1.y,
				value2.y, amount), MathHelper.smoothStep(value1.z, value2.z, amount), MathHelper.smoothStep(value1.w,
				value2.w, amount));
	}

	public static void smoothStep(final Vector4 value1, final Vector4 value2, float amount, Vector4 result)
	{
		result.x = MathHelper.smoothStep(value1.x, value2.x, amount);
		result.y = MathHelper.smoothStep(value1.y, value2.y, amount);
		result.z = MathHelper.smoothStep(value1.z, value2.z, amount);
		result.w = MathHelper.smoothStep(value1.w, value2.w, amount);
	}

	// / <summary>
	// / Performs vector subtraction on <paramfinal name="value1"/> and <paramfinal name="value2"/>.
	// / </summary>
	// / <param name="value1">The vector to be subtracted from.</param>
	// / <param name="value2">The vector to be subtracted from <paramfinal name="value1"/>.</param>
	// / <returns>The result of the vector subtraction.</returns>
	public static Vector4 subtract(Vector4 value1, Vector4 value2)
	{
		value1.w -= value2.w;
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
	public static void subtract(final Vector4 value1, final Vector4 value2, Vector4 result)
	{
		result.w = value1.w - value2.w;
		result.x = value1.x - value2.x;
		result.y = value1.y - value2.y;
		result.z = value1.z - value2.z;
	}

	public static Vector4 transform(Vector2 position, Matrix matrix)
	{
		Vector4 result = new Vector4();
		transform(position, matrix, result);
		return result;
	}

	public static Vector4 transform(Vector3 position, Matrix matrix)
	{
		Vector4 result = new Vector4();
		transform(position, matrix, result);
		return result;
	}

	public static Vector4 transform(Vector4 vector, Matrix matrix)
	{
		transform(vector, matrix, vector);
		return vector;
	}

	public static void transform(final Vector2 position, final Matrix matrix, Vector4 result)
	{
		result.x = (position.x * matrix.M11) + (position.y * matrix.M21) + matrix.M41;
		result.y = (position.x * matrix.M12) + (position.y * matrix.M22) + matrix.M42;
		result.z = (position.x * matrix.M13) + (position.y * matrix.M23) + matrix.M43;
		result.w = (position.x * matrix.M14) + (position.y * matrix.M24) + matrix.M44;
	}

	public static void transform(final Vector3 position, final Matrix matrix, Vector4 result)
	{
		result.x = (position.x * matrix.M11) + (position.y * matrix.M21) + (position.z * matrix.M31) + matrix.M41;
		result.y = (position.x * matrix.M12) + (position.y * matrix.M22) + (position.z * matrix.M32) + matrix.M42;
		result.z = (position.x * matrix.M13) + (position.y * matrix.M23) + (position.z * matrix.M33) + matrix.M43;
		result.w = (position.x * matrix.M14) + (position.y * matrix.M24) + (position.z * matrix.M34) + matrix.M44;
	}

	public static void transform(final Vector4 vector, final Matrix matrix, Vector4 result)
	{
		float x = (vector.x * matrix.M11) + (vector.y * matrix.M21) + (vector.z * matrix.M31) + (vector.w * matrix.M41);
		float y = (vector.x * matrix.M12) + (vector.y * matrix.M22) + (vector.z * matrix.M32) + (vector.w * matrix.M42);
		float z = (vector.x * matrix.M13) + (vector.y * matrix.M23) + (vector.z * matrix.M33) + (vector.w * matrix.M43);
		float w = (vector.x * matrix.M14) + (vector.y * matrix.M24) + (vector.z * matrix.M34) + (vector.w * matrix.M44);
		result.x = x;
		result.y = y;
		result.z = z;
		result.w = w;
	}

	protected String debugDisplayString()
	{
		return (this.x + "  " + this.y + "  " + this.z + "  " + this.w);
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
		sb.append(" W:");
		sb.append(this.w);
		sb.append("}");
		return sb.toString();
	}

	// TODO: Turn some of those operators into methods like equals and not equals
	// public static Vector4 operator -(Vector4 value)
	// {
	// return new Vector4(-value.X, -value.Y, -value.Z, -value.W);
	// }
	//
	// public static bool operator ==(Vector4 value1, Vector4 value2)
	// {
	// return value1.W == value2.W
	// && value1.X == value2.X
	// && value1.Y == value2.Y
	// && value1.Z == value2.Z;
	// }
	//
	// public static bool operator !=(Vector4 value1, Vector4 value2)
	// {
	// return !(value1 == value2);
	// }
	//
	// public static Vector4 operator +(Vector4 value1, Vector4 value2)
	// {
	// value1.W += value2.W;
	// value1.X += value2.X;
	// value1.Y += value2.Y;
	// value1.Z += value2.Z;
	// return value1;
	// }
	//
	// public static Vector4 operator -(Vector4 value1, Vector4 value2)
	// {
	// value1.W -= value2.W;
	// value1.X -= value2.X;
	// value1.Y -= value2.Y;
	// value1.Z -= value2.Z;
	// return value1;
	// }
	//
	// public static Vector4 operator *(Vector4 value1, Vector4 value2)
	// {
	// value1.W *= value2.W;
	// value1.X *= value2.X;
	// value1.Y *= value2.Y;
	// value1.Z *= value2.Z;
	// return value1;
	// }
	//
	// public static Vector4 operator *(Vector4 value1, float scaleFactor)
	// {
	// value1.W *= scaleFactor;
	// value1.X *= scaleFactor;
	// value1.Y *= scaleFactor;
	// value1.Z *= scaleFactor;
	// return value1;
	// }
	//
	// public static Vector4 operator *(float scaleFactor, Vector4 value1)
	// {
	// value1.W *= scaleFactor;
	// value1.X *= scaleFactor;
	// value1.Y *= scaleFactor;
	// value1.Z *= scaleFactor;
	// return value1;
	// }
	//
	// public static Vector4 operator /(Vector4 value1, Vector4 value2)
	// {
	// value1.W /= value2.W;
	// value1.X /= value2.X;
	// value1.Y /= value2.Y;
	// value1.Z /= value2.Z;
	// return value1;
	// }
	//
	// public static Vector4 operator /(Vector4 value1, float divider)
	// {
	// float factor = 1f / divider;
	// value1.W *= factor;
	// value1.X *= factor;
	// value1.Y *= factor;
	// value1.Z *= factor;
	// return value1;
	// }

}
