package jMono_Framework.graphics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DisplayModeCollection implements Iterable<DisplayMode>
{
    private List<DisplayMode> _modes;

    public List<DisplayMode> getDisplayModeCollection(SurfaceFormat format)
    {
        List<DisplayMode> list = new ArrayList<DisplayMode>();
        for (DisplayMode mode : _modes)
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
        return _modes.iterator();
    }

    // IEnumerator IEnumerable.GetEnumerator()
    // {
    // return modes.GetEnumerator();
    // }

    public DisplayModeCollection(List<DisplayMode> modes)
    {
        // Sort the modes in a consistent way that happens
        // to match XNA behavior on some graphics devices.

        modes.sort(new Comparator<DisplayMode>()
        {
            @Override
            public int compare(DisplayMode a, DisplayMode b)
            {
                if (a.equals(b))
                    return 0;
                if (a.getFormat().ordinal() <= b.getFormat().ordinal() && a.getWidth() <= b.getWidth() && a.getHeight() <= b.getHeight())
                    return -1;
                return 1;
            }
        });

        _modes = modes;
    }
}
