package jMono_Framework.components;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.time.GameTime;

/**
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 * 
 */
public interface IDrawable
{
    int getDrawOrder();
    boolean isVisible();

    Event<EventArgs> getDrawOrderChanged();
    Event<EventArgs> getVisibleChanged();

    void draw(GameTime gameTime);
}
