package gameCore;

import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.graphics.GraphicsAdapter;
import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.GraphicsProfile;
import gameCore.graphics.IGraphicsDeviceService;
import gameCore.graphics.PresentInterval;
import gameCore.graphics.PresentationParameters;
import gameCore.graphics.SurfaceFormat;
import gameCore.graphics.states.DepthFormat;

// TODO: Events in IGraphicsDeviceService
// public class GraphicsDeviceManager implements IGraphicsDeviceService, IGraphicsDeviceManager,
// AutoCloseable {
public class GraphicsDeviceManager extends IGraphicsDeviceService implements IGraphicsDeviceManager, AutoCloseable
{
	private Game _game;
	private GraphicsDevice _graphicsDevice;
	private int _preferredBackBufferHeight;
	private int _preferredBackBufferWidth;
	private SurfaceFormat _preferredBackBufferFormat;
	private DepthFormat _preferredDepthStencilFormat;
	private boolean _preferMultiSampling;
	private DisplayOrientation _supportedOrientations;
	private boolean _synchronizedWithVerticalRetrace = true;
	private boolean _drawBegun;
	boolean disposed;
	private boolean _hardwareModeSwitch = true;

// #if (WINDOWS || WINDOWS_UAP) && DIRECTX
	private boolean _firstLaunch = true;
//  #endif

// #if !WINRT
	private boolean _wantFullScreen = false;
// #endif

	public static int DefaultBackBufferHeight = 480;
	public static int DefaultBackBufferWidth = 800;

	public GraphicsDeviceManager(Game game)
	{
		if (game == null)
			throw new NullPointerException("The game cannot be null!");

		_game = game;

		_supportedOrientations = DisplayOrientation.Default;

// #if WINDOWS || MONOMAC || LINUX
		_preferredBackBufferHeight = DefaultBackBufferHeight;
		_preferredBackBufferWidth = DefaultBackBufferWidth;
// #else
		// Preferred buffer width/height is used to determine default supported orientations,
		// so set the default values to match Xna behaviour of landscape only by default.
		// Note also that it's using the device window dimensions.
		// _preferredBackBufferWidth = Math.Max(_game.Window.ClientBounds.Height,
		// _game.Window.ClientBounds.Width);
		// _preferredBackBufferHeight = Math.Min(_game.Window.ClientBounds.Height,
		// _game.Window.ClientBounds.Width);
// #endif

		_preferredBackBufferFormat = SurfaceFormat.Color;
		_preferredDepthStencilFormat = DepthFormat.Depth24;
		_synchronizedWithVerticalRetrace = true;

		graphicsProfile = GraphicsDevice.getHighestSupportedGraphicsProfile(null);

		if (_game.getServices().getService(IGraphicsDeviceManager.class) != null)
			throw new IllegalArgumentException("Graphics Device Manager Already Present");

		_game.getServices().addService(IGraphicsDeviceManager.class, this);
		_game.getServices().addService(IGraphicsDeviceService.class, this);
	}

	@Override
	public void finalize()
	{
		dispose(false);
	}

	public void createDevice()
	{
		initialize();

		onDeviceCreated(EventArgs.Empty);
	}

	public boolean beginDraw()
	{
		if (_graphicsDevice == null)
			return false;

		_drawBegun = true;
		return true;
	}

	public void endDraw()
	{
		if (_graphicsDevice != null && _drawBegun)
		{
			_drawBegun = false;
			_graphicsDevice.present();
		}
	}

	// TODO: For now we have an abstract class so these are in there.
	// public EventHandler<EventArgs> deviceCreated;
	// public EventHandler<EventArgs> deviceDisposing;
	// public EventHandler<EventArgs> deviceReset;
	// public EventHandler<EventArgs> deviceResetting;
	public EventHandler<PreparingDeviceSettingsEventArgs> preparingDeviceSettings;

