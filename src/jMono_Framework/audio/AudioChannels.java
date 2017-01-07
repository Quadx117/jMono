package jMono_Framework.audio;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents how many channels are used in the audio data.
 * 
 * @author Eric Perron
 *
 */
public enum AudioChannels
{
	/**
	 * Single channel.
	 */
	Mono(1),
	/**
	 * Two channels.
	 */
	Stereo(2);

	// NOTE: This is for flags
	private final int value;

	private AudioChannels(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	// NOTE: This is to convert the data read from the disk into an enum value
	private static Map<Integer, AudioChannels> map = new HashMap<Integer, AudioChannels>();

	static
	{
		for (AudioChannels audioChannels : AudioChannels.values())
		{
			map.put(audioChannels.ordinal(), audioChannels);
		}
	}

	public static AudioChannels valueOf(int audioChannels)
	{
		return map.get(audioChannels);
	}
}
