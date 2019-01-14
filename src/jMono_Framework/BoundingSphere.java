package jMono_Framework;

import jMono_Framework.math.Matrix;
import jMono_Framework.math.Vector3;

// C# struct
/**
 * Describes a sphere in 3D-space for bounding operations.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class BoundingSphere // implements IEquatable<BoundingSphere>
{
    // #region Public Fields

    /**
     * The sphere center.
     */
    public Vector3 center;

    /**
     * The sphere radius.
     */
    public float radius;

    // #endregion

    // #region Internal Properties

    protected String debugDisplayString()
    {
        return ("Center( " + this.center.debugDisplayString() + " )  \r\n" +
                "Radius( " + this.radius + " )");
    }

    // #endregion

    // #region Constructors

    // Note: Added this since it is provided by default for struct in C#
    /**
     * Constructs a bounding sphere with the center and radius set to 0.
     */
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

    // #endregion

    // #region Public Methods

    // #region Contains

    /**
     * Test if a bounding box is fully inside, outside, or just intersecting the sphere.
     * 
     * @param box
     *        The box for testing.
     * @return The containment type.
     */
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

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Test if a bounding box is fully inside, outside, or just intersecting the sphere.
     * 
     * @param box
     *        The box for testing.
     * @param result
     *        The containment type as an output parameter.
     */
    // public void contains(final BoundingBox box, ContainmentType result)
    // {
    // result = this.contains(box);
    // }

    /**
     * Test if a frustum is fully inside, outside, or just intersecting the sphere.
     * 
     * @param frustum
     *        The frustum for testing.
     * @return The containment type.
     */
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

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Test if a frustum is fully inside, outside, or just intersecting the sphere.
     * 
     * @param frustum
     *        The frustum for testing.
     * @param result
     *        The containment type as an output parameter.
     */
    // public void Contains(BoundingFrustum frustum, ContainmentType result)
    // {
    // result = this.contains(frustum);
    // }

    /**
     * Test if a sphere is fully inside, outside, or just intersecting the sphere.
     * 
     * @param sphere
     *        The other sphere for testing.
     * @return The containment type.
     */
    public ContainmentType contains(BoundingSphere sphere)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // contains(sphere, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        float sqDistance = Vector3.distanceSquared(sphere.center, center);

        if (sqDistance > (sphere.radius + radius) * (sphere.radius + radius))
            return ContainmentType.Disjoint;

        else if (sqDistance <= (radius - sphere.radius) * (radius - sphere.radius))
            return ContainmentType.Contains;

        else
            return ContainmentType.Intersects;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Test if a sphere is fully inside, outside, or just intersecting the sphere.
     * 
     * @param sphere
     *        The other sphere for testing.
     * @param result
     *        The containment type as an output parameter.
     */
    // public void contains(final BoundingSphere sphere, ContainmentType result)
    // {
    // float sqDistance = Vector3.distanceSquared(sphere.center, center);
    //
    // if (sqDistance > (sphere.radius + radius) * (sphere.radius + radius))
    // result = ContainmentType.Disjoint;
    //
    // else if (sqDistance <= (radius - sphere.radius) * (radius - sphere.radius))
    // result = ContainmentType.Contains;
    //
    // else
    // result = ContainmentType.Intersects;
    // }

    /**
     * Test if a point is fully inside, outside, or just intersecting the sphere.
     * 
     * @param point
     *        The vector in 3D-space for testing.
     * @return The containment type.
     */
    public ContainmentType contains(Vector3 point)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // contains(point, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        float sqRadius = radius * radius;
        float sqDistance = Vector3.distanceSquared(point, center);

        if (sqDistance > sqRadius)
            return ContainmentType.Disjoint;

        else if (sqDistance < sqRadius)
            return ContainmentType.Contains;

        else
            return ContainmentType.Intersects;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Test if a point is fully inside, outside, or just intersecting the sphere.
     * 
     * @param point
     *        The vector in 3D-space for testing.
     * @param result
     *        The containment type as an output parameter.
     */
    // public void contains(final Vector3 point, ContainmentType result)
    // {
    // float sqRadius = radius * radius;
    // float sqDistance = Vector3.distanceSquared(point, center);
    //
    // if (sqDistance > sqRadius)
    // result = ContainmentType.Disjoint;
    //
    // else if (sqDistance < sqRadius)
    // result = ContainmentType.Contains;
    //
    // else
    // result = ContainmentType.Intersects;
    // }

    // #endregion

    // #region CreateFromBoundingBox

    /**
     * Creates the smallest {@link BoundingSphere} that can contain a specified {@link BoundingBox}.
     * 
     * @param box
     *        The box to create the sphere from.
     * @return The new {@code BoundingSphere}.
     */
    public static BoundingSphere createFromBoundingBox(BoundingBox box)
    {
        BoundingSphere result = new BoundingSphere();
        createFromBoundingBox(box, result);
        return result;
    }

    /**
     * Creates the smallest {@link BoundingSphere} that can contain a specified {@link BoundingBox}.
     * 
     * @param box
     *        The box to create the sphere from.
     * @param result
     *        The new {@code BoundingSphere} as an output parameter.
     */
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

    // #endregion

    /**
     * Creates the smallest {@link BoundingSphere} that can contain a specified
     * {@link BoundingFrustum}.
     * 
     * @param frustum
     *        The frustum to create the sphere from.
     * @return The new {@code BoundingSphere}.
     */
    public static BoundingSphere createFromFrustum(BoundingFrustum frustum)
    {
        return BoundingSphere.createFromPoints(frustum.getCorners());
    }

    /**
     * Creates the smallest {@link BoundingSphere} that can contain a specified list of points in
     * 3D-space.
     * 
     * @param points
     *        List of point to create the sphere from.
     * @return The new {@code BoundingSphere}.
     */
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

        Vector3 center = Vector3.add(min, max).multiply(0.5f);
        float radius = Vector3.distance(max, center);

        // Test every point and expand the sphere.
        // The current bounding sphere is just a good approximation and may not enclose all points.
        // From: Mathematics for 3D Game Programming and Computer Graphics, Eric Lengyel, Third
        // Edition. Page 218
        float sqRadius = radius * radius;
        for (Vector3 pt : points)
        {
            Vector3 diff = Vector3.subtract(pt, center);
            float sqDist = diff.lengthSquared();
            if (sqDist > sqRadius)
            {
                float distance = (float) Math.sqrt(sqDist); // equal to diff.Length();
                Vector3 direction = Vector3.divide(diff, distance);
                Vector3 G = Vector3.subtract(center, Vector3.multiply(direction, radius));
                center = Vector3.divide(Vector3.add(G, pt), 2);
                radius = Vector3.distance(pt, center);
                sqRadius = radius * radius;
            }
        }

        return new BoundingSphere(center, radius);
    }

    /**
     * Creates the smallest {@link BoundingSphere} that can contain two spheres.
     * 
     * @param original
     *        First sphere.
     * @param additional
     *        Second sphere.
     * @return The new {@code BoundingSphere}.
     */
    public static BoundingSphere createMerged(BoundingSphere original, BoundingSphere additional)
    {
        BoundingSphere result = new BoundingSphere();
        createMerged(original, additional, result);
        return result;
    }

    /**
     * Creates the smallest {@link BoundingSphere} that can contain two spheres.
     * 
     * @param original
     *        First sphere.
     * @param additional
     *        Second sphere.
     * @param result
     *        The new {@code BoundingSphere} as an output parameter.
     */
    public static void createMerged(final BoundingSphere original, final BoundingSphere additional,
                                    BoundingSphere result)
    {
        Vector3 ocenterToaCenter = Vector3.subtract(additional.center, original.center);
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

        Vector3 temp = Vector3.multiply(ocenterToaCenter,
                                        ((leftRadius - rightRadius) / (2 * ocenterToaCenter.length())));
        ocenterToaCenter.add(temp);

        result = new BoundingSphere();
        result.center = Vector3.add(original.center, ocenterToaCenter);
        result.radius = (leftRadius + rightRadius) / 2;
    }

    /**
     * Indicates whether some other {@link Object} is "equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
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
        return this.equals((BoundingSphere) obj);
    }

    /**
     * Indicates whether some other {@link BoundingSphere} is "equal to" this one.
     * 
     * @param other
     *        The other {@code BoundingSphere} to test for equality.
     * @return {@code true} if this object is the same as the specified argument; {@code false}
     *         otherwise.
     */
    public boolean equals(BoundingSphere other)
    {
        return this.center.equals(other.center) && this.radius == other.radius;
    }

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
     * @return {@code false} if this object is the same as the specified argument; {@code true}
     *         otherwise.
     * @see BoundingSphere#equals(Object)
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

    // #region Intersects

    /**
     * Gets whether or not a specified {@link BoundingBox} intersects with this sphere.
     * 
     * @param box
     *        The box for testing.
     * @return {@code true} if {@code BoundingBox} intersects with this sphere; {@code false}
     *         otherwise.
     */
    public boolean intersects(BoundingBox box)
    {
        return box.intersects(this);
    }

    /**
     * Gets whether or not a specified {@link BoundingBox} intersects with this sphere.
     * 
     * @param box
     *        The box for testing.
     * @param result
     *        {@code true} if {@code BoundingBox} intersects with this sphere; {@code false}
     *        otherwise. As an output parameter.
     */
    // public void intersects(final BoundingBox box, boolean result)
    // {
    // box.intersects(this, result);
    // }

    /*
     * TODO : Make the public boolean intersects(BoundingFrustum frustum) overload
     * 
     * public boolean intersects(BoundingFrustum frustum)
     * {
     * if (frustum == null)
     * throw new NullReferenceException();
     * 
     * throw new NotImplementedException();
     * }
     */

    /**
     * Gets whether or not the other {@link BoundingSphere} intersects with this sphere.
     * 
     * @param sphere
     *        The other sphere for testing.
     * @return {@code true} if other {@code BoundingSphere} intersects with this sphere;
     *         {@code false} otherwise.
     */
    public boolean intersects(BoundingSphere sphere)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class around the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // boolean result;
        // intersects(sphere, result);
        // return result;

        boolean result = false;

        // NOTE(Eric): This code was copied from the next method.
        float sqDistance = Vector3.distanceSquared(sphere.center, center);
        if (sqDistance > (sphere.radius + radius) * (sphere.radius + radius))
            result = false;
        else
            result = true;

        return result;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not the other {@link BoundingSphere} intersects with this sphere.
     * 
     * @param sphere
     *        The other sphere for testing.
     * @param result
     *        {@code true} if other {@code BoundingSphere} intersects with this sphere;
     *        {@code false} otherwise. As an output parameter.
     */
    // public void intersects(final BoundingSphere sphere, boolean result)
    // {
    // float sqDistance = Vector3.distanceSquared(sphere.Center, Center);
    //
    // if (sqDistance > (sphere.Radius + Radius) * (sphere.Radius + Radius))
    // result = false;
    // else
    // result = true;
    // }

    /**
     * Gets whether or not a specified {@link Plane} intersects with this sphere.
     * 
     * @param plane
     *        The plane for testing.
     * @return Type of intersection.
     */
    public PlaneIntersectionType intersects(Plane plane)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // PlaneIntersectionType result = PlaneIntersectionType.Back;
        // TODO: we might want to inline this for performance reasons
        // this.intersects(plane, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        // TODO: we might want to inline this for performance reasons
        float distance = Vector3.dot(plane.normal, this.center);
        distance += plane.D;
        if (distance > this.radius)
            return PlaneIntersectionType.Front;
        else if (distance < -this.radius)
            return PlaneIntersectionType.Back;
        else
            return PlaneIntersectionType.Intersecting;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Gets whether or not a specified {@link Plane} intersects with this sphere.
     * 
     * @param plane
     *        The plane for testing.
     * @param result
     *        Type of intersection as an output parameter.
     */
    // public void intersects(final Plane plane, PlaneIntersectionType result)
    // {
    // // TODO: we might want to inline this for performance reasons
    // float distance = Vector3.dot(plane.normal, this.center);
    // distance += plane.D;
    // if (distance > this.radius)
    // result = PlaneIntersectionType.Front;
    // else if (distance < -this.radius)
    // result = PlaneIntersectionType.Back;
    // else
    // result = PlaneIntersectionType.Intersecting;
    // }

    /**
     * Gets whether or not a specified {@link Ray} intersects with this sphere.
     * 
     * @param ray
     *        The ray for testing.
     * @return Distance of ray intersection or {@code null} if there is no intersection.
     */
    public Float intersects(Ray ray)
    {
        return ray.intersects(this);
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not a specified {@link Ray} intersects with this sphere.
     * 
     * @param ray
     *        The ray for testing.
     * @param result
     *        Distance of ray intersection or {@code null} if there is no intersection as an output
     *        parameter.
     */
    // public void intersects(Ray ray, Float result)
    // {
    // ray.intersects(this, result);
    // }

    // #endregion

    /**
     * Returns a {@link String} representation of this {@link BoundingSphere} in the format:
     * {Center:{@link #center} Radius:{@link #radius}
     * 
     * @return A {@code String} representation of this {@code BoundingSphere}.
     */
    @Override
    public String toString()
    {
        return ("{{Center:" + this.center.toString() + " Radius:" + this.radius + "}}");
    }

    // #region Transform

    /**
     * Creates a new {@link BoundingSphere} that contains a transformation of translation and scale
     * from this sphere by the specified {@link Matrix}.
     * 
     * @param matrix
     *        The transformation {@code Matrix}.
     * @return Transformed {@code BoundingSphere}.
     */
    public BoundingSphere transform(Matrix matrix)
    {
        BoundingSphere sphere = new BoundingSphere();
        sphere.center = Vector3.transform(this.center, matrix);
        sphere.radius = this.radius
                        * ((float) Math.sqrt((double) Math.max(((matrix.m11 * matrix.m11) + (matrix.m12 * matrix.m12))
                                                               + (matrix.m13 * matrix.m13), Math.max(((matrix.m21 * matrix.m21) + (matrix.m22 * matrix.m22))
                                                                                                     + (matrix.m23 * matrix.m23), ((matrix.m31 * matrix.m31) + (matrix.m32 * matrix.m32))
                                                                                                                                  + (matrix.m33 * matrix.m33)))));
        return sphere;
    }

    /**
     * Creates a new {@link BoundingSphere} that contains a transformation of translation and scale
     * from this sphere by the specified {@link Matrix}.
     * 
     * @param matrix
     *        The transformation {@code Matrix}.
     * @param result
     *        Transformed {@code BoundingSphere} as an output parameter.
     */
    public void transform(final Matrix matrix, BoundingSphere result)
    {
        result.center = Vector3.transform(this.center, matrix);
        result.radius = this.radius
                        * ((float) Math.sqrt((double) Math.max(((matrix.m11 * matrix.m11) + (matrix.m12 * matrix.m12))
                                                               + (matrix.m13 * matrix.m13), Math.max(((matrix.m21 * matrix.m21) + (matrix.m22 * matrix.m22))
                                                                                                     + (matrix.m23 * matrix.m23), ((matrix.m31 * matrix.m31) + (matrix.m32 * matrix.m32))
                                                                                                                                  + (matrix.m33 * matrix.m33)))));
    }

    // #endregion

    // #endregion
}
