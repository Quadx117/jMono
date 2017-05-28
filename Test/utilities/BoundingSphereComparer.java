package utilities;

import jMono_Framework.BoundingSphere;
import jMono_Framework.dotNet.IEqualityComparer;

public class BoundingSphereComparer implements IEqualityComparer<BoundingSphere>
{
	static public BoundingSphereComparer Epsilon = new BoundingSphereComparer(0.000001f);

	private final float _epsilon;

	private BoundingSphereComparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(BoundingSphere x, BoundingSphere y)
	{
		return (x != null && y != null &&
				Math.abs(x.center.x - y.center.x) < _epsilon &&
				Math.abs(x.center.y - y.center.y) < _epsilon &&
				Math.abs(x.center.z - y.center.z) < _epsilon &&
				Math.abs(x.radius - y.radius) < _epsilon);
	}

	@Override
	public int hashCode(BoundingSphere obj)
	{
		throw new UnsupportedOperationException();
	}

}