	// FIXME: Why does the GraphicsDeviceManager not know enough about the
	// GraphicsDevice to raise these events without help?
	protected void onDeviceDisposing(EventArgs e)
	{
		raise(deviceDisposing, e);
	}

	// FIXME: Why does the GraphicsDeviceManager not know enough about the
	// GraphicsDevice to raise these events without help?
	protected void onDeviceResetting(EventArgs e)
	{
		raise(deviceResetting, e);
	}

	// FIXME: Why does the GraphicsDeviceManager not know enough about the
	// GraphicsDevice to raise these events without help?
	protected void onDeviceReset(EventArgs e)
	{
		raise(deviceReset, e);
	}

	// FIXME: Why does the GraphicsDeviceManager not know enough about the
	// GraphicsDevice to raise these events without help?
	protected void onDeviceCreated(EventArgs e)
	{
		raise(deviceCreated, e);
	}

	private <TEventArgs extends EventArgs> void raise(EventHandler<TEventArgs> handler, TEventArgs e)
	{
		if (handler != null)
			handler.accept(this, e);
	}

	@Override
	public void close()
	{
		dispose(true);
		// GC.SuppressFinalize(this);
	}

	protected void dispose(boolean disposing)
	{
		if (!disposed)
		{
			if (disposing)
			{
				if (_graphicsDevice != null)
				{
					_graphicsDevice.close();
					_graphicsDevice = null;
				}
			}
			disposed = true;
		}
	}

