package jMono_Framework;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.dotNet.events.EventHandler;
import jMono_Framework.graphics.GraphicsAdapter;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsProfile;
import jMono_Framework.graphics.IGraphicsDeviceService;
import jMono_Framework.graphics.NoSuitableGraphicsDeviceException;
import jMono_Framework.graphics.PresentInterval;
import jMono_Framework.graphics.PresentationParameters;
import jMono_Framework.graphics.SurfaceFormat;
import jMono_Framework.graphics.states.DepthFormat;

/**
 * Used to initialize and control the presentation of the graphics device.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class GraphicsDeviceManager implements IGraphicsDeviceService, IGraphicsDeviceManager, AutoCloseable
{
    private final Game _game;
    private GraphicsDevice _graphicsDevice;
    private boolean _initialized = false;

    private int _preferredBackBufferHeight;
    private int _preferredBackBufferWidth;
    private SurfaceFormat _preferredBackBufferFormat;
    private DepthFormat _preferredDepthStencilFormat;
    private boolean _preferMultiSampling;
    private DisplayOrientation _supportedOrientations;
    private boolean _synchronizedWithVerticalRetrace = true;
    private boolean _drawBegun;
    private boolean _disposed;
    private boolean _hardwareModeSwitch = true;
    private boolean _wantFullScreen;
    private GraphicsProfile _graphicsProfile;
    // dirty flag for ApplyChanges
    private boolean _shouldApplyChanges;

    /**
     * The default back buffer width.
     */
    public static final int DefaultBackBufferWidth = 800;

    /**
     * The default back buffer height.
     */
    public static final int DefaultBackBufferHeight = 480;

    /***
     * Optional override for platform specific defaults.
     */
    // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
    // partial void platformConstruct();

    /**
     * Associates this graphics device manager to a game instances.
     * 
     * @param game
     *        The game instance to attach.
     */
    public GraphicsDeviceManager(Game game)
    {
        if (game == null)
            throw new NullPointerException("The game cannot be null!");

        _game = game;

        _supportedOrientations = DisplayOrientation.Default;
        _preferredBackBufferFormat = SurfaceFormat.Color;
        _preferredDepthStencilFormat = DepthFormat.Depth24;
        _synchronizedWithVerticalRetrace = true;

        // Assume the window client size as the default back
        // buffer resolution in the landscape orientation.
        Rectangle clientBounds = _game.getWindow().getClientBounds();
        if (clientBounds.width >= clientBounds.height)
        {
            _preferredBackBufferWidth = clientBounds.width;
            _preferredBackBufferHeight = clientBounds.height;
        }
        else
        {
            _preferredBackBufferWidth = clientBounds.height;
            _preferredBackBufferHeight = clientBounds.width;
        }

        // Default to windowed mode... this is ignored on platforms that don't support it.
        _wantFullScreen = false;

        // XNA would read this from the manifest, but it would always default
        // to reach unless changed. So lets mimic that without the manifest bit.
        setGraphicsProfile(GraphicsProfile.Reach);

        // Let the platform optionally overload construction defaults.
        // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
        // platformConstruct();

        if (_game.getServices().getService(IGraphicsDeviceManager.class) != null)
            throw new IllegalArgumentException("A graphics device manager is already registered.  The graphics device manager cannot be changed once it is set.");

        _game.getServices().addService(IGraphicsDeviceManager.class, this);
        _game.getServices().addService(IGraphicsDeviceService.class, this);
    }

    @Override
    public void finalize()
    {
        dispose(false);
    }

    @Override
    public void createDevice()
    {
        if (_graphicsDevice != null)
            return;

        try
        {
            if (!_initialized)
                initialize();

            GraphicsDeviceInformation gdi = doPreparingDeviceSettings();
            createDevice(gdi);
        }
        catch (NoSuitableGraphicsDeviceException e)
        {
            throw e;
        }
        catch (Exception ex)
        {
            throw new NoSuitableGraphicsDeviceException("Failed to create graphics device!", ex);
        }
    }

    private void createDevice(GraphicsDeviceInformation gdi) throws NoSuitableGraphicsDeviceException
    {
        if (_graphicsDevice != null)
            return;

        _graphicsDevice = new GraphicsDevice(gdi);
        _shouldApplyChanges = false;

        // hook up reset events
        getGraphicsDevice().deviceReset.add((sender, args) -> onDeviceReset(args));
        getGraphicsDevice().deviceResetting.add((sender, args) -> onDeviceResetting(args));

        // update the touch panel display size when the graphics device is reset
        _graphicsDevice.deviceReset.add(this::updateTouchPanel);
        _graphicsDevice.presentationChanged.add(this::onPresentationChanged);

        onDeviceCreated(EventArgs.Empty);
    }

    // TODO(Eric): Need to look at this in MonoGame / C#
    /*
     * void IGraphicsDeviceManager.createDevice()
     * {
     * createDevice();
     * }
     */

    @Override
    public boolean beginDraw()
    {
        if (_graphicsDevice == null)
            return false;

        _drawBegun = true;
        return true;
    }

    @Override
    public void endDraw()
    {
        if (_graphicsDevice != null && _drawBegun)
        {
            _drawBegun = false;
            _graphicsDevice.present();
        }
    }

    // #region IGraphicsDeviceService Members

    public Event<EventArgs> deviceCreated = new Event<EventArgs>();
    public Event<EventArgs> deviceDisposing = new Event<EventArgs>();
    public Event<EventArgs> deviceReset = new Event<EventArgs>();
    public Event<EventArgs> deviceResetting = new Event<EventArgs>();
    public Event<PreparingDeviceSettingsEventArgs> preparingDeviceSettings = new Event<PreparingDeviceSettingsEventArgs>();
    public Event<EventArgs> disposed = new Event<EventArgs>();

    // NOTE(Eric): This is form IGraphicsDeviceService and is how we make sure we have all the
    // events handler
    @Override
    public Event<EventArgs> getDeviceCreated()
    {
        return deviceCreated;
    }

    @Override
    public Event<EventArgs> getDeviceDisposing()
    {
        return deviceDisposing;
    }

    @Override
    public Event<EventArgs> getDeviceReset()
    {
        return deviceReset;
    }

    @Override
    public Event<EventArgs> getDeviceResetting()
    {
        return deviceResetting;
    }

    protected void onDeviceDisposing(EventArgs e)
    {
        raise(deviceDisposing, e);
    }

    protected void onDeviceResetting(EventArgs e)
    {
        raise(deviceResetting, e);
    }

    protected void onDeviceReset(EventArgs e)
    {
        raise(deviceReset, e);
    }

    protected void onDeviceCreated(EventArgs e)
    {
        raise(deviceCreated, e);
    }

    /**
     * This populates a GraphicsDeviceInformation instance and invokes PreparingDeviceSettings to
     * allow users to change the settings. Then returns that GraphicsDeviceInformation.
     * Throws NullReferenceException if users set GraphicsDeviceInformation.PresentationParameters
     * to null.
     * 
     * @return
     */
    private GraphicsDeviceInformation doPreparingDeviceSettings()
    {
        GraphicsDeviceInformation gdi = new GraphicsDeviceInformation();
        prepareGraphicsDeviceInformation(gdi);

        if (preparingDeviceSettings != null)
        {
            // this allows users to overwrite settings through the argument
            PreparingDeviceSettingsEventArgs args = new PreparingDeviceSettingsEventArgs(gdi);
            preparingDeviceSettings.handleEvent(this, args);

            if (gdi.presentationParameters == null || gdi.adapter == null)
                throw new NullPointerException("Members should not be set to null in PreparingDeviceSettingsEventArgs");

            if (gdi.presentationParameters.getMultiSampleCount() > 0)
            {
                // Round down MultiSampleCount to the nearest power of two
                // hack from http://stackoverflow.com/a/2681094
                // Note: this will return an incorrect, but large value
                // for very large numbers. That doesn't matter because
                // the number will get clamped below anyway in this case.
                int msc = gdi.presentationParameters.getMultiSampleCount();
                msc = msc | (msc >> 1);
                msc = msc | (msc >> 2);
                msc = msc | (msc >> 4);
                msc -= (msc >> 1);

                if (getGraphicsDevice() != null)
                {
                    // and clamp it to what the device can handle
                    if (msc > getGraphicsDevice().getGraphicsCapabilities().getMaxMultiSampleCount())
                        msc = getGraphicsDevice().getGraphicsCapabilities().getMaxMultiSampleCount();
                }
                gdi.presentationParameters.setMultiSampleCount(msc);
            }
        }

        return gdi;
    }

    private <TEventArgs extends EventArgs> void raise(EventHandler<TEventArgs> handler, TEventArgs e)
    {
        if (handler != null)
            handler.handleEvent(this, e);
    }

    // #endregion

    // #region IDisposable Members

    @Override
    public void close()
    {
        dispose(true);
        // GC.SuppressFinalize(this);
    }

    protected void dispose(boolean disposing)
    {
        if (!_disposed)
        {
            if (disposing)
            {
                if (_graphicsDevice != null)
                {
                    _graphicsDevice.close();
                    _graphicsDevice = null;
                }
            }
            _disposed = true;
            if (disposed != null)
                disposed.handleEvent(this, EventArgs.Empty);
        }
    }

    // #endregion

    // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
    // partial void platformApplyChanges();

    // partial void platformPreparePresentationParameters(PresentationParameters
    // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
    // presentationParameters);

    private void preparePresentationParameters(PresentationParameters presentationParameters)
    {
        presentationParameters.setBackBufferFormat(_preferredBackBufferFormat);
        presentationParameters.setBackBufferWidth(_preferredBackBufferWidth);
        presentationParameters.setBackBufferHeight(_preferredBackBufferHeight);
        presentationParameters.setDepthStencilFormat(_preferredDepthStencilFormat);
        presentationParameters.setFullScreen(_wantFullScreen);
        presentationParameters.presentationInterval = _synchronizedWithVerticalRetrace ? PresentInterval.One : PresentInterval.Immediate;
        presentationParameters.displayOrientation = _game.getWindow().getCurrentOrientation();
        presentationParameters.setDeviceWindowHandle(_game.getWindow().getHandle());

        if (_preferMultiSampling)
        {
            // always initialize MultiSampleCount to the maximum, if users want to overwrite
            // this they have to respond to the PreparingDeviceSettingsEvent and modify
            // args.GraphicsDeviceInformation.PresentationParameters.MultiSampleCount
            presentationParameters.setMultiSampleCount((getGraphicsDevice() != null)
                    ? getGraphicsDevice().getGraphicsCapabilities().getMaxMultiSampleCount()
                    : 32);
        }
        else
        {
            presentationParameters.setMultiSampleCount(0);
        }

        // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
        // platformPreparePresentationParameters(presentationParameters);
    }

    private void prepareGraphicsDeviceInformation(GraphicsDeviceInformation gdi)
    {
        gdi.adapter = GraphicsAdapter.getDefaultAdapter();
        gdi.graphicsProfile = getGraphicsProfile();
        PresentationParameters pp = new PresentationParameters();
        preparePresentationParameters(pp);
        gdi.presentationParameters = pp;
    }

    /**
     * Applies any pending property changes to the graphics device.
     */
    public void applyChanges()
    {
        // If the device hasn't been created then create it now.
        if (_graphicsDevice == null)
            createDevice();

        if (!_shouldApplyChanges)
            return;

        _game.getWindow().setSupportedOrientations(_supportedOrientations);

        // Allow for optional platform specific behavior.
        // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
        // platformApplyChanges();

        // populates a gdi with settings in this gdm and allows users to override them with
        // PrepareDeviceSettings event this information should be applied to the GraphicsDevice
        GraphicsDeviceInformation gdi = doPreparingDeviceSettings();

        if (gdi.graphicsProfile != getGraphicsDevice().getGraphicsProfile())
        {
            // if the GraphicsProfile changed we need to create a new GraphicsDevice
            disposeGraphicsDevice();
            createDevice(gdi);
            return;
        }

        getGraphicsDevice().reset(gdi.presentationParameters);

        _shouldApplyChanges = false;
    }

    private void disposeGraphicsDevice()
    {
        _graphicsDevice.close();

        if (deviceDisposing != null)
            deviceDisposing.handleEvent(this, EventArgs.Empty);

        _graphicsDevice = null;
    }

    // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
    // partial void platformInitialize(PresentationParameters presentationParameters);

    private void initialize()
    {
        _game.getWindow().setSupportedOrientations(_supportedOrientations);

        PresentationParameters presentationParameters = new PresentationParameters();
        preparePresentationParameters(presentationParameters);

        // Allow for any per-platform changes to the presentation.
        // TODO(Eric): Platform specific code, see other GraphicsDeviceManager files if needed.
        // platformInitialize(presentationParameters);

        _initialized = true;
    }

    private void updateTouchPanel(Object sender, EventArgs eventArgs)
    {
        // TODO(Eric): TouchPanel ?
        // TouchPanel.DisplayWidth = _graphicsDevice.PresentationParameters.BackBufferWidth;
        // TouchPanel.DisplayHeight = _graphicsDevice.PresentationParameters.BackBufferHeight;
        // TouchPanel.DisplayOrientation =
        // _graphicsDevice.PresentationParameters.DisplayOrientation;
    }

    /**
     * Toggles between windowed and full screen modes.
     * <p>
     * Note that on platforms that do not support windowed modes this has no affect.
     */
    public void toggleFullScreen()
    {
        setIsFullScreen(!isFullScreen());
        applyChanges();
    }

    private void onPresentationChanged(Object sender, EventArgs args)
    {
        _game.platform.onPresentationChanged();
    }

    /**
     * Returns the profile which determines the graphics feature level.
     * 
     * @return The profile which determines the graphics feature level.
     */
    public GraphicsProfile getGraphicsProfile()
    {
        return _graphicsProfile;
    }

    /**
     * Sets the profile which determines the graphics feature level.
     * 
     * @param value
     *        The new {@link GraphicsProfile}.
     */
    public void setGraphicsProfile(GraphicsProfile value)
    {
        _shouldApplyChanges = true;
        _graphicsProfile = value;
    }

    /**
     * Returns the graphics device for this manager.
     */
    public GraphicsDevice getGraphicsDevice()
    {
        return _graphicsDevice;
    }

    /**
     * Indicates the desire to switch into full screen mode.
     * <p>
     * When called at startup this will automatically set full screen mode during initialization. If
     * set after startup you must call ApplyChanges() for the full screen mode to be changed. Note
     * that for some platforms that do not support windowed modes this property has no affect.
     * 
     * @return Whether or not this {@link GraphicsDeviceManager} desires to be in full screen mode.
     */
    public boolean isFullScreen()
    {
        return _wantFullScreen;
    }

    public void setIsFullScreen(boolean value)
    {
        _shouldApplyChanges = true;
        _wantFullScreen = value;
    }

    /**
     * Returns the boolean which defines how window switches from windowed to full screen state.
     * "Hard" mode(true) is slow to switch, but more efficient for performance, while "soft"
     * mode(false) is vice versa. The default value is {@code true}.
     * 
     * @return {@code true} if window switches should be done in "Hard" mode; {@code false} if
     *         window switches should be done in "Soft" mode
     */
    public boolean getHardwareModeSwitch()
    {
        return _hardwareModeSwitch;
    }

    /**
     * Sets the boolean which defines how window switches from windowed to full screen state. "Hard"
     * mode(true) is slow to switch, but more efficient for performance, while "soft" mode(false) is
     * vice versa. The default value is {@code true}.
     * 
     * @param value
     */
    public void setHardwareModeSwitch(boolean value)
    {
        _shouldApplyChanges = true;
        _hardwareModeSwitch = value;
    }

    /**
     * Indicates the desire for a multisampled back buffer.
     * <p>
     * When called at startup this will automatically set the MSAA mode during initialization. If
     * set after startup you must call ApplyChanges() for the MSAA mode to be changed.
     * 
     * @return Whether or not this {@link GraphicsDeviceManager} desires a multisampled back buffer.
     */
    public boolean preferMultiSampling()
    {
        return _preferMultiSampling;
    }

    /**
     * Sets whether the {@link GraphicsDeviceManager} prefers a multisample back buffer to the
     * specified value.
     * 
     * @param value
     *        Whether or not this {@link GraphicsDeviceManager} desires a multisampled back buffer.
     */
    public void setPreferMultiSampling(boolean value)
    {
        _shouldApplyChanges = true;
        _preferMultiSampling = value;
    }

    /**
     * Returns the desired back buffer color format.
     * <p>
     * When called at startup this will automatically set the format during initialization. If set
     * after startup you must call ApplyChanges() for the format to be changed.
     * 
     * @return The desired back buffer color format.
     */
    public SurfaceFormat getPreferredBackBufferFormat()
    {
        return _preferredBackBufferFormat;
    }

    /**
     * Sets the desired back buffer color format to the specified value.
     * 
     * @param value
     *        The new desired back buffer color format value.
     */
    public void setPreferredBackBufferFormat(SurfaceFormat value)
    {
        _shouldApplyChanges = true;
        _preferredBackBufferFormat = value;
    }

    /**
     * Returns the desired back buffer height in pixels.
     * <p>
     * When called at startup this will automatically set the height during initialization. If set
     * after startup you must call ApplyChanges() for the height to be changed.
     * 
     * @return The desired back buffer height in pixels.
     */
    public int getPreferredBackBufferHeight()
    {
        return _preferredBackBufferHeight;
    }

    /**
     * Sets the desired back buffer height to the specified value in pixels.
     * 
     * @param value
     *        The new desired back buffer height value in pixels.
     */
    public void setPreferredBackBufferHeight(int value)
    {
        _shouldApplyChanges = true;
        _preferredBackBufferHeight = value;
    }

    /**
     * Returns the desired back buffer width in pixels.
     * <p>
     * When called at startup this will automatically set the width during initialization. If set
     * after startup you must call ApplyChanges() for the width to be changed.
     * 
     * @return The desired back buffer width in pixels.
     */
    public int getPreferredBackBufferWidth()
    {
        return _preferredBackBufferWidth;
    }

    /**
     * Sets the desired back buffer width to the specified value in pixels.
     * 
     * @param value
     *        The new desired back buffer width value in pixels.
     */
    public void setPreferredBackBufferWidth(int value)
    {
        _shouldApplyChanges = true;
        _preferredBackBufferWidth = value;
    }

    /**
     * Returns the desired depth-stencil buffer format.
     * <p>
     * The depth-stencil buffer format defines the scene depth precision and stencil bits available
     * for effects during rendering. When called at startup this will automatically set the format
     * during initialization. If set after startup you must call ApplyChanges() for the format to be
     * changed.
     * 
     * @return The desired depth-stencil buffer format.
     */
    public DepthFormat getPreferredDepthStencilFormat()
    {
        return _preferredDepthStencilFormat;
    }

    /**
     * Sets the desired depth-stencil buffer format to the specified value.
     * 
     * @param value
     *        The new desired depth-stencil buffer format value.
     */
    public void setPreferredDepthStencilFormat(DepthFormat value)
    {
        _shouldApplyChanges = true;
        _preferredDepthStencilFormat = value;
    }

    /**
     * Returns the desire for v-sync when presenting the back buffer.
     * <p>
     * V-sync limits the frame rate of the game to the monitor refresh rate to prevent screen
     * tearing. When called at startup this will automatically set the v-sync mode during
     * initialization. If set after startup you must call ApplyChanges() for the v-sync mode to be
     * changed.
     * 
     * @return The desire for v-sync when presenting the back buffer.
     */
    public boolean synchronizeWithVerticalRetrace()
    {
        return _synchronizedWithVerticalRetrace;
    }

    /**
     * Sets the desire for v-sync when presenting the back buffer.
     * 
     * @param value
     *        The new desired value for v-sync when presenting the back buffer.
     */
    public void setSynchronizeWithVerticalRetrace(boolean value)
    {
        _shouldApplyChanges = true;
        _synchronizedWithVerticalRetrace = value;
    }

    /**
     * Returns the desired allowable display orientations when the device is rotated.
     * <p>
     * This property only applies to mobile platforms with automatic display rotation. When called
     * at startup this will automatically apply the supported orientations during initialization. If
     * set after startup you must call ApplyChanges() for the supported orientations to be changed.
     * 
     * @return The desired allowable display orientations when the device is rotated.
     */
    public DisplayOrientation supportedOrientations()
    {
        return _supportedOrientations;
    }

    /**
     * Sets the desired allowable display orientations when the device is rotated.
     * 
     * @param value
     *        The new desired allowable display orientation value.
     */
    public void setSupportedOrientations(DisplayOrientation value)
    {
        _shouldApplyChanges = true;
        _supportedOrientations = value;
    }

}
