package gameCore;

import gameCore.math.Matrix;
import gameCore.math.Vector3;

// C# struct
public class BoundingSphere
{

	/**
	 * The sphere center.
	 */
	public Vector3 center;

	/**
	 * The sphere radius.
	 */
	public float radius;

	protected String debugDisplayString()
	{
		return ("Pos( " + this.center.debugDisplayString() + " )  \r\n" +
				"Radius( " + this.radius + " )");
	}

	// Note: Added this since it is provided by default for struct in C#
	public BoundingSphere()
	{
		this.center = new Vector3();
		this.radius = 0.0f;
	}

	/**
	 * Constructs a bounding sphere with the specified center and radius.
	 * 
	 * @param center
	 *        The sphere center.
	 * @param radius
	 *        The sphere radius.
	 */
	public BoundingSphere(Vector3 center, float radius)
	{
		this.center = center;
		this.radius = radius;
	}

	public ContainmentType contains(BoundingBox box)
	{
		// check if all corner is in sphere
		boolean inside = true;
		for (Vector3 corner : box.getCorners())
		{
			if (this.contains(corner) == ContainmentType.Disjoint)
			{
				inside = false;
				break;
			}
		}

		if (inside)
			return ContainmentType.Contains;

		// check if the distance from sphere center to cube face < radius
		double dmin = 0;

		if (center.x < box.min.x)
			dmin += (center.x - box.min.x) * (center.x - box.min.x);

		else if (center.x > box.max.x)
			dmin += (center.x - box.max.x) * (center.x - box.max.x);

		if (center.y < box.min.y)
			dmin += (center.y - box.min.y) * (center.y - box.min.y);

		else if (center.y > box.max.y)
			dmin += (center.y - box.max.y) * (center.y - box.max.y);

		if (center.z < box.min.z)
			dmin += (center.z - box.min.z) * (center.z - box.min.z);

		else if (center.z > box.max.z)
			dmin += (center.z - box.max.z) * (center.z - box.max.z);

		if (dmin <= radius * radius)
			return ContainmentType.Intersects;

		// else disjoint
		return ContainmentType.Disjoint;
	}

	public void contains(final BoundingBox box, ContainmentType result)
	{
		result = this.contains(box);
	}

	public ContainmentType contains(BoundingFrustum frustum)
	{
		// check if all corner is in sphere
		boolean inside = true;

		Vector3[] corners = frustum.getCorners();
		for (Vector3 corner : corners)
		{
			if (this.contains(corner) == ContainmentType.Disjoint)
			{
				inside = false;
				break;
			}
		}
		if (inside)
			return ContainmentType.Contains;

		// check if the distance from sphere center to frustrum face < radius
		double dmin = 0;
		// TODO : calcul dmin

		if (dmin <= radius * radius)
			return ContainmentType.Intersects;

		// else disjoint
		return ContainmentType.Disjoint;
	}

	// / <summary>
	// / Test if a frustum is fully inside, outside, or just intersecting the sphere.
	// / </summary>
	// / <param name="frustum">The frustum for testing.</param>
	// / <param name="result">The containment type as an output parameter.</param>
	public void Contains(BoundingFrustum frustum, ContainmentType result)
	{
		result = this.contains(frustum);
	}

	public ContainmentType contains(BoundingSphere sphere)
	{
		// TODO : Should we initialize this to null ? (see other in same file)
		ContainmentType result = ContainmentType.Disjoint;
		contains(sphere, result);
		return result;
	}

	public void contains(final BoundingSphere sphere, ContainmentType result)
	{
		float sqDistance = Vector3.distanceSquared(sphere.center, center);

		if (sqDistance > (sphere.radius + radius) * (sphere.radius + radius))
			result = ContainmentType.Disjoint;

		else if (sqDistance <= (radius - sphere.radius) * (radius - sphere.radius))
			result = ContainmentType.Contains;

		else
			result = ContainmentType.Intersects;
	}

	public ContainmentType contains(Vector3 point)
	{
		ContainmentType result = ContainmentType.Disjoint;
		contains(point, result);
		return result;
	}

	public void contains(final Vector3 point, ContainmentType result)
	{
		float sqRadius = radius * radius;
		float sqDistance = Vector3.distanceSquared(point, center);

		if (sqDistance > sqRadius)
			result = ContainmentType.Disjoint;

		else if (sqDistance < sqRadius)
			result = ContainmentType.Contains;

		else
			result = ContainmentType.Intersects;
	}

