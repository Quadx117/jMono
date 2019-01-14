package jMono_Framework;

import jMono_Framework.audio.SoundEffect;
import jMono_Framework.collections.SortingFilteringCollection;
import jMono_Framework.components.GameComponentCollection;
import jMono_Framework.components.GameComponentCollectionEventArgs;
import jMono_Framework.components.IDrawable;
import jMono_Framework.components.IGameComponent;
import jMono_Framework.components.IUpdateable;
import jMono_Framework.content.ContentManager;
import jMono_Framework.content.ContentTypeReaderManager;
import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.dotNet.events.EventHandler;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.IGraphicsDeviceService;
import jMono_Framework.graphics.Viewport;
import jMono_Framework.input.KeyboardRawInput;
import jMono_Framework.input.MouseRawInput;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.Stopwatch;
import jMono_Framework.time.TimeSpan;

import java.awt.Canvas;
import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * Provides basic graphics device initialization, game logic, and rendering code.
 * <p>
 * This class contains the core of the game and shoudldn't be exposed to the client. The game logic
 * goes into Game which inherits from this class.
 * <p>
 * The mouse and it's associated listeners are handled here, but everything related to the keyboard
 * or other inputs have to be handled in the child class.
 */
@SuppressWarnings("serial")
// NOTE(Eric): extends Canvas is used for the mouse and keyboard listeners
public abstract class Game extends Canvas implements Runnable, AutoCloseable
{
    private GameComponentCollection _components;
    private GameServiceContainer _services;
    private ContentManager _content;
    protected GamePlatform platform;

    private SortingFilteringCollection<IDrawable> _drawables =
            new SortingFilteringCollection<IDrawable>(d -> d.isVisible(),
                                                      (d, handler) -> d.getVisibleChanged().add(handler),
                                                      (d, handler) -> d.getVisibleChanged().remove(handler),
                                                      new Comparator<IDrawable>() {

                                                          @Override
                                                          public int compare(IDrawable d1, IDrawable d2)
                                                          {
                                                              return d1.getDrawOrder() - d2.getDrawOrder();
                                                          }
                                                      },
                                                      (d, handler) -> d.getDrawOrderChanged().add(handler),
                                                      (d, handler) -> d.getDrawOrderChanged().remove(handler));

    private SortingFilteringCollection<IUpdateable> _updateables =
            new SortingFilteringCollection<IUpdateable>(u -> u.isEnabled(),
                                                        (u, handler) -> u.getEnabledChanged().add(handler),
                                                        (u, handler) -> u.getEnabledChanged().remove(handler),
                                                        new Comparator<IUpdateable>() {

                                                            @Override
                                                            public int compare(IUpdateable u1, IUpdateable u2)
                                                            {
                                                                return u1.getUpdateOrder() - u2.getUpdateOrder();
                                                            }
                                                        },
                                                        (u, handler) -> u.getUpdateOrderChanged().add(handler),
                                                        (u, handler) -> u.getUpdateOrderChanged().remove(handler));

    private IGraphicsDeviceManager _graphicsDeviceManager;
    private IGraphicsDeviceService _graphicsDeviceService;

    private boolean _initialized = false;

    /**
     * Value indicating whether to use fixed time steps, {@code true} by default.
     * <p>
     * A fixed-step Game tries to call its update method on the fixed interval specified in
     * targetElapsedTime. Setting Game.isFixedTimeStep to {@code true} causes a Game to use a
     * fixed-step game loop. A new project uses a fixed-step game loop with a default
     * targetElapsedTime of 1/60th of a second.
     * <p>
     * In a fixed-step game loop, Game calls update once the targetElapsedTime has elapsed. After
     * update is called, if it is not time to call update again, Game calls Draw. After Draw is
     * called, if it is not time to call update again, Game idles until it is time to call update.
     * If update takes too long to process, Game sets isRunningSlowly to {@code true} and calls
     * update again, without calling Draw in between. When an update runs longer than the
     * targetElapsedTime, Game responds by calling update extra times and dropping the frames
     * associated with those updates to catch up. This ensures that update will have been called the
     * expected number of times when the game loop catches up from a slowdown. You can check the
     * value of isRunningSlowly in your update if you want to detect dropped frames and shorten your
     * update processing to compensate. You can reset the elapsed times by calling resetElapsedTime.
     */
    private boolean _isFixedTimeStep = true;

