package jMono_Framework.audio;

import jMono_Framework.dotNet.io.BinaryReader;
import jMono_Framework.dotNet.io.Stream;

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
	private AudioLoader() {}

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

	// ############################################################
	// #        Methods from MonoGame's AudioLoader class         #
	// ############################################################

//	private static ALFormat getSoundFormat(int channels, int bits)
//    {
//        switch (channels)
//        {
//            case 1: return bits == 8 ? OpenTK.Audio.OpenAL.ALFormat.Mono8 : OpenTK.Audio.OpenAL.ALFormat.Mono16;
//            case 2: return bits == 8 ? OpenTK.Audio.OpenAL.ALFormat.Stereo8 : OpenTK.Audio.OpenAL.ALFormat.Stereo16;
//            default: throw new NotSupportedException("The specified sound format is not supported.");
//        }
//    }

//    public static byte[] load(Stream data, ALFormat format, out int size, out int frequency)
	public static byte[] load(Stream data, AudioChannels format, int size, int frequency)
    {
        byte[] audioData = null;
        format = AudioChannels.Mono;
        size = 0;
        frequency = 0;

        try (BinaryReader reader = new BinaryReader(data))
        {
            // decide which data type is this

            // for now we'll only support wave files
//            audioData = loadWave(reader, out format, out size, out frequency);
        	audioData = loadWave(reader, format, size, frequency);
        }

        return audioData;
    }

//    private static byte[] loadWave(BinaryReader reader, out ALFormat format, out int size, out int frequency)
	private static byte[] loadWave(BinaryReader reader, AudioChannels format, int size, int frequency)
    {
        // code based on opentk exemple

        byte[] audioData;

        //header
        String signature = new String(reader.readChars(4));
        if (signature != "RIFF")
        {
            throw new UnsupportedOperationException("Specified stream is not a wave file.");
        }

		reader.readInt32(); // riff_chunck_size

        String wformat = new String(reader.readChars(4));
        if (wformat != "WAVE")
        {
            throw new UnsupportedOperationException("Specified stream is not a wave file.");
        }

        // WAVE header
        String format_signature = new String(reader.readChars(4));
        while (format_signature != "fmt ") {
            reader.readBytes(reader.readInt32());
            format_signature = new String(reader.readChars(4));
        }

        int format_chunk_size = reader.readInt32();

        // total bytes read: tbp
        int audio_format = reader.readInt16(); // 2
        int num_channels = reader.readInt16(); // 4
        int sample_rate = reader.readInt32();  // 8
		reader.readInt32();    // 12, byte_rate
		reader.readInt16();  // 14, block_align
        int bits_per_sample = reader.readInt16(); // 16

        if (audio_format != 1)
        {
            throw new UnsupportedOperationException("Wave compression is not supported.");
        }

        // reads residual bytes
        if (format_chunk_size > 16)
            reader.readBytes(format_chunk_size - 16);
        
        String data_signature = new String(reader.readChars(4));

        while (data_signature.toLowerCase() != "data")
        {
            reader.readBytes(reader.readInt32());
            data_signature = new String(reader.readChars(4));
        }

        if (data_signature != "data")
        {
            throw new UnsupportedOperationException("Specified wave file is not supported.");
        }

        int data_chunk_size = reader.readInt32();
        //TODO: Need to finish this class
AudioFormat af = new AudioFormat(sample_rate, bits_per_sample, num_channels, true, true);
        frequency = sample_rate;
        format = AudioChannels.valueOf(num_channels);	// getSoundFormat(num_channels, bits_per_sample);
        audioData = reader.readBytes((int)reader.getBaseStream().getLength());
        size = data_chunk_size;

        return audioData;
    }
}
