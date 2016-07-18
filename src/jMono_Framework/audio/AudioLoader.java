package jMono_Framework.audio;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioLoader
{
	// Make sure we can't instantiate this class
	private AudioLoader()
	{}

	// ############################################################
	// # Methods I added compared to MonoGame's AudioLoader class #
	// ############################################################
	public static byte[] load(AudioInputStream audioStream)
	{
		if (audioStream == null)
		{
			return null;
		}

		// get the number of bytes to read
		int length = (int) (audioStream.getFrameLength() * audioStream.getFormat().getFrameSize());

		// read the entire stream
		byte[] samples = new byte[length];
		DataInputStream is = new DataInputStream(audioStream);
		try
		{
			is.readFully(samples);
			is.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		// return the samples
		return samples;
	}

	public static AudioInputStream getAudioInputStream(String filename, AudioFormat format)
	{
		try
		{
			return getAudioInputStream(new FileInputStream(filename), format);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public static AudioInputStream getAudioInputStream(InputStream is, AudioFormat format)
	{
		try
		{
			if (!is.markSupported())
			{
				is = new BufferedInputStream(is);
			}
			// open the source stream
			AudioInputStream source = AudioSystem.getAudioInputStream(is);

			// convert to playback format
			return AudioSystem.getAudioInputStream(format, source);
		}
		catch (UnsupportedAudioFileException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

}
