package jMono_Framework.graphics;

import jMono_Framework.DisplayOrientation;
import jMono_Framework.GraphicsDeviceManager;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.states.DepthFormat;

public class PresentationParameters implements AutoCloseable
{
	public final int DefaultPresentRate = 60;

	private DepthFormat depthStencilFormat;
	private SurfaceFormat backBufferFormat;
	private int backBufferHeight = GraphicsDeviceManager.DefaultBackBufferHeight;
	private int backBufferWidth = GraphicsDeviceManager.DefaultBackBufferWidth;
	// private IntPtr deviceWindowHandle;
	private int multiSampleCount;
	private boolean disposed;
// #if !WINRT || WINDOWS_UAP
	private boolean isFullScreen;
// #endif

	public PresentationParameters()
	{
		clear();
	}

	@Override
	public void finalize()
	{
		dispose(false);
	}

	public SurfaceFormat getBackBufferFormat()
	{
		return backBufferFormat;
	}

	public void setBackBufferFormat(SurfaceFormat value)
	{
		backBufferFormat = value;
	}

	public int getBackBufferHeight()
	{
		return backBufferHeight;
	}

	public void setBackBufferHeight(int value)
	{
		backBufferHeight = value;
	}

	public int getBackBufferWidth()
	{
		return backBufferWidth;
	}

	public void setBackBufferWidth(int value)
	{
		backBufferWidth = value;
	}

	public Rectangle getBounds()
	{
		return new Rectangle(0, 0, backBufferWidth, backBufferHeight);
	}

	// public IntPtr DeviceWindowHandle
	// {
	// get { return deviceWindowHandle; }
	// set { deviceWindowHandle = value; }
	// }

// #if WINDOWS_STOREAPP
	// [CLSCompliant(false)]
	// public SwapChainBackgroundPanel SwapChainBackgroundPanel { get; set; }
// #endif

	public DepthFormat getDepthStencilFormat()
	{
		return depthStencilFormat;
	}

	public void setDepthStencilFormat(DepthFormat value)
	{
		depthStencilFormat = value;
	}

	public boolean isFullScreen()
	{
// #if WINRT && !WINDOWS_UAP
		// Always return true for Windows 8
		// return true;
// #else
		return isFullScreen;
// #endif
	}

	public void setFullScreen(boolean value)
	{
// #if !WINRT || WINDOWS_UAP
		// If we are not on windows 8 set the value otherwise ignore it.
		isFullScreen = value;
// #endif
// #if IOS
		// UIApplication.SharedApplication.StatusBarHidden = isFullScreen;
// #endif

	}

	public int getMultiSampleCount()
	{
		return multiSampleCount;
	}

	public void setMultiSampleCount(int value)
	{
		multiSampleCount = value;
	}

	public PresentInterval presentationInterval;

	public DisplayOrientation displayOrientation;

	public RenderTargetUsage renderTargetUsage;

	public void clear()
	{
		backBufferFormat = SurfaceFormat.Color;
// #if IOS
		// Mainscreen.Bounds does not account for the device's orientation. it ALWAYS assumes
		// portrait
		// var width = (int)(UIScreen.MainScreen.Bounds.Width * UIScreen.MainScreen.Scale);
		// var height = (int)(UIScreen.MainScreen.Bounds.Height * UIScreen.MainScreen.Scale);
		//
		// Flip the dimentions if we need to.
		// if (TouchPanel.DisplayOrientation == DisplayOrientation.LandscapeLeft ||
		// TouchPanel.DisplayOrientation == DisplayOrientation.LandscapeRight)
		// {
		// width = height;
		// height = (int)(UIScreen.MainScreen.Bounds.Width * UIScreen.MainScreen.Scale);
		// }
		//
		// backBufferWidth = width;
		// backBufferHeight = height;
// #else
		backBufferWidth = GraphicsDeviceManager.DefaultBackBufferWidth;
		backBufferHeight = GraphicsDeviceManager.DefaultBackBufferHeight;
// #endif
		// deviceWindowHandle = IntPtr.Zero;
// #if IOS
		// isFullScreen = UIApplication.SharedApplication.StatusBarHidden;
// #else
		isFullScreen = false;
// #endif
		depthStencilFormat = DepthFormat.None;
		multiSampleCount = 0;
		presentationInterval = PresentInterval.Default;
		displayOrientation = DisplayOrientation.Default;
	}

	@Override
	public PresentationParameters clone()
	{
		PresentationParameters clone = new PresentationParameters();
		clone.backBufferFormat = this.backBufferFormat;
		clone.backBufferHeight = this.backBufferHeight;
		clone.backBufferWidth = this.backBufferWidth;
		// clone.deviceWindowHandle = this.deviceWindowHandle;
		clone.disposed = this.disposed;
		clone.isFullScreen = this.isFullScreen;
		clone.depthStencilFormat = this.depthStencilFormat;
		clone.multiSampleCount = this.multiSampleCount;
		clone.presentationInterval = this.presentationInterval;
		clone.displayOrientation = this.displayOrientation;
		return clone;
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
			disposed = true;
			if (disposing)
			{
				// Dispose managed resources
			}
			// Dispose unmanaged resources
		}
	}

}
