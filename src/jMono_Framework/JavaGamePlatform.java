package jMono_Framework;

import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.PresentationParameters;
import jMono_Framework.input.Keyboard;
import jMono_Framework.input.Keys;
import jMono_Framework.input.Mouse;
import jMono_Framework.time.GameTime;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class JavaGamePlatform extends GamePlatform
{
    // NOTE(Eric): Already commented out in the original code
    // internal static string LaunchParameters;

    private JavaGameWindow _window;
    private java.awt.Point _locationBeforeFullscreen = new java.awt.Point();

    // TODO(Eric): To delete
    private List<Keys> _keyState;

    public JavaGamePlatform(Game game)
    {
        super(game);
        // TODO(Eric): To delete
        _keyState = new ArrayList<Keys>();
        Keyboard.setKeys(_keyState);

        _window = new JavaGameWindow(this);
        // TODO(Eric): To delete
        _window.keyState = _keyState;

        Mouse.setWindow(_window._frame);

        // TODO(Eric): Added this, see where it is done in MonoGame
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
        // TODO(Eric): Old version
        // _window.initialize(getGame().getGraphicsDeviceManager().getPreferredBackBufferWidth(),
        // getGame()
        // .getGraphicsDeviceManager().getPreferredBackBufferHeight());

        // super.beforeInitialize();

        // #if (WINDOWS && DIRECTX)
        // if (getGame().getGraphicsDeviceManager().isFullScreen())
        // {
        // enterFullScreen();
        // }
        // else
        // {
        // exitFullScreen();
        // }
        // #endif

        // TODO(Eric): New version
        super.beforeInitialize();

        GraphicsDeviceManager gdm = getGame().getGraphicsDeviceManager();
        if (gdm == null)
        {
            _window.initialize(GraphicsDeviceManager.DefaultBackBufferWidth, GraphicsDeviceManager.DefaultBackBufferHeight);
        }
        else
        {
            PresentationParameters pp = getGame().getGraphicsDevice().getPresentationParameters();
            _window.initialize(pp.getBackBufferWidth(), pp.getBackBufferHeight());

            if (gdm.isFullScreen())
                enterFullScreen();
        }
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
        if (_window != null)
            _window.close();
        _window = null;
        setWindow(null);
        // TODO(Eric): where did this go in the new MonoGame version ?
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
        // TODO(Eric): Old version
        // #if (WINDOWS && DIRECTX)
        // if (_alreadyInFullScreenMode)
        // {
        // return;
        // }

        // if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
        // {
        // getGame().getGraphicsDevice().getPresentationParameters().setFullScreen(true);
        // // getGame().getGraphicsDevice().createSizeDependentResources(true);
        // getGame().getGraphicsDevice().applyRenderTargets(null);
        // _window._frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // }
        // else
        // {
        // _window.setBorderless(true);
        // _window._frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // }

        // _alreadyInWindowedMode = false;
        // _alreadyInFullScreenMode = true;
        // // #endif

        // TODO(Eric): New version
        // store the location of the window so we can restore it later
        _locationBeforeFullscreen = _window.getPosition(); // _window.Form.Location;
        // TODO(Eric): setHardwareFullscreen() is from GraphicsDevice.DirectX and not sure what it is.
        // if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
        //     getGame().getGraphicsDevice().setHardwareFullscreen();
        // else
            _window.setBorderless(true);

        _window._frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        inFullScreenMode = true;
    }

    @Override
    public void exitFullScreen()
    {
        // TODO(Eric): Old version
        // // #if (WINDOWS && DIRECTX)
        // if (_alreadyInWindowedMode)
        // {
        // return;
        // }
        //
        // if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
        // {
        // _window._frame.setExtendedState(JFrame.NORMAL);
        // getGame().getGraphicsDevice().getPresentationParameters().setFullScreen(false);
        // // getGame().getGraphicsDevice().createSizeDependentResources(true);
        // getGame().getGraphicsDevice().applyRenderTargets(null);
        // }
        // else
        // {
        // _window._frame.setExtendedState(JFrame.NORMAL);
        // _window.setBorderless(false);
        // }
        // resetWindowBounds();
        //
        // _alreadyInWindowedMode = true;
        // _alreadyInFullScreenMode = false;
        // // #endif

        // TODO(Eric): New version
        // TODO(Eric): setHardwareFullscreen() is from GraphicsDevice.DirectX and not sure what it is.
        // if (getGame().getGraphicsDeviceManager().getHardwareModeSwitch())
        //     getGame().getGraphicsDevice().setHardwareFullscreen();
        // else
            _window.setBorderless(false);

        _window._frame.setExtendedState(JFrame.NORMAL);
        _window.setPosition(_locationBeforeFullscreen);

        inFullScreenMode = false;
    }

    @Override
    protected void onPresentationChanged()
    {
        PresentationParameters pp = getGame().getGraphicsDevice().getPresentationParameters();
        _window.changeClientSize(new Dimension(pp.getBackBufferWidth(), pp.getBackBufferHeight()));

        if (getGame().getGraphicsDevice().getPresentationParameters().isFullScreen() && !inFullScreenMode)
        {
            enterFullScreen();
            _window.onClientSizeChanged();
        }
        else if (!getGame().getGraphicsDevice().getPresentationParameters().isFullScreen() && inFullScreenMode)
        {
            exitFullScreen();
            _window.onClientSizeChanged();
        }
    }

    @Override
    public void endScreenDeviceChange(String screenDeviceName, int clientWidth, int clientHeight)
    {}

    @Override
    public void beginScreenDeviceChange(boolean willBeFullScreen)
    {}

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

                // TODO(Eric): MediaManagerState.CheckShutdown();
                // Microsoft.Xna.Framework.Media.MediaManagerState.CheckShutdown();
            }
        }
        // TODO(Eric): To delete
        Keyboard.setKeys(null);

        super.dispose(disposing);
    }
}
