package gameCore.input;

import gameCore.Point;

// C# struct
/**
 * Represents a mouse state with cursor position and button press information.
 * 
 * @author Eric Perron
 *
 */
public class MouseState
{
	int _x, _y;
	public int _scrollWheelValue;
	ButtonState _leftButton;
	ButtonState _rightButton;
	ButtonState _middleButton;
	ButtonState _xButton1;
	ButtonState _xButton2;

	// Note: Added this since it is provided by default for struct in C#
	public MouseState()
	{
		
	}
	
	/**
	 * Initializes a new instance of the MouseState.
	 * 
	 * <p>
	 * Normally {@link Mouse#getState()} should be used to get mouse current state. The constructor
	 * is provided for simulating mouse input.
	 * 
	 * @param x
	 *        Horizontal position of the mouse.
	 * @param y
	 *        Vertical position of the mouse.
	 * @param scrollWheel
	 *        Mouse scroll wheel's value.
	 * @param leftButton
	 *        Left mouse button's state.
	 * @param middleButton
	 *        Middle mouse button's state.
	 * @param rightButton
	 *        Right mouse button's state.
	 * @param xButton1
	 *        XBUTTON1's state.
	 * @param xButton2
	 *        XBUTTON2's state.
	 */
	public MouseState(
			int x,
			int y,
			int scrollWheel,
			ButtonState leftButton,
			ButtonState middleButton,
			ButtonState rightButton,
			ButtonState xButton1,
			ButtonState xButton2)
	{
		_x = x;
		_y = y;
		_scrollWheelValue = scrollWheel;
		_leftButton = leftButton;
		_middleButton = middleButton;
		_rightButton = rightButton;
		_xButton1 = xButton1;
		_xButton2 = xButton2;
	}

	/**
	 * Compares whether two MouseState instances are equal.
	 * 
	 * @param left
	 *        MouseState instance on the left of the equal sign.
	 * @param right
	 *        MouseState instance on the right of the equal sign.
	 * @return {@code true} if the instances are equal; {@code false} otherwise.
	 */
	public static boolean equals(MouseState left, MouseState right)
	{
		return left._x == right._x &&
			   left._y == right._y &&
			   left._leftButton == right._leftButton &&
			   left._middleButton == right._middleButton &&
			   left._rightButton == right._rightButton &&
			   left._scrollWheelValue == right._scrollWheelValue;
	}

	/**
	 * Compares whether two MouseState instances are not equal.
	 * 
	 * @param left
	 *        MouseState instance on the left of the equal sign.
	 * @param right
	 *        MouseState instance on the right of the equal sign.
	 * @return {@code true} if the objects are not equal; {@code false} otherwise.
	 */
	public static boolean notEquals(MouseState left, MouseState right)
	{
		return !(MouseState.equals(left, right));
	}

	/**
	 * Compares whether the current instance is equal to the specified object.
	 * 
	 * @param obj
	 *        The MouseState to compare.
	 * @return {@code true} if the objects are equal; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof MouseState)
			return this.equals((MouseState) obj);
		return false;
	}

	/**
	 * Gets the hash code for MouseState instance.
	 * 
	 * @return Hash code of the object.
	 */
	@Override
	public int hashCode()
	{
		// TODO: Need to do check hash method
		return super.hashCode();	// base.GetHashCode();
	}

	/**
	 * Gets the horizontal position of the cursor.
	 * 
	 * @return The horizontal position of the cursor.
	 */
	public int getX()
	{
		return _x;
	}

	public void setX(int value)
	{
		_x = value;
	}

	/**
	 * Gets the vertical position of the cursor.
	 * 
	 * @return The vertical position of the cursor.
	 */
	public int getY()
	{
		return _y;
	}

	public void setY(int value)
	{
		_y = value;
	}

	/**
	 * Gets the cursor position.
	 * 
	 * @return The cursor position.
	 */
	public Point getPosition()
	{
		return new Point(_x, _y);
	}

	/**
	 * Gets the state of the left mouse button.
	 * 
	 * @return The state of the left mouse button.
	 */
	public ButtonState getLeftButton()
	{
		return _leftButton;
	}

	public void setLeftButton(ButtonState value)
	{
		_leftButton = value;
	}

	/**
	 * Gets the state of the middle mouse button.
	 * 
	 * @return The state of the middle mouse button.
	 */
	public ButtonState getMiddleButton()
	{
		return _middleButton;
	}

	public void setMiddleButton(ButtonState value)
	{
		_middleButton = value;
	}

	/**
	 * Gets the state of the right mouse button.
	 * 
	 * @return The state of the right mouse button.
	 */
	public ButtonState getRightButton()
	{
		return _rightButton;
	}

	public void setRightButton(ButtonState value)
	{
		_rightButton = value;
	}

	/**
	 * Returns the cumulative scroll wheel value since the game start.
	 * 
	 * @return The cumulative scroll wheel value since the game start.
	 */
	public int getScrollWheelValue()
	{
		return _scrollWheelValue;
	}

	public void setScrollWheelValue(int value)
	{
		_scrollWheelValue = value;
	}

	/**
	 * Gets the state of the XButton1.
	 * 
	 * @return The state of the XButton1.
	 */
	public ButtonState getXButton1()
	{
		return _xButton1;
	}

	public void setXButton1(ButtonState value)
	{
		_xButton1 = value;
	}

	/**
	 * Gets the state of the XButton2.
	 * 
	 * @return The state of the XButton2.
	 */
	public ButtonState getXButton2()
	{
		return _xButton2;
	}

	public void setXButton2(ButtonState value)
	{
		_xButton2 = value;
	}
}
