package jMono_Framework.math;

/**
 * Contains commonly used precalculated values and mathematical operations.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 */
public final class MathHelper
{
    /**
     * Represents the mathematical constant e(2.71828175).
     */
    public static final float E = (float) Math.E;

    /**
     * Represents the log base ten of e(0.4342945).
     */
    public static final float Log10E = 0.4342945f;

    /**
     * Represents the log base two of e(1.442695).
     */
    public static final float Log2E = 1.442695f;

    /**
     * Represents the value of pi(3.14159274).
     */
    public static final float Pi = (float) Math.PI;

    /**
     * Represents the value of pi divided by two(1.57079637).
     */
    public static final float PiOver2 = (float) (Math.PI / 2.0);

    /**
     * Represents the value of pi divided by four(0.7853982).
     */
    public static final float PiOver4 = (float) (Math.PI / 4.0);

    /**
     * Represents the value of pi times two(6.28318548).
     */
    public static final float TwoPi = (float) (Math.PI * 2.0);

    /**
     * Returns the Cartesian coordinate for one axis of a point that is defined
     * by a given triangle and two normalized barycentric (areal) coordinates.
     * 
     * @param value1
     *        The coordinate on one axis of vertex 1 of the defining triangle.
     * @param value2
     *        The coordinate on the same axis of vertex 2 of the defining triangle.
     * @param value3
     *        The coordinate on the same axis of vertex 3 of the defining triangle.
     * @param amount1
     *        The normalized barycentric (areal) coordinate b2, equal to the weighting factor for
     *        vertex 2, the coordinate of which is specified in value2.
     * @param amount2
     *        The normalized barycentric (areal) coordinate b3, equal to the weighting factor for
     *        vertex 3, the coordinate of which is specified in value3.
     * @return Cartesian coordinate of the specified point with respect to the axis being used.
     */
    public static float barycentric(float value1, float value2, float value3, float amount1, float amount2)
    {
        return value1 + (value2 - value1) * amount1 + (value3 - value1) * amount2;
    }

    /**
     * Performs a Catmull-Rom interpolation using the specified positions.
     * 
     * @param value1
     *        The first position in the interpolation.
     * @param value2
     *        The second position in the interpolation.
     * @param value3
     *        The third position in the interpolation.
     * @param value4
     *        The fourth position in the interpolation.
     * @param amount
     *        Weighting factor.
     * @return A position that is the result of the Catmull-Rom interpolation.
     */
    public static float catmullRom(float value1, float value2, float value3, float value4, float amount)
    {
        // Using formula from http://www.mvps.org/directx/articles/catmull/
        // Internally using doubles not to lose precission
        double amountSquared = amount * amount;
        double amountCubed = amountSquared * amount;
        return (float) (0.5 * (2.0 * value2 + (value3 - value1) * amount
                               + (2.0 * value1 - 5.0 * value2 + 4.0 * value3 - value4) * amountSquared + (3.0 * value2 - value1 - 3.0
                                                                                                          * value3 + value4)
                                                                                                         * amountCubed));
    }

    /**
     * Restricts a value to be within a specified range.
     * 
     * @param value
     *        The value to clamp.
     * @param min
     *        The minimum value. If {@code value} is less than {@code min}, {@code min} will be
     *        returned.
     * @param max
     *        The maximum value. If {@code value} is greater than {@code max}, {@code max} will be
     *        returned.
     * @return The clamped value.
     */
    public static float clamp(float value, float min, float max)
    {
        // First we check to see if we're greater than the max
        value = (value > max) ? max : value;

        // Then we check to see if we're less than the min.
        value = (value < min) ? min : value;

        // There's no check to see if min > max.
        return value;
    }