	public void applyChanges()
	{
		// Calling ApplyChanges() before CreateDevice() should have no effect
		if (_graphicsDevice == null)
			return;

// #if WINDOWS_PHONE
		// _graphicsDevice.GraphicsProfile = GraphicsProfile;
		// Display orientation is always portrait on WP8
		// _graphicsDevice.PresentationParameters.DisplayOrientation = DisplayOrientation.Portrait;
// #elif WINDOWS_STOREAPP || WINDOWS_UAP

		// TODO: Does this need to occur here?
		// _game.getWindow().setSupportedOrientations(_supportedOrientations);

		// _graphicsDevice.PresentationParameters.BackBufferFormat = _preferredBackBufferFormat;
		// _graphicsDevice.PresentationParameters.BackBufferWidth = _preferredBackBufferWidth;
		// _graphicsDevice.PresentationParameters.BackBufferHeight = _preferredBackBufferHeight;
		// _graphicsDevice.PresentationParameters.DepthStencilFormat = _preferredDepthStencilFormat;

		// TODO: We probably should be resetting the whole device
		// if this changes as we are targeting a different 
		// hardware feature level.
		// _graphicsDevice.GraphicsProfile = GraphicsProfile;

   // #if WINDOWS_UAP
		// _graphicsDevice.PresentationParameters.DeviceWindowHandle = IntPtr.Zero;
		// _graphicsDevice.PresentationParameters.SwapChainPanel = this.SwapChainPanel;
		// _graphicsDevice.PresentationParameters.IsFullScreen = _wantFullScreen;
   // #else
		// _graphicsDevice.PresentationParameters.IsFullScreen = false;

		// The graphics device can use a XAML panel or a window
		// to created the default swapchain target.
		// if (this.SwapChainBackgroundPanel != null)
		// {
			// _graphicsDevice.PresentationParameters.DeviceWindowHandle = IntPtr.Zero;
			// _graphicsDevice.PresentationParameters.SwapChainBackgroundPanel = this.SwapChainBackgroundPanel;
		// }
		// else
		// {
			// _graphicsDevice.PresentationParameters.DeviceWindowHandle = _game.Window.Handle;
			// _graphicsDevice.PresentationParameters.SwapChainBackgroundPanel = null;
		// }
   // #endif
		// Update the back buffer.
		// _graphicsDevice.createSizeDependentResources();
		// _graphicsDevice.applyRenderTargets(null);

   // #if WINDOWS_UAP
		// ((UAPGameWindow)_game.Window).SetClientSize(_preferredBackBufferWidth, _preferredBackBufferHeight);
   // #endif

// #elif WINDOWS && DIRECTX

		_graphicsDevice.getPresentationParameters().setBackBufferFormat(_preferredBackBufferFormat);
		_graphicsDevice.getPresentationParameters().setBackBufferWidth(_preferredBackBufferWidth);
		_graphicsDevice.getPresentationParameters().setBackBufferHeight(_preferredBackBufferHeight);
		_graphicsDevice.getPresentationParameters().setDepthStencilFormat(_preferredDepthStencilFormat);
		_graphicsDevice.getPresentationParameters().presentationInterval = _synchronizedWithVerticalRetrace ? PresentInterval.Default
				: PresentInterval.Immediate;
		_graphicsDevice.getPresentationParameters().setFullScreen(_wantFullScreen);

		// // TODO: We probably should be resetting the whole
		// // device if this changes as we are targeting a different
		// // hardware feature level.
		_graphicsDevice.setGraphicsProfile(graphicsProfile);

		// TODO: Do I need this ?
		// _graphicsDevice.PresentationParameters.DeviceWindowHandle = _game.Window.Handle;

		// // Update the back buffer.
		// _graphicsDevice.CreateSizeDependentResources();  // NOTE: Thi is in GraphicsDevice.DirectX.cs
		_graphicsDevice.applyRenderTargets(null);

		((JavaGamePlatform)_game.platform).resetWindowBounds();

// #elif DESKTOPGL
		// ((OpenTKGamePlatform)_game.Platform).ResetWindowBounds();

		//Set the swap interval based on if vsync is desired or not.
		//See GetSwapInterval for more details
		// int swapInterval;
		// if (_synchronizedWithVerticalRetrace)
			// swapInterval = _graphicsDevice.PresentationParameters.PresentationInterval.GetSwapInterval();
		// else
			// swapInterval = 0;
		// _graphicsDevice.Context.SwapInterval = swapInterval;
// #elif MONOMAC
		// _graphicsDevice.PresentationParameters.IsFullScreen = _wantFullScreen;

		// TODO: Implement multisampling (aka anti-alising) for all platforms!

		// _game.applyChanges(this);
// #else

   // #if ANDROID
		// Trigger a change in orientation in case the supported orientations have changed
		// ((AndroidGameWindow)_game.Window).SetOrientation(_game.Window.CurrentOrientation, false);
   // #endif
		// Ensure the presentation parameter orientation and buffer size matches the window
		// _graphicsDevice.PresentationParameters.DisplayOrientation = _game.Window.CurrentOrientation;

		// Set the presentation parameters' actual buffer size to match the orientation
        // bool isLandscape = (0 != (_game.Window.CurrentOrientation & (DisplayOrientation.LandscapeLeft | DisplayOrientation.LandscapeRight)));
		// int w = PreferredBackBufferWidth;
		// int h = PreferredBackBufferHeight;

		// _graphicsDevice.PresentationParameters.BackBufferWidth = isLandscape ? Math.Max(w, h) : Math.Min(w, h);
		// _graphicsDevice.PresentationParameters.BackBufferHeight = isLandscape ? Math.Min(w, h) : Math.Max(w, h);

		// ResetClientBounds();
// #endif

		// Set the new display size on the touch panel.
		//
		// TODO: In XNA this seems to be done as part of the 
		// GraphicsDevice.DeviceReset event... we need to get 
		// those working.
		//
		// TouchPanel.DisplayWidth = _graphicsDevice.PresentationParameters.BackBufferWidth;
		// TouchPanel.DisplayHeight = _graphicsDevice.PresentationParameters.BackBufferHeight;

// #if (WINDOWS || WINDOWS_UAP) && DIRECTX

		if (!_firstLaunch)
        {
        	if (isFullScreen())
            {
            	_game.platform.enterFullScreen();
            }
            else
            {
            	_game.platform.exitFullScreen();
            }
        }
        _firstLaunch = false;
// #endif
	}

