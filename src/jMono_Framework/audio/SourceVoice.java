package jMono_Framework.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

// TODO: Should I add a list of clips like in SharpDX.XAudio2.SourceVoice ?
// @see https://github.com/sharpdx/SharpDX/blob/master/Source/SharpDX.XAudio2/SourceVoice.cs
// @see https://msdn.microsoft.com/en-us/library/windows/desktop/microsoft.directx_sdk.ixaudio2sourcevoice.ixaudio2sourcevoice.aspx
public class SourceVoice
{
	// NOTE: Types of Control for a Clip: Master Gain, Mute
	// NOTE: If PLAYBACK_FORMAT == stereo (2 channels) Balance, Pan
	// TODO: What's the difference between Balance and Pan ?
	private FloatControl _panController;
	private FloatControl _volumeController;

	public final AudioFormat PLAYBACK_FORMAT;
	private Clip line;		// TODO: SourceDataLine or Clip ?
	private byte[] data;	// TODO: Do I need to store this ?

	public boolean isLooped = false;

	public SourceVoice(AudioFormat format, byte[] data)
	{
		this.data = data;
		PLAYBACK_FORMAT = format;
		initialize();
	}

	private void initialize()
	{
		initialize(PLAYBACK_FORMAT);
	}

	/**
	 * Creates the Thread's line and initialize all the controls
	 */
	private void initialize(AudioFormat format)
	{
		// create, open, and start the line
		DataLine.Info lineInfo = new DataLine.Info(Clip.class, PLAYBACK_FORMAT);	// TODO: SourceDataLine or Clip ?
		try
		{
			line = (Clip) AudioSystem.getLine(lineInfo);	// TODO: SourceDataLine or Clip ?
			line.open(PLAYBACK_FORMAT, data, 0, data.length);
		}
		catch (LineUnavailableException ex)
		{
			// the line is unavailable
			ex.printStackTrace();
			System.out.println(ex.toString());
			return;
		}

//		line.addLineListener(this);

		_volumeController = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		// TODO: Check panning if PLAYBACK_FORMAT == Mono (1 channel)
//		_panController = (FloatControl) line.getControl(FloatControl.Type.PAN);
//		System.out.println(_panController.getMaximum());
//		System.out.println(_panController.getMinimum());
	}

	/**
     * Listens to the START and STOP events of the audio line.
     */
//    @Override
//    public void update(LineEvent event)
//    {
//        LineEvent.Type type = event.getType();
         
//        if (type == LineEvent.Type.START)
//        {
        	// Do nothing
//        }
//        else if (type == LineEvent.Type.STOP)
//        {
			// Do nothing
//        }
 
//    }

	public void start(boolean loop)
	{
//		if (line != null)
//		{
			if (loop == true)
			{
				line.loop(Clip.LOOP_CONTINUOUSLY);
			}
			else
			{
				line.loop(0);
			}
//		}
	}

	public void stop()
	{
		if (line != null)
		{
			line.stop();
		}
	}

	public void flush()
	{
		line.flush();
	}
	
	public void setFramePosition(int value)
	{
		line.setFramePosition(value);
	}
	
	public void setVolume(float value)
	{
		// Interpolate the 0-1 value to the range of _volumeController min and max
		float volume = (_volumeController.getMaximum() - _volumeController.getMinimum()) * value + _volumeController.getMinimum();
		_volumeController.setValue(volume);
	}

	// TODO: test this
	public void setFrequencyRatio(float value)
	{
		float sampleRate = PLAYBACK_FORMAT.getSampleRate() * value;
		AudioFormat format = new AudioFormat(sampleRate,
											 PLAYBACK_FORMAT.getSampleSizeInBits(),
											 PLAYBACK_FORMAT.getChannels(),
											 PLAYBACK_FORMAT.getEncoding() == Encoding.PCM_SIGNED,
											 PLAYBACK_FORMAT.isBigEndian());
		dispose();
		initialize(format);
	}
	
	/**
	 * Drains and closes the line.
	 */
	public void dispose()
	{
		if (line != null)
		{
			line.stop();
			line.flush();
			line.close();
		}
		line = null;
	}

	public boolean isRunning()
	{
		boolean result = false;
		
		if (line != null)
		{
			result = line.isRunning();
		}
		
		return result;
	}

	public int available()
	{
		return line.available();
	}
}
