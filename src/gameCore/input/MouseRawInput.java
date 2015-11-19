package gameCore.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseRawInput implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int delta = -1;
	private static int clicks = -1;
	private static MouseButtons mouseButton = MouseButtons.None;

	public static int getX()
	{
		return mouseX;
	}

	public static int getY()
	{
		return mouseY;
	}

	public static int getDelta()
	{
		return delta;
	}

	public static int getClicks()
	{
		return clicks;
	}

	public static MouseButtons getButton()
	{
		return mouseButton;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e)
	{
		switch (e.getButton())
		{
			case 1:
				mouseButton = MouseButtons.Left;
				break;
			case 2:
				mouseButton = MouseButtons.Middle;
				break;
			case 3:
				mouseButton = MouseButtons.Right;
				break;
			case 4:
				mouseButton = MouseButtons.XButton1;
				break;
			case 5:
				mouseButton = MouseButtons.XButton2;
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseButton = MouseButtons.None;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// TODO: Should I add to delta and reset delta only when I pool the mouse ?
		delta = e.getWheelRotation();
	}
}