    /**
     * Restricts a value to be within a specified range.
     * 
     * @param value
     *        The value to clamp.
     * @param min
     *        The minimum value. If {@code value} is less than {@code min}, {@code min} will be
     *        returned.
     * @param max
     *        The maximum value. If {@code value} is greater than {@code max}, {@code max} will be
     *        returned.
     * @return The clamped value.
     */
    public static int clamp(int value, int min, int max)
    {
        value = (value > max) ? max : value;
        value = (value < min) ? min : value;
        return value;
    }

    /**
     * Calculates the absolute value of the difference of two values.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @return Distance between the two values.
     */
    public static float distance(float value1, float value2)
    {
        return Math.abs(value1 - value2);
    }

    /**
     * Performs a Hermite spline interpolation.
     * 
     * @param value1
     *        Source position.
     * @param tangent1
     *        Source tangent.
     * @param value2
     *        Source position.
     * @param tangent2
     *        Source tangent.
     * @param amount
     *        Weighting factor.
     * @return The result of the Hermite spline interpolation.
     */
    public static float hermite(float value1, float tangent1, float value2, float tangent2, float amount)
    {
        // All transformed to double not to lose precision
        // Otherwise, for high numbers of param:amount the result is NaN instead of Infinity
        double v1 = value1, v2 = value2, t1 = tangent1, t2 = tangent2, s = amount, result;
        double sSquared = s * s;
        double sCubed = sSquared * s;

        if (amount == 0f)
            result = value1;
        else if (amount == 1f)
            result = value2;
        else
            result = (2 * v1 - 2 * v2 + t2 + t1) * sCubed +
                     (3 * v2 - 3 * v1 - 2 * t1 - t2) * sSquared +
                     t1 * s +
                     v1;
        return (float) result;
    }

    /**
     * Linearly interpolates between two values.
     * <p>
     * This method performs the linear interpolation based on the following formula :<br/>
     * {@code value1 + (value2 - value1) * amount}<br/>
     * Passing amount a value of 0 will cause value1 to be returned, a value of 1 will cause value2
     * to be returned. See {@link MathHelper#lerpPrecise} for a less efficient version with more
     * precision around edge cases.
     *
     * @param value1
     *        Source value.
     * @param value2
     *        Destination value.
     * @param amount
     *        Value between 0 and 1 indicating the weight of value2.
     * @return Interpolated value.
     */
    public static float lerp(float value1, float value2, float amount)
    {
        return value1 + (value2 - value1) * amount;
    }

    /**
     * Linearly interpolates between two values.
     * This method is a less efficient, more precise version of {@link MathHelper#lerp}.
     * <p>
     * This method performs the linear interpolation based on the following formula :<br/>
     * {@code ((1 - amount) * value1) + (value2 * amount)}<br/>
     * Passing amount a value of 0 will cause value1 to be returned, a value of 1 will cause value2
     * to be returned.
     * <p>
     * This method does not have the floating point precision issue that {@code MathHelper#lerp}
     * has. i.e. If there is a big gap between value1 and value2 in magnitude (e.g.
     * value1=10000000000000000, value2=1), right at the edge of the interpolation range (amount=1),
     * {@code MathHelper#lerp} will return 0 (whereas it should return 1). This also holds for
     * value1=10^17, value2=10; value1=10^18,value2=10^2... so on.
     * <p>
     * For an in depth explanation of the issue, see below references:<br/>
     * Relevant Wikipedia Article: <a
     * href="https://en.wikipedia.org/wiki/Linear_interpolation#Programming_language_support"
     * >https://en.wikipedia.org/wiki/Linear_interpolation#Programming_language_support</a><br/>
     * Relevant StackOverflow Answer: <a href=
     * "http://stackoverflow.com/questions/4353525/floating-point-linear-interpolation#answer-23716956"
     * >http://stackoverflow.com/questions/4353525/floating-point-linear-interpolation#answer-
     * 23716956</a>
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Destination value.
     * @param amount
     *        Value between 0 and 1 indicating the weight of value2.
     * @return Interpolated value.
     */
    public static float lerpPrecise(float value1, float value2, float amount)
    {
        return ((1 - amount) * value1) + (value2 * amount);
    }