	public static BoundingSphere createFromBoundingBox(BoundingBox box)
	{
		BoundingSphere result = new BoundingSphere();
		createFromBoundingBox(box, result);
		return result;
	}

	public static void createFromBoundingBox(final BoundingBox box, BoundingSphere result)
	{
		// Find the center of the box.
		Vector3 center = new Vector3((box.min.x + box.max.x) / 2.0f,
									 (box.min.y + box.max.y) / 2.0f,
									 (box.min.z + box.max.z) / 2.0f);

		// Find the distance between the center and one of the corners of the box.
		float radius = Vector3.distance(center, box.max);

		result = new BoundingSphere(center, radius);
	}

	public static BoundingSphere createFromFrustum(BoundingFrustum frustum)
	{
		return BoundingSphere.createFromPoints(frustum.getCorners());
	}

	// public static BoundingSphere createFromPoints(Iterable<Vector3> points)
	public static BoundingSphere createFromPoints(Vector3[] points)
	{
		if (points == null)
			throw new NullPointerException("points");

		// From "Real-Time Collision Detection" (Page 89)

		Vector3 minx = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3 maxx = Vector3.negate(minx);
		Vector3 miny = new Vector3(minx);
		Vector3 maxy = Vector3.negate(minx);
		Vector3 minz = new Vector3(minx);
		Vector3 maxz = Vector3.negate(minx);

		// Find the most extreme points along the principle axis.
		int numPoints = 0;
		for (Vector3 pt : points)
		{
			++numPoints;

			if (pt.x < minx.x)
				minx = pt;
			if (pt.x > maxx.x)
				maxx = pt;
			if (pt.y < miny.y)
				miny = pt;
			if (pt.y > maxy.y)
				maxy = pt;
			if (pt.z < minz.z)
				minz = pt;
			if (pt.z > maxz.z)
				maxz = pt;
		}

		if (numPoints == 0)
			throw new IllegalArgumentException("You should have at least one point in points.");

		float sqDistX = Vector3.distanceSquared(maxx, minx);
		float sqDistY = Vector3.distanceSquared(maxy, miny);
		float sqDistZ = Vector3.distanceSquared(maxz, minz);

		// Pick the pair of most distant points.
		Vector3 min = new Vector3(minx);
		Vector3 max = new Vector3(maxx);
		if (sqDistY > sqDistX && sqDistY > sqDistZ)
		{
			max = maxy;
			min = miny;
		}
		if (sqDistZ > sqDistX && sqDistZ > sqDistY)
		{
			max = maxz;
			min = minz;
		}

		Vector3 center = new Vector3();
		Vector3.add(min, max, center);
		Vector3.multiply(center, 0.5f);
		float radius = Vector3.distance(max, center);

		// Test every point and expand the sphere.
		// The current bounding sphere is just a good approximation and may not enclose all points.
		// From: Mathematics for 3D Game Programming and Computer Graphics, Eric Lengyel, Third
		// Edition.
		// Page 218
		float sqRadius = radius * radius;
		for (Vector3 pt : points)
		{
			Vector3 diff = new Vector3();
			Vector3.subtract(pt, center, diff);
			float sqDist = diff.lengthSquared();
			if (sqDist > sqRadius)
			{
				float distance = (float) Math.sqrt(sqDist); // equal to diff.Length();
				Vector3 direction = new Vector3();
				Vector3.divide(diff, distance, direction);

				Vector3 G = new Vector3();
				Vector3.multiply(direction, radius, G);
				Vector3.subtract(center, G, G);

				Vector3.add(G, pt, center);
				Vector3.divide(center, 2);

				radius = Vector3.distance(pt, center);
				sqRadius = radius * radius;
			}
		}

		return new BoundingSphere(center, radius);
	}

	public static BoundingSphere createMerged(BoundingSphere original, BoundingSphere additional)
	{
		BoundingSphere result = new BoundingSphere();
		createMerged(original, additional, result);
		return result;
	}

