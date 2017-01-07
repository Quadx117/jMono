package jMono_Framework.math;

import jMono_Framework.Plane;
import jMono_Framework.Rectangle;

// C# struct
/**
 * Represents the right-handed 4x4 floating point matrix, which can store translation, scale and
 * rotation information.
 * 
 * @author Eric
 *
 */
public class Matrix // implements IEquatable<Matrix>
{
	// TODO: Should probably refactor these with lower case.
	public float M11, M12, M13, M14;
	public float M21, M22, M23, M24;
	public float M31, M32, M33, M34;
	public float M41, M42, M43, M44;

	// Note: Added this since it is provided by default for struct in C#
	public Matrix()
	{
		this.M11 = 0.0f;
		this.M12 = 0.0f;
		this.M13 = 0.0f;
		this.M14 = 0.0f;
		this.M21 = 0.0f;
		this.M22 = 0.0f;
		this.M23 = 0.0f;
		this.M24 = 0.0f;
		this.M31 = 0.0f;
		this.M32 = 0.0f;
		this.M33 = 0.0f;
		this.M34 = 0.0f;
		this.M41 = 0.0f;
		this.M42 = 0.0f;
		this.M43 = 0.0f;
		this.M44 = 0.0f;
	}
	
	public Matrix(Matrix m)
	{
		this.M11 = m.M11;
		this.M12 = m.M12;
		this.M13 = m.M13;
		this.M14 = m.M14;
		this.M21 = m.M21;
		this.M22 = m.M22;
		this.M23 = m.M23;
		this.M24 = m.M24;
		this.M31 = m.M31;
		this.M32 = m.M32;
		this.M33 = m.M33;
		this.M34 = m.M34;
		this.M41 = m.M41;
		this.M42 = m.M42;
		this.M43 = m.M43;
		this.M44 = m.M44;
	}

	public Matrix(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24, float m31,
			float m32, float m33, float m34, float m41, float m42, float m43, float m44)
	{
		this.M11 = m11;
		this.M12 = m12;
		this.M13 = m13;
		this.M14 = m14;
		this.M21 = m21;
		this.M22 = m22;
		this.M23 = m23;
		this.M24 = m24;
		this.M31 = m31;
		this.M32 = m32;
		this.M33 = m33;
		this.M34 = m34;
		this.M41 = m41;
		this.M42 = m42;
		this.M43 = m43;
		this.M44 = m44;
	}

	public Matrix(Vector4 row1, Vector4 row2, Vector4 row3, Vector4 row4)
	{
		this.M11 = row1.x;
		this.M12 = row1.y;
		this.M13 = row1.z;
		this.M14 = row1.w;
		this.M21 = row2.x;
		this.M22 = row2.y;
		this.M23 = row2.z;
		this.M24 = row2.w;
		this.M31 = row3.x;
		this.M32 = row3.y;
		this.M33 = row3.z;
		this.M34 = row3.w;
		this.M41 = row4.x;
		this.M42 = row4.y;
		this.M43 = row4.z;
		this.M44 = row4.w;
	}

	public float getMatrixValue(int index)
	{
		switch (index)
		{
			case 0:
				return M11;
			case 1:
				return M12;
			case 2:
				return M13;
			case 3:
				return M14;
			case 4:
				return M21;
			case 5:
				return M22;
			case 6:
				return M23;
			case 7:
				return M24;
			case 8:
				return M31;
			case 9:
				return M32;
			case 10:
				return M33;
			case 11:
				return M34;
			case 12:
				return M41;
			case 13:
				return M42;
			case 14:
				return M43;
			case 15:
				return M44;
		}
		throw new IllegalArgumentException();
	}

	public void setMatrixValue(int index, float value)
	{
		switch (index)
		{
			case 0:
				M11 = value;
				break;
			case 1:
				M12 = value;
				break;
			case 2:
				M13 = value;
				break;
			case 3:
				M14 = value;
				break;
			case 4:
				M21 = value;
				break;
			case 5:
				M22 = value;
				break;
			case 6:
				M23 = value;
				break;
			case 7:
				M24 = value;
				break;
			case 8:
				M31 = value;
				break;
			case 9:
				M32 = value;
				break;
			case 10:
				M33 = value;
				break;
			case 11:
				M34 = value;
				break;
			case 12:
				M41 = value;
				break;
			case 13:
				M42 = value;
				break;
			case 14:
				M43 = value;
				break;
			case 15:
				M44 = value;
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

	private static final Matrix identity = new Matrix(1f, 0f, 0f, 0f,	//
													  0f, 1f, 0f, 0f,	//
													  0f, 0f, 1f, 0f,	//
													  0f, 0f, 0f, 1f);	//

	public Vector3 backward()
	{
		return new Vector3(this.M31, this.M32, this.M33);
	}

	public void setBackward(Vector3 value)
	{
		this.M31 = value.x;
		this.M32 = value.y;
		this.M33 = value.z;
	}

	public Vector3 down()
	{
		return new Vector3(-this.M21, -this.M22, -this.M23);
	}

	public void setDown(Vector3 value)
	{
		this.M21 = -value.x;
		this.M22 = -value.y;
		this.M23 = -value.z;
	}

	public Vector3 forward()
	{
		return new Vector3(-this.M31, -this.M32, -this.M33);
	}

	public void setForward(Vector3 value)
	{
		this.M31 = -value.x;
		this.M32 = -value.y;
		this.M33 = -value.z;
	}

	public static Matrix identity()
	{
		return new Matrix(identity);
	}

	public Vector3 left()
	{
		return new Vector3(-this.M11, -this.M12, -this.M13);
	}

	public void setLeft(Vector3 value)
	{
		this.M11 = -value.x;
		this.M12 = -value.y;
		this.M13 = -value.z;
	}

	public Vector3 right()
	{
		return new Vector3(this.M11, this.M12, this.M13);
	}

	public void setRight(Vector3 value)
	{
		this.M11 = value.x;
		this.M12 = value.y;
		this.M13 = value.z;
	}

	/**
	 * Rotation stored in this matrix.
	 * 
	 * @return The rotation stored in this matrix.
	 */
	public Quaternion getRotation()
	{
		return Quaternion.createFromRotationMatrix(this);
	}

	/**
	 * Position stored in this matrix.
	 * 
	 * @return The position stored in this matrix.
	 */
	public Vector3 translation()
	{
		return new Vector3(this.M41, this.M42, this.M43);
	}

	public void setTranslation(Vector3 value)
	{
		this.M41 = value.x;
		this.M42 = value.y;
		this.M43 = value.z;
	}

	/**
	 * Scale stored in this matrix.
	 * 
	 * @return The scale stored in this matrix.
	 */
	public Vector3 scale()
    {
		return new Vector3(this.M11, this.M22, this.M33);
    }
            
	public void setScale(Vector3 value)
	{
		this.M11 = value.x;
		this.M22 = value.y;
		this.M33 = value.z;
	}

	public Vector3 up()
	{
		return new Vector3(this.M21, this.M22, this.M23);
	}

	public void setUp(Vector3 value)
	{
		this.M21 = value.x;
		this.M22 = value.y;
		this.M23 = value.z;
	}

	public static Matrix add(Matrix matrix1, Matrix matrix2)
	{
		matrix1.M11 += matrix2.M11;
		matrix1.M12 += matrix2.M12;
		matrix1.M13 += matrix2.M13;
		matrix1.M14 += matrix2.M14;
		matrix1.M21 += matrix2.M21;
		matrix1.M22 += matrix2.M22;
		matrix1.M23 += matrix2.M23;
		matrix1.M24 += matrix2.M24;
		matrix1.M31 += matrix2.M31;
		matrix1.M32 += matrix2.M32;
		matrix1.M33 += matrix2.M33;
		matrix1.M34 += matrix2.M34;
		matrix1.M41 += matrix2.M41;
		matrix1.M42 += matrix2.M42;
		matrix1.M43 += matrix2.M43;
		matrix1.M44 += matrix2.M44;
		return matrix1;
	}

	public static void add(final Matrix matrix1, final Matrix matrix2, Matrix result)
	{
		result.M11 = matrix1.M11 + matrix2.M11;
		result.M12 = matrix1.M12 + matrix2.M12;
		result.M13 = matrix1.M13 + matrix2.M13;
		result.M14 = matrix1.M14 + matrix2.M14;
		result.M21 = matrix1.M21 + matrix2.M21;
		result.M22 = matrix1.M22 + matrix2.M22;
		result.M23 = matrix1.M23 + matrix2.M23;
		result.M24 = matrix1.M24 + matrix2.M24;
		result.M31 = matrix1.M31 + matrix2.M31;
		result.M32 = matrix1.M32 + matrix2.M32;
		result.M33 = matrix1.M33 + matrix2.M33;
		result.M34 = matrix1.M34 + matrix2.M34;
		result.M41 = matrix1.M41 + matrix2.M41;
		result.M42 = matrix1.M42 + matrix2.M42;
		result.M43 = matrix1.M43 + matrix2.M43;
		result.M44 = matrix1.M44 + matrix2.M44;
	}

	public static Matrix createBillboard(Vector3 objectPosition, Vector3 cameraPosition, Vector3 cameraUpVector,
			Vector3 cameraForwardVector)
	{
		Matrix result = Matrix.identity();

		// Delegate to the other overload of the function to do the work
		createBillboard(objectPosition, cameraPosition, cameraUpVector, cameraForwardVector, result);

		return result;
	}

	public static void createBillboard(final Vector3 objectPosition, final Vector3 cameraPosition,
			final Vector3 cameraUpVector, Vector3 cameraForwardVector, Matrix result)
	{
		Vector3 vector = new Vector3(1f);
		Vector3 vector2 = new Vector3(1f);
		Vector3 vector3 = new Vector3(1f);
		vector.x = objectPosition.x - cameraPosition.x;
		vector.y = objectPosition.y - cameraPosition.y;
		vector.z = objectPosition.z - cameraPosition.z;
		float num = vector.lengthSquared();
		if (num < 0.0001f)
		{
			vector = cameraForwardVector != null ? cameraForwardVector.negate() : Vector3.forward();
		}
		else
		{
			Vector3.multiply(vector, (float) (1f / ((float) Math.sqrt((double) num))), vector);
		}
		Vector3.cross(cameraUpVector, vector, vector3);
		vector3.normalize();
		Vector3.cross(vector, vector3, vector2);
		result.M11 = vector3.x;
		result.M12 = vector3.y;
		result.M13 = vector3.z;
		result.M14 = 0;
		result.M21 = vector2.x;
		result.M22 = vector2.y;
		result.M23 = vector2.z;
		result.M24 = 0;
		result.M31 = vector.x;
		result.M32 = vector.y;
		result.M33 = vector.z;
		result.M34 = 0;
		result.M41 = objectPosition.x;
		result.M42 = objectPosition.y;
		result.M43 = objectPosition.z;
		result.M44 = 1;
	}

	public static Matrix createConstrainedBillboard(Vector3 objectPosition, Vector3 cameraPosition, Vector3 rotateAxis,
			Vector3 cameraForwardVector, Vector3 objectForwardVector)
	{
		Matrix result = Matrix.identity();
		createConstrainedBillboard(objectPosition, cameraPosition, rotateAxis, cameraForwardVector,
				objectForwardVector, result);
		return result;
	}

	public static void createConstrainedBillboard(final Vector3 objectPosition, final Vector3 cameraPosition,
			final Vector3 rotateAxis, Vector3 cameraForwardVector, Vector3 objectForwardVector, Matrix result)
	{
		float num;
		Vector3 vector = Vector3.one();
		Vector3 vector2 = Vector3.one();
		Vector3 vector3 = Vector3.one();
		vector2.x = objectPosition.x - cameraPosition.x;
		vector2.y = objectPosition.y - cameraPosition.y;
		vector2.z = objectPosition.z - cameraPosition.z;
		float num2 = vector2.lengthSquared();
		if (num2 < 0.0001f)
		{
			vector2 = cameraForwardVector != null ? cameraForwardVector.negate() : Vector3.forward();
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
					num = ((rotateAxis.x * Vector3.forward().x) + (rotateAxis.y * Vector3.forward().y))
							+ (rotateAxis.z * Vector3.forward().z);
					vector = (Math.abs(num) > 0.9982547f) ? Vector3.right() : Vector3.forward();
				}
			}
			else
			{
				num = ((rotateAxis.x * Vector3.forward().x) + (rotateAxis.y * Vector3.forward().y))
						+ (rotateAxis.z * Vector3.forward().z);
				vector = (Math.abs(num) > 0.9982547f) ? Vector3.right() : Vector3.forward();
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
		result.M11 = vector3.x;
		result.M12 = vector3.y;
		result.M13 = vector3.z;
		result.M14 = 0;
		result.M21 = vector4.x;
		result.M22 = vector4.y;
		result.M23 = vector4.z;
		result.M24 = 0;
		result.M31 = vector.x;
		result.M32 = vector.y;
		result.M33 = vector.z;
		result.M34 = 0;
		result.M41 = objectPosition.x;
		result.M42 = objectPosition.y;
		result.M43 = objectPosition.z;
		result.M44 = 1;

	}

	public static Matrix createFromAxisAngle(Vector3 axis, float angle)
	{
		Matrix result = Matrix.identity();
		createFromAxisAngle(axis, angle, result);
		return result;
	}

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
		result.M11 = num11 + (num * (1f - num11));
		result.M12 = (num8 - (num * num8)) + (num2 * z);
		result.M13 = (num7 - (num * num7)) - (num2 * y);
		result.M14 = 0;
		result.M21 = (num8 - (num * num8)) - (num2 * z);
		result.M22 = num10 + (num * (1f - num10));
		result.M23 = (num6 - (num * num6)) + (num2 * x);
		result.M24 = 0;
		result.M31 = (num7 - (num * num7)) + (num2 * y);
		result.M32 = (num6 - (num * num6)) - (num2 * x);
		result.M33 = num9 + (num * (1f - num9));
		result.M34 = 0;
		result.M41 = 0;
		result.M42 = 0;
		result.M43 = 0;
		result.M44 = 1;
	}

	public static Matrix createFromQuaternion(Quaternion quaternion)
	{
		Matrix result = Matrix.identity();
		createFromQuaternion(quaternion, result);
		return result;
	}

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
		result.M11 = 1f - (2f * (num8 + num7));
		result.M12 = 2f * (num6 + num5);
		result.M13 = 2f * (num4 - num3);
		result.M14 = 0f;
		result.M21 = 2f * (num6 - num5);
		result.M22 = 1f - (2f * (num7 + num9));
		result.M23 = 2f * (num2 + num);
		result.M24 = 0f;
		result.M31 = 2f * (num4 + num3);
		result.M32 = 2f * (num2 - num);
		result.M33 = 1f - (2f * (num8 + num9));
		result.M34 = 0f;
		result.M41 = 0f;
		result.M42 = 0f;
		result.M43 = 0f;
		result.M44 = 1f;
	}

