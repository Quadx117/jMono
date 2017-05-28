package utilities;

import jMono_Framework.dotNet.IEqualityComparer;
import jMono_Framework.math.Vector4;

public class Vector4Comparer implements IEqualityComparer<Vector4>
{
	static public Vector4Comparer Epsilon = new Vector4Comparer(0.000001f);

	private final float _epsilon;

	private Vector4Comparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Vector4 x, Vector4 y)
	{
		return (x != null && y != null &&
				Math.abs(x.x - y.x) < _epsilon &&
				Math.abs(x.y - y.y) < _epsilon &&
				Math.abs(x.z - y.z) < _epsilon &&
				Math.abs(x.w - y.w) < _epsilon);
	}

	@Override
	public int hashCode(Vector4 obj)
	{
		throw new UnsupportedOperationException();
	}

}
