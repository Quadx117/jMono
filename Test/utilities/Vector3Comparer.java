package utilities;

import jMono_Framework.dotNet.IEqualityComparer;
import jMono_Framework.math.Vector3;

public class Vector3Comparer implements IEqualityComparer<Vector3>
{
	static public Vector3Comparer Epsilon = new Vector3Comparer(0.000001f);

	private final float _epsilon;

	private Vector3Comparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Vector3 x, Vector3 y)
	{
		return (x != null && y != null &&
				Math.abs(x.x - y.x) < _epsilon &&
				Math.abs(x.y - y.y) < _epsilon &&
				Math.abs(x.z - y.z) < _epsilon);
	}

	@Override
	public int hashCode(Vector3 obj)
	{
		throw new UnsupportedOperationException();
	}
}
