package gameCore;

import gameCore.graphics.GraphicsDevice;
import gameCore.time.GameTime;

import java.awt.Dimension;

public class JavaGamePlatform extends GamePlatform {

	// NOTE: Already commented out in the original code
	// internal static string LaunchParameters;

	private JavaGameWindow _window;

	// TODO: Keyboard input
	// private List<XnaKeys> _keyState;

	public JavaGamePlatform(Game game) {
		super(game);
		// _keyState = new ArrayList<XnaKeys>();
		// Keyboard.SetKeys(_keyState);

		_window = new JavaGameWindow(this);
		// _window.KeyState = _keyState;

		// Mouse.SetWindows(_window._form);

		setWindow(_window);
	}

	@Override
	public GameRunBehavior getDefaultRunBehavior() {
		return GameRunBehavior.Synchronous;
	}

	@Override
	protected void onIsMouseVisibleChanged() {
		_window.mouseVisibleToggled();
	}

	@Override
	public boolean beforeRun() {
		_window.updateWindows();
		return super.beforeRun();
	}

	@Override
	public void beforeInitialize() {
		_window.initialize(getGame().getGraphicsDeviceManager().getPreferredBackBufferWidth(), getGame()
				.getGraphicsDeviceManager().getPreferredBackBufferHeight());

		super.beforeInitialize();
	}

	@Override
	public void runLoop() {
		_window.runLoop();
	}

	@Override
	public void startRunLoop() {
		throw new UnsupportedOperationException("The Windows platform does not support asynchronous run loops");
	}

	@Override
	public void exit() {
		// Application.Exit();
		System.exit(0);
	}

	@Override
	public boolean beforeUpdate(GameTime gameTime) {
		return true;
	}

	@Override
	public boolean beforeDraw(GameTime gameTime) {
		return true;
	}

	@Override
	public void enterFullScreen() {}

	@Override
	public void exitFullScreen() {}

	public void resetWindowBounds() {
		_window.changeClientSize(new Dimension(getGame().getGraphicsDeviceManager().getPreferredBackBufferWidth(),
				getGame().getGraphicsDeviceManager().getPreferredBackBufferHeight()));
	}

	@Override
	public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight) {}

	@Override
	public void beginScreenDeviceChange(boolean willBeFullScreen) {}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public void present() {
		GraphicsDevice device = getGame().getGraphicsDevice();
		if (device != null)
			device.present();
	}

	@Override
	protected void dispose(boolean disposing) {
		if (disposing) {
			if (_window != null) {
				_window.dispose();
				_window = null;
				setWindow(null);

				// Microsoft.Xna.Framework.Media.MediaManagerState.CheckShutdown();
			}
		}

		super.dispose(disposing);
	}
}
