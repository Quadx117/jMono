package jMono_Framework;

import jMono_Framework.math.Matrix;
import jMono_Framework.math.Vector3;

import java.util.Arrays;

/**
 * Defines a viewing frustum for intersection operations.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class BoundingFrustum // implements IEquatable<BoundingFrustum>
{
    // #region Public Fields

    /**
     * The number of planes in the frustum.
     */
    public final int PlaneCount = 6;

    /**
     * The number of corner points in the frustum.
     */
    public final int CornerCount = 8;

    // #endregion

    // #region Private Fields

    private Matrix matrix;
    private Vector3[] corners = new Vector3[CornerCount];
    private Plane[] planes = new Plane[PlaneCount];

    // #endregion

    // #region Properties

    /**
     * Returns the {@link Matrix} of the frustum.
     * 
     * @return The {@code Matrix} of the frustum.
     */
    public Matrix getMatrix()
    {
        return this.matrix;
    }

    /**
     * Sets the {@link Matrix} of the frustum to the specified value.
     * 
     * @param value
     *        The new value for this {@code Matrix}
     */
    public void setMatrix(Matrix value)
    {
        this.matrix = value;
        this.createPlanes();	// FIXME: The odds are the planes will be used a lot more often than
        this.createCorners();   // the matrix is updated, so this should help performance. I hope ;)
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

    // #endregion

    // #region Internal Properties

    protected String debugDisplayString()
    {
        return ("Near( " + this.planes[0].debugDisplayString() + " )  \r\n" +
                "Far( " + this.planes[1].debugDisplayString() + " )  \r\n" +
                "Left( " + this.planes[2].debugDisplayString() + " )  \r\n" +
                "Right( " + this.planes[3].debugDisplayString() + " )  \r\n" +
                "Top( " + this.planes[4].debugDisplayString() + " )  \r\n" +
                "Bottom( " + this.planes[5].debugDisplayString() + " )  ");
    }

    // #endregion

    // #region Constructors

    /**
     * Constructs the frustum by extracting the view planes from a {@link Matrix}.
     * 
     * @param value
     *        Combined {@code Matrix} which usually is (View * Projection).
     */
    public BoundingFrustum(Matrix value)
    {
        this.matrix = value;
        this.createPlanes();
        this.createCorners();
    }

    // #endregion

    // #region Public Methods

    // #region Contains

    /**
     * Containment test between this {@link BoundingFrustum} and the specified {@link BoundingBox}.
     * 
     * @param box
     *        A {@code BoundingBox} for testing.
     * @return Result of testing for containment between this {@code BoundingFrustum} and
     *         the specified {@code BoundingBox}.
     */
    public ContainmentType contains(BoundingBox box)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // this.contains(box, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        boolean intersects = false;
        for (int i = 0; i < PlaneCount; ++i)
        {
            PlaneIntersectionType planeIntersectionType = box.intersects(this.planes[i]);
            switch (planeIntersectionType)
            {
                case Front:
                    return ContainmentType.Disjoint;
                case Intersecting:
                    intersects = true;
                    break;
                case Back:
                    break;
            }
        }
        return intersects ? ContainmentType.Intersects : ContainmentType.Contains;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Containment test between this {@link BoundingFrustum} and the specified {@link BoundingBox}.
     * 
     * @param box
     *        A {@code BoundingBox} for testing.
     * @param result
     *        Result of testing for containment between this {@code BoundingFrustum} and
     *        the specified {@code BoundingBox} as an output parameter.
     */
    // public void contains(final BoundingBox box, ContainmentType result)
    // {
    // boolean intersects = false;
    // for (int i = 0; i < PlaneCount; ++i)
    // {
    // PlaneIntersectionType planeIntersectionType = PlaneIntersectionType.Front;
    // box.intersects(this.planes[i], planeIntersectionType);
    // switch (planeIntersectionType)
    // {
    // case Front:
    // result = ContainmentType.Disjoint;
    // return;
    // case Intersecting:
    // intersects = true;
    // break;
    // case Back:
    // break;
    // }
    // }
    // result = intersects ? ContainmentType.Intersects : ContainmentType.Contains;
    // }

    /**
     * Containment test between this {@link BoundingFrustum} and the specified
     * {@link BoundingFrustum}.
     * 
     * @param frustum
     *        A {@code BoundingFrustum} for testing.
     * @return Result of testing for containment between this {@code BoundingFrustum} and
     *         the specified {@code BoundingFrustum}.
     */
    public ContainmentType contains(BoundingFrustum frustum)
    {
        if (this == frustum)                // We check to see if the two frustums are equal
            return ContainmentType.Contains;// If they are, there's no need to go any further.

        boolean intersects = false;
        for (int i = 0; i < PlaneCount; ++i)
        {
            PlaneIntersectionType planeIntersectionType = frustum.intersects(planes[i]);
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

    /**
     * Containment test between this {@link BoundingFrustum} and the specified
     * {@link BoundingSphere}.
     * 
     * @param sphere
     *        A {@code BoundingSphere} for testing.
     * @return Result of testing for containment between this {@code BoundingFrustum} and
     *         the specified {@code BoundingSphere}.
     */
    public ContainmentType contains(BoundingSphere sphere)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // this.contains(sphere, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        boolean intersects = false;
        for (int i = 0; i < PlaneCount; ++i)
        {
            // TODO: we might want to inline this for performance reasons
            PlaneIntersectionType planeIntersectionType = sphere.intersects(this.planes[i]);
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

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Containment test between this {@link BoundingFrustum} and the specified
     * {@link BoundingSphere}.
     * 
     * @param sphere
     *        A {@code BoundingSphere} for testing.
     * @param result
     *        Result of testing for containment between this {@code BoundingFrustum} and
     *        the specified {@code BoundingSphere} as an output parameter.
     */
    // public void contains(final BoundingSphere sphere, ContainmentType result)
    // {
    // boolean intersects = false;
    // for (int i = 0; i < PlaneCount; ++i)
    // {
    // PlaneIntersectionType planeIntersectionType = PlaneIntersectionType.Front;
    //
    // // TODO: we might want to inline this for performance reasons
    // sphere.intersects(this.planes[i], planeIntersectionType);
    // switch (planeIntersectionType)
    // {
    // case Front:
    // result = ContainmentType.Disjoint;
    // return;
    // case Intersecting:
    // intersects = true;
    // break;
    // default:
    // break;
    // }
    // }
    // result = intersects ? ContainmentType.Intersects : ContainmentType.Contains;
    // }

    /**
     * Containment test between this {@link BoundingFrustum} and the specified {@link Vector3}.
     * 
     * @param point
     *        A {@code Vector3} for testing.
     * @return Result of testing for containment between this {@code BoundingFrustum} and
     *         the specified {@code Vector3}.
     */
    public ContainmentType contains(Vector3 point)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // ContainmentType result = ContainmentType.Disjoint;
        // this.contains(point, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        for (int i = 0; i < PlaneCount; ++i)
        {
            // TODO: we might want to inline this for performance reasons
            if (PlaneHelper.classifyPoint(point, this.planes[i]) > 0)
            {
                return ContainmentType.Disjoint;
            }
        }
        return ContainmentType.Contains;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Containment test between this {@link BoundingFrustum} and the specified {@link Vector3}.
     * 
     * @param point
     *        A {@code Vector3} for testing.
     * @param result
     *        Result of testing for containment between this {@code BoundingFrustum} and
     *        the specified {@code Vector3} as an output parameter.
     */
    // public void contains(final Vector3 point, ContainmentType result)
    // {
    // for (int i = 0; i < PlaneCount; ++i)
    // {
    // // TODO: we might want to inline this for performance reasons
    // if (PlaneHelper.classifyPoint(point, this.planes[i]) > 0)
    // {
    // result = ContainmentType.Disjoint;
    // return;
    // }
    // }
    // result = ContainmentType.Contains;
    // }

    // #endregion

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
        return this.equals((BoundingFrustum) obj);
    }

    /**
     * Indicates whether current instance is equal to the specified {@link BoundingFrustum}.
     * 
     * @param other
     *        The {@code BoundingFrustum} to compare with.
     * @return {@code true} if the instances are equal; {@code false} otherwise.
     */
    public boolean equals(BoundingFrustum other)
    {
        return this.matrix.equals(other.matrix);
    }

    /**
     * Indicates whether some other {@code Object} is "not equal to" this one.
     * 
     * @param obj
     *        The reference {@code Object} with which to compare.
     * @return {@code false} if this object is the same as the specified argument;< {@code true}
     *         otherwise.
     * @see BoundingFrustum#equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    /**
     * Returns a copy of the internal corners array.
     * 
     * @return The array of corners.
     */
    public Vector3[] getCorners()
    {
        return (Vector3[]) this.corners.clone();
    }

    /**
     * Returns a copy of the internal corners array.
     * 
     * @param corners
     *        The array which values will be replaced to corner values of this instance.
     *        It must have size of {@link BoundingFrustum#CornerCount}.
     */
    public void getCorners(Vector3[] corners)
    {
        if (corners == null)
            throw new NullPointerException("corners");
        if (corners.length < CornerCount)
            throw new IllegalArgumentException("corners");

        corners = Arrays.copyOf(this.corners, this.corners.length);
    }

    /**
     * Gets the hash code of this {@link BoundingFrustum}.
     * 
     * @return Hash code of this {@code BoundingFrustum}.
     */
    @Override
    public int hashCode()
    {
        return this.matrix.hashCode();
    }

    /**
     * Gets whether or not a specified {@link BoundingBox} intersects with this
     * {@link BoundingFrustum}.
     * 
     * @param box
     *        A {@code BoundingBox} for intersection test.
     * @return {@code true} if specified {@code BoundingBox} intersects with
     *         this {@link BoundingFrustum}; {@code false} otherwise.
     */
    public boolean intersects(BoundingBox box)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class aroud the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // boolean result = false;
        // this.intersects(box, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        ContainmentType containment = this.contains(box);
        return containment != ContainmentType.Disjoint;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not a specified {@link BoundingBox} intersects with this
     * {@link BoundingFrustum}.
     * 
     * @param box
     *        A {@link BoundingBox} for intersection test.
     * @param result
     *        {@code true} if specified {@link BoundingBox} intersects with this
     *        {@link BoundingFrustum}; {@code false} otherwise as an output parameter.
     */
    // public void intersects(final BoundingBox box, boolean result)
    // {
    // ContainmentType containment = ContainmentType.Disjoint;
    // this.contains(box, containment);
    // result = containment != ContainmentType.Disjoint;
    // }

    /**
     * Gets whether or not a specified {@link BoundingFrustum} intersects with this
     * {@link BoundingFrustum}.
     * 
     * @param frustum
     *        An other {@code BoundingFrustum} for intersection test.
     * @return {@code true} if other {@code BoundingFrustum} intersects with this
     *         {@code BoundingFrustum}; {@code false} otherwise.
     */
    public boolean intersects(BoundingFrustum frustum)
    {
        return contains(frustum) != ContainmentType.Disjoint;
    }

    /**
     * Gets whether or not a specified {@link BoundingSphere} intersects with this
     * {@link BoundingFrustum}.
     * 
     * @param sphere
     *        A {@code BoundingSphere} for intersection test.
     * @return {@code true} if specified {@code BoundingSphere} intersects with this
     *         {@code BoundingFrustum}; {@code false} otherwise.
     */
    public boolean intersects(BoundingSphere sphere)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class aroud the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // boolean result = false;
        // this.intersects(sphere, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        ContainmentType containment = this.contains(sphere);
        return containment != ContainmentType.Disjoint;
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class around the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets whether or not a specified {@link BoundingSphere} intersects with this
     * {@link BoundingFrustum}.
     * 
     * @param sphere
     *        A {@link BoundingSphere} for intersection test.
     * @param result
     *        {@code true} if specified {@link BoundingSphere} intersects with this
     *        {@link BoundingFrustum}; {@code false} otherwise as an output parameter.
     */
    // public void intersects(final BoundingSphere sphere, boolean result)
    // {
    // ContainmentType containment = ContainmentType.Disjoint;
    // this.contains(sphere, containment);
    // result = containment != ContainmentType.Disjoint;
    // }

    /**
     * Gets type of intersection between specified {@link Plane} and this {@link BoundingFrustum}.
     * 
     * @param plane
     *        A {@link Plane} for intersection test.
     * @return A plane intersection type.
     */
    public PlaneIntersectionType intersects(Plane plane)
    {
        // NOTE(Eric): This does not work in java since assigning a new enum constant
        // changes the reference of the object. Could implement a Wrapper to get around
        // this if we really wanted this behavior.
        // PlaneIntersectionType result = PlaneIntersectionType.Front;
        // intersects(plane, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        PlaneIntersectionType result = plane.intersects(corners[0]);
        for (int i = 1; i < corners.length; ++i)
            if (plane.intersects(corners[i]) != result)
                result = PlaneIntersectionType.Intersecting;

        return result;
    }

    // NOTE(Eric): This does not work in java since assigning a new enum constant
    // changes the reference of the object. Could implement a Wrapper to get around
    // this if we really wanted this behavior.
    /**
     * Gets type of intersection between specified {@link Plane} and this {@link BoundingFrustum}.
     * 
     * @param plane
     *        A {@link Plane} for intersection test.
     * @param result
     *        A plane intersection type as an output parameter.
     */
    // public void intersects(final Plane plane, PlaneIntersectionType result)
    // {
    // result = plane.intersects(corners[0]);
    // for (int i = 1; i < corners.length; ++i)
    // if (plane.intersects(corners[i]) != result)
    // result = PlaneIntersectionType.Intersecting;
    // }

    /**
     * Gets the distance of intersection of {@link Ray} and this {@link BoundingFrustum} or
     * {@code null} if no intersection happens.
     * 
     * @param ray
     *        A {@code Ray} for intersection test.
     * @return Distance at which ray intersects with this {@code BoundingFrustum} or {@code null} if
     *         no intersection happens.
     */
    public Float intersects(Ray ray)
    {
        // TODO(Eric): Original code with out parameters on primitive date types.
        // This could be emulated in Java using a Wrapper class aroud the primitive
        // see :
        // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
        // Float result = null;
        // intersects(ray, result);
        // return result;

        // NOTE(Eric): This code was copied from the next method.
        ContainmentType ctype = this.contains(ray.position);

        switch (ctype)
        {
            case Disjoint:
                return null;
            case Contains:
                return 0.0f;
            case Intersects:
                throw new UnsupportedOperationException();
            default:
                throw new IllegalArgumentException();
        }
    }

    // TODO(Eric): Original code with out parameters on primitive date types.
    // This could be emulated in Java using a Wrapper class aroud the primitive
    // see :
    // http://stackoverflow.com/questions/4319537/how-do-i-pass-a-primitive-data-type-by-reference/4319581#4319581
    /**
     * Gets the distance of intersection of this {@link BoundingFrustum} and the
     * specified {@link Ray} or {@code null} if no intersection happens.
     * 
     * @param ray
     *        A {@link Ray} for intersection test.
     * @param result
     *        Distance at which the specified ray intersects with this {@link BoundingFrustum} or
     *        {@code null} if no intersection happens as an output parameter.
     */
    // public void intersects(final Ray ray, Float result)
    // {
    // ContainmentType ctype = ContainmentType.Disjoint;
    // this.contains(ray.position, ctype);
    //
    // switch (ctype)
    // {
    // case Disjoint:
    // result = null;
    // return;
    // case Contains:
    // result = 0.0f;
    // return;
    // case Intersects:
    // throw new UnsupportedOperationException();
    // default:
    // throw new IllegalArgumentException();
    // }
    // }

    /**
     * Returns a {@link String} representation of this {@link BoundingFrustum} in the format:
     * {Near:[nearPlane] Far:[farPlane] Left:[leftPlane] Right:[rightPlane] Top:[topPlane]
     * Bottom:[bottomPlane]}
     * 
     * @return {@code String} representation of this {@code BoundingFrustum}.
     */
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

    // #endregion

    // #region Private Methods

    private void createCorners()
    {

        intersectionPoint(this.planes[0], this.planes[2], this.planes[4], this.corners[0] = new Vector3());
        intersectionPoint(this.planes[0], this.planes[3], this.planes[4], this.corners[1] = new Vector3());
        intersectionPoint(this.planes[0], this.planes[3], this.planes[5], this.corners[2] = new Vector3());
        intersectionPoint(this.planes[0], this.planes[2], this.planes[5], this.corners[3] = new Vector3());
        intersectionPoint(this.planes[1], this.planes[2], this.planes[4], this.corners[4] = new Vector3());
        intersectionPoint(this.planes[1], this.planes[3], this.planes[4], this.corners[5] = new Vector3());
        intersectionPoint(this.planes[1], this.planes[3], this.planes[5], this.corners[6] = new Vector3());
        intersectionPoint(this.planes[1], this.planes[2], this.planes[5], this.corners[7] = new Vector3());
    }

    private void createPlanes()
    {
        this.planes[0] = new Plane(-this.matrix.m13, -this.matrix.m23, -this.matrix.m33, -this.matrix.m43);
        this.planes[1] = new Plane(this.matrix.m13 - this.matrix.m14, this.matrix.m23 - this.matrix.m24, this.matrix.m33 - this.matrix.m34, this.matrix.m43 - this.matrix.m44);
        this.planes[2] = new Plane(-this.matrix.m14 - this.matrix.m11, -this.matrix.m24 - this.matrix.m21, -this.matrix.m34 - this.matrix.m31, -this.matrix.m44 - this.matrix.m41);
        this.planes[3] = new Plane(this.matrix.m11 - this.matrix.m14, this.matrix.m21 - this.matrix.m24, this.matrix.m31 - this.matrix.m34, this.matrix.m41 - this.matrix.m44);
        this.planes[4] = new Plane(this.matrix.m12 - this.matrix.m14, this.matrix.m22 - this.matrix.m24, this.matrix.m32 - this.matrix.m34, this.matrix.m42 - this.matrix.m44);
        this.planes[5] = new Plane(-this.matrix.m14 - this.matrix.m12, -this.matrix.m24 - this.matrix.m22, -this.matrix.m34 - this.matrix.m32, -this.matrix.m44 - this.matrix.m42);

        this.normalizePlane(this.planes[0]);
        this.normalizePlane(this.planes[1]);
        this.normalizePlane(this.planes[2]);
        this.normalizePlane(this.planes[3]);
        this.normalizePlane(this.planes[4]);
        this.normalizePlane(this.planes[5]);
    }

    private static void intersectionPoint(final Plane a, final Plane b, final Plane c, Vector3 result)
    {
        // @formatter:off
        // Formula used
        //                d1 ( N2 * N3 ) + d2 ( N3 * N1 ) + d3 ( N1 * N2 )
        // P =   -------------------------------------------------------------------------
        //                             N1 . ( N2 * N3 )
        //
        // Note: N refers to the normal, d refers to the displacement.
        //       '.' means dot product. '*' means cross product
        // @formatter:on

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

    // #endregion
}
