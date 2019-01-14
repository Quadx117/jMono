package jMono_Framework;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.dotNet.events.EventHandler;
import jMono_Framework.input.Mouse;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

public abstract class GamePlatform implements AutoCloseable
{
    // #region Fields

    protected TimeSpan _inactiveSleepTime = TimeSpan.fromMilliseconds(20.0);
    protected boolean _needsToResetElapsedTime = false;
    boolean isDisposed;
    protected boolean inFullScreenMode = false;

    protected boolean isDisposed()
    {
        return isDisposed;
    }

    // #endregion

    // #region Construction/Destruction

    protected GamePlatform(Game game)
    {
        if (game == null)
            throw new NullPointerException("The argument game cannot be null.");
        this.game = game;
    }

    @Override
    public void finalize()
    {
        dispose(false);
    }

    // #endregion Construction/Destruction

    // #region Public Properties

    /**
     * When implemented in a derived class, reports the default GameRunBehavior for this platform.
     * 
     * @return The default GameRunBehavior for this platform.
     */
    public abstract GameRunBehavior getDefaultRunBehavior();

    private Game game;

    /**
     * Gets the Game instance that owns this GamePlatform instance.
     * 
     * @return The Game instance that owns this GamePlatform instance.
     */
    public Game getGame()
    {
        return game;
    }

    private boolean _isActive;

    public boolean isActive()
    {
        return _isActive;
    }

    protected void setActive(boolean value)
    {
        if (_isActive != value)
        {
            _isActive = value;
            raise(_isActive ? activated : deactivated, EventArgs.Empty);
        }
    }

    private boolean _isMouseVisible;

    public boolean isMouseVisible()
    {
        return _isMouseVisible;
    }

    public void setMouseVisible(boolean value)
    {
        if (_isMouseVisible != value)
        {
            _isMouseVisible = value;
            onIsMouseVisibleChanged();
        }
    }

    private GameWindow _window;

    public GameWindow getWindow()
    {
        return _window;
    }

    protected void setWindow(GameWindow value)
    {
        if (_window == null)
        {
            Mouse.PrimaryWindow = value;
            // TouchPanel.PrimaryWindow = value;
        }

        _window = value;
    }

    // #endregion

    // #region Events

    public Event<EventArgs> asyncRunLoopEnded = new Event<EventArgs>();
    public Event<EventArgs> activated = new Event<EventArgs>();
    public Event<EventArgs> deactivated = new Event<EventArgs>();

    private <TEventArgs extends EventArgs> void raise(EventHandler<TEventArgs> handler, TEventArgs e)
    {
        if (handler != null)
            handler.handleEvent(this, e);
    }

    /**
     * Raises the AsyncRunLoopEnded event. This method must be called by derived classes when the
     * asynchronous run loop they start has stopped running.
     */
    protected void raiseAsyncRunLoopEnded()
    {
        raise(asyncRunLoopEnded, EventArgs.Empty);
    }

    // #endregion Events

    // #region Methods

    /**
     * Gives derived classes an opportunity to do work before any components are initialized. Note
     * that the base implementation sets IsActive to true, so derived classes should either call the
     * base implementation or set IsActive to true by their own means.
     */
    public void beforeInitialize()
    {
        setActive(true);
    }

    /**
     * Gives derived classes an opportunity to do work just before the run loop is begun.
     * Implementations may also return false to prevent the run loop from starting.
     * 
     * @return
     */
    public boolean beforeRun()
    {
        return true;
    }

    /**
     * When implemented in a derived, ends the active run loop.
     */
    public abstract void exit();

    /**
     * When implemented in a derived, starts the run loop and blocks until it has ended.
     */
    public abstract void runLoop();

    /**
     * When implemented in a derived, starts the run loop and returns immediately.
     */
    public abstract void startRunLoop();

    /**
     * Gives derived classes an opportunity to do work just before Update is called for all
     * IUpdatable components. Returning false from this method will result in this round of Update
     * calls being skipped.
     * 
     * @param gameTime
     * @return
     */
    public abstract boolean beforeUpdate(GameTime gameTime);

