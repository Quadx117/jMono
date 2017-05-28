package utilities;

import jMono_Framework.Plane;
import jMono_Framework.dotNet.IEqualityComparer;

public class PlaneComparer implements IEqualityComparer<Plane>
{
	static public PlaneComparer Epsilon = new PlaneComparer(0.000001f);

	private final float _epsilon;

	private PlaneComparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Plane x, Plane y)
	{
		return (x != null && y != null &&
				Math.abs(x.normal.x - y.normal.x) < _epsilon &&
				Math.abs(x.normal.y - y.normal.y) < _epsilon &&
				Math.abs(x.normal.z - y.normal.z) < _epsilon &&
				Math.abs(x.D - y.D) < _epsilon);
	}

	@Override
	public int hashCode(Plane obj)
	{
		throw new UnsupportedOperationException();
	}

}
