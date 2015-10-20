package gameCore;

import gameCore.graphics.GraphicsDevice;
import gameCore.input.Keyboard;
import gameCore.input.Keys;
import gameCore.time.GameTime;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class JavaGamePlatform extends GamePlatform
{
	// NOTE: Already commented out in the original code
	// internal static string LaunchParameters;

	private JavaGameWindow _window;

	private List<Keys> _keyState;

	public JavaGamePlatform(Game game)
	{
		super(game);
		_keyState = new ArrayList<Keys>();
		Keyboard.setKeys(_keyState);

		_window = new JavaGameWindow(this);
		_window.keyState = _keyState;

		// Mouse.SetWindows(_window._form);
		
		// TODO: Added this, see where it is done in MonoGame
		_window.mouseVisibleToggled();

		setWindow(_window);
	}

	@Override
	public GameRunBehavior getDefaultRunBehavior()
	{
		return GameRunBehavior.Synchronous;
	}

	@Override
	protected void onIsMouseVisibleChanged()
	{
		_window.mouseVisibleToggled();
	}

	@Override
	public boolean beforeRun()
	{
		_window.updateWindows();
		return super.beforeRun();
	}

	@Override
	public void beforeInitialize()
	{
		_window.initialize(getGame().getGraphicsDeviceManager().getPreferredBackBufferWidth(), getGame()
				.getGraphicsDeviceManager().getPreferredBackBufferHeight());

		super.beforeInitialize();
		
// #if (WINDOWS && DIRECTX)
		if (getGame().getGraphicsDeviceManager().isFullScreen())
		{
			enterFullScreen();
		}
		else
		{
			exitFullScreen();
		}
// #endif
	}

	@Override
	public void runLoop()
	{
		_window.runLoop();
	}

	@Override
	public void startRunLoop()
	{
		throw new UnsupportedOperationException("The Windows platform does not support asynchronous run loops");
	}

	@Override
	public void exit()
	{
		_window.close();
		_window = null;
		setWindow(null);
		// TODO: where did this go in the new MonoGame version ?
		System.exit(0);
	}

	@Override
	public boolean beforeUpdate(GameTime gameTime)
	{
		return true;
	}

	@Override
	public boolean beforeDraw(GameTime gameTime)
	{
		return true;
	}

	@Override
	public void enterFullScreen()
	{
// #if (WINDOWS && DIRECTX)
		if (_alreadyInFullScreenMode)
		{
			return;
		}

		if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
		{
			getGame().getGraphicsDevice().getPresentationParameters().setFullScreen(true);
			// getGame().getGraphicsDevice().createSizeDependentResources(true);
			getGame().getGraphicsDevice().applyRenderTargets(null);
			_window.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else
		{
			_window.setBorderless(true);
			_window.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		_alreadyInWindowedMode = false;
		_alreadyInFullScreenMode = true;
// #endif
	}

	@Override
	public void exitFullScreen()
	{
// #if (WINDOWS && DIRECTX)
		if (_alreadyInWindowedMode)
		{
			return;
		}

		if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
		{
			_window.frame.setExtendedState(JFrame.NORMAL);
			getGame().getGraphicsDevice().getPresentationParameters().setFullScreen(false);
			// getGame().getGraphicsDevice().createSizeDependentResources(true);
			getGame().getGraphicsDevice().applyRenderTargets(null);
		}
		else
		{
			_window.frame.setExtendedState(JFrame.NORMAL);
			_window.setBorderless(false);
		}
		resetWindowBounds();

		_alreadyInWindowedMode = true;
		_alreadyInFullScreenMode = false;
// #endif
	}

	public void resetWindowBounds()
	{
		_window.changeClientSize(new Dimension(getGame().getGraphicsDeviceManager().getPreferredBackBufferWidth(),
				getGame().getGraphicsDeviceManager().getPreferredBackBufferHeight()));
	}

	@Override
	public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight) {}

	@Override
	public void beginScreenDeviceChange(boolean willBeFullScreen) {}

	@Override
	public void log(String message)
	{
		System.out.println(message);
	}

	@Override
	public void present()
	{
		GraphicsDevice device = getGame().getGraphicsDevice();
		if (device != null)
			device.present();
	}

	@Override
	protected void dispose(boolean disposing)
	{
		if (disposing)
		{
			if (_window != null)
			{
				_window.close();
				_window = null;
				setWindow(null);

				// Microsoft.Xna.Framework.Media.MediaManagerState.CheckShutdown();
			}
		}
		Keyboard.setKeys(null);

		super.dispose(disposing);
	}
}