	public static void createMerged(final BoundingSphere original, final BoundingSphere additional,
			BoundingSphere result)
	{
		Vector3 ocenterToaCenter = new Vector3();
		Vector3.subtract(additional.center, original.center, ocenterToaCenter);

		float distance = ocenterToaCenter.length();
		if (distance <= original.radius + additional.radius)// intersect
		{
			if (distance <= original.radius - additional.radius)// original contain additional
			{
				result = original;
				return;
			}
			if (distance <= additional.radius - original.radius)// additional contain original
			{
				result = additional;
				return;
			}
		}
		// else find center of new sphere and radius
		float leftRadius = Math.max(original.radius - distance, additional.radius);
		float rightRadius = Math.max(original.radius + distance, additional.radius);

		Vector3 temp = new Vector3();
		Vector3.multiply(ocenterToaCenter, ((leftRadius - rightRadius) / (2 * ocenterToaCenter.length())), temp);
		Vector3.add(ocenterToaCenter, temp);
		// ocenterToaCenter = ocenterToaCenter + (((leftRadius - Rightradius) / (2 *
		// ocenterToaCenter.length())) * ocenterToaCenter);

		result = new BoundingSphere();
		Vector3.add(original.center, ocenterToaCenter, result.center);
		result.radius = (leftRadius + rightRadius) / 2;
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
		return this.equals((BoundingSphere) obj);
	}

	// Helper method
	private boolean equals(BoundingSphere other)
	{
		return this.center == other.center && this.radius == other.radius;
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
		return this.center.hashCode() + Float.hashCode(radius);
	}

	public boolean intersects(BoundingBox box)
	{
		return box.intersects(this);
	}

	// public void intersects(final BoundingBox box, boolean result) {
	// box.intersects(this, result);
	// }

	public boolean intersects(BoundingSphere sphere)
	{
		boolean result = false;

		float sqDistance = Vector3.distanceSquared(sphere.center, center);
		if (sqDistance > (sphere.radius + radius) * (sphere.radius + radius))
			result = false;
		else
			result = true;

		return result;
	}

	// public void intersects(final BoundingSphere sphere, boolean result) {
	// float sqDistance = Vector3.distanceSquared(sphere.Center, Center);
	//
	// if (sqDistance > (sphere.Radius + Radius) * (sphere.Radius + Radius))
	// result = false;
	// else
	// result = true;
	// }

	public PlaneIntersectionType intersects(Plane plane)
	{
		PlaneIntersectionType result = PlaneIntersectionType.Back;
		// TODO: we might want to inline this for performance reasons
		this.intersects(plane, result);
		return result;
	}

	public void intersects(final Plane plane, PlaneIntersectionType result)
	{
		// TODO: we might want to inline this for performance reasons
		float distance = Vector3.dot(plane.normal, this.center);
		distance += plane.D;
		if (distance > this.radius)
			result = PlaneIntersectionType.Front;
		else if (distance < -this.radius)
			result = PlaneIntersectionType.Back;
		else
			result = PlaneIntersectionType.Intersecting;
	}

	public Float intersects(Ray ray)
	{
		return ray.intersects(this);
	}

	// public void intersects(Ray ray, Float result) {
	// ray.intersects(this, result);
	// }

	@Override
	public String toString()
	{
		return ("{{Center:" + this.center.toString() + " Radius:" + this.radius + "}}");
	}
	
	public BoundingSphere transform(Matrix matrix)
	{
		BoundingSphere sphere = new BoundingSphere();
		sphere.center = Vector3.transform(this.center, matrix);
		sphere.radius = this.radius
				* ((float) Math.sqrt((double) Math.max(((matrix.M11 * matrix.M11) + (matrix.M12 * matrix.M12))
						+ (matrix.M13 * matrix.M13), Math.max(((matrix.M21 * matrix.M21) + (matrix.M22 * matrix.M22))
						+ (matrix.M23 * matrix.M23), ((matrix.M31 * matrix.M31) + (matrix.M32 * matrix.M32))
						+ (matrix.M33 * matrix.M33)))));
		return sphere;
	}

	public void transform(final Matrix matrix, BoundingSphere result)
	{
		result.center = Vector3.transform(this.center, matrix);
		result.radius = this.radius
				* ((float) Math.sqrt((double) Math.max(((matrix.M11 * matrix.M11) + (matrix.M12 * matrix.M12))
						+ (matrix.M13 * matrix.M13), Math.max(((matrix.M21 * matrix.M21) + (matrix.M22 * matrix.M22))
						+ (matrix.M23 * matrix.M23), ((matrix.M31 * matrix.M31) + (matrix.M32 * matrix.M32))
						+ (matrix.M33 * matrix.M33)))));
	}
	
}
