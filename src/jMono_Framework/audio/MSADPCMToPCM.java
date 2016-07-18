package jMono_Framework.audio;

import jMono_Framework.dotNet.BinaryReader;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;

/**
 * For more on the MSADPCM format, see the MultimediaWiki:
 * 
 * @see http://wiki.multimedia.cx/index.php?title=Microsoft_ADPCM
 * @author Ethan "flibitijibibo" Lee
 *
 */
public class MSADPCMToPCM
{
	/**
	 * A bunch of magical numbers that predict the sample data from the
	 * MSADPCM wavedata. Do not attempt to understand at all costs!
	 */
	private static final int[] AdaptionTable = {
			230, 230, 230, 230, 307, 409, 512, 614,
			768, 614, 512, 409, 307, 230, 230, 230
	};
	private static final int[] AdaptCoeff_1 = {
			256, 512, 0, 192, 240, 460, 392
	};
	private static final int[] AdaptCoeff_2 = {
			0, -256, 0, 64, 0, -208, -232
	};

	/**
	 * Splits the MSADPCM samples from each byte block.
	 * 
	 * @param block
	 *        An MSADPCM sample byte
	 * @param nibbleBlock
	 *        we copy the parsed shorts into here
	 */
	private static void getNibbleBlock(byte block, byte[] nibbleBlock)
	{
		nibbleBlock[0] = (byte) (block >> 4); // Upper half
		nibbleBlock[1] = (byte) (block & 0xF); // Lower half
	}

	/**
	 * Calculates PCM samples based on previous samples and a nibble input.
	 * 
	 * <p>
	 * Since Java has no ref parameters like C#, we are using an array of shorts which bundles up the three parameters that we want to be modified outside of the method.
	 * 
	 * @param nibble
	 *        A parsed MSADPCM sample we got from getNibbleBlock
	 * @param predictor
	 *        The predictor we get from the MSADPCM block's preamble
	 * @param refParameters
	 *        sample_1, sample_2 and delta bundled together :
	 * 			sample_1
	 * 				The first sample we use to predict the next sample
	 * 			sample_2
	 * 				The second sample we use to predict the next sample
	 * 			delta
	 * 				Used to calculate the final sample
	 * @return The calculated PCM sample
	 */
	private static short calculateSample(
			short nibble,
			byte predictor,
			short[] refParameters
	)
	{
		// unpack our short values
		short sample_1 = refParameters[0];
		short sample_2 = refParameters[1];
		short delta = refParameters[2];

		// Get a signed number out of the nibble. We need to retain the
		// original nibble value for when we access AdaptionTable[].
		byte signedNibble = (byte) nibble;
		if ((signedNibble & 0x8) == 0x8)
		{
			signedNibble -= 0x10;
		}

		// Calculate new sample
		int sampleInt = (
				((sample_1 * AdaptCoeff_1[predictor]) +
				(sample_2 * AdaptCoeff_2[predictor])
				) / 256
				);
		sampleInt += signedNibble * delta;

		// Clamp result to 16-bit
		short sample;
		if (sampleInt < Short.MIN_VALUE)
		{
			sample = Short.MIN_VALUE;
		}
		else if (sampleInt > Short.MAX_VALUE)
		{
			sample = Short.MAX_VALUE;
		}
		else
		{
			sample = (short) sampleInt;
		}

		// Shuffle samples, get new delta
		sample_2 = sample_1;
		sample_1 = sample;
		delta = (short) (AdaptionTable[nibble] * delta / 256);

		// Saturate the delta to a lower bound of 16
		if (delta < 16)
			delta = 16;

		// pack our short values back so they get modified outside this method
		refParameters[0] = sample_1;
		refParameters[1] = sample_2;
		refParameters[2] = delta;

		return sample;
	}

	/**
	 * Decodes MSADPCM data to signed 16-bit PCM data.
	 * 
	 * @param Source
	 *        A BinaryReader containing the headerless MSADPCM data
	 * @param numChannels
	 *        The number of channels (WAVEFORMATEX nChannels)
	 * @param blockAlign
	 *        The ADPCM block size (WAVEFORMATEX nBlockAlign)
	 * @return A byte array containing the raw 16-bit PCM wavedata
	 *
	 *         NOTE: The original MSADPCMToPCM class returns as a short[] array!
	 */
	public static byte[] MSADPCM_TO_PCM(
			BinaryReader source,
			short numChannels,
			short blockAlign
			)
	{
		// We write to output when reading the PCM data, then we convert
		// it back to a short array at the end.
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		FilterOutputStream pcmOut = new FilterOutputStream(output);	// BinaryWriter

		// We'll be using this to get each sample from the blocks.
		byte[] nibbleBlock = new byte[2];

		// Assuming the whole stream is what we want.
		long fileLength = source.getBaseStream().getLength() - blockAlign;

		// Mono or Stereo?
		if (numChannels == 1)
		{
			// Read to the end of the file.
			while (source.getBaseStream().getPosition() <= fileLength)
			{
				// Read block preamble
				byte predictor = source.readByte();
				short delta = source.readInt16();
				short sample_1 = source.readInt16();
				short sample_2 = source.readInt16();

				// Send the initial samples straight to PCM out.
				pcmOut.write(sample_2);
				pcmOut.write(sample_1);

				// pack our data so it can be modified from inside calculateSample 
				short[] refData = {sample_1, sample_2, delta};

				// Go through the bytes in this MSADPCM block.
				for (int bytes = 0; bytes < (blockAlign + 15); ++bytes)
				{
					// Each sample is one half of a nibbleBlock.
					getNibbleBlock(source.readByte(), nibbleBlock);
					for (int i = 0; i < 2; ++i)
					{
						pcmOut.write(
								calculateSample(
										nibbleBlock[i],
										predictor,
										refData
								)
								);
					}
				}
			}
		}
		else if (numChannels == 2)
		{
			// Read to the end of the file.
			while (source.getBaseStream().getPosition() <= fileLength)
			{
				// Read block preamble
				byte l_predictor = source.readByte();
				byte r_predictor = source.readByte();
				short l_delta = source.readInt16();
				short r_delta = source.readInt16();
				short l_sample_1 = source.readInt16();
				short r_sample_1 = source.readInt16();
				short l_sample_2 = source.readInt16();
				short r_sample_2 = source.readInt16();

				// Send the initial samples straight to PCM out.
				pcmOut.write(l_sample_2);
				pcmOut.write(r_sample_2);
				pcmOut.write(l_sample_1);
				pcmOut.write(r_sample_1);

				// pack our data so it can be modified from inside calculateSample 
				short[] refLeftData  = {l_sample_1, l_sample_2, l_delta};
				short[] refRightData = {r_sample_1, r_sample_2, r_delta};

				// Go through the bytes in this MSADPCM block.
				for (int bytes = 0; bytes < ((blockAlign + 15) * 2); ++bytes)
				{
					// Each block carries one left/right sample.
					getNibbleBlock(source.readByte(), nibbleBlock);

					// Left channel...
					pcmOut.write(
							calculateSample(
									nibbleBlock[0],
									l_predictor,
									refLeftData
							)
							);

					// Right channel...
					pcmOut.write(
							calculateSample(
									nibbleBlock[1],
									r_predictor,
									refRightData
							)
							);
				}
			}
		}
		else
		{
			System.out.println("MSADPCM WAVEDATA IS NOT MONO OR STEREO!");
			pcmOut.close();
			output.close();
			return null;
		}

		// We're done writing PCM data...
		pcmOut.close();
		output.close();

		// Return the array.
		return output.toByteArray();
	}
}
// TODO: Test this class to see if it works properly since C# bytes are unsigned
