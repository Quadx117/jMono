package utilities;

import jMono_Framework.dotNet.IEqualityComparer;
import jMono_Framework.math.Matrix;

public class MatrixComparer implements IEqualityComparer<Matrix>
{
	static public MatrixComparer Epsilon = new MatrixComparer(0.000001f);

	private final float _epsilon;

	private MatrixComparer(float epsilon)
	{
		_epsilon = epsilon;
	}

	@Override
	public boolean equals(Matrix x, Matrix y)
	{
		return  Math.abs(x.m11 - y.m11) < _epsilon &&
                Math.abs(x.m12 - y.m12) < _epsilon &&
                Math.abs(x.m13 - y.m13) < _epsilon &&
                Math.abs(x.m14 - y.m14) < _epsilon &&
                Math.abs(x.m21 - y.m21) < _epsilon &&
                Math.abs(x.m22 - y.m22) < _epsilon &&
                Math.abs(x.m23 - y.m23) < _epsilon &&
                Math.abs(x.m24 - y.m24) < _epsilon &&
                Math.abs(x.m31 - y.m31) < _epsilon &&
                Math.abs(x.m32 - y.m32) < _epsilon &&
                Math.abs(x.m33 - y.m33) < _epsilon &&
                Math.abs(x.m34 - y.m34) < _epsilon &&
                Math.abs(x.m41 - y.m41) < _epsilon &&
                Math.abs(x.m42 - y.m42) < _epsilon &&
                Math.abs(x.m43 - y.m43) < _epsilon &&
                Math.abs(x.m44 - y.m44) < _epsilon;
	}

	@Override
	public int hashCode(Matrix obj)
	{
		throw new UnsupportedOperationException();
	}

}
