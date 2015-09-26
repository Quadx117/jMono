package gameCore;

import gameCore.math.Vector3;

public class Ray // implements IEquatable<Ray>
{

	public Vector3 direction;

	public Vector3 position;

	public Ray(Vector3 position, Vector3 direction)
	{
		this.position = position;
		this.direction = direction;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Ray) ? this.equals((Ray) obj) : false;
	}

	public boolean equals(Ray other)
	{
		return this.position.equals(other.position) && this.direction.equals(other.direction);
	}
	
	public static boolean equals(Ray a, Ray b)
	{
		return a.equals(b);
	}

	public static boolean notEquals(Ray a, Ray b)
	{
		return !a.equals(b);
	}
	
	@Override
	public int hashCode()
	{
		return position.hashCode() ^ direction.hashCode();
	}

	// adapted from
	// http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-box-intersection/
	public Float intersects(BoundingBox box)
	{
		final float epsilon = 1e-6f;

		Float tMin = null;
		Float tMax = null;

		if (Math.abs(direction.x) < epsilon)
		{
			if (position.x < box.min.x || position.x > box.max.x)
				return null;
		}
		else
		{
			tMin = (box.min.x - position.x) / direction.x;
			tMax = (box.max.x - position.x) / direction.x;

			if (tMin > tMax)
			{
				Float temp = tMin;
				tMin = tMax;
				tMax = temp;
			}
		}

		if (Math.abs(direction.y) < epsilon)
		{
			if (position.y < box.min.y || position.y > box.max.y)
				return null;
		}
		else
		{
			Float tMinY = (box.min.y - position.y) / direction.y;
			Float tMaxY = (box.max.y - position.y) / direction.y;

			if (tMinY > tMaxY)
			{
				Float temp = tMinY;
				tMinY = tMaxY;
				tMaxY = temp;
			}

			if ((tMin != null && tMin > tMaxY) || (tMax != null && tMinY > tMax))
				return null;

			if (tMin == null || tMinY > tMin)
				tMin = tMinY;
			if (tMax == null || tMaxY < tMax)
				tMax = tMaxY;
		}

		if (Math.abs(direction.z) < epsilon)
		{
			if (position.z < box.min.z || position.z > box.max.z)
				return null;
		}
		else
		{
			Float tMinZ = (box.min.z - position.z) / direction.z;
			Float tMaxZ = (box.max.z - position.z) / direction.z;

			if (tMinZ > tMaxZ)
			{
				Float temp = tMinZ;
				tMinZ = tMaxZ;
				tMaxZ = temp;
			}

			if ((tMin != null && tMin > tMaxZ) || (tMax != null && tMinZ > tMax))
				return null;

			if (tMin == null || tMinZ > tMin)
				tMin = tMinZ;
			if (tMax == null || tMaxZ < tMax)
				tMax = tMaxZ;
		}

		// having a positive tMin and a negative tMax means the ray is inside the box
		// we expect the intesection distance to be 0 in that case
		if ((tMin != null && tMin < 0) && tMax > 0)
			return 0f;

		// a negative tMin means that the intersection point is behind the ray's origin
		// we discard these as not hitting the AABB
		if (tMin < 0)
			return null;

		return tMin;
	}

	public void intersects(final BoundingBox box, Float result)
	{
		result = intersects(box);
	}

	// NOTE: Already commented out in the original code
	/*
	 * public float? Intersects(BoundingFrustum frustum)
	 * {
	 * if (frustum == null)
	 * {
	 * throw new ArgumentNullException("frustum");
	 * }
	 * 
	 * return frustum.Intersects(this);
	 * }
	 */

	// TODO: Validate but if I remember correctly it doesn't work with Java's wrapper.
	// Need to make my own classes instead if I want this functionality.
	/*
	 * public Float Intersects(BoundingSphere sphere) {
	 * Float result;
	 * intersects(sphere, result);
	 * return result;
	 * }
	 * 
	 * public Float intersects(Plane plane) {
	 * Float result;
	 * intersects(plane, result);
	 * return result;
	 * }
	 */

	// public void Intersects(ref Plane plane, out float? result)
	public Float intersects(final Plane plane)
	{
		Float result = null;

		float den = Vector3.dot(direction, plane.normal);
		if (Math.abs(den) < 0.00001f)
		{
			result = null;
			return result;
		}

		result = (-plane.D - Vector3.dot(plane.normal, position)) / den;

		if (result < 0.0f)
		{
			if (result < -0.00001f)
			{
				result = null;
				return result;
			}

			result = 0.0f;
		}
		return result;
	}

	public Float intersects(final BoundingSphere sphere)
	{
		Float result = null;

		// Find the vector between where the ray starts the the sphere's centre
		Vector3 difference = new Vector3();
		Vector3.subtract(sphere.center, this.position, difference);

		float differenceLengthSquared = difference.lengthSquared();
		float sphereRadiusSquared = sphere.radius * sphere.radius;

		float distanceAlongRay;

		// If the distance between the ray start and the sphere's centre is less than
		// the radius of the sphere, it means we've intersected. N.B. checking the LengthSquared is
		// faster.
		if (differenceLengthSquared < sphereRadiusSquared)
		{
			result = 0.0f;
			return result;
		}

		distanceAlongRay = Vector3.dot(this.direction, difference);
		// If the ray is pointing away from the sphere then we don't ever intersect
		if (distanceAlongRay < 0)
		{
			result = null;
			return result;
		}

		// Next we kinda use Pythagoras to check if we are within the bounds of the sphere
		// if x = radius of sphere
		// if y = distance between ray position and sphere centre
		// if z = the distance we've travelled along the ray
		// if x^2 + z^2 - y^2 < 0, we do not intersect
		float dist = sphereRadiusSquared + distanceAlongRay * distanceAlongRay - differenceLengthSquared;

		result = (dist < 0) ? null : distanceAlongRay - (float) Math.sqrt(dist);
		return result;
	}

	protected String debugDisplayString()
	{
		return ("Pos( " + this.position.debugDisplayString() + " )  \r\n" +	//
				"Dir( " + this.direction.debugDisplayString() + " )");
	}

	@Override
	public String toString()
	{
		return "{{Position:" + position.toString() + " Direction:" + direction.toString() + "}}";
	}

}
