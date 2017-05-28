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
		return  Math.abs(x.M11 - y.M11) < _epsilon &&
                Math.abs(x.M12 - y.M12) < _epsilon &&
                Math.abs(x.M13 - y.M13) < _epsilon &&
                Math.abs(x.M14 - y.M14) < _epsilon &&
                Math.abs(x.M21 - y.M21) < _epsilon &&
                Math.abs(x.M22 - y.M22) < _epsilon &&
                Math.abs(x.M23 - y.M23) < _epsilon &&
                Math.abs(x.M24 - y.M24) < _epsilon &&
                Math.abs(x.M31 - y.M31) < _epsilon &&
                Math.abs(x.M32 - y.M32) < _epsilon &&
                Math.abs(x.M33 - y.M33) < _epsilon &&
                Math.abs(x.M34 - y.M34) < _epsilon &&
                Math.abs(x.M41 - y.M41) < _epsilon &&
                Math.abs(x.M42 - y.M42) < _epsilon &&
                Math.abs(x.M43 - y.M43) < _epsilon &&
                Math.abs(x.M44 - y.M44) < _epsilon;
	}

	@Override
	public int hashCode(Matrix obj)
	{
		throw new UnsupportedOperationException();
	}

}
