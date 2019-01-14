package jMono_Framework.math;

// C# struct
/**
 * Defines a vector with four float components.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Vector4 // implements IEquatable<Vector4>
{
    private static Vector4 zero = new Vector4(0f, 0f, 0f, 0f);
    private static Vector4 one = new Vector4(1f, 1f, 1f, 1f);
    private static Vector4 unitX = new Vector4(1f, 0f, 0f, 0f);
    private static Vector4 unitY = new Vector4(0f, 1f, 0f, 0f);
    private static Vector4 unitZ = new Vector4(0f, 0f, 1f, 0f);
    private static Vector4 unitW = new Vector4(0f, 0f, 0f, 1f);

    /**
     * The x coordinate of this {@code Vector4}.
     */
    public float x;

    /**
     * The y coordinate of this {@code Vector4}.
     */
    public float y;

    /**
     * The z coordinate of this {@code Vector4}.
     */
    public float z;

    /**
     * The w coordinate of this {@code Vector4}.
     */
    public float w;

    /**
     * Returns a {@link Vector4} with components 0, 0, 0, 0.
     * 
     * @return A {@code Vector4} with components 0, 0, 0, 0.
     */
    public static Vector4 Zero()
    {
        return new Vector4(zero);
    }

    /**
     * Returns a {@link Vector4} with components 1, 1, 1, 1.
     * 
     * @return A {@code Vector4} with components 1, 1, 1, 1.
     */
    public static Vector4 One()
    {
        return new Vector4(one);
    }

    /**
     * Returns a {@link Vector4} with components 1, 0, 0, 0.
     * 
     * @return A {@code Vector4} with components 1, 0, 0, 0.
     */
    public static Vector4 UnitX()
    {
        return new Vector4(unitX);
    }

    /**
     * Returns a {@link Vector4} with components 0, 1, 0, 0.
     * 
     * @return A {@code Vector4} with components 0, 1, 0, 0.
     */
    public static Vector4 UnitY()
    {
        return new Vector4(unitY);
    }

    /**
     * Returns a {@link Vector4} with components 0, 0, 1, 0.
     * 
     * @return A {@code Vector4} with components 0, 0, 1, 0.
     */
    public static Vector4 UnitZ()
    {
        return new Vector4(unitZ);
    }

    /**
     * Returns a {@link Vector4} with components 0, 0, 0, 1.
     * 
     * @return A {@code Vector4} with components 0, 0, 0, 1.
     */
    public static Vector4 UnitW()
    {
        return new Vector4(unitW);
    }

    public String debugDisplayString()
    {
        return (this.x + "  " + this.y + "  " + this.z + "  " + this.w);
    }

    // Note: Added this since it is provided by default for struct in C#
    /**
     * Constructs a 4d vector with X, Y, Z and W set to 0.
     */
    public Vector4()
    {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    /**
     * Constructs a 4d vector with X, Y, Z and W from four values.
     * 
     * @param x
     *        The x coordinate in 4d-space.
     * @param y
     *        The y coordinate in 4d-space.
     * @param z
     *        The z coordinate in 4d-space.
     * @param w
     *        The w coordinate in 4d-space.
     */
    public Vector4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructs a 4d vector with X and Z from {@link Vector2} and Z and W from the scalars.
     * 
     * @param value
     *        The x and y coordinates in 4d-space.
     * @param z
     *        The z coordinate in 4d-space.
     * @param w
     *        The w coordinate in 4d-space.
     */
    public Vector4(Vector2 value, float z, float w)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructs a 4d vector with X, Y, Z from {@link Vector3} and W from a scalar.
     * 
     * @param value
     *        The x, y and z coordinates in 4d-space.
     * @param w
     *        The w coordinate in 4d-space.
     */
    public Vector4(Vector3 value, float w)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
        this.w = w;
    }

    /**
     * Constructs a 4d vector with X, Y, Z and W set to the same value as the supplied
     * {@link Vector4}.
     * 
     * @param value
     *        The x, y, z and w coordinates in 4d-space.
     */
    public Vector4(Vector4 value)
    {
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
        this.w = value.w;
    }

    /**
     * Constructs a 4d vector with X, Y, Z and W set to the same value.
     * 
     * @param value
     *        The x, y, z and w coordinates in 4d-space.
     */
    public Vector4(float value)
    {
        this.x = value;
        this.y = value;
        this.z = value;
        this.w = value;
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
    public static Vector4 add(Vector4 value1, Vector4 value2)
    {
        Vector4 result = new Vector4(value1);
        result.x += value2.x;
        result.y += value2.y;
        result.z += value2.z;
        result.w += value2.w;
        return result;
    }

    /**
     * Performs vector addition on {@code value1} and {@code value2}, storing the result
     * of the addition in {@code result}.
     * 
     * @param value1
     *        The first vector to add.
     * @param value2
     *        The second vector to add.
     * @param result
     *        The result of the vector addition.
     */
    public static void add(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.x = value1.x + value2.x;
        result.y = value1.y + value2.y;
        result.z = value1.z + value2.z;
        result.w = value1.w + value2.w;
    }

    /**
     * Creates a new {@link Vector4} that contains the cartesian coordinates of a vector specified
     * in barycentric coordinates and relative to 4d-triangle.
     * 
     * @param value1
     *        The first vector of 4d-triangle.
     * @param value2
     *        The second vector of 4d-triangle.
     * @param value3
     *        The third vector of 4d-triangle.
     * @param amount1
     *        Barycentric scalar {@code b2} which represents a weighting factor towards
     *        second vector of 4d-triangle.
     * @param amount2
     *        Barycentric scalar {@code b3} which represents a weighting factor towards
     *        third vector of 4d-triangle.
     * @return The cartesian translation of barycentric coordinates.
     */
    public static Vector4 barycentric(Vector4 value1, Vector4 value2, Vector4 value3, float amount1, float amount2)
    {
        return new Vector4(MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2),
                           MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2),
                           MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2),
                           MathHelper.barycentric(value1.w, value2.w, value3.w, amount1, amount2));
    }

    /**
     * Creates a new {@link Vector4} that contains the cartesian coordinates of a vector specified
     * in barycentric coordinates and relative to 4d-triangle.
     * 
     * @param value1
     *        The first vector of 4d-triangle.
     * @param value2
     *        The second vector of 4d-triangle.
     * @param value3
     *        The third vector of 4d-triangle.
     * @param amount1
     *        Barycentric scalar {@code b2} which represents a weighting factor towards
     *        second vector of 4d-triangle.
     * @param amount2
     *        Barycentric scalar {@code b3} which represents a weighting factor towards
     *        third vector of 4d-triangle.
     * @param result
     *        The cartesian translation of barycentric coordinates as an output parameter.
     */
    public static void barycentric(final Vector4 value1, final Vector4 value2, final Vector4 value3, float amount1,
                                   float amount2, Vector4 result)
    {
        result.x = MathHelper.barycentric(value1.x, value2.x, value3.x, amount1, amount2);
        result.y = MathHelper.barycentric(value1.y, value2.y, value3.y, amount1, amount2);
        result.z = MathHelper.barycentric(value1.z, value2.z, value3.z, amount1, amount2);
        result.w = MathHelper.barycentric(value1.w, value2.w, value3.w, amount1, amount2);
    }

    /**
     * Creates a new {@link Vector4} that contains CatmullRom interpolation of the specified
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
    public static Vector4 catmullRom(Vector4 value1, Vector4 value2, Vector4 value3, Vector4 value4, float amount)
    {
        return new Vector4(MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount),
                           MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount),
                           MathHelper.catmullRom(value1.z, value2.z, value3.z, value4.z, amount),
                           MathHelper.catmullRom(value1.w, value2.w, value3.w, value4.w, amount));
    }

    /**
     * Creates a new {@link Vector4} that contains CatmullRom interpolation of the specified
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
    public static void catmullRom(final Vector4 value1, final Vector4 value2, final Vector4 value3,
                                  final Vector4 value4, float amount, Vector4 result)
    {
        result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
        result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
        result.z = MathHelper.catmullRom(value1.z, value2.z, value3.z, value4.z, amount);
        result.w = MathHelper.catmullRom(value1.w, value2.w, value3.w, value4.w, amount);
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
    public static Vector4 clamp(Vector4 value1, Vector4 min, Vector4 max)
    {
        return new Vector4(MathHelper.clamp(value1.x, min.x, max.x),
                           MathHelper.clamp(value1.y, min.y, max.y),
                           MathHelper.clamp(value1.z, min.z, max.z),
                           MathHelper.clamp(value1.w, min.w, max.w));
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
    public static void clamp(final Vector4 value1, final Vector4 min, final Vector4 max, Vector4 result)
    {
        result.x = MathHelper.clamp(value1.x, min.x, max.x);
        result.y = MathHelper.clamp(value1.y, min.y, max.y);
        result.z = MathHelper.clamp(value1.z, min.z, max.z);
        result.w = MathHelper.clamp(value1.w, min.w, max.w);
    }

    /**
     * Returns the distance between two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The distance between two vectors.
     */
    public static float distance(Vector4 value1, Vector4 value2)
    {
        return (float) Math.sqrt(distanceSquared(value1, value2));
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // <summary>
    // Returns the distance between two vectors.
    // </summary>
    // <param name="value1">The first vector.</param>
    // <param name="value2">The second vector.</param>
    // <param name="result">The distance between two vectors as an output parameter.</param>
    // public static void distance(final Vector4 value1, final Vector4 value2, float result) {
    // result = (float)Math.sqrt(DistanceSquared(value1, value2));
    // }

    /**
     * Returns the squared distance between two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The squared distance between two vectors.
     */
    public static float distanceSquared(Vector4 value1, Vector4 value2)
    {
        return (value1.w - value2.w) * (value1.w - value2.w) +
               (value1.x - value2.x) * (value1.x - value2.x) +
               (value1.y - value2.y) * (value1.y - value2.y) +
               (value1.z - value2.z) * (value1.z - value2.z);
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // <summary>
    // Returns the squared distance between two vectors.
    // </summary>
    // <param name="value1">The first vector.</param>
    // <param name="value2">The second vector.</param>
    // <param name="result">The squared distance between two vectors as an output parameter.</param>
    // public static void distanceSquared(final Vector4 value1, final Vector4 value2, float
    // result) {
    // result = (value1.W - value2.W) * (value1.W - value2.W) +
    // (value1.X - value2.X) * (value1.X - value2.X) +
    // (value1.Y - value2.Y) * (value1.Y - value2.Y) +
    // (value1.Z - value2.Z) * (value1.Z - value2.Z);
    // }

    /**
     * Divides the components of a {@link Vector4} by the components of another {@link Vector4}.
     * 
     * @param value1
     *        Source {@code Vector4}.
     * @param value2
     *        Divisor {@code Vector4}.
     * @return The result of dividing the vectors.
     */
    public static Vector4 divide(Vector4 value1, Vector4 value2)
    {
        Vector4 result = new Vector4(value1);
        result.w /= value2.w;
        result.x /= value2.x;
        result.y /= value2.y;
        result.z /= value2.z;
        return result;
    }

    /**
     * Divides the components of a {@link Vector4} by a scalar.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param divider
     *        Divisor scalar.
     * @return The result of dividing a vector by a scalar.
     */
    public static Vector4 divide(Vector4 value1, float divider)
    {
        float factor = 1f / divider;
        Vector4 result = new Vector4(value1);
        result.w *= factor;
        result.x *= factor;
        result.y *= factor;
        result.z *= factor;
        return result;
    }

    /**
     * Divides the components of a {@link Vector4} by a scalar.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param divider
     *        Divisor scalar.
     * @param result
     *        The result of dividing a vector by a scalar as an output parameter.
     */
    public static void divide(final Vector4 value1, float divider, Vector4 result)
    {
        float factor = 1f / divider;
        result.w = value1.w * factor;
        result.x = value1.x * factor;
        result.y = value1.y * factor;
        result.z = value1.z * factor;
    }

    /**
     * Divides the components of a {@link Vector4} by the components of another {@link Vector4}.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param value2
     *        Divisor {@link Vector4}.
     * @param result
     *        The result of dividing the vectors as an output parameter.
     */
    public static void divide(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.w = value1.w / value2.w;
        result.x = value1.x / value2.x;
        result.y = value1.y / value2.y;
        result.z = value1.z / value2.z;
    }

    /**
     * Returns a dot product of two vectors.
     * 
     * @param vector1
     *        The first vector.
     * @param vector2
     *        The second vector.
     * @return The dot product of two vectors.
     */
    public static float dot(Vector4 vector1, Vector4 vector2)
    {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z + vector1.w * vector2.w;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // <summary>
    // Returns a dot product of two vectors.
    // </summary>
    // <param name="value1">The first vector.</param>
    // <param name="value2">The second vector.</param>
    // <param name="result">The dot product of two vectors as an output parameter.</param>
    // public static void dot(final Vector4 vector1, final Vector4 vector2, float result)
    // {
    // result = vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z + vector1.w * vector2.w;
    // }

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
        return this.equals((Vector4) obj);
    }

    /**
     * Compares whether current instance is equal to specified {@link Vector4}.
     * 
     * @param other
     *        The {@link Vector4} to compare.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(Vector4 other)
    {
        return this.w == other.w &&
               this.x == other.x &&
               this.y == other.y &&
               this.z == other.z;
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
     * Gets the hash code of this {@link Vector4}.
     * 
     * @return Hash code of this {@link Vector4}.
     */
    @Override
    public int hashCode()
    {
        int hashCode = Float.hashCode(w);
        hashCode = (hashCode * 397) ^ Float.hashCode(x);
        hashCode = (hashCode * 397) ^ Float.hashCode(y);
        hashCode = (hashCode * 397) ^ Float.hashCode(z);
        return hashCode;
    }

    /**
     * Creates a new {@link Vector4} that contains hermite spline interpolation.
     * 
     * @param value1
     *        The first position vector.
     * @param tangent1
     *        The first tangent vector.
     * @param value2
     *        The second position vector.
     * @param tangent2
     *        The second tangent vector.
     * @param amount
     *        Weighting factor.
     * @return The hermite spline interpolation vector.
     */
    public static Vector4 hermite(Vector4 value1, Vector4 tangent1, Vector4 value2, Vector4 tangent2, float amount)
    {
        Vector4 result = new Vector4();
        hermite(value1, tangent1, value2, tangent2, amount, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains hermite spline interpolation.
     * 
     * @param value1
     *        The first position vector.
     * @param tangent1
     *        The first tangent vector.
     * @param value2
     *        The second position vector.
     * @param tangent2
     *        The second tangent vector.
     * @param amount
     *        Weighting factor.
     * @param result
     *        The hermite spline interpolation vector as an output parameter.
     */
    public static void hermite(final Vector4 value1, final Vector4 tangent1, final Vector4 value2,
                               final Vector4 tangent2, float amount, Vector4 result)
    {
        result.w = MathHelper.hermite(value1.w, tangent1.w, value2.w, tangent2.w, amount);
        result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
        result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
        result.z = MathHelper.hermite(value1.z, tangent1.z, value2.z, tangent2.z, amount);
    }

    /**
     * Returns the length of this {@link Vector4}.
     * 
     * @return The length of this {@link Vector4}.
     */
    public float length()
    {
        float result = distanceSquared(this, Vector4.Zero());
        return (float) Math.sqrt(result);
    }

    /**
     * Returns the squared length of this {@link Vector4}.
     * 
     * @return The squared length of this {@link Vector4}.
     */
    public float lengthSquared()
    {
        float result = distanceSquared(this, Vector4.Zero());
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains linear interpolation of the specified vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @return The result of linear interpolation of the specified vectors.
     */
    public static Vector4 lerp(Vector4 value1, Vector4 value2, float amount)
    {
        return new Vector4(MathHelper.lerp(value1.x, value2.x, amount),
                           MathHelper.lerp(value1.y, value2.y, amount),
                           MathHelper.lerp(value1.z, value2.z, amount),
                           MathHelper.lerp(value1.w, value2.w, amount));
    }

    /**
     * Creates a new {@link Vector4} that contains linear interpolation of the specified vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @param result
     *        The result of linear interpolation of the specified vectors as an output parameter.
     */
    public static void lerp(final Vector4 value1, final Vector4 value2, float amount, Vector4 result)
    {
        result.x = MathHelper.lerp(value1.x, value2.x, amount);
        result.y = MathHelper.lerp(value1.y, value2.y, amount);
        result.z = MathHelper.lerp(value1.z, value2.z, amount);
        result.w = MathHelper.lerp(value1.w, value2.w, amount);
    }

    /**
     * Creates a new {@link Vector4} that contains linear interpolation of the specified vectors.
     * Uses {@link MathHelper#lerPrecise} on MathHelper for the interpolation.
     * Less efficient but more precise compared to {@link Vector4#lerp(Vector4, Vector4, float)}.
     * See remarks section of {@link MathHelper#lerPrecise} on MathHelper for more info.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @return The result of linear interpolation of the specified vectors.
     */
    public static Vector4 lerpPrecise(Vector4 value1, Vector4 value2, float amount)
    {
        return new Vector4(MathHelper.lerpPrecise(value1.x, value2.x, amount),
                           MathHelper.lerpPrecise(value1.y, value2.y, amount),
                           MathHelper.lerpPrecise(value1.z, value2.z, amount),
                           MathHelper.lerpPrecise(value1.w, value2.w, amount));
    }

    /**
     * Creates a new {@link Vector4} that contains linear interpolation of the specified vectors.
     * Uses {@link MathHelper#lerPrecise} on MathHelper for the interpolation.
     * Less efficient but more precise compared to
     * {@link Vector4#lerp(Vector4, Vector4, float, Vector4)}.
     * See remarks section of {@link MathHelper#lerPrecise} on MathHelper for more info.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @param result
     *        The result of linear interpolation of the specified vectors as an output parameter.
     */
    public static void lerpPrecise(final Vector4 value1, final Vector4 value2, float amount, Vector4 result)
    {
        result.x = MathHelper.lerpPrecise(value1.x, value2.x, amount);
        result.y = MathHelper.lerpPrecise(value1.y, value2.y, amount);
        result.z = MathHelper.lerpPrecise(value1.z, value2.z, amount);
        result.w = MathHelper.lerpPrecise(value1.w, value2.w, amount);
    }

    /**
     * Creates a new {@link Vector4} that contains a maximal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The {@link Vector4} with maximal values from the two vectors.
     */
    public static Vector4 max(Vector4 value1, Vector4 value2)
    {
        return new Vector4(MathHelper.max(value1.x, value2.x),
                           MathHelper.max(value1.y, value2.y),
                           MathHelper.max(value1.z, value2.z),
                           MathHelper.max(value1.w, value2.w));
    }

    /**
     * Creates a new {@link Vector4} that contains a maximal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param result
     *        The {@link Vector4} with maximal values from the two vectors as an output parameter.
     */
    public static void max(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.x = MathHelper.max(value1.x, value2.x);
        result.y = MathHelper.max(value1.y, value2.y);
        result.z = MathHelper.max(value1.z, value2.z);
        result.w = MathHelper.max(value1.w, value2.w);
    }

    /**
     * Creates a new {@link Vector4} that contains a minimal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The {@link Vector4} with minimal values from the two vectors.
     */
    public static Vector4 min(Vector4 value1, Vector4 value2)
    {
        return new Vector4(MathHelper.min(value1.x, value2.x),
                           MathHelper.min(value1.y, value2.y),
                           MathHelper.min(value1.z, value2.z),
                           MathHelper.min(value1.w, value2.w));
    }

    /**
     * Creates a new {@link Vector4} that contains a minimal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param result
     *        The {@link Vector4} with minimal values from the two vectors as an output parameter.
     */
    public static void min(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.x = MathHelper.min(value1.x, value2.x);
        result.y = MathHelper.min(value1.y, value2.y);
        result.z = MathHelper.min(value1.z, value2.z);
        result.w = MathHelper.min(value1.w, value2.w);
    }

    /**
     * Creates a new {@link Vector4} that contains a multiplication of two vectors.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param value2
     *        Source {@link Vector4}.
     * @return The result of the vector multiplication.
     */
    public static Vector4 multiply(Vector4 value1, Vector4 value2)
    {
        Vector4 result = new Vector4(value1);
        result.w *= value2.w;
        result.x *= value2.x;
        result.y *= value2.y;
        result.z *= value2.z;
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a multiplication of {@link Vector4} and a scalar.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param scaleFactor
     *        Scalar value.
     * @return The result of the vector multiplication with a scalar.
     */
    public static Vector4 multiply(Vector4 value1, float scaleFactor)
    {
        Vector4 result = new Vector4(value1);
        result.w *= scaleFactor;
        result.x *= scaleFactor;
        result.y *= scaleFactor;
        result.z *= scaleFactor;
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a multiplication of {@link Vector4} and a scalar.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param scaleFactor
     *        Scalar value.
     * @param result
     *        The result of the multiplication with a scalar as an output parameter.
     */
    public static void multiply(final Vector4 value1, float scaleFactor, Vector4 result)
    {
        result.w = value1.w * scaleFactor;
        result.x = value1.x * scaleFactor;
        result.y = value1.y * scaleFactor;
        result.z = value1.z * scaleFactor;
    }

    /**
     * Creates a new {@link Vector4} that contains a multiplication of two vectors.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param value2
     *        Source {@link Vector4}.
     * @param result
     *        The result of the vector multiplication as an output parameter.
     */
    public static void multiply(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.w = value1.w * value2.w;
        result.x = value1.x * value2.x;
        result.y = value1.y * value2.y;
        result.z = value1.z * value2.z;
    }

    /**
     * Creates a new {@link Vector4} that contains the specified vector inversion.
     * 
     * @param value
     *        Source {@link Vector4}.
     * @return The result of the vector inversion.
     */
    public static Vector4 negate(Vector4 value)
    {
        Vector4 result = new Vector4(-value.x, -value.y, -value.z, -value.w);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains the specified vector inversion.
     * 
     * @param value
     *        Source {@link Vector4}.
     * @param result
     *        The result of the vector inversion as an output parameter.
     */
    public static void negate(final Vector4 value, Vector4 result)
    {
        result.x = -value.x;
        result.y = -value.y;
        result.z = -value.z;
        result.w = -value.w;
    }

    /**
     * Turns this {@link Vector4} to a unit vector with the same direction.
     */
    public void normalize()
    {
        normalize(this, this);
    }

    /**
     * Creates a new {@link Vector4} that contains a normalized values from another vector.
     * 
     * @param vector
     *        Source {@link Vector4}.
     * @return Unit vector.
     */
    public static Vector4 normalize(Vector4 vector)
    {
        float val = 1.0f / (float) Math.sqrt((vector.x * vector.x) + (vector.y * vector.y) + (vector.z * vector.z) + (vector.w * vector.w));
        return new Vector4(vector.x * val, vector.y * val, vector.z * val, vector.w * val);
    }

    /**
     * Creates a new {@link Vector4} that contains a normalized values from another vector.
     * 
     * @param vector
     *        Source {@link Vector4}.
     * @param result
     *        Unit vector as an output parameter.
     */
    public static void normalize(final Vector4 vector, Vector4 result)
    {
        float factor = distanceSquared(vector, zero);
        factor = 1f / (float) Math.sqrt(factor);

        result.w = vector.w * factor;
        result.x = vector.x * factor;
        result.y = vector.y * factor;
        result.z = vector.z * factor;
    }

    /**
     * Creates a new {@link Vector4} that contains cubic interpolation of the specified vectors.
     * 
     * @param value1
     *        Source {@link Vector4}
     * @param value2
     *        Source {@link Vector4}.
     * @param amount
     *        Weighting value.
     * @return Cubic interpolation of the specified vectors.
     */
    public static Vector4 smoothStep(Vector4 value1, Vector4 value2, float amount)
    {
        return new Vector4(MathHelper.smoothStep(value1.x, value2.x, amount),
                           MathHelper.smoothStep(value1.y, value2.y, amount),
                           MathHelper.smoothStep(value1.z, value2.z, amount),
                           MathHelper.smoothStep(value1.w, value2.w, amount));
    }

    /**
     * Creates a new {@link Vector4} that contains cubic interpolation of the specified vectors.
     * 
     * @param value1
     *        Source {@link Vector4}.
     * @param value2
     *        Source {@link Vector4}.
     * @param amount
     *        Weighting value.
     * @param result
     *        Cubic interpolation of the specified vectors as an output parameter.
     */
    public static void smoothStep(final Vector4 value1, final Vector4 value2, float amount, Vector4 result)
    {
        result.x = MathHelper.smoothStep(value1.x, value2.x, amount);
        result.y = MathHelper.smoothStep(value1.y, value2.y, amount);
        result.z = MathHelper.smoothStep(value1.z, value2.z, amount);
        result.w = MathHelper.smoothStep(value1.w, value2.w, amount);
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
    public static Vector4 subtract(Vector4 value1, Vector4 value2)
    {
        Vector4 result = new Vector4(value1);
        result.w -= value2.w;
        result.x -= value2.x;
        result.y -= value2.y;
        result.z -= value2.z;
        return result;
    }

    /**
     * Performs vector subtraction on {@code value1} and {@code value2}.
     * 
     * @param value1
     *        The vector to be subtracted from.
     * @param value2
     *        The vector to be subtracted from {@code value1}.
     * @param result
     *        The result of the vector subtraction.
     */
    public static void subtract(final Vector4 value1, final Vector4 value2, Vector4 result)
    {
        result.w = value1.w - value2.w;
        result.x = value1.x - value2.x;
        result.y = value1.y - value2.y;
        result.z = value1.z - value2.z;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 2d-vector
     * by the specified {@link Matrix}.
     * 
     * @param position
     *        Source {@link Vector2}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector2 position, Matrix matrix)
    {
        Vector4 result = new Vector4();
        transform(position, matrix, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 2d-vector
     * by the specified {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector2}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector2 value, Quaternion rotation)
    {
        Vector4 result = new Vector4();
        transform(value, rotation, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 3d-vector
     * by the specified {@link Matrix}.
     * 
     * @param position
     *        Source {@link Vector3}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector3 position, Matrix matrix)
    {
        Vector4 result = new Vector4();
        transform(position, matrix, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 3d-vector by the specified
     * {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector3}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector3 value, Quaternion rotation)
    {
        Vector4 result = new Vector4();
        transform(value, rotation, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 4d-vector
     * by the specified {@link Matrix}.
     * 
     * @param vector
     *        Source {@link Vector4}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector4 vector, Matrix matrix)
    {
        Vector4 result = new Vector4();
        transform(vector, matrix, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 4d-vector
     * by the specified {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector4}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @return Transformed {@link Vector4}.
     */
    public static Vector4 transform(Vector4 value, Quaternion rotation)
    {
        Vector4 result = new Vector4();
        transform(value, rotation, result);
        return result;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 2d-vector
     * by the specified {@link Matrix}.
     * 
     * @param position
     *        Source {@link Vector2}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector2 position, final Matrix matrix, Vector4 result)
    {
        result.x = (position.x * matrix.m11) + (position.y * matrix.m21) + matrix.m41;
        result.y = (position.x * matrix.m12) + (position.y * matrix.m22) + matrix.m42;
        result.z = (position.x * matrix.m13) + (position.y * matrix.m23) + matrix.m43;
        result.w = (position.x * matrix.m14) + (position.y * matrix.m24) + matrix.m44;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 2d-vector
     * by the specified {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector2}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector2 value, final Quaternion rotation, Vector4 result)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 3d-vector by the specified
     * {@link Matrix}.
     * 
     * @param position
     *        Source {@link Vector3}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector3 position, final Matrix matrix, Vector4 result)
    {
        result.x = (position.x * matrix.m11) + (position.y * matrix.m21) + (position.z * matrix.m31) + matrix.m41;
        result.y = (position.x * matrix.m12) + (position.y * matrix.m22) + (position.z * matrix.m32) + matrix.m42;
        result.z = (position.x * matrix.m13) + (position.y * matrix.m23) + (position.z * matrix.m33) + matrix.m43;
        result.w = (position.x * matrix.m14) + (position.y * matrix.m24) + (position.z * matrix.m34) + matrix.m44;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 3d-vector
     * by the specified {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector3}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector3 value, final Quaternion rotation, Vector4 result)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 4d-vector
     * by the specified {@link Matrix}.
     * 
     * @param vector
     *        Source {@link Vector4}.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector4 vector, final Matrix matrix, Vector4 result)
    {
        float x = (vector.x * matrix.m11) + (vector.y * matrix.m21) + (vector.z * matrix.m31) + (vector.w * matrix.m41);
        float y = (vector.x * matrix.m12) + (vector.y * matrix.m22) + (vector.z * matrix.m32) + (vector.w * matrix.m42);
        float z = (vector.x * matrix.m13) + (vector.y * matrix.m23) + (vector.z * matrix.m33) + (vector.w * matrix.m43);
        float w = (vector.x * matrix.m14) + (vector.y * matrix.m24) + (vector.z * matrix.m34) + (vector.w * matrix.m44);
        result.x = x;
        result.y = y;
        result.z = z;
        result.w = w;
    }

    /**
     * Creates a new {@link Vector4} that contains a transformation of 4d-vector
     * by the specified {@link Quaternion}.
     * 
     * @param value
     *        Source {@link Vector4}.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param result
     *        Transformed {@link Vector4} as an output parameter.
     */
    public static void transform(final Vector4 value, final Quaternion rotation, Vector4 result)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Apply transformation on vectors within array of {@link Vector4} by the
     * specified {@link Matrix} and places the results in an another array.
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
     *        The starting index in the destination array, where the
     *        first {@link Vector4} should be written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector4[] sourceArray,
                                 int sourceIndex,
                                 final Matrix matrix,
                                 Vector4[] destinationArray,
                                 int destinationIndex,
                                 int length)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (sourceArray.length < sourceIndex + length)
            throw new IllegalArgumentException("Source array length is lesser than sourceIndex + length");
        if (destinationArray.length < destinationIndex + length)
            throw new IllegalArgumentException("Destination array length is lesser than destinationIndex + length");

        for (int i = 0; i < length; i++)
        {
            Vector4 value = sourceArray[sourceIndex + i];
            destinationArray[destinationIndex + i] = transform(value, matrix);
        }
    }

    /**
     * Apply transformation on vectors within array of {@link Vector4} by the
     * specified {@link Quaternion} and places the results in an another array.
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
     *        The starting index in the destination array, where the
     *        first {@link Vector4} should be written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector4[] sourceArray,
                                 int sourceIndex,
                                 final Quaternion rotation,
                                 Vector4[] destinationArray,
                                 int destinationIndex,
                                 int length)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (sourceArray.length < sourceIndex + length)
            throw new IllegalArgumentException("Source array length is lesser than sourceIndex + length");
        if (destinationArray.length < destinationIndex + length)
            throw new IllegalArgumentException("Destination array length is lesser than destinationIndex + length");

        for (int i = 0; i < length; i++)
        {
            Vector4 value = sourceArray[sourceIndex + i];
            destinationArray[destinationIndex + i] = transform(value, rotation);
        }
    }

    /**
     * Apply transformation on all vectors within array of {@link Vector4} by the
     * specified {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param matrix
     *        The transformation {@link Matrix}.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector4[] sourceArray, final Matrix matrix, Vector4[] destinationArray)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        for (int i = 0; i < sourceArray.length; i++)
        {
            Vector4 value = sourceArray[i];
            destinationArray[i] = transform(value, matrix);
        }
    }

    /**
     * Apply transformation on all vectors within array of {@link Vector4} by the
     * specified {@link Quaternion} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param rotation
     *        The {@link Quaternion} which contains rotation transformation.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector4[] sourceArray, final Quaternion rotation, Vector4[] destinationArray)
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        for (int i = 0; i < sourceArray.length; i++)
        {
            Vector4 value = sourceArray[i];
            destinationArray[i] = transform(value, rotation);
        }
    }

    /**
     * Returns a {@link String} representation of this {@link Vector4} in the format:
     * {X:{@link #x} Y:{@link #y} Z:{@link #z} W:{@link #w}
     * 
     * @return A {@link String} representation of this {@link Vector4}.
     */
    @Override
    public String toString()
    {
        return "{X:" + this.x + " Y:" + this.y + " Z:" + this.z + " W:" + this.w + "}";
    }

    public Vector4 negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.y = -this.w;
        return this;
    }

    /**
     * Adds the supplied {@code Vector4} to this {@code Vector4}.
     * 
     * @param other
     *        The other {@code Vector4} to add to this {@code Vector4}.
     * @return The result of the addition applied to this vector.
     */
    public Vector4 add(Vector4 other)
    {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        this.w += other.w;
        return this;
    }

    /**
     * Subtracts the supplied {@code Vector4} to this {@code Vector4}.
     * 
     * @param other
     *        The other {@code Vector4} to subtract to this vector.
     * @return The result of the subtraction applied to this vector.
     */
    public Vector4 subtract(Vector4 other)
    {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        this.w -= other.w;
        return this;
    }

    /**
     * Multiplies the components of this {@code Vector4} by the components of
     * the supplied {@code Vector4}.
     * 
     * @param other
     *        The other {@code Vector4} to multiply to this vector.
     * @return The result of the multiplication applied to this vector.
     */
    public Vector4 multiply(Vector4 other)
    {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
        this.w *= other.w;
        return this;
    }

    /**
     * Multiplies the components of this {@code Vector4} by a scalar.
     * 
     * @param scaleFactor
     *        Scalar value used to multiply each component of this {@code Vector4}.
     * @return The result of the vector multiplication with a scalar.
     */
    public Vector4 multiply(float scaleFactor)
    {
        this.x *= scaleFactor;
        this.y *= scaleFactor;
        this.z *= scaleFactor;
        this.w *= scaleFactor;
        return this;
    }

    /**
     * Divides the components of this {@code Vector4} by the components of the
     * supplied {@code Vector4}.
     * 
     * @param other
     *        The other {@code Vector4} to divide to this vector.
     * @return The result of the division applied to this vector.
     */
    public Vector4 divide(Vector4 other)
    {
        this.x /= other.x;
        this.y /= other.y;
        this.z /= other.z;
        this.w /= other.w;
        return this;
    }

    /**
     * Divides the components of this {@code Vector4} by a scalar.
     * 
     * @param divider
     *        Scalar value used to divide each component of this {@code Vector4}.
     * @return The result of the vector division by a scalar.
     */
    public Vector4 divide(float divider)
    {
        float factor = 1 / divider;
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        this.w *= factor;
        return this;
    }
}
