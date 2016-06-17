package jMono_Framework.input;

import jMono_Framework.GameWindow;
import jMono_Framework.JFrameGameFrame;

import java.awt.Robot;

/**
 * Allows reading position and button click information from mouse.
 * 
 * @author Eric Perron
 *
 */
public class Mouse
{
	public static GameWindow PrimaryWindow = null;

	private static MouseState _defaultState = new MouseState();

// #if DESKTOPGL || ANGLE

	// static OpenTK.INativeWindow Window;

	// protected static void setWindows(GameWindow window)
	// {
		// PrimaryWindow = window;
		// if (window is OpenTKGameWindow)
		// {
		// Window = (window as OpenTKGameWindow).Window;
		// }
	// }

// #elif (WINDOWS && DIRECTX)

	static JFrameGameFrame window;

	public static void setWindows(JFrameGameFrame window)
	{
		Mouse.window = window;
	}

// #elif MONOMAC
	// protected static GameWindow Window;
	// protected static float ScrollWheelValue;
// #endif

	/// <summary>
	/// Gets or sets the window handle for current mouse processing.
	/// </summary> 
/*	public static IntPtr WindowHandle 
	{ 
		get
		{ 
#if DESKTOPGL || ANGLE
			return Window.WindowInfo.Handle;
#elif WINRT
			return IntPtr.Zero; // WinRT platform does not create traditionally window, so returns IntPtr.Zero.
#elif(WINDOWS && DIRECTX)
			return Window.Handle; 
#elif MONOMAC
			return IntPtr.Zero;
#else
			return IntPtr.Zero;
#endif
		}
		set
		{
			// only for XNA compatibility, yet
		}
	}*/

	/// <summary>
	/// This API is an extension to XNA.
	/// Gets mouse state information that includes position and button
	/// presses for the provided window
	/// </summary>
	/// <returns>Current state of the mouse.</returns>
	public static MouseState getState(GameWindow window)
	{
// #if MONOMAC
		// We need to maintain precision...
		// window.MouseState.ScrollWheelValue = (int)ScrollWheelValue;

// #elif DESKTOPGL || ANGLE

		// var state = OpenTK.Input.Mouse.GetCursorState();
		// var pc = ((OpenTKGameWindow)window).Window.PointToClient(new System.Drawing.Point(state.X, state.Y));
		// window.MouseState.X = pc.X;
		// window.MouseState.Y = pc.Y;

		// window.MouseState.LeftButton = (ButtonState)state.LeftButton;
		// window.MouseState.RightButton = (ButtonState)state.RightButton;
		// window.MouseState.MiddleButton = (ButtonState)state.MiddleButton;
		// window.MouseState.XButton1 = (ButtonState)state.XButton1;
		// window.MouseState.XButton2 = (ButtonState)state.XButton2;

		// XNA uses the winapi convention of 1 click = 120 delta
		// OpenTK scales 1 click = 1.0 delta, so make that match
		// window.MouseState.ScrollWheelValue = (int)(state.Scroll.Y * 120);
// #endif

		return window.mouseState;
	}

	/// <summary>
	/// Gets mouse state information that includes position and button presses
	/// for the primary window
	/// </summary>
	/// <returns>Current state of the mouse.</returns>
	public static MouseState getState()
	{
// #if ANDROID

		// Before MouseState was changed to take in a 
		// gamewindow, Android seemed to never update 
		// the previous static MouseState that existed.
		// This implies that the default behavior is to return
		// default(MouseState); A static one is used to prevent
		// constant reallocations
		// This will need to change when MonoGame supports desktop Android.
		// Related discussion: https://github.com/mono/MonoGame/pull/1749

		// return _defaultState;
// #else
		if (PrimaryWindow != null)
			return getState(PrimaryWindow);

		return _defaultState;
// #endif
	}

	// TODO: need some test to see if working properly
	/// <summary>
	/// Sets mouse cursor's relative position to game-window.
	/// </summary>
	/// <param name="x">Relative horizontal position of the cursor.</param>
	/// <param name="y">Relative vertical position of the cursor.</param>
	public static void setPosition(int x, int y)
	{
		updateStatePosition(x, y);

// #if (WINDOWS && DIRECTX) || DESKTOPGL || ANGLE
		// correcting the coordinate system
		// Only way to set the mouse position !!!
		// Point pt = Window.PointToScreen(new System.Drawing.Point(x, y));
// #elif WINDOWS
		// var pt = new System.Drawing.Point(0, 0);
// #endif

// #if DESKTOPGL || ANGLE
		// OpenTK.Input.Mouse.SetPosition(pt.X, pt.Y);
// #elif WINDOWS
		// setCursorPos(pt.X, pt.Y);
		robot.mouseMove(x, y);
// #elif MONOMAC
		// var mousePt = NSEvent.CurrentMouseLocation;
		// NSScreen currentScreen = null;
		// foreach (var screen in NSScreen.Screens) {
			// if (screen.Frame.Contains(mousePt)) {
				// currentScreen = screen;
				// break;
			// }
		// }

		//var point = new PointF(x, Window.ClientBounds.Height-y);
		//var windowPt = Window.ConvertPointToView(point, null);
		//var screenPt = Window.Window.ConvertBaseToScreen(windowPt);
		//var flippedPt = new PointF(screenPt.X, currentScreen.Frame.Size.Height-screenPt.Y);
		//flippedPt.Y += currentScreen.Frame.Location.Y;


		 // CGSetLocalEventsSuppressionInterval(0.0);
		 // CGWarpMouseCursorPosition(flippedPt);
		 // CGSetLocalEventsSuppressionInterval(0.25);
// #endif
	}

	private static void updateStatePosition(int x, int y)
	{
		PrimaryWindow.mouseState.setX(x);
		PrimaryWindow.mouseState.setY(y);
	}

// #if WINDOWS

	// [DllImportAttribute("user32.dll", EntryPoint = "SetCursorPos")]
	// [return: MarshalAsAttribute(System.Runtime.InteropServices.UnmanagedType.Bool)]
	// protected static extern bool SetCursorPos(int X, int Y);
	private static Robot robot; {
		try
		{
			robot = new Robot();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// TODO: Do I need this ?
	/// <summary>
	/// Struct representing a point. 
	/// (Suggestion : Make another class for mouse extensions)
	/// </summary>
	// [StructLayout(System.Runtime.InteropServices.LayoutKind.Sequential)]
/*	 protected struct POINT
    {
        public int X;
        public int Y;

        public System.Drawing.Point ToPoint()
        {
            return new System.Drawing.Point(X, Y);
        }

    }*/

// #elif MONOMAC
	// [DllImport (MonoMac.Constants.CoreGraphicsLibrary)]
	// extern static void CGWarpMouseCursorPosition(PointF newCursorPosition);

	// [DllImport (MonoMac.Constants.CoreGraphicsLibrary)]
	// extern static void CGSetLocalEventsSuppressionInterval(double seconds);
// #endif
}
