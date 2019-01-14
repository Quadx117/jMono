package jMono_Framework.math;

import jMono_Framework.Point;

// C# struct
/**
 * Defines a vector with two float components.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 * 
 */
public class Vector2 // implements IEquatable<Vector2>
{
    private static final Vector2 zeroVector = new Vector2(0.0f, 0.0f);
    private static final Vector2 unitVector = new Vector2(1.0f, 1.0f);
    private static final Vector2 unitXVector = new Vector2(1.0f, 0.0f);
    private static final Vector2 unitYVector = new Vector2(0.0f, 1.0f);

    /**
     * The x component of this Vector.
     */
    public float x;

    /**
     * The y component of this Vector.
     */
    public float y;

    /**
     * Returns a {@code Vector2} with components 0, 0.
     * 
     * @return A {@code Vector2} with components 0, 0.
     */
    public static Vector2 Zero()
    {
        return new Vector2(zeroVector);
    }

    /**
     * Returns a {@code Vector2} with components 1, 1.
     * 
     * @return A {@code Vector2} with components 1, 1.
     */
    public static Vector2 One()
    {
        return new Vector2(unitVector);
    }

    /**
     * Returns a {@code Vector2} with components 1, 0.
     * 
     * @return A {@code Vector2} with components 1, 0.
     */
    public static Vector2 UnitX()
    {
        return new Vector2(unitXVector);
    }

    /**
     * Returns a {@code Vector2} with components 0, 1.
     * 
     * @return A {@code Vector2} with components 0, 1.
     */
    public static Vector2 UnitY()
    {
        return new Vector2(unitYVector);
    }

