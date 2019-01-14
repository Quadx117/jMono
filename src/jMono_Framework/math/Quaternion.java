package jMono_Framework.math;

// C# struct
/**
 * An efficient mathematical representation for three dimensional rotations.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Quaternion // implements IEquatable<Quaternion>
{
    // #region Private Fields

    private static final Quaternion identity = new Quaternion(0, 0, 0, 1);

    // #endregion

    // #region Public Fields

    /**
     * The x coordinate of this {@link Quaternion}.
     */
    public float x;

    /**
     * The y coordinate of this {@link Quaternion}.
     */
    public float y;

    /**
     * The z coordinate of this {@link Quaternion}.
     */
    public float z;

    /**
     * The rotation component of this {@link Quaternion}.
     */
    public float w;

    // #endregion

    // #region Constructors

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    public Quaternion()
    {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Quaternion(Quaternion q)
    {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    /**
     * Constructs a quaternion with X, Y, Z and W from four values.
     * 
     * @param x
     *        The x coordinate in 3d-space.
     * @param y
     *        The y coordinate in 3d-space.
     * @param z
     *        The z coordinate in 3d-space.
     * @param w
     *        The rotation component.
     */
    public Quaternion(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructs a quaternion with X, Y, Z from {@link Vector3} and rotation component from a
     * scalar.
     * 
     * @param value
     *        The x, y, z coordinates in 3d-space.
     * @param w
     *        The rotation component.
     */
    public Quaternion(Vector3 value, float w)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
        this.w = w;
    }

    /**
     * Constructs a quaternion from {@link Vector4}.
     * 
     * @param value
     *        The x, y, z coordinates in 3d-space and the rotation component.
     */
    public Quaternion(Vector4 value)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
        this.w = value.w;
    }

    // #endregion

    // #region Public Properties

    /**
     * Returns a quaternion representing no rotation.
     * 
     * @return A quaternion representing no rotation.
     */
    public static Quaternion identity()
    {
        return new Quaternion(identity);
    }

    // #endregion

    // #region Internal Properties

    protected String debugDisplayString()
    {
        if (this.equals(Quaternion.identity))
        {
            return "Identity";
        }

        return (this.x + " " + this.y + " " + this.z + " " + this.w);
    }

    // #endregion

    // #region Public Methods

    // #region Add

    /**
     * Creates a new {@link Quaternion} that contains the sum of two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @return The result of the quaternion addition.
     */
    public static Quaternion add(Quaternion quaternion1, Quaternion quaternion2)
    {
        Quaternion quaternion = new Quaternion();
        quaternion.x = quaternion1.x + quaternion2.x;
        quaternion.y = quaternion1.y + quaternion2.y;
        quaternion.z = quaternion1.z + quaternion2.z;
        quaternion.w = quaternion1.w + quaternion2.w;
        return quaternion;
    }

    /**
     * Creates a new {@link Quaternion} that contains the sum of two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param result
     *        The result of the quaternion addition as an output parameter.
     */
    public static void add(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
    {
        result.x = quaternion1.x + quaternion2.x;
        result.y = quaternion1.y + quaternion2.y;
        result.z = quaternion1.z + quaternion2.z;
        result.w = quaternion1.w + quaternion2.w;
    }

    // #endregion

    // #region Concatenate

    /**
     * Creates a new {@link Quaternion} that contains concatenation between two quaternion.
     * 
     * @param value1
     *        The first {@link Quaternion} to concatenate.
     * @param value2
     *        The second {@link Quaternion} to concatenate.
     * @return The result of rotation of {@code value1} followed by {@code value2} rotation.
     */
    public static Quaternion concatenate(Quaternion value1, Quaternion value2)
    {
        Quaternion quaternion = new Quaternion();

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

    /**
     * Creates a new {@link Quaternion} that contains concatenation between two quaternion.
     * 
     * @param value1
     *        The first {@link Quaternion} to concatenate.
     * @param value2
     *        The second {@link Quaternion} to concatenate.
     * @param result
     *        The result of rotation of {@code value1} followed by {@code value2} rotation as an
     *        output parameter.
     */
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

    // #endregion

    // #region Conjugate

    /**
     * Transforms this quaternion into its conjugated version.
     */
    public void conjugate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    /**
     * Creates a new {@link Quaternion} that contains conjugated version of the specified
     * quaternion.
     * 
     * @param value
     *        The quaternion which values will be used to create the conjugated version.
     * @return The conjugate version of the specified quaternion.
     */
    public static Quaternion conjugate(Quaternion value)
    {
        return new Quaternion(-value.x, -value.y, -value.z, value.w);
    }

    /**
     * Creates a new {@link Quaternion} that contains conjugated version of the specified
     * quaternion.
     * 
     * @param value
     *        The quaternion which values will be used to create the conjugated version.
     * @param result
     *        The conjugated version of the specified quaternion as an output parameter.
     */
    public static void conjugate(final Quaternion value, Quaternion result)
    {
        result.x = -value.x;
        result.y = -value.y;
        result.z = -value.z;
        result.w = value.w;
    }

    // #endregion

    // #region CreateFromAxisAngle

    /**
     * Creates a new {@link Quaternion} from the specified axis and angle.
     * 
     * @param axis
     *        The axis of rotation.
     * @param angle
     *        The angle in radians.
     * @return The new quaternion builded from axis and angle.
     */
    public static Quaternion createFromAxisAngle(Vector3 axis, float angle)
    {
        float half = angle * 0.5f;
        float sin = (float) Math.sin(half);
        float cos = (float) Math.cos(half);
        return new Quaternion(axis.x * sin, axis.y * sin, axis.z * sin, cos);
    }

    /**
     * Creates a new {@link Quaternion} from the specified axis and angle.
     * 
     * @param axis
     *        The axis of rotation.
     * @param angle
     *        The angle in radians.
     * @param result
     *        The new quaternion builded from axis and angle as an output parameter.
     */
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

    // #endregion

    // #region CreateFromRotationMatrix

    /**
     * Creates a new {@link Quaternion} from the specified {@link Matrix}.
     * 
     * @param matrix
     *        The rotation matrix.
     * @return A quaternion composed from the rotation part of the matrix.
     */
    public static Quaternion createFromRotationMatrix(Matrix matrix)
    {
        Quaternion quaternion = new Quaternion();
        float sqrt;
        float half;
        float scale = matrix.m11 + matrix.m22 + matrix.m33;

        if (scale > 0.0f)
        {
            sqrt = (float) Math.sqrt(scale + 1.0f);
            quaternion.w = sqrt * 0.5f;
            sqrt = 0.5f / sqrt;

            quaternion.x = (matrix.m23 - matrix.m32) * sqrt;
            quaternion.y = (matrix.m31 - matrix.m13) * sqrt;
            quaternion.z = (matrix.m12 - matrix.m21) * sqrt;

            return quaternion;
        }
        if ((matrix.m11 >= matrix.m22) && (matrix.m11 >= matrix.m33))
        {
            sqrt = (float) Math.sqrt(1.0f + matrix.m11 - matrix.m22 - matrix.m33);
            half = 0.5f / sqrt;

            quaternion.x = 0.5f * sqrt;
            quaternion.y = (matrix.m12 + matrix.m21) * half;
            quaternion.z = (matrix.m13 + matrix.m31) * half;
            quaternion.w = (matrix.m23 - matrix.m32) * half;

            return quaternion;
        }
        if (matrix.m22 > matrix.m33)
        {
            sqrt = (float) Math.sqrt(1.0f + matrix.m22 - matrix.m11 - matrix.m33);
            half = 0.5f / sqrt;

            quaternion.x = (matrix.m21 + matrix.m12) * half;
            quaternion.y = 0.5f * sqrt;
            quaternion.z = (matrix.m32 + matrix.m23) * half;
            quaternion.w = (matrix.m31 - matrix.m13) * half;

            return quaternion;
        }
        sqrt = (float) Math.sqrt(1.0f + matrix.m33 - matrix.m11 - matrix.m22);
        half = 0.5f / sqrt;

        quaternion.x = (matrix.m31 + matrix.m13) * half;
        quaternion.y = (matrix.m32 + matrix.m23) * half;
        quaternion.z = 0.5f * sqrt;
        quaternion.w = (matrix.m12 - matrix.m21) * half;

        return quaternion;
    }

    /**
     * Creates a new {@link Quaternion} from the specified {@link Matrix}.
     * 
     * @param matrix
     *        The rotation matrix.
     * @param result
     *        A quaternion composed from the rotation part of the matrix as an output parameter.
     */
    public static void createFromRotationMatrix(final Matrix matrix, Quaternion result)
    {
        float sqrt;
        float half;
        float scale = matrix.m11 + matrix.m22 + matrix.m33;

        if (scale > 0.0f)
        {
            sqrt = (float) Math.sqrt(scale + 1.0f);
            result.w = sqrt * 0.5f;
            sqrt = 0.5f / sqrt;

            result.x = (matrix.m23 - matrix.m32) * sqrt;
            result.y = (matrix.m31 - matrix.m13) * sqrt;
            result.z = (matrix.m12 - matrix.m21) * sqrt;
        }
        else if ((matrix.m11 >= matrix.m22) && (matrix.m11 >= matrix.m33))
        {
            sqrt = (float) Math.sqrt(1.0f + matrix.m11 - matrix.m22 - matrix.m33);
            half = 0.5f / sqrt;

            result.x = 0.5f * sqrt;
            result.y = (matrix.m12 + matrix.m21) * half;
            result.z = (matrix.m13 + matrix.m31) * half;
            result.w = (matrix.m23 - matrix.m32) * half;
        }
        else if (matrix.m22 > matrix.m33)
        {
            sqrt = (float) Math.sqrt(1.0f + matrix.m22 - matrix.m11 - matrix.m33);
            half = 0.5f / sqrt;

            result.x = (matrix.m21 + matrix.m12) * half;
            result.y = 0.5f * sqrt;
            result.z = (matrix.m32 + matrix.m23) * half;
            result.w = (matrix.m31 - matrix.m13) * half;
        }
        else
        {
            sqrt = (float) Math.sqrt(1.0f + matrix.m33 - matrix.m11 - matrix.m22);
            half = 0.5f / sqrt;

            result.x = (matrix.m31 + matrix.m13) * half;
            result.y = (matrix.m32 + matrix.m23) * half;
            result.z = 0.5f * sqrt;
            result.w = (matrix.m12 - matrix.m21) * half;
        }
    }

    // #endregion

    // #region CreateFromYawPitchRoll

    /**
     * Creates a new {@link Quaternion} from the specified yaw, pitch and roll angles.
     * 
     * @param yaw
     *        Yaw around the y axis in radians.
     * @param pitch
     *        Pitch around the x axis in radians.
     * @param roll
     *        Roll around the z axis in radians.
     * @return A new quaternion from the concatenated yaw, pitch, and roll angles.
     */
    public static Quaternion createFromYawPitchRoll(float yaw, float pitch, float roll)
    {
        float halfRoll = roll * 0.5f;
        float halfPitch = pitch * 0.5f;
        float halfYaw = yaw * 0.5f;

        float sinRoll = (float) Math.sin(halfRoll);
        float cosRoll = (float) Math.cos(halfRoll);
        float sinPitch = (float) Math.sin(halfPitch);
        float cosPitch = (float) Math.cos(halfPitch);
        float sinYaw = (float) Math.sin(halfYaw);
        float cosYaw = (float) Math.cos(halfYaw);

        return new Quaternion((cosYaw * sinPitch * cosRoll) + (sinYaw * cosPitch * sinRoll),
                              (sinYaw * cosPitch * cosRoll) - (cosYaw * sinPitch * sinRoll),
                              (cosYaw * cosPitch * sinRoll) - (sinYaw * sinPitch * cosRoll),
                              (cosYaw * cosPitch * cosRoll) + (sinYaw * sinPitch * sinRoll));
    }

    /**
     * Creates a new {@link Quaternion} from the specified yaw, pitch and roll angles.
     * 
     * @param yaw
     *        Yaw around the y axis in radians.
     * @param pitch
     *        Pitch around the x axis in radians.
     * @param roll
     *        Roll around the z axis in radians.
     * @param result
     *        A new quaternion from the concatenated yaw, pitch, and roll angles as an output
     *        parameter.
     */
    public static void createFromYawPitchRoll(float yaw, float pitch, float roll, Quaternion result)
    {
        float halfRoll = roll * 0.5f;
        float halfPitch = pitch * 0.5f;
        float halfYaw = yaw * 0.5f;

        float sinRoll = (float) Math.sin(halfRoll);
        float cosRoll = (float) Math.cos(halfRoll);
        float sinPitch = (float) Math.sin(halfPitch);
        float cosPitch = (float) Math.cos(halfPitch);
        float sinYaw = (float) Math.sin(halfYaw);
        float cosYaw = (float) Math.cos(halfYaw);

        result.x = (cosYaw * sinPitch * cosRoll) + (sinYaw * cosPitch * sinRoll);
        result.y = (sinYaw * cosPitch * cosRoll) - (cosYaw * sinPitch * sinRoll);
        result.z = (cosYaw * cosPitch * sinRoll) - (sinYaw * sinPitch * cosRoll);
        result.w = (cosYaw * cosPitch * cosRoll) + (sinYaw * sinPitch * sinRoll);
    }

    // #endregion

    // #region Divide

    // <returns></returns>
    /**
     * Divides a {@link Quaternion} by the other {@link Quaternion}.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Divisor {@link Quaternion}.
     * @return The result of dividing the quaternions.
     */
    public static Quaternion divide(Quaternion quaternion1, Quaternion quaternion2)
    {
        Quaternion quaternion = new Quaternion();
        float x = quaternion1.x;
        float y = quaternion1.y;
        float z = quaternion1.z;
        float w = quaternion1.w;
        float num14 = (((quaternion2.x * quaternion2.x) + (quaternion2.y * quaternion2.y)) + (quaternion2.z * quaternion2.z)) + (quaternion2.w * quaternion2.w);
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

    /**
     * Divides a {@link Quaternion} by the other {@link Quaternion}.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Divisor {@link Quaternion}.
     * @param result
     *        The result of dividing the quaternions as an output parameter.
     */
    public static void divide(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
    {
        float x = quaternion1.x;
        float y = quaternion1.y;
        float z = quaternion1.z;
        float w = quaternion1.w;
        float num14 = (((quaternion2.x * quaternion2.x) + (quaternion2.y * quaternion2.y)) + (quaternion2.z * quaternion2.z)) + (quaternion2.w * quaternion2.w);
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

    // #endregion

    // #region Dot

    /**
     * Returns a dot product of two quaternions.
     * 
     * @param quaternion1
     *        The first quaternion.
     * @param quaternion2
     *        The second quaternion.
     * @return The dot product of two quaternions.
     */
    public static float dot(Quaternion quaternion1, Quaternion quaternion2)
    {
        return ((((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w));
    }

    /**
     * Returns a dot product of two quaternions.
     * 
     * @param quaternion1
     *        The first quaternion.
     * @param quaternion2
     *        The second quaternion.
     * @param result
     *        The dot product of two quaternions as an output parameter.
     */
    public static void dot(final Quaternion quaternion1, final Quaternion quaternion2, float result)
    {
        result = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w);
    }

    // #endregion

    // #region Equals

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
        return this.equals((Quaternion) obj);
    }

    /**
     * Indicates whether the current instance is equal to the specified {@link Quaternion}.
     * 
     * @param other
     *        The {@code Quaternion} to compare.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(Quaternion other)
    {
        return this.x == other.x &&
               this.y == other.y &&
               this.z == other.z &&
               this.w == other.w;
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

    // #endregion

    /**
     * Returns the hash code of this {@link Quaternion}.
     * 
     * @return Hash code of this {@link Quaternion}.
     */
    @Override
    public int hashCode()
    {
        return (((Float.hashCode(x) + Float.hashCode(y)) + Float.hashCode(z)) + Float.hashCode(w));
    }

    // #region Inverse

    /**
     * Returns the inverse quaternion which represents the opposite rotation.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @return The inverse quaternion.
     */
    public static Quaternion inverse(Quaternion quaternion)
    {
        Quaternion quaternion2 = new Quaternion();
        float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num = 1f / num2;
        quaternion2.x = -quaternion.x * num;
        quaternion2.y = -quaternion.y * num;
        quaternion2.z = -quaternion.z * num;
        quaternion2.w = quaternion.w * num;
        return quaternion2;
    }

    /**
     * Returns the inverse quaternion which represents the opposite rotation.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @param result
     *        The inverse quaternion as an output parameter.
     */
    public static void inverse(final Quaternion quaternion, Quaternion result)
    {
        float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num = 1f / num2;
        result.x = -quaternion.x * num;
        result.y = -quaternion.y * num;
        result.z = -quaternion.z * num;
        result.w = quaternion.w * num;
    }

    // #endregion

    /**
     * Returns the magnitude of the quaternion components.
     * 
     * @return The magnitude of the quaternion components.
     */
    public float length()
    {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
    }

    /**
     * Returns the squared magnitude of the quaternion components.
     * 
     * @return The squared magnitude of the quaternion components.
     */
    public float lengthSquared()
    {
        return ((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w));
    }

    // #region Lerp

    /**
     * Performs a linear blend between two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param amount
     *        The blend amount where 0 returns {@code quaternion1} and 1 {@code quaternion2}.
     * @return The result of linear blending between two quaternions.
     */
    public static Quaternion lerp(Quaternion quaternion1, Quaternion quaternion2, float amount)
    {
        float num = amount;
        float num2 = 1f - num;
        Quaternion quaternion = new Quaternion();
        float num5 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w);
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
        float num4 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num3 = 1f / ((float) Math.sqrt((double) num4));
        quaternion.x *= num3;
        quaternion.y *= num3;
        quaternion.z *= num3;
        quaternion.w *= num3;
        return quaternion;
    }

    /**
     * Performs a linear blend between two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param amount
     *        The blend amount where 0 returns {@code quaternion1} and 1 {@code quaternion2}.
     * @param result
     *        The result of linear blending between two quaternions as an output parameter.
     */
    public static void lerp(final Quaternion quaternion1, final Quaternion quaternion2, float amount, Quaternion result)
    {
        float num = amount;
        float num2 = 1f - num;
        float num5 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w);
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

    // #endregion

    // #region Slerp

    /**
     * Performs a spherical linear blend between two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param amount
     *        The blend amount where 0 returns {@code quaternion1} and 1 {@code quaternion2}.
     * @return The result of spherical linear blending between two quaternions.
     */
    public static Quaternion slerp(Quaternion quaternion1, Quaternion quaternion2, float amount)
    {
        float num2;
        float num3;
        Quaternion quaternion = new Quaternion();
        float num = amount;
        float num4 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w);
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
            num2 = flag ? (((float) -Math.sin((double) (num * num5))) * num6) : (((float) Math.sin((double) (num * num5))) * num6);
        }
        quaternion.x = (num3 * quaternion1.x) + (num2 * quaternion2.x);
        quaternion.y = (num3 * quaternion1.y) + (num2 * quaternion2.y);
        quaternion.z = (num3 * quaternion1.z) + (num2 * quaternion2.z);
        quaternion.w = (num3 * quaternion1.w) + (num2 * quaternion2.w);
        return quaternion;
    }

    /**
     * Performs a spherical linear blend between two quaternions.
     * 
     * @param quaternion1
     * @param quaternion2
     * @param amount
     *        The blend amount where 0 returns {@code quaternion1} and 1 {@code quaternion2}.
     * @param result
     *        The result of spherical linear blending between two quaternions as an output
     *        parameter.
     */
    public static void slerp(final Quaternion quaternion1, final Quaternion quaternion2, float amount, Quaternion result)
    {
        float num2;
        float num3;
        float num = amount;
        float num4 = (((quaternion1.x * quaternion2.x) + (quaternion1.y * quaternion2.y)) + (quaternion1.z * quaternion2.z)) + (quaternion1.w * quaternion2.w);
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
            num2 = flag ? (((float) -Math.sin((double) (num * num5))) * num6) : (((float) Math.sin((double) (num * num5))) * num6);
        }
        result.x = (num3 * quaternion1.x) + (num2 * quaternion2.x);
        result.y = (num3 * quaternion1.y) + (num2 * quaternion2.y);
        result.z = (num3 * quaternion1.z) + (num2 * quaternion2.z);
        result.w = (num3 * quaternion1.w) + (num2 * quaternion2.w);
    }

    // #endregion

    // #region Subtract

    /**
     * Creates a new {@link Quaternion} that contains subtraction of one {@link Quaternion} from
     * another.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @return The result of the quaternion subtraction.
     */
    public static Quaternion subtract(Quaternion quaternion1, Quaternion quaternion2)
    {
        Quaternion quaternion = new Quaternion();
        quaternion.x = quaternion1.x - quaternion2.x;
        quaternion.y = quaternion1.y - quaternion2.y;
        quaternion.z = quaternion1.z - quaternion2.z;
        quaternion.w = quaternion1.w - quaternion2.w;
        return quaternion;
    }

    /**
     * Creates a new {@link Quaternion} that contains subtraction of one {@link Quaternion} from
     * another.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param result
     *        The result of the quaternion subtraction as an output parameter.
     */
    public static void subtract(final Quaternion quaternion1, final Quaternion quaternion2, Quaternion result)
    {
        result.x = quaternion1.x - quaternion2.x;
        result.y = quaternion1.y - quaternion2.y;
        result.z = quaternion1.z - quaternion2.z;
        result.w = quaternion1.w - quaternion2.w;
    }

    // #endregion

    // #region Multiply

    /**
     * Creates a new {@link Quaternion} that contains a multiplication of two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @return The result of the quaternion multiplication.
     */
    public static Quaternion multiply(Quaternion quaternion1, Quaternion quaternion2)
    {
        Quaternion quaternion = new Quaternion();
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

    /**
     * Creates a new {@link Quaternion} that contains a multiplication of {@link Quaternion} and a
     * scalar.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param scaleFactor
     *        Scalar value.
     * @return The result of the quaternion multiplication with a scalar.
     */
    public static Quaternion multiply(Quaternion quaternion1, float scaleFactor)
    {
        Quaternion quaternion = new Quaternion();
        quaternion.x = quaternion1.x * scaleFactor;
        quaternion.y = quaternion1.y * scaleFactor;
        quaternion.z = quaternion1.z * scaleFactor;
        quaternion.w = quaternion1.w * scaleFactor;
        return quaternion;
    }

    /**
     * Creates a new {@link Quaternion} that contains a multiplication of {@link Quaternion} and a
     * scalar.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param scaleFactor
     *        Scalar value.
     * @param result
     *        The result of the quaternion multiplication with a scalar as an output parameter.
     */
    public static void multiply(final Quaternion quaternion1, float scaleFactor, Quaternion result)
    {
        result.x = quaternion1.x * scaleFactor;
        result.y = quaternion1.y * scaleFactor;
        result.z = quaternion1.z * scaleFactor;
        result.w = quaternion1.w * scaleFactor;
    }

    /**
     * Creates a new {@link Quaternion} that contains a multiplication of two quaternions.
     * 
     * @param quaternion1
     *        Source {@link Quaternion}.
     * @param quaternion2
     *        Source {@link Quaternion}.
     * @param result
     *        The result of the quaternion multiplication as an output parameter.
     */
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

    // #endregion

    // #region Negate

    /**
     * Flips the sign of the all the quaternion components.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @return The result of the quaternion negation.
     */
    public static Quaternion negate(Quaternion quaternion)
    {
        return new Quaternion(-quaternion.x, -quaternion.y, -quaternion.z, -quaternion.w);
    }

    /**
     * Flips the sign of the all the quaternion components.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @param result
     *        The result of the quaternion negation as an output parameter.
     */
    public static void negate(final Quaternion quaternion, Quaternion result)
    {
        result.x = -quaternion.x;
        result.y = -quaternion.y;
        result.z = -quaternion.z;
        result.w = -quaternion.w;
    }

    // #endregion

    // #region Normalize

    /**
     * Scales the quaternion magnitude to unit length.
     */
    public void normalize()
    {
        float num2 = (((this.x * this.x) + (this.y * this.y)) + (this.z * this.z)) + (this.w * this.w);
        float num = 1f / ((float) Math.sqrt((double) num2));
        this.x *= num;
        this.y *= num;
        this.z *= num;
        this.w *= num;
    }

    /**
     * Scales the quaternion magnitude to unit length.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @return The unit length quaternion.
     */
    public static Quaternion normalize(Quaternion quaternion)
    {
        Quaternion quaternion2 = new Quaternion();
        float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num = 1f / ((float) Math.sqrt((double) num2));
        quaternion2.x = quaternion.x * num;
        quaternion2.y = quaternion.y * num;
        quaternion2.z = quaternion.z * num;
        quaternion2.w = quaternion.w * num;
        return quaternion2;
    }

    /**
     * Scales the quaternion magnitude to unit length.
     * 
     * @param quaternion
     *        Source {@link Quaternion}.
     * @param result
     *        The unit length quaternion an output parameter.
     */
    public static void normalize(final Quaternion quaternion, Quaternion result)
    {
        float num2 = (((quaternion.x * quaternion.x) + (quaternion.y * quaternion.y)) + (quaternion.z * quaternion.z)) + (quaternion.w * quaternion.w);
        float num = 1f / ((float) Math.sqrt((double) num2));
        result.x = quaternion.x * num;
        result.y = quaternion.y * num;
        result.z = quaternion.z * num;
        result.w = quaternion.w * num;
    }

    // #endregion

    /**
     * Returns a {@link String} representation of this {@link Quaternion} in the format:<br/>
     * {X:[{@link #x}] Y:[{@link #y}] Z:[{@link #z}] W:[{@link #w}]}
     * 
     * @return A {@link String} representation of this {@link Quaternion}.
     */
    @Override
    public String toString()
    {
        return "{X:" + x + " Y:" + y + " Z:" + z + " W:" + w + "}";
    }

    /**
     * Returns a {@link Vector4} representation for this object.
     * 
     * @return A {@link Vector4} representation for this object.
     */
    public Vector4 toVector4()
    {
        return new Vector4(x, y, z, w);
    }

    // #endregion

    // #region Operators

    /**
     * Adds the components of the specified {@link Quaternion} to the components of the current
     * instance.
     * 
     * @param other
     *        The other {@code Quaternion} to add to the current instance.
     * @return The result of the sum applied to this {@code Quaternion}.
     */
    public Quaternion add(Quaternion other)
    {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        this.w += other.w;
        return this;
    }

    /**
     * Divides the components of the current instance by the components of the specified
     * {@link Quaternion}.
     * 
     * @param other
     *        The other {@code Quaternion} to divide the current instance by.
     * @return The result of the division applied to this {@code Quaternion}.
     */
    public Quaternion divide(Quaternion other)
    {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float w = this.w;
        float num14 = (((other.x * other.x) + (other.y * other.y)) + (other.z * other.z)) + (other.w * other.w);
        float num5 = 1f / num14;
        float num4 = -other.x * num5;
        float num3 = -other.y * num5;
        float num2 = -other.z * num5;
        float num = other.w * num5;
        float num13 = (y * num2) - (z * num3);
        float num12 = (z * num4) - (x * num2);
        float num11 = (x * num3) - (y * num4);
        float num10 = ((x * num4) + (y * num3)) + (z * num2);
        this.x = ((x * num) + (num4 * w)) + num13;
        this.y = ((y * num) + (num3 * w)) + num12;
        this.z = ((z * num) + (num2 * w)) + num11;
        this.w = (w * num) - num10;
        return this;
    }

    /**
     * Multiplies the components of the current instance by the components of the specified
     * {@link Quaternion}.
     * 
     * @param other
     *        The other {@code Quaternion} to multiply to the current instance.
     * @return The result of the multiplication applied to this {@code Quaternion}.
     */
    public Quaternion multiply(Quaternion other)
    {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float w = this.w;
        float num4 = other.x;
        float num3 = other.y;
        float num2 = other.z;
        float num = other.w;
        float num12 = (y * num2) - (z * num3);
        float num11 = (z * num4) - (x * num2);
        float num10 = (x * num3) - (y * num4);
        float num9 = ((x * num4) + (y * num3)) + (z * num2);
        this.x = ((x * num) + (num4 * w)) + num12;
        this.y = ((y * num) + (num3 * w)) + num11;
        this.z = ((z * num) + (num2 * w)) + num10;
        this.w = (w * num) - num9;
        return this;
    }

    /**
     * Multiplies the components of the current instance by the specified scalar.
     * 
     * @param scaleFactor
     *        The scalar used to multiply each components of the current instance.
     * @return The result of the multiplication by the specified scalar.
     */
    public Quaternion multiply(float scaleFactor)
    {
        this.x *= scaleFactor;
        this.y *= scaleFactor;
        this.z *= scaleFactor;
        this.w *= scaleFactor;
        return this;
    }

    /**
     * Subtracts the components of the specified {@link Quaternion} from the components of the
     * current instance.
     * 
     * @param other
     *        The other {@code Quaternion} to subtract to the current instance.
     * @return The result of the subtraction applied to this {@code Quaternion}.
     */
    public Quaternion subtract(Quaternion other)
    {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        this.w -= other.w;
        return this;

    }

    /**
     * Flips the sign of the all the quaternion components.
     * 
     * @return The result of the quaternion negation.
     */
    public Quaternion negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }

    // #endregion
}
