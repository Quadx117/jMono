package utilities;

import jMono_Framework.dotNet.IEqualityComparer;

public class FloatComparer implements IEqualityComparer<Float>
{
	static public FloatComparer Epsilon = new FloatComparer(0.000001f);

	private final float _epsilon;

	private FloatComparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Float x, Float y)
	{
		return (x != null && y != null &&
				Math.abs(x - y) < _epsilon);
	}

	@Override
	public int hashCode(Float obj)
	{
		throw new UnsupportedOperationException();
	}

}
