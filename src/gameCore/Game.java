package gameCore;

import gameCore.collections.SortingFilteringCollection;
import gameCore.components.GameComponentCollection;
import gameCore.components.GameComponentCollectionEventArgs;
import gameCore.components.IDrawable;
import gameCore.components.IGameComponent;
import gameCore.components.IUpdateable;
import gameCore.content.ContentManager;
import gameCore.content.ContentTypeReaderManager;
import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.IGraphicsDeviceService;
import gameCore.graphics.Viewport;
import gameCore.input.KeyboardRawInput;
import gameCore.input.Mouse;
import gameCore.time.GameTime;
import gameCore.time.Stopwatch;
import gameCore.time.TimeSpan;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * Provides basic graphics device initialization, game logic, and rendering code.
 * 
 * <p>
 * This class contains the core of the game and shoudldn't be exposed to the client. The game logic
 * goes into Game which inherits from this class.
 * 
 * <p>
 * The mouse and it's associated listeners are handled here, but everything related to the keyboard
 * or other inputs have to be handled in the child class.
 */
public abstract class Game extends Canvas implements Runnable, AutoCloseable
{

	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	private GameComponentCollection _components;
	private GameServiceContainer _services;
	private ContentManager _content;
	protected GamePlatform platform; // not useful since java is crossplatform ?

	// TODO: Validate arguments (Events)
	// TODO: Should I go back to the other way I was doing ?
	// I had to change IDrawable to an abstract class.
	private SortingFilteringCollection<IDrawable> _drawables =
			new SortingFilteringCollection<IDrawable>(
					d -> d.isVisible(),
					// (d, handler) -> d.visibleChanged(handler),
					// (d, handler) -> d.visibleChanged(handler),
					(d, handler) -> d.visibleChanged = handler,
					(d, handler) -> d.visibleChanged = handler,
					new Comparator<IDrawable>() {

						@Override
						public int compare(IDrawable d1, IDrawable d2)
						{
							return d1.getDrawOrder() - d2.getDrawOrder();
						}
					},
					// (d, handler) -> d.drawOrderChanged(handler),
					// (d, handler) -> d.drawOrderChanged(handler),
					(d, handler) -> d.drawOrderChanged = handler,
					(d, handler) -> d.drawOrderChanged = handler);
	/*
	 * private SortingFilteringCollection<IDrawable> _drawables =
	 * new SortingFilteringCollection<IDrawable>(
	 * d => d.Visible,
	 * (d, handler) => d.VisibleChanged += handler,
	 * (d, handler) => d.VisibleChanged -= handler,
	 * (d1 ,d2) => Comparer<int>.Default.Compare(d1.DrawOrder, d2.DrawOrder),
	 * (d, handler) => d.DrawOrderChanged += handler,
	 * (d, handler) => d.DrawOrderChanged -= handler);
	 */

	// TODO: Validate arguments (Events)
	private SortingFilteringCollection<IUpdateable> _updateables =
			new SortingFilteringCollection<IUpdateable>(
					u -> u.isEnabled(),
					// (u, handler) -> u.enabledChanged(handler),
					// (u, handler) -> u.enabledChanged(handler),
					(u, handler) -> u.enabledChanged = handler,
					(u, handler) -> u.enabledChanged = handler,
					new Comparator<IUpdateable>() {

						@Override
						public int compare(IUpdateable u1, IUpdateable u2)
						{
							return u1.getUpdateOrder() - u2.getUpdateOrder();
						}
					},
					// (u, handler) -> u.updateOrderChanged(handler, null),
					// (u, handler) -> u.updateOrderChanged(handler, null)
					(u, handler) -> u.updateOrderChanged = handler,
					(u, handler) -> u.updateOrderChanged = handler);

	/*
	 * private SortingFilteringCollection<IUpdateable> _updateables =
	 * new SortingFilteringCollection<IUpdateable>(
	 * u => u.Enabled,
	 * (u, handler) => u.EnabledChanged += handler,
	 * (u, handler) => u.EnabledChanged -= handler,
	 * (u1, u2) => Comparer<int>.Default.Compare(u1.UpdateOrder, u2.UpdateOrder),
	 * (u, handler) => u.UpdateOrderChanged += handler,
	 * (u, handler) => u.UpdateOrderChanged -= handler);
	 */

