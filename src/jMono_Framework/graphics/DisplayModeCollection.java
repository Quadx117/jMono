package jMono_Framework.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisplayModeCollection implements Iterable<DisplayMode> //, IEnumerable
{
	private List<DisplayMode> modes;

	public List<DisplayMode> getDisplayModeCollection(SurfaceFormat format)
	{
		List<DisplayMode> list = new ArrayList<DisplayMode>();
		for (DisplayMode mode : this.modes)
		{
			if (mode.getFormat().equals(format))
			{
				list.add(mode);
			}
		}
		return list;

	}

	@Override
	public Iterator<DisplayMode> iterator()
	{
		return modes.iterator();
	}

	// IEnumerator IEnumerable.GetEnumerator() {
	// return modes.GetEnumerator();
	// }

	public DisplayModeCollection(List<DisplayMode> setmodes)
	{
		modes = setmodes;
	}

}
