package gameCore;

import java.util.Arrays;

import gameCore.math.Matrix;
import gameCore.math.Vector3;

public class BoundingFrustum
{

	/**
	 * The number of planes in the frustum.
	 */
	private final int PlaneCount = 6;

	/**
	 * The number of corner points in the frustum.
	 */
	public final int CornerCount = 8;

	private Matrix matrix;
	private Vector3[] corners = new Vector3[CornerCount];
	private Plane[] planes = new Plane[PlaneCount];

	// / <summary>
	// / Gets or sets the <see cref="Matrix"/> of the frustum.
	// / </summary>
	public Matrix getMatrix()
	{
		return this.matrix;
	}

	public void setMatrix(Matrix value)
	{
		this.matrix = value;
		this.createPlanes();
		this.createCorners();
	}

	/**
	 * Gets the near plane of the frustum.
	 * 
	 * @return The near plane of the frustum.
	 */
	public Plane near()
	{
		return this.planes[0];
	}

	/**
	 * Gets the far plane of the frustum.
	 * 
	 * @return The far plane of the frustum.
	 */
	public Plane far()
	{
		return this.planes[1];
	}

	/**
	 * Gets the left plane of the frustum.
	 * 
	 * @return The left plane of the frustum.
	 */
	public Plane left()
	{
		return this.planes[2];
	}

	/**
	 * Gets the right plane of the frustum.
	 * 
	 * @return The right plane of the frustum.
	 */
	public Plane right()
	{
		return this.planes[3];
	}

	/**
	 * Gets the top plane of the frustum.
	 * 
	 * @return The top plane of the frustum.
	 */
	public Plane top()
	{
		return this.planes[4];
	}

	/**
	 * Gets the bottom plane of the frustum.
	 * 
	 * @return The bottom plane of the frustum.
	 */
	public Plane bottom()
	{
		return this.planes[5];
	}

	protected String debugDisplayString()
	{
		return ("Near( " + this.planes[0].debugDisplayString() + " )  \r\n" +
				"Far( " + this.planes[1].debugDisplayString() + " )  \r\n" +
				"Left( " + this.planes[2].debugDisplayString() + " )  \r\n" +
				"Right( " + this.planes[3].debugDisplayString() + " )  \r\n" +
				"Top( " + this.planes[4].debugDisplayString() + " )  \r\n" +
				"Bottom( " + this.planes[5].debugDisplayString() + " )  ");
	}

	/**
	 * Constructs the frustum by extracting the view planes from a matrix.
	 * 
	 * @param value
	 *        Combined matrix which usually is (View * Projection).
	 */
	public BoundingFrustum(Matrix value)
	{
		this.matrix = value;
		this.createPlanes();
		this.createCorners();
	}

