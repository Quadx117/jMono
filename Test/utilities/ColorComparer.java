package utilities;

import jMono_Framework.Color;
import jMono_Framework.dotNet.IEqualityComparer;

public class ColorComparer implements IEqualityComparer<Color>
{
	static public ColorComparer Equal = new ColorComparer();

	private ColorComparer() {}

	@Override
	public boolean equals(Color x, Color y)
	{
		return (x != null && y != null &&
				x.getPackedValue() == y.getPackedValue());
	}

	@Override
	public int hashCode(Color obj)
	{
		throw new UnsupportedOperationException();
	}

}
