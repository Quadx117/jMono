package jMono_Framework;

/**
 * Defines the intersection between a {@link Plane} and a bounding volume.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public enum PlaneIntersectionType
{
    /**
     * There is no intersection, the bounding volume is in the negative half space of the plane.
     */
    Front,

    /**
     * There is no intersection, the bounding volume is in the positive half space of the plane.
     */
    Back,

    /**
     * The plane is intersected.
     */
    Intersecting
}