    protected String debugDisplayString()
    {
        return (this.x + "  " + this.y);
    }

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    /**
     * Initializes a new instance of Vector2 with both of its component set to 0.0f.
     */
    public Vector2()
    {
        x = 0.0f;
        y = 0.0f;
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
     * Initializes a new instance of Vector2 with both of its component set to
     * the same values as the specified vector.
     * 
     * @param vector
     *        The vector used to set the components of this vector.
     */
    public Vector2(Vector2 vector)
    {
        this.x = vector.x;
        this.y = vector.y;
    }

    /**
     * Inverts the sign of the components of this {@link Vector2}.
     * 
     * @return The result of the inversion.
     */
    public Vector2 negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        return this;
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

    public Vector2 multiply(float scaleFactor)
    {
        this.x *= scaleFactor;
        this.y *= scaleFactor;
        return this;
    }

    public Vector2 divide(Vector2 other)
    {
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2 divide(float divider)
    {
        this.x /= divider;
        this.y /= divider;
        return this;
    }

    /**
     * Adds two {@link Vector2}.
     * 
     * @param value1
     *        Source {@code Vector2} on the left of the add sign.
     * @param value2
     *        Source {@code Vector2} on the right of the add sign.
     * @return
     */
    public static Vector2 add(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x += value2.x;
        result.y += value2.y;
        return result;
    }

    /**
     * Performs vector addition on {@code value1} and {@code value2},
     * storing the result of the addition in {@code result}.
     * 
     * @param value1
     *        The first vector to add.
     * @param value2
     *        The second vector to add.
     * @param result
     *        The result of the vector addition.
     */
    public static void add(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x + value2.x;
        result.y = value1.y + value2.y;
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
     *        Barycentric scalar {@code b2} which represents a weighting factor towards second
     *        vector of 2d-triangle.
     * @param amount2
     *        Barycentric scalar {@code b3} which represents a weighting factor towards third vector
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
     *        Barycentric scalar {@code b2} which represents a weighting factor towards second
     *        vector of 2d-triangle.
     * @param amount2
     *        Barycentric scalar {@code b3} which represents a weighting factor towards third vector
     *        of 2d-triangle.
     * @param result
     *        A cartesian translation of barycentric coordinates as an output parameter.
     */
    public static void barycentric(final Vector2 value1, final Vector2 value2, final Vector2 value3, float amount1, float amount2, Vector2 result)
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
    public static void catmullRom(final Vector2 value1, final Vector2 value2, final Vector2 value3, final Vector2 value4, float amount, Vector2 result)
    {
        result.x = MathHelper.catmullRom(value1.x, value2.x, value3.x, value4.x, amount);
        result.y = MathHelper.catmullRom(value1.y, value2.y, value3.y, value4.y, amount);
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
    public static Vector2 clamp(Vector2 value1, Vector2 min, Vector2 max)
    {
        return new Vector2(MathHelper.clamp(value1.x, min.x, max.x),
                           MathHelper.clamp(value1.y, min.y, max.y));
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
     * Returns the distance between two {@link Vector2}.
     * 
     * @param value1
     *        The first {@code Vector2}.
     * @param value2
     *        The second {@code Vector2}.
     * @return The distance between two vectors.
     */
    public static float distance(Vector2 value1, Vector2 value2)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        return (float) Math.sqrt((v1 * v1) + (v2 * v2));
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Returns the distance between two {@link Vector2}.
     * 
     * @param value1
     *        The first {@code Vector2}.
     * @param value2
     *        The second {@code Vector2}.
     * @param result
     *        The distance between two vectors as an output parameter.
     */
    // public static void distance(final Vector2 value1, final Vector2 value2, float result)
    // {
    // float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
    // result = (float) Math.sqrt((v1 * v1) + (v2 * v2));
    // }

    /**
     * Returns the squared distance between two {@link Vector2}.
     * 
     * @param value1
     *        The first {@code Vector2}.
     * @param value2
     *        The second {@code Vector2}.
     * @return The squared distance between two vectors.
     */
    public static float distanceSquared(Vector2 value1, Vector2 value2)
    {
        float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
        return (v1 * v1) + (v2 * v2);
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Returns the squared distance between two {@link Vector2}.
     * 
     * @param value1
     *        The first {@code Vector2}.
     * @param value2
     *        The second {@code Vector2}.
     * @param result
     *        The squared distance between two vectors as an output parameter.
     */
    // public static void distanceSquared(final Vector2 value1, final Vector2 value2, float result)
    // {
    // float v1 = value1.x - value2.x, v2 = value1.y - value2.y;
    // result = (v1 * v1) + (v2 * v2);
    // }

    /**
     * Divides the components of a {@link Vector2} by the components of another {@code Vector2}.
     * 
     * @param value1
     *        Source {@code Vector2} on the left of the div sign.
     * @param value2
     *        Divisor {@code Vector2} on the right of the div sign.
     * @return The result of dividing the vectors.
     */
    public static Vector2 divide(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x /= value2.x;
        result.y /= value2.y;
        return result;
    }

    /**
     * Divides the components of a {@link Vector2} by the components of another {@code Vector2}.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param value2
     *        Divisor {@code Vector2}.
     * @param result
     *        The result of dividing the vectors as an output parameter.
     */
    public static void divide(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x / value2.x;
        result.y = value1.y / value2.y;
    }

    /**
     * Divides the components of a {@link Vector2} by a scalar.
     * 
     * @param vector
     *        Source {@code Vector2} on the left of the div sign.
     * @param divider
     *        Divisor scalar on the right of the div sign.
     * @return The result of dividing a vector by a scalar.
     */
    public static Vector2 divide(Vector2 vector, float divider)
    {
        float factor = 1 / divider;
        Vector2 result = new Vector2(vector);
        result.x *= factor;
        result.y *= factor;
        return result;
    }

    /**
     * Divides the components of a {@link Vector2} by a scalar.
     * 
     * @param vector
     *        Source {@code Vector2}.
     * @param divider
     *        Divisor scalar.
     * @param result
     *        The result of dividing a vector by a scalar as an output parameter.
     */
    public static void divide(final Vector2 vector, float divider, Vector2 result)
    {
        float factor = 1 / divider;
        result.x = vector.x * factor;
        result.y = vector.y * factor;
    }

    /**
     * Returns the dot product of this {@link Vector2} with the supplied {@code Vector2}.
     * 
     * @param other
     *        The second {@code Vector2}
     * @return The dot product of two vectors.
     */
    public float dot(Vector2 other)
    {
        return x * other.x + y * other.y;
    }

    /**
     * Returns a dot product of two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The dot product of two vectors.
     */
    public static float dot(Vector2 value1, Vector2 value2)
    {
        return (value1.x * value2.x) + (value1.y * value2.y);
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // / <summary>
    // / Returns a dot product of two vectors.
    // / </summary>
    // / <param name="value1">The first vector.</param>
    // / <param name="value2">The second vector.</param>
    // / <param name="result">The dot product of two vectors as an output parameter.</param>
    // public static void dot(final Vector2 value1, final Vector2 value2, float result)
    // {
    // result = (value1.x * value2.x) + (value1.y * value2.y);
    // }

    /**
     * Indicates whether some other {@link Object} is "equal to" this one.
     * 
     * @param obj
     *        The reference {@code Object} with which to compare.
     * @return {@code true} if this object is the same as the specified argument; {@code false}
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
        return this.equals((Vector2) obj);
    }

    /**
     * Compares whether current instance is equal to the specified {@link Vector2}.
     * 
     * @param other
     *        The {@code Vector2} to compare.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(Vector2 other)
    {
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
     * @return {@code false} if this object is the same as the specified argument; {@code true}
     *         otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    /**
     * Returns the hash code value of this {@link Vector2}.
     * 
     * @return The hash code value of this {@code Vector2}.
     */
    @Override
    public int hashCode()
    {
        return (Float.hashCode(x) * 397) ^ Float.hashCode(y);
    }

    /**
     * Creates a new {@link Vector2} that contains hermite spline interpolation.
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
    public static Vector2 hermite(Vector2 value1, Vector2 tangent1, Vector2 value2, Vector2 tangent2, float amount)
    {
        return new Vector2(MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount),
                           MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains hermite spline interpolation.
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
    public static void hermite(final Vector2 value1, final Vector2 tangent1, final Vector2 value2,
                               final Vector2 tangent2, float amount, Vector2 result)
    {
        result.x = MathHelper.hermite(value1.x, tangent1.x, value2.x, tangent2.x, amount);
        result.y = MathHelper.hermite(value1.y, tangent1.y, value2.y, tangent2.y, amount);
    }

    /**
     * Returns the length of this {@link Vector2}.
     * 
     * @return The length of this {@code Vector2}.
     */
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the squared length of this {@link Vector2}.
     * 
     * @return The squared length of this {@code Vector2}.
     */
    public float lengthSquared()
    {
        return (x * x) + (y * y);
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the specified vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @return The result of linear interpolation of the specified vectors.
     */
    public static Vector2 lerp(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2(MathHelper.lerp(value1.x, value2.x, amount),
                           MathHelper.lerp(value1.y, value2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the specified vectors.
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
    public static void lerp(final Vector2 value1, final Vector2 value2, float amount, Vector2 result)
    {
        result.x = MathHelper.lerp(value1.x, value2.x, amount);
        result.y = MathHelper.lerp(value1.y, value2.y, amount);
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the specified vectors.
     * Uses {@link MathHelper#lerpPrecise} on MathHelper for the interpolation.
     * Less efficient but more precise compared to {@link Vector2#lerp(Vector2, Vector2, float)}.
     * See remarks section of {@link MathHelper#lerpPrecise} on MathHelper for more info.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @return The result of linear interpolation of the specified vectors.
     */
    public static Vector2 lerpPrecise(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2(MathHelper.lerpPrecise(value1.x, value2.x, amount),
                           MathHelper.lerpPrecise(value1.y, value2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains linear interpolation of the specified vectors.
     * Uses {@link MathHelper#lerpPrecise(float, float)} in MathHelper for the interpolation.
     * Less efficient but more precise compared to
     * {@link Vector2#lerp(Vector2, Vector2, float, Vector2)}.
     * See remarks section of {@link MathHelper#lerpPrecise(float, float)} in MathHelper for more
     * info.
     * 
     * @param value1
     *        The first {@code vector}.
     * @param value2
     *        The second {@code vector}.
     * @param amount
     *        Weighting value(between 0.0 and 1.0).
     * @param result
     *        The result of linear interpolation of the specified vectors as an output parameter.
     */
    public static void lerpPrecise(final Vector2 value1, final Vector2 value2, float amount, Vector2 result)
    {
        result.x = MathHelper.lerpPrecise(value1.x, value2.x, amount);
        result.y = MathHelper.lerpPrecise(value1.y, value2.y, amount);
    }

    /**
     * Creates a new {@link Vector2} that contains a maximal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The {@code Vector2} with maximal values from the two vectors.
     */
    public static Vector2 max(Vector2 value1, Vector2 value2)
    {
        return new Vector2(value1.x > value2.x ? value1.x : value2.x,
                           value1.y > value2.y ? value1.y : value2.y);
    }

    /**
     * Creates a new {@link Vector2} that contains a maximal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param result
     *        The {@code Vector2} with maximal values from the two vectors as an output parameter.
     */
    public static void max(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x > value2.x ? value1.x : value2.x;
        result.y = value1.y > value2.y ? value1.y : value2.y;
    }

    /**
     * Creates a new {@link Vector2} that contains a minimal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @return The {@code Vector2} with minimal values from the two vectors.
     */
    public static Vector2 min(Vector2 value1, Vector2 value2)
    {
        return new Vector2(value1.x < value2.x ? value1.x : value2.x,
                           value1.y < value2.y ? value1.y : value2.y);
    }

    /**
     * Creates a new {@link Vector2} that contains a minimal values from the two vectors.
     * 
     * @param value1
     *        The first vector.
     * @param value2
     *        The second vector.
     * @param result
     *        The {@code Vector2} with minimal values from the two vectors as an output parameter.
     */
    public static void min(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x < value2.x ? value1.x : value2.x;
        result.y = value1.y < value2.y ? value1.y : value2.y;
    }

    /**
     * Multiplies the components of two vectors by each other.
     * 
     * @param vector1
     *        Source {@link Vector2} on the left of the mul sign.
     * @param vector2
     *        Source {@code Vector2} on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(Vector2 vector1, Vector2 vector2)
    {
        Vector2 result = new Vector2(vector1);
        result.x *= vector2.x;
        result.y *= vector2.y;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a multiplication of two vectors.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param value2
     *        Source {@code Vector2}.
     * @param result
     *        The result of the vector multiplication as an output parameter.
     */
    public static void multiply(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x * value2.x;
        result.y = value1.y * value2.y;
    }

    /**
     * Multiplies the components of vector by a scalar.
     * 
     * @param scaleFactor
     *        Scalar value on the left of the mul sign.
     * @param vector
     *        Source {@link Vector2} on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(float scaleFactor, Vector2 vector)
    {
        Vector2 result = new Vector2(vector);
        result.x *= scaleFactor;
        result.y *= scaleFactor;
        return result;
    }

    /**
     * Multiplies the components of vector by a scalar.
     * 
     * @param vector
     *        Source {@link Vector2} on the left of the mul sign.
     * @param scaleFactor
     *        Scalar value on the right of the mul sign.
     * @return Result of the vector multiplication.
     */
    public static Vector2 multiply(Vector2 vector, float scaleFactor)
    {
        Vector2 result = new Vector2(vector);
        result.x *= scaleFactor;
        result.y *= scaleFactor;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a multiplication of {@code Vector2} and a scalar.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param scaleFactor
     *        Scalar value.
     * @param result
     *        The result of the multiplication with a scalar as an output parameter.
     */
    public static void multiply(final Vector2 value1, float scaleFactor, Vector2 result)
    {
        result.x = value1.x * scaleFactor;
        result.y = value1.y * scaleFactor;
    }

    /**
     * Inverts values in the specified {@link Vector2}.
     * 
     * @param value
     *        Source {@code Vector2} on the right of the sub sign.
     * @return Result of the inversion.
     */
    public static Vector2 negate(Vector2 value)
    {
        return new Vector2(-value.x, -value.y);
    }

    /**
     * Creates a new {@link Vector2} that contains the specified vector inversion.
     * 
     * @param value
     *        Source {@code Vector2}.
     * @param result
     *        The result of the vector inversion as an output parameter.
     */
    public static void negate(final Vector2 value, Vector2 result)
    {
        result.x = -value.x;
        result.y = -value.y;
    }

    /**
     * Turns this {@link Vector2} to a unit vector with the same direction.
     */
    public void normalize()
    {
        float val = 1.0f / (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
        x *= val;
        y *= val;
    }

    /**
     * Creates a new {@link Vector2} that contains a normalized values from another vector.
     * 
     * @param value
     *        Source {@code Vector2}.
     * @return Unit vector.
     */
    public static Vector2 normalize(Vector2 value)
    {
        Vector2 result = new Vector2(value);
        float val = 1.0f / (float) Math.sqrt((value.x * value.x) + (value.y * value.y));
        result.x *= val;
        result.y *= val;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a normalized values from another vector.
     * 
     * @param value
     *        Source {@code Vector2}.
     * @param result
     *        Unit vector as an output parameter.
     */
    public static void normalize(final Vector2 value, Vector2 result)
    {
        float val = 1.0f / (float) Math.sqrt((value.x * value.x) + (value.y * value.y));
        result.x = value.x * val;
        result.y = value.y * val;
    }

    /**
     * Creates a new {@link Vector2} that contains reflect vector of the given vector and normal.
     * 
     * @param vector
     *        Source {@code Vector2}
     * @param normal
     *        Reflection normal.
     * @return Reflected vector.
     */
    public static Vector2 reflect(Vector2 vector, Vector2 normal)
    {
        Vector2 result = new Vector2();
        float val = 2.0f * ((vector.x * normal.x) + (vector.y * normal.y));
        result.x = vector.x - (normal.x * val);
        result.y = vector.y - (normal.y * val);
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains reflect vector of the given vector and normal.
     * 
     * @param vector
     *        Source {@code Vector2}.
     * @param normal
     *        Reflection normal.
     * @param result
     *        Reflected vector as an output parameter.
     */
    public static void reflect(final Vector2 vector, final Vector2 normal, Vector2 result)
    {
        float val = 2.0f * ((vector.x * normal.x) + (vector.y * normal.y));
        result.x = vector.x - (normal.x * val);
        result.y = vector.y - (normal.y * val);
    }

    /**
     * Creates a new {@link Vector2} that contains cubic interpolation of the specified vectors.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param value2
     *        Source {@code Vector2}.
     * @param amount
     *        Weighting value.
     * @return Cubic interpolation of the specified vectors.
     */
    public static Vector2 smoothStep(Vector2 value1, Vector2 value2, float amount)
    {
        return new Vector2(MathHelper.smoothStep(value1.x, value2.x, amount),
                           MathHelper.smoothStep(value1.y, value2.y, amount));
    }

    /**
     * Creates a new {@link Vector2} that contains cubic interpolation of the specified vectors.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param value2
     *        Source {@code Vector2}.
     * @param amount
     *        Weighting value.
     * @param result
     *        Cubic interpolation of the specified vectors as an output parameter.
     */
    public static void smoothStep(final Vector2 value1, final Vector2 value2, float amount, Vector2 result)
    {
        result.x = MathHelper.smoothStep(value1.x, value2.x, amount);
        result.y = MathHelper.smoothStep(value1.y, value2.y, amount);
    }

    /**
     * Subtracts a {@link Vector2} from a {@code Vector2}.
     * 
     * @param value1
     *        Source {@code Vector2} on the left of the sub sign.
     * @param value2
     *        Source {@code Vector2} on the right of the sub sign.
     * @return Result of the vector subtraction.
     */
    public static Vector2 subtract(Vector2 value1, Vector2 value2)
    {
        Vector2 result = new Vector2(value1);
        result.x -= value2.x;
        result.y -= value2.y;
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains subtraction of on {@code Vector2} from a another.
     * 
     * @param value1
     *        Source {@code Vector2}.
     * @param value2
     *        Source {@code Vector2}.
     * @param result
     *        The result of the vector subtraction as an output parameter.
     */
    public static void subtract(final Vector2 value1, final Vector2 value2, Vector2 result)
    {
        result.x = value1.x - value2.x;
        result.y = value1.y - value2.y;
    }

    /**
     * Returns a {@link String} representation of this {@code Vector2} in the format:
     * {X:{@link #x} Y:{@link #y}
     * 
     * @return A {@link String} representation of this {@code Vector2}.
     */
    @Override
    public String toString()
    {
        return "{X:" + x + " Y:" + y + "}";
    }

    /**
     * Returns a {@link Point} representation for this object.
     * 
     * @return A {@code Point} representation for this object.
     */
    public Point toPoint()
    {
        return new Point((int) x, (int) y);
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of 2d-vector by the specified
     * {@link Matrix}.
     * 
     * @param position
     *        Source {@code Vector2}.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @return Transformed {@code Vector2}.
     */
    public static Vector2 transform(Vector2 position, Matrix matrix)
    {
        return new Vector2((position.x * matrix.m11) + (position.y * matrix.m21) + matrix.m41,
                           (position.x * matrix.m12) + (position.y * matrix.m22) + matrix.m42);
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of 2d-vector by the specified
     * {@link Matrix}.
     * 
     * @param position
     *        Source {@code Vector2}.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param result
     *        Transformed {@code Vector2} as an output parameter.
     */
    public static void transform(final Vector2 position, final Matrix matrix, Vector2 result)
    {
        float x = (position.x * matrix.m11) + (position.y * matrix.m21) + matrix.m41;
        float y = (position.x * matrix.m12) + (position.y * matrix.m22) + matrix.m42;
        result.x = x;
        result.y = y;
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of 2d-vector by the specified
     * {@link Quaternion}, representing the rotation.
     * 
     * @param value
     *        Source {@code Vector2}.
     * @param rotation
     *        The {@code Quaternion} which contains rotation transformation.
     * @return Transformed {@code Vector2}.
     */
    public static Vector2 transform(Vector2 value, Quaternion rotation)
    {
        Vector2 result = new Vector2();
        transform(value, rotation, result);
        return result;
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of 2d-vector by the specified
     * {@link Quaternion}, representing the rotation.
     * 
     * @param value
     *        Source {@code Vector2}.
     * @param rotation
     *        The {@code Quaternion} which contains rotation transformation.
     * @param result
     *        Transformed {@code Vector2} as an output parameter.
     */
    public static void transform(final Vector2 value, final Quaternion rotation, Vector2 result)
    {
        Vector3 rot1 = new Vector3(rotation.x + rotation.x, rotation.y + rotation.y, rotation.z + rotation.z);
        Vector3 rot2 = new Vector3(rotation.x, rotation.x, rotation.w);
        Vector3 rot3 = new Vector3(1, rotation.y, rotation.z);
        Vector3 rot4 = Vector3.multiply(rot1, rot2);
        Vector3 rot5 = Vector3.multiply(rot1, rot3);

        Vector2 v = new Vector2();
        v.x = (float) ((double) value.x * (1.0 - (double) rot5.y - (double) rot5.z) + (double) value.y
                                                                                      * ((double) rot4.y - (double) rot4.z));
        v.y = (float) ((double) value.x * ((double) rot4.y + (double) rot4.z) + (double) value.y
                                                                                * (1.0 - (double) rot4.x - (double) rot5.z));
        result.x = v.x;
        result.y = v.y;
    }

    /**
     * Apply transformation on vectors within array of {@link Vector2} by the specified
     * {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param sourceIndex
     *        The starting index of transformation in the source array.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param destinationArray
     *        Destination array.
     * @param destinationIndex
     *        The starting index in the destination array, where the first {@code Vector2} should be
     *        written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector2[] sourceArray,
                                 int sourceIndex,
                                 final Matrix matrix,
                                 Vector2[] destinationArray,
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

        for (int x = 0; x < length; ++x)
        {
            Vector2 position = sourceArray[sourceIndex + x];
            Vector2 destination = destinationArray[destinationIndex + x];
            destination.x = (position.x * matrix.m11) + (position.y * matrix.m21) + matrix.m41;
            destination.y = (position.x * matrix.m12) + (position.y * matrix.m22) + matrix.m42;
            destinationArray[destinationIndex + x] = destination;
        }
    }

    /**
     * Apply transformation on vectors within array of {@link Vector2} by the specified
     * {@link Quaternion} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param sourceIndex
     *        The starting index of transformation in the source array.
     * @param rotation
     *        The {@code Quaternion} which contains rotation transformation.
     * @param destinationArray
     *        Destination array.
     * @param destinationIndex
     *        The starting index in the destination array, where the first {@code Vector2} should be
     *        written.
     * @param length
     *        The number of vectors to be transformed.
     */
    public static void transform(Vector2[] sourceArray,
                                 int sourceIndex,
                                 final Quaternion rotation,
                                 Vector2[] destinationArray,
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

        for (int x = 0; x < length; ++x)
        {
            Vector2 position = sourceArray[sourceIndex + x];
            Vector2 destination = destinationArray[destinationIndex + x];

            Vector2 v = new Vector2();
            transform(position, rotation, v);

            destination.x = v.x;
            destination.y = v.y;

            destinationArray[destinationIndex + x] = destination;
        }
    }

    /**
     * Apply transformation on all vectors within array of {@link Vector2} by the specified
     * {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector2[] sourceArray, final Matrix matrix, Vector2[] destinationArray)
    {
        transform(sourceArray, 0, matrix, destinationArray, 0, sourceArray.length);
    }

    /**
     * Apply transformation on all vectors within array of {@link Vector2} by the specified
     * {@link Quaternion} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param rotation
     *        The {@code Quaternion} which contains rotation transformation.
     * @param destinationArray
     *        Destination array.
     */
    public static void transform(Vector2[] sourceArray, final Quaternion rotation, Vector2[] destinationArray)
    {
        transform(sourceArray, 0, rotation, destinationArray, 0, sourceArray.length);
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of the specified normal by the
     * specified {@link Matrix}.
     * 
     * @param normal
     *        Source {@code Vector2} which represents a normal vector.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @return Transformed normal.
     */
    public static Vector2 transformNormal(Vector2 normal, Matrix matrix)
    {
        return new Vector2((normal.x * matrix.m11) + (normal.y * matrix.m21),
                           (normal.x * matrix.m12) + (normal.y * matrix.m22));
    }

    /**
     * Creates a new {@link Vector2} that contains a transformation of the specified normal by the
     * specified {@link Matrix}.
     * 
     * @param normal
     *        Source {@code Vector2} which represents a normal vector.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param result
     *        Transformed normal as an output parameter.
     */
    public static void transformNormal(final Vector2 normal, final Matrix matrix, Vector2 result)
    {
        float x = (normal.x * matrix.m11) + (normal.y * matrix.m21);
        float y = (normal.x * matrix.m12) + (normal.y * matrix.m22);
        result.x = x;
        result.y = y;
    }

    /**
     * Apply transformation on normals within array of {@link Vector2} by the specified
     * {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param sourceIndex
     *        The starting index of transformation in the source array.
     * @param matrix
     *        The transformation {@code Matrix}
     * @param destinationArray
     *        Destination array.
     * @param destinationIndex
     *        The starting index in the destination array, where the first {@code Vector2} should be
     *        written.
     * @param length
     *        The number of normals to be transformed.
     */
    public static void transformNormal(Vector2[] sourceArray,
                                       int sourceIndex,
                                       final Matrix matrix,
                                       Vector2[] destinationArray,
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

        for (int i = 0; i < length; ++i)
        {
            Vector2 normal = sourceArray[sourceIndex + i];

            destinationArray[destinationIndex + i] = new Vector2((normal.x * matrix.m11) + (normal.y * matrix.m21),
                                                                 (normal.x * matrix.m12) + (normal.y * matrix.m22));
        }
    }

    /**
     * Apply transformation on all normals within array of {@link Vector2} by the specified
     * {@link Matrix} and places the results in an another array.
     * 
     * @param sourceArray
     *        Source array.
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param destinationArray
     *        Destination array.
     */
    public static void transformNormal
            (
             Vector2[] sourceArray,
             final Matrix matrix,
             Vector2[] destinationArray
            )
    {
        if (sourceArray == null)
            throw new NullPointerException("sourceArray");
        if (destinationArray == null)
            throw new NullPointerException("destinationArray");
        if (destinationArray.length < sourceArray.length)
            throw new IllegalArgumentException("Destination array length is lesser than source array length");

        for (int i = 0; i < sourceArray.length; i++)
        {
            Vector2 normal = sourceArray[i];

            destinationArray[i] = new Vector2((normal.x * matrix.m11) + (normal.y * matrix.m21),
                                              (normal.x * matrix.m12) + (normal.y * matrix.m22));
        }
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
     * Sets the components of this {@link Vector2} to the values of the specified {@code Vector2}
     * 
     * @param vector2
     *        The vector containing the new values for this {@code Vector2}.
     */
    public void set(Vector2 vector2)
    {
        set(vector2.x, vector2.y);
    }

    /**
     * Sets the components of this {@link Vector2} to the specified values.
     * 
     * @param x
     *        The new value for the {@link #x} component of this {@code Vector2}.
     * @param y
     *        The new value for the {@link #y} component of this {@code Vector2}.
     */
    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2 rotate(float angle)
    {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
    }

}