	private void initialize()
	{
		PresentationParameters presentationParameters = new PresentationParameters();
		presentationParameters.setDepthStencilFormat(DepthFormat.Depth24);

// #if (WINDOWS || WINRT) && !DESKTOPGL
		_game.getWindow().setSupportedOrientations(_supportedOrientations);

		presentationParameters.setBackBufferFormat(_preferredBackBufferFormat);
		presentationParameters.setBackBufferWidth(_preferredBackBufferWidth);
		presentationParameters.setBackBufferHeight(_preferredBackBufferHeight);
		presentationParameters.setDepthStencilFormat(_preferredDepthStencilFormat);

		presentationParameters.setFullScreen(false);
   // #if WINDOWS_PHONE
		// Nothing to do!
   // #elif WINDOWS_UAP
		// presentationParameters.DeviceWindowHandle = IntPtr.Zero;
		// presentationParameters.SwapChainPanel = this.SwapChainPanel;
   // #elif WINDOWS_STORE
		// The graphics device can use a XAML panel or a window
		// to created the default swapchain target.
		// if (this.SwapChainBackgroundPanel != null)
		// {
			// presentationParameters.DeviceWindowHandle = IntPtr.Zero;
			// presentationParameters.SwapChainBackgroundPanel = this.SwapChainBackgroundPanel;
		//}
		// else
		// {
			// presentationParameters.DeviceWindowHandle = _game.Window.Handle;
			// presentationParameters.SwapChainBackgroundPanel = null;
		// }
   // #else
		// presentationParameters.DeviceWindowHandle = _game.Window.Handle;
   // #endif

// #else

   // #if MONOMAC
		// presentationParameters.IsFullScreen = _wantFullScreen;
   // #elif DESKTOPGL
		// presentationParameters.IsFullScreen = _wantFullScreen;
   // #else
		// Set "full screen"  as default
		// presentationParameters.setIsFullScreen(true);
   // #endif // MONOMAC

// #endif // WINDOWS || WINRT

		// TODO: Implement multisampling (aka anti-aliasing) for all platforms!
		if (preparingDeviceSettings != null)
		{
			GraphicsDeviceInformation gdi = new GraphicsDeviceInformation();
			gdi.graphicsProfile = graphicsProfile; // Microsoft defaults this to Reach.
			gdi.adapter = GraphicsAdapter.getDefaultAdapter();
			gdi.presentationParameters = presentationParameters;
			PreparingDeviceSettingsEventArgs pe = new PreparingDeviceSettingsEventArgs(gdi);
			preparingDeviceSettings.accept(this, pe);
			presentationParameters = pe.getGraphicsDeviceInformation().presentationParameters;
			graphicsProfile = pe.getGraphicsDeviceInformation().graphicsProfile;
		}

		// Needs to be before ApplyChanges()
		_graphicsDevice = new GraphicsDevice(GraphicsAdapter.getDefaultAdapter(), graphicsProfile, presentationParameters);

// #if !MONOMAC
		applyChanges();
// #endif

		// Set the new display size on the touch panel.
		//
		// TODO: In XNA this seems to be done as part of the
		// GraphicsDevice.DeviceReset event... we need to get
		// those working.
		//
		// TouchPanel.DisplayWidth = _graphicsDevice.PresentationParameters.BackBufferWidth;
		// TouchPanel.DisplayHeight = _graphicsDevice.PresentationParameters.BackBufferHeight;
		// TouchPanel.DisplayOrientation = _graphicsDevice.PresentationParameters.DisplayOrientation;
	}

	public void toggleFullScreen()
	{
		setIsFullScreen(!isFullScreen());
		
// #if (WINDOWS || WINDOWS_UAP) && DIRECTX
        applyChanges();
// #endif
	}

// #if WINDOWS_STOREAPP
	// [CLSCompliant(false)]
	// public SwapChainBackgroundPanel SwapChainBackgroundPanel;
// #endif

// #if WINDOWS_UAP
    // [CLSCompliant(false)]
    // public SwapChainPanel SwapChainPanel { get; set; }
// #endif

