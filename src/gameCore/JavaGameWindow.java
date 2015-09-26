package gameCore;

import gameCore.events.EventArgs;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class JavaGameWindow extends GameWindow {

	// protected WinFormsGameForm _form;
	protected JFrame frame;

	static private List<JavaGameWindow> _allWindows = new ArrayList<JavaGameWindow>();

	private JavaGamePlatform _platform;

	private boolean _isResizable;

	private boolean _isBorderless;

	private boolean _isMouseHidden;

	private boolean _isMouseInBounds;

	// protected Game Game { get; private set; }
	private Game game;

	public Game getGame() {
		return this.game;
	}

	// public override IntPtr Handle { get { return _form.Handle; } }

	public String getScreenDeviceName() {
		return "";
	}

	public Rectangle getClientBounds() {
		Rectangle clientRect = frame.getContentPane().getBounds();
		return new Rectangle(clientRect.x, clientRect.y, clientRect.width, clientRect.height);
	}

	public boolean allowUserResizing() {
		return _isResizable;
	}

	public void allowUserResizing(boolean value) {
		if (_isResizable != value) {
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
	public boolean allowAltF4() {
		return super.allowAltF4();
	}

	public void setAllowAltF4(boolean value) {
		// _form.AllowAltF4 = value;
		super.setAllowAltF4(value);
	}

	public DisplayOrientation getCurrentOrientation() {
		return DisplayOrientation.Default;
	}

	// TODO: Implement XnaPoint ? What's the difference with java.Point.
	// TODO: See using declarations : XnaPoint = Point.cs
	// public XnaPoint getPosition() {
	public Point getPosition() {
		return frame.getLocationOnScreen();
	}

	// public void setPosition(XnaPoint value) {
	public void setPosition(Point value) {
		// _form.DesktopLocation = new Point(value.X, value.Y);
		frame.setLocation(value);
	}

	@Override
	protected void setSupportedOrientations(DisplayOrientation orientations) {}

	@Override
	public boolean isBorderless() {
		return _isBorderless;
	}

	public void isBorderless(boolean value) {
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

	// TODO: KeyListener I guess.
	// protected List<XnaKey> keyState;

	protected JavaGameWindow(JavaGamePlatform platform) {
		_platform = platform;
		this.game = platform.getGame();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
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

		// TODO: Mouse events
		// Capture mouse events.
		// _form.MouseWheel += OnMouseScroll;
		// _form.MouseEnter += OnMouseEnter;
		// _form.MouseLeave += OnMouseLeave;

		// Use RawInput to capture key events.
		// Device.RegisterDevice(UsagePage.Generic, UsageId.GenericKeyboard, DeviceFlags.None);
		// Device.KeyboardInput += OnRawKeyEvent;

		// _form.Activated += OnActivated;
		// _form.Deactivate += OnDeactivate;
		// _form.ClientSizeChanged += OnClientSizeChanged;

		// _form.KeyPress += OnKeyPress;

		_allWindows.add(this);
	}

	private void onActivated(Object sender, EventArgs eventArgs) {
		_platform.setActive(true);
	}

	private void onDeactivate(Object sender, EventArgs eventArgs) {
		_platform.setActive(false);

		// TODO: Keyboard input
		// if (keyState != null)
		// keyState.clear();
	}

	// TODO: How can I port this
	// private void onMouseScroll(Object sender, MouseEventArgs mouseEventArgs) {
	private void onMouseScroll(Object sender, EventArgs mouseEventArgs) {
		// TODO: Mouse input
		// MouseState.ScrollWheelValue += mouseEventArgs.Delta;
	}

	private void updateMouseState() {
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
	private void onMouseEnter(Object sender, EventArgs e) {
		_isMouseInBounds = true;
		if (!_platform.isMouseVisible() && !_isMouseHidden) {
			_isMouseHidden = true;
			hideCursor();
		}
	}

	private void onMouseLeave(Object sender, EventArgs e) {
		_isMouseInBounds = false;
		if (_isMouseHidden) {
			_isMouseHidden = false;
			showCursor();
		}
	}

	// TODO: How can I port this.
	// private void onRawKeyEvent(Object sender, KeyboardInputEventArgs args) {
	private void onRawKeyEvent(Object sender, EventArgs args) {
		// if (KeyState == null)
		// return;
		//
		// if ((int) args.Key == 0xff) {
		// // dead key, e.g. a "shift" automatically happens when using Up/Down/Left/Right
		// return;
		// }
		//
		// XnaKey xnaKey;
		//
		// switch (args.MakeCode) {
		// case 0x2a: // LShift
		// xnaKey = XnaKey.LeftShift;
		// break;
		//
		// case 0x36: // RShift
		// xnaKey = XnaKey.RightShift;
		// break;
		//
		// case 0x1d: // Ctrl
		// xnaKey = (args.ScanCodeFlags & ScanCodeFlags.E0) != 0 ? XnaKey.RightControl :
		// XnaKey.LeftControl;
		// break;
		//
		// case 0x38: // Alt
		// xnaKey = (args.ScanCodeFlags & ScanCodeFlags.E0) != 0 ? XnaKey.RightAlt : XnaKey.LeftAlt;
		// break;
		//
		// default:
		// xnaKey = (XnaKey) args.Key;
		// break;
		// }
		//
		// if ((args.State == SharpDX.RawInput.KeyState.KeyDown || args.State ==
		// SharpDX.RawInput.KeyState.SystemKeyDown)
		// && !KeyState.Contains(xnaKey))
		// KeyState.Add(xnaKey);
		// else if (args.State == SharpDX.RawInput.KeyState.KeyUp || args.State ==
		// SharpDX.RawInput.KeyState.SystemKeyUp)
		// KeyState.Remove(xnaKey);
	}

	// private void onKeyPress(Object sender, KeyPressEventArgs e) {
	private void onKeyPress(Object sender, EventArgs e) {
		// onTextInput(sender, new TextInputEventArgs(e.KeyChar));
	}

	protected void initialize(int width, int height) {
		// _form.ClientSize = new Size(width, height);
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		// TODO: Do I need this line of code ?
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// TODO: Do I need this line of code ?
		frame.requestFocus();
	}

	private void onClientSizeChanged(Object sender, EventArgs eventArgs) {
		if (game.getWindow() == this) {
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
	protected void SetTitle(String title) {
		frame.setTitle(title);
	}

	protected void runLoop() {
		
		while (true) {
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

/*	private void onIdle(Object sender, EventArgs eventArgs) {
		// While there are no pending messages
		// to be processed tick the game.
		// NativeMessage msg;
		// while (!PeekMessage(out msg, IntPtr.Zero, 0, 0, 0))
		{
			updateWindows();
			game.tick();
		}
	}
*/
	
	protected void updateWindows() {
		// Update the mouse state for each window.
		for (JavaGameWindow window : _allWindows)
			window.updateMouseState();
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
	protected void changeClientSize(Dimension clientBounds) {
		// this._form.ClientSize = clientBounds;
		frame.getContentPane().setPreferredSize(clientBounds);
	}

	// private static extern boolean PeekMessage(out NativeMessage msg, IntPtr hWnd, uint
	// messageFilterMin, uint messageFilterMax, uint flags);

	public void dispose() {
		if (frame != null) {
			_allWindows.remove(this);
			// NOTE: This may cause the JVM to terminate
			// see : https://docs.oracle.com/javase/8/docs/api/java/awt/Window.html#dispose--
			frame.dispose();
			frame = null;
		}
	}

	@Override
	public void beginScreenDeviceChange(boolean willBeFullScreen) {}

	@Override
	public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight) {}

	public void mouseVisibleToggled() {
		if (_platform.isMouseVisible()) {
			if (_isMouseHidden) {
				showCursor();
				_isMouseHidden = false;
			}
		}
		else if (!_isMouseHidden && _isMouseInBounds) {
			hideCursor();
			_isMouseHidden = true;
		}
	}

	// Helper method that I added
	private void hideCursor() {
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), null));
	}

	private void showCursor() {
		frame.setCursor(Cursor.getDefaultCursor());
	}
}