	public static Matrix createFromYawPitchRoll(float yaw, float pitch, float roll)
	{
		Matrix result = Matrix.identity();
		createFromYawPitchRoll(yaw, pitch, roll, result);
		return result;
	}

	public static void createFromYawPitchRoll(float yaw, float pitch, float roll, Matrix result)
	{
		Quaternion quaternion = Quaternion.identity();
		Quaternion.createFromYawPitchRoll(yaw, pitch, roll, quaternion);
		createFromQuaternion(quaternion, result);
	}

	public static Matrix createLookAt(Vector3 cameraPosition, Vector3 cameraTarget, Vector3 cameraUpVector)
	{
		Matrix matrix = Matrix.identity();
		createLookAt(cameraPosition, cameraTarget, cameraUpVector, matrix);
		return matrix;
	}

	public static void createLookAt(final Vector3 cameraPosition, final Vector3 cameraTarget,
			final Vector3 cameraUpVector, Matrix result)
	{
		Vector3 vector = Vector3.normalize(Vector3.subtract(cameraPosition, cameraTarget));
		Vector3 vector2 = Vector3.normalize(Vector3.cross(cameraUpVector, vector));
		Vector3 vector3 = Vector3.cross(vector, vector2);
		result.M11 = vector2.x;
		result.M12 = vector3.x;
		result.M13 = vector.x;
		result.M14 = 0f;
		result.M21 = vector2.y;
		result.M22 = vector3.y;
		result.M23 = vector.y;
		result.M24 = 0f;
		result.M31 = vector2.z;
		result.M32 = vector3.z;
		result.M33 = vector.z;
		result.M34 = 0f;
		result.M41 = -Vector3.dot(vector2, cameraPosition);
		result.M42 = -Vector3.dot(vector3, cameraPosition);
		result.M43 = -Vector3.dot(vector, cameraPosition);
		result.M44 = 1f;
	}

	public static Matrix createOrthographic(float width, float height, float zNearPlane, float zFarPlane)
	{
		Matrix result = Matrix.identity();
		createOrthographic(width, height, zNearPlane, zFarPlane, result);
		return result;
	}

	public static void createOrthographic(float width, float height, float zNearPlane, float zFarPlane, Matrix result)
	{
		result.M11 = 2f / width;
		result.M12 = result.M13 = result.M14 = 0f;
		result.M22 = 2f / height;
		result.M21 = result.M23 = result.M24 = 0f;
		result.M33 = 1f / (zNearPlane - zFarPlane);
		result.M31 = result.M32 = result.M34 = 0f;
		result.M41 = result.M42 = 0f;
		result.M43 = zNearPlane / (zNearPlane - zFarPlane);
		result.M44 = 1f;
	}

	public static Matrix createOrthographicOffCenter(float left, float right, float bottom, float top,
			float zNearPlane, float zFarPlane)
	{
		Matrix result = Matrix.identity();
		createOrthographicOffCenter(left, right, bottom, top, zNearPlane, zFarPlane, result);
		return result;
	}

	public static void createOrthographicOffCenter(float left, float right, float bottom, float top, float zNearPlane,
			float zFarPlane, Matrix result)
	{
		result.M11 = (float) (2.0 / ((double) right - (double) left));
		result.M12 = 0.0f;
		result.M13 = 0.0f;
		result.M14 = 0.0f;
		result.M21 = 0.0f;
		result.M22 = (float) (2.0 / ((double) top - (double) bottom));
		result.M23 = 0.0f;
		result.M24 = 0.0f;
		result.M31 = 0.0f;
		result.M32 = 0.0f;
		result.M33 = (float) (1.0 / ((double) zNearPlane - (double) zFarPlane));
		result.M34 = 0.0f;
		result.M41 = (float) (((double) left + (double) right) / ((double) left - (double) right));
		result.M42 = (float) (((double) top + (double) bottom) / ((double) bottom - (double) top));
		result.M43 = (float) ((double) zNearPlane / ((double) zNearPlane - (double) zFarPlane));
		result.M44 = 1.0f;
	}