	public GraphicsProfile graphicsProfile;

	public GraphicsDevice getGraphicsDevice()
	{
		return _graphicsDevice;
	}

	public boolean isFullScreen()
	{
// #if WINDOWS_UAP
        // return _wantFullScreen;
// #elif WINRT
        // return true;
// #else
		if (_graphicsDevice != null)
			return _graphicsDevice.getPresentationParameters().isFullScreen();
		else
			return _wantFullScreen;
// #endif
	}

	public void setIsFullScreen(boolean value)
	{
// #if WINDOWS_UAP
        // _wantFullScreen = value;
// #elif WINRT
        // Just ignore this as it is not relevant on Windows 8
// #elif WINDOWS && DIRECTX
        // _wantFullScreen = value;
// #else
        _wantFullScreen = value;
        if (_graphicsDevice != null)
        {
        	_graphicsDevice.getPresentationParameters().setFullScreen(value);
   // #if ANDROID
            // ForceSetFullScreen();
   // #endif
        }
// #endif
		// }
// #endif
	}

// #if ANDROID
	// protected void ForceSetFullScreen()
	// {
	// if (IsFullScreen)
	// {
	// Game.Activity.Window.ClearFlags(Android.Views.WindowManagerFlags.ForceNotFullscreen);
	// Game.Activity.Window.SetFlags(WindowManagerFlags.Fullscreen, WindowManagerFlags.Fullscreen);
	// }
	// else
	// Game.Activity.Window.SetFlags(WindowManagerFlags.ForceNotFullscreen,
	// WindowManagerFlags.ForceNotFullscreen);
	// }
// #endif

    /// <summary>
    /// Gets or sets the boolean which defines how window switches from windowed to fullscreen state.
    /// "Hard" mode(true) is slow to switch, but more effecient for performance, while "soft" mode(false) is vice versa.
    /// The default value is <c>true</c>. Can only be changed before graphics device is created (in game's constructor).
    /// </summary>
    public boolean getHardwareModeSwitch()
    {
    	return _hardwareModeSwitch;
    }

    public void setHardwareModeSwitch(boolean value)
	{
    	if (_graphicsDevice == null)
    		_hardwareModeSwitch = value;
    	else
    		throw new IllegalStateException("This property can only be changed before graphics device is created(in game constructor).");
	}
	
	public boolean preferMultiSampling()
	{
		return _preferMultiSampling;
	}

	public void setPreferMultiSampling(boolean value)
	{
		_preferMultiSampling = value;
	}

	public SurfaceFormat getPreferredBackBufferFormat()
	{
		return _preferredBackBufferFormat;
	}

	public void setPreferredBackBufferFormat(SurfaceFormat value)
	{
		_preferredBackBufferFormat = value;
	}

	public int getPreferredBackBufferHeight()
	{
		return _preferredBackBufferHeight;
	}

	public void setPreferredBackBufferHeight(int value)
	{
		_preferredBackBufferHeight = value;
	}

	public int getPreferredBackBufferWidth()
	{
		return _preferredBackBufferWidth;
	}

	public void setPreferredBackBufferWidth(int value)
	{
		_preferredBackBufferWidth = value;
	}

	public DepthFormat getPreferredDepthStencilFormat()
	{
		return _preferredDepthStencilFormat;
	}

	public void setPreferredDepthStencilFormat(DepthFormat value)
	{
		_preferredDepthStencilFormat = value;
	}

	public boolean synchronizeWithVerticalRetrace()
	{
		return _synchronizedWithVerticalRetrace;
	}

	public void setSynchronizeWithVerticalRetrace(boolean value)
	{
		_synchronizedWithVerticalRetrace = value;
	}

	public DisplayOrientation supportedOrientations()
	{
		return _supportedOrientations;
	}

	public void setSupportedOrientations(DisplayOrientation value)
	{
		_supportedOrientations = value;
		if (_game.getWindow() != null)
			_game.getWindow().setSupportedOrientations(_supportedOrientations);
	}