	private IGraphicsDeviceManager _graphicsDeviceManager;
	private IGraphicsDeviceService _graphicsDeviceService;

	private boolean _initialized = false;

	/**
	 * Value indicating whether to use fixed time steps, {@code true} by
	 * default.
	 * 
	 * A fixed-step Game tries to call its update method on the fixed interval
	 * specified in targetElapsedTime. Setting Game.isFixedTimeStep to {@code true} causes a Game to
	 * use a fixed-step game loop. A new project
	 * uses a fixed-step game loop with a default targetElapsedTime of 1/60th of
	 * a second.
	 * 
	 * In a fixed-step game loop, Game calls update once the targetElapsedTime
	 * has elapsed. After update is called, if it is not time to call update
	 * again, Game calls Draw. After Draw is called, if it is not time to call
	 * update again, Game idles until it is time to call update.
	 * 
	 * If update takes too long to process, Game sets isRunningSlowly to {@code true} and calls
	 * update again, without calling Draw in between.
	 * When an update runs longer than the targetElapsedTime, Game responds by
	 * calling update extra times and dropping the frames associated with those
	 * updates to catch up. This ensures that update will have been called the
	 * expected number of times when the game loop catches up from a slowdown.
	 * You can check the value of isRunningSlowly in your update if you want to
	 * detect dropped frames and shorten your update processing to compensate.
	 * You can reset the elapsed times by calling resetElapsedTime.
	 */
	private boolean _isFixedTimeStep = true;

	/**
	 * Target time between calls to {@code update} when {@code isFixedTimeStep} is {@code true}.
	 * 
	 * When the game frame rate is less than targetElapsedTime, isRunningSlowly
	 * will return {@code true}.
	 * 
	 * The default value for targetElapsedTime is 1/60th of a second.
	 * 
	 * A fixed-step Game tries to call its update method on the fixed interval
	 * specified in targetElapsedTime. Setting Game.isFixedTimeStep to {@code true} causes a Game to
	 * use a fixed-step game loop. A new project
	 * uses a fixed-step game loop with a default targetElapsedTime of 1/60th of
	 * a second.
	 * 
	 * In a fixed-step game loop, Game calls update once the targetElapsedTime
	 * has elapsed. After update is called, if it is not time to call update
	 * again, Game calls Draw. After Draw is called, if it is not time to call
	 * update again, Game idles until it is time to call update.
	 * 
	 * If update takes too long to process, Game sets isRunningSlowly to {@code true} and calls
	 * update again, without calling Draw in between.
	 * When an update
	 * runs longer than the targetElapsedTime, Game responds by calling update
	 * extra times and dropping the frames associated with those updates to
	 * catch up. This ensures that update will have been called the expected
	 * number of times when the game loop catches up from a slowdown. You can
	 * check the value of isRunningSlowly in your update if you want to detect
	 * dropped frames and shorten your update processing to compensate. You can
	 * reset the elapsed times by calling resetElapsedTime.
	 */
	private TimeSpan _targetElapsedTime = TimeSpan.fromTicks(166667); // 60fps

	// TODO: I think this is not used anywhere (plus getter and setter)
	/**
	 * The time to sleep when the game is inactive.
	 */
	private TimeSpan _inactiveSleepTime = TimeSpan.fromSeconds(0.02);

	// TODO: Complete comments
	/**
	 * Specifies the maximum length we will be updating the game before calling
	 * the {@code draw()} method. The default value is 500 milliseconds.
	 * 
	 * If something outside update is taking too much time, we only catch up to
	 * this maximum value. This makes sure we will call the {@code draw()} method at least once
	 * every time interval specified by this variable.
	 */
	private TimeSpan _maxElapsedTime = TimeSpan.fromMilliseconds(500);

	/**
	 * Value indicating whether to skip the call to draw until the next update.
	 * Default value is set to {@code false}
	 */
	private boolean _suppressDraw = false;

