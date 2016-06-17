package jMono_Framework.math;

// C# struct
/**
 * An efficient mathematical representation for three dimensional rotations.
 * 
 * @author Eric
 *
 */
public class Quaternion // implements IEquatable<Quaternion>
{
	static final Quaternion identity = new Quaternion(0, 0, 0, 1);

	public float x;

	public float y;

	public float z;

	public float w;

	// Note: Added this since it is provided by default for struct in C#
	public Quaternion()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
		this.w = 0.0f;
	}
	
	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Vector3 value, float w)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
		this.w = w;
	}

	public Quaternion(Vector4 value)
	{
		this.x = value.x;
		this.y = value.y;
		this.z = value.z;
		this.w = value.w;
	}

	public Quaternion(Quaternion q)
	{
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
	}

	public static Quaternion identity()
	{
		return new Quaternion(identity);
	}

	protected String debugDisplayString()
	{
		if (this.equals(Quaternion.identity))
		{
			return "Identity";
		}

		return (this.x + " " + this.y + " " + this.z + " " + this.w);
	}

	public static Quaternion add(Quaternion quaternion1, Quaternion quaternion2)
	{
		Quaternion quaternion = Quaternion.identity();
		quaternion.x = quaternion1.x + quaternion2.x;
		quaternion.y = quaternion1.y + quaternion2.y;
		quaternion.z = quaternion1.z + quaternion2.z;
		quaternion.w = quaternion1.w + quaternion2.w;
		return quaternion;
	}

	public static void add(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
	{
		result.x = quaternion1.x + quaternion2.x;
		result.y = quaternion1.y + quaternion2.y;
		result.z = quaternion1.z + quaternion2.z;
		result.w = quaternion1.w + quaternion2.w;
	}

	public static Quaternion concatenate(Quaternion value1, Quaternion value2)
	{
		Quaternion quaternion = Quaternion.identity();
		
		float x1 = value1.x;
		float y1 = value1.y;
		float z1 = value1.z;
		float w1 = value1.w;

		float x2 = value2.x;
		float y2 = value2.y;
		float z2 = value2.z;
		float w2 = value2.w;
		
		quaternion.x = ((x2 * w1) + (x1 * w2)) + ((y2 * z1) - (z2 * y1));
		quaternion.y = ((y2 * w1) + (y1 * w2)) + ((z2 * x1) - (x2 * z1));
		quaternion.z = ((z2 * w1) + (z1 * w2)) + ((x2 * y1) - (y2 * x1));
		quaternion.w = (w2 * w1) - (((x2 * x1) + (y2 * y1)) + (z2 * z1));
		
		return quaternion;
	}

	public static void concatenate(final Quaternion value1, final Quaternion value2, Quaternion result)
	{
		float x1 = value1.x;
		float y1 = value1.y;
		float z1 = value1.z;
		float w1 = value1.w;

		float x2 = value2.x;
		float y2 = value2.y;
		float z2 = value2.z;
		float w2 = value2.w;
		
		result.x = ((x2 * w1) + (x1 * w2)) + ((y2 * z1) - (z2 * y1));
		result.y = ((y2 * w1) + (y1 * w2)) + ((z2 * x1) - (x2 * z1));
		result.z = ((z2 * w1) + (z1 * w2)) + ((x2 * y1) - (y2 * x1));
		result.w = (w2 * w1) - (((x2 * x1) + (y2 * y1)) + (z2 * z1));
	}

	public void conjugate()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public static Quaternion conjugate(Quaternion value)
	{
		return new Quaternion(-value.x,-value.y,-value.z,value.w);
	}

	public static void conjugate(final Quaternion value, Quaternion result)
	{
		result.x = -value.x;
		result.y = -value.y;
		result.z = -value.z;
		result.w = value.w;
	}

	public static Quaternion createFromAxisAngle(Vector3 axis, float angle)
	{
		float half = angle * 0.5f;
	    float sin = (float)Math.sin(half);
	    float cos = (float)Math.cos(half);
	    return new Quaternion(axis.x * sin, axis.y * sin, axis.z * sin, cos);
	}

	public static void createFromAxisAngle(final Vector3 axis, float angle, Quaternion result)
	{
		float half = angle * 0.5f;
		float sin = (float) Math.sin((double) half);
		float cos = (float) Math.cos((double) half);
		result.x = axis.x * sin;
		result.y = axis.y * sin;
		result.z = axis.z * sin;
		result.w = cos;

	}

	public static Quaternion createFromRotationMatrix(Matrix matrix)
	{
		Quaternion quaternion = Quaternion.identity();
		float sqrt;
		float half;
		float scale = matrix.M11 + matrix.M22 + matrix.M33;
        
		if (scale > 0.0f)
		{
			sqrt = (float)Math.sqrt(scale + 1.0f);
			quaternion.w = sqrt * 0.5f;
			sqrt = 0.5f / sqrt;

			quaternion.x = (matrix.M23 - matrix.M32) * sqrt;
			quaternion.y = (matrix.M31 - matrix.M13) * sqrt;
			quaternion.z = (matrix.M12 - matrix.M21) * sqrt;

			return quaternion;
	    }
		if ((matrix.M11 >= matrix.M22) && (matrix.M11 >= matrix.M33))
		{
			sqrt = (float) Math.sqrt(1.0f + matrix.M11 - matrix.M22 - matrix.M33);
			half = 0.5f / sqrt;

			quaternion.x = 0.5f * sqrt;
			quaternion.y = (matrix.M12 + matrix.M21) * half;
			quaternion.z = (matrix.M13 + matrix.M31) * half;
			quaternion.w = (matrix.M23 - matrix.M32) * half;

	        return quaternion;
		}
		if (matrix.M22 > matrix.M33)
		{
			sqrt = (float) Math.sqrt(1.0f + matrix.M22 - matrix.M11 - matrix.M33);
			half = 0.5f / sqrt;

			quaternion.x = (matrix.M21 + matrix.M12) * half;
			quaternion.y = 0.5f * sqrt;
			quaternion.z = (matrix.M32 + matrix.M23) * half;
			quaternion.w = (matrix.M31 - matrix.M13) * half;

			return quaternion;
		}
		sqrt = (float) Math.sqrt(1.0f + matrix.M33 - matrix.M11 - matrix.M22);
		half = 0.5f / sqrt;

		quaternion.x = (matrix.M31 + matrix.M13) * half;
		quaternion.y = (matrix.M32 + matrix.M23) * half;
		quaternion.z = 0.5f * sqrt;
		quaternion.w = (matrix.M12 - matrix.M21) * half;

		return quaternion;
	}

	public static void createFromRotationMatrix(final Matrix matrix, Quaternion result)
	{
		float sqrt;
		float half;
		float scale = matrix.M11 + matrix.M22 + matrix.M33;

		if (scale > 0.0f)
		{
			sqrt = (float)Math.sqrt(scale + 1.0f);
			result.w = sqrt * 0.5f;
			sqrt = 0.5f / sqrt;

			result.x = (matrix.M23 - matrix.M32) * sqrt;
			result.y = (matrix.M31 - matrix.M13) * sqrt;
			result.z = (matrix.M12 - matrix.M21) * sqrt;
		}
		else
			if ((matrix.M11 >= matrix.M22) && (matrix.M11 >= matrix.M33))
			{
				sqrt = (float)Math.sqrt(1.0f + matrix.M11 - matrix.M22 - matrix.M33);
				half = 0.5f / sqrt;

				result.x = 0.5f * sqrt;
				result.y = (matrix.M12 + matrix.M21) * half;
				result.z = (matrix.M13 + matrix.M31) * half;
				result.w = (matrix.M23 - matrix.M32) * half;
			}
			else if (matrix.M22 > matrix.M33)
			{
				sqrt = (float) Math.sqrt(1.0f + matrix.M22 - matrix.M11 - matrix.M33);
				half = 0.5f/sqrt;

				result.x = (matrix.M21 + matrix.M12)*half;
				result.y = 0.5f*sqrt;
				result.z = (matrix.M32 + matrix.M23)*half;
				result.w = (matrix.M31 - matrix.M13)*half;
			}
			else
			{
				sqrt = (float)Math.sqrt(1.0f + matrix.M33 - matrix.M11 - matrix.M22);
				half = 0.5f / sqrt;

				result.x = (matrix.M31 + matrix.M13) * half;
				result.y = (matrix.M32 + matrix.M23) * half;
				result.z = 0.5f * sqrt;
				result.w = (matrix.M12 - matrix.M21) * half;
			}
	}

	public static Quaternion createFromYawPitchRoll(float yaw, float pitch, float roll)
	{
		float halfRoll = roll * 0.5f;
		float halfPitch = pitch * 0.5f;
		float halfYaw = yaw * 0.5f;

		float sinRoll = (float)Math.sin(halfRoll);
		float cosRoll = (float)Math.cos(halfRoll);
		float sinPitch = (float)Math.sin(halfPitch);
		float cosPitch = (float)Math.cos(halfPitch);
		float sinYaw = (float)Math.sin(halfYaw);
		float cosYaw = (float)Math.cos(halfYaw);

		return new Quaternion((cosYaw * sinPitch * cosRoll) + (sinYaw * cosPitch * sinRoll),	//
							  (sinYaw * cosPitch * cosRoll) - (cosYaw * sinPitch * sinRoll),	//
							  (cosYaw * cosPitch * sinRoll) - (sinYaw * sinPitch * cosRoll),	//
							  (cosYaw * cosPitch * cosRoll) + (sinYaw * sinPitch * sinRoll));
	}

	public static void createFromYawPitchRoll(float yaw, float pitch, float roll, Quaternion result)
	{
		float halfRoll = roll * 0.5f;
		float halfPitch = pitch * 0.5f;
		float halfYaw = yaw * 0.5f;

		float sinRoll = (float)Math.sin(halfRoll);
		float cosRoll = (float)Math.cos(halfRoll);
		float sinPitch = (float)Math.sin(halfPitch);
		float cosPitch = (float)Math.cos(halfPitch);
		float sinYaw = (float)Math.sin(halfYaw);
		float cosYaw = (float)Math.cos(halfYaw);

		result.x = (cosYaw * sinPitch * cosRoll) + (sinYaw * cosPitch * sinRoll);
		result.y = (sinYaw * cosPitch * cosRoll) - (cosYaw * sinPitch * sinRoll);
		result.z = (cosYaw * cosPitch * sinRoll) - (sinYaw * sinPitch * cosRoll);
		result.w = (cosYaw * cosPitch * cosRoll) + (sinYaw * sinPitch * sinRoll);
	}

	public static Quaternion divide(Quaternion quaternion1, Quaternion quaternion2)
	{
		Quaternion quaternion = Quaternion.identity();
		float x = quaternion1.x;
		float y = quaternion1.y;
		float z = quaternion1.z;
		float w = quaternion1.w;
		float num14 = (((quaternion2.x * quaternion2.x) + (quaternion2.y * quaternion2.y)) + (quaternion2.z * quaternion2.z))
				+ (quaternion2.w * quaternion2.w);
		float num5 = 1f / num14;
		float num4 = -quaternion2.x * num5;
		float num3 = -quaternion2.y * num5;
		float num2 = -quaternion2.z * num5;
		float num = quaternion2.w * num5;
		float num13 = (y * num2) - (z * num3);
		float num12 = (z * num4) - (x * num2);
		float num11 = (x * num3) - (y * num4);
		float num10 = ((x * num4) + (y * num3)) + (z * num2);
		quaternion.x = ((x * num) + (num4 * w)) + num13;
		quaternion.y = ((y * num) + (num3 * w)) + num12;
		quaternion.z = ((z * num) + (num2 * w)) + num11;
		quaternion.w = (w * num) - num10;
		return quaternion;

	}

	public static void divide(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
	{
		float x = quaternion1.x;
		float y = quaternion1.y;
		float z = quaternion1.z;
		float w = quaternion1.w;
		float num14 = (((quaternion2.x * quaternion2.x) + (quaternion2.y * quaternion2.y)) + (quaternion2.z * quaternion2.z))
				+ (quaternion2.w * quaternion2.w);
		float num5 = 1f / num14;
		float num4 = -quaternion2.x * num5;
		float num3 = -quaternion2.y * num5;
		float num2 = -quaternion2.z * num5;
		float num = quaternion2.w * num5;
		float num13 = (y * num2) - (z * num3);
		float num12 = (z * num4) - (x * num2);
		float num11 = (x * num3) - (y * num4);
		float num10 = ((x * num4) + (y * num3)) + (z * num2);
		result.x = ((x * num) + (num4 * w)) + num13;
		result.y = ((y * num) + (num3 * w)) + num12;
		result.z = ((z * num) + (num2 * w)) + num11;
		result.w = (w * num) - num10;

	}

	public static float dot(Quaternion quaternion1, Quaternion quaternion2)
	{
		return ((((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w));
	}

	public static void dot(final Quaternion quaternion1, final Quaternion quaternion2, float result)
	{
		result = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z))
				+ (quaternion1.w * quaternion2.w);
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
		return this.equals((Quaternion) obj);
	}

	// Helper method
	private boolean equals(Quaternion other)
	{
		return this.x == other.x &&	//
			   this.y == other.y &&	//
			   this.z == other.z &&	//
			   this.w == other.w;
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
	
	@Override
	public int hashCode()
	{
		return (((Float.hashCode(x) + Float.hashCode(y)) + Float.hashCode(z)) + Float.hashCode(w));
	}

	public static Quaternion inverse(Quaternion quaternion)
	{
		Quaternion quaternion2 = Quaternion.identity();
		float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z))
				+ (quaternion.w * quaternion.w);
		float num = 1f / num2;
		quaternion2.x = -quaternion.x * num;
		quaternion2.y = -quaternion.y * num;
		quaternion2.z = -quaternion.z * num;
		quaternion2.w = quaternion.w * num;
		return quaternion2;

	}

	public static void inverse(final Quaternion quaternion, Quaternion result)
	{
		float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z))
				+ (quaternion.w * quaternion.w);
		float num = 1f / num2;
		result.x = -quaternion.x * num;
		result.y = -quaternion.y * num;
		result.z = -quaternion.z * num;
		result.w = quaternion.w * num;
	}

	public float length()
	{
		return (float) Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
	}

	public float lengthSquared()
	{
		return ((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
	}

	public static Quaternion lerp(Quaternion quaternion1, Quaternion quaternion2, float amount)
	{
		float num = amount;
		float num2 = 1f - num;
		Quaternion quaternion = Quaternion.identity();
		float num5 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z))
				+ (quaternion1.w * quaternion2.w);
		if (num5 >= 0f)
		{
			quaternion.x = (num2 * quaternion1.x) + (num * quaternion2.x);
			quaternion.y = (num2 * quaternion1.y) + (num * quaternion2.y);
			quaternion.z = (num2 * quaternion1.z) + (num * quaternion2.z);
			quaternion.w = (num2 * quaternion1.w) + (num * quaternion2.w);
		}
		else
		{
			quaternion.x = (num2 * quaternion1.x) - (num * quaternion2.x);
			quaternion.y = (num2 * quaternion1.y) - (num * quaternion2.y);
			quaternion.z = (num2 * quaternion1.z) - (num * quaternion2.z);
			quaternion.w = (num2 * quaternion1.w) - (num * quaternion2.w);
		}
		float num4 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z))
				+ (quaternion.w * quaternion.w);
		float num3 = 1f / ((float) Math.sqrt((double) num4));
		quaternion.x *= num3;
		quaternion.y *= num3;
		quaternion.z *= num3;
		quaternion.w *= num3;
		return quaternion;
	}

	public static void lerp(final Quaternion quaternion1, final Quaternion quaternion2, float amount, Quaternion result)
	{
		float num = amount;
		float num2 = 1f - num;
		float num5 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z))
				+ (quaternion1.w * quaternion2.w);
		if (num5 >= 0f)
		{
			result.x = (num2 * quaternion1.x) + (num * quaternion2.x);
			result.y = (num2 * quaternion1.y) + (num * quaternion2.y);
			result.z = (num2 * quaternion1.z) + (num * quaternion2.z);
			result.w = (num2 * quaternion1.w) + (num * quaternion2.w);
		}
		else
		{
			result.x = (num2 * quaternion1.x) - (num * quaternion2.x);
			result.y = (num2 * quaternion1.y) - (num * quaternion2.y);
			result.z = (num2 * quaternion1.z) - (num * quaternion2.z);
			result.w = (num2 * quaternion1.w) - (num * quaternion2.w);
		}
		float num4 = (((result.x * result.x) + (result.y * result.y)) + (result.z * result.z)) + (result.w * result.w);
		float num3 = 1f / ((float) Math.sqrt((double) num4));
		result.x *= num3;
		result.y *= num3;
		result.z *= num3;
		result.w *= num3;

	}

	public static Quaternion slerp(Quaternion quaternion1, Quaternion quaternion2, float amount)
	{
		float num2;
		float num3;
		Quaternion quaternion = Quaternion.identity();
		float num = amount;
		float num4 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z))
				+ (quaternion1.w * quaternion2.w);
		boolean flag = false;
		if (num4 < 0f)
		{
			flag = true;
			num4 = -num4;
		}
		if (num4 > 0.999999f)
		{
			num3 = 1f - num;
			num2 = flag ? -num : num;
		}
		else
		{
			float num5 = (float) Math.acos((double) num4);
			float num6 = (float) (1.0 / Math.sin((double) num5));
			num3 = ((float) Math.sin((double) ((1f - num) * num5))) * num6;
			num2 = flag ? (((float) -Math.sin((double) (num * num5))) * num6) : (((float) Math
					.sin((double) (num * num5))) * num6);
		}
		quaternion.x = (num3 * quaternion1.x) + (num2 * quaternion2.x);
		quaternion.y = (num3 * quaternion1.y) + (num2 * quaternion2.y);
		quaternion.z = (num3 * quaternion1.z) + (num2 * quaternion2.z);
		quaternion.w = (num3 * quaternion1.w) + (num2 * quaternion2.w);
		return quaternion;
	}

	public static void slerp(final Quaternion quaternion1, final Quaternion quaternion2, float amount, Quaternion result)
	{
		float num2;
		float num3;
		float num = amount;
		float num4 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z))
				+ (quaternion1.w * quaternion2.w);
		boolean flag = false;
		if (num4 < 0f)
		{
			flag = true;
			num4 = -num4;
		}
		if (num4 > 0.999999f)
		{
			num3 = 1f - num;
			num2 = flag ? -num : num;
		}
		else
		{
			float num5 = (float) Math.acos((double) num4);
			float num6 = (float) (1.0 / Math.sin((double) num5));
			num3 = ((float) Math.sin((double) ((1f - num) * num5))) * num6;
			num2 = flag ? (((float) -Math.sin((double) (num * num5))) * num6) : (((float) Math
					.sin((double) (num * num5))) * num6);
		}
		result.x = (num3 * quaternion1.x) + (num2 * quaternion2.x);
		result.y = (num3 * quaternion1.y) + (num2 * quaternion2.y);
		result.z = (num3 * quaternion1.z) + (num2 * quaternion2.z);
		result.w = (num3 * quaternion1.w) + (num2 * quaternion2.w);
	}

	public static Quaternion subtract(Quaternion quaternion1, Quaternion quaternion2)
	{
		Quaternion quaternion = Quaternion.identity();
		quaternion.x = quaternion1.x - quaternion2.x;
		quaternion.y = quaternion1.y - quaternion2.y;
		quaternion.z = quaternion1.z - quaternion2.z;
		quaternion.w = quaternion1.w - quaternion2.w;
		return quaternion;
	}

	public static void subtract(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
	{
		result.x = quaternion1.x - quaternion2.x;
		result.y = quaternion1.y - quaternion2.y;
		result.z = quaternion1.z - quaternion2.z;
		result.w = quaternion1.w - quaternion2.w;
	}

	public static Quaternion multiply(Quaternion quaternion1, Quaternion quaternion2)
	{
		Quaternion quaternion = Quaternion.identity();
		float x = quaternion1.x;
		float y = quaternion1.y;
		float z = quaternion1.z;
		float w = quaternion1.w;
		float num4 = quaternion2.x;
		float num3 = quaternion2.y;
		float num2 = quaternion2.z;
		float num = quaternion2.w;
		float num12 = (y * num2) - (z * num3);
		float num11 = (z * num4) - (x * num2);
		float num10 = (x * num3) - (y * num4);
		float num9 = ((x * num4) + (y * num3)) + (z * num2);
		quaternion.x = ((x * num) + (num4 * w)) + num12;
		quaternion.y = ((y * num) + (num3 * w)) + num11;
		quaternion.z = ((z * num) + (num2 * w)) + num10;
		quaternion.w = (w * num) - num9;
		return quaternion;
	}

	public static Quaternion multiply(Quaternion quaternion1, float scaleFactor)
	{
		Quaternion quaternion = Quaternion.identity();
		quaternion.x = quaternion1.x * scaleFactor;
		quaternion.y = quaternion1.y * scaleFactor;
		quaternion.z = quaternion1.z * scaleFactor;
		quaternion.w = quaternion1.w * scaleFactor;
		return quaternion;
	}

	public static void multiply(final Quaternion quaternion1, float scaleFactor, Quaternion result)
	{
		result.x = quaternion1.x * scaleFactor;
		result.y = quaternion1.y * scaleFactor;
		result.z = quaternion1.z * scaleFactor;
		result.w = quaternion1.w * scaleFactor;
	}

	public static void multiply(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
	{
		float x = quaternion1.x;
		float y = quaternion1.y;
		float z = quaternion1.z;
		float w = quaternion1.w;
		float num4 = quaternion2.x;
		float num3 = quaternion2.y;
		float num2 = quaternion2.z;
		float num = quaternion2.w;
		float num12 = (y * num2) - (z * num3);
		float num11 = (z * num4) - (x * num2);
		float num10 = (x * num3) - (y * num4);
		float num9 = ((x * num4) + (y * num3)) + (z * num2);
		result.x = ((x * num) + (num4 * w)) + num12;
		result.y = ((y * num) + (num3 * w)) + num11;
		result.z = ((z * num) + (num2 * w)) + num10;
		result.w = (w * num) - num9;
	}

	public static Quaternion negate(Quaternion quaternion)
	{
		return new Quaternion(-quaternion.x, -quaternion.y, -quaternion.z, -quaternion.w);
	}

	public static void negate(final Quaternion quaternion, Quaternion result)
	{
		result.x = -quaternion.x;
		result.y = -quaternion.y;
		result.z = -quaternion.z;
		result.w = -quaternion.w;
	}

	public void normalize()
	{
		float num2 = (((this.x * this.x) + (this.y * this.y)) + (this.z * this.z)) + (this.w * this.w);
		float num = 1f / ((float) Math.sqrt((double) num2));
		this.x *= num;
		this.y *= num;
		this.z *= num;
		this.w *= num;
	}

	public static Quaternion normalize(Quaternion quaternion)
	{
		Quaternion quaternion2 = Quaternion.identity();
		float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z))
				+ (quaternion.w * quaternion.w);
		float num = 1f / ((float) Math.sqrt((double) num2));
		quaternion2.x = quaternion.x * num;
		quaternion2.y = quaternion.y * num;
		quaternion2.z = quaternion.z * num;
		quaternion2.w = quaternion.w * num;
		return quaternion2;
	}

	public static void normalize(final Quaternion quaternion, Quaternion result)
	{
		float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z))
				+ (quaternion.w * quaternion.w);
		float num = 1f / ((float) Math.sqrt((double) num2));
		result.x = quaternion.x * num;
		result.y = quaternion.y * num;
		result.z = quaternion.z * num;
		result.w = quaternion.w * num;
	}
	
	@Override
	public String toString()
	{
		return "{X:" + x + " Y:" + y + " Z:" + z + " W:" + w + "}";
	}

	/// <summary>
	/// Gets a <see cref="Vector4"/> representation for this object.
	/// </summary>
	/// <returns>A <see cref="Vector4"/> representation for this object.</returns>
	public Vector4 toVector4()
	{
		return new Vector4(x, y, z, w);
	}
    
	// TODO: Turn some of those operators into methods like equals and not equals
	// public static Quaternion operator +(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// Quaternion quaternion;
	// quaternion.X = quaternion1.X + quaternion2.X;
	// quaternion.Y = quaternion1.Y + quaternion2.Y;
	// quaternion.Z = quaternion1.Z + quaternion2.Z;
	// quaternion.W = quaternion1.W + quaternion2.W;
	// return quaternion;
	// }
	//
	// public static Quaternion operator /(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// Quaternion quaternion;
	// float x = quaternion1.X;
	// float y = quaternion1.Y;
	// float z = quaternion1.Z;
	// float w = quaternion1.W;
	// float num14 = (((quaternion2.X * quaternion2.X) + (quaternion2.Y * quaternion2.Y)) +
	// (quaternion2.Z * quaternion2.Z)) + (quaternion2.W * quaternion2.W);
	// float num5 = 1f / num14;
	// float num4 = -quaternion2.X * num5;
	// float num3 = -quaternion2.Y * num5;
	// float num2 = -quaternion2.Z * num5;
	// float num = quaternion2.W * num5;
	// float num13 = (y * num2) - (z * num3);
	// float num12 = (z * num4) - (x * num2);
	// float num11 = (x * num3) - (y * num4);
	// float num10 = ((x * num4) + (y * num3)) + (z * num2);
	// quaternion.X = ((x * num) + (num4 * w)) + num13;
	// quaternion.Y = ((y * num) + (num3 * w)) + num12;
	// quaternion.Z = ((z * num) + (num2 * w)) + num11;
	// quaternion.W = (w * num) - num10;
	// return quaternion;
	// }
	//
	//
	// public static bool operator ==(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// return ((((quaternion1.X == quaternion2.X) && (quaternion1.Y == quaternion2.Y)) &&
	// (quaternion1.Z == quaternion2.Z)) && (quaternion1.W == quaternion2.W));
	// }
	//
	//
	// public static bool operator !=(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// if (((quaternion1.X == quaternion2.X) && (quaternion1.Y == quaternion2.Y)) && (quaternion1.Z
	// == quaternion2.Z))
	// {
	// return (quaternion1.W != quaternion2.W);
	// }
	// return true;
	// }
	//
	// public static Quaternion operator *(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// Quaternion quaternion;
	// float x = quaternion1.X;
	// float y = quaternion1.Y;
	// float z = quaternion1.Z;
	// float w = quaternion1.W;
	// float num4 = quaternion2.X;
	// float num3 = quaternion2.Y;
	// float num2 = quaternion2.Z;
	// float num = quaternion2.W;
	// float num12 = (y * num2) - (z * num3);
	// float num11 = (z * num4) - (x * num2);
	// float num10 = (x * num3) - (y * num4);
	// float num9 = ((x * num4) + (y * num3)) + (z * num2);
	// quaternion.X = ((x * num) + (num4 * w)) + num12;
	// quaternion.Y = ((y * num) + (num3 * w)) + num11;
	// quaternion.Z = ((z * num) + (num2 * w)) + num10;
	// quaternion.W = (w * num) - num9;
	// return quaternion;
	// }
	//
	// public static Quaternion operator *(Quaternion quaternion1, float scaleFactor)
	// {
	// Quaternion quaternion;
	// quaternion.X = quaternion1.X * scaleFactor;
	// quaternion.Y = quaternion1.Y * scaleFactor;
	// quaternion.Z = quaternion1.Z * scaleFactor;
	// quaternion.W = quaternion1.W * scaleFactor;
	// return quaternion;
	// }
	//
	// public static Quaternion operator -(Quaternion quaternion1, Quaternion quaternion2)
	// {
	// Quaternion quaternion;
	// quaternion.X = quaternion1.X - quaternion2.X;
	// quaternion.Y = quaternion1.Y - quaternion2.Y;
	// quaternion.Z = quaternion1.Z - quaternion2.Z;
	// quaternion.W = quaternion1.W - quaternion2.W;
	// return quaternion;
	//
	// }
	//
	// public static Quaternion operator -(Quaternion quaternion)
	// {
	// Quaternion quaternion2;
	// quaternion2.X = -quaternion.X;
	// quaternion2.Y = -quaternion.Y;
	// quaternion2.Z = -quaternion.Z;
	// quaternion2.W = -quaternion.W;
	// return quaternion2;
	// }

}
