package gameCore.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	/**
	 * The array of keys. Each index represent a key code and its value is
	 * {@code true} if it pressed, {@code false} otherwise.
	 */
	private boolean[] keysDown = new boolean[256];

	/**
	 * The number of Keys pressed.
	 */
	private int[] length = new int[256];

	@Override
	public void keyPressed(KeyEvent e) {
		keysDown[e.getKeyCode()] = true;
		length[e.getKeyCode()]++;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysDown[e.getKeyCode()] = false;
		length[e.getKeyCode()] = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public int[] getLength() {
		return length;
	}

	public boolean[] getKeysDown() {
		return keysDown.clone();
	}
}
