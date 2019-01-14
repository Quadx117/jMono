package jMono_Framework;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.input.MouseState;

public abstract class GameWindow
{
    // #region Properties

    public abstract boolean allowUserResizing();

    public abstract void setAllowUserResizing(boolean value);

    public abstract Rectangle getClientBounds();

    protected boolean _allowAltF4 = true;

    /**
     * Gets a boolean that enables usage of Alt+F4 for window closing on desktop platforms.
     * Value is {@code true} by default.
     * 
     * @return Whether or not Alt+F4 is enabled for closing the window.
     * 
     */
    public boolean allowAltF4()
    {
        return _allowAltF4;
    }

    /**
     * Sets a boolean that enables usage of Alt+F4 for window closing on desktop platforms.
     * Value is {@code true} by default.
     * 
     * @param value
     *        The new value for this attribute.
     */
    public void setAllowAltF4(boolean value)
    {
        _allowAltF4 = value;
    }

// #if (WINDOWS && !WINRT) || DESKTOPGL
    /**
     * The location of this window on the desktop, eg: global coordinate space which stretches
     * across all screens.
     */
    public abstract java.awt.Point getPosition();

    public abstract void setPosition(java.awt.Point value);
// #endif

    public abstract DisplayOrientation getCurrentOrientation();

    public abstract JavaGameWindow getHandle();

    public abstract String getScreenDeviceName();

    private String _title;

    /**
     * Returns the title of the game window.
     * 
     * @return The title of the game window.
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Sets the title of the game window.
     * 
     * @param value
     *        The new title value for the window.
     */
    public void setTitle(String value)
    {
        if (_title != value)
        {
            SetTitle(value);
            _title = value;
        }
    }

    // TODO(Eric): refactor for JFrame ?
    /**
     * Determines whether the border of the window is visible.
     * 
     * @return Whether the border of the window is visible.
     */
    public boolean isBorderless()
    {
        return false;
    }

    public void setBorderless(boolean value)
    {
        throw new UnsupportedOperationException();
    }

    public MouseState mouseState = new MouseState();

    // protected TouchPanelState TouchPanelState;

    protected GameWindow()
    {
        // TouchPanelState = new TouchPanelState(this);
    }

    // #endregion Properties

    // #region Events

    public Event<EventArgs> clientSizeChanged = new Event<EventArgs>();
    public Event<EventArgs> orientationChanged = new Event<EventArgs>();
    public Event<EventArgs> screenDeviceNameChanged = new Event<EventArgs>();

// #if WINDOWS || WINDOWS_UAP || DESKTOPGL|| ANGLE
    /**
     * Use this event to retrieve text for objects like textbox's.
     * This event is not raised by non-character keys.
     * This event also supports key repeat.
     * For more information this event is based off:
     * http://msdn.microsoft.com/en-AU/library/system.windows.forms.control.keypress.aspx
     * This event is only supported on the Windows DirectX, Windows OpenGL and Linux platforms.
     */
    public Event<TextInputEventArgs> textInput = new Event<TextInputEventArgs>();
// #endif

    // #endregion Events

    public abstract void beginScreenDeviceChange(boolean willBeFullScreen);

    public abstract void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight);

    public void endScreenDeviceChange(String screenDeviceName)
    {
        endScreenDeviceChange(screenDeviceName, getClientBounds().width, getClientBounds().height);
    }

    protected void onActivated()
    {}

    protected void onClientSizeChanged()
    {
        if (clientSizeChanged != null)
            clientSizeChanged.handleEvent(this, EventArgs.Empty);
    }

    protected void onDeactivated()
    {}

    protected void onOrientationChanged()
    {
        if (orientationChanged != null)
            orientationChanged.handleEvent(this, EventArgs.Empty);
    }

    protected void onPaint()
    {}

    protected void onScreenDeviceNameChanged()
    {
        if (screenDeviceNameChanged != null)
            screenDeviceNameChanged.handleEvent(this, EventArgs.Empty);
    }

// #if WINDOWS || WINDOWS_UAP || DESKTOPGL || ANGLE
    protected void onTextInput(Object sender, TextInputEventArgs e)
    {
        if (textInput != null)
            textInput.handleEvent(sender, e);
    }
// #endif

    protected abstract void setSupportedOrientations(DisplayOrientation orientations);

    protected abstract void SetTitle(String title);

// #if DIRECTX && WINDOWS
    public static GameWindow create(Game game, int width, int height)
    {
        JavaGameWindow window = new JavaGameWindow((JavaGamePlatform) game.platform);
        window.initialize(width, height);

        return window;
    }
// #endif

}
