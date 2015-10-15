package gameCore;

import gameCore.math.Matrix;
import gameCore.math.Quaternion;
import gameCore.math.Vector3;
import gameCore.math.Vector4;

class PlaneHelper
{
	/**
	 * Returns a value indicating what side (positive/negative) of a plane a point is.
	 * 
	 * @param point
	 *        The point to check with
	 * @param plane
	 *        The plane to check against
	 * @return Greater than zero if on the positive side, less than zero if on the negative size, 0
	 *         otherwise
	 */
	public static float classifyPoint(final Vector3 point, final Plane plane)
	{
		return point.x * plane.normal.x + point.y * plane.normal.y + point.z * plane.normal.z + plane.D;
	}

	/**
	 * Returns the perpendicular distance from a point to a plane.
	 * 
	 * @param point
	 *        The point to check
	 * @param plane
	 *        The place to check
	 * @return The perpendicular distance from the point to the plane
	 */
	public static float perpendicularDistance(final Vector3 point, final Plane plane)
	{
		// dist = (ax + by + cz + d) / sqrt(a*a + b*b + c*c)
		return (float) Math.abs((plane.normal.x * point.x + plane.normal.y * point.y + plane.normal.z * point.z)
				/ Math.sqrt(plane.normal.x * plane.normal.x + plane.normal.y * plane.normal.y + plane.normal.z
						* plane.normal.z));
	}
}

// C# struct
public class Plane // IEquatable<Plane>
{
	public float D;

	public Vector3 normal;

	// Note: Added this since it is provided by default for struct in C#
	public Plane()
	{
		normal = new Vector3();
		this.D = 0f;
	}

	public Plane(Vector4 value)
	{
		this(new Vector3(value.x, value.y, value.z), value.w);
	}

	public Plane(Vector3 normal, float d)
	{
		this.normal = normal;
		this.D = d;
	}

	public Plane(Vector3 a, Vector3 b, Vector3 c)
	{
		Vector3 ab = new Vector3();
		Vector3.subtract(b, a, ab);

		Vector3 ac = new Vector3();
		Vector3.subtract(c, a, ac);

		Vector3 cross = Vector3.cross(ab, ac);
		normal = Vector3.normalize(cross);
		D = -(Vector3.dot(normal, a));
	}

	public Plane(float a, float b, float c, float d)
	{
		this(new Vector3(a, b, c), d);
	}