    /**
     * Target time between calls to {@code update} when {@code isFixedTimeStep} is {@code true}.
     * <p>
     * When the game frame rate is less than targetElapsedTime, isRunningSlowly will return
     * {@code true}.
     * <p>
     * The default value for targetElapsedTime is 1/60th of a second.
     * <p>
     * A fixed-step Game tries to call its update method on the fixed interval specified in
     * targetElapsedTime. Setting Game.isFixedTimeStep to {@code true} causes a Game to use a
     * fixed-step game loop. A new project uses a fixed-step game loop with a default
     * targetElapsedTime of 1/60th of a second.
     * <p>
     * In a fixed-step game loop, Game calls update once the targetElapsedTime has elapsed. After
     * update is called, if it is not time to call update again, Game calls Draw. After Draw is
     * called, if it is not time to call update again, Game idles until it is time to call update.
     * <p>
     * If update takes too long to process, Game sets isRunningSlowly to {@code true} and calls
     * update again, without calling Draw in between. When an update runs longer than the
     * targetElapsedTime, Game responds by calling update extra times and dropping the frames
     * associated with those updates to catch up. This ensures that update will have been called the
     * expected number of times when the game loop catches up from a slowdown. You can check the
     * value of isRunningSlowly in your update if you want to detect dropped frames and shorten your
     * update processing to compensate. You can reset the elapsed times by calling resetElapsedTime.
     */
    private TimeSpan _targetElapsedTime = TimeSpan.fromTicks(166667); // 60fps

    /**
     * The time to sleep when the game is inactive.
     */
    private TimeSpan _inactiveSleepTime = TimeSpan.fromSeconds(0.02);

    // TODO(Eric): Complete comments
    /**
     * Specifies the maximum length we will be updating the game before calling the {@code draw()}
     * method. The default value is 500 milliseconds.
     * <p>
     * If something outside update is taking too much time, we only catch up to this maximum value.
     * This makes sure we will call the {@code draw()} method at least once every time interval
     * specified by this variable.
     */
    private TimeSpan _maxElapsedTime = TimeSpan.fromMilliseconds(500);

    private boolean _shouldExit;

    /**
     * Value indicating whether to skip the call to draw until the next update.
     * Default value is set to {@code false}
     */
    private boolean _suppressDraw = false;

