package jMono_Framework.dotNet.io;

/**
 * Provides seek reference points. To seek to the end of a stream, call stream.Seek(0, SeekOrigin.End).
 * 
 * @author Eric Perron (based on Microsoft .NET)
 *
 */
public enum SeekOrigin
{
	// These constants match Win32's FILE_BEGIN, FILE_CURRENT, and FILE_END
	Begin(0),
	Current(1),
	End(2);

	// TODO: Do I really need this since the values already ranges from 0-2 ?
	// NOTE: This is for flags
	private final int value;

	private SeekOrigin(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
