package utilities;

import jMono_Framework.dotNet.IEqualityComparer;
import jMono_Framework.math.Vector2;

public class Vector2Comparer implements IEqualityComparer<Vector2>
{
	static public Vector2Comparer Epsilon = new Vector2Comparer(0.000001f);

	private final float _epsilon;

	private Vector2Comparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Vector2 x, Vector2 y)
	{
		return (x != null && y != null &&
				Math.abs(x.x - y.x) < _epsilon &&
				Math.abs(x.y - y.y) < _epsilon);
	}

	@Override
	public int hashCode(Vector2 obj)
	{
		throw new UnsupportedOperationException();
	}

}
