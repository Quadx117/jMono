package jMono_Framework;

import jMono_Framework.math.Vector3;

// C# struct
/**
 * Describes a box in 3D-space for bounding operations.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class BoundingBox // implements IEquatable<BoundingBox>
{
    // #region Public Fields

    public Vector3 min;

    public Vector3 max;

    public final int CORNER_COUNT = 8;

    // #endregion Public Fields

    // #region Public Constructors

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    public BoundingBox()
    {
        this.min = new Vector3();
        this.max = new Vector3();
    }

    public BoundingBox(Vector3 min, Vector3 max)
    {
        this.min = min;
        this.max = max;
    }

    // #endregion Public Constructors

    // #region Public Methods

    public ContainmentType contains(BoundingBox box)
    {
        // test if all corner is in the same side of a face by just checking min and max
        if (box.max.x < min.x || box.min.x > max.x ||
            box.max.y < min.y || box.min.y > max.y ||
            box.max.z < min.z || box.min.z > max.z)
        {
            return ContainmentType.Disjoint;
        }

        if (box.min.x >= min.x && box.max.x <= max.x &&
            box.min.y >= min.y && box.max.y <= max.y &&
            box.min.z >= min.z && box.max.z <= max.z)
        {
            return ContainmentType.Contains;
        }

        return ContainmentType.Intersects;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    // public void contains(final BoundingBox box, ContainmentType result)
    // {
    // result = contains(box);
    // }

    public ContainmentType contains(BoundingFrustum frustum)
    {
        // TODO: bad done here need a fix.
        // Because question is not frustum contain box but reverse and this is not the same
        int i;
        ContainmentType contained = ContainmentType.Disjoint;
        Vector3[] corners = frustum.getCorners();

        // First we check if frustum is in box
        for (i = 0; i < corners.length; ++i)
        {
            contained = this.contains(corners[i]);
            if (contained == ContainmentType.Disjoint)
                break;
        }

        // This means we checked all the corners and they were all contain or instersect
        if (i == corners.length)
            return ContainmentType.Contains;

        // if i is not equal to zero, we can fastpath and say that this box intersects
        if (i != 0)
            return ContainmentType.Intersects;

        // If we get here, it means the first (and only) point we checked was actually contained in
        // the frustum. So we assume that all other points will also be contained. If one of the
        // points is disjoint, we can exit immediately saying that the result is Intersects
        ++i;
        for (; i < corners.length; ++i)
        {
            contained = this.contains(corners[i]);
            if (contained != ContainmentType.Contains)
                return ContainmentType.Intersects;

        }

        // If we get here, then we know all the points were actually contained,
        // therefore result is Contains
        return ContainmentType.Contains;
    }

    public ContainmentType contains(BoundingSphere sphere)
    {
        if (sphere.center.x - min.x >= sphere.radius &&
            sphere.center.y - min.y >= sphere.radius &&
            sphere.center.z - min.z >= sphere.radius &&
            max.x - sphere.center.x >= sphere.radius &&
            max.y - sphere.center.y >= sphere.radius &&
            max.z - sphere.center.z >= sphere.radius)
            return ContainmentType.Contains;

        double dmin = 0;

        double e = sphere.center.x - min.x;
        if (e < 0)
        {
            if (e < -sphere.radius)
            {
                return ContainmentType.Disjoint;
            }
            dmin += e * e;
        }
        else
        {
            e = sphere.center.x - max.x;
            if (e > 0)
            {
                if (e > sphere.radius)
                {
                    return ContainmentType.Disjoint;
                }
                dmin += e * e;
            }
        }

        e = sphere.center.y - min.y;
        if (e < 0)
        {
            if (e < -sphere.radius)
            {
                return ContainmentType.Disjoint;
            }
            dmin += e * e;
        }
        else
        {
            e = sphere.center.y - max.y;
            if (e > 0)
            {
                if (e > sphere.radius)
                {
                    return ContainmentType.Disjoint;
                }
                dmin += e * e;
            }
        }

        e = sphere.center.z - min.z;
        if (e < 0)
        {
            if (e < -sphere.radius)
            {
                return ContainmentType.Disjoint;
            }
            dmin += e * e;
        }
        else
        {
            e = sphere.center.z - max.z;
            if (e > 0)
            {
                if (e > sphere.radius)
                {
                    return ContainmentType.Disjoint;
                }
                dmin += e * e;
            }
        }

        if (dmin <= sphere.radius * sphere.radius)
            return ContainmentType.Intersects;

        return ContainmentType.Disjoint;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    // public void contains(final BoundingSphere sphere, ContainmentType result)
    // {
    // result = this.contains(sphere);
    // }

    public ContainmentType contains(Vector3 point)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // this.contains(point, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        // first we check if point is out of box
        if (point.x < this.min.x ||
            point.x > this.max.x ||
            point.y < this.min.y ||
            point.y > this.max.y ||
            point.z < this.min.z ||
            point.z > this.max.z)
        {
            return ContainmentType.Disjoint;
        }
        else
        {
            return ContainmentType.Contains;
        }
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    // public void contains(final Vector3 point, ContainmentType result)
    // {
    // // first we check if point is out of box
    // if (point.x < this.min.x ||
    // point.x > this.max.x ||
    // point.y < this.min.y ||
    // point.y > this.max.y ||
    // point.z < this.min.z ||
    // point.z > this.max.z)
    // {
    // result = ContainmentType.Disjoint;
    // }
    // else
    // {
    // result = ContainmentType.Contains;
    // }
    // }

    private static final Vector3 MaxVector3 = new Vector3(Float.MAX_VALUE);
    private static final Vector3 MinVector3 = new Vector3(Float.MIN_VALUE);

    private static Vector3 MAX_VECTOR3()
    {
        return new Vector3(MaxVector3);
    }

    private static Vector3 MIN_VECTOR3()
    {
        return new Vector3(MinVector3);
    }

    /**
     * Create a bounding box from the given list of points.
     * 
     * @param points
     *        The list of Vector3 instances defining the point cloud to bound.
     * @return A bounding box that encapsulates the given point cloud.
     * @throws NullPointerException
     *         Thrown if the given list of points is {@code null}
     * @throws IllegalArgumentException
     *         Thrown if the given list has no points.
     */
    public static BoundingBox createFromPoints(Iterable<Vector3> points)
                                                                        throws NullPointerException, IllegalArgumentException
    {
        if (points == null)
            throw new NullPointerException();

        boolean empty = true;
        Vector3 minVec = MAX_VECTOR3();
        Vector3 maxVec = MIN_VECTOR3();
        for (Vector3 ptVector : points)
        {
            minVec.x = (minVec.x < ptVector.x) ? minVec.x : ptVector.x;
            minVec.y = (minVec.y < ptVector.y) ? minVec.y : ptVector.y;
            minVec.z = (minVec.z < ptVector.z) ? minVec.z : ptVector.z;

            maxVec.x = (maxVec.x > ptVector.x) ? maxVec.x : ptVector.x;
            maxVec.y = (maxVec.y > ptVector.y) ? maxVec.y : ptVector.y;
            maxVec.z = (maxVec.z > ptVector.z) ? maxVec.z : ptVector.z;

            empty = false;
        }
        if (empty)
            throw new IllegalArgumentException();

        return new BoundingBox(minVec, maxVec);
    }

    public static BoundingBox createFromSphere(BoundingSphere sphere)
    {
        BoundingBox result = new BoundingBox();
        createFromSphere(sphere, result);
        return result;
    }

    public static void createFromSphere(final BoundingSphere sphere, BoundingBox result)
    {
        Vector3 corner = new Vector3(sphere.radius);
        Vector3.subtract(sphere.center, corner, result.min);
        Vector3.add(sphere.center, corner, result.max);
    }

    public static BoundingBox createMerged(BoundingBox original, BoundingBox additional)
    {
        BoundingBox result = new BoundingBox();
        createMerged(original, additional, result);
        return result;
    }

    public static void createMerged(final BoundingBox original, final BoundingBox additional, BoundingBox result)
    {
        result.min.x = Math.min(original.min.x, additional.min.x);
        result.min.y = Math.min(original.min.y, additional.min.y);
        result.min.z = Math.min(original.min.z, additional.min.z);
        result.max.x = Math.max(original.max.x, additional.max.x);
        result.max.y = Math.max(original.max.y, additional.max.y);
        result.max.z = Math.max(original.max.z, additional.max.z);
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
        return this.equals((BoundingBox) obj);
    }

    public boolean equals(BoundingBox other)
    {
        return (this.min.equals(other.min)) && (this.max.equals(other.max));
    }

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        the reference {@code Object} with which to compare.
     * @return {@code false} if this object is the same as the specified argument; {@code true}
     *         otherwise.
     * @see BoundingBox#equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    public Vector3[] getCorners()
    {
        return new Vector3[] {
                              new Vector3(this.min.x, this.max.y, this.max.z),
                              new Vector3(this.max.x, this.max.y, this.max.z),
                              new Vector3(this.max.x, this.min.y, this.max.z),
                              new Vector3(this.min.x, this.min.y, this.max.z),
                              new Vector3(this.min.x, this.max.y, this.min.z),
                              new Vector3(this.max.x, this.max.y, this.min.z),
                              new Vector3(this.max.x, this.min.y, this.min.z),
                              new Vector3(this.min.x, this.min.y, this.min.z)
        };
    }

    public void getCorners(Vector3[] corners)
    {
        if (corners == null)
        {
            throw new NullPointerException("corners");
        }
        if (corners.length < 8)
        {
            throw new IllegalArgumentException("corners: Not Enought Corners");
        }
        corners[0].x = this.min.x;
        corners[0].y = this.max.y;
        corners[0].z = this.max.z;
        corners[1].x = this.max.x;
        corners[1].y = this.max.y;
        corners[1].z = this.max.z;
        corners[2].x = this.max.x;
        corners[2].y = this.min.y;
        corners[2].z = this.max.z;
        corners[3].x = this.min.x;
        corners[3].y = this.min.y;
        corners[3].z = this.max.z;
        corners[4].x = this.min.x;
        corners[4].y = this.max.y;
        corners[4].z = this.min.z;
        corners[5].x = this.max.x;
        corners[5].y = this.max.y;
        corners[5].z = this.min.z;
        corners[6].x = this.max.x;
        corners[6].y = this.min.y;
        corners[6].z = this.min.z;
        corners[7].x = this.min.x;
        corners[7].y = this.min.y;
        corners[7].z = this.min.z;
    }

    @Override
    public int hashCode()
    {
        return this.min.hashCode() + this.max.hashCode();
    }

    public boolean intersects(BoundingBox box)
    {
        boolean result = false;
        if ((this.max.x >= box.min.x) && (this.min.x <= box.max.x))
        {
            if ((this.max.y < box.min.y) || (this.min.y > box.max.y))
            {
                result = false;
                return result;
            }

            result = (this.max.z >= box.min.z) && (this.min.z <= box.max.z);
            return result;
        }

        result = false;
        return result;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final BoundingBox box, bool result)
    // {
    // if ((this.Max.x >= box.Min.x) && (this.Min.x <= box.Max.x))
    // {
    // if ((this.Max.y < box.Min.y) || (this.Min.y > box.Max.y))
    // {
    // result = false;
    // return;
    // }
    //
    // result = (this.Max.z >= box.Min.z) && (this.Min.z <= box.Max.z);
    // return;
    // }
    //
    // result = false;
    // return;
    // }

    public boolean intersects(BoundingFrustum frustum)
    {
        return frustum.intersects(this);
    }

    public boolean intersects(BoundingSphere sphere)
    {
        if (sphere.center.x - min.x > sphere.radius &&
            sphere.center.y - min.y > sphere.radius &&
            sphere.center.z - min.z > sphere.radius &&
            max.x - sphere.center.x > sphere.radius &&
            max.y - sphere.center.y > sphere.radius &&
            max.z - sphere.center.z > sphere.radius)
            return true;

        double dmin = 0;

        if (sphere.center.x - min.x <= sphere.radius)
            dmin += (sphere.center.x - min.x) * (sphere.center.x - min.x);
        else if (max.x - sphere.center.x <= sphere.radius)
            dmin += (sphere.center.x - max.x) * (sphere.center.x - max.x);

        if (sphere.center.y - min.y <= sphere.radius)
            dmin += (sphere.center.y - min.y) * (sphere.center.y - min.y);
        else if (max.y - sphere.center.y <= sphere.radius)
            dmin += (sphere.center.y - max.y) * (sphere.center.y - max.y);

        if (sphere.center.z - min.z <= sphere.radius)
            dmin += (sphere.center.z - min.z) * (sphere.center.z - min.z);
        else if (max.z - sphere.center.z <= sphere.radius)
            dmin += (sphere.center.z - max.z) * (sphere.center.z - max.z);

        if (dmin <= sphere.radius * sphere.radius)
            return true;

        return false;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final BoundingSphere sphere, boolean result)
    // {
    // result = intersects(sphere);
    // }

    public PlaneIntersectionType intersects(Plane plane)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // PlaneIntersectionType result = PlaneIntersectionType.Front;
        // intersects(plane, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        // See
        // http://zach.in.tu-clausthal.de/teaching/cg_literatur/lighthouse3d_view_frustum_culling/index.html

        Vector3 positiveVertex = new Vector3();
        Vector3 negativeVertex = new Vector3();

        if (plane.normal.x >= 0)
        {
            positiveVertex.x = max.x;
            negativeVertex.x = min.x;
        }
        else
        {
            positiveVertex.x = min.x;
            negativeVertex.x = max.x;
        }

        if (plane.normal.y >= 0)
        {
            positiveVertex.y = max.y;
            negativeVertex.y = min.y;
        }
        else
        {
            positiveVertex.y = min.y;
            negativeVertex.y = max.y;
        }

        if (plane.normal.z >= 0)
        {
            positiveVertex.z = max.z;
            negativeVertex.z = min.z;
        }
        else
        {
            positiveVertex.z = min.z;
            negativeVertex.z = max.z;
        }

        // Inline Vector3.dot(plane.normal, negativeVertex) + plane.D;
        float distance = plane.normal.x * negativeVertex.x + plane.normal.y * negativeVertex.y + plane.normal.z * negativeVertex.z + plane.D;
        if (distance > 0)
        {
            return PlaneIntersectionType.Front;
        }

        // Inline Vector3.dot(plane.normal, positiveVertex) + plane.D;
        distance = plane.normal.x * positiveVertex.x + plane.normal.y * positiveVertex.y + plane.normal.z * positiveVertex.z + plane.D;
        if (distance < 0)
        {
            return PlaneIntersectionType.Back;
        }

        return PlaneIntersectionType.Intersecting;

    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    // public void intersects(final Plane plane, PlaneIntersectionType result)
    // {
    // // See
    // //
    // http://zach.in.tu-clausthal.de/teaching/cg_literatur/lighthouse3d_view_frustum_culling/index.html
    //
    // Vector3 positiveVertex = new Vector3();
    // Vector3 negativeVertex = new Vector3();
    //
    // if (plane.normal.x >= 0)
    // {
    // positiveVertex.x = max.x;
    // negativeVertex.x = min.x;
    // }
    // else
    // {
    // positiveVertex.x = min.x;
    // negativeVertex.x = max.x;
    // }
    //
    // if (plane.normal.y >= 0)
    // {
    // positiveVertex.y = max.y;
    // negativeVertex.y = min.y;
    // }
    // else
    // {
    // positiveVertex.y = min.y;
    // negativeVertex.y = max.y;
    // }
    //
    // if (plane.normal.z >= 0)
    // {
    // positiveVertex.z = max.z;
    // negativeVertex.z = min.z;
    // }
    // else
    // {
    // positiveVertex.z = min.z;
    // negativeVertex.z = max.z;
    // }
    //
    // // Inline Vector3.dot(plane.normal, negativeVertex) + plane.D;
    // float distance = plane.normal.x * negativeVertex.x + plane.normal.y * negativeVertex.y +
    // plane.normal.z * negativeVertex.z + plane.D;
    // if (distance > 0)
    // {
    // result = PlaneIntersectionType.Front;
    // return;
    // }
    //
    // // Inline Vector3.dot(plane.normal, positiveVertex) + plane.D;
    // distance = plane.normal.x * positiveVertex.x + plane.normal.y * positiveVertex.y +
    // plane.normal.z * positiveVertex.z + plane.D;
    // if (distance < 0)
    // {
    // result = PlaneIntersectionType.Back;
    // return;
    // }
    //
    // result = PlaneIntersectionType.Intersecting;
    // }

    public Float intersects(Ray ray)
    {
        return ray.intersects(this);
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    // public void intersects(final Ray ray, Float result)
    // {
    // result = intersects(ray);
    // }

    protected String debugDisplayString()
    {
        return ("Min( " + this.min.debugDisplayString() + " )  \r\n" +
                "Max( " + this.max.debugDisplayString() + " )");
    }

    @Override
    public String toString()
    {
        return "{{Min:" + this.min.toString() + " Max:" + this.max.toString() + "}}";
    }

    // #endregion Public Methods
}
