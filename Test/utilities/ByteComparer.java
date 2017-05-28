package utilities;

import jMono_Framework.dotNet.IEqualityComparer;

public class ByteComparer implements IEqualityComparer<Byte>
{
	static public ByteComparer Equal = new ByteComparer();

	private ByteComparer() {}

	@Override
	public boolean equals(Byte x, Byte y)
	{
		return (x != null && y != null &&
				x.byteValue() == y.byteValue());
	}

	@Override
	public int hashCode(Byte obj)
	{
		throw new UnsupportedOperationException();
	}

}
