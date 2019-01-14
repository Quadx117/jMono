package jMono_Framework;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class JFrameGameFrame extends JFrame
{
    GameWindow _window;

    public boolean allowAltF4 = true;

    public JFrameGameFrame(GameWindow window)
    {
        _window = window;
    }

    public void centerOnPrimaryMonitor()
    {
        setLocationRelativeTo(null);

        // NOTE(Eric): or this
        // setLocation(new Point((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() -
        // getWidth()) / 2,
        // (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2));
    }

    //
    // NOTE(Eric): Added these events that are provided by Windows.Forms in C#
    //

    // Keyboard event
    Event<EventArgs> keyPress = new Event<EventArgs>();

    // Frame events
    Event<EventArgs> activated = new Event<EventArgs>();
    Event<EventArgs> deactivate = new Event<EventArgs>();
    Event<EventArgs> onResizeEnd = new Event<EventArgs>();

    //
    // NOTE(Eric): Added these methods that are provided by Windows.Forms in C#
    //

    public Dimension getClientSize()
    {
        // NOTE(Eric): Use the same as setClientSize(Dimension size)
        return this.getContentPane().getPreferredSize();
    }

    public void setClientSize(Dimension size)
    {
        // NOTE(Eric): setSize() does not seem to work
        this.getContentPane().setPreferredSize(size);
    }

    public Rectangle getClientRectangle()
    {
        return this.getContentPane().getBounds();
    }

}
