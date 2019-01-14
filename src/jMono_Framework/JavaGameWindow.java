package jMono_Framework;

import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.input.ButtonState;
import jMono_Framework.input.KeyState;
import jMono_Framework.input.KeyboardInputEventArgs;
import jMono_Framework.input.KeyboardRawInput;
import jMono_Framework.input.Keys;
import jMono_Framework.input.Mouse;
import jMono_Framework.input.MouseButtons;
import jMono_Framework.input.MouseEventArgs;
import jMono_Framework.input.MouseRawInput;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class JavaGameWindow extends GameWindow implements AutoCloseable
{
    protected JFrameGameFrame _frame;

    // static private ReaderWriterLockSlim _allWindowsReaderWriterLockSlim = new
    // ReaderWriterLockSlim(LockRecursionPolicy.NoRecursion);
    static private List<JavaGameWindow> _allWindows = new ArrayList<JavaGameWindow>();

    private JavaGamePlatform _platform;

    private boolean _isResizable;

    private boolean _isBorderless;

    private boolean _isMouseHidden;

    private boolean _isMouseInBounds;

    // true if window position was moved either through code or by dragging/resizing the form
    private boolean _wasMoved;

    // #region Internal Properties

    private Game _game;

    public Game getGame()
    {
        return _game;
    }

    // #endregion

    // #region Public Properties

    @Override
    public JavaGameWindow getHandle()
    {
        return this;
    }

    @Override
    public String getScreenDeviceName()
    {
        return "";
    }

    @Override
    public Rectangle getClientBounds()
    {
        Point position = _frame.getLocationOnScreen();
        Dimension size = _frame.getClientSize();
        return new Rectangle(position.x, position.y, size.width, size.height);
    }

    @Override
    public boolean allowUserResizing()
    {
        return _isResizable;
    }

    @Override
    public void setAllowUserResizing(boolean value)
    {
        if (_isResizable != value)
        {
            _isResizable = value;
            _frame.setResizable(_isResizable);
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

    @Override
    public void setAllowAltF4(boolean value)
    {
        // TODO(Eric): Test to see if we want to set
        // _frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        _frame.allowAltF4 = value;
        super.setAllowAltF4(value);
    }

    @Override
    public DisplayOrientation getCurrentOrientation()
    {
        return DisplayOrientation.Default;
    }

    // TODO(Eric): This uses XnaPoint = Microsoft.Xna.Framework.Point;
    @Override
    public Point getPosition()
    {
        return _frame.getLocationOnScreen();
        // return new XnaPoint(_frame.getLocationOnScreen().getX(),
        // _frame.getLocationOnScreen().getY());
    }

    // TODO(Eric): This uses XnaPoint = Microsoft.Xna.Framework.Point;
    @Override
    public void setPosition(Point value)
    {
        _wasMoved = true;
        _frame.setLocation(value);
    }

    @Override
    protected void setSupportedOrientations(DisplayOrientation orientations)
    {}

    @Override
    public boolean isBorderless()
    {
        return _isBorderless;
    }

    @Override
    public void setBorderless(boolean value)
    {
        if (_isBorderless != value)
            _isBorderless = value;
        else
            return;
        if (_isBorderless)
            _frame.setUndecorated(true); // NOTE(Eric): this also affect the title bar
        else
            _frame.setUndecorated(false);
    }

    // #endregion

    protected List<Keys> keyState; // TODO(Eric): This was deleted in 3.6

    protected JavaGameWindow(JavaGamePlatform platform)
    {
        _platform = platform;
        _game = platform.getGame();

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        _frame = new JFrameGameFrame(this);
        _frame.setClientSize(new Dimension(GraphicsDeviceManager.DefaultBackBufferWidth, GraphicsDeviceManager.DefaultBackBufferHeight));

        setIcon();
        // TODO(Eric): How do I want to handle setting the title of the window by default ?
        // Title = Utilities.AssemblyHelper.GetDefaultWindowTitle();
        setTitle("XNA Framework in Java");

        _frame.setResizable(false);
        // Form.StartPosition = FormStartPosition.Manual;

        _frame.add(_game);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO(Eric): Move these into the _frame
        // Capture mouse events.
        MouseRawInput.mouseWheel.add(this::onMouseScroll);
        MouseRawInput.mouseEnter.add(this::onMouseEnter);
        MouseRawInput.mouseLeave.add(this::onMouseLeave);

        // TODO(Eric): Move these into the _frame
        _frame.activated.add(this::onActivated);
        _frame.deactivate.add(this::onDeactivate);
        _frame.onResizeEnd.add(this::onResizeEnd);

        // TODO(Eric): This was removed
        KeyboardRawInput.keyboardInput.add(this::onRawKeyEvent);

        _frame.keyPress.add(this::onKeyPress);

        registerToAllWindows();
        
        // TODO(Eric): Added this so the game can run
        _frame.setVisible(true);
    }

    // [DllImport("shell32.dll", CharSet = CharSet.Auto, BestFitMapping = false)]
    // private static extern IntPtr ExtractIcon(IntPtr hInst, string exeFileName, int iconIndex);

    // TODO(Eric): See DrillPad for how to set the icon
    private void setIcon()
    {
        // When running unit tests this can return null.
        // var assembly = Assembly.GetEntryAssembly();
        // if (assembly == null)
        // return;
        // var handle = ExtractIcon(IntPtr.Zero, assembly.Location, 0);
        // if (handle != IntPtr.Zero)
        // Form.Icon = Icon.FromHandle(handle);
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
        _platform.setActive(true);
        // Keyboard.setActive(true); // TODO(Eric): see if I need this.
    }

    private void onDeactivate(Object sender, EventArgs eventArgs)
    {
        _platform.setActive(false);
        // Keyboard.setActive(false); // TODO(Eric): see if I need this.
    }

    private void onMouseScroll(Object sender, MouseEventArgs mouseEventArgs)
    {
        // TODO(Eric): Need to validate that.
        mouseState._scrollWheelValue += mouseEventArgs.getDelta();
    }

    private void updateMouseState()
    {
        // If we call the form client functions before the form has
        // been made visible it will cause the wrong window size to
        // be applied at startup.
        if (!_frame.isVisible())
            return;

        Point clientPos = _frame.getContentPane().getMousePosition();
        // TODO(Eric): Could probably skip the last part and just put true instead
        boolean withinClient = (clientPos == null) ? false : _frame.getClientRectangle().contains(clientPos);
        // NOTE(Eric): new method = var buttons = Control.MouseButtons;
        int buttons = MouseRawInput.getButton().getValue();

        // ButtonState previousState = mouseState.getLeftButton();

        // TODO(Eric): Do I need to keep this if ?
        if (clientPos != null)
        {
            mouseState.setX(clientPos.x);
            mouseState.setY(clientPos.y);
            mouseState.setLeftButton((buttons & MouseButtons.Left.getValue()) == MouseButtons.Left.getValue() ? ButtonState.Pressed : ButtonState.Released);
            mouseState.setMiddleButton((buttons & MouseButtons.Middle.getValue()) == MouseButtons.Middle.getValue() ? ButtonState.Pressed : ButtonState.Released);
            mouseState.setRightButton((buttons & MouseButtons.Right.getValue()) == MouseButtons.Right.getValue() ? ButtonState.Pressed : ButtonState.Released);
        }

        // Don't process touch state if we're not active
        // and the mouse is within the client area.
        if (!_platform.isActive() || !withinClient)
            return;

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

    // TODO(Eric): Don't think I need these two methods. I think I could just hide the cursor in the
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

    // TODO(Eric): Remove this
    private void onRawKeyEvent(Object sender, KeyboardInputEventArgs args)
    {
        if (keyState == null)
            return;

        // NOTE(Eric): This is from the original code but doesn't seem to be required in java
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

    // TODO(Eric): KeyPressEventArgs from System.Windows.Forms
    // private void onKeyPress(Object sender, KeyPressEventArgs e)
    private void onKeyPress(Object sender, EventArgs e)
    {
        // onTextInput(sender, new TextInputEventArgs(e.KeyChar));
    }

    protected void initialize(int width, int height)
    {
        _frame.setClientSize(new Dimension(width, height));
        _frame.pack();
        // TODO(Eric): Do I need this, where is the Form.Location set in MonoGame ?
        // _frame.setLocationRelativeTo(null);

        // Enables us to receive key events for focus traversal keys (i.e. tab key)
        // so we can treat it ourselves
        // _frame.getContentPane().setFocusTraversalKeysEnabled(false);
        // _frame.setFocusTraversalKeysEnabled(false);
        for (int id : new int[] { KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                                 KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                                 KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
                                 KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS })
            _frame.setFocusTraversalKeys(id, Collections.emptySet());
    }

    private void onResizeEnd(Object sender, EventArgs eventArgs)
    {
        _wasMoved = true;
        if (_game.getWindow() == this)
        {
            GraphicsDeviceManager manager = _game.getGraphicsDeviceManager();
            if (manager.getGraphicsDevice() == null)
                return;

            Dimension newSize = _frame.getClientSize();
            if (newSize.width == manager.getPreferredBackBufferWidth() &&
                newSize.height == manager.getPreferredBackBufferHeight())
                return;

            // Set the default new back buffer size and viewport, but this
            // can be overloaded by the two events below.
            manager.setPreferredBackBufferWidth(newSize.width);
            manager.setPreferredBackBufferHeight(newSize.height);
            manager.applyChanges();
        }

        // Set the new view state which will trigger the
        // Game.ApplicationViewChanged event and signal
        // the client size changed event.
        onClientSizeChanged();
    }

    @Override
    protected void SetTitle(String title)
    {
        _frame.setTitle(title);
    }

    protected void runLoop()
    {
        _frame.setVisible(true);
        // TODO(Eric): Do I need this line of code ?
        _frame.requestFocus();

        while (_frame != null && _frame.isEnabled() == true)
        {
            // TODO: Delete when done testing
            // System.out.println("------------START----------------");
            // for (Thread t : Thread.getAllStackTraces().keySet())
            // {
            // if ("Direct Clip".equals(t.getName()))
            // System.out.println(t.getName());
            // }
            // System.out.println("------------END----------------");

            // TODO(Eric): Is this what exits the game now ?
            // if (nativeMsg.msg == WM_QUIT)
            // break;
            updateWindows();
            _game.tick();
        }

        /*
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
                if (window.getGame() == this.getGame())
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
        if (!_frame.getClientSize().equals(clientBounds))
            _frame.setClientSize(clientBounds);

        // if the window wasn't moved manually and it's resized, it should be centered
        if (!_wasMoved)
            _frame.centerOnPrimaryMonitor();
    }

    // [System.Security.SuppressUnmanagedCodeSecurity] // We won't use this maliciously
    // [DllImport("User32.dll", CharSet = CharSet.Auto)]
    // private static extern bool PeekMessage(out NativeMessage msg, IntPtr hWnd, uint
    // messageFilterMin, uint messageFilterMax, uint flags);

    // #region Public Methods

    public void close()
    {
        dispose(true);
        // GC.SuppressFinalize(this);
    }

    void dispose(boolean disposing)
    {
        if (disposing)
        {
            if (_frame != null)
            {
                unregisterFromAllWindows();
                _frame.dispose();
                _frame = null;
            }
        }
        _platform = null;
        _game = null;
        Mouse.setWindow(null);
        // TODO(Eric): To be deleted
        KeyboardRawInput.keyboardInput.remove(this::onRawKeyEvent);
    }

    @Override
    public void beginScreenDeviceChange(boolean willBeFullScreen)
    {}

    @Override
    public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight)
    {}

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
        else if (!_isMouseHidden && _isMouseInBounds)
        {
            hideCursor();
            _isMouseHidden = true;
        }
    }

    // #endregion

    // NOTE(Eric): Helper method that I added
    private void hideCursor()
    {
        _frame.setCursor(_frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
                                                                new Point(0, 0), null));
    }

    private void showCursor()
    {
        _frame.setCursor(Cursor.getDefaultCursor());
    }
}