    /**
     * Initializes a new instance of this class with the default screenWidth, screenHeight and
     * title, which provides basic graphics device initialization, game logic, rendering code,
     * and a game loop.
     */
    public Game()
    {
        _instance = this;

        // TODO(Eric): This doesn't seem to be used anywhere
        // LaunchParameters = new LaunchParameters();
        _services = new GameServiceContainer();
        _components = new GameComponentCollection();
        _content = new ContentManager(_services);

        platform = GamePlatform.platformCreate(this);
        platform.activated.add(this::onActivated);
        platform.deactivated.add(this::onDeactivated);
        _services.addService(GamePlatform.class, platform);

        // Calling Update() for first time initializes some systems
        FrameworkDispatcher.update();

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
        // Platform.ViewStateChanged += Platform_ApplicationViewChanged;
// #endif

        // TODO(Eric): This is all added by me.

        // TODO(Eric): Check if I want to keep all of this here.
        // Should probably go in JavaGameWindow
        KeyboardRawInput keyboard = new KeyboardRawInput();
        addKeyListener(keyboard);

        MouseRawInput mouse = new MouseRawInput();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    @Override
    public void finalize()
    {
        dispose(false);
    }

    protected void log(String Message)
    {
        if (platform != null)
            platform.log(Message);
    }

    // #region IDisposable Implementation

    private boolean _isDisposed;

    @Override
    public void close()
    {
        dispose(true);
        // GC.SuppressFinalize(this);
        raise(disposed, EventArgs.Empty);
    }

    protected void dispose(boolean disposing)
    {
        if (!_isDisposed)
        {
            if (disposing)
            {
                // Dispose loaded game components
                for (int i = 0; i < _components.size(); ++i)
                {
                    AutoCloseable disposable = As.as(_components.get(i), AutoCloseable.class);
                    if (disposable != null)
                        try
                        {
                            disposable.close();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                }
                _components = null;

                if (_content != null)
                {
                    _content.close();
                    _content = null;
                }

                if (_graphicsDeviceManager != null)
                {
                    // NOTE(Eric): Should I use As.as here ?
                    ((GraphicsDeviceManager) _graphicsDeviceManager).close();
                    _graphicsDeviceManager = null;
                }

                if (platform != null)
                {
                    platform.activated.remove(this::onActivated);
                    platform.deactivated.remove(this::onDeactivated);
                    _services.removeService(GamePlatform.class);
// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
                    // Platform.ViewStateChanged -= Platform_ApplicationViewChanged;
// #endif
                    platform.close();
                    platform = null;
                }

                ContentTypeReaderManager.clearTypeCreators();

                SoundEffect.platformShutdown();
            }
// #if ANDROID
            // Activity = null;
// #endif
            _isDisposed = true;
            _instance = null;
        }
    }

    private void assertNotDisposed()
    {
        if (_isDisposed)
        {
            String name = this.getClass().getName();
            throw new IllegalStateException(String.format("The %s object was used after being Disposed.", name));
        }
    }

    // #endregion IDisposable Implementation

    // #region Properties

// #if ANDROID
    // public static AndroidGameActivity Activity { get; internal set; }
// #endif

    private static Game _instance = null;

    public static Game getInstance()
    {
        return Game._instance;
    }

    // TODO(Eric): Doesn't seem to be used anywhere.
    // public LaunchParameters LaunchParameters { get; private set; }

    public GameComponentCollection getComponents()
    {
        return _components;
    }

    /**
     * Gets the time to sleep when the game is inactive.
     * 
     * @return The time to sleep when the game is inactive.
     */
    public TimeSpan getInactiveSleepTime()
    {
        return _inactiveSleepTime;
    }

    /**
     * Sets the time to sleep when the game is inactive.
     * 
     * @param value
     *        The new inactiveSleepTime value.
     * @throws IllegalArgumentException
     *         If the value specified for InactiveSleepTime is not greater than or equal to zero.
     *         Specify zero or a positive value.
     */
    public void setInactiveSleepTime(TimeSpan value) throws IllegalArgumentException
    {
        if (value.getTicks() < TimeSpan.ZERO.getTicks())
            throw new IllegalArgumentException("The time must be positive.");

        _inactiveSleepTime = value;
    }

    /**
     * Returns the maxElapsedTime used by this game.
     * 
     * @return The maxElapsedTime used by this game.
     */
    public TimeSpan getMaxElapsedTime()
    {
        return _maxElapsedTime;
    }

    /** Set the maxElapsedTime to the specified value */
    public void setMaxElapsedTime(TimeSpan value)
    {
        if (value.getTicks() < TimeSpan.ZERO.getTicks())
            throw new IllegalArgumentException("The time must be positive.");
        if (value.getTicks() < _targetElapsedTime.getTicks())
            throw new IllegalArgumentException("The time must be at least targetElapsedTime");

        _maxElapsedTime = value;
    }

    public boolean isActive()
    {
        return platform.isActive();
    }

    /**
     * Returns {@code true} if the mouse should be visible in the game.
     * 
     * @return {@code true} if the mouse cursor should be visible; {@code false} otherwise.
     */
    public boolean isMouseVisible()
    {
        return platform.isMouseVisible();
    }

    /**
     * Set isMouseVisible to the specified value.
     * 
     * @param value
     *        the new value to be assigned to this variable.
     */
    public void setMouseVisible(boolean value)
    {
        platform.setMouseVisible(value);
    }

    public TimeSpan getTargetElapsedTime()
    {
        return _targetElapsedTime;
    }

    public void setTargetElapsedTime(TimeSpan value)
    {
        // Give GamePlatform implementations an opportunity to override
        // the new value.
        value = platform.targetElapsedTimeChanging(value);

        if (value.getTicks() <= TimeSpan.ZERO.getTicks())
            throw new IllegalArgumentException("The time must be positive and non-zero.");

        if (value != _targetElapsedTime)
        {
            _targetElapsedTime = value;
            platform.targetElapsedTimeChanged();
        }
    }

    /**
     * Returns {@code true} if this game uses fixed time step.
     * 
     * @return {@code true} if this game uses fixed time step; {@code false} otherwise.
     */
    public boolean isFixedTimeStep()
    {
        return _isFixedTimeStep;
    }

    /**
     * Set isFixedTimeStep to the specified value.
     * 
     * @param value
     *        the new value to be assigned to this variable.
     */
    public void setIsFixedTimeStep(boolean value)
    {
        _isFixedTimeStep = value;
    }

    public GameServiceContainer getServices()
    {
        return _services;
    }

    public ContentManager getContent()
    {
        return _content;
    }

    public void setContent(ContentManager value)
    {
        if (value == null)
            throw new NullPointerException();

        _content = value;
    }

    public GraphicsDevice getGraphicsDevice()
    {
        if (_graphicsDeviceService == null)
        {
            _graphicsDeviceService = (IGraphicsDeviceService) getServices().getService(IGraphicsDeviceService.class);

            if (_graphicsDeviceService == null)
                throw new NullPointerException("No Graphics Device Service");
        }
        return _graphicsDeviceService.getGraphicsDevice();
    }

    public GameWindow getWindow()
    {
        return platform.getWindow();
    }

    // #endregion Properties

    // #region Internal Properties

    protected boolean isInitialized()
    {
        return _initialized;
    }

    // #endregion Internal Properties

    // #region Events

    public Event<EventArgs> activated = new Event<EventArgs>();
    public Event<EventArgs> deactivated = new Event<EventArgs>();
    public Event<EventArgs> disposed = new Event<EventArgs>();
    public Event<EventArgs> exiting = new Event<EventArgs>();

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
    // public event EventHandler<ViewStateChangedEventArgs> ApplicationViewChanged;
// #endif

// #if WINRT
    // public ApplicationExecutionState PreviousExecutionState { get; internal set; }
// #endif

    // #endregion

    // #region Public Methods

    public void exit()
    {
        _shouldExit = true;
        _suppressDraw = true;
    }

    /**
     * Resets the elapsed time counter.
     * 
     * Use this method if your game is recovering from a slow-running state, and elapsedGameTime is
     * too large to be useful.
     */
    public void resetElapsedTime()
    {
        platform.resetElapsedTime();
        _gameTimer.reset();
        _gameTimer.start();
        _accumulatedElapsedTime.setTimeSpan(TimeSpan.ZERO);
        _gameTime.setElapsedGameTime(TimeSpan.ZERO);
        _previousTicks = 0L;
    }

    /**
     * Prevents calls to draw until the next update.
     * <p>
     * Call this method during update to prevent any calls to draw until after the next call to
     * update. This method can be used on small devices to conserve battery life if the display does
     * not change as a result of update. For example, if the screen is static with no background
     * animations, the player input can be examined during update to determine whether the player is
     * performing any action. If no input is detected, this method allows the game to skip drawing
     * until the next update.
     */
    public void suppressDraw()
    {
        _suppressDraw = true;
    }

    public void runOneFrame()
    {
        if (platform == null)
            return;

        if (!platform.beforeRun())
            return;

        if (!_initialized)
        {
            doInitialize();
            _gameTimer = Stopwatch.startNew();
            _initialized = true;
        }

        beginRun();

        // Not quite right..
        tick();

        endRun();

    }

    public void run()
    {
        run(platform.getDefaultRunBehavior());
    }

    public void run(GameRunBehavior runBehavior)
    {
        assertNotDisposed();
        if (!platform.beforeRun())
        {
            beginRun();
            _gameTimer = Stopwatch.startNew();
            return;
        }

        if (!_initialized)
        {
            doInitialize();
            _initialized = true;
        }

        beginRun();
        _gameTimer = Stopwatch.startNew();
        switch (runBehavior)
        {
            case Asynchronous:
                platform.asyncRunLoopEnded.add(this::platform_AsyncRunLoopEnded);
                platform.startRunLoop();
                break;
            case Synchronous:
                // XNA runs one Update even before showing the window
                doUpdate(new GameTime());

                platform.runLoop();
                endRun();
                doExiting();
                break;
            default:
                throw new IllegalArgumentException(String.format(
                                                                 "Handling for the run behavior %s is not implemented.", runBehavior));
        }
    }

    /** The accumulated elapsed time between each tick */
    private TimeSpan _accumulatedElapsedTime = new TimeSpan();
    /** The object used to hold our elapsed and total game time */
    private GameTime _gameTime = new GameTime();
    /** The timer used to measure elapsed time */
    private Stopwatch _gameTimer;
    private long _previousTicks = 0L;
    /** Holds the number of frame we skipped while we were running too slow */
    private int _updateFrameLag;

    /**
     * Updates the game's clock and calls update and draw.
     * <p>
     * This is the main update method of our game which updates the game logic and draws to the
     * screen. If both of these operations cannot be done in less time than our targetElapsedTime,
     * then we skip draws and only update to catch up.
     * <p>
     * In a fixed-step game, Tick calls Update only after a target time interval has elapsed.
     * <p>
     * In a variable-step game, Update is called every time Tick is called.
     */
    public void tick()
    {
        do
        {
            // Advance the accumulated elapsed time.
            long currentTicks = _gameTimer.getElapsedTicks();
            // TODO(Eric): Do we want to have to handle the exception or not
            try
            {
                _accumulatedElapsedTime.add(TimeSpan.fromTicks(currentTicks - _previousTicks));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            _previousTicks = currentTicks;

            /*
             * If we're in the fixed-time step mode and not enough time has
             * elapsed to perform an update we sleep off the the remaining time
             * to save battery life and/or release CPU time to other threads and
             * processes.
             */
            int sleepTime = 0;
            if (_isFixedTimeStep && _accumulatedElapsedTime.getTicks() < _targetElapsedTime.getTicks())
            {
                try
                {
                    sleepTime = (int) TimeSpan.subtract(_targetElapsedTime, _accumulatedElapsedTime).getTotalMilliseconds();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }

                // NOTE: While sleep can be inaccurate in general it is
                // accurate enough for frame limiting purposes if some
                // fluctuation is an acceptable result.
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                break;
            }
        } while (true);

        // Do not allow any update to take longer than our maximum. This makes
        // sure we draw once every time interval specified by maxElapsedTime.
        if (_accumulatedElapsedTime.getTicks() > _maxElapsedTime.getTicks())
            _accumulatedElapsedTime.setTimeSpan(_maxElapsedTime.getTicks());

        if (_isFixedTimeStep)
        {
            _gameTime.setElapsedGameTime(_targetElapsedTime);
            int stepCount = 0;

            // Perform as many full fixed length time steps as we can.
            while (_accumulatedElapsedTime.getTicks() >= _targetElapsedTime.getTicks() && !_shouldExit)
            {
                try
                {
                    _gameTime.getTotalGameTime().add(_targetElapsedTime);
                    _accumulatedElapsedTime.subtract(_targetElapsedTime);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                ++stepCount;
                doUpdate(_gameTime);
            }

            // Every update after the first accumulates lag
            _updateFrameLag += Math.max(0, stepCount - 1);

            // If we think we are running slowly, wait until the lag clears before resetting it
            if (_gameTime.isRunningSlowly)
            {
                if (_updateFrameLag == 0)
                    _gameTime.isRunningSlowly = false;
            }
            else if (_updateFrameLag >= 5)
            {
                // If we lag more than 5 frames, start thinking we are running slowly
                _gameTime.isRunningSlowly = true;
            }

            // Every time we just do one update and one draw, then we are not running slowly, so
            // decrease the lag
            if (stepCount == 1 && _updateFrameLag > 0)
                --_updateFrameLag;

            // Draw needs to know the total elapsed time that occurred for the fixed length updates.
            _gameTime.setElapsedGameTime(TimeSpan.fromTicks(_targetElapsedTime.getTicks() * stepCount));
        }
        else
        {
            // Perform a single variable length update.
            _gameTime.setElapsedGameTime(_accumulatedElapsedTime);
            try
            {
                _gameTime.getTotalGameTime().add(_accumulatedElapsedTime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            _accumulatedElapsedTime.setTimeSpan(TimeSpan.ZERO);

            doUpdate(_gameTime);
        }

        // Draw unless the update suppressed it.
        if (_suppressDraw)
        {
            _suppressDraw = false;
        }
        else
        {
            doDraw(_gameTime);
        }

        if (_shouldExit)
            platform.exit();
    }

    // #endregion

    // #region Protected Methods

    protected boolean beginDraw()
    {
        return true;
    }

    /**
     * Disposes of this graphics context and releases any system resources that it is using and
     * makes the next available buffer visible.
     */
    protected void endDraw()
    {
        platform.present();
    }

    protected void beginRun()
    {}

    protected void endRun()
    {}

    /**
     * LoadContent will be called once per game and is the place to load all of your content.
     */
    protected void loadContent()
    {}

    /**
     * Unload any unmanaged content here
     */
    protected void unloadContent()
    {}

    protected void initialize()
    {
        // TODO: This should be removed once all platforms use the new GraphicsDeviceManager
        applyChanges(getGraphicsDeviceManager());

        // According to the information given on MSDN (see link below), all
        // GameComponents in Components at the time Initialize() is called
        // are initialized.
        // http://msdn.microsoft.com/en-us/library/microsoft.xna.framework.game.initialize.aspx
        // Initialize all existing components
        initializeExistingComponents();

        _graphicsDeviceService = (IGraphicsDeviceService) getServices().getService(IGraphicsDeviceService.class);

        if (_graphicsDeviceService != null && _graphicsDeviceService.getGraphicsDevice() != null)
        {
            loadContent();
        }
    }

    private static BiConsumer<IDrawable, GameTime> drawAction = (drawable, gameTime) -> drawable.draw(gameTime);

    /**
     * Called when the game determines it is time to draw a frame. Override this method with
     * game-specific rendering code.
     * <p>
     * Update and draw are called at different rates depending on whether isFixedTimeStep is
     * {@code true} or {@code false}. If isFixedTimeStep is {@code false}, update and draw will be
     * called in a continuous loop, sequentially as often as possible.. If isFixedTimeStep is
     * {@code true}, update will be called at the interval specified in targetElapsedTime, while
     * draw will only be called if an update is not due. If draw is not called, isRunningSlowly will
     * be set to {@code true}.
     */
    protected void draw(GameTime gameTime)
    {
        _drawables.forEachFilteredItem(drawAction, gameTime);
    }

    private static BiConsumer<IUpdateable, GameTime> updateAction = (updateable, gameTime) -> updateable.update(gameTime);

    /**
     * Called when the game has determined that game logic needs to be processed. This might include
     * the management of the game state, the processing of user input, or the updating of simulation
     * data. Override this method with game-specific logic.
     * <p>
     * update and draw are called at different rates depending on whether isFixedTimeStep is
     * {@code true} or {@code false}. If isFixedTimeStep is {@code false}, update and draw will be
     * called in a continuous loop, sequentially as often as possible.. If isFixedTimeStep is
     * {@code true}, update will be called at the interval specified in targetElapsedTime, while
     * draw will only be called if an update is not due. If draw is not called, isRunningSlowly will
     * be set to {@code true}.
     */
    protected void update(GameTime gameTime)
    {
        _updateables.forEachFilteredItem(updateAction, gameTime);
    }

    protected void onExiting(Object sender, EventArgs args)
    {
        raise(exiting, args);
    }

    protected void onActivated(Object sender, EventArgs args)
    {
        assertNotDisposed();
        raise(activated, args);
    }

    protected void onDeactivated(Object sender, EventArgs args)
    {
        assertNotDisposed();
        raise(deactivated, args);
    }

    // #endregion Protected Methods

    // #region Event Handlers

    private void components_ComponentAdded(Object sender, GameComponentCollectionEventArgs e)
    {
        // Since we only subscribe to ComponentAdded after the graphics
        // devices are set up, it is safe to just blindly call Initialize.
        e.getGameComponent().initialize();
        categorizeComponent(e.getGameComponent());
    }

    private void components_ComponentRemoved(Object sender, GameComponentCollectionEventArgs e)
    {
        decategorizeComponent(e.getGameComponent());
    }

    private void platform_AsyncRunLoopEnded(Object sender, EventArgs e)
    {
        assertNotDisposed();

        GamePlatform platform = (GamePlatform) sender;
        platform.asyncRunLoopEnded.remove(this::platform_AsyncRunLoopEnded);
        endRun();
        doExiting();
    }

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
    // private void Platform_ApplicationViewChanged(object sender, ViewStateChangedEventArgs e) {
    // AssertNotDisposed();
    // Raise(ApplicationViewChanged, e);
    // }
// #endif

    // #endregion Event Handlers

    // #region Internal Methods

    protected void applyChanges(GraphicsDeviceManager manager)
    {
        platform.beginScreenDeviceChange(getGraphicsDevice().getPresentationParameters().isFullScreen());

// #if !(WINDOWS && DIRECTX)
        if (getGraphicsDevice().getPresentationParameters().isFullScreen())
            platform.enterFullScreen();
        else
            platform.exitFullScreen();
// #endif
        Viewport viewport = new Viewport(0, 0,
                                         getGraphicsDevice().getPresentationParameters().getBackBufferWidth(),
                                         getGraphicsDevice().getPresentationParameters().getBackBufferHeight());

        getGraphicsDevice().setViewport(viewport);
        platform.endScreenDeviceChange("", viewport.getWidth(), viewport.getHeight());
    }

    protected void doUpdate(GameTime gameTime)
    {
        assertNotDisposed();
        if (platform.beforeUpdate(gameTime))
        {
            FrameworkDispatcher.update();

            update(gameTime);

            // The TouchPanel needs to know the time for when touches arrive
            // TouchPanelState.CurrentTimestamp = gameTime.TotalGameTime;
        }
    }

    protected void doDraw(GameTime gameTime)
    {
        assertNotDisposed();
        // Draw and EndDraw should not be called if BeginDraw returns false.
        // http://stackoverflow.com/questions/4054936/manual-control-over-when-to-redraw-the-screen/4057180#4057180
        // http://stackoverflow.com/questions/4235439/xna-3-1-to-4-0-requires-constant-redraw-or-will-display-a-purple-screen
        if (platform.beforeDraw(gameTime) && beginDraw())
        {
            draw(gameTime);
            endDraw();
        }
    }

    protected void doInitialize()
    {
        assertNotDisposed();
        if (getGraphicsDevice() == null && getGraphicsDeviceManager() != null)
            _graphicsDeviceManager.createDevice();

        platform.beforeInitialize();
        initialize();

        // We need to do this after virtual Initialize(...) is called.
        // 1. Categorize components into IUpdateable and IDrawable lists.
        // 2. Subscribe to Added/Removed events to keep the categorized
        // lists synced and to Initialize future components as they are
        // added.
        categorizeComponents();
        _components.componentAdded.add(this::components_ComponentAdded);
        _components.componentRemoved.add(this::components_ComponentRemoved);
    }

    protected void doExiting()
    {
        onExiting(this, EventArgs.Empty);
        unloadContent();
    }

    // #endregion Internal Methods

    protected GraphicsDeviceManager getGraphicsDeviceManager()
    {
        if (_graphicsDeviceManager == null)
        {
            _graphicsDeviceManager = (IGraphicsDeviceManager) getServices().getService(IGraphicsDeviceManager.class);
        }
        return (GraphicsDeviceManager) _graphicsDeviceManager;
    }

    // NOTE: InitializeExistingComponents really should only be called once.
    // Game.Initialize is the only method in a position to guarantee
    // that no component will get a duplicate Initialize call.
    // Further calls to Initialize occur immediately in response to
    // Components.ComponentAdded.
    private void initializeExistingComponents()
    {
        for (int i = 0; i < getComponents().size(); ++i)
            getComponents().get(i).initialize();
    }

    private void categorizeComponents()
    {
        decategorizeComponents();
        for (int i = 0; i < getComponents().size(); ++i)
        {
            categorizeComponent(getComponents().get(i));
        }
    }

    // FIXME: I am open to a better name for this method. It does the
    // opposite of CategorizeComponents.
    private void decategorizeComponents()
    {
        _updateables.clear();
        _drawables.clear();
    }

    private void categorizeComponent(IGameComponent component)
    {
        if (component instanceof IUpdateable)
            _updateables.add((IUpdateable) component);
        if (component instanceof IDrawable)
            _drawables.add((IDrawable) component);
    }

    // FIXME: I am open to a better name for this method. It does the
    // opposite of CategorizeComponent.
    private void decategorizeComponent(IGameComponent component)
    {
        if (component instanceof IUpdateable)
            _updateables.remove((IUpdateable) component);
        if (component instanceof IDrawable)
            _drawables.remove((IDrawable) component);
    }

    private <TEventArgs extends EventArgs> void raise(EventHandler<TEventArgs> handler, TEventArgs e)
    {
        if (handler != null)
            handler.handleEvent(this, e);
    }

}

// TODO(Eric): Do I keep the SortingFilteringCollection class in its own file ?
// TODO(Eric): validate SortingFilteringCollection
// TODO(Eric): refactor all setIsSomething to setSomething

// TODO(Eric): Finish comments
// TODO(Eric): Create ArgumentOutOfRangeException ? (See RangeExcpetion and OutOfRangeException)