	/**
	 * Initializes a new instance of this class with the default screenWidth,
	 * screenHeight and title, which provides basic graphics device
	 * initialization, game logic, rendering code, and a game loop.
	 */
	public Game()
	{
		_instance = this;

		// TODO: This doesn't seem to be used anywhere
		// LaunchParameters = new LaunchParameters();
		_services = new GameServiceContainer();
		_components = new GameComponentCollection();
		_content = new ContentManager(_services);

		platform = GamePlatform.create(this);
		// Platform.Activated += OnActivated;
		// Platform.Deactivated += OnDeactivated;
		platform.activated = this::onActivated;
		platform.deactivated = this::onDeactivated;
		_services.addService(GamePlatform.class, platform);

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
		// Platform.ViewStateChanged += Platform_ApplicationViewChanged;
// #endif

		// TODO: This is all added by me.
		_accumulatedElapsedTime = new TimeSpan(0);

		image = new BufferedImage(GraphicsDeviceManager.DefaultBackBufferWidth,
				GraphicsDeviceManager.DefaultBackBufferHeight, BufferedImage.TYPE_INT_RGB);

		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		KeyboardRawInput keyboard = new KeyboardRawInput();
		addKeyListener(keyboard);
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
					AutoCloseable disposable = (AutoCloseable) _components.get(i);
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
					((GraphicsDeviceManager) _graphicsDeviceManager).close();
					_graphicsDeviceManager = null;
				}

				if (platform != null)
				{
					// Platform.Activated -= OnActivated;
					// Platform.Deactivated -= OnDeactivated;
					_services.removeService(GamePlatform.class);
// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
					// Platform.ViewStateChanged -= Platform_ApplicationViewChanged;
// #endif
					platform.close();
					platform = null;
				}

				ContentTypeReaderManager.clearTypeCreators();

				// SoundEffect.PlatformShutdown();
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

	private static Game _instance = null;

	public static Game getInstance()
	{
		return Game._instance;
	}

	// TODO: Doesn't seem to be used anywhere.
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
	 *         If the value specified for InactiveSleepTime is not greater
	 *         than or equal to zero. Specify zero or a positive value.
	 */
	public void setInactiveSleepTime(TimeSpan value) throws IllegalArgumentException
	{
		if (value.getTicks() < TimeSpan.ZERO.getTicks())
			throw new IllegalArgumentException("The time must be positive.");

		_inactiveSleepTime = value;
	}

	/** Returns the maxElapsedTime used by this game */
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

	// NOTE: GraphicsDevice gets created from platform.beforeInitialize();
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

	public boolean isInitialized()
	{
		return _initialized;
	}

	public EventHandler<EventArgs> activated;
	public EventHandler<EventArgs> deactivated;
	public EventHandler<EventArgs> disposed;
	public EventHandler<EventArgs> exiting;

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
	// public event EventHandler<ViewStateChangedEventArgs> ApplicationViewChanged;
// #endif

// #if WINRT
	// public ApplicationExecutionState PreviousExecutionState { get; internal set; }
// #endif

	public void exit()
	{
		platform.exit();
		_suppressDraw = true;
	}

	/**
	 * Resets the elapsed time counter.
	 * 
	 * Use this method if your game is recovering from a slow-running state, and
	 * elapsedGameTime is too large to be useful.
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
	 * 
	 * Call this method during update to prevent any calls to draw until after
	 * the next call to update. This method can be used on small devices to
	 * conserve battery life if the display does not change as a result of
	 * update. For example, if the screen is static with no background
	 * animations, the player input can be examined during update to determine
	 * whether the player is performing any action. If no input is detected,
	 * this method allows the game to skip drawing until the next update.
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
				// platform.asyncRunLoopEnded += Platform_AsyncRunLoopEnded;
				platform.asyncRunLoopEnded = this::platform_AsyncRunLoopEnded;
				platform.startRunLoop();
				break;
			case Synchronous:
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
	private TimeSpan _accumulatedElapsedTime;
	/** The object used to hold our elapsed and total game time */
	private GameTime _gameTime = new GameTime();
	/** The timer used to measure elapsed time */
	private Stopwatch _gameTimer;
	private long _previousTicks = 0L;
	/** Holds the number of frame we skipped while we were running too slow */
	private int _updateFrameLag;

