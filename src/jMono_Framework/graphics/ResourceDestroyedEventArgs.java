package jMono_Framework.graphics;

import jMono_Framework.dotNet.events.EventArgs;

public class ResourceDestroyedEventArgs extends EventArgs
{
    /**
     * The name of the destroyed resource.
     */
    public String name;

    /**
     * The resource manager tag of the destroyed resource.
     */
    public Object Tag;
}
