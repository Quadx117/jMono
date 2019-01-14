package jMono_Framework;

import jMono_Framework.math.Vector3;

// C# struct
public class Ray // implements IEquatable<Ray>
{
    // #region Public Fields

    public Vector3 direction;

    public Vector3 position;

    // #endregion

    // #region Public Constructors

    // Note: Added this since it is provided by default for struct in C#
    public Ray()
    {
        this.position = new Vector3();
        this.direction = new Vector3();
    }

    public Ray(Vector3 position, Vector3 direction)
    {
        this.position = position;
        this.direction = direction;
    }

    // #endregion

    // #region Public Methods

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
        return this.equals((Ray) obj);
    }

    /**
     * Indicates whether the specified {@link Ray} is "equal to" this one.
     * 
     * @param ohter
     *        The {@code Ray} with which to compare.
     * @return {@code true} if the specified {@code Ray} is the same as the current instance;
     *         {@code false} otherwise.
     */
    public boolean equals(Ray other)
    {
        return this.position.equals(other.position) && this.direction.equals(other.direction);
    }

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
     * @return {@code false} if this {@code Object} is the same as the obj argument; {@code true}
     *         otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
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
        // we expect the intersection distance to be 0 in that case
        if ((tMin != null && tMin < 0) && tMax > 0)
            return 0f;

        // a negative tMin means that the intersection point is behind the ray's origin
        // we discard these as not hitting the AABB
        if (tMin < 0)
            return null;

        return tMin;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final BoundingBox box, Float result)
    // {
    // result = intersects(box);
    // }

    // NOTE(Eric): Already commented out in the original code
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

    public Float intersects(BoundingSphere sphere)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class around the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // Float result;
        // intersects(sphere, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        Float result = null;

        // Find the vector between where the ray starts the the sphere's center
        Vector3 difference = Vector3.subtract(sphere.center, this.position);

        float differenceLengthSquared = difference.lengthSquared();
        float sphereRadiusSquared = sphere.radius * sphere.radius;

        float distanceAlongRay;

        // If the distance between the ray start and the sphere's center is less than the radius of
        // the sphere, it means we've intersected. N.B. checking the LengthSquared is faster.
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
        // if y = distance between ray position and sphere center
        // if z = the distance we've traveled along the ray
        // if x^2 + z^2 - y^2 < 0, we do not intersect
        float dist = sphereRadiusSquared + distanceAlongRay * distanceAlongRay - differenceLengthSquared;

        result = (dist < 0) ? null : distanceAlongRay - (float) Math.sqrt(dist);
        return result;
    }

    public Float intersects(Plane plane)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class around the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // Float result;
        // intersects(plane, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
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

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final Plane plane, Float result)
    // {
    // float den = Vector3.dot(direction, plane.normal);
    // if (Math.abs(den) < 0.00001f)
    // {
    // result = null;
    // return result;
    // }

    // result = (-plane.D - Vector3.dot(plane.normal, position)) / den;

    // if (result < 0.0f)
    // {
    // if (result < -0.00001f)
    // {
    // result = null;
    // return result;
    // }

    // result = 0.0f;
    // }
    // return result;
    // }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final BoundingSphere sphere, Float result)
    // {
    // Find the vector between where the ray starts the the sphere's center
    // Vector3 difference = new Vector3();
    // Vector3.subtract(sphere.center, this.position, difference);

    // float differenceLengthSquared = difference.lengthSquared();
    // float sphereRadiusSquared = sphere.radius * sphere.radius;

    // float distanceAlongRay;

    // If the distance between the ray start and the sphere's center is less than
    // the radius of the sphere, it means we've intersected. N.B. checking the LengthSquared is
    // faster.
    // if (differenceLengthSquared < sphereRadiusSquared)
    // {
    // result = 0.0f;
    // return result;
    // }

    // distanceAlongRay = Vector3.dot(this.direction, difference);
    // If the ray is pointing away from the sphere then we don't ever intersect
    // if (distanceAlongRay < 0)
    // {
    // result = null;
    // return result;
    // }

    // Next we kinda use Pythagoras to check if we are within the bounds of the sphere
    // if x = radius of sphere
    // if y = distance between ray position and sphere center
    // if z = the distance we've traveled along the ray
    // if x^2 + z^2 - y^2 < 0, we do not intersect
    // float dist = sphereRadiusSquared + distanceAlongRay * distanceAlongRay -
    // differenceLengthSquared;

    // result = (dist < 0) ? null : distanceAlongRay - (float) Math.sqrt(dist);
    // return result;
    // }

    protected String debugDisplayString()
    {
        return ("Pos( " + this.position.debugDisplayString() + " )  \r\n" +
                "Dir( " + this.direction.debugDisplayString() + " )");
    }

    @Override
    public String toString()
    {
        return "{{Position:" + position.toString() + " Direction:" + direction.toString() + "}}";
    }

    // #endregion
}
