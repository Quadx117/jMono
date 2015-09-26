package gameCore;

/**
 * Defines the intersection between a <see cref="Plane"/> and a bounding volume.
 * 
 * @author Eric
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
