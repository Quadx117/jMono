package jMono_Framework.math;

import jMono_Framework.Plane;
import jMono_Framework.Rectangle;

// C# struct
/**
 * Represents the right-handed 4x4 floating point matrix, which can store translation, scale and
 * rotation information.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Matrix // implements IEquatable<Matrix>
{
    // #region Public Constructors

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    public Matrix()
    {
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m14 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m24 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
        this.m34 = 0.0f;
        this.m41 = 0.0f;
        this.m42 = 0.0f;
        this.m43 = 0.0f;
        this.m44 = 0.0f;
    }

    public Matrix(Matrix m)
    {
        this.m11 = m.m11;
        this.m12 = m.m12;
        this.m13 = m.m13;
        this.m14 = m.m14;
        this.m21 = m.m21;
        this.m22 = m.m22;
        this.m23 = m.m23;
        this.m24 = m.m24;
        this.m31 = m.m31;
        this.m32 = m.m32;
        this.m33 = m.m33;
        this.m34 = m.m34;
        this.m41 = m.m41;
        this.m42 = m.m42;
        this.m43 = m.m43;
        this.m44 = m.m44;
    }

    /**
     * Constructs a matrix.
     * 
     * @param m11
     *        A first row and first column value.
     * @param m12
     *        A first row and second column value.
     * @param m13
     *        A first row and third column value.
     * @param m14
     *        A first row and fourth column value.
     * @param m21
     *        A second row and first column value.
     * @param m22
     *        A second row and second column value.
     * @param m23
     *        A second row and third column value.
     * @param m24
     *        A second row and fourth column value.
     * @param m31
     *        A third row and first column value.
     * @param m32
     *        A third row and second column value.
     * @param m33
     *        A third row and third column value.
     * @param m34
     *        A third row and fourth column value.
     * @param m41
     *        A fourth row and first column value.
     * @param m42
     *        A fourth row and second column value.
     * @param m43
     *        A fourth row and third column value.
     * @param m44
     *        A fourth row and fourth column value.
     */
    public Matrix(float m11, float m12, float m13, float m14,
                  float m21, float m22, float m23, float m24,
                  float m31, float m32, float m33, float m34,
                  float m41, float m42, float m43, float m44)
    {
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m24 = m24;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.m34 = m34;
        this.m41 = m41;
        this.m42 = m42;
        this.m43 = m43;
        this.m44 = m44;
    }

    /**
     * Constructs a matrix.
     * 
     * @param row1
     *        A first row of the created matrix.
     * @param row2
     *        A second row of the created matrix.
     * @param row3
     *        A third row of the created matrix.
     * @param row4
     *        A fourth row of the created matrix.
     */
    public Matrix(Vector4 row1, Vector4 row2, Vector4 row3, Vector4 row4)
    {
        this.m11 = row1.x;
        this.m12 = row1.y;
        this.m13 = row1.z;
        this.m14 = row1.w;
        this.m21 = row2.x;
        this.m22 = row2.y;
        this.m23 = row2.z;
        this.m24 = row2.w;
        this.m31 = row3.x;
        this.m32 = row3.y;
        this.m33 = row3.z;
        this.m34 = row3.w;
        this.m41 = row4.x;
        this.m42 = row4.y;
        this.m43 = row4.z;
        this.m44 = row4.w;
    }

    // #endregion

    // #region Public Fields

    /**
     * A first row and first column value.
     */
    public float m11;

    /**
     * A first row and second column value.
     */
    public float m12;

    /**
     * A first row and third column value.
     */
    public float m13;

    /**
     * A first row and fourth column value.
     */
    public float m14;

    /**
     * A second row and first column value.
     */
    public float m21;

    /**
     * A second row and second column value.
     */
    public float m22;

    /**
     * A second row and third column value.
     */
    public float m23;

    /**
     * A second row and fourth column value.
     */
    public float m24;

    /**
     * A third row and first column value.
     */
    public float m31;

    /**
     * A third row and second column value.
     */
    public float m32;

    /**
     * A third row and third column value.
     */
    public float m33;

    /**
     * A third row and fourth column value.
     */
    public float m34;

    /**
     * A fourth row and first column value.
     */
    public float m41;

    /**
     * A fourth row and second column value.
     */
    public float m42;

    /**
     * A fourth row and third column value.
     */
    public float m43;

    /**
     * A fourth row and fourth column value.
     */
    public float m44;

    // #endregion

    // #region Indexers

    public float getMatrixValue(int index)
    {
        switch (index)
        {
            case 0:
                return m11;
            case 1:
                return m12;
            case 2:
                return m13;
            case 3:
                return m14;
            case 4:
                return m21;
            case 5:
                return m22;
            case 6:
                return m23;
            case 7:
                return m24;
            case 8:
                return m31;
            case 9:
                return m32;
            case 10:
                return m33;
            case 11:
                return m34;
            case 12:
                return m41;
            case 13:
                return m42;
            case 14:
                return m43;
            case 15:
                return m44;
        }
        throw new IllegalArgumentException();
    }

    public void setMatrixValue(int index, float value)
    {
        switch (index)
        {
            case 0:
                m11 = value;
                break;
            case 1:
                m12 = value;
                break;
            case 2:
                m13 = value;
                break;
            case 3:
                m14 = value;
                break;
            case 4:
                m21 = value;
                break;
            case 5:
                m22 = value;
                break;
            case 6:
                m23 = value;
                break;
            case 7:
                m24 = value;
                break;
            case 8:
                m31 = value;
                break;
            case 9:
                m32 = value;
                break;
            case 10:
                m33 = value;
                break;
            case 11:
                m34 = value;
                break;
            case 12:
                m41 = value;
                break;
            case 13:
                m42 = value;
                break;
            case 14:
                m43 = value;
                break;
            case 15:
                m44 = value;
                break;
            default:
                throw new IllegalArgumentException("index is out of range");
        }
    }

    public float getMatrixValue(int row, int column)
    {
        return getMatrixValue((row * 4) + column);
    }

    public void setMatrixValue(int row, int column, float value)
    {
        setMatrixValue((row * 4) + column, value);
    }

    // #endregion

    // #region Private Members
    private static final Matrix identity = new Matrix(1f, 0f, 0f, 0f,
                                                      0f, 1f, 0f, 0f,
                                                      0f, 0f, 1f, 0f,
                                                      0f, 0f, 0f, 1f);

    // #endregion

    // #region Public Properties

    /**
     * Returns the backward vector formed from the third row m31, m32, m33 elements.
     * @return The backward vector stored in this {@link Matrix}.
     */
    public Vector3 backward()
    {
        return new Vector3(this.m31, this.m32, this.m33);
    }

    public void setBackward(Vector3 value)
    {
        this.m31 = value.x;
        this.m32 = value.y;
        this.m33 = value.z;
    }

    /**
     * Returns the down vector formed from the second row -m21, -m22, -m23 elements.
     * @return The down vector stored in this {@link Matrix}.
     */
    public Vector3 down()
    {
        return new Vector3(-this.m21, -this.m22, -this.m23);
    }

    public void setDown(Vector3 value)
    {
        this.m21 = -value.x;
        this.m22 = -value.y;
        this.m23 = -value.z;
    }

    /**
     * Returns the forward vector formed from the third row -m31, -m32, -m33 elements.
     * @return The forward vector stored in this {@link Matrix}.
     */
    public Vector3 forward()
    {
        return new Vector3(-this.m31, -this.m32, -this.m33);
    }

    public void setForward(Vector3 value)
    {
        this.m31 = -value.x;
        this.m32 = -value.y;
        this.m33 = -value.z;
    }

    /**
     * Returns the identity matrix.
     * @return The identity matrix.
     */
    public static Matrix identity()
    {
        return new Matrix(identity);
    }

    /**
     * Returns the left vector formed from the first row -m11, -m12, -m13 elements.
     * @return The left vector stored in this {@link Matrix}.
     */
    public Vector3 left()
    {
        return new Vector3(-this.m11, -this.m12, -this.m13);
    }

    public void setLeft(Vector3 value)
    {
        this.m11 = -value.x;
        this.m12 = -value.y;
        this.m13 = -value.z;
    }

    /**
     * Returns the right vector formed from the first row m11, m12, m13 elements.
     * @return The right vector stored in this {@link Matrix}.
     */
    public Vector3 right()
    {
        return new Vector3(this.m11, this.m12, this.m13);
    }

    public void setRight(Vector3 value)
    {
        this.m11 = value.x;
        this.m12 = value.y;
        this.m13 = value.z;
    }

    /**
     * Returns the rotation stored in this matrix.
     * 
     * @return The rotation stored in this matrix.
     */
    public Quaternion getRotation()
    {
        return Quaternion.createFromRotationMatrix(this);
    }

    /**
     * Returns the position stored in this matrix.
     * 
     * @return The position stored in this matrix.
     */
    public Vector3 translation()
    {
        return new Vector3(this.m41, this.m42, this.m43);
    }

    public void setTranslation(Vector3 value)
    {
        this.m41 = value.x;
        this.m42 = value.y;
        this.m43 = value.z;
    }

    /**
     * Returns the scale stored in this matrix.
     * 
     * @return The scale stored in this matrix.
     */
    public Vector3 scale()
    {
        return new Vector3(this.m11, this.m22, this.m33);
    }

    public void setScale(Vector3 value)
    {
        this.m11 = value.x;
        this.m22 = value.y;
        this.m33 = value.z;
    }

    /**
     * Returns the upper vector formed from the second row M21, M22, M23 elements.
     * @return The upper vector stored in this {@link Matrix}.
     */
    public Vector3 up()
    {
        return new Vector3(this.m21, this.m22, this.m23);
    }

    public void setUp(Vector3 value)
    {
        this.m21 = value.x;
        this.m22 = value.y;
        this.m23 = value.z;
    }

    // #endregion

    // #region Public Methods

    /**
     * Creates a new {@link Matrix} which contains sum of two matrixes.
     * @param matrix1 The first matrix to add.
     * @param matrix2 The second matrix to add.
     * @return The result of the matrix addition.
     */
    public static Matrix add(Matrix matrix1, Matrix matrix2)
    {
        Matrix result = new Matrix(matrix1);
        result.m11 += matrix2.m11;
        result.m12 += matrix2.m12;
        result.m13 += matrix2.m13;
        result.m14 += matrix2.m14;
        result.m21 += matrix2.m21;
        result.m22 += matrix2.m22;
        result.m23 += matrix2.m23;
        result.m24 += matrix2.m24;
        result.m31 += matrix2.m31;
        result.m32 += matrix2.m32;
        result.m33 += matrix2.m33;
        result.m34 += matrix2.m34;
        result.m41 += matrix2.m41;
        result.m42 += matrix2.m42;
        result.m43 += matrix2.m43;
        result.m44 += matrix2.m44;
        return result;
    }

    /**
     * Creates a new {@link Matrix} which contains sum of two matrixes.
     * @param matrix1 The first matrix to add.
     * @param matrix2 The second matrix to add.
     * @param result The result of the matrix addition as an output parameter.
     */
    public static void add(final Matrix matrix1, final Matrix matrix2, Matrix result)
    {
        result.m11 = matrix1.m11 + matrix2.m11;
        result.m12 = matrix1.m12 + matrix2.m12;
        result.m13 = matrix1.m13 + matrix2.m13;
        result.m14 = matrix1.m14 + matrix2.m14;
        result.m21 = matrix1.m21 + matrix2.m21;
        result.m22 = matrix1.m22 + matrix2.m22;
        result.m23 = matrix1.m23 + matrix2.m23;
        result.m24 = matrix1.m24 + matrix2.m24;
        result.m31 = matrix1.m31 + matrix2.m31;
        result.m32 = matrix1.m32 + matrix2.m32;
        result.m33 = matrix1.m33 + matrix2.m33;
        result.m34 = matrix1.m34 + matrix2.m34;
        result.m41 = matrix1.m41 + matrix2.m41;
        result.m42 = matrix1.m42 + matrix2.m42;
        result.m43 = matrix1.m43 + matrix2.m43;
        result.m44 = matrix1.m44 + matrix2.m44;
    }

    /**
     * Creates a new {@link Matrix} for spherical billboarding that rotates around specified object position.
     * @param objectPosition Position of billboard object. It will rotate around that vector.
     * @param cameraPosition The camera position.
     * @param cameraUpVector The camera up vector.
     * @param cameraForwardVector Optional camera forward vector.
     * @return The {@link Matrix} for spherical billboarding.
     */
    public static Matrix createBillboard(Vector3 objectPosition, Vector3 cameraPosition,
                                         Vector3 cameraUpVector, Vector3 cameraForwardVector)
    {
        Matrix result = new Matrix();

        // Delegate to the other overload of the function to do the work
        createBillboard(objectPosition, cameraPosition, cameraUpVector, cameraForwardVector, result);

        return result;
    }

    /**
     * Creates a new {@link Matrix} for spherical billboarding that rotates around specified object position.
     * @param objectPosition Position of billboard object. It will rotate around that vector.
     * @param cameraPosition The camera position.
     * @param cameraUpVector The camera up vector.
     * @param cameraForwardVector Optional camera forward vector.
     * @param result The {@link Matrix} for spherical billboarding as an output parameter.
     */
    public static void createBillboard(final Vector3 objectPosition, final Vector3 cameraPosition,
                                       final Vector3 cameraUpVector, Vector3 cameraForwardVector, Matrix result)
    {
        Vector3 vector = new Vector3();
        Vector3 vector2 = new Vector3();
        Vector3 vector3 = new Vector3();
        vector.x = objectPosition.x - cameraPosition.x;
        vector.y = objectPosition.y - cameraPosition.y;
        vector.z = objectPosition.z - cameraPosition.z;
        float num = vector.lengthSquared();
        if (num < 0.0001f)
        {
            vector = cameraForwardVector != null ? cameraForwardVector.negate() : Vector3.Forward();
        }
        else
        {
            Vector3.multiply(vector, (float) (1f / ((float) Math.sqrt((double) num))), vector);
        }
        Vector3.cross(cameraUpVector, vector, vector3);
        vector3.normalize();
        Vector3.cross(vector, vector3, vector2);
        result.m11 = vector3.x;
        result.m12 = vector3.y;
        result.m13 = vector3.z;
        result.m14 = 0;
        result.m21 = vector2.x;
        result.m22 = vector2.y;
        result.m23 = vector2.z;
        result.m24 = 0;
        result.m31 = vector.x;
        result.m32 = vector.y;
        result.m33 = vector.z;
        result.m34 = 0;
        result.m41 = objectPosition.x;
        result.m42 = objectPosition.y;
        result.m43 = objectPosition.z;
        result.m44 = 1;
    }

    /**
     * Creates a new {@link Matrix} for cylindrical billboarding that rotates around specified axis.
     * @param objectPosition Object position the billboard will rotate around.
     * @param cameraPosition Camera position.
     * @param rotateAxis Axis of billboard for rotation.
     * @param cameraForwardVector Optional camera forward vector.
     * @param objectForwardVector Optional object forward vector.
     * @return The {@link Matrix} for cylindrical billboarding.
     */
    public static Matrix createConstrainedBillboard(Vector3 objectPosition, Vector3 cameraPosition,
                                                    Vector3 rotateAxis, Vector3 cameraForwardVector, Vector3 objectForwardVector)
    {
        Matrix result = new Matrix();
        createConstrainedBillboard(objectPosition, cameraPosition, rotateAxis,
                                   cameraForwardVector, objectForwardVector, result);
        return result;
    }

    /**
     * Creates a new {@link Matrix} for cylindrical billboarding that rotates around specified axis.
     * @param objectPosition Object position the billboard will rotate around.
     * @param cameraPosition Camera position.
     * @param rotateAxis Axis of billboard for rotation.
     * @param cameraForwardVector Optional camera forward vector.
     * @param objectForwardVector Optional object forward vector.
     * @param result The {@link Matrix} for cylindrical billboarding as an output parameter.
     */
    public static void createConstrainedBillboard(final Vector3 objectPosition, final Vector3 cameraPosition,
                                                  final Vector3 rotateAxis, Vector3 cameraForwardVector, Vector3 objectForwardVector, Matrix result)
    {
        float num;
        Vector3 vector = new Vector3();
        Vector3 vector2 = new Vector3();
        Vector3 vector3 = new Vector3();
        vector2.x = objectPosition.x - cameraPosition.x;
        vector2.y = objectPosition.y - cameraPosition.y;
        vector2.z = objectPosition.z - cameraPosition.z;
        float num2 = vector2.lengthSquared();
        if (num2 < 0.0001f)
        {
            vector2 = cameraForwardVector != null ? cameraForwardVector.negate() : Vector3.Forward();
        }
        else
        {
            Vector3.multiply(vector2, (float) (1f / ((float) Math.sqrt((double) num2))), vector2);
        }
        Vector3 vector4 = new Vector3(rotateAxis);
        num = Vector3.dot(rotateAxis, vector2);
        if (Math.abs(num) > 0.9982547f)
        {
            if (objectForwardVector != null)
            {
                vector = new Vector3(objectForwardVector);
                num = Vector3.dot(rotateAxis, vector);
                if (Math.abs(num) > 0.9982547f)
                {
                    num = ((rotateAxis.x * Vector3.Forward().x) + (rotateAxis.y * Vector3.Forward().y)) + (rotateAxis.z * Vector3.Forward().z);
                    vector = (Math.abs(num) > 0.9982547f) ? Vector3.Right() : Vector3.Forward();
                }
            }
            else
            {
                num = ((rotateAxis.x * Vector3.Forward().x) + (rotateAxis.y * Vector3.Forward().y)) + (rotateAxis.z * Vector3.Forward().z);
                vector = (Math.abs(num) > 0.9982547f) ? Vector3.Right() : Vector3.Forward();
            }
            Vector3.cross(rotateAxis, vector, vector3);
            vector3.normalize();
            Vector3.cross(vector3, rotateAxis, vector);
            vector.normalize();
        }
        else
        {
            Vector3.cross(rotateAxis, vector2, vector3);
            vector3.normalize();
            Vector3.cross(vector3, vector4, vector);
            vector.normalize();
        }
        result.m11 = vector3.x;
        result.m12 = vector3.y;
        result.m13 = vector3.z;
        result.m14 = 0;
        result.m21 = vector4.x;
        result.m22 = vector4.y;
        result.m23 = vector4.z;
        result.m24 = 0;
        result.m31 = vector.x;
        result.m32 = vector.y;
        result.m33 = vector.z;
        result.m34 = 0;
        result.m41 = objectPosition.x;
        result.m42 = objectPosition.y;
        result.m43 = objectPosition.z;
        result.m44 = 1;

    }

    /**
     * Creates a new {@link Matrix} which contains the rotation moment around specified axis.
     * @param axis The axis of rotation.
     * @param angle The angle of rotation in radians.
     * @return The rotation {@link Matrix}.
     */
    public static Matrix createFromAxisAngle(Vector3 axis, float angle)
    {
        Matrix result = new Matrix();
        createFromAxisAngle(axis, angle, result);
        return result;
    }

    /**
     * Creates a new {@link Matrix} which contains the rotation moment around specified axis.
     * @param axis The axis of rotation.
     * @param angle The angle of rotation in radians.
     * @param result The rotation {@link Matrix} as an output parameter.
     */
    public static void createFromAxisAngle(final Vector3 axis, float angle, Matrix result)
    {
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
        float num2 = (float) Math.sin((double) angle);
        float num = (float) Math.cos((double) angle);
        float num11 = x * x;
        float num10 = y * y;
        float num9 = z * z;
        float num8 = x * y;
        float num7 = x * z;
        float num6 = y * z;
        result.m11 = num11 + (num * (1f - num11));
        result.m12 = (num8 - (num * num8)) + (num2 * z);
        result.m13 = (num7 - (num * num7)) - (num2 * y);
        result.m14 = 0;
        result.m21 = (num8 - (num * num8)) - (num2 * z);
        result.m22 = num10 + (num * (1f - num10));
        result.m23 = (num6 - (num * num6)) + (num2 * x);
        result.m24 = 0;
        result.m31 = (num7 - (num * num7)) + (num2 * y);
        result.m32 = (num6 - (num * num6)) - (num2 * x);
        result.m33 = num9 + (num * (1f - num9));
        result.m34 = 0;
        result.m41 = 0;
        result.m42 = 0;
        result.m43 = 0;
        result.m44 = 1;
    }

    /**
     * Creates a new rotation {@link Matrix} from a {@link Quaternion}.
     * @param quaternion {@link Quaternion} of rotation moment.
     * @return The rotation {@link Matrix}.
     */
    public static Matrix createFromQuaternion(Quaternion quaternion)
    {
        Matrix result = new Matrix();
        createFromQuaternion(quaternion, result);
        return result;
    }

    /**
     * Creates a new rotation {@link Matrix} from a {@link Quaternion}.
     * @param quaternion {@link Quaternion} of rotation moment.
     * @param result The rotation {@link Matrix} as an output parameter.
     */
    public static void createFromQuaternion(final Quaternion quaternion, Matrix result)
    {
        float num9 = quaternion.x * quaternion.x;
        float num8 = quaternion.y * quaternion.y;
        float num7 = quaternion.z * quaternion.z;
        float num6 = quaternion.x * quaternion.y;
        float num5 = quaternion.z * quaternion.w;
        float num4 = quaternion.z * quaternion.x;
        float num3 = quaternion.y * quaternion.w;
        float num2 = quaternion.y * quaternion.z;
        float num = quaternion.x * quaternion.w;
        result.m11 = 1f - (2f * (num8 + num7));
        result.m12 = 2f * (num6 + num5);
        result.m13 = 2f * (num4 - num3);
        result.m14 = 0f;
        result.m21 = 2f * (num6 - num5);
        result.m22 = 1f - (2f * (num7 + num9));
        result.m23 = 2f * (num2 + num);
        result.m24 = 0f;
        result.m31 = 2f * (num4 + num3);
        result.m32 = 2f * (num2 - num);
        result.m33 = 1f - (2f * (num8 + num9));
        result.m34 = 0f;
        result.m41 = 0f;
        result.m42 = 0f;
        result.m43 = 0f;
        result.m44 = 1f;
    }

    /**
     * Creates a new rotation {@link Matrix} from the specified yaw, pitch and roll values.
     * <p>
     * For more information about yaw, pitch and roll visit <a href="http://en.wikipedia.org/wiki/Euler_angles">http://en.wikipedia.org/wiki/Euler_angles</a>.
     * @param yaw The yaw rotation value in radians.
     * @param pitch The pitch rotation value in radians.
     * @param roll The roll rotation value in radians.
     * @return The rotation {@link Matrix}.
     */
    public static Matrix createFromYawPitchRoll(float yaw, float pitch, float roll)
    {
        Matrix result = new Matrix();
        createFromYawPitchRoll(yaw, pitch, roll, result);
        return result;
    }

    /**
     * Creates a new rotation {@link Matrix} from the specified yaw, pitch and roll values.
     * <p>
     * For more information about yaw, pitch and roll visit <a href="http://en.wikipedia.org/wiki/Euler_angles">http://en.wikipedia.org/wiki/Euler_angles</a>.
     * @param yaw The yaw rotation value in radians.
     * @param pitch The pitch rotation value in radians.
     * @param roll The roll rotation value in radians.
     * @param result The rotation {@link Matrix} as an output parameter.
     */
    public static void createFromYawPitchRoll(float yaw, float pitch, float roll, Matrix result)
    {
        Quaternion quaternion = new Quaternion();
        Quaternion.createFromYawPitchRoll(yaw, pitch, roll, quaternion);
        createFromQuaternion(quaternion, result);
    }

    /**
     * Creates a new viewing {@link Matrix}.
     * @param cameraPosition Position of the camera.
     * @param cameraTarget Lookup vector of the camera.
     * @param cameraUpVector The direction of the upper edge of the camera.
     * @return The viewing {@link Matrix}.
     */
    public static Matrix createLookAt(Vector3 cameraPosition, Vector3 cameraTarget, Vector3 cameraUpVector)
    {
        Matrix matrix = new Matrix();
        createLookAt(cameraPosition, cameraTarget, cameraUpVector, matrix);
        return matrix;
    }

    /**
     * Creates a new viewing {@link Matrix}.
     * @param cameraPosition Position of the camera.
     * @param cameraTarget Lookup vector of the camera.
     * @param cameraUpVector The direction of the upper edge of the camera.
     * @param result The viewing {@link Matrix} as an output parameter.
     */
    public static void createLookAt(final Vector3 cameraPosition, final Vector3 cameraTarget,
                                    final Vector3 cameraUpVector, Matrix result)
    {
        Vector3 vector = Vector3.normalize(Vector3.subtract(cameraPosition, cameraTarget));
        Vector3 vector2 = Vector3.normalize(Vector3.cross(cameraUpVector, vector));
        Vector3 vector3 = Vector3.cross(vector, vector2);
        result.m11 = vector2.x;
        result.m12 = vector3.x;
        result.m13 = vector.x;
        result.m14 = 0f;
        result.m21 = vector2.y;
        result.m22 = vector3.y;
        result.m23 = vector.y;
        result.m24 = 0f;
        result.m31 = vector2.z;
        result.m32 = vector3.z;
        result.m33 = vector.z;
        result.m34 = 0f;
        result.m41 = -Vector3.dot(vector2, cameraPosition);
        result.m42 = -Vector3.dot(vector3, cameraPosition);
        result.m43 = -Vector3.dot(vector, cameraPosition);
        result.m44 = 1f;
    }

    /**
     * Creates a new projection {@link Matrix} for orthographic view.
     * @param width Width of the viewing volume.
     * @param height Height of the viewing volume.
     * @param zNearPlane Depth of the near plane.
     * @param zFarPlane Depth of the far plane.
     * @return The new projection {@link Matrix} for orthographic view.
     */
    public static Matrix createOrthographic(float width, float height, float zNearPlane, float zFarPlane)
    {
        Matrix result = new Matrix();
        createOrthographic(width, height, zNearPlane, zFarPlane, result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for orthographic view.
     * @param width Width of the viewing volume.
     * @param height Height of the viewing volume.
     * @param zNearPlane Depth of the near plane.
     * @param zFarPlane Depth of the far plane.
     * @param result The new projection {@link Matrix} for orthographic view as an output parameter.
     */
    public static void createOrthographic(float width, float height,
                                          float zNearPlane, float zFarPlane, Matrix result)
    {
        result.m11 = 2f / width;
        result.m12 = result.m13 = result.m14 = 0f;
        result.m22 = 2f / height;
        result.m21 = result.m23 = result.m24 = 0f;
        result.m33 = 1f / (zNearPlane - zFarPlane);
        result.m31 = result.m32 = result.m34 = 0f;
        result.m41 = result.m42 = 0f;
        result.m43 = zNearPlane / (zNearPlane - zFarPlane);
        result.m44 = 1f;
    }

    /**
     * Creates a new projection {@link Matrix} for customized orthographic view.
     * @param left Lower x-value at the near plane.
     * @param right Upper x-value at the near plane.
     * @param bottom Lower y-coordinate at the near plane.
     * @param top Upper y-value at the near plane.
     * @param zNearPlane Depth of the near plane.
     * @param zFarPlane Depth of the far plane.
     * @return The new projection {@link Matrix} for customized orthographic view.
     */
    public static Matrix createOrthographicOffCenter(float left, float right, float bottom, float top,
                                                     float zNearPlane, float zFarPlane)
    {
        Matrix result = new Matrix();
        createOrthographicOffCenter(left, right, bottom, top, zNearPlane, zFarPlane, result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for customized orthographic view.
     * @param viewingVolume The viewing volume.
     * @param zNearPlane Depth of the near plane.
     * @param zFarPlane Depth of the far plane.
     * @return The new projection {@link Matrix} for customized orthographic view.
     */
    public static Matrix createOrthographicOffCenter(Rectangle viewingVolume,
                                                     float zNearPlane, float zFarPlane)
    {
        Matrix result = new Matrix();
        createOrthographicOffCenter(viewingVolume.left(), viewingVolume.right(),
                                    viewingVolume.bottom(), viewingVolume.top(),
                                    zNearPlane, zFarPlane,
                                    result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for customized orthographic view.
     * @param left Lower x-value at the near plane.
     * @param right Upper x-value at the near plane.
     * @param bottom Lower y-coordinate at the near plane.
     * @param top Upper y-value at the near plane.
     * @param zNearPlane Depth of the near plane.
     * @param zFarPlane Depth of the far plane.
     * @param result The new projection {@link Matrix} for customized orthographic view as an output parameter.
     */
    public static void createOrthographicOffCenter(float left, float right, float bottom, float top,
                                                   float zNearPlane, float zFarPlane, Matrix result)
    {
        result.m11 = (float) (2.0 / ((double) right - (double) left));
        result.m12 = 0.0f;
        result.m13 = 0.0f;
        result.m14 = 0.0f;
        result.m21 = 0.0f;
        result.m22 = (float) (2.0 / ((double) top - (double) bottom));
        result.m23 = 0.0f;
        result.m24 = 0.0f;
        result.m31 = 0.0f;
        result.m32 = 0.0f;
        result.m33 = (float) (1.0 / ((double) zNearPlane - (double) zFarPlane));
        result.m34 = 0.0f;
        result.m41 = (float) (((double) left + (double) right) / ((double) left - (double) right));
        result.m42 = (float) (((double) top + (double) bottom) / ((double) bottom - (double) top));
        result.m43 = (float) ((double) zNearPlane / ((double) zNearPlane - (double) zFarPlane));
        result.m44 = 1.0f;
    }

    /**
     * Creates a new projection {@link Matrix} for perspective view.
     * @param width Width of the viewing volume.
     * @param height Height of the viewing volume.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @return The new projection {@link Matrix} for perspective view.
     */
    public static Matrix createPerspective(float width, float height, float nearPlaneDistance, float farPlaneDistance)
    {
        Matrix result = new Matrix();
        createPerspective(width, height, nearPlaneDistance, farPlaneDistance, result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for perspective view.
     * @param width Width of the viewing volume.
     * @param height Height of the viewing volume.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @param result The new projection {@link Matrix} for perspective view as an output parameter.
     */
    public static void createPerspective(float width, float height,
                                         float nearPlaneDistance, float farPlaneDistance,
                                         Matrix result)
    {
        if (nearPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("nearPlaneDistance <= 0");
        }
        if (farPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("farPlaneDistance <= 0");
        }
        if (nearPlaneDistance >= farPlaneDistance)
        {
            throw new IllegalArgumentException("nearPlaneDistance >= farPlaneDistance");
        }
        result.m11 = (2f * nearPlaneDistance) / width;
        result.m12 = result.m13 = result.m14 = 0f;
        result.m22 = (2f * nearPlaneDistance) / height;
        result.m21 = result.m23 = result.m24 = 0f;
        result.m33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
        result.m31 = result.m32 = 0f;
        result.m34 = -1f;
        result.m41 = result.m42 = result.m44 = 0f;
        result.m43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
    }

    /**
     * Creates a new projection {@link Matrix} for perspective view with field of view.
     * @param fieldOfView Field of view in the y direction in radians.
     * @param aspectRatio Width divided by height of the viewing volume.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @return The new projection {@link Matrix} for perspective view with FOV.
     */
    public static Matrix createPerspectiveFieldOfView(float fieldOfView, float aspectRatio,
                                                      float nearPlaneDistance, float farPlaneDistance)
    {
        Matrix result = new Matrix();
        createPerspectiveFieldOfView(fieldOfView, aspectRatio, nearPlaneDistance, farPlaneDistance, result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for perspective view with field of view.
     * @param fieldOfView Field of view in the y direction in radians.
     * @param aspectRatio Width divided by height of the viewing volume.
     * @param nearPlaneDistance Distance of the near plane.
     * @param farPlaneDistance Distance of the far plane.
     * @param result The new projection {@link Matrix} for perspective view with FOV as an output parameter.
     */
    public static void createPerspectiveFieldOfView(float fieldOfView, float aspectRatio,
                                                    float nearPlaneDistance, float farPlaneDistance,
                                                    Matrix result)
    {
        if ((fieldOfView <= 0f) || (fieldOfView >= 3.141593f))
        {
            throw new IllegalArgumentException("fieldOfView <= 0 or >= PI");
        }
        if (nearPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("nearPlaneDistance <= 0");
        }
        if (farPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("farPlaneDistance <= 0");
        }
        if (nearPlaneDistance >= farPlaneDistance)
        {
            throw new IllegalArgumentException("nearPlaneDistance >= farPlaneDistance");
        }
        float num = 1f / ((float) Math.tan((double) (fieldOfView * 0.5f)));
        float num9 = num / aspectRatio;
        result.m11 = num9;
        result.m12 = result.m13 = result.m14 = 0;
        result.m22 = num;
        result.m21 = result.m23 = result.m24 = 0;
        result.m31 = result.m32 = 0f;
        result.m33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
        result.m34 = -1;
        result.m41 = result.m42 = result.m44 = 0;
        result.m43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
    }

    /**
     * Creates a new projection {@link Matrix} for customized perspective view.
     * @param left Lower x-value at the near plane.
     * @param right Upper x-value at the near plane.
     * @param bottom Lower y-coordinate at the near plane.
     * @param top Upper y-value at the near plane.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @return The new {@link Matrix} for customized perspective view.
     */
    public static Matrix createPerspectiveOffCenter(float left, float right, float bottom, float top,
                                                    float nearPlaneDistance, float farPlaneDistance)
    {
        Matrix result = new Matrix();
        createPerspectiveOffCenter(left, right, bottom, top, nearPlaneDistance, farPlaneDistance, result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for customized perspective view.
     * @param viewingVolume The viewing volume.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @return The new {@link Matrix} for customized perspective view.
     */
    public static Matrix createPerspectiveOffCenter(Rectangle viewingVolume,
                                                    float nearPlaneDistance, float farPlaneDistance)
    {
        Matrix result = new Matrix();
        createPerspectiveOffCenter(viewingVolume.left(), viewingVolume.right(),
                                   viewingVolume.bottom(), viewingVolume.top(),
                                   nearPlaneDistance, farPlaneDistance,
                                   result);
        return result;
    }

    /**
     * Creates a new projection {@link Matrix} for customized perspective view.
     * @param left Lower x-value at the near plane.
     * @param right Upper x-value at the near plane.
     * @param bottom Lower y-coordinate at the near plane.
     * @param top Upper y-value at the near plane.
     * @param nearPlaneDistance Distance to the near plane.
     * @param farPlaneDistance Distance to the far plane.
     * @param result The new {@link Matrix} for customized perspective view as an output parameter.
     */
    public static void createPerspectiveOffCenter(float left, float right, float bottom, float top,
                                                  float nearPlaneDistance, float farPlaneDistance,
                                                  Matrix result)
    {
        if (nearPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("nearPlaneDistance <= 0");
        }
        if (farPlaneDistance <= 0f)
        {
            throw new IllegalArgumentException("farPlaneDistance <= 0");
        }
        if (nearPlaneDistance >= farPlaneDistance)
        {
            throw new IllegalArgumentException("nearPlaneDistance >= farPlaneDistance");
        }
        result.m11 = (2f * nearPlaneDistance) / (right - left);
        result.m12 = result.m13 = result.m14 = 0;
        result.m22 = (2f * nearPlaneDistance) / (top - bottom);
        result.m21 = result.m23 = result.m24 = 0;
        result.m31 = (left + right) / (right - left);
        result.m32 = (top + bottom) / (top - bottom);
        result.m33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
        result.m34 = -1;
        result.m43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
        result.m41 = result.m42 = result.m44 = 0;
    }

    /**
     * Creates a new rotation {@link Matrix} around X axis.
     * @param radians Angle in radians.
     * @return The rotation {@link Matrix} around X axis.
     */
    public static Matrix createRotationX(float radians)
    {
        Matrix result = new Matrix();
        createRotationX(radians, result);
        return result;
    }

    /**
     * Creates a new rotation {@link Matrix} around X axis.
     * @param radians Angle in radians.
     * @param result The rotation {@link Matrix} around X axis as an output parameter.
     */
    public static void createRotationX(float radians, Matrix result)
    {
        result = Matrix.identity();

        float val1 = (float) Math.cos(radians);
        float val2 = (float) Math.sin(radians);

        result.m22 = val1;
        result.m23 = val2;
        result.m32 = -val2;
        result.m33 = val1;
    }

    /**
     * Creates a new rotation {@link Matrix} around Y axis.
     * @param radians Angle in radians.
     * @return The rotation {@link Matrix} around Y axis.
     */
    public static Matrix createRotationY(float radians)
    {
        Matrix result = new Matrix();
        createRotationY(radians, result);
        return result;
    }

    /**
     * Creates a new rotation {@link Matrix} around Y axis.
     * @param radians Angle in radians.
     * @param result The rotation {@link Matrix} around Y axis as an output parameter.
     */
    public static void createRotationY(float radians, Matrix result)
    {
        result = Matrix.identity();

        float val1 = (float) Math.cos(radians);
        float val2 = (float) Math.sin(radians);

        result.m11 = val1;
        result.m13 = -val2;
        result.m31 = val2;
        result.m33 = val1;
    }

    /**
     * Creates a new rotation {@link Matrix} around Z axis.
     * @param radians Angle in radians.
     * @return The rotation {@link Matrix} around Z axis.
     */
    public static Matrix createRotationZ(float radians)
    {
        Matrix result = new Matrix();
        createRotationZ(radians, result);
        return result;
    }

    /**
     * Creates a new rotation {@link Matrix} around Z axis.
     * @param radians Angle in radians.
     * @param result The rotation {@link Matrix} around Z axis as an output parameter.
     */
    public static void createRotationZ(float radians, Matrix result)
    {
        result = Matrix.identity();

        float val1 = (float) Math.cos(radians);
        float val2 = (float) Math.sin(radians);

        result.m11 = val1;
        result.m12 = val2;
        result.m21 = -val2;
        result.m22 = val1;
    }

    /**
     * Creates a new scaling {@link Matrix}.
     * @param scale Scale value for all three axes.
     * @return The scaling {@link Matrix}.
     */
    public static Matrix createScale(float scale)
    {
        Matrix result = new Matrix();
        createScale(scale, scale, scale, result);
        return result;
    }

    /**
     * Creates a new scaling {@link Matrix}.
     * @param scale Scale value for all three axes.
     * @param result The scaling {@link Matrix} as an output parameter.
     */
    public static void createScale(float scale, Matrix result)
    {
        createScale(scale, scale, scale, result);
    }

    // <summary>
    // Creates a new scaling {@link Matrix}.
    // </summary>
    // <param name="xScale">Scale value for X axis.
    // <param name="yScale">Scale value for Y axis.
    // <param name="zScale">Scale value for Z axis.
    // <returns>The scaling {@link Matrix}.
    public static Matrix createScale(float xScale, float yScale, float zScale)
    {
        Matrix result = new Matrix();
        createScale(xScale, yScale, zScale, result);
        return result;
    }

    // <summary>
    // Creates a new scaling {@link Matrix}.
    // </summary>
    // <param name="xScale">Scale value for X axis.
    // <param name="yScale">Scale value for Y axis.
    // <param name="zScale">Scale value for Z axis.
    // <param name="result">The scaling {@link Matrix} as an output parameter.
    public static void createScale(float xScale, float yScale, float zScale, Matrix result)
    {
        result.m11 = xScale;
        result.m12 = 0;
        result.m13 = 0;
        result.m14 = 0;
        result.m21 = 0;
        result.m22 = yScale;
        result.m23 = 0;
        result.m24 = 0;
        result.m31 = 0;
        result.m32 = 0;
        result.m33 = zScale;
        result.m34 = 0;
        result.m41 = 0;
        result.m42 = 0;
        result.m43 = 0;
        result.m44 = 1;
    }

    // <summary>
    // Creates a new scaling {@link Matrix}.
    // </summary>
    // <param name="scales">{@link Vector3} representing x,y and z scale values.
    // <returns>The scaling {@link Matrix}.
    public static Matrix createScale(Vector3 scales)
    {
        Matrix result = new Matrix();
        createScale(scales, result);
        return result;
    }

    // <summary>
    // Creates a new scaling {@link Matrix}.
    // </summary>
    // <param name="scales">{@link Vector3} representing x,y and z scale values.
    // <param name="result">The scaling {@link Matrix} as an output parameter.
    public static void createScale(final Vector3 scales, Matrix result)
    {
        result.m11 = scales.x;
        result.m12 = 0;
        result.m13 = 0;
        result.m14 = 0;
        result.m21 = 0;
        result.m22 = scales.y;
        result.m23 = 0;
        result.m24 = 0;
        result.m31 = 0;
        result.m32 = 0;
        result.m33 = scales.z;
        result.m34 = 0;
        result.m41 = 0;
        result.m42 = 0;
        result.m43 = 0;
        result.m44 = 1;
    }

    /**
     * Creates a {@link Matrix} that flattens geometry into a specified {@link Plane} as if casting
     * a shadow from a specified light source.
     * 
     * @param lightDirection
     *        A Vector3 specifying the direction from which the light that will cast the shadow is
     *        coming.
     * @param plane
     *        The Plane onto which the new matrix should flatten geometry so as to cast a shadow.
     * @return A Matrix that can be used to flatten geometry onto the specified plane from the
     *         specified direction.
     */
    public static Matrix createShadow(Vector3 lightDirection, Plane plane)
    {
        Matrix result = new Matrix();
        createShadow(lightDirection, plane, result);
        return result;
    }

    /**
     * Creates a {@link Matrix that flattens geometry into a specified {@link Plane} as if casting
     * a shadow from a specified light source.
     * 
     * @param lightDirection
     *        A Vector3 specifying the direction from which the light that will cast the shadow is
     *        coming.
     * @param plane
     *        The Plane onto which the new matrix should flatten geometry so as to cast a shadow.
     * @param result
     *        A Matrix that can be used to flatten geometry onto the specified plane from the
     *        specified direction.
     */
    public static void createShadow(final Vector3 lightDirection, final Plane plane, Matrix result)
    {
        float dot = (plane.normal.x * lightDirection.x) + (plane.normal.y * lightDirection.y) + (plane.normal.z * lightDirection.z);
        float x = -plane.normal.x;
        float y = -plane.normal.y;
        float z = -plane.normal.z;
        float d = -plane.D;

        result.m11 = (x * lightDirection.x) + dot;
        result.m12 = x * lightDirection.y;
        result.m13 = x * lightDirection.z;
        result.m14 = 0;
        result.m21 = y * lightDirection.x;
        result.m22 = (y * lightDirection.y) + dot;
        result.m23 = y * lightDirection.z;
        result.m24 = 0;
        result.m31 = z * lightDirection.x;
        result.m32 = z * lightDirection.y;
        result.m33 = (z * lightDirection.z) + dot;
        result.m34 = 0;
        result.m41 = d * lightDirection.x;
        result.m42 = d * lightDirection.y;
        result.m43 = d * lightDirection.z;
        result.m44 = dot;
    }

    // <summary>
    // Creates a new translation {@link Matrix}.
    // </summary>
    // <param name="xPosition">X coordinate of translation.
    // <param name="yPosition">Y coordinate of translation.
    // <param name="zPosition">Z coordinate of translation.
    // <returns>The translation {@link Matrix}.
    public static Matrix createTranslation(float xPosition, float yPosition, float zPosition)
    {
        Matrix result = new Matrix();
        createTranslation(xPosition, yPosition, zPosition, result);
        return result;
    }

    // <summary>
    // Creates a new translation {@link Matrix}.
    // </summary>
    // <param name="position">X,Y and Z coordinates of translation.
    // <param name="result">The translation {@link Matrix} as an output parameter.
    public static void createTranslation(final Vector3 position, Matrix result)
    {
        result.m11 = 1;
        result.m12 = 0;
        result.m13 = 0;
        result.m14 = 0;
        result.m21 = 0;
        result.m22 = 1;
        result.m23 = 0;
        result.m24 = 0;
        result.m31 = 0;
        result.m32 = 0;
        result.m33 = 1;
        result.m34 = 0;
        result.m41 = position.x;
        result.m42 = position.y;
        result.m43 = position.z;
        result.m44 = 1;
    }

    // <summary>
    // Creates a new translation {@link Matrix}.
    // </summary>
    // <param name="position">X,Y and Z coordinates of translation.
    // <returns>The translation {@link Matrix}.
    public static Matrix createTranslation(Vector3 position)
    {
        Matrix result = new Matrix();
        createTranslation(position, result);
        return result;
    }

    // <summary>
    // Creates a new translation {@link Matrix}.
    // </summary>
    // <param name="xPosition">X coordinate of translation.
    // <param name="yPosition">Y coordinate of translation.
    // <param name="zPosition">Z coordinate of translation.
    // <param name="result">The translation {@link Matrix} as an output parameter.
    public static void createTranslation(float xPosition, float yPosition, float zPosition, Matrix result)
    {
        result.m11 = 1;
        result.m12 = 0;
        result.m13 = 0;
        result.m14 = 0;
        result.m21 = 0;
        result.m22 = 1;
        result.m23 = 0;
        result.m24 = 0;
        result.m31 = 0;
        result.m32 = 0;
        result.m33 = 1;
        result.m34 = 0;
        result.m41 = xPosition;
        result.m42 = yPosition;
        result.m43 = zPosition;
        result.m44 = 1;
    }

    // <summary>
    // Creates a new reflection {@link Matrix}.
    // </summary>
    // <param name="value">The plane that used for reflection calculation.
    // <returns>The reflection {@link Matrix}.
    public static Matrix createReflection(Plane value)
    {
        Matrix result = new Matrix();
        createReflection(value, result);
        return result;
    }

    // <summary>
    // Creates a new reflection {@link Matrix}.
    // </summary>
    // <param name="value">The plane that used for reflection calculation.
    // <param name="result">The reflection {@link Matrix} as an output parameter.
    public static void createReflection(final Plane value, Matrix result)
    {
        Plane plane = new Plane();
        Plane.normalize(value, plane);
        value.normalize();
        float x = plane.normal.x;
        float y = plane.normal.y;
        float z = plane.normal.z;
        float num3 = -2f * x;
        float num2 = -2f * y;
        float num = -2f * z;
        result.m11 = (num3 * x) + 1f;
        result.m12 = num2 * x;
        result.m13 = num * x;
        result.m14 = 0;
        result.m21 = num3 * y;
        result.m22 = (num2 * y) + 1;
        result.m23 = num * y;
        result.m24 = 0;
        result.m31 = num3 * z;
        result.m32 = num2 * z;
        result.m33 = (num * z) + 1;
        result.m34 = 0;
        result.m41 = num3 * plane.D;
        result.m42 = num2 * plane.D;
        result.m43 = num * plane.D;
        result.m44 = 1;
    }

    // <summary>
    // Creates a new world {@link Matrix}.
    // </summary>
    // <param name="position">The position vector.
    // <param name="forward">The forward direction vector.
    // <param name="up">The upward direction vector. Usually {@link Vector3#up()}.
    // <returns>The world {@link Matrix}.
    public static Matrix createWorld(Vector3 position, Vector3 forward, Vector3 up)
    {
        Matrix result = new Matrix();
        createWorld(position, forward, up, result);
        return result;
    }

    // <summary>
    // Creates a new world {@link Matrix}.
    // </summary>
    // <param name="position">The position vector.
    // <param name="forward">The forward direction vector.
    // <param name="up">The upward direction vector. Usually {@link Vector3#up()}.
    // <param name="result">The world {@link Matrix} as an output parameter.
    public static void createWorld(final Vector3 position, final Vector3 forward, final Vector3 up, Matrix result)
    {
        Vector3 x = new Vector3();
        Vector3 y = new Vector3();
        Vector3 z = new Vector3();
        Vector3.normalize(forward, z);
        Vector3.cross(forward, up, x);
        Vector3.cross(x, forward, y);
        x.normalize();
        y.normalize();

        result.setRight(x);
        result.setUp(y);
        result.setForward(z);
        result.setTranslation(position);
        result.m44 = 1f;
    }

    // <summary>
    // Decomposes this matrix to translation, rotation and scale elements. Returns <c>true</c> if
    // matrix can be decomposed; <c>false</c> otherwise.
    // </summary>
    // <param name="scale">Scale vector as an output parameter.
    // <param name="rotation">Rotation quaternion as an output parameter.
    // <param name="translation">Translation vector as an output parameter.
    // <returns><c>true</c> if matrix can be decomposed; <c>false</c> otherwise.
    public boolean decompose(Vector3 scale, Quaternion rotation, Vector3 translation)
    {
        translation.x = this.m41;
        translation.y = this.m42;
        translation.z = this.m43;

        float xs = (Math.signum(m11 * m12 * m13 * m14) < 0) ? -1 : 1;
        float ys = (Math.signum(m21 * m22 * m23 * m24) < 0) ? -1 : 1;
        float zs = (Math.signum(m31 * m32 * m33 * m34) < 0) ? -1 : 1;

        scale.x = xs * (float) Math.sqrt(this.m11 * this.m11 + this.m12 * this.m12 + this.m13 * this.m13);
        scale.y = ys * (float) Math.sqrt(this.m21 * this.m21 + this.m22 * this.m22 + this.m23 * this.m23);
        scale.z = zs * (float) Math.sqrt(this.m31 * this.m31 + this.m32 * this.m32 + this.m33 * this.m33);

        if (scale.x == 0.0 || scale.y == 0.0 || scale.z == 0.0)
        {
            rotation = Quaternion.identity();
            return false;
        }

        Matrix m1 = new Matrix(this.m11 / scale.x, m12 / scale.x, m13 / scale.x, 0,
                               this.m21 / scale.y, m22 / scale.y, m23 / scale.y, 0,
                               this.m31 / scale.z, m32 / scale.z, m33 / scale.z, 0,
                               0, 0, 0, 1);

        rotation = Quaternion.createFromRotationMatrix(m1);
        return true;
    }

    // <summary>
    // Returns a determinant of this {@link Matrix}.
    // </summary>
    // <returns>Determinant of this {@link Matrix}</returns>
    // <remarks>See more about determinant here - http://en.wikipedia.org/wiki/Determinant.
    // </remarks>
    public float determinant()
    {
        float num22 = this.m11;
        float num21 = this.m12;
        float num20 = this.m13;
        float num19 = this.m14;
        float num12 = this.m21;
        float num11 = this.m22;
        float num10 = this.m23;
        float num9 = this.m24;
        float num8 = this.m31;
        float num7 = this.m32;
        float num6 = this.m33;
        float num5 = this.m34;
        float num4 = this.m41;
        float num3 = this.m42;
        float num2 = this.m43;
        float num = this.m44;
        float num18 = (num6 * num) - (num5 * num2);
        float num17 = (num7 * num) - (num5 * num3);
        float num16 = (num7 * num2) - (num6 * num3);
        float num15 = (num8 * num) - (num5 * num4);
        float num14 = (num8 * num2) - (num6 * num4);
        float num13 = (num8 * num3) - (num7 * num4);
        return ((((num22 * (((num11 * num18) - (num10 * num17)) + (num9 * num16))) - (num21 * (((num12 * num18) - (num10 * num15)) + (num9 * num14)))) + (num20 * (((num12 * num17) - (num11 * num15)) + (num9 * num13)))) - (num19 * (((num12 * num16) - (num11 * num14)) + (num10 * num13))));
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by the elements of another matrix.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="matrix2">Divisor {@link Matrix}.
    // <returns>The result of dividing the matrix.
    public static Matrix divide(Matrix matrix1, Matrix matrix2)
    {
        Matrix result = new Matrix();
        result.m11 = matrix1.m11 / matrix2.m11;
        result.m12 = matrix1.m12 / matrix2.m12;
        result.m13 = matrix1.m13 / matrix2.m13;
        result.m14 = matrix1.m14 / matrix2.m14;
        result.m21 = matrix1.m21 / matrix2.m21;
        result.m22 = matrix1.m22 / matrix2.m22;
        result.m23 = matrix1.m23 / matrix2.m23;
        result.m24 = matrix1.m24 / matrix2.m24;
        result.m31 = matrix1.m31 / matrix2.m31;
        result.m32 = matrix1.m32 / matrix2.m32;
        result.m33 = matrix1.m33 / matrix2.m33;
        result.m34 = matrix1.m34 / matrix2.m34;
        result.m41 = matrix1.m41 / matrix2.m41;
        result.m42 = matrix1.m42 / matrix2.m42;
        result.m43 = matrix1.m43 / matrix2.m43;
        result.m44 = matrix1.m44 / matrix2.m44;
        return result;
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by the elements of another matrix.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="matrix2">Divisor {@link Matrix}.
    // <param name="result">The result of dividing the matrix as an output parameter.
    public static void divide(final Matrix matrix1, final Matrix matrix2, Matrix result)
    {
        result.m11 = matrix1.m11 / matrix2.m11;
        result.m12 = matrix1.m12 / matrix2.m12;
        result.m13 = matrix1.m13 / matrix2.m13;
        result.m14 = matrix1.m14 / matrix2.m14;
        result.m21 = matrix1.m21 / matrix2.m21;
        result.m22 = matrix1.m22 / matrix2.m22;
        result.m23 = matrix1.m23 / matrix2.m23;
        result.m24 = matrix1.m24 / matrix2.m24;
        result.m31 = matrix1.m31 / matrix2.m31;
        result.m32 = matrix1.m32 / matrix2.m32;
        result.m33 = matrix1.m33 / matrix2.m33;
        result.m34 = matrix1.m34 / matrix2.m34;
        result.m41 = matrix1.m41 / matrix2.m41;
        result.m42 = matrix1.m42 / matrix2.m42;
        result.m43 = matrix1.m43 / matrix2.m43;
        result.m44 = matrix1.m44 / matrix2.m44;
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by a scalar.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="divider">Divisor scalar.
    // <returns>The result of dividing a matrix by a scalar.
    public static Matrix divide(Matrix matrix1, float divider)
    {
        Matrix result = new Matrix();
        float num = 1f / divider;
        result.m11 = matrix1.m11 * num;
        result.m12 = matrix1.m12 * num;
        result.m13 = matrix1.m13 * num;
        result.m14 = matrix1.m14 * num;
        result.m21 = matrix1.m21 * num;
        result.m22 = matrix1.m22 * num;
        result.m23 = matrix1.m23 * num;
        result.m24 = matrix1.m24 * num;
        result.m31 = matrix1.m31 * num;
        result.m32 = matrix1.m32 * num;
        result.m33 = matrix1.m33 * num;
        result.m34 = matrix1.m34 * num;
        result.m41 = matrix1.m41 * num;
        result.m42 = matrix1.m42 * num;
        result.m43 = matrix1.m43 * num;
        result.m44 = matrix1.m44 * num;
        return result;
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by a scalar.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="divider">Divisor scalar.
    // <param name="result">The result of dividing a matrix by a scalar as an output
    // parameter.
    public static void divide(final Matrix matrix1, float divider, Matrix result)
    {
        float num = 1f / divider;
        result.m11 = matrix1.m11 * num;
        result.m12 = matrix1.m12 * num;
        result.m13 = matrix1.m13 * num;
        result.m14 = matrix1.m14 * num;
        result.m21 = matrix1.m21 * num;
        result.m22 = matrix1.m22 * num;
        result.m23 = matrix1.m23 * num;
        result.m24 = matrix1.m24 * num;
        result.m31 = matrix1.m31 * num;
        result.m32 = matrix1.m32 * num;
        result.m33 = matrix1.m33 * num;
        result.m34 = matrix1.m34 * num;
        result.m41 = matrix1.m41 * num;
        result.m42 = matrix1.m42 * num;
        result.m43 = matrix1.m43 * num;
        result.m44 = matrix1.m44 * num;
    }

    // <summary>
    // Compares whether current instance is equal to specified {@link Matrix} without any
    // tolerance.
    // </summary>
    // <param name="other">The {@link Matrix} to compare.
    // <returns><c>true</c> if the instances are equal; <c>false</c> otherwise.
    public boolean equals(Matrix other)
    {
        return (this.m11 == other.m11 && this.m12 == other.m12 && this.m13 == other.m13 && this.m14 == other.m14 &&
                this.m21 == other.m21 && this.m22 == other.m22 && this.m23 == other.m23 && this.m24 == other.m24 &&
                this.m31 == other.m31 && this.m32 == other.m32 && this.m33 == other.m33 && this.m34 == other.m34 &&
                this.m41 == other.m41 && this.m42 == other.m42 && this.m43 == other.m43 && this.m44 == other.m44);
    }

    /**
     * Indicates whether the current instance is equal to the specified {@link Object} without any
     * tolerance.
     * 
     * @param obj
     *        The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
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
        return this.equals((Matrix) obj);
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

    // <summary>
    // Gets the hash code of this {@link Matrix}.
    // </summary>
    // <returns>Hash code of this {@link Matrix}.
    @Override
    public int hashCode()
    {
        return (((((((((((((((Float.hashCode(m11) + Float.hashCode(m12)) + Float.hashCode(m13)) + Float.hashCode(m14)) + Float.hashCode(m21)) + Float.hashCode(m22)) + Float.hashCode(m23)) + Float
                .hashCode(m24)) + Float.hashCode(m31)) + Float.hashCode(m32)) + Float.hashCode(m33)) + Float.hashCode(m34)) + Float.hashCode(m41)) + Float.hashCode(m42)) + Float.hashCode(m43)) + Float
                .hashCode(m44));
    }

    // <summary>
    // Creates a new {@link Matrix} which contains inversion of the specified matrix.
    // </summary>
    // <param name="matrix">Source {@link Matrix}.
    // <returns>The inverted matrix.
    public static Matrix invert(Matrix matrix)
    {
        Matrix result = new Matrix();
        invert(matrix, result);
        return result;
    }

    // <summary>
    // Creates a new {@link Matrix} which contains inversion of the specified matrix.
    // </summary>
    // <param name="matrix">Source {@link Matrix}.
    // <param name="result">The inverted matrix as output parameter.
    public static void invert(final Matrix matrix, Matrix result)
    {
        float num1 = matrix.m11;
        float num2 = matrix.m12;
        float num3 = matrix.m13;
        float num4 = matrix.m14;
        float num5 = matrix.m21;
        float num6 = matrix.m22;
        float num7 = matrix.m23;
        float num8 = matrix.m24;
        float num9 = matrix.m31;
        float num10 = matrix.m32;
        float num11 = matrix.m33;
        float num12 = matrix.m34;
        float num13 = matrix.m41;
        float num14 = matrix.m42;
        float num15 = matrix.m43;
        float num16 = matrix.m44;
        float num17 = (float) ((double) num11 * (double) num16 - (double) num12 * (double) num15);
        float num18 = (float) ((double) num10 * (double) num16 - (double) num12 * (double) num14);
        float num19 = (float) ((double) num10 * (double) num15 - (double) num11 * (double) num14);
        float num20 = (float) ((double) num9 * (double) num16 - (double) num12 * (double) num13);
        float num21 = (float) ((double) num9 * (double) num15 - (double) num11 * (double) num13);
        float num22 = (float) ((double) num9 * (double) num14 - (double) num10 * (double) num13);
        float num23 = (float) ((double) num6 * (double) num17 - (double) num7 * (double) num18 + (double) num8 * (double) num19);
        float num24 = (float) -((double) num5 * (double) num17 - (double) num7 * (double) num20 + (double) num8 * (double) num21);
        float num25 = (float) ((double) num5 * (double) num18 - (double) num6 * (double) num20 + (double) num8 * (double) num22);
        float num26 = (float) -((double) num5 * (double) num19 - (double) num6 * (double) num21 + (double) num7 * (double) num22);
        float num27 = (float) (1.0 / ((double) num1 * (double) num23 + (double) num2 * (double) num24 + (double) num3 * (double) num25 + (double) num4 * (double) num26));

        result.m11 = num23 * num27;
        result.m21 = num24 * num27;
        result.m31 = num25 * num27;
        result.m41 = num26 * num27;
        result.m12 = (float) -((double) num2 * (double) num17 - (double) num3 * (double) num18 + (double) num4 * (double) num19) * num27;
        result.m22 = (float) ((double) num1 * (double) num17 - (double) num3 * (double) num20 + (double) num4 * (double) num21) * num27;
        result.m32 = (float) -((double) num1 * (double) num18 - (double) num2 * (double) num20 + (double) num4 * (double) num22) * num27;
        result.m42 = (float) ((double) num1 * (double) num19 - (double) num2 * (double) num21 + (double) num3 * (double) num22) * num27;
        float num28 = (float) ((double) num7 * (double) num16 - (double) num8 * (double) num15);
        float num29 = (float) ((double) num6 * (double) num16 - (double) num8 * (double) num14);
        float num30 = (float) ((double) num6 * (double) num15 - (double) num7 * (double) num14);
        float num31 = (float) ((double) num5 * (double) num16 - (double) num8 * (double) num13);
        float num32 = (float) ((double) num5 * (double) num15 - (double) num7 * (double) num13);
        float num33 = (float) ((double) num5 * (double) num14 - (double) num6 * (double) num13);
        result.m13 = (float) ((double) num2 * (double) num28 - (double) num3 * (double) num29 + (double) num4 * (double) num30) * num27;
        result.m23 = (float) -((double) num1 * (double) num28 - (double) num3 * (double) num31 + (double) num4 * (double) num32) * num27;
        result.m33 = (float) ((double) num1 * (double) num29 - (double) num2 * (double) num31 + (double) num4 * (double) num33) * num27;
        result.m43 = (float) -((double) num1 * (double) num30 - (double) num2 * (double) num32 + (double) num3 * (double) num33) * num27;
        float num34 = (float) ((double) num7 * (double) num12 - (double) num8 * (double) num11);
        float num35 = (float) ((double) num6 * (double) num12 - (double) num8 * (double) num10);
        float num36 = (float) ((double) num6 * (double) num11 - (double) num7 * (double) num10);
        float num37 = (float) ((double) num5 * (double) num12 - (double) num8 * (double) num9);
        float num38 = (float) ((double) num5 * (double) num11 - (double) num7 * (double) num9);
        float num39 = (float) ((double) num5 * (double) num10 - (double) num6 * (double) num9);
        result.m14 = (float) -((double) num2 * (double) num34 - (double) num3 * (double) num35 + (double) num4 * (double) num36) * num27;
        result.m24 = (float) ((double) num1 * (double) num34 - (double) num3 * (double) num37 + (double) num4 * (double) num38) * num27;
        result.m34 = (float) -((double) num1 * (double) num35 - (double) num2 * (double) num37 + (double) num4 * (double) num39) * num27;
        result.m44 = (float) ((double) num1 * (double) num36 - (double) num2 * (double) num38 + (double) num3 * (double) num39) * num27;

        /*
         * 
         * 
         * ///
         * // Use Laplace expansion theorem to calculate the inverse of a 4x4 matrix
         * //
         * // 1. Calculate the 2x2 determinants needed the 4x4 determinant based on the 2x2
         * determinants
         * // 3. Create the adjugate matrix, which satisfies: A * adj(A) = det(A) * I
         * // 4. Divide adjugate matrix with the determinant to find the inverse
         * 
         * float det1, det2, det3, det4, det5, det6, det7, det8, det9, det10, det11, det12;
         * float detMatrix;
         * FindDeterminants(final matrix, out detMatrix, out det1, out det2, out det3, out det4, out
         * det5, out det6,
         * out det7, out det8, out det9, out det10, out det11, out det12);
         * 
         * float invDetMatrix = 1f / detMatrix;
         * 
         * Matrix ret; // Allow for matrix and result to point to the same structure
         * 
         * ret.M11 = (matrix.M22*det12 - matrix.M23*det11 + matrix.M24*det10) * invDetMatrix;
         * ret.M12 = (-matrix.M12*det12 + matrix.M13*det11 - matrix.M14*det10) * invDetMatrix;
         * ret.M13 = (matrix.M42*det6 - matrix.M43*det5 + matrix.M44*det4) * invDetMatrix;
         * ret.M14 = (-matrix.M32*det6 + matrix.M33*det5 - matrix.M34*det4) * invDetMatrix;
         * ret.M21 = (-matrix.M21*det12 + matrix.M23*det9 - matrix.M24*det8) * invDetMatrix;
         * ret.M22 = (matrix.M11*det12 - matrix.M13*det9 + matrix.M14*det8) * invDetMatrix;
         * ret.M23 = (-matrix.M41*det6 + matrix.M43*det3 - matrix.M44*det2) * invDetMatrix;
         * ret.M24 = (matrix.M31*det6 - matrix.M33*det3 + matrix.M34*det2) * invDetMatrix;
         * ret.M31 = (matrix.M21*det11 - matrix.M22*det9 + matrix.M24*det7) * invDetMatrix;
         * ret.M32 = (-matrix.M11*det11 + matrix.M12*det9 - matrix.M14*det7) * invDetMatrix;
         * ret.M33 = (matrix.M41*det5 - matrix.M42*det3 + matrix.M44*det1) * invDetMatrix;
         * ret.M34 = (-matrix.M31*det5 + matrix.M32*det3 - matrix.M34*det1) * invDetMatrix;
         * ret.M41 = (-matrix.M21*det10 + matrix.M22*det8 - matrix.M23*det7) * invDetMatrix;
         * ret.M42 = (matrix.M11*det10 - matrix.M12*det8 + matrix.M13*det7) * invDetMatrix;
         * ret.M43 = (-matrix.M41*det4 + matrix.M42*det2 - matrix.M43*det1) * invDetMatrix;
         * ret.M44 = (matrix.M31*det4 - matrix.M32*det2 + matrix.M33*det1) * invDetMatrix;
         * 
         * result = ret;
         */
    }

    // <summary>
    // Creates a new {@link Matrix} that contains linear interpolation of the values in
    // specified matrixes.
    // </summary>
    // <param name="matrix1">The first {@link Matrix}.
    // <param name="matrix2">The second {@link Vector2}.
    // <param name="amount">Weighting value(between 0.0 and 1.0).
    // <returns>>The result of linear interpolation of the specified matrixes.
    public static Matrix lerp(Matrix matrix1, Matrix matrix2, float amount)
    {
        Matrix result = new Matrix();
        result.m11 = matrix1.m11 + ((matrix2.m11 - matrix1.m11) * amount);
        result.m12 = matrix1.m12 + ((matrix2.m12 - matrix1.m12) * amount);
        result.m13 = matrix1.m13 + ((matrix2.m13 - matrix1.m13) * amount);
        result.m14 = matrix1.m14 + ((matrix2.m14 - matrix1.m14) * amount);
        result.m21 = matrix1.m21 + ((matrix2.m21 - matrix1.m21) * amount);
        result.m22 = matrix1.m22 + ((matrix2.m22 - matrix1.m22) * amount);
        result.m23 = matrix1.m23 + ((matrix2.m23 - matrix1.m23) * amount);
        result.m24 = matrix1.m24 + ((matrix2.m24 - matrix1.m24) * amount);
        result.m31 = matrix1.m31 + ((matrix2.m31 - matrix1.m31) * amount);
        result.m32 = matrix1.m32 + ((matrix2.m32 - matrix1.m32) * amount);
        result.m33 = matrix1.m33 + ((matrix2.m33 - matrix1.m33) * amount);
        result.m34 = matrix1.m34 + ((matrix2.m34 - matrix1.m34) * amount);
        result.m41 = matrix1.m41 + ((matrix2.m41 - matrix1.m41) * amount);
        result.m42 = matrix1.m42 + ((matrix2.m42 - matrix1.m42) * amount);
        result.m43 = matrix1.m43 + ((matrix2.m43 - matrix1.m43) * amount);
        result.m44 = matrix1.m44 + ((matrix2.m44 - matrix1.m44) * amount);
        return result;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains linear interpolation of the values in
    // specified matrixes.
    // </summary>
    // <param name="matrix1">The first {@link Matrix}.
    // <param name="matrix2">The second {@link Vector2}.
    // <param name="amount">Weighting value(between 0.0 and 1.0).
    // <param name="result">The result of linear interpolation of the specified matrixes as an
    // output parameter.
    public static void lerp(final Matrix matrix1, final Matrix matrix2, float amount, Matrix result)
    {
        result.m11 = matrix1.m11 + ((matrix2.m11 - matrix1.m11) * amount);
        result.m12 = matrix1.m12 + ((matrix2.m12 - matrix1.m12) * amount);
        result.m13 = matrix1.m13 + ((matrix2.m13 - matrix1.m13) * amount);
        result.m14 = matrix1.m14 + ((matrix2.m14 - matrix1.m14) * amount);
        result.m21 = matrix1.m21 + ((matrix2.m21 - matrix1.m21) * amount);
        result.m22 = matrix1.m22 + ((matrix2.m22 - matrix1.m22) * amount);
        result.m23 = matrix1.m23 + ((matrix2.m23 - matrix1.m23) * amount);
        result.m24 = matrix1.m24 + ((matrix2.m24 - matrix1.m24) * amount);
        result.m31 = matrix1.m31 + ((matrix2.m31 - matrix1.m31) * amount);
        result.m32 = matrix1.m32 + ((matrix2.m32 - matrix1.m32) * amount);
        result.m33 = matrix1.m33 + ((matrix2.m33 - matrix1.m33) * amount);
        result.m34 = matrix1.m34 + ((matrix2.m34 - matrix1.m34) * amount);
        result.m41 = matrix1.m41 + ((matrix2.m41 - matrix1.m41) * amount);
        result.m42 = matrix1.m42 + ((matrix2.m42 - matrix1.m42) * amount);
        result.m43 = matrix1.m43 + ((matrix2.m43 - matrix1.m43) * amount);
        result.m44 = matrix1.m44 + ((matrix2.m44 - matrix1.m44) * amount);
    }

    // <summary>
    // Creates a new {@link Matrix} that contains a multiplication of two matrix.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="matrix2">Source {@link Matrix}.
    // <returns>Result of the matrix multiplication.
    public static Matrix multiply(Matrix matrix1, Matrix matrix2)
    {
        Matrix result = new Matrix();
        float m11 = (((matrix1.m11 * matrix2.m11) + (matrix1.m12 * matrix2.m21)) + (matrix1.m13 * matrix2.m31)) + (matrix1.m14 * matrix2.m41);
        float m12 = (((matrix1.m11 * matrix2.m12) + (matrix1.m12 * matrix2.m22)) + (matrix1.m13 * matrix2.m32)) + (matrix1.m14 * matrix2.m42);
        float m13 = (((matrix1.m11 * matrix2.m13) + (matrix1.m12 * matrix2.m23)) + (matrix1.m13 * matrix2.m33)) + (matrix1.m14 * matrix2.m43);
        float m14 = (((matrix1.m11 * matrix2.m14) + (matrix1.m12 * matrix2.m24)) + (matrix1.m13 * matrix2.m34)) + (matrix1.m14 * matrix2.m44);
        float m21 = (((matrix1.m21 * matrix2.m11) + (matrix1.m22 * matrix2.m21)) + (matrix1.m23 * matrix2.m31)) + (matrix1.m24 * matrix2.m41);
        float m22 = (((matrix1.m21 * matrix2.m12) + (matrix1.m22 * matrix2.m22)) + (matrix1.m23 * matrix2.m32)) + (matrix1.m24 * matrix2.m42);
        float m23 = (((matrix1.m21 * matrix2.m13) + (matrix1.m22 * matrix2.m23)) + (matrix1.m23 * matrix2.m33)) + (matrix1.m24 * matrix2.m43);
        float m24 = (((matrix1.m21 * matrix2.m14) + (matrix1.m22 * matrix2.m24)) + (matrix1.m23 * matrix2.m34)) + (matrix1.m24 * matrix2.m44);
        float m31 = (((matrix1.m31 * matrix2.m11) + (matrix1.m32 * matrix2.m21)) + (matrix1.m33 * matrix2.m31)) + (matrix1.m34 * matrix2.m41);
        float m32 = (((matrix1.m31 * matrix2.m12) + (matrix1.m32 * matrix2.m22)) + (matrix1.m33 * matrix2.m32)) + (matrix1.m34 * matrix2.m42);
        float m33 = (((matrix1.m31 * matrix2.m13) + (matrix1.m32 * matrix2.m23)) + (matrix1.m33 * matrix2.m33)) + (matrix1.m34 * matrix2.m43);
        float m34 = (((matrix1.m31 * matrix2.m14) + (matrix1.m32 * matrix2.m24)) + (matrix1.m33 * matrix2.m34)) + (matrix1.m34 * matrix2.m44);
        float m41 = (((matrix1.m41 * matrix2.m11) + (matrix1.m42 * matrix2.m21)) + (matrix1.m43 * matrix2.m31)) + (matrix1.m44 * matrix2.m41);
        float m42 = (((matrix1.m41 * matrix2.m12) + (matrix1.m42 * matrix2.m22)) + (matrix1.m43 * matrix2.m32)) + (matrix1.m44 * matrix2.m42);
        float m43 = (((matrix1.m41 * matrix2.m13) + (matrix1.m42 * matrix2.m23)) + (matrix1.m43 * matrix2.m33)) + (matrix1.m44 * matrix2.m43);
        float m44 = (((matrix1.m41 * matrix2.m14) + (matrix1.m42 * matrix2.m24)) + (matrix1.m43 * matrix2.m34)) + (matrix1.m44 * matrix2.m44);
        result.m11 = m11;
        result.m12 = m12;
        result.m13 = m13;
        result.m14 = m14;
        result.m21 = m21;
        result.m22 = m22;
        result.m23 = m23;
        result.m24 = m24;
        result.m31 = m31;
        result.m32 = m32;
        result.m33 = m33;
        result.m34 = m34;
        result.m41 = m41;
        result.m42 = m42;
        result.m43 = m43;
        result.m44 = m44;
        return result;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains a multiplication of two matrix.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="matrix2">Source {@link Matrix}.
    // <param name="result">Result of the matrix multiplication as an output parameter.
    public static void multiply(final Matrix matrix1, final Matrix matrix2, Matrix result)
    {
        float m11 = (((matrix1.m11 * matrix2.m11) + (matrix1.m12 * matrix2.m21)) + (matrix1.m13 * matrix2.m31)) + (matrix1.m14 * matrix2.m41);
        float m12 = (((matrix1.m11 * matrix2.m12) + (matrix1.m12 * matrix2.m22)) + (matrix1.m13 * matrix2.m32)) + (matrix1.m14 * matrix2.m42);
        float m13 = (((matrix1.m11 * matrix2.m13) + (matrix1.m12 * matrix2.m23)) + (matrix1.m13 * matrix2.m33)) + (matrix1.m14 * matrix2.m43);
        float m14 = (((matrix1.m11 * matrix2.m14) + (matrix1.m12 * matrix2.m24)) + (matrix1.m13 * matrix2.m34)) + (matrix1.m14 * matrix2.m44);
        float m21 = (((matrix1.m21 * matrix2.m11) + (matrix1.m22 * matrix2.m21)) + (matrix1.m23 * matrix2.m31)) + (matrix1.m24 * matrix2.m41);
        float m22 = (((matrix1.m21 * matrix2.m12) + (matrix1.m22 * matrix2.m22)) + (matrix1.m23 * matrix2.m32)) + (matrix1.m24 * matrix2.m42);
        float m23 = (((matrix1.m21 * matrix2.m13) + (matrix1.m22 * matrix2.m23)) + (matrix1.m23 * matrix2.m33)) + (matrix1.m24 * matrix2.m43);
        float m24 = (((matrix1.m21 * matrix2.m14) + (matrix1.m22 * matrix2.m24)) + (matrix1.m23 * matrix2.m34)) + (matrix1.m24 * matrix2.m44);
        float m31 = (((matrix1.m31 * matrix2.m11) + (matrix1.m32 * matrix2.m21)) + (matrix1.m33 * matrix2.m31)) + (matrix1.m34 * matrix2.m41);
        float m32 = (((matrix1.m31 * matrix2.m12) + (matrix1.m32 * matrix2.m22)) + (matrix1.m33 * matrix2.m32)) + (matrix1.m34 * matrix2.m42);
        float m33 = (((matrix1.m31 * matrix2.m13) + (matrix1.m32 * matrix2.m23)) + (matrix1.m33 * matrix2.m33)) + (matrix1.m34 * matrix2.m43);
        float m34 = (((matrix1.m31 * matrix2.m14) + (matrix1.m32 * matrix2.m24)) + (matrix1.m33 * matrix2.m34)) + (matrix1.m34 * matrix2.m44);
        float m41 = (((matrix1.m41 * matrix2.m11) + (matrix1.m42 * matrix2.m21)) + (matrix1.m43 * matrix2.m31)) + (matrix1.m44 * matrix2.m41);
        float m42 = (((matrix1.m41 * matrix2.m12) + (matrix1.m42 * matrix2.m22)) + (matrix1.m43 * matrix2.m32)) + (matrix1.m44 * matrix2.m42);
        float m43 = (((matrix1.m41 * matrix2.m13) + (matrix1.m42 * matrix2.m23)) + (matrix1.m43 * matrix2.m33)) + (matrix1.m44 * matrix2.m43);
        float m44 = (((matrix1.m41 * matrix2.m14) + (matrix1.m42 * matrix2.m24)) + (matrix1.m43 * matrix2.m34)) + (matrix1.m44 * matrix2.m44);
        result.m11 = m11;
        result.m12 = m12;
        result.m13 = m13;
        result.m14 = m14;
        result.m21 = m21;
        result.m22 = m22;
        result.m23 = m23;
        result.m24 = m24;
        result.m31 = m31;
        result.m32 = m32;
        result.m33 = m33;
        result.m34 = m34;
        result.m41 = m41;
        result.m42 = m42;
        result.m43 = m43;
        result.m44 = m44;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains a multiplication of {@link Matrix}
    // and a scalar.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="scaleFactor">Scalar value.
    // <returns>Result of the matrix multiplication with a scalar.
    public static Matrix multiply(Matrix matrix1, float scaleFactor)
    {
        Matrix result = new Matrix(matrix1);
        result.m11 *= scaleFactor;
        result.m12 *= scaleFactor;
        result.m13 *= scaleFactor;
        result.m14 *= scaleFactor;
        result.m21 *= scaleFactor;
        result.m22 *= scaleFactor;
        result.m23 *= scaleFactor;
        result.m24 *= scaleFactor;
        result.m31 *= scaleFactor;
        result.m32 *= scaleFactor;
        result.m33 *= scaleFactor;
        result.m34 *= scaleFactor;
        result.m41 *= scaleFactor;
        result.m42 *= scaleFactor;
        result.m43 *= scaleFactor;
        result.m44 *= scaleFactor;
        return result;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains a multiplication of {@link Matrix}
    // and a scalar.
    // </summary>
    // <param name="matrix1">Source {@link Matrix}.
    // <param name="scaleFactor">Scalar value.
    // <param name="result">Result of the matrix multiplication with a scalar as an output
    // parameter.
    public static void multiply(final Matrix matrix1, float sclaeFactor, Matrix result)
    {
        result.m11 = matrix1.m11 * sclaeFactor;
        result.m12 = matrix1.m12 * sclaeFactor;
        result.m13 = matrix1.m13 * sclaeFactor;
        result.m14 = matrix1.m14 * sclaeFactor;
        result.m21 = matrix1.m21 * sclaeFactor;
        result.m22 = matrix1.m22 * sclaeFactor;
        result.m23 = matrix1.m23 * sclaeFactor;
        result.m24 = matrix1.m24 * sclaeFactor;
        result.m31 = matrix1.m31 * sclaeFactor;
        result.m32 = matrix1.m32 * sclaeFactor;
        result.m33 = matrix1.m33 * sclaeFactor;
        result.m34 = matrix1.m34 * sclaeFactor;
        result.m41 = matrix1.m41 * sclaeFactor;
        result.m42 = matrix1.m42 * sclaeFactor;
        result.m43 = matrix1.m43 * sclaeFactor;
        result.m44 = matrix1.m44 * sclaeFactor;
    }

    // <summary>
    // Copy the values of specified {@link Matrix} to the float array.
    // </summary>
    // <param name="matrix">The source {@link Matrix}.
    // <returns>The array which matrix values will be stored.
    // <remarks>
    // Required for OpenGL 2.0 projection matrix stuff.
    // </remarks>
    public static float[] toFloatArray(Matrix mat)
    {
        float[] matarray = {
                            mat.m11, mat.m12, mat.m13, mat.m14,
                            mat.m21, mat.m22, mat.m23, mat.m24,
                            mat.m31, mat.m32, mat.m33, mat.m34,
                            mat.m41, mat.m42, mat.m43, mat.m44
        };
        return matarray;
    }

    // <summary>
    // Returns a matrix with the all values negated.
    // </summary>
    // <param name="matrix">Source {@link Matrix}.
    // <returns>Result of the matrix negation.
    public static Matrix negate(Matrix matrix)
    {
        Matrix result = new Matrix(matrix);
        result.m11 = -matrix.m11;
        result.m12 = -matrix.m12;
        result.m13 = -matrix.m13;
        result.m14 = -matrix.m14;
        result.m21 = -matrix.m21;
        result.m22 = -matrix.m22;
        result.m23 = -matrix.m23;
        result.m24 = -matrix.m24;
        result.m31 = -matrix.m31;
        result.m32 = -matrix.m32;
        result.m33 = -matrix.m33;
        result.m34 = -matrix.m34;
        result.m41 = -matrix.m41;
        result.m42 = -matrix.m42;
        result.m43 = -matrix.m43;
        result.m44 = -matrix.m44;
        return result;
    }

    // <summary>
    // Returns a matrix with the all values negated.
    // </summary>
    // <param name="matrix">Source {@link Matrix}.
    // <param name="result">Result of the matrix negation as an output parameter.
    public static void negate(final Matrix matrix, Matrix result)
    {
        result.m11 = -matrix.m11;
        result.m12 = -matrix.m12;
        result.m13 = -matrix.m13;
        result.m14 = -matrix.m14;
        result.m21 = -matrix.m21;
        result.m22 = -matrix.m22;
        result.m23 = -matrix.m23;
        result.m24 = -matrix.m24;
        result.m31 = -matrix.m31;
        result.m32 = -matrix.m32;
        result.m33 = -matrix.m33;
        result.m34 = -matrix.m34;
        result.m41 = -matrix.m41;
        result.m42 = -matrix.m42;
        result.m43 = -matrix.m43;
        result.m44 = -matrix.m44;
    }

    // <summary>
    // Adds two matrixes.
    // </summary>
    // <param name="other">Source {@link Matrix} on the left of the add sign.
    public Matrix add(Matrix other)
    {
        this.m11 += other.m11;
        this.m12 += other.m12;
        this.m13 += other.m13;
        this.m14 += other.m14;
        this.m21 += other.m21;
        this.m22 += other.m22;
        this.m23 += other.m23;
        this.m24 += other.m24;
        this.m31 += other.m31;
        this.m32 += other.m32;
        this.m33 += other.m33;
        this.m34 += other.m34;
        this.m41 += other.m41;
        this.m42 += other.m42;
        this.m43 += other.m43;
        this.m44 += other.m44;
        return this;
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by the elements of another {@link Matrix}.
    // </summary>
    // <param name="matrix1">Source {@link Matrix} on the left of the div sign.
    // <param name="matrix2">Divisor {@link Matrix} on the right of the div sign.
    // <returns>The result of dividing the matrixes.
    public Matrix divide(Matrix other)
    {
        this.m11 /= other.m11;
        this.m12 /= other.m12;
        this.m13 /= other.m13;
        this.m14 /= other.m14;
        this.m21 /= other.m21;
        this.m22 /= other.m22;
        this.m23 /= other.m23;
        this.m24 /= other.m24;
        this.m31 /= other.m31;
        this.m32 /= other.m32;
        this.m33 /= other.m33;
        this.m34 /= other.m34;
        this.m41 /= other.m41;
        this.m42 /= other.m42;
        this.m43 /= other.m43;
        this.m44 /= other.m44;
        return this;
    }

    // <summary>
    // Divides the elements of a {@link Matrix} by a scalar.
    // </summary>
    // <param name="matrix">Source {@link Matrix} on the left of the div sign.
    // <param name="divider">Divisor scalar on the right of the div sign.
    // <returns>The result of dividing a matrix by a scalar.
    public Matrix divide(float divider)
    {
        float num = 1f / divider;
        this.m11 *= num;
        this.m12 *= num;
        this.m13 *= num;
        this.m14 *= num;
        this.m21 *= num;
        this.m22 *= num;
        this.m23 *= num;
        this.m24 *= num;
        this.m31 *= num;
        this.m32 *= num;
        this.m33 *= num;
        this.m34 *= num;
        this.m41 *= num;
        this.m42 *= num;
        this.m43 *= num;
        this.m44 *= num;
        return this;
    }

    // <summary>
    // Multiplies two matrixes.
    // </summary>
    // <param name="matrix1">Source {@link Matrix} on the left of the mul sign.
    // <param name="matrix2">Source {@link Matrix} on the right of the mul sign.
    // <returns>Result of the matrix multiplication.
    // <remarks>
    // Using matrix multiplication algorithm - see
    // http://en.wikipedia.org/wiki/Matrix_multiplication.
    // </remarks>
    public Matrix multiply(Matrix other)
    {
        float m11 = (((this.m11 * other.m11) + (this.m12 * other.m21)) + (this.m13 * other.m31)) + (this.m14 * other.m41);
        float m12 = (((this.m11 * other.m12) + (this.m12 * other.m22)) + (this.m13 * other.m32)) + (this.m14 * other.m42);
        float m13 = (((this.m11 * other.m13) + (this.m12 * other.m23)) + (this.m13 * other.m33)) + (this.m14 * other.m43);
        float m14 = (((this.m11 * other.m14) + (this.m12 * other.m24)) + (this.m13 * other.m34)) + (this.m14 * other.m44);
        float m21 = (((this.m21 * other.m11) + (this.m22 * other.m21)) + (this.m23 * other.m31)) + (this.m24 * other.m41);
        float m22 = (((this.m21 * other.m12) + (this.m22 * other.m22)) + (this.m23 * other.m32)) + (this.m24 * other.m42);
        float m23 = (((this.m21 * other.m13) + (this.m22 * other.m23)) + (this.m23 * other.m33)) + (this.m24 * other.m43);
        float m24 = (((this.m21 * other.m14) + (this.m22 * other.m24)) + (this.m23 * other.m34)) + (this.m24 * other.m44);
        float m31 = (((this.m31 * other.m11) + (this.m32 * other.m21)) + (this.m33 * other.m31)) + (this.m34 * other.m41);
        float m32 = (((this.m31 * other.m12) + (this.m32 * other.m22)) + (this.m33 * other.m32)) + (this.m34 * other.m42);
        float m33 = (((this.m31 * other.m13) + (this.m32 * other.m23)) + (this.m33 * other.m33)) + (this.m34 * other.m43);
        float m34 = (((this.m31 * other.m14) + (this.m32 * other.m24)) + (this.m33 * other.m34)) + (this.m34 * other.m44);
        float m41 = (((this.m41 * other.m11) + (this.m42 * other.m21)) + (this.m43 * other.m31)) + (this.m44 * other.m41);
        float m42 = (((this.m41 * other.m12) + (this.m42 * other.m22)) + (this.m43 * other.m32)) + (this.m44 * other.m42);
        float m43 = (((this.m41 * other.m13) + (this.m42 * other.m23)) + (this.m43 * other.m33)) + (this.m44 * other.m43);
        float m44 = (((this.m41 * other.m14) + (this.m42 * other.m24)) + (this.m43 * other.m34)) + (this.m44 * other.m44);
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m24 = m24;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.m34 = m34;
        this.m41 = m41;
        this.m42 = m42;
        this.m43 = m43;
        this.m44 = m44;
        return this;
    }

    // <summary>
    // Multiplies the elements of matrix by a scalar.
    // </summary>
    // <param name="matrix">Source {@link Matrix} on the left of the mul sign.
    // <param name="scaleFactor">Scalar value on the right of the mul sign.
    // <returns>Result of the matrix multiplication with a scalar.
    public Matrix multiply(float scaleFactor)
    {
        this.m11 *= scaleFactor;
        this.m12 *= scaleFactor;
        this.m13 *= scaleFactor;
        this.m14 *= scaleFactor;
        this.m21 *= scaleFactor;
        this.m22 *= scaleFactor;
        this.m23 *= scaleFactor;
        this.m24 *= scaleFactor;
        this.m31 *= scaleFactor;
        this.m32 *= scaleFactor;
        this.m33 *= scaleFactor;
        this.m34 *= scaleFactor;
        this.m41 *= scaleFactor;
        this.m42 *= scaleFactor;
        this.m43 *= scaleFactor;
        this.m44 *= scaleFactor;
        return this;
    }

    // <summary>
    // Subtracts the values of one {@link Matrix} from another {@link Matrix}.
    // </summary>
    // <param name="matrix1">Source {@link Matrix} on the left of the sub sign.
    // <param name="matrix2">Source {@link Matrix} on the right of the sub sign.
    // <returns>Result of the matrix subtraction.
    public Matrix subtract(Matrix other)
    {
        this.m11 -= other.m11;
        this.m12 -= other.m12;
        this.m13 -= other.m13;
        this.m14 -= other.m14;
        this.m21 -= other.m21;
        this.m22 -= other.m22;
        this.m23 -= other.m23;
        this.m24 -= other.m24;
        this.m31 -= other.m31;
        this.m32 -= other.m32;
        this.m33 -= other.m33;
        this.m34 -= other.m34;
        this.m41 -= other.m41;
        this.m42 -= other.m42;
        this.m43 -= other.m43;
        this.m44 -= other.m44;
        return this;
    }

    // <summary>
    // Inverts values in the specified {@link Matrix}.
    // </summary>
    // <param name="matrix">Source {@link Matrix} on the right of the sub sign.
    // <returns>Result of the inversion.
    public Matrix negate()
    {
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m13 = -this.m13;
        this.m14 = -this.m14;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
        this.m23 = -this.m23;
        this.m24 = -this.m24;
        this.m31 = -this.m31;
        this.m32 = -this.m32;
        this.m33 = -this.m33;
        this.m34 = -this.m34;
        this.m41 = -this.m41;
        this.m42 = -this.m42;
        this.m43 = -this.m43;
        this.m44 = -this.m44;
        return this;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains subtraction of one matrix from another.
    // </summary>
    // <param name="matrix1">The first {@link Matrix}.
    // <param name="matrix2">The second {@link Matrix}.
    // <returns>The result of the matrix subtraction.
    public static Matrix subtract(Matrix matrix1, Matrix matrix2)
    {
        Matrix result = new Matrix();
        result.m11 = matrix1.m11 - matrix2.m11;
        result.m12 = matrix1.m12 - matrix2.m12;
        result.m13 = matrix1.m13 - matrix2.m13;
        result.m14 = matrix1.m14 - matrix2.m14;
        result.m21 = matrix1.m21 - matrix2.m21;
        result.m22 = matrix1.m22 - matrix2.m22;
        result.m23 = matrix1.m23 - matrix2.m23;
        result.m24 = matrix1.m24 - matrix2.m24;
        result.m31 = matrix1.m31 - matrix2.m31;
        result.m32 = matrix1.m32 - matrix2.m32;
        result.m33 = matrix1.m33 - matrix2.m33;
        result.m34 = matrix1.m34 - matrix2.m34;
        result.m41 = matrix1.m41 - matrix2.m41;
        result.m42 = matrix1.m42 - matrix2.m42;
        result.m43 = matrix1.m43 - matrix2.m43;
        result.m44 = matrix1.m44 - matrix2.m44;
        return result;
    }

    // <summary>
    // Creates a new {@link Matrix} that contains subtraction of one matrix from another.
    // </summary>
    // <param name="matrix1">The first {@link Matrix}.
    // <param name="matrix2">The second {@link Matrix}.
    // <param name="result">The result of the matrix subtraction as an output parameter.
    public static void subtract(final Matrix matrix1, final Matrix matrix2, Matrix result)
    {
        result.m11 = matrix1.m11 - matrix2.m11;
        result.m12 = matrix1.m12 - matrix2.m12;
        result.m13 = matrix1.m13 - matrix2.m13;
        result.m14 = matrix1.m14 - matrix2.m14;
        result.m21 = matrix1.m21 - matrix2.m21;
        result.m22 = matrix1.m22 - matrix2.m22;
        result.m23 = matrix1.m23 - matrix2.m23;
        result.m24 = matrix1.m24 - matrix2.m24;
        result.m31 = matrix1.m31 - matrix2.m31;
        result.m32 = matrix1.m32 - matrix2.m32;
        result.m33 = matrix1.m33 - matrix2.m33;
        result.m34 = matrix1.m34 - matrix2.m34;
        result.m41 = matrix1.m41 - matrix2.m41;
        result.m42 = matrix1.m42 - matrix2.m42;
        result.m43 = matrix1.m43 - matrix2.m43;
        result.m44 = matrix1.m44 - matrix2.m44;
    }

    protected String debugDisplayString()
    {
        if (this.equals(identity))
        {
            return "Identity";
        }

        return ("( " + this.m11 + "  " + this.m12 + "  " + this.m13 + "  " + this.m14 + " )  \r\n" +
                "( " + this.m21 + "  " + this.m22 + "  " + this.m23 + "  " + this.m24 + " )  \r\n" +
                "( " + this.m31 + "  " + this.m32 + "  " + this.m33 + "  " + this.m34 + " )  \r\n" +
                "( " + this.m41 + "  " + this.m42 + "  " + this.m43 + "  " + this.m44 + " )");
    }

    // <summary>
    // Returns a {@link String} representation of this {@link Matrix} in the format:<br/>
    // {M11:[{@link #m11}] M12:[{@link #m12}] M13:[{@link #m13}] M14:[{@link #m14}]}<br/>
    // {M21:[{@link #m21}] M22:[{@link #m22}] M223:[{@link #m23}] M24:[{@link #m24}]}<br/>
    // {M31:[{@link #m31}] M32:[{@link #m32}] M33:[{@link #m33}] M34:[{@link #m34}]}<br/>
    // {M41:[{@link #m41}] M42:[{@link #m42}] M43:[{@link #m43}] M44:[{@link #m44}]}<br/>
    // </summary>
    // <returns>A {@link String} representation of this {@link Matrix}.
    @Override
    public String toString()
    {
        return "{M11:" + m11 + " M12:" + m12 + " M13:" + m13 + " M14:" + m14 + "}" +
               " {M21:" + m21 + " M22:" + m22 + " M23:" + m23 + " M24:" + m24 + "}" +
               " {M31:" + m31 + " M32:" + m32 + " M33:" + m33 + " M34:" + m34 + "}" +
               " {M41:" + m41 + " M42:" + m42 + " M43:" + m43 + " M44:" + m44 + "}";
    }

    // <summary>
    // Swap the matrix rows and columns.
    // </summary>
    // <param name="matrix">The matrix for transposing operation.
    // <returns>The new {@link Matrix} which contains the transposing result.
    public static Matrix transpose(Matrix matrix)
    {
        Matrix result = new Matrix();
        transpose(matrix, result);
        return result;
    }

    // <summary>
    // Swap the matrix rows and columns.
    // </summary>
    // <param name="matrix">The matrix for transposing operation.
    // <param name="result">The new {@link Matrix} which contains the transposing result as
    // an output parameter.
    public static void transpose(final Matrix matrix, Matrix result)
    {
        Matrix ret = new Matrix();

        ret.m11 = matrix.m11;
        ret.m12 = matrix.m21;
        ret.m13 = matrix.m31;
        ret.m14 = matrix.m41;

        ret.m21 = matrix.m12;
        ret.m22 = matrix.m22;
        ret.m23 = matrix.m32;
        ret.m24 = matrix.m42;

        ret.m31 = matrix.m13;
        ret.m32 = matrix.m23;
        ret.m33 = matrix.m33;
        ret.m34 = matrix.m43;

        ret.m41 = matrix.m14;
        ret.m42 = matrix.m24;
        ret.m43 = matrix.m34;
        ret.m44 = matrix.m44;

        result = ret;
    }
    // #endregion

    // #region Private Static Methods

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // <summary>
    // Helper method for using the Laplace expansion theorem using two rows expansions to
    // calculate major and
    // minor determinants of a 4x4 matrix. This method is used for inverting a matrix.
    // </summary>
    /*
     * private static void findDeterminants(final Matrix matrix, float major, float minor1, float
     * minor2, float minor3,
     * float minor4, float minor5, float minor6, float minor7, float minor8, float minor9, float
     * minor10,
     * float minor11, float minor12)
     * {
     * double det1 = (double) matrix.M11 * (double) matrix.M22 - (double) matrix.M12 * (double)
     * matrix.M21;
     * double det2 = (double) matrix.M11 * (double) matrix.M23 - (double) matrix.M13 * (double)
     * matrix.M21;
     * double det3 = (double) matrix.M11 * (double) matrix.M24 - (double) matrix.M14 * (double)
     * matrix.M21;
     * double det4 = (double) matrix.M12 * (double) matrix.M23 - (double) matrix.M13 * (double)
     * matrix.M22;
     * double det5 = (double) matrix.M12 * (double) matrix.M24 - (double) matrix.M14 * (double)
     * matrix.M22;
     * double det6 = (double) matrix.M13 * (double) matrix.M24 - (double) matrix.M14 * (double)
     * matrix.M23;
     * double det7 = (double) matrix.M31 * (double) matrix.M42 - (double) matrix.M32 * (double)
     * matrix.M41;
     * double det8 = (double) matrix.M31 * (double) matrix.M43 - (double) matrix.M33 * (double)
     * matrix.M41;
     * double det9 = (double) matrix.M31 * (double) matrix.M44 - (double) matrix.M34 * (double)
     * matrix.M41;
     * double det10 = (double) matrix.M32 * (double) matrix.M43 - (double) matrix.M33 * (double)
     * matrix.M42;
     * double det11 = (double) matrix.M32 * (double) matrix.M44 - (double) matrix.M34 * (double)
     * matrix.M42;
     * double det12 = (double) matrix.M33 * (double) matrix.M44 - (double) matrix.M34 * (double)
     * matrix.M43;
     * 
     * major = (float) (det1 * det12 - det2 * det11 + det3 * det10 + det4 * det9 - det5 * det8 +
     * det6 * det7);
     * minor1 = (float) det1;
     * minor2 = (float) det2;
     * minor3 = (float) det3;
     * minor4 = (float) det4;
     * minor5 = (float) det5;
     * minor6 = (float) det6;
     * minor7 = (float) det7;
     * minor8 = (float) det8;
     * minor9 = (float) det9;
     * minor10 = (float) det10;
     * minor11 = (float) det11;
     * minor12 = (float) det12;
     * }
     */

    // #endregion

}