	/**
	 * Containment test between this <see cref="BoundingFrustum"/> and specified <see
	 * cref="BoundingBox"/>.
	 * 
	 * @param box
	 *        A <see cref="BoundingBox"/> for testing.
	 * @return Result of testing for containment between this <see cref="BoundingFrustum"/> and
	 *         specified <see cref="BoundingBox"/>.
	 */
	public ContainmentType contains(BoundingBox box)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType result = ContainmentType.Disjoint;
		this.contains(box, result);
		return result;
	}

	/**
	 * Containment test between this <see cref="BoundingFrustum"/> and specified <see
	 * cref="BoundingBox"/>.
	 * 
	 * @param box
	 *        A <see cref="BoundingBox"/> for testing.
	 * @param result
	 *        Result of testing for containment between this <see cref="BoundingFrustum"/> and
	 *        specified <see cref="BoundingBox"/> as an output parameter.
	 */
	public void contains(final BoundingBox box, ContainmentType result)
	{
		boolean intersects = false;
		for (int i = 0; i < PlaneCount; ++i)
		{
			// TODO: Should we initialize this to null ?
			PlaneIntersectionType planeIntersectionType = PlaneIntersectionType.Front;
			box.intersects(this.planes[i], planeIntersectionType);
			switch (planeIntersectionType)
			{
				case Front:
					result = ContainmentType.Disjoint;
					return;
				case Intersecting:
					intersects = true;
					break;
				case Back:
					break;
			}
		}
		result = intersects ? ContainmentType.Intersects : ContainmentType.Contains;
	}

	// / <summary>
	// / Containment test between this <see cref="BoundingFrustum"/> and specified <see
	// cref="BoundingFrustum"/>.
	// / </summary>
	// / <param name="frustum">A <see cref="BoundingFrustum"/> for testing.</param>
	// / <returns>Result of testing for containment between this <see cref="BoundingFrustum"/> and
	// specified <see cref="BoundingFrustum"/>.</returns>
	public ContainmentType contains(BoundingFrustum frustum)
	{
		if (this == frustum)                // We check to see if the two frustums are equal
			return ContainmentType.Contains;// If they are, there's no need to go any further.

		boolean intersects = false;
		for (int i = 0; i < PlaneCount; ++i)
		{
			// TODO: Should we initialize this to null ?
			PlaneIntersectionType planeIntersectionType = PlaneIntersectionType.Front;
			frustum.intersects(planes[i], planeIntersectionType);
			switch (planeIntersectionType)
			{
				case Front:
					return ContainmentType.Disjoint;
				case Intersecting:
					intersects = true;
					break;
				default:
					break;
			}
		}
		return intersects ? ContainmentType.Intersects : ContainmentType.Contains;
	}

	public ContainmentType contains(BoundingSphere sphere)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType result = ContainmentType.Disjoint;
		this.contains(sphere, result);
		return result;
	}

	public void contains(final BoundingSphere sphere, ContainmentType result)
	{
		boolean intersects = false;
		for (int i = 0; i < PlaneCount; ++i)
		{
			// TODO: Should we initialize this to null ?
			PlaneIntersectionType planeIntersectionType = PlaneIntersectionType.Front;

			// TODO: we might want to inline this for performance reasons
			sphere.intersects(this.planes[i], planeIntersectionType);
			switch (planeIntersectionType)
			{
				case Front:
					result = ContainmentType.Disjoint;
					return;
				case Intersecting:
					intersects = true;
					break;
				default:
					break;
			}
		}
		result = intersects ? ContainmentType.Intersects : ContainmentType.Contains;
	}

	public ContainmentType contains(Vector3 point)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType result = ContainmentType.Disjoint;
		this.contains(point, result);
		return result;
	}

	public void contains(final Vector3 point, ContainmentType result)
	{
		for (int i = 0; i < PlaneCount; ++i)
		{
			// TODO: we might want to inline this for performance reasons
			if (PlaneHelper.classifyPoint(point, this.planes[i]) > 0)
			{
				result = ContainmentType.Disjoint;
				return;
			}
		}
		result = ContainmentType.Contains;
	}

	public boolean equals(BoundingFrustum other)
	{
		if (other == null)
			return (this == null);

		return this.matrix.equals(other.matrix);
	}

	@Override
	public boolean equals(Object obj)
	{
		BoundingFrustum f = (BoundingFrustum) obj;
		return (f == null) ? false : (this.equals(f));
	}

	public boolean notEquals(BoundingFrustum b)
	{
		return !(this.equals(b));
	}

	public Vector3[] getCorners()
	{
		return (Vector3[]) this.corners.clone();
	}

	public void getCorners(Vector3[] corners)
	{
		if (corners == null)
			throw new NullPointerException("corners");
		if (corners.length < CornerCount)
			throw new IllegalArgumentException("corners");

		corners = Arrays.copyOf(this.corners, this.corners.length);
	}

	@Override
	public int hashCode()
	{
		return this.matrix.hashCode();
	}

	public boolean intersects(BoundingBox box)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType containment = ContainmentType.Disjoint;
		this.contains(box, containment);
		boolean result = containment != ContainmentType.Disjoint;
		return result;
	}

	// public void Intersects(final BoundingBox box, boolean result) {
	// ContainmentType containment = ContainmentType.Disjoint;
	// this.contains(box, containment);
	// result = containment != ContainmentType.Disjoint;
	// }

	/**
	 * Gets whether or not a specified <see cref="BoundingFrustum"/> intersects with this <see
	 * cref="BoundingFrustum"/>.
	 * 
	 * @param frustum
	 *        An other <see cref="BoundingFrustum"/> for intersection test.
	 * @return {@code true} if other <see cref="BoundingFrustum"/> intersects with this <see
	 *         cref="BoundingFrustum"/>; {@code false} otherwise.
	 */
	public boolean intersects(BoundingFrustum frustum)
	{
		return contains(frustum) != ContainmentType.Disjoint;
	}

	public boolean intersects(BoundingSphere sphere)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType containment = ContainmentType.Disjoint;
		this.contains(sphere, containment);
		boolean result = containment != ContainmentType.Disjoint;
		return result;
	}

	// public void intersects(final BoundingSphere sphere, boolean result) {
	// ContainmentType containment = ContainmentType.Disjoint;
	// this.contains(sphere, containment);
	// result = containment != ContainmentType.Disjoint;
	// }

	// / <summary>
	// / Gets type of intersection between specified <see cref="Plane"/> and this <see
	// cref="BoundingFrustum"/>.
	// / </summary>
	// / <param name="plane">A <see cref="Plane"/> for intersection test.</param>
	// / <returns>A plane intersection type.</returns>
	public PlaneIntersectionType intersects(Plane plane)
	{
		// TODO: Should we initialize this to null ?
		PlaneIntersectionType result = PlaneIntersectionType.Front;
		intersects(plane, result);
		return result;
	}

	// / <summary>
	// / Gets type of intersection between specified <see cref="Plane"/> and this <see
	// cref="BoundingFrustum"/>.
	// / </summary>
	// / <param name="plane">A <see cref="Plane"/> for intersection test.</param>
	// / <param name="result">A plane intersection type as an output parameter.</param>
	public void intersects(final Plane plane, PlaneIntersectionType result)
	{
		result = plane.intersects(corners[0]);
		for (int i = 1; i < corners.length; ++i)
			if (plane.intersects(corners[i]) != result)
				result = PlaneIntersectionType.Intersecting;
	}

	// / <summary>
	// / Gets the distance of intersection of <see cref="Ray"/> and this <see
	// cref="BoundingFrustum"/> or null if no intersection happens.
	// / </summary>
	// / <param name="ray">A <see cref="Ray"/> for intersection test.</param>
	// / <returns>Distance at which ray intersects with this <see cref="BoundingFrustum"/> or null
	// if no intersection happens.</returns>
	// TODO: These don't work in java unless I create my own wrapper
	public Float intersects(Ray ray)
	{
		Float result = null;
		intersects(ray, result);
		return result;
	}

	// / <summary>
	// / Gets the distance of intersection of <see cref="Ray"/> and this <see
	// cref="BoundingFrustum"/> or null if no intersection happens.
	// / </summary>
	// / <param name="ray">A <see cref="Ray"/> for intersection test.</param>
	// / <param name="result">Distance at which ray intersects with this <see
	// cref="BoundingFrustum"/> or null if no intersection happens as an output parameter.</param>
	public void intersects(final Ray ray, Float result)
	{
		// TODO: Should we initialize this to null ?
		ContainmentType ctype = ContainmentType.Disjoint;
		this.contains(ray.position, ctype);

		switch (ctype)
		{
			case Disjoint:
				result = null;
				return;
			case Contains:
				result = 0.0f;
				return;
			case Intersects:

				// TODO: Needs additional test for not 0.0 and null results.

				result = null;

				float min = Float.MIN_VALUE;
				float max = Float.MAX_VALUE;
				for (Plane plane : this.planes)
				{
					Vector3 normal = plane.normal;

					float result2 =	Vector3.dot(ray.direction, normal);

					float result3 = Vector3.dot(ray.position, normal);

					result3 += plane.D;

					if ((double) Math.abs(result2) < 9.99999974737875E-06)
					{
						if ((double) result3 > 0.0)
							return;
					}
					else
					{
						float result4 = -result3 / result2;
						if ((double) result2 < 0.0)
						{
							if ((double) result4 > (double) max)
								return;
							if ((double) result4 > (double) min)
								min = result4;
						}
						else
						{
							if ((double) result4 < (double) min)
								return;
							if ((double) result4 < (double) max)
								max = result4;
						}
					}

					Float distance = ray.intersects(plane);
					if (distance != null)
					{
						min = Math.min(min, distance);
						max = Math.max(max, distance);
					}
				}

				float temp = min >= 0.0 ? min : max;
				if (temp < 0.0)
				{
					return;
				}
				result = temp;

				return;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString()
	{
		return "{Near: " + this.planes[0] +
				" Far:" + this.planes[1] +
				" Left:" + this.planes[2] +
				" Right:" + this.planes[3] +
				" Top:" + this.planes[4] +
				" Bottom:" + this.planes[5] +
				"}";
	}

	private void createCorners()
	{
		intersectionPoint(this.planes[0], this.planes[2], this.planes[4], this.corners[0]);
		intersectionPoint(this.planes[0], this.planes[3], this.planes[4], this.corners[1]);
		intersectionPoint(this.planes[0], this.planes[3], this.planes[5], this.corners[2]);
		intersectionPoint(this.planes[0], this.planes[2], this.planes[5], this.corners[3]);
		intersectionPoint(this.planes[1], this.planes[2], this.planes[4], this.corners[4]);
		intersectionPoint(this.planes[1], this.planes[3], this.planes[4], this.corners[5]);
		intersectionPoint(this.planes[1], this.planes[3], this.planes[5], this.corners[6]);
		intersectionPoint(this.planes[1], this.planes[2], this.planes[5], this.corners[7]);
	}

	private void createPlanes()
	{
		this.planes[0] = new Plane(-this.matrix.M13, -this.matrix.M23, -this.matrix.M33, -this.matrix.M43);
		this.planes[1] = new Plane(this.matrix.M13 - this.matrix.M14, this.matrix.M23 - this.matrix.M24,
				this.matrix.M33 - this.matrix.M34, this.matrix.M43 - this.matrix.M44);
		this.planes[2] = new Plane(-this.matrix.M14 - this.matrix.M11, -this.matrix.M24 - this.matrix.M21,
				-this.matrix.M34 - this.matrix.M31, -this.matrix.M44 - this.matrix.M41);
		this.planes[3] = new Plane(this.matrix.M11 - this.matrix.M14, this.matrix.M21 - this.matrix.M24,
				this.matrix.M31 - this.matrix.M34, this.matrix.M41 - this.matrix.M44);
		this.planes[4] = new Plane(this.matrix.M12 - this.matrix.M14, this.matrix.M22 - this.matrix.M24,
				this.matrix.M32 - this.matrix.M34, this.matrix.M42 - this.matrix.M44);
		this.planes[5] = new Plane(-this.matrix.M14 - this.matrix.M12, -this.matrix.M24 - this.matrix.M22,
				-this.matrix.M34 - this.matrix.M32, -this.matrix.M44 - this.matrix.M42);

		this.normalizePlane(this.planes[0]);
		this.normalizePlane(this.planes[1]);
		this.normalizePlane(this.planes[2]);
		this.normalizePlane(this.planes[3]);
		this.normalizePlane(this.planes[4]);
		this.normalizePlane(this.planes[5]);
	}

	private static void intersectionPoint(final Plane a, final Plane b, final Plane c, Vector3 result)
	{
		// Formula used
        //                d1 ( N2 * N3 ) + d2 ( N3 * N1 ) + d3 ( N1 * N2 )
        //P =   -------------------------------------------------------------------------
        //                             N1 . ( N2 * N3 )
        //
        // Note: N refers to the normal, d refers to the displacement. '.' means dot product. '*' means cross product

		Vector3 v1 = new Vector3();
		Vector3 v2 = new Vector3();
		Vector3 v3 = new Vector3();
		Vector3 cross = new Vector3();

		Vector3.cross(b.normal, c.normal, cross);

		float f = Vector3.dot(a.normal, cross);
		f *= -1.0f;

		Vector3.cross(b.normal, c.normal, cross);
		Vector3.multiply(cross, a.D, v1);
		// v1 = (a.D * (Vector3.Cross(b.Normal, c.Normal)));

		Vector3.cross(c.normal, a.normal, cross);
		Vector3.multiply(cross, b.D, v2);
		// v2 = (b.D * (Vector3.Cross(c.Normal, a.Normal)));

		Vector3.cross(a.normal, b.normal, cross);
		Vector3.multiply(cross, c.D, v3);
		// v3 = (c.D * (Vector3.Cross(a.Normal, b.Normal)));

		result.x = (v1.x + v2.x + v3.x) / f;
		result.y = (v1.y + v2.y + v3.y) / f;
		result.z = (v1.z + v2.z + v3.z) / f;
	}

	private void normalizePlane(final Plane p)
	{
		float factor = 1f / p.normal.length();
		p.normal.x *= factor;
		p.normal.y *= factor;
		p.normal.z *= factor;
		p.D *= factor;
	}

}