	// / <summary>
	// / This method is used by MonoGame Android to adjust the game's drawn to area to fill
	// / as much of the screen as possible whilst retaining the aspect ratio inferred from
	// / aspectRatio = (PreferredBackBufferWidth / PreferredBackBufferHeight)
	// /
	// / NOTE: this is a hack that should be removed if proper back buffer to screen scaling
	// / is implemented. To disable it's effect, in the game's constructor use:
	// /
	// / graphics.IsFullScreen = true;
	// / graphics.PreferredBackBufferHeight = Window.ClientBounds.Height;
	// / graphics.PreferredBackBufferWidth = Window.ClientBounds.Width;
	// /
	// / </summary>
	protected void resetClientBounds()
	{
// #if ANDROID
		// float preferredAspectRatio = (float)PreferredBackBufferWidth /
		// (float)PreferredBackBufferHeight;
		// float displayAspectRatio = (float)GraphicsDevice.DisplayMode.Width /
		// (float)GraphicsDevice.DisplayMode.Height;
		//
		// float adjustedAspectRatio = preferredAspectRatio;
		//
		// if ((preferredAspectRatio > 1.0f && displayAspectRatio < 1.0f) ||
		// (preferredAspectRatio < 1.0f && displayAspectRatio > 1.0f))
		// {
		// // Invert preferred aspect ratio if it's orientation differs from the display mode
		// orientation.
		// // This occurs when user sets preferredBackBufferWidth/Height and also allows multiple
		// supported orientations
		// adjustedAspectRatio = 1.0f / preferredAspectRatio;
		// }
		//
		// const float EPSILON = 0.00001f;
		// var newClientBounds = new Rectangle();
		// if (displayAspectRatio > (adjustedAspectRatio + EPSILON))
		// {
		// // Fill the entire height and reduce the width to keep aspect ratio
		// newClientBounds.Height = _graphicsDevice.DisplayMode.Height;
		// newClientBounds.Width = (int)(newClientBounds.Height * adjustedAspectRatio);
		// newClientBounds.X = (_graphicsDevice.DisplayMode.Width - newClientBounds.Width) / 2;
		// }
		// else if (displayAspectRatio < (adjustedAspectRatio - EPSILON))
		// {
		// // Fill the entire width and reduce the height to keep aspect ratio
		// newClientBounds.Width = _graphicsDevice.DisplayMode.Width;
		// newClientBounds.Height = (int)(newClientBounds.Width / adjustedAspectRatio);
		// newClientBounds.Y = (_graphicsDevice.DisplayMode.Height - newClientBounds.Height) / 2;
		// }
		// else
		// {
		// // Set the ClientBounds to match the DisplayMode
		// newClientBounds.Width = GraphicsDevice.DisplayMode.Width;
		// newClientBounds.Height = GraphicsDevice.DisplayMode.Height;
		// }
		//
		// // Ensure buffer size is reported correctly
		// _graphicsDevice.PresentationParameters.BackBufferWidth = newClientBounds.Width;
		// _graphicsDevice.PresentationParameters.BackBufferHeight = newClientBounds.Height;
		//
		// // Set the veiwport so the (potentially) resized client bounds are drawn in the middle of
		// the screen
		// _graphicsDevice.Viewport = new Viewport(newClientBounds.X, -newClientBounds.Y,
		// newClientBounds.Width, newClientBounds.Height);
		//
		// ((AndroidGameWindow)_game.Window).ChangeClientBounds(newClientBounds);
		//
		// // Touch panel needs latest buffer size for scaling
		// TouchPanel.DisplayWidth = newClientBounds.Width;
		// TouchPanel.DisplayHeight = newClientBounds.Height;
		//
		// Android.Util.Log.Debug("MonoGame",
		// "GraphicsDeviceManager.ResetClientBounds: newClientBounds=" +
		// newClientBounds.ToString());
// #endif
	}

}
