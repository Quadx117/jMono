package jMono_Framework.audio;

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
}