	public static Matrix createPerspective(float width, float height, float nearPlaneDistance, float farPlaneDistance)
	{
		Matrix result = Matrix.identity();
		createPerspective(width, height, nearPlaneDistance, farPlaneDistance, result);
		return result;
	}

	public static void createPerspective(float width, float height, float nearPlaneDistance, float farPlaneDistance,
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
		result.M11 = (2f * nearPlaneDistance) / width;
		result.M12 = result.M13 = result.M14 = 0f;
		result.M22 = (2f * nearPlaneDistance) / height;
		result.M21 = result.M23 = result.M24 = 0f;
		result.M33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
		result.M31 = result.M32 = 0f;
		result.M34 = -1f;
		result.M41 = result.M42 = result.M44 = 0f;
		result.M43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
	}

	public static Matrix createPerspectiveFieldOfView(float fieldOfView, float aspectRatio, float nearPlaneDistance,
			float farPlaneDistance)
	{
		Matrix result = Matrix.identity();
		createPerspectiveFieldOfView(fieldOfView, aspectRatio, nearPlaneDistance, farPlaneDistance, result);
		return result;
	}

	public static void createPerspectiveFieldOfView(float fieldOfView, float aspectRatio, float nearPlaneDistance,
			float farPlaneDistance, Matrix result)
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
		result.M11 = num9;
		result.M12 = result.M13 = result.M14 = 0;
		result.M22 = num;
		result.M21 = result.M23 = result.M24 = 0;
		result.M31 = result.M32 = 0f;
		result.M33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
		result.M34 = -1;
		result.M41 = result.M42 = result.M44 = 0;
		result.M43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
	}

	public static Matrix createPerspectiveOffCenter(float left, float right, float bottom, float top,
			float nearPlaneDistance, float farPlaneDistance)
	{
		Matrix result = Matrix.identity();
		createPerspectiveOffCenter(left, right, bottom, top, nearPlaneDistance, farPlaneDistance, result);
		return result;
	}

	public static Matrix createPerspectiveOffCenter(Rectangle viewingVolume, float nearPlaneDistance, float farPlaneDistance)
    {
        Matrix result = Matrix.identity();
        createPerspectiveOffCenter(viewingVolume.x, viewingVolume.x + viewingVolume.width, viewingVolume.y + viewingVolume.height, viewingVolume.y, nearPlaneDistance, farPlaneDistance, result);
        return result;
    }
	
	public static void createPerspectiveOffCenter(float left, float right, float bottom, float top,
			float nearPlaneDistance, float farPlaneDistance, Matrix result)
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
		result.M11 = (2f * nearPlaneDistance) / (right - left);
		result.M12 = result.M13 = result.M14 = 0;
		result.M22 = (2f * nearPlaneDistance) / (top - bottom);
		result.M21 = result.M23 = result.M24 = 0;
		result.M31 = (left + right) / (right - left);
		result.M32 = (top + bottom) / (top - bottom);
		result.M33 = farPlaneDistance / (nearPlaneDistance - farPlaneDistance);
		result.M34 = -1;
		result.M43 = (nearPlaneDistance * farPlaneDistance) / (nearPlaneDistance - farPlaneDistance);
		result.M41 = result.M42 = result.M44 = 0;
	}

	public static Matrix createRotationX(float radians)
	{
		Matrix result = Matrix.identity();
		createRotationX(radians, result);
		return result;
	}

	public static void createRotationX(float radians, Matrix result)
	{
		result = Matrix.identity();

		float val1 = (float) Math.cos(radians);
		float val2 = (float) Math.sin(radians);

		result.M22 = val1;
		result.M23 = val2;
		result.M32 = -val2;
		result.M33 = val1;
	}

	public static Matrix createRotationY(float radians)
	{
		Matrix result = Matrix.identity();
		createRotationY(radians, result);
		return result;
	}

	public static void createRotationY(float radians, Matrix result)
	{
		result = Matrix.identity();

		float val1 = (float) Math.cos(radians);
		float val2 = (float) Math.sin(radians);

		result.M11 = val1;
		result.M13 = -val2;
		result.M31 = val2;
		result.M33 = val1;
	}

	public static Matrix createRotationZ(float radians)
	{
		Matrix result = Matrix.identity();
		createRotationZ(radians, result);
		return result;
	}

	public static void createRotationZ(float radians, Matrix result)
	{
		result = Matrix.identity();

		float val1 = (float) Math.cos(radians);
		float val2 = (float) Math.sin(radians);

		result.M11 = val1;
		result.M12 = val2;
		result.M21 = -val2;
		result.M22 = val1;
	}

	public static Matrix createScale(float scale)
	{
		Matrix result = Matrix.identity();
		createScale(scale, scale, scale, result);
		return result;
	}

	public static void createScale(float scale, Matrix result)
	{
		createScale(scale, scale, scale, result);
	}

	public static Matrix createScale(float xScale, float yScale, float zScale)
	{
		Matrix result = Matrix.identity();
		createScale(xScale, yScale, zScale, result);
		return result;
	}

	public static void createScale(float xScale, float yScale, float zScale, Matrix result)
	{
		result.M11 = xScale;
		result.M12 = 0;
		result.M13 = 0;
		result.M14 = 0;
		result.M21 = 0;
		result.M22 = yScale;
		result.M23 = 0;
		result.M24 = 0;
		result.M31 = 0;
		result.M32 = 0;
		result.M33 = zScale;
		result.M34 = 0;
		result.M41 = 0;
		result.M42 = 0;
		result.M43 = 0;
		result.M44 = 1;
	}

	public static Matrix createScale(Vector3 scales)
	{
		Matrix result = Matrix.identity();
		createScale(scales, result);
		return result;
	}

	public static void createScale(final Vector3 scales, Matrix result)
	{
		result.M11 = scales.x;
		result.M12 = 0;
		result.M13 = 0;
		result.M14 = 0;
		result.M21 = 0;
		result.M22 = scales.y;
		result.M23 = 0;
		result.M24 = 0;
		result.M31 = 0;
		result.M32 = 0;
		result.M33 = scales.z;
		result.M34 = 0;
		result.M41 = 0;
		result.M42 = 0;
		result.M43 = 0;
		result.M44 = 1;
	}

	/**
	 * Creates a Matrix that flattens geometry into a specified Plane as if casting a shadow from a
	 * specified light source.
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
		Matrix result = Matrix.identity();
		createShadow(lightDirection, plane, result);
		return result;
	}

	/**
	 * Creates a Matrix that flattens geometry into a specified Plane as if casting a shadow from a
	 * specified light source.
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
		float dot = (plane.normal.x * lightDirection.x) + (plane.normal.y * lightDirection.y)
				+ (plane.normal.z * lightDirection.z);
		float x = -plane.normal.x;
		float y = -plane.normal.y;
		float z = -plane.normal.z;
		float d = -plane.D;

		result.M11 = (x * lightDirection.x) + dot;
		result.M12 = x * lightDirection.y;
		result.M13 = x * lightDirection.z;
		result.M14 = 0;
		result.M21 = y * lightDirection.x;
		result.M22 = (y * lightDirection.y) + dot;
		result.M23 = y * lightDirection.z;
		result.M24 = 0;
		result.M31 = z * lightDirection.x;
		result.M32 = z * lightDirection.y;
		result.M33 = (z * lightDirection.z) + dot;
		result.M34 = 0;
		result.M41 = d * lightDirection.x;
		result.M42 = d * lightDirection.y;
		result.M43 = d * lightDirection.z;
		result.M44 = dot;
	}

	public static Matrix createTranslation(float xPosition, float yPosition, float zPosition)
	{
		Matrix result = Matrix.identity();
		createTranslation(xPosition, yPosition, zPosition, result);
		return result;
	}

	public static void createTranslation(final Vector3 position, Matrix result)
	{
		result.M11 = 1;
		result.M12 = 0;
		result.M13 = 0;
		result.M14 = 0;
		result.M21 = 0;
		result.M22 = 1;
		result.M23 = 0;
		result.M24 = 0;
		result.M31 = 0;
		result.M32 = 0;
		result.M33 = 1;
		result.M34 = 0;
		result.M41 = position.x;
		result.M42 = position.y;
		result.M43 = position.z;
		result.M44 = 1;
	}

	public static Matrix createTranslation(Vector3 position)
	{
		Matrix result = Matrix.identity();
		createTranslation(position, result);
		return result;
	}

	public static void createTranslation(float xPosition, float yPosition, float zPosition, Matrix result)
	{
		result.M11 = 1;
		result.M12 = 0;
		result.M13 = 0;
		result.M14 = 0;
		result.M21 = 0;
		result.M22 = 1;
		result.M23 = 0;
		result.M24 = 0;
		result.M31 = 0;
		result.M32 = 0;
		result.M33 = 1;
		result.M34 = 0;
		result.M41 = xPosition;
		result.M42 = yPosition;
		result.M43 = zPosition;
		result.M44 = 1;
	}

	public static Matrix createReflection(Plane value)
	{
		Matrix result = Matrix.identity();
		createReflection(value, result);
		return result;
	}

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
		result.M11 = (num3 * x) + 1f;
		result.M12 = num2 * x;
		result.M13 = num * x;
		result.M14 = 0;
		result.M21 = num3 * y;
		result.M22 = (num2 * y) + 1;
		result.M23 = num * y;
		result.M24 = 0;
		result.M31 = num3 * z;
		result.M32 = num2 * z;
		result.M33 = (num * z) + 1;
		result.M34 = 0;
		result.M41 = num3 * plane.D;
		result.M42 = num2 * plane.D;
		result.M43 = num * plane.D;
		result.M44 = 1;
	}

	public static Matrix createWorld(Vector3 position, Vector3 forward, Vector3 up)
	{
		Matrix result = Matrix.identity();
		createWorld(position, forward, up, result);
		return result;
	}

	public static void createWorld(final Vector3 position, final Vector3 forward, final Vector3 up, Matrix result)
	{
		Vector3 x = Vector3.one();
		Vector3 y = Vector3.one();
		Vector3 z = Vector3.one();
		Vector3.normalize(forward, z);
		Vector3.cross(forward, up, x);
		Vector3.cross(x, forward, y);
		x.normalize();
		y.normalize();

		result = Matrix.identity();
		result.setRight(x);
		result.setUp(y);
		result.setForward(z);
		result.setTranslation(position);
		result.M44 = 1f;
	}

	public boolean decompose(Vector3 scale, Quaternion rotation, Vector3 translation)
	{
		translation.x = this.M41;
		translation.y = this.M42;
		translation.z = this.M43;

		float xs = (Math.signum(M11 * M12 * M13 * M14) < 0) ? -1 : 1;
		float ys = (Math.signum(M21 * M22 * M23 * M24) < 0) ? -1 : 1;
		float zs = (Math.signum(M31 * M32 * M33 * M34) < 0) ? -1 : 1;

		scale.x = xs * (float) Math.sqrt(this.M11 * this.M11 + this.M12 * this.M12 + this.M13 * this.M13);
		scale.y = ys * (float) Math.sqrt(this.M21 * this.M21 + this.M22 * this.M22 + this.M23 * this.M23);
		scale.z = zs * (float) Math.sqrt(this.M31 * this.M31 + this.M32 * this.M32 + this.M33 * this.M33);

		if (scale.x == 0.0 || scale.y == 0.0 || scale.z == 0.0)
		{
			rotation = Quaternion.identity();
			return false;
		}

		Matrix m1 = new Matrix(this.M11 / scale.x, M12 / scale.x, M13 / scale.x, 0,	//
							   this.M21 / scale.y, M22 / scale.y, M23 / scale.y, 0,	//
							   this.M31 / scale.z, M32 / scale.z, M33 / scale.z, 0,	//
							   0, 0, 0, 1);

		rotation = Quaternion.createFromRotationMatrix(m1);
		return true;
	}

	public float determinant()
	{
		float num22 = this.M11;
		float num21 = this.M12;
		float num20 = this.M13;
		float num19 = this.M14;
		float num12 = this.M21;
		float num11 = this.M22;
		float num10 = this.M23;
		float num9 = this.M24;
		float num8 = this.M31;
		float num7 = this.M32;
		float num6 = this.M33;
		float num5 = this.M34;
		float num4 = this.M41;
		float num3 = this.M42;
		float num2 = this.M43;
		float num = this.M44;
		float num18 = (num6 * num) - (num5 * num2);
		float num17 = (num7 * num) - (num5 * num3);
		float num16 = (num7 * num2) - (num6 * num3);
		float num15 = (num8 * num) - (num5 * num4);
		float num14 = (num8 * num2) - (num6 * num4);
		float num13 = (num8 * num3) - (num7 * num4);
		return ((((num22 * (((num11 * num18) - (num10 * num17)) + (num9 * num16))) - (num21 * (((num12 * num18) - (num10 * num15)) + (num9 * num14)))) + (num20 * (((num12 * num17) - (num11 * num15)) + (num9 * num13)))) - (num19 * (((num12 * num16) - (num11 * num14)) + (num10 * num13))));
	}

	public static Matrix divide(Matrix matrix1, Matrix matrix2)
	{
		matrix1.M11 = matrix1.M11 / matrix2.M11;
		matrix1.M12 = matrix1.M12 / matrix2.M12;
		matrix1.M13 = matrix1.M13 / matrix2.M13;
		matrix1.M14 = matrix1.M14 / matrix2.M14;
		matrix1.M21 = matrix1.M21 / matrix2.M21;
		matrix1.M22 = matrix1.M22 / matrix2.M22;
		matrix1.M23 = matrix1.M23 / matrix2.M23;
		matrix1.M24 = matrix1.M24 / matrix2.M24;
		matrix1.M31 = matrix1.M31 / matrix2.M31;
		matrix1.M32 = matrix1.M32 / matrix2.M32;
		matrix1.M33 = matrix1.M33 / matrix2.M33;
		matrix1.M34 = matrix1.M34 / matrix2.M34;
		matrix1.M41 = matrix1.M41 / matrix2.M41;
		matrix1.M42 = matrix1.M42 / matrix2.M42;
		matrix1.M43 = matrix1.M43 / matrix2.M43;
		matrix1.M44 = matrix1.M44 / matrix2.M44;
		return matrix1;
	}

	public static void divide(final Matrix matrix1, final Matrix matrix2, Matrix result)
	{
		result.M11 = matrix1.M11 / matrix2.M11;
		result.M12 = matrix1.M12 / matrix2.M12;
		result.M13 = matrix1.M13 / matrix2.M13;
		result.M14 = matrix1.M14 / matrix2.M14;
		result.M21 = matrix1.M21 / matrix2.M21;
		result.M22 = matrix1.M22 / matrix2.M22;
		result.M23 = matrix1.M23 / matrix2.M23;
		result.M24 = matrix1.M24 / matrix2.M24;
		result.M31 = matrix1.M31 / matrix2.M31;
		result.M32 = matrix1.M32 / matrix2.M32;
		result.M33 = matrix1.M33 / matrix2.M33;
		result.M34 = matrix1.M34 / matrix2.M34;
		result.M41 = matrix1.M41 / matrix2.M41;
		result.M42 = matrix1.M42 / matrix2.M42;
		result.M43 = matrix1.M43 / matrix2.M43;
		result.M44 = matrix1.M44 / matrix2.M44;
	}

	public static Matrix divide(Matrix matrix1, float divider)
	{
		float num = 1f / divider;
		matrix1.M11 = matrix1.M11 * num;
		matrix1.M12 = matrix1.M12 * num;
		matrix1.M13 = matrix1.M13 * num;
		matrix1.M14 = matrix1.M14 * num;
		matrix1.M21 = matrix1.M21 * num;
		matrix1.M22 = matrix1.M22 * num;
		matrix1.M23 = matrix1.M23 * num;
		matrix1.M24 = matrix1.M24 * num;
		matrix1.M31 = matrix1.M31 * num;
		matrix1.M32 = matrix1.M32 * num;
		matrix1.M33 = matrix1.M33 * num;
		matrix1.M34 = matrix1.M34 * num;
		matrix1.M41 = matrix1.M41 * num;
		matrix1.M42 = matrix1.M42 * num;
		matrix1.M43 = matrix1.M43 * num;
		matrix1.M44 = matrix1.M44 * num;
		return matrix1;
	}

	public static void divide(final Matrix matrix1, float divider, Matrix result)
	{
		float num = 1f / divider;
		result.M11 = matrix1.M11 * num;
		result.M12 = matrix1.M12 * num;
		result.M13 = matrix1.M13 * num;
		result.M14 = matrix1.M14 * num;
		result.M21 = matrix1.M21 * num;
		result.M22 = matrix1.M22 * num;
		result.M23 = matrix1.M23 * num;
		result.M24 = matrix1.M24 * num;
		result.M31 = matrix1.M31 * num;
		result.M32 = matrix1.M32 * num;
		result.M33 = matrix1.M33 * num;
		result.M34 = matrix1.M34 * num;
		result.M41 = matrix1.M41 * num;
		result.M42 = matrix1.M42 * num;
		result.M43 = matrix1.M43 * num;
		result.M44 = matrix1.M44 * num;
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
		return this.equals((Matrix) obj);
	}

	// Helper method
	private boolean equals(Matrix other)
	{
		return (this.M11 == other.M11 && this.M12 == other.M12 && this.M13 == other.M13 && this.M14 == other.M14 &&	//
				this.M21 == other.M21 && this.M22 == other.M22 && this.M23 == other.M23 && this.M24 == other.M24 &&	// 
				this.M31 == other.M31 && this.M32 == other.M32 && this.M33 == other.M33 && this.M34 == other.M34 &&	//
				this.M41 == other.M41 && this.M42 == other.M42 && this.M43 == other.M43 && this.M44 == other.M44);
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
		return (((((((((((((((Float.hashCode(M11) + Float.hashCode(M12)) + Float.hashCode(M13)) + Float.hashCode(M14)) + Float
				.hashCode(M21)) + Float.hashCode(M22)) + Float.hashCode(M23)) + Float.hashCode(M24)) + Float
				.hashCode(M31)) + Float.hashCode(M32)) + Float.hashCode(M33)) + Float.hashCode(M34)) + Float
				.hashCode(M41)) + Float.hashCode(M42)) + Float.hashCode(M43)) + Float.hashCode(M44));
	}

	public static Matrix invert(Matrix matrix)
	{
		invert(matrix, matrix);
		return matrix;
	}

	public static void invert(final Matrix matrix, Matrix result)
	{
		float num1 = matrix.M11;
		float num2 = matrix.M12;
		float num3 = matrix.M13;
		float num4 = matrix.M14;
		float num5 = matrix.M21;
		float num6 = matrix.M22;
		float num7 = matrix.M23;
		float num8 = matrix.M24;
		float num9 = matrix.M31;
		float num10 = matrix.M32;
		float num11 = matrix.M33;
		float num12 = matrix.M34;
		float num13 = matrix.M41;
		float num14 = matrix.M42;
		float num15 = matrix.M43;
		float num16 = matrix.M44;
		float num17 = (float) ((double) num11 * (double) num16 - (double) num12 * (double) num15);
		float num18 = (float) ((double) num10 * (double) num16 - (double) num12 * (double) num14);
		float num19 = (float) ((double) num10 * (double) num15 - (double) num11 * (double) num14);
		float num20 = (float) ((double) num9 * (double) num16 - (double) num12 * (double) num13);
		float num21 = (float) ((double) num9 * (double) num15 - (double) num11 * (double) num13);
		float num22 = (float) ((double) num9 * (double) num14 - (double) num10 * (double) num13);
		float num23 = (float) ((double) num6 * (double) num17 - (double) num7 * (double) num18 + (double) num8
				* (double) num19);
		float num24 = (float) -((double) num5 * (double) num17 - (double) num7 * (double) num20 + (double) num8
				* (double) num21);
		float num25 = (float) ((double) num5 * (double) num18 - (double) num6 * (double) num20 + (double) num8
				* (double) num22);
		float num26 = (float) -((double) num5 * (double) num19 - (double) num6 * (double) num21 + (double) num7
				* (double) num22);
		float num27 = (float) (1.0 / ((double) num1 * (double) num23 + (double) num2 * (double) num24 + (double) num3
				* (double) num25 + (double) num4 * (double) num26));

		result.M11 = num23 * num27;
		result.M21 = num24 * num27;
		result.M31 = num25 * num27;
		result.M41 = num26 * num27;
		result.M12 = (float) -((double) num2 * (double) num17 - (double) num3 * (double) num18 + (double) num4
				* (double) num19)
				* num27;
		result.M22 = (float) ((double) num1 * (double) num17 - (double) num3 * (double) num20 + (double) num4
				* (double) num21)
				* num27;
		result.M32 = (float) -((double) num1 * (double) num18 - (double) num2 * (double) num20 + (double) num4
				* (double) num22)
				* num27;
		result.M42 = (float) ((double) num1 * (double) num19 - (double) num2 * (double) num21 + (double) num3
				* (double) num22)
				* num27;
		float num28 = (float) ((double) num7 * (double) num16 - (double) num8 * (double) num15);
		float num29 = (float) ((double) num6 * (double) num16 - (double) num8 * (double) num14);
		float num30 = (float) ((double) num6 * (double) num15 - (double) num7 * (double) num14);
		float num31 = (float) ((double) num5 * (double) num16 - (double) num8 * (double) num13);
		float num32 = (float) ((double) num5 * (double) num15 - (double) num7 * (double) num13);
		float num33 = (float) ((double) num5 * (double) num14 - (double) num6 * (double) num13);
		result.M13 = (float) ((double) num2 * (double) num28 - (double) num3 * (double) num29 + (double) num4
				* (double) num30)
				* num27;
		result.M23 = (float) -((double) num1 * (double) num28 - (double) num3 * (double) num31 + (double) num4
				* (double) num32)
				* num27;
		result.M33 = (float) ((double) num1 * (double) num29 - (double) num2 * (double) num31 + (double) num4
				* (double) num33)
				* num27;
		result.M43 = (float) -((double) num1 * (double) num30 - (double) num2 * (double) num32 + (double) num3
				* (double) num33)
				* num27;
		float num34 = (float) ((double) num7 * (double) num12 - (double) num8 * (double) num11);
		float num35 = (float) ((double) num6 * (double) num12 - (double) num8 * (double) num10);
		float num36 = (float) ((double) num6 * (double) num11 - (double) num7 * (double) num10);
		float num37 = (float) ((double) num5 * (double) num12 - (double) num8 * (double) num9);
		float num38 = (float) ((double) num5 * (double) num11 - (double) num7 * (double) num9);
		float num39 = (float) ((double) num5 * (double) num10 - (double) num6 * (double) num9);
		result.M14 = (float) -((double) num2 * (double) num34 - (double) num3 * (double) num35 + (double) num4
				* (double) num36)
				* num27;
		result.M24 = (float) ((double) num1 * (double) num34 - (double) num3 * (double) num37 + (double) num4
				* (double) num38)
				* num27;
		result.M34 = (float) -((double) num1 * (double) num35 - (double) num2 * (double) num37 + (double) num4
				* (double) num39)
				* num27;
		result.M44 = (float) ((double) num1 * (double) num36 - (double) num2 * (double) num38 + (double) num3
				* (double) num39)
				* num27;

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

	public static Matrix lerp(Matrix matrix1, Matrix matrix2, float amount)
	{
		matrix1.M11 = matrix1.M11 + ((matrix2.M11 - matrix1.M11) * amount);
		matrix1.M12 = matrix1.M12 + ((matrix2.M12 - matrix1.M12) * amount);
		matrix1.M13 = matrix1.M13 + ((matrix2.M13 - matrix1.M13) * amount);
		matrix1.M14 = matrix1.M14 + ((matrix2.M14 - matrix1.M14) * amount);
		matrix1.M21 = matrix1.M21 + ((matrix2.M21 - matrix1.M21) * amount);
		matrix1.M22 = matrix1.M22 + ((matrix2.M22 - matrix1.M22) * amount);
		matrix1.M23 = matrix1.M23 + ((matrix2.M23 - matrix1.M23) * amount);
		matrix1.M24 = matrix1.M24 + ((matrix2.M24 - matrix1.M24) * amount);
		matrix1.M31 = matrix1.M31 + ((matrix2.M31 - matrix1.M31) * amount);
		matrix1.M32 = matrix1.M32 + ((matrix2.M32 - matrix1.M32) * amount);
		matrix1.M33 = matrix1.M33 + ((matrix2.M33 - matrix1.M33) * amount);
		matrix1.M34 = matrix1.M34 + ((matrix2.M34 - matrix1.M34) * amount);
		matrix1.M41 = matrix1.M41 + ((matrix2.M41 - matrix1.M41) * amount);
		matrix1.M42 = matrix1.M42 + ((matrix2.M42 - matrix1.M42) * amount);
		matrix1.M43 = matrix1.M43 + ((matrix2.M43 - matrix1.M43) * amount);
		matrix1.M44 = matrix1.M44 + ((matrix2.M44 - matrix1.M44) * amount);
		return matrix1;
	}

	public static void lerp(final Matrix matrix1, final Matrix matrix2, float amount, Matrix result)
	{
		result.M11 = matrix1.M11 + ((matrix2.M11 - matrix1.M11) * amount);
		result.M12 = matrix1.M12 + ((matrix2.M12 - matrix1.M12) * amount);
		result.M13 = matrix1.M13 + ((matrix2.M13 - matrix1.M13) * amount);
		result.M14 = matrix1.M14 + ((matrix2.M14 - matrix1.M14) * amount);
		result.M21 = matrix1.M21 + ((matrix2.M21 - matrix1.M21) * amount);
		result.M22 = matrix1.M22 + ((matrix2.M22 - matrix1.M22) * amount);
		result.M23 = matrix1.M23 + ((matrix2.M23 - matrix1.M23) * amount);
		result.M24 = matrix1.M24 + ((matrix2.M24 - matrix1.M24) * amount);
		result.M31 = matrix1.M31 + ((matrix2.M31 - matrix1.M31) * amount);
		result.M32 = matrix1.M32 + ((matrix2.M32 - matrix1.M32) * amount);
		result.M33 = matrix1.M33 + ((matrix2.M33 - matrix1.M33) * amount);
		result.M34 = matrix1.M34 + ((matrix2.M34 - matrix1.M34) * amount);
		result.M41 = matrix1.M41 + ((matrix2.M41 - matrix1.M41) * amount);
		result.M42 = matrix1.M42 + ((matrix2.M42 - matrix1.M42) * amount);
		result.M43 = matrix1.M43 + ((matrix2.M43 - matrix1.M43) * amount);
		result.M44 = matrix1.M44 + ((matrix2.M44 - matrix1.M44) * amount);
	}

	public static Matrix multiply(Matrix matrix1, Matrix matrix2)
	{
		float m11 = (((matrix1.M11 * matrix2.M11) + (matrix1.M12 * matrix2.M21)) + (matrix1.M13 * matrix2.M31))
				+ (matrix1.M14 * matrix2.M41);
		float m12 = (((matrix1.M11 * matrix2.M12) + (matrix1.M12 * matrix2.M22)) + (matrix1.M13 * matrix2.M32))
				+ (matrix1.M14 * matrix2.M42);
		float m13 = (((matrix1.M11 * matrix2.M13) + (matrix1.M12 * matrix2.M23)) + (matrix1.M13 * matrix2.M33))
				+ (matrix1.M14 * matrix2.M43);
		float m14 = (((matrix1.M11 * matrix2.M14) + (matrix1.M12 * matrix2.M24)) + (matrix1.M13 * matrix2.M34))
				+ (matrix1.M14 * matrix2.M44);
		float m21 = (((matrix1.M21 * matrix2.M11) + (matrix1.M22 * matrix2.M21)) + (matrix1.M23 * matrix2.M31))
				+ (matrix1.M24 * matrix2.M41);
		float m22 = (((matrix1.M21 * matrix2.M12) + (matrix1.M22 * matrix2.M22)) + (matrix1.M23 * matrix2.M32))
				+ (matrix1.M24 * matrix2.M42);
		float m23 = (((matrix1.M21 * matrix2.M13) + (matrix1.M22 * matrix2.M23)) + (matrix1.M23 * matrix2.M33))
				+ (matrix1.M24 * matrix2.M43);
		float m24 = (((matrix1.M21 * matrix2.M14) + (matrix1.M22 * matrix2.M24)) + (matrix1.M23 * matrix2.M34))
				+ (matrix1.M24 * matrix2.M44);
		float m31 = (((matrix1.M31 * matrix2.M11) + (matrix1.M32 * matrix2.M21)) + (matrix1.M33 * matrix2.M31))
				+ (matrix1.M34 * matrix2.M41);
		float m32 = (((matrix1.M31 * matrix2.M12) + (matrix1.M32 * matrix2.M22)) + (matrix1.M33 * matrix2.M32))
				+ (matrix1.M34 * matrix2.M42);
		float m33 = (((matrix1.M31 * matrix2.M13) + (matrix1.M32 * matrix2.M23)) + (matrix1.M33 * matrix2.M33))
				+ (matrix1.M34 * matrix2.M43);
		float m34 = (((matrix1.M31 * matrix2.M14) + (matrix1.M32 * matrix2.M24)) + (matrix1.M33 * matrix2.M34))
				+ (matrix1.M34 * matrix2.M44);
		float m41 = (((matrix1.M41 * matrix2.M11) + (matrix1.M42 * matrix2.M21)) + (matrix1.M43 * matrix2.M31))
				+ (matrix1.M44 * matrix2.M41);
		float m42 = (((matrix1.M41 * matrix2.M12) + (matrix1.M42 * matrix2.M22)) + (matrix1.M43 * matrix2.M32))
				+ (matrix1.M44 * matrix2.M42);
		float m43 = (((matrix1.M41 * matrix2.M13) + (matrix1.M42 * matrix2.M23)) + (matrix1.M43 * matrix2.M33))
				+ (matrix1.M44 * matrix2.M43);
		float m44 = (((matrix1.M41 * matrix2.M14) + (matrix1.M42 * matrix2.M24)) + (matrix1.M43 * matrix2.M34))
				+ (matrix1.M44 * matrix2.M44);
		matrix1.M11 = m11;
		matrix1.M12 = m12;
		matrix1.M13 = m13;
		matrix1.M14 = m14;
		matrix1.M21 = m21;
		matrix1.M22 = m22;
		matrix1.M23 = m23;
		matrix1.M24 = m24;
		matrix1.M31 = m31;
		matrix1.M32 = m32;
		matrix1.M33 = m33;
		matrix1.M34 = m34;
		matrix1.M41 = m41;
		matrix1.M42 = m42;
		matrix1.M43 = m43;
		matrix1.M44 = m44;
		return matrix1;
	}

	public static void multiply(final Matrix matrix1, final Matrix matrix2, Matrix result)
	{
		float m11 = (((matrix1.M11 * matrix2.M11) + (matrix1.M12 * matrix2.M21)) + (matrix1.M13 * matrix2.M31))
				+ (matrix1.M14 * matrix2.M41);
		float m12 = (((matrix1.M11 * matrix2.M12) + (matrix1.M12 * matrix2.M22)) + (matrix1.M13 * matrix2.M32))
				+ (matrix1.M14 * matrix2.M42);
		float m13 = (((matrix1.M11 * matrix2.M13) + (matrix1.M12 * matrix2.M23)) + (matrix1.M13 * matrix2.M33))
				+ (matrix1.M14 * matrix2.M43);
		float m14 = (((matrix1.M11 * matrix2.M14) + (matrix1.M12 * matrix2.M24)) + (matrix1.M13 * matrix2.M34))
				+ (matrix1.M14 * matrix2.M44);
		float m21 = (((matrix1.M21 * matrix2.M11) + (matrix1.M22 * matrix2.M21)) + (matrix1.M23 * matrix2.M31))
				+ (matrix1.M24 * matrix2.M41);
		float m22 = (((matrix1.M21 * matrix2.M12) + (matrix1.M22 * matrix2.M22)) + (matrix1.M23 * matrix2.M32))
				+ (matrix1.M24 * matrix2.M42);
		float m23 = (((matrix1.M21 * matrix2.M13) + (matrix1.M22 * matrix2.M23)) + (matrix1.M23 * matrix2.M33))
				+ (matrix1.M24 * matrix2.M43);
		float m24 = (((matrix1.M21 * matrix2.M14) + (matrix1.M22 * matrix2.M24)) + (matrix1.M23 * matrix2.M34))
				+ (matrix1.M24 * matrix2.M44);
		float m31 = (((matrix1.M31 * matrix2.M11) + (matrix1.M32 * matrix2.M21)) + (matrix1.M33 * matrix2.M31))
				+ (matrix1.M34 * matrix2.M41);
		float m32 = (((matrix1.M31 * matrix2.M12) + (matrix1.M32 * matrix2.M22)) + (matrix1.M33 * matrix2.M32))
				+ (matrix1.M34 * matrix2.M42);
		float m33 = (((matrix1.M31 * matrix2.M13) + (matrix1.M32 * matrix2.M23)) + (matrix1.M33 * matrix2.M33))
				+ (matrix1.M34 * matrix2.M43);
		float m34 = (((matrix1.M31 * matrix2.M14) + (matrix1.M32 * matrix2.M24)) + (matrix1.M33 * matrix2.M34))
				+ (matrix1.M34 * matrix2.M44);
		float m41 = (((matrix1.M41 * matrix2.M11) + (matrix1.M42 * matrix2.M21)) + (matrix1.M43 * matrix2.M31))
				+ (matrix1.M44 * matrix2.M41);
		float m42 = (((matrix1.M41 * matrix2.M12) + (matrix1.M42 * matrix2.M22)) + (matrix1.M43 * matrix2.M32))
				+ (matrix1.M44 * matrix2.M42);
		float m43 = (((matrix1.M41 * matrix2.M13) + (matrix1.M42 * matrix2.M23)) + (matrix1.M43 * matrix2.M33))
				+ (matrix1.M44 * matrix2.M43);
		float m44 = (((matrix1.M41 * matrix2.M14) + (matrix1.M42 * matrix2.M24)) + (matrix1.M43 * matrix2.M34))
				+ (matrix1.M44 * matrix2.M44);
		result.M11 = m11;
		result.M12 = m12;
		result.M13 = m13;
		result.M14 = m14;
		result.M21 = m21;
		result.M22 = m22;
		result.M23 = m23;
		result.M24 = m24;
		result.M31 = m31;
		result.M32 = m32;
		result.M33 = m33;
		result.M34 = m34;
		result.M41 = m41;
		result.M42 = m42;
		result.M43 = m43;
		result.M44 = m44;
	}

	public static Matrix multiply(Matrix matrix1, float scaleFactor)
	{
		matrix1.M11 *= scaleFactor;
		matrix1.M12 *= scaleFactor;
		matrix1.M13 *= scaleFactor;
		matrix1.M14 *= scaleFactor;
		matrix1.M21 *= scaleFactor;
		matrix1.M22 *= scaleFactor;
		matrix1.M23 *= scaleFactor;
		matrix1.M24 *= scaleFactor;
		matrix1.M31 *= scaleFactor;
		matrix1.M32 *= scaleFactor;
		matrix1.M33 *= scaleFactor;
		matrix1.M34 *= scaleFactor;
		matrix1.M41 *= scaleFactor;
		matrix1.M42 *= scaleFactor;
		matrix1.M43 *= scaleFactor;
		matrix1.M44 *= scaleFactor;
		return matrix1;
	}

	public static void multiply(final Matrix matrix1, float sclaeFactor, Matrix result)
	{
		result.M11 = matrix1.M11 * sclaeFactor;
		result.M12 = matrix1.M12 * sclaeFactor;
		result.M13 = matrix1.M13 * sclaeFactor;
		result.M14 = matrix1.M14 * sclaeFactor;
		result.M21 = matrix1.M21 * sclaeFactor;
		result.M22 = matrix1.M22 * sclaeFactor;
		result.M23 = matrix1.M23 * sclaeFactor;
		result.M24 = matrix1.M24 * sclaeFactor;
		result.M31 = matrix1.M31 * sclaeFactor;
		result.M32 = matrix1.M32 * sclaeFactor;
		result.M33 = matrix1.M33 * sclaeFactor;
		result.M34 = matrix1.M34 * sclaeFactor;
		result.M41 = matrix1.M41 * sclaeFactor;
		result.M42 = matrix1.M42 * sclaeFactor;
		result.M43 = matrix1.M43 * sclaeFactor;
		result.M44 = matrix1.M44 * sclaeFactor;
	}

	/// <summary>
    /// Copy the values of specified <see cref="Matrix"/> to the float array.
    /// </summary>
    /// <param name="matrix">The source <see cref="Matrix"/>.</param>
    /// <returns>The array which matrix values will be stored.</returns>
    /// <remarks>
    /// Required for OpenGL 2.0 projection matrix stuff.
    /// </remarks>
	public static float[] toFloatArray(Matrix mat)
	{
		float[] matarray = {	//
				mat.M11, mat.M12, mat.M13, mat.M14,	//
				mat.M21, mat.M22, mat.M23, mat.M24,	//
				mat.M31, mat.M32, mat.M33, mat.M34,	//
				mat.M41, mat.M42, mat.M43, mat.M44 };
		return matarray;
	}
	
	public static Matrix negate(Matrix matrix)
	{
		matrix.M11 = -matrix.M11;
		matrix.M12 = -matrix.M12;
		matrix.M13 = -matrix.M13;
		matrix.M14 = -matrix.M14;
		matrix.M21 = -matrix.M21;
		matrix.M22 = -matrix.M22;
		matrix.M23 = -matrix.M23;
		matrix.M24 = -matrix.M24;
		matrix.M31 = -matrix.M31;
		matrix.M32 = -matrix.M32;
		matrix.M33 = -matrix.M33;
		matrix.M34 = -matrix.M34;
		matrix.M41 = -matrix.M41;
		matrix.M42 = -matrix.M42;
		matrix.M43 = -matrix.M43;
		matrix.M44 = -matrix.M44;
		return matrix;
	}

	public static void negate(final Matrix matrix, Matrix result)
	{
		result.M11 = -matrix.M11;
		result.M12 = -matrix.M12;
		result.M13 = -matrix.M13;
		result.M14 = -matrix.M14;
		result.M21 = -matrix.M21;
		result.M22 = -matrix.M22;
		result.M23 = -matrix.M23;
		result.M24 = -matrix.M24;
		result.M31 = -matrix.M31;
		result.M32 = -matrix.M32;
		result.M33 = -matrix.M33;
		result.M34 = -matrix.M34;
		result.M41 = -matrix.M41;
		result.M42 = -matrix.M42;
		result.M43 = -matrix.M43;
		result.M44 = -matrix.M44;
	}

	// TODO: Turn some of those operators into methods like equals and not equals
	// public static Matrix operator +(Matrix matrix1, Matrix matrix2)
	// {
	// Matrix.Add(final matrix1, final matrix2, matrix1);
	// return matrix1;
	// }

	// public static Matrix operator /(Matrix matrix1, Matrix matrix2)
	// {
	// matrix1.M11 = matrix1.M11 / matrix2.M11;
	// matrix1.M12 = matrix1.M12 / matrix2.M12;
	// matrix1.M13 = matrix1.M13 / matrix2.M13;
	// matrix1.M14 = matrix1.M14 / matrix2.M14;
	// matrix1.M21 = matrix1.M21 / matrix2.M21;
	// matrix1.M22 = matrix1.M22 / matrix2.M22;
	// matrix1.M23 = matrix1.M23 / matrix2.M23;
	// matrix1.M24 = matrix1.M24 / matrix2.M24;
	// matrix1.M31 = matrix1.M31 / matrix2.M31;
	// matrix1.M32 = matrix1.M32 / matrix2.M32;
	// matrix1.M33 = matrix1.M33 / matrix2.M33;
	// matrix1.M34 = matrix1.M34 / matrix2.M34;
	// matrix1.M41 = matrix1.M41 / matrix2.M41;
	// matrix1.M42 = matrix1.M42 / matrix2.M42;
	// matrix1.M43 = matrix1.M43 / matrix2.M43;
	// matrix1.M44 = matrix1.M44 / matrix2.M44;
	// return matrix1;
	// }

	// public static Matrix operator /(Matrix matrix, float divider)
	// {
	// float num = 1f / divider;
	// matrix.M11 = matrix.M11 * num;
	// matrix.M12 = matrix.M12 * num;
	// matrix.M13 = matrix.M13 * num;
	// matrix.M14 = matrix.M14 * num;
	// matrix.M21 = matrix.M21 * num;
	// matrix.M22 = matrix.M22 * num;
	// matrix.M23 = matrix.M23 * num;
	// matrix.M24 = matrix.M24 * num;
	// matrix.M31 = matrix.M31 * num;
	// matrix.M32 = matrix.M32 * num;
	// matrix.M33 = matrix.M33 * num;
	// matrix.M34 = matrix.M34 * num;
	// matrix.M41 = matrix.M41 * num;
	// matrix.M42 = matrix.M42 * num;
	// matrix.M43 = matrix.M43 * num;
	// matrix.M44 = matrix.M44 * num;
	// return matrix;
	// }

	// public static boolean operator ==(Matrix matrix1, Matrix matrix2)
	// {
	// return (
	// matrix1.M11 == matrix2.M11 &&
	// matrix1.M12 == matrix2.M12 &&
	// matrix1.M13 == matrix2.M13 &&
	// matrix1.M14 == matrix2.M14 &&
	// matrix1.M21 == matrix2.M21 &&
	// matrix1.M22 == matrix2.M22 &&
	// matrix1.M23 == matrix2.M23 &&
	// matrix1.M24 == matrix2.M24 &&
	// matrix1.M31 == matrix2.M31 &&
	// matrix1.M32 == matrix2.M32 &&
	// matrix1.M33 == matrix2.M33 &&
	// matrix1.M34 == matrix2.M34 &&
	// matrix1.M41 == matrix2.M41 &&
	// matrix1.M42 == matrix2.M42 &&
	// matrix1.M43 == matrix2.M43 &&
	// matrix1.M44 == matrix2.M44
	// );
	// }

	// public static boolean operator !=(Matrix matrix1, Matrix matrix2)
	// {
	// return (
	// matrix1.M11 != matrix2.M11 ||
	// matrix1.M12 != matrix2.M12 ||
	// matrix1.M13 != matrix2.M13 ||
	// matrix1.M14 != matrix2.M14 ||
	// matrix1.M21 != matrix2.M21 ||
	// matrix1.M22 != matrix2.M22 ||
	// matrix1.M23 != matrix2.M23 ||
	// matrix1.M24 != matrix2.M24 ||
	// matrix1.M31 != matrix2.M31 ||
	// matrix1.M32 != matrix2.M32 ||
	// matrix1.M33 != matrix2.M33 ||
	// matrix1.M34 != matrix2.M34 ||
	// matrix1.M41 != matrix2.M41 ||
	// matrix1.M42 != matrix2.M42 ||
	// matrix1.M43 != matrix2.M43 ||
	// matrix1.M44 != matrix2.M44
	// );
	// }

	// public static Matrix operator *(Matrix matrix1, Matrix matrix2)
	// {
	// float m11 = (((matrix1.M11 * matrix2.M11) + (matrix1.M12 * matrix2.M21)) + (matrix1.M13 *
	// matrix2.M31)) + (matrix1.M14 * matrix2.M41);
	// float m12 = (((matrix1.M11 * matrix2.M12) + (matrix1.M12 * matrix2.M22)) + (matrix1.M13 *
	// matrix2.M32)) + (matrix1.M14 * matrix2.M42);
	// float m13 = (((matrix1.M11 * matrix2.M13) + (matrix1.M12 * matrix2.M23)) + (matrix1.M13 *
	// matrix2.M33)) + (matrix1.M14 * matrix2.M43);
	// float m14 = (((matrix1.M11 * matrix2.M14) + (matrix1.M12 * matrix2.M24)) + (matrix1.M13 *
	// matrix2.M34)) + (matrix1.M14 * matrix2.M44);
	// float m21 = (((matrix1.M21 * matrix2.M11) + (matrix1.M22 * matrix2.M21)) + (matrix1.M23 *
	// matrix2.M31)) + (matrix1.M24 * matrix2.M41);
	// float m22 = (((matrix1.M21 * matrix2.M12) + (matrix1.M22 * matrix2.M22)) + (matrix1.M23 *
	// matrix2.M32)) + (matrix1.M24 * matrix2.M42);
	// float m23 = (((matrix1.M21 * matrix2.M13) + (matrix1.M22 * matrix2.M23)) + (matrix1.M23 *
	// matrix2.M33)) + (matrix1.M24 * matrix2.M43);
	// float m24 = (((matrix1.M21 * matrix2.M14) + (matrix1.M22 * matrix2.M24)) + (matrix1.M23 *
	// matrix2.M34)) + (matrix1.M24 * matrix2.M44);
	// float m31 = (((matrix1.M31 * matrix2.M11) + (matrix1.M32 * matrix2.M21)) + (matrix1.M33 *
	// matrix2.M31)) + (matrix1.M34 * matrix2.M41);
	// float m32 = (((matrix1.M31 * matrix2.M12) + (matrix1.M32 * matrix2.M22)) + (matrix1.M33 *
	// matrix2.M32)) + (matrix1.M34 * matrix2.M42);
	// float m33 = (((matrix1.M31 * matrix2.M13) + (matrix1.M32 * matrix2.M23)) + (matrix1.M33 *
	// matrix2.M33)) + (matrix1.M34 * matrix2.M43);
	// float m34 = (((matrix1.M31 * matrix2.M14) + (matrix1.M32 * matrix2.M24)) + (matrix1.M33 *
	// matrix2.M34)) + (matrix1.M34 * matrix2.M44);
	// float m41 = (((matrix1.M41 * matrix2.M11) + (matrix1.M42 * matrix2.M21)) + (matrix1.M43 *
	// matrix2.M31)) + (matrix1.M44 * matrix2.M41);
	// float m42 = (((matrix1.M41 * matrix2.M12) + (matrix1.M42 * matrix2.M22)) + (matrix1.M43 *
	// matrix2.M32)) + (matrix1.M44 * matrix2.M42);
	// float m43 = (((matrix1.M41 * matrix2.M13) + (matrix1.M42 * matrix2.M23)) + (matrix1.M43 *
	// matrix2.M33)) + (matrix1.M44 * matrix2.M43);
	// float m44 = (((matrix1.M41 * matrix2.M14) + (matrix1.M42 * matrix2.M24)) + (matrix1.M43 *
	// matrix2.M34)) + (matrix1.M44 * matrix2.M44);
	// matrix1.M11 = m11;
	// matrix1.M12 = m12;
	// matrix1.M13 = m13;
	// matrix1.M14 = m14;
	// matrix1.M21 = m21;
	// matrix1.M22 = m22;
	// matrix1.M23 = m23;
	// matrix1.M24 = m24;
	// matrix1.M31 = m31;
	// matrix1.M32 = m32;
	// matrix1.M33 = m33;
	// matrix1.M34 = m34;
	// matrix1.M41 = m41;
	// matrix1.M42 = m42;
	// matrix1.M43 = m43;
	// matrix1.M44 = m44;
	// return matrix1;
	// }

	// public static Matrix operator *(Matrix matrix, float scaleFactor)
	// {
	// matrix.M11 = matrix.M11 * scaleFactor;
	// matrix.M12 = matrix.M12 * scaleFactor;
	// matrix.M13 = matrix.M13 * scaleFactor;
	// matrix.M14 = matrix.M14 * scaleFactor;
	// matrix.M21 = matrix.M21 * scaleFactor;
	// matrix.M22 = matrix.M22 * scaleFactor;
	// matrix.M23 = matrix.M23 * scaleFactor;
	// matrix.M24 = matrix.M24 * scaleFactor;
	// matrix.M31 = matrix.M31 * scaleFactor;
	// matrix.M32 = matrix.M32 * scaleFactor;
	// matrix.M33 = matrix.M33 * scaleFactor;
	// matrix.M34 = matrix.M34 * scaleFactor;
	// matrix.M41 = matrix.M41 * scaleFactor;
	// matrix.M42 = matrix.M42 * scaleFactor;
	// matrix.M43 = matrix.M43 * scaleFactor;
	// matrix.M44 = matrix.M44 * scaleFactor;
	// return matrix;
	// }

	// public static Matrix operator -(Matrix matrix1, Matrix matrix2)
	// {
	// matrix1.M11 = matrix1.M11 - matrix2.M11;
	// matrix1.M12 = matrix1.M12 - matrix2.M12;
	// matrix1.M13 = matrix1.M13 - matrix2.M13;
	// matrix1.M14 = matrix1.M14 - matrix2.M14;
	// matrix1.M21 = matrix1.M21 - matrix2.M21;
	// matrix1.M22 = matrix1.M22 - matrix2.M22;
	// matrix1.M23 = matrix1.M23 - matrix2.M23;
	// matrix1.M24 = matrix1.M24 - matrix2.M24;
	// matrix1.M31 = matrix1.M31 - matrix2.M31;
	// matrix1.M32 = matrix1.M32 - matrix2.M32;
	// matrix1.M33 = matrix1.M33 - matrix2.M33;
	// matrix1.M34 = matrix1.M34 - matrix2.M34;
	// matrix1.M41 = matrix1.M41 - matrix2.M41;
	// matrix1.M42 = matrix1.M42 - matrix2.M42;
	// matrix1.M43 = matrix1.M43 - matrix2.M43;
	// matrix1.M44 = matrix1.M44 - matrix2.M44;
	// return matrix1;
	// }

	// public static Matrix operator -(Matrix matrix)
	// {
	// matrix.M11 = -matrix.M11;
	// matrix.M12 = -matrix.M12;
	// matrix.M13 = -matrix.M13;
	// matrix.M14 = -matrix.M14;
	// matrix.M21 = -matrix.M21;
	// matrix.M22 = -matrix.M22;
	// matrix.M23 = -matrix.M23;
	// matrix.M24 = -matrix.M24;
	// matrix.M31 = -matrix.M31;
	// matrix.M32 = -matrix.M32;
	// matrix.M33 = -matrix.M33;
	// matrix.M34 = -matrix.M34;
	// matrix.M41 = -matrix.M41;
	// matrix.M42 = -matrix.M42;
	// matrix.M43 = -matrix.M43;
	// matrix.M44 = -matrix.M44;
	// return matrix;
	// }

	public static Matrix subtract(Matrix matrix1, Matrix matrix2)
	{
		matrix1.M11 = matrix1.M11 - matrix2.M11;
		matrix1.M12 = matrix1.M12 - matrix2.M12;
		matrix1.M13 = matrix1.M13 - matrix2.M13;
		matrix1.M14 = matrix1.M14 - matrix2.M14;
		matrix1.M21 = matrix1.M21 - matrix2.M21;
		matrix1.M22 = matrix1.M22 - matrix2.M22;
		matrix1.M23 = matrix1.M23 - matrix2.M23;
		matrix1.M24 = matrix1.M24 - matrix2.M24;
		matrix1.M31 = matrix1.M31 - matrix2.M31;
		matrix1.M32 = matrix1.M32 - matrix2.M32;
		matrix1.M33 = matrix1.M33 - matrix2.M33;
		matrix1.M34 = matrix1.M34 - matrix2.M34;
		matrix1.M41 = matrix1.M41 - matrix2.M41;
		matrix1.M42 = matrix1.M42 - matrix2.M42;
		matrix1.M43 = matrix1.M43 - matrix2.M43;
		matrix1.M44 = matrix1.M44 - matrix2.M44;
		return matrix1;
	}

	public static void subtract(final Matrix matrix1, final Matrix matrix2, Matrix result)
	{
		result.M11 = matrix1.M11 - matrix2.M11;
		result.M12 = matrix1.M12 - matrix2.M12;
		result.M13 = matrix1.M13 - matrix2.M13;
		result.M14 = matrix1.M14 - matrix2.M14;
		result.M21 = matrix1.M21 - matrix2.M21;
		result.M22 = matrix1.M22 - matrix2.M22;
		result.M23 = matrix1.M23 - matrix2.M23;
		result.M24 = matrix1.M24 - matrix2.M24;
		result.M31 = matrix1.M31 - matrix2.M31;
		result.M32 = matrix1.M32 - matrix2.M32;
		result.M33 = matrix1.M33 - matrix2.M33;
		result.M34 = matrix1.M34 - matrix2.M34;
		result.M41 = matrix1.M41 - matrix2.M41;
		result.M42 = matrix1.M42 - matrix2.M42;
		result.M43 = matrix1.M43 - matrix2.M43;
		result.M44 = matrix1.M44 - matrix2.M44;
	}

	protected String debugDisplayString()
	{
		if (this.equals(identity))
		{
			return "Identity";
		}

		return (	//
				"( " + this.M11 + "  " + this.M12 + "  " + this.M13 + "  " + this.M14 + " )  \r\n" +	//
				"( " + this.M21 + "  " + this.M22 + "  " + this.M23 + "  " + this.M24 + " )  \r\n" +	//
				"( " + this.M31 + "  " + this.M32 + "  " + this.M33 + "  " + this.M34 + " )  \r\n" +	//
				"( " + this.M41 + "  " + this.M42 + "  " + this.M43 + "  " + this.M44 + " )");
	}

	@Override
	public String toString()
	{
		return "{M11:" + M11 + " M12:" + M12 + " M13:" + M13 + " M14:" + M14 + "}" +	//
			  " {M21:" + M21 + " M22:" + M22 + " M23:" + M23 + " M24:" + M24 + "}" +	//
			  " {M31:" + M31 + " M32:" + M32 + " M33:" + M33 + " M34:" + M34 + "}" +	//
			  " {M41:" + M41 + " M42:" + M42 + " M43:" + M43 + " M44:" + M44 + "}";
	}

	public static Matrix transpose(Matrix matrix)
	{
		Matrix result = Matrix.identity();
		transpose(matrix, result);
		return result;
	}

	public static void transpose(final Matrix matrix, Matrix result)
	{
		Matrix ret = Matrix.identity();

		ret.M11 = matrix.M11;
		ret.M12 = matrix.M21;
		ret.M13 = matrix.M31;
		ret.M14 = matrix.M41;

		ret.M21 = matrix.M12;
		ret.M22 = matrix.M22;
		ret.M23 = matrix.M32;
		ret.M24 = matrix.M42;

		ret.M31 = matrix.M13;
		ret.M32 = matrix.M23;
		ret.M33 = matrix.M33;
		ret.M34 = matrix.M43;

		ret.M41 = matrix.M14;
		ret.M42 = matrix.M24;
		ret.M43 = matrix.M34;
		ret.M44 = matrix.M44;

		result = ret;
	}

	// / <summary>
	// / Helper method for using the Laplace expansion theorem using two rows expansions to
	// calculate major and
	// / minor determinants of a 4x4 matrix. This method is used for inverting a matrix.
	// / </summary>
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

}
