package utilities;

import jMono_Framework.dotNet.IEqualityComparer;
import jMono_Framework.math.Quaternion;

public class QuaternionComparer implements IEqualityComparer<Quaternion>
{
	static public QuaternionComparer Epsilon = new QuaternionComparer(0.000001f);

	private final float _epsilon;

	private QuaternionComparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Quaternion x, Quaternion y)
	{
		return (x != null && y != null &&
				Math.abs(x.x - y.x) < _epsilon &&
				Math.abs(x.y - y.y) < _epsilon &&
				Math.abs(x.z - y.z) < _epsilon &&
				Math.abs(x.w - y.w) < _epsilon);
	}

	@Override
	public int hashCode(Quaternion obj)
	{
		throw new UnsupportedOperationException();
	}
}
