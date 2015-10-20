package gameCore;

import gameCore.events.EventArgs;
import gameCore.input.KeyState;
import gameCore.input.KeyboardRawInput;
import gameCore.input.KeyboardInputEventArgs;
import gameCore.input.Keys;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class JavaGameWindow extends GameWindow implements AutoCloseable
{

	// protected WinFormsGameForm _form;
	// TODO: Need to create subclass of JFrame so I can add the events
	protected JFrame frame;

	// static private ReaderWriterLockSlim _allWindowsReaderWriterLockSlim = new
	// ReaderWriterLockSlim(LockRecursionPolicy.NoRecursion);
	static private List<JavaGameWindow> _allWindows = new ArrayList<JavaGameWindow>();

	private JavaGamePlatform _platform;

	private boolean _isResizable;

	private boolean _isBorderless;

	private boolean _isMouseHidden;

	private boolean _isMouseInBounds;

	// protected Game Game { get; private set; }
	private Game game;

	public Game getGame()
	{
		return this.game;
	}

	// public override IntPtr Handle { get { return _form.Handle; } }

	public String getScreenDeviceName()
	{
		return "";
	}

	public Rectangle getClientBounds()
	{
		java.awt.Rectangle clientRect = frame.getContentPane().getBounds();
		return new Rectangle(clientRect.x, clientRect.y, clientRect.width, clientRect.height);
	}

	public boolean allowUserResizing()
	{
		return _isResizable;
	}

	public void allowUserResizing(boolean value)
	{
		if (_isResizable != value)
		{
			_isResizable = value;
			frame.setResizable(_isResizable);
		}
		else
			return;

		// NOTE: This part is useless in Java since setResizable disables the maximize button and
		// make the borders fixed.
		// if (_isBorderless)
		// return;

		// frame.FormBorderStyle = _isResizable ? FormBorderStyle.Sizable :
		// FormBorderStyle.FixedSingle;
	}

	@Override
	public boolean allowAltF4()
	{
		return super.allowAltF4();
	}

	public void setAllowAltF4(boolean value)
	{
		// _form.AllowAltF4 = value;
		super.setAllowAltF4(value);
	}

	public DisplayOrientation getCurrentOrientation()
	{
		return DisplayOrientation.Default;
	}

	public Point getPosition()
	{
		return frame.getLocationOnScreen();
	}

	public void setPosition(Point value)
	{
		// _form.DesktopLocation = new Point(value.X, value.Y);
		frame.setLocation(value);
	}

	@Override
	protected void setSupportedOrientations(DisplayOrientation orientations) {}

	@Override
	public boolean isBorderless()
	{
		return _isBorderless;
	}

	public void isBorderless(boolean value)
	{
		if (_isBorderless != value)
			_isBorderless = value;
		else
			return;
		if (_isBorderless)
			// _form.FormBorderStyle = FormBorderStyle.None;
			frame.setUndecorated(true);
		else
			// _form.FormBorderStyle = _isResizable ? FormBorderStyle.Sizable :
			// FormBorderStyle.FixedSingle;
			frame.setUndecorated(false);
	}

	protected List<Keys> keyState;

	protected JavaGameWindow(JavaGamePlatform platform)
	{
		_platform = platform;
		this.game = platform.getGame();

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// _form = new WinFormsGameForm(this);
		frame = new JFrame();

		// When running unit tests this can return null.
		// var assembly = Assembly.GetEntryAssembly();
		// if (assembly != null)
		// TODO: Loading Icon for the frame
		// _form.Icon = Icon.ExtractAssociatedIcon(assembly.Location);
		// TODO: How do I want to handle setting the title of the window by default ?
		// Title = Utilities.AssemblyHelper.GetDefaultWindowTitle();
		setTitle("XNA Framework in Java");

		// _form.MaximizeBox = false;
		// _form.FormBorderStyle = FormBorderStyle.FixedSingle;
		frame.setResizable(false);
		// _form.StartPosition = FormStartPosition.CenterScreen;

		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TODO: Need to create subclass of JFrame so I can add the events
		// TODO: Mouse events
		// Capture mouse events.
		// _frame.MouseWheel += OnMouseScroll;
		// frame.MouseEnter += OnMouseEnter;
		// frame.MouseLeave += OnMouseLeave;

		// Use RawInput to capture key events.
		// Device.RegisterDevice(UsagePage.Generic, UsageId.GenericKeyboard, DeviceFlags.None);
		// Device.KeyboardInput += OnRawKeyEvent;
		KeyboardRawInput.onRawKeyEvent = this::onRawKeyEvent;

		// frame.Activated += OnActivated;
		// frame.Deactivate += OnDeactivate;
		// frame.clientSizeChanged += OnClientSizeChanged;

		// _form.KeyPress += OnKeyPress;

		registerToAllWindows();
	}

	@Override
	public void finalize()
	{
		dispose(false);
	}

	private void registerToAllWindows()
	{
		// _allWindowsReaderWriterLockSlim.EnterWriteLock();

		try
		{
			_allWindows.add(this);
		}
		finally
		{
			// _allWindowsReaderWriterLockSlim.ExitWriteLock();
		}
	}

	private void unregisterFromAllWindows()
	{
		// _allWindowsReaderWriterLockSlim.EnterWriteLock();

		try
		{
			_allWindows.remove(this);
		}
		finally
		{
			// _allWindowsReaderWriterLockSlim.ExitWriteLock();
		}
	}

	private void onActivated(Object sender, EventArgs eventArgs)
	{
// #if (WINDOWS && DIRECTX)
		if (getGame().getGraphicsDevice() != null)
		{
			if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
			{
				if (!_platform.isActive() && getGame().getGraphicsDevice().getPresentationParameters().isFullScreen())
				{
					getGame().getGraphicsDevice().getPresentationParameters().setFullScreen(true);
					// getGame().getGraphicsDevice().createSizeDependentResources(true);
					getGame().getGraphicsDevice().applyRenderTargets(null);
				}
			}
		}
// #endif
		_platform.setActive(true);
	}

	private void onDeactivate(Object sender, EventArgs eventArgs)
	{
		_platform.setActive(false);

		if (keyState != null)
			keyState.clear();
	}

	// TODO: How can I port this
	// private void onMouseScroll(Object sender, MouseEventArgs mouseEventArgs) {
	private void onMouseScroll(Object sender, EventArgs mouseEventArgs)
	{
		// TODO: Mouse input
		// MouseState.ScrollWheelValue += mouseEventArgs.Delta;
	}

	private void updateMouseState()
	{
		// If we call the form client functions before the form has
		// been made visible it will cause the wrong window size to
		// be applied at startup.
		if (!frame.isVisible())
			return;

		// Point clientPos = _form.PointToClient(Control.MousePosition);
		// boolean withinClient = _form.ClientRectangle.Contains(clientPos);
		// var buttons = Control.MouseButtons;

		// var previousState = MouseState.LeftButton;

		// MouseState.X = clientPos.X;
		// MouseState.Y = clientPos.Y;
		// MouseState.LeftButton = (buttons & MouseButtons.Left) == MouseButtons.Left ?
		// ButtonState.Pressed : ButtonState.Released;
		// MouseState.MiddleButton = (buttons & MouseButtons.Middle) == MouseButtons.Middle ?
		// ButtonState.Pressed : ButtonState.Released;
		// MouseState.RightButton = (buttons & MouseButtons.Right) == MouseButtons.Right ?
		// ButtonState.Pressed : ButtonState.Released;

		// Don't process touch state if we're not active
		// and the mouse is within the client area.
		// if (!_platform.isActive() || !withinClient)
		// return;

		// TouchLocationState? touchState = null;
		// if (MouseState.LeftButton == ButtonState.Pressed)
		// if (previousState == ButtonState.Released)
		// touchState = TouchLocationState.Pressed;
		// else
		// touchState = TouchLocationState.Moved;
		// else if (previousState == ButtonState.Pressed)
		// touchState = TouchLocationState.Released;
		//
		// if (touchState.HasValue)
		// TouchPanelState.AddEvent(0, touchState.Value, new Vector2(MouseState.X, MouseState.Y),
		// true);
	}

	// TODO: Don't think I need these two methods. I think I could just hide the cursor in the
	// constructor as java re-enables the cursor when it leaves the bounds of the JFrame.
	private void onMouseEnter(Object sender, EventArgs e)
	{
		_isMouseInBounds = true;
		if (!_platform.isMouseVisible() && !_isMouseHidden)
		{
			_isMouseHidden = true;
			hideCursor();
		}
	}

	private void onMouseLeave(Object sender, EventArgs e)
	{
		_isMouseInBounds = false;
		if (_isMouseHidden)
		{
			_isMouseHidden = false;
			showCursor();
		}
	}

	private void onRawKeyEvent(Object sender, KeyboardInputEventArgs args)
	{
		if (keyState == null)
			return;

		// NOTE: This is from the original code but doesn't seem to be required in java
		if (args.key == 0xff)
		{
			// dead key, e.g. a "shift" automatically happens when using Up/Down/Left/Right
			return;
		}

		Keys key = Keys.None;

		switch (args.keyLocation)
		{
			case KeyEvent.KEY_LOCATION_LEFT:
				switch (args.key)
				{
					case KeyEvent.VK_SHIFT:
						key = Keys.LeftShift;
						break;
					case KeyEvent.VK_CONTROL:
						key = Keys.LeftControl;
						break;
					case KeyEvent.VK_ALT:
						key = Keys.LeftAlt;
						break;
					case KeyEvent.VK_WINDOWS:
						key = Keys.LeftWindows;
						break;
				}
				break;

			case KeyEvent.KEY_LOCATION_RIGHT:
				switch (args.key)
				{
					case KeyEvent.VK_SHIFT:
						key = Keys.RightShift;
						break;
					case KeyEvent.VK_CONTROL:
						key = Keys.RightControl;
						break;
					case KeyEvent.VK_ALT:
						key = Keys.RightAlt;
						break;
					case KeyEvent.VK_WINDOWS:
						key = Keys.RightWindows;
						break;
				}
				break;

			default:
				key = Keys.valueOf(args.key);
				break;
		}

		if ((args.state == KeyState.Down) && !keyState.contains(key))
		{
			keyState.add(key);
		}
		else if (args.state == KeyState.Up)
		{
			keyState.remove(key);
		}
	}

	// private void onKeyPress(Object sender, KeyPressEventArgs e) {
	private void onKeyPress(Object sender, EventArgs e)
	{
		// onTextInput(sender, new TextInputEventArgs(e.KeyChar));
	}

	protected void initialize(int width, int height)
	{
		// _form.ClientSize = new Size(width, height);
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		// TODO: Do I need this line of code ?
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// TODO: Do I need this line of code ?
		frame.requestFocus();
	}

	private void onClientSizeChanged(Object sender, EventArgs eventArgs)
	{
		if (game.getWindow() == this)
		{
			GraphicsDeviceManager manager = game.getGraphicsDeviceManager();

			// Set the default new back buffer size and viewport, but this
			// can be overloaded by the two events below.

			int newWidth = frame.getContentPane().getBounds().width;	// _form.ClientRectangle.Width;
			int newHeight = frame.getContentPane().getBounds().height;	// _form.ClientRectangle.Height;
			manager.setPreferredBackBufferWidth(newWidth);
			manager.setPreferredBackBufferHeight(newHeight);

			if (manager.getGraphicsDevice() == null)
				return;
		}

		// Set the new view state which will trigger the
		// Game.ApplicationViewChanged event and signal
		// the client size changed event.
		onClientSizeChanged();
	}

	@Override
	protected void SetTitle(String title)
	{
		frame.setTitle(title);
	}

	protected void runLoop()
	{

		// while (_form != null && _form.IsDisposed == false)
		while (true)
		{
			updateWindows();
			game.tick();
		}

		/*
		 * Application.Idle += onIdle;
		 * Application.Run(_form);
		 * Application.Idle -= onIdle;
		 * 
		 * 
		 * // We need to remove the WM_QUIT message in the message
		 * // pump as it will keep us from restarting on this
		 * // same thread.
		 * //
		 * // This is critical for some NUnit runners which
		 * // typically will run all the tests on the same
		 * // process/thread.
		 * 
		 * var msg = new NativeMessage();
		 * do
		 * {
		 * if (msg.msg == WM_QUIT)
		 * break;
		 * 
		 * Thread.sleep(100);
		 * }
		 * while (PeekMessage(out msg, IntPtr.Zero, 0, 0, 1));
		 */
	}

	protected void updateWindows()
	{
		// _allWindowsReaderWriterLockSlim.EnterReadLock();

		try
		{
			// Update the mouse state for each window.
			for (JavaGameWindow window : _allWindows)
				window.updateMouseState();
		}
		finally
		{
			// _allWindowsReaderWriterLockSlim.ExitReadLock();
		}
	}

	// private final int WM_QUIT = 0x12;

	/*
	 * public struct NativeMessage
	 * {
	 * public IntPtr handle;
	 * public uint msg;
	 * public IntPtr wParam;
	 * public IntPtr lParam;
	 * public uint time;
	 * public System.Drawing.Point p;
	 * }
	 */
	protected void changeClientSize(Dimension clientBounds)
	{
		// this._form.ClientSize = clientBounds;
		frame.getContentPane().setPreferredSize(clientBounds);
	}

	// private static extern boolean PeekMessage(out NativeMessage msg, IntPtr hWnd, uint
	// messageFilterMin, uint messageFilterMax, uint flags);

	public void close()
	{
		dispose(true);
		// GC.SuppressFinalize(this);
	}

	void dispose(boolean disposing)
	{
		if (disposing)
		{
			if (frame != null)
			{
				unregisterFromAllWindows();
				frame.dispose();
				frame = null;
			}
		}
		_platform = null;
		this.game = null;
		// Mouse.SetWindows(null);
		// Device.KeyboardInput -= OnRawKeyEvent;
		// Device.RegisterDevice(UsagePage.Generic, UsageId.GenericKeyboard, DeviceFlags.Remove);
	}

	@Override
	public void beginScreenDeviceChange(boolean willBeFullScreen) {}

	@Override
	public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight) {}

	public void mouseVisibleToggled()
	{
		if (_platform.isMouseVisible())
		{
			if (_isMouseHidden)
			{
				showCursor();
				_isMouseHidden = false;
			}
		}
		else // if (!_isMouseHidden && _isMouseInBounds)
		{
			hideCursor();
			_isMouseHidden = true;
		}
	}

	// Helper method that I added
	private void hideCursor()
	{
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), null));
	}

	private void showCursor()
	{
		frame.setCursor(Cursor.getDefaultCursor());
	}
}