	public float dot(Vector4 value)
	{
		return ((((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z)) + (this.D * value.w));
	}

	public void dot(final Vector4 value, float result)
	{
		result = (((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z))
				+ (this.D * value.w);
	}

	public float dotCoordinate(Vector3 value)
	{
		return ((((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z)) + this.D);
	}

	public void dotCoordinate(final Vector3 value, float result)
	{
		result = (((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z)) + this.D;
	}

	public float dotNormal(Vector3 value)
	{
		return (((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z));
	}

	public void dotNormal(final Vector3 value, float result)
	{
		result = ((this.normal.x * value.x) + (this.normal.y * value.y)) + (this.normal.z * value.z);
	}

	/// <summary>
	/// Transforms a normalized plane by a matrix.
	/// </summary>
	/// <param name="plane">The normalized plane to transform.</param>
	/// <param name="matrix">The transformation matrix.</param>
	/// <returns>The transformed plane.</returns>
	public static Plane transform(Plane plane, Matrix matrix)
	{
		Plane result = new Plane();
		transform(plane, matrix, result);
		return result;
	}

	/// <summary>
	/// Transforms a normalized plane by a matrix.
	/// </summary>
	/// <param name="plane">The normalized plane to transform.</param>
	/// <param name="matrix">The transformation matrix.</param>
	/// <param name="result">The transformed plane.</param>
	public static void transform(final Plane plane, final Matrix matrix, Plane result)
	{
		// See "Transforming Normals" in http://www.glprogramming.com/red/appendixf.html
		// for an explanation of how this works.

		Matrix transformedMatrix = Matrix.identity();
		Matrix.invert(matrix, transformedMatrix);
		Matrix.transpose(transformedMatrix, transformedMatrix);

		Vector4 vector = new Vector4(plane.normal, plane.D);

		Vector4 transformedVector = new Vector4();
		Vector4.transform(vector, transformedMatrix, transformedVector);

		result = new Plane(transformedVector);
    }

	/// <summary>
	/// Transforms a normalized plane by a quaternion rotation.
	/// </summary>
	/// <param name="plane">The normalized plane to transform.</param>
	/// <param name="rotation">The quaternion rotation.</param>
	/// <returns>The transformed plane.</returns>
	public static Plane transform(Plane plane, Quaternion rotation)
	{
		Plane result = new Plane();
		transform(plane, rotation, result);
		return result;
	}

	/// <summary>
	/// Transforms a normalized plane by a quaternion rotation.
	/// </summary>
	/// <param name="plane">The normalized plane to transform.</param>
	/// <param name="rotation">The quaternion rotation.</param>
	/// <param name="result">The transformed plane.</param>
	public static void transform(final Plane plane, final Quaternion rotation, Plane result)
	{
		Vector3.transform(plane.normal, rotation, result.normal);
		result.D = plane.D;
	}

	public void normalize()
	{
		float factor;
		Vector3 normal = this.normal;
		this.normal = Vector3.normalize(this.normal);
		factor = (float) Math.sqrt(this.normal.x * this.normal.x + this.normal.y * this.normal.y + this.normal.z
				* this.normal.z)
				/ (float) Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);
		D = D * factor;
	}

	public static Plane normalize(Plane value)
	{
		Plane ret = new Plane();
		normalize(value, ret);
		return ret;
	}

	public static void normalize(final Plane value, Plane result)
	{
		float factor;
		result.normal = Vector3.normalize(value.normal);
		factor = (float) Math.sqrt(result.normal.x * result.normal.x + result.normal.y * result.normal.y
				+ result.normal.z * result.normal.z)
				/ (float) Math.sqrt(value.normal.x * value.normal.x + value.normal.y * value.normal.y + value.normal.z
						* value.normal.z);
		result.D = value.D * factor;
	}

	public static boolean notEquals(Plane plane1, Plane plane2)
	{
		return !plane1.equals(plane2);
	}

	@Override
	public boolean equals(Object other)
	{
		return (other instanceof Plane) ? this.equals((Plane) other) : false;
	}

	public boolean equals(Plane other)
	{
		return ((normal.equals(other.normal)) && (D == other.D));
	}

	@Override
	public int hashCode()
	{
		return normal.hashCode() ^ Float.hashCode(D);
	}

	public PlaneIntersectionType intersects(BoundingBox box)
	{
		return box.intersects(this);
	}

	public void intersects(final BoundingBox box, PlaneIntersectionType result)
	{
		box.intersects(this, result);
	}

	public PlaneIntersectionType intersects(BoundingFrustum frustum)
	{
		return frustum.intersects(this);
	}

	public PlaneIntersectionType intersects(BoundingSphere sphere)
	{
		return sphere.intersects(this);
	}

	public void intersects(final BoundingSphere sphere, PlaneIntersectionType result)
	{
		sphere.intersects(this, result);
	}

	protected PlaneIntersectionType intersects(final Vector3 point)
	{
		float distance = dotCoordinate(point);

		if (distance > 0)
			return PlaneIntersectionType.Front;

		if (distance < 0)
			return PlaneIntersectionType.Back;

		return PlaneIntersectionType.Intersecting;
	}

	protected String debugDisplayString()
	{
		return (this.normal.debugDisplayString() + "  " + this.D);
	}

	@Override
	public String toString()
	{
		return "{{Normal:" + normal + " D:" + D + "}}";
	}

}