	/**
	 * Updates the game's clock and calls update and draw.
	 * 
	 * This is the main update method of our game which updates the game logic
	 * and draws to the screen. If both of these operations cannot be done in
	 * less time than our targetElapsedTime, then we skip draws and only update
	 * to catch up.
	 * 
	 * In a fixed-step game, Tick calls Update only after a target time interval
	 * has elapsed.
	 * 
	 * In a variable-step game, Update is called every time Tick is called.
	 */
	public void tick()
	{
		do
		{
			// Advance the accumulated elapsed time.
			long currentTicks = _gameTimer.getElapsedTicks();
			// TODO: Do we want to have to handle the exception or not
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
			if (_isFixedTimeStep && _accumulatedElapsedTime.getTicks() < _targetElapsedTime.getTicks())
			{
				try
				{
					// TODO: Delete if working
					// delta.setTimeSpan((TimeSpan.subtract(_targetElapsedTime,
					// _accumulatedElapsedTime)).getTicks());
					delta = TimeSpan.subtract(_targetElapsedTime, _accumulatedElapsedTime);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				int sleepTime = (int) delta.getTotalMilliseconds();

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
			while (_accumulatedElapsedTime.getTicks() >= _targetElapsedTime.getTicks())
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
			if (_gameTime.isRunningSlowly())
			{
				if (_updateFrameLag == 0)
					_gameTime.setIsRunningSlowly(false);
			}
			else if (_updateFrameLag >= 5)
			{
				// If we lag more than 5 frames, start thinking we are running slowly
				_gameTime.setIsRunningSlowly(true);
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
	}

	protected boolean beginDraw()
	{
		return true;
	}

	/**
	 * Disposes of this graphics context and releases any system resources that
	 * it is using and makes the next available buffer visible.
	 */
	protected void endDraw()
	{
		platform.present();
		// TODO: Added this, but is it in the right place ?
		// Maybe platform.present()
		g.dispose();
		buffStrat.show();
	}

	protected void beginRun() {}

	protected void endRun() {}

	/**
	 * LoadContent will be called once per game and is the place to load all of
	 * your content.
	 */
	protected void loadContent() {}

	/**
	 * Unload any unmanaged content here
	 */
	protected void unloadContent() {}

	protected void initialize()
	{
		// TODO: Check if I want to keep all of this here.
		// Should probably go in JavaGameWindow
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		// Initialize the buffer strategy
		createBufferStrategy(3);
		buffStrat = getBufferStrategy();
		g = buffStrat.getDrawGraphics();

		// TODO: We shouldn't need to do this here.
		applyChanges(getGraphicsDeviceManager());

		// According to the information given on MSDN (see link below), all
		// GameComponents in Components at the time Initialize() is called
		// are initialized.
		// http://msdn.microsoft.com/en-us/library/microsoft.xna.framework.game.initialize.aspx
		// Initialize all existing components
		initializeExistingComponents();

		_graphicsDeviceService = (IGraphicsDeviceService) getServices().getService(IGraphicsDeviceService.class);

		// FIXME: If this test fails, is LoadContent ever called? This
		// seems like a condition that warrants an exception more
		// than a silent failure.
		if (_graphicsDeviceService != null && _graphicsDeviceService.getGraphicsDevice() != null)
		{
			loadContent();
		}
	}

	// private static Action<IDrawable, GameTime> DrawAction = (drawable, gameTime) =>
	// drawable.Draw(gameTime);
	private static BiConsumer<IDrawable, GameTime> drawAction = (drawable, gameTime) -> drawable.draw(gameTime);

	/**
	 * Called when the game determines it is time to draw a frame. Override this
	 * method with game-specific rendering code.
	 * 
	 * <p>
	 * update and draw are called at different rates depending on whether isFixedTimeStep is
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

	// private static Action<IUpdateable, GameTime> UpdateAction = (updateable, gameTime) =>
	// updateable.Update(gameTime);
	private static BiConsumer<IUpdateable, GameTime> updateAction = (updateable, gameTime) -> updateable
			.update(gameTime);

	/**
	 * Called when the game has determined that game logic needs to be
	 * processed. This might include the management of the game state, the
	 * processing of user input, or the updating of simulation data. Override
	 * this method with game-specific logic.
	 * 
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

	// TODO: Proper handling of events
	private void platform_AsyncRunLoopEnded(Object sender, EventArgs e)
	{
		assertNotDisposed();

		// GamePlatform platform = (GamePlatform) sender;
		// platform.asyncRunLoopEnded -= Platform_AsyncRunLoopEnded;
		endRun();
		doExiting();
	}

// #if WINDOWS_STOREAPP && !WINDOWS_PHONE81
	// private void Platform_ApplicationViewChanged(object sender, ViewStateChangedEventArgs e) {
	// AssertNotDisposed();
	// Raise(ApplicationViewChanged, e);
	// }
// #endif

	protected void applyChanges(GraphicsDeviceManager manager)
	{
		platform.beginScreenDeviceChange(getGraphicsDevice().getPresentationParameters().isFullScreen());
		if (getGraphicsDevice().getPresentationParameters().isFullScreen())
			platform.enterFullScreen();
		else
			platform.exitFullScreen();

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
			// Once per frame, we need to check currently
			// playing sounds to see if they've stopped,
			// and return them back to the pool if so.
			// SoundEffectInstancePool;

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
			// TODO: Should this be in the JavaGamePlatform or JavaGameWindow ?
			// Maybe platform.present()
			drawImageToScreen();
			endDraw();
		}
	}

	protected void doInitialize()
	{
		assertNotDisposed();
		platform.beforeInitialize();
		initialize();

		// We need to do this after virtual Initialize(...) is called.
		// 1. Categorize components into IUpdateable and IDrawable lists.
		// 2. Subscribe to Added/Removed events to keep the categorized
		// lists synced and to Initialize future components as they are
		// added.
		categorizeComponents();
		// TODO: Proper event handling
		// _components.ComponentAdded += Components_ComponentAdded;
		// _components.ComponentRemoved += Components_ComponentRemoved;
		_components.componentAdded = this::components_ComponentAdded;
		_components.componentRemoved = this::components_ComponentRemoved;
	}

	protected void doExiting()
	{
		onExiting(this, EventArgs.Empty);
		unloadContent();
	}

	protected GraphicsDeviceManager getGraphicsDeviceManager()
	{
		if (_graphicsDeviceManager == null)
		{
			_graphicsDeviceManager = (IGraphicsDeviceManager) getServices().getService(IGraphicsDeviceManager.class);

			if (_graphicsDeviceManager == null)
				throw new NullPointerException("No Graphics Device Manager");
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
		// TODO: Would be nice to get rid of this copy, but since it only
		// happens once per game, it's fairly low priority.
		IGameComponent[] copy = new IGameComponent[getComponents().size()];

		getComponents().toArray(copy);
		for (IGameComponent component : copy)
			component.initialize();
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
			handler.accept(this, e);
	}

	// Stuff that I added
	// TODO: Delete these at some point

	// TODO: Should I use this in JavaGameWindow in the while loop ?
	/** True if the game is running */
	// private boolean isRunning;

	// TODO: All this should probably be added to the JavaGameWindow or some sort of
	// SoftRenderedPlatform
	/** The object used to draw the image in the frame */
	private BufferedImage image;
	/** The BufferStrategy used in this game */
	private BufferStrategy buffStrat;
	/** The graphic context used to draw onto the component */
	protected Graphics g;
	/** The array of pixels to be painted on the screen */
	private int[] pixels;

	// Holds the delta between targetElapsedTime and accumulatedElapasedTime
	private TimeSpan delta = new TimeSpan(0);

	/** Draws the image from the buffer to the screen */
	private void drawImageToScreen()
	{
		buffStrat = getBufferStrategy();
		if (buffStrat == null)
		{
			createBufferStrategy(3);
			return;
		}

		for (int i = 0; i < pixels.length; ++i)
		{
			pixels[i] = getGraphicsDevice().pixels[i];
		}

		g = buffStrat.getDrawGraphics();
		g.drawImage(image, 0, 0, getGraphicsDeviceManager().getPreferredBackBufferWidth(), getGraphicsDeviceManager()
				.getPreferredBackBufferHeight(), null);
	}
}

// TODO : Do I keep the SortingFilteringCollection class in its own file ?
// TODO: validate SortingFilteringCollection
// TODO: refactor all setIsSomething to setSomething
// TODO: Search for equivalent of GC.SuppressFinalize(this)

// TODO: Get rid of getters and setters in some classes (Vectors and others)
// and make the members public instead. This will make the code easier
// to use. Will have to refactor a lot of code.

// TODO: Handle isMouseVisible
// TODO: Finish comments (getters and setters, etc)
// TODO: Create ArgumentOutOfRangeException ? (See RangeExcpetion and OutOfRangeException)
