package gameCore.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the color channels for render target blending operations.
 * 
 * @author Eric
 *
 */
public enum ColorWriteChannels
{
	/**
	 * No channels selected.
	 */
	None(0),

	/**
	 * Red channel selected.
	 */
	Red(1),

	/**
	 * Green channel selected.
	 */
	Green(2),

	/**
	 * Blue channel selected.
	 */
	Blue(4),

	/**
	 * Alpha channel selected.
	 */
	Alpha(8),

	/**
	 * All channels selected.
	 */
	All(15);

	// NOTE: This is for flags
	private final int value;

	private ColorWriteChannels(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, ColorWriteChannels> map = new HashMap<Integer, ColorWriteChannels>();

	static
	{
		for (ColorWriteChannels colorWriteChannels : ColorWriteChannels.values())
		{
			map.put(colorWriteChannels.getValue(), colorWriteChannels);
		}
	}

	public static ColorWriteChannels valueOf(int colorWriteChannels)
	{
		return map.get(colorWriteChannels);
	}
}
