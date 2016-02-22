package gameCore;

import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.input.MouseState;

public abstract class GameWindow
{
	public boolean allowUserResizing;

	public Rectangle clientBounds;

	protected boolean _allowAltF4 = true;

	/**
	 * Gets a boolean that enables usage of Alt+F4 for window closing on desktop platforms. Value is
	 * true by default.
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
	 * Value is true by default.
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
	public Point position;
// #endif

// #if DESKTOPGL
    // public abstract System.Drawing.Icon Icon { get; set; }
// #endif

	public DisplayOrientation currentOrientation;

	// NOTE: Don't need that
	// public IntPtr handle;

	public String screenDeviceName;

	private String _title;
	public String getTitle() { return _title; }

	public void setTitle(String value)
	{
		if (_title != value)
		{
			SetTitle(value);
			_title = value;
		}
	}

	// TODO: refactor for JFrame
	/**
	 * Determines whether the border of the window is visible.
	 * 
	 * @return Whether the border of the window is visible.
	 */
	public boolean isBorderless()
	{
		return false;
	}

	public boolean setBorderless(boolean value)
	{
		throw new UnsupportedOperationException();
	}

	public MouseState mouseState;
	// protected TouchPanelState TouchPanelState;

	protected GameWindow()
	{
		// NOTE: Needed to add this because somehow it is initialized automatically in C#
		mouseState = new MouseState();
		// TouchPanelState = new TouchPanelState(this);
	}

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

	public abstract void beginScreenDeviceChange(boolean willBeFullScreen);

	public abstract void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight);

	public void endScreenDeviceChange(String screenDeviceName)
	{
		endScreenDeviceChange(screenDeviceName, clientBounds.width, clientBounds.height);
	}

	protected void onActivated() {}

	protected void onClientSizeChanged()
	{
		if (clientSizeChanged != null)
			clientSizeChanged.handleEvent(this, EventArgs.Empty);
	}

	protected void onDeactivated() {}

	protected void onOrientationChanged()
	{
		if (orientationChanged != null)
			orientationChanged.handleEvent(this, EventArgs.Empty);
	}

	protected void onPaint() {}

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
		// GameWindow window = new WinFormsGameWindow((WinFormsGamePlatform) game.platform);
		GameWindow window = new JavaGameWindow((JavaGamePlatform) game.platform);
		window.initialize(width, height);

		return window;
	}

// #endif

	// NOTE: I needed to add this so subclasses could override it.
	protected abstract void initialize(int width, int height);
}