    /**
     * Returns the greater of two values.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @return The greater value.
     */
    public static float max(float value1, float value2)
    {
        return value1 > value2 ? value1 : value2;
    }

    /**
     * Returns the greater of two values.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @return The greater value.
     */
    public static int max(int value1, int value2)
    {
        return value1 > value2 ? value1 : value2;
    }

    /**
     * Returns the lesser of two values.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @return The lesser value.
     */
    public static float min(float value1, float value2)
    {
        return value1 < value2 ? value1 : value2;
    }

    /**
     * Returns the lesser of two values.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @return The lesser value.
     */
    public static int min(int value1, int value2)
    {
        return value1 < value2 ? value1 : value2;
    }

    /**
     * Interpolates between two values using a cubic equation.
     * 
     * @param value1
     *        Source value.
     * @param value2
     *        Source value.
     * @param amount
     *        Weighting value.
     * @return The interpolated value.
     */
    public static float smoothStep(float value1, float value2, float amount)
    {
        // It is expected that 0 < amount < 1
        // If amount < 0, return value1
        // If amount > 1, return value2
        float result = MathHelper.clamp(amount, 0f, 1f);
        result = MathHelper.hermite(value1, 0f, value2, 0f, result);

        return result;
    }

    /**
     * Converts radians to degrees.
     * <p>
     * This method uses double precision internally, though it returns single float.<br/>
     * Factor = 180 / pi.
     * 
     * @param radians
     *        The angle in radians.
     * @return The angle in degrees.
     */
    public static float toDegrees(float radians)
    {
        return (float) (radians * 57.295779513082320876798154814105);
    }

    /**
     * Converts degrees to radians.
     * <p>
     * This method uses double precision internally, though it returns single float.<br/>
     * Factor = pi / 180.
     * 
     * @param degrees
     *        The angle in degrees.
     * @return The angle in radians.
     */
    public static float toRadians(float degrees)
    {
        return (float) (degrees * 0.017453292519943295769236907684886);
    }

    /**
     * Reduces a given angle to a value between {@link Pi} and -{@code Pi}.
     * 
     * @param angle
     *        The angle to reduce, in radians.
     * @return The new angle, in radians.
     */
    public static float wrapAngle(float angle)
    {
        if ((angle > -Pi) && (angle <= Pi))
            return angle;
        angle %= TwoPi;
        if (angle <= -Pi)
            return angle + TwoPi;
        if (angle > Pi)
            return angle - TwoPi;
        // NOTE(Eric): C# seems to return +0.0 and not -0.0f when angle passed in is -TwoPi
        // so I added this check so the unit tests passes.
        if (angle == -0.0f)
            angle = 0f;
        return angle;
    }

    /**
     * Determines if value is powered by two.
     * 
     * @param value
     *        A value.
     * @return {@code true} if {@code value} is powered by two; otherwise {@code false}.
     */
    public static boolean isPowerOfTwo(int value)
    {
        return (value > 0) && ((value & (value - 1)) == 0);
    }

    /**
     * Rotates a point around another specified point.
     * 
     * @param point
     *        The point to rotate.
     * @param origin
     *        The rotation origin or "axis".
     * @param rotation
     *        The rotation amount in radians.
     * @return The position of the point after rotating it.
     */
    public static Vector2 rotateAboutOrigin(Vector2 point, Vector2 origin, float rotation)
    {
        // Point relative to origin
        Vector2 u = new Vector2(Vector2.subtract(point, origin));

        if (u.equals(Vector2.Zero()))
            return point;

        // Angle relative to origin
        float a = (float) Math.atan2(u.y, u.x);

        // Rotate
        a += rotation;

        // U is now the new point relative to origin
        u = new Vector2((float) Math.cos(a) * u.length(), (float) Math.sin(a) * u.length());

        return u.add(origin);
    }
}
