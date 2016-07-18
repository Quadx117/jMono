package jMono_Framework.content.contentReaders;

import jMono_Framework.audio.AudioChannels;
import jMono_Framework.audio.MSADPCMToPCM;
import jMono_Framework.audio.SoundEffect;
import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;
import jMono_Framework.dotNet.BinaryReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SoundEffectReader extends ContentTypeReader<SoundEffect>
{
// #if ANDROID
//	static string[] supportedExtensions = new string[] { ".wav", ".mp3", ".ogg", ".mid" };
// #else
	static String[] supportedExtensions = new String[] { ".wav", ".aiff", ".ac3", ".mp3" };

// #endif

	protected static String normalize(String fileName)
	{
		return normalize(fileName, supportedExtensions);
	}

	public SoundEffectReader()
	{
		super(SoundEffect.class);
	}

	@Override
	protected SoundEffect read(ContentReader input, SoundEffect existingInstance)
	{
		// NXB format for SoundEffect...
		//
		// Byte [format size] Format WAVEFORMATEX structure
		// UInt32 Data size
		// Byte [data size] Data Audio waveform data
		// Int32 Loop start In bytes (start must be format block aligned)
		// Int32 Loop length In bytes (length must be format block aligned)
		// Int32 Duration In milliseconds

		// WAVEFORMATEX structure...
		//
		// typedef struct {
		// WORD wFormatTag; // byte[0] +2
		// WORD nChannels; // byte[2] +2
		// DWORD nSamplesPerSec; // byte[4] +4
		// DWORD nAvgBytesPerSec; // byte[8] +4
		// WORD nBlockAlign; // byte[12] +2
		// WORD wBitsPerSample; // byte[14] +2
		// WORD cbSize; // byte[16] +2
		// } WAVEFORMATEX;

		byte[] header = input.readBytes(input.readInt32());
		byte[] data = input.readBytes(input.readInt32());
		int loopStart = input.readInt32();
		int loopLength = input.readInt32();
		input.readInt32();

// #if DIRECTX
//		int count = data.Length;
//		int format = (int)BitConverter.ToUInt16(header, 0);
//		int sampleRate = (int)BitConverter.ToUInt16(header, 4);
//		int channels = BitConverter.ToUInt16(header, 2);
		// var avgBPS = (int)BitConverter.ToUInt16(header, 8); // NOTE: Alrady commented
//		int blockAlignment = (int)BitConverter.ToUInt16(header, 12);
		// var bps = (int)BitConverter.ToUInt16(header, 14); // NOTE: Alrady commented

//		SharpDX.Multimedia.WaveFormat waveFormat;
//		if (format == 1)
//			waveFormat = new SharpDX.Multimedia.WaveFormat(sampleRate, channels);
//		else if (format == 2)
//			waveFormat = new SharpDX.Multimedia.WaveFormatAdpcm(sampleRate, channels, blockAlignment);
//		else
//			throw new NotSupportedException("Unsupported wave format!");

//		return new SoundEffect(data, 0, count, sampleRate, (AudioChannels)channels, loopStart, loopLength)
//		{
//			_format = waveFormat,
//			Name = input.AssetName,
//		};
// #else
		if (loopStart == loopLength)
		{
			// do nothing. just killing the warning for non-DirectX path
		}
		if (header[0] == 2 && header[1] == 0)
		{
			// We've found MSADPCM data! Let's decode it here.
			try (ByteArrayInputStream origDataStream = new ByteArrayInputStream(data))
			{
				try (BinaryReader reader = new BinaryReader(origDataStream))
				{
					byte[] newData = MSADPCMToPCM.MSADPCM_TO_PCM(
							reader,
							header[2],
							(short) ((header[12] / header[2]) - 22)
							);
					data = newData;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			// This is PCM data now!
			header[0] = 1;
		}

		int sampleRate = (
					(header[4]) & 0xff |
					(header[5] << 8) & 0xff00 |
					(header[6] << 16) & 0xff0000 |
					(header[7] << 24) & 0xff000000
				);

		AudioChannels channels = (header[2] == 2) ? AudioChannels.Stereo : AudioChannels.Mono;
		SoundEffect result = new SoundEffect(data, sampleRate, channels);
		result.setName(input.getAssetName());
		return result;
// #endif
	}
}