    /**
     * Gives derived classes an opportunity to do work just before Draw is called for all IDrawable
     * components. Returning false from this method will result in this round of Draw calls being
     * skipped.
     * 
     * @param gameTime
     * @return
     */
    public abstract boolean beforeDraw(GameTime gameTime);

    /**
     * When implemented in a derived class, causes the game to enter full-screen mode.
     */
    public abstract void enterFullScreen();

    /**
     * When implemented in a derived class, causes the game to exit full-screen mode.
     */
    public abstract void exitFullScreen();

    /**
     * Gives derived classes an opportunity to modify Game.TargetElapsedTime before it is set.
     * 
     * @param value
     *        The proposed new value of TargetElapsedTime.
     * @return The new value of TargetElapsedTime that will be set.
     */
    public TimeSpan targetElapsedTimeChanging(TimeSpan value)
    {
        return value;
    }

    /**
     * Starts a device transition (windowed to full screen or vice versa).
     * 
     * @param willBeFullScreen
     *        Specifies whether the device will be in full-screen mode upon completion of the
     *        change.
     */
    public abstract void beginScreenDeviceChange(boolean willBeFullScreen);

    /**
     * Completes a device transition.
     * 
     * @param screenDeviceName
     *        Screen device name.
     * @param clientWidth
     *        The new width of the game's client window.
     * @param clientHeight
     *        The new height of the game's client window.
     */
    public abstract void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight);

    /**
     * Gives derived classes an opportunity to take action after Game.TargetElapsedTime has been
     * set.
     */
    public void targetElapsedTimeChanged() {}

    /**
     * Use this method if your game is recovering from a slow-running state, and ElapsedGameTime is
     * too large to be useful.
     * <p>
     * Frame timing is generally handled by the Game class, but some platforms still handle it
     * elsewhere. Once all platforms rely on the Game class's functionality, this method and any
     * overrides should be removed.
     */
    public void resetElapsedTime() {}

    public void present() {}

    protected void onIsMouseVisibleChanged() {}

    /**
     * Used by the GraphicsDeviceManager to update the platform window after the graphics device has
     * changed the presentation.
     */
    protected void onPresentationChanged() {}

    // #endregion Methods

    // #region IDisposable implementation

    /**
     * Performs application-defined tasks associated with freeing, releasing, or resetting unmanaged
     * resources.
     */
    @Override
    public void close()
    {
        dispose(true);
        // GC.SuppressFinalize(this);
    }

    protected void dispose(boolean disposing)
    {
        if (!isDisposed)
        {
            Mouse.PrimaryWindow = null;
            // TouchPanel.PrimaryWindow = null;

            isDisposed = true;
        }
    }

    /**
     * Log the specified Message.
     * 
     * @param message
     */
    public void log(String message) {}

    // #endregion

    //
    // From GamePlatform.Desktop.cs
    //
    public static GamePlatform platformCreate(Game game)
    {
        return new JavaGamePlatform(game);
        // #if MONOMAC
        // return new MacGamePlatform(game);
        // #elif DESKTOPGL || ANGLE
        // return new SdlGamePlatform(game);
        // #elif WINDOWS && DIRECTX
        // return new MonoGame.Framework.WinFormsGamePlatform(game);
        // #elif WINDOWS_UAP
        // return new UAPGamePlatform(game);
        // #elif WINRT
        // return new MetroGamePlatform(game);
        // #endif
    }

    // #if WINDOWS_STOREAPP
    // public event EventHandler<ViewStateChangedEventArgs> ViewStateChanged;

    // private ApplicationViewState _viewState;
    // public ApplicationViewState ViewState
    // {
    // get { return _viewState; }
    // set
    // {
    // if (_viewState == value)
    // return;

    // Raise(ViewStateChanged, new ViewStateChangedEventArgs(value));

    // _viewState = value;
    // }
    // }
    // #endif
    // }

}
