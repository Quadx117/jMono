package jMono_Framework.input;

import java.util.List;

// C# struct

/**
 * Holds the state of keystrokes by a keyboard.
 * 
 * @author Eric Perron
 *
 */
public class KeyboardState
{
	// Used for the common situation where GetPressedKeys will return an empty array
	static Keys[] empty = new Keys[0];

	// Array of 256 bits:
	int keys0, keys1, keys2, keys3, keys4, keys5, keys6, keys7; // uint

	boolean internalGetKey(Keys key)
	{
		int mask = 1 << ((key.getValue()) & 0x1f); // uint

		int element; // uint
		switch ((key.getValue()) >> 5)
		{
			case 0:
				element = keys0;
				break;
			case 1:
				element = keys1;
				break;
			case 2:
				element = keys2;
				break;
			case 3:
				element = keys3;
				break;
			case 4:
				element = keys4;
				break;
			case 5:
				element = keys5;
				break;
			case 6:
				element = keys6;
				break;
			case 7:
				element = keys7;
				break;
			default:
				element = 0;
				break;
		}

		return (element & mask) != 0;
	}

	void internalSetKey(Keys key)
	{
		int mask = 1 << ((key.getValue()) & 0x1f); // uint
		switch ((key.getValue()) >> 5)
		{
			case 0:
				keys0 |= mask;
				break;
			case 1:
				keys1 |= mask;
				break;
			case 2:
				keys2 |= mask;
				break;
			case 3:
				keys3 |= mask;
				break;
			case 4:
				keys4 |= mask;
				break;
			case 5:
				keys5 |= mask;
				break;
			case 6:
				keys6 |= mask;
				break;
			case 7:
				keys7 |= mask;
				break;
		}
	}

	void internalClearKey(Keys key)
	{
		int mask = 1 << ((key.getValue()) & 0x1f); // uint
		switch ((key.getValue()) >> 5)
		{
			case 0:
				keys0 &= ~mask;
				break;
			case 1:
				keys1 &= ~mask;
				break;
			case 2:
				keys2 &= ~mask;
				break;
			case 3:
				keys3 &= ~mask;
				break;
			case 4:
				keys4 &= ~mask;
				break;
			case 5:
				keys5 &= ~mask;
				break;
			case 6:
				keys6 &= ~mask;
				break;
			case 7:
				keys7 &= ~mask;
				break;
		}
	}

	void internalClearAllKeys()
	{
		keys0 = 0;
		keys1 = 0;
		keys2 = 0;
		keys3 = 0;
		keys4 = 0;
		keys5 = 0;
		keys6 = 0;
		keys7 = 0;
	}

	/**
	 * Initializes a new instance of the {@link KeyboardState} class.
	 * 
	 * @param keys
	 *        List of keys to be flagged as pressed on initialization.
	 */
	protected KeyboardState(List<Keys> keys)
	{
		keys0 = 0;
		keys1 = 0;
		keys2 = 0;
		keys3 = 0;
		keys4 = 0;
		keys5 = 0;
		keys6 = 0;
		keys7 = 0;

		if (keys != null)
			for (Keys k : keys)
				internalSetKey(k);
	}

	/**
	 * Initializes a new instance of the {@code KeyboardState} class.
	 * 
	 * @param keys
	 *        List of keys to be flagged as pressed on initialization.
	 */
	public KeyboardState(Keys... keys)
	{
		keys0 = 0;
		keys1 = 0;
		keys2 = 0;
		keys3 = 0;
		keys4 = 0;
		keys5 = 0;
		keys6 = 0;
		keys7 = 0;

		if (keys != null)
			for (Keys k : keys)
				internalSetKey(k);
	}

	// / <summary>
	// / Returns the state of a specified key.
	// / </summary>
	// / <param name="key">The key to query.</param>
	// / <returns>The state of the key.</returns>
	public KeyState getKeyState(Keys key)
	{
		return internalGetKey(key) ? KeyState.Down : KeyState.Up;
	}

	// / <summary>
	// / Gets whether given key is currently being pressed.
	// / </summary>
	// / <param name="key">The key to query.</param>
	// / <returns>true if the key is pressed; false otherwise.</returns>
	public boolean isKeyDown(Keys key)
	{
		return internalGetKey(key);
	}

	// / <summary>
	// / Gets whether given key is currently being not pressed.
	// / </summary>
	// / <param name="key">The key to query.</param>
	// / <returns>true if the key is not pressed; false otherwise.</returns>
	public boolean isKeyUp(Keys key)
	{
		return !internalGetKey(key);
	}

	private static int countBits(int v) // uint
	{
		// http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
		v = v - ((v >> 1) & 0x55555555);                    // reuse input as temporary
		v = (v & 0x33333333) + ((v >> 2) & 0x33333333);     // temp
		return ((v + (v >> 4) & 0xF0F0F0F) * 0x1010101) >> 24; // count
	}

	private static int addKeysToArray(int keys, int offset, Keys[] pressedKeys, int index) // uint
	{
		for (int i = 0; i < 32; ++i)
		{
			if ((keys & (1 << i)) != 0)
				pressedKeys[index++] = Keys.valueOf(offset + i);
		}
		return index;
	}

	// / <summary>
	// / Returns an array of values holding keys that are currently being pressed.
	// / </summary>
	// / <returns>The keys that are currently being pressed.</returns>
	public Keys[] getPressedKeys()
	{
		int count = countBits(keys0) + countBits(keys1) + countBits(keys2) + countBits(keys3) // uint
				+ countBits(keys4) + countBits(keys5) + countBits(keys6) + countBits(keys7);
		if (count == 0)
			return empty;
		Keys[] keys = new Keys[count];

		int index = 0;
		if (keys0 != 0)
			index = addKeysToArray(keys0, 0 * 32, keys, index);
		if (keys1 != 0)
			index = addKeysToArray(keys1, 1 * 32, keys, index);
		if (keys2 != 0)
			index = addKeysToArray(keys2, 2 * 32, keys, index);
		if (keys3 != 0)
			index = addKeysToArray(keys3, 3 * 32, keys, index);
		if (keys4 != 0)
			index = addKeysToArray(keys4, 4 * 32, keys, index);
		if (keys5 != 0)
			index = addKeysToArray(keys5, 5 * 32, keys, index);
		if (keys6 != 0)
			index = addKeysToArray(keys6, 6 * 32, keys, index);
		if (keys7 != 0)
			index = addKeysToArray(keys7, 7 * 32, keys, index);

		return keys;
	}

	/**
	 * Returns the hash code for {@code KeyboardState} instance.
	 * 
	 * @return The hash code of this object.
	 */
	@Override
	public int hashCode()
	{
		return (int) (keys0 ^ keys1 ^ keys2 ^ keys3 ^ keys4 ^ keys5 ^ keys6 ^ keys7);
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj.getClass() != this.getClass())
		{
			return false;
		}
		return this.equals((KeyboardState) obj);
	}
	
	// Helper method
	private boolean equals(KeyboardState other)
	{
		return this.keys0 == other.keys0 && //
			   this.keys1 == other.keys1 && //
			   this.keys2 == other.keys2 && //
			   this.keys3 == other.keys3 && //
			   this.keys4 == other.keys4 && //
			   this.keys5 == other.keys5 && //
			   this.keys6 == other.keys6 && ///
			   this.keys7 == other.keys7;
	}

	/**
	 * Indicates whether some other object is "not equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see #equals(Object)
	 */
	public boolean notEquals(Object obj)
	{
		return !this.equals(obj);
	}
	
}
