package jMono_Framework.audio;

import jMono_Framework.time.TimeSpan;

import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;

public class SoundEffect implements AutoCloseable
{
	private String _name;

	private boolean _isDisposed = false;
	private TimeSpan _duration = TimeSpan.ZERO;

	private SoundEffect() {}

	/**
	 * Create a new {@code SoundEffect} instance.
	 * 
	 * @param buffer
	 *        Buffer containing PCM wave data.
	 * @param sampleRate
	 *        Sample rate, in Hertz (Hz)
	 * @param channels
	 *        Number of channels (mono or stereo).
	 */
	public SoundEffect(byte[] buffer, int sampleRate, AudioChannels channels)
	{
		_duration = getSampleDuration(buffer.length, sampleRate, channels);

		platformInitialize(buffer, sampleRate, channels);
	}

	/**
	 * Create a new {@code SoundEffect} instance.
	 * 
	 * <p>
	 * Use {@link #getSampleDuration()} to convert time to samples.
	 * 
	 * @param buffer
	 *        Buffer containing PCM wave data.
	 * @param offset
	 *        Offset, in bytes, to the starting position of the audio data.
	 * @param count
	 *        Amount, in bytes, of audio data.
	 * @param sampleRate
	 *        Sample rate, in Hertz (Hz)
	 * @param channels
	 *        Number of channels (mono or stereo).
	 * @param loopStart
	 *        The position, in samples, where the audio should begin looping.
	 * @param loopLength
	 *        The duration, in samples, that audio should loop over.
	 */
	public SoundEffect(byte[] buffer, int offset, int count, int sampleRate, AudioChannels channels, int loopStart,
			int loopLength)
	{
		_duration = getSampleDuration(count, sampleRate, channels);

		platformInitialize(buffer, offset, count, sampleRate, channels, loopStart, loopLength);
	}

	/**
	 * Releases unmanaged resources and performs other cleanup operations before the {@code SoundEffect} is reclaimed by
	 * garbage collection.
	 */
	@Override
	public void finalize()
	{
		dispose(false);
	}

	/**
	 * Creates a new {@link SoundEffectInstance} for this {@code SoundEffect}.
	 * 
	 * <p>
	 * Creating a {@code SoundEffectInstance} before calling {@link SoundEffectInstance#play()} allows you to access
	 * advanced playback features, such as volume, pitch, and 3D positioning.
	 * 
	 * @return A new {@code SoundEffectInstance} for this {@code SoundEffect}.
	 */
	public SoundEffectInstance createInstance()
	{
		SoundEffectInstance inst = new SoundEffectInstance();
		platformSetupInstance(inst);

		inst._isPooled = false;
		inst._effect = this;

		return inst;
	}

	/**
	 * Creates a {@code SoundEffect} object based on the specified data stream.
	 * 
	 * <p>
	 * The Stream object must point to the head of a valid PCM wave file. Also, this wave file must be in the RIFF
	 * bitstream format.
	 * 
	 * @param s
	 *        {@code Stream} object containing PCM wave data.
	 * @return A new {@code SoundEffect} object.
	 */
	public static SoundEffect fromStream(Stream s) // TODO: create my own or use FileInputStream or InputStream ?
	{
		if (s == null)
			throw new NullPointerException();

		// Notes from the docs:

		/*
		 * The Stream object must point to the head of a valid PCM wave file. Also, this wave file must be in the RIFF
		 * bitstream format.
		 * The audio format has the following restrictions:
		 * Must be a PCM wave file
		 * Can only be mono or stereo
		 * Must be 8 or 16 bit
		 * Sample rate must be between 8,000 Hz and 48,000 Hz
		 */

		SoundEffect sfx = new SoundEffect();

		sfx.platformLoadAudioStream(s);

		return sfx;
	}

	/**
	 * Returns the {@link TimeSpan} representation of a single sample.
	 * 
	 * @param sizeInBytes
	 *        Size, in bytes, of audio data.
	 * @param sampleRate
	 *        Sample rate, in Hertz (Hz). Must be between 8000 Hz and 48000 Hz
	 * @param channels
	 *        Number of channels in the audio data.
	 * @return {@code TimeSpan} object that represents the calculated sample duration.
	 */
	public static TimeSpan getSampleDuration(int sizeInBytes, int sampleRate, AudioChannels channels)
	{
		if (sampleRate < 8000 || sampleRate > 48000)
			throw new IllegalArgumentException();

		// Reference:
		// http://social.msdn.microsoft.com/Forums/windows/en-US/5a92be69-3b4e-4d92-b1d2-141ef0a50c91/how-to-calculate-duration-of-wave-file-from-its-size?forum=winforms
		int numChannels = channels.getValue();

		float dur = sizeInBytes / (sampleRate * numChannels * 16f / 8f);

		TimeSpan duration = TimeSpan.fromSeconds(dur);

		return duration;
	}

	/**
	 * Returns the size of a sample from a {@link TimeSpan}.
	 * 
	 * @param duration
	 *        {@code TimeSpan} object that contains the sample duration.
	 * @param sampleRate
	 *        Sample rate, in Hertz (Hz), of audio data. Must be between 8,000 and 48,000 Hz.
	 * @param channels
	 *        Number of channels in the audio data.
	 * @return Size of a single sample of audio data.
	 */
	public static int getSampleSizeInBytes(TimeSpan duration, int sampleRate, AudioChannels channels)
	{
		if (sampleRate < 8000 || sampleRate > 48000)
			throw new IllegalArgumentException();

		// Reference:
		// http://social.msdn.microsoft.com/Forums/windows/en-US/5a92be69-3b4e-4d92-b1d2-141ef0a50c91/how-to-calculate-duration-of-wave-file-from-its-size?forum=winforms

		int numChannels = channels.getValue();

		double sizeInBytes = duration.getTotalSeconds() * (sampleRate * numChannels * 16f / 8f);

		return (int) sizeInBytes;
	}

	/**
	 * Returns an internal {@link SoundEffectInstance} and plays it.
	 * 
	 * <p>
	 * Play returns {@code false} if more {@code SoundEffectInstance}s are currently playing than the platform allows.
	 * <p>
	 * To loop a sound or apply 3D effects, call {@link SoundEffect#createInstance()} and
	 * {@link SoundEffectInstance#play()} instead.
	 * <p>
	 * {@code SoundEffectInstance}s used by {@code SoundEffect.play()} are pooled internally.
	 * 
	 * @return {@code true} if a {@code SoundEffectInstance} was successfully played, false if not.
	 */
	public boolean play()
	{
		SoundEffectInstance inst = getPooledInstance(false);
		if (inst == null)
			return false;

		inst.play();

		return true;
	}

	/**
	 * Returns an internal {@link SoundEffectInstance} and plays it with the specified volume, pitch, and panning.
	 * 
	 * <p>
	 * Play returns {@code false} if more {@code SoundEffectInstance}s are currently playing than the platform allows.
	 * <p>
	 * To loop a sound or apply 3D effects, call {@link SoundEffect#createInstance()} and
	 * {@link SoundEffectInstance#play()} instead.
	 * <p>
	 * {@code SoundEffectInstance}s used by {@code SoundEffect.play()} are pooled internally.
	 * 
	 * @param volume
	 *        Volume, ranging from 0.0 (silence) to 1.0 (full volume). Volume during playback is scaled by
	 *        {@code SoundEffect.MasterVolume}.
	 * @param pitch
	 *        Pitch adjustment, ranging from -1.0 (down an octave) to 0.0 (no change) to 1.0 (up an octave).
	 * @param pan
	 *        Panning, ranging from -1.0 (left speaker) to 0.0 (centered), 1.0 (right speaker).
	 * @return {@code true} if a {@code SoundEffectInstance} was successfully created and played, false if not.
	 */
	public boolean play(float volume, float pitch, float pan)
	{
		SoundEffectInstance inst = getPooledInstance(false);
		if (inst == null)
			return false;

		inst.setVolume(volume);
		inst.setPitch(pitch);
		inst.setPan(pan);

		inst.play();

		return true;
	}

	/**
	 * Returns a sound effect instance from the pool or null if none are available.
	 * 
	 * @param forXAct
	 *        Whether the returned sound effect is for XAct or not.
	 * @return A sound effect instance from the pool or null if none are available.
	 */
	private SoundEffectInstance getPooledInstance(boolean forXAct)
	{
		if (!SoundEffectInstancePool.isSoundsAvailable())
			return null;

		SoundEffectInstance inst = SoundEffectInstancePool.getInstance(forXAct);
		inst._effect = this;
		platformSetupInstance(inst);

		return inst;
	}

	/**
	 * Returns the duration of the {@code SoundEffect}.
	 * 
	 * @return The duration of the {@code SoundEffect}.
	 */
	public TimeSpan getDuration()
	{
		return _duration;
	}

	/**
	 * Returns the asset name of this {@code SoundEffect}.
	 * 
	 * @return the asset name of this {@code SoundEffect}.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Sets the asset name of this {@code SoundEffect}.
	 * 
	 * @param value
	 *        The new asset name of this {@code SoundEffect}.
	 */
	public void setName(String value)
	{
		_name = value;
	}

	static float _masterVolume = 1.0f;

	/**
	 * Returns the master volume scale applied to all {@link SoundEffectInstance}s.
	 * 
	 * <p>
	 * Each {@code SoundEffectInstance} has its own volume property that is independent to
	 * {@code SoundEffect.MasterVolume}. During playback {@code SoundEffectInstance.Volume} is multiplied by
	 * {@code SoundEffect.MasterVolume}.
	 * <p>
	 * This property is used to adjust the volume on all current and newly created {@code SoundEffectInstance}s. The
	 * volume of an individual {@code SoundEffectInstance} can be adjusted on its own.
	 * 
	 * @return The master volume value of this {@code SoundEffect}.
	 */
	public static float getMasterVolume()
	{
		return _masterVolume;
	}

	/**
	 * Sets the master volume scale applied to all {@link SoundEffectInstance}s.
	 * 
	 * <p>
	 * Each {@code SoundEffectInstance} has its own volume property that is independent to
	 * {@code SoundEffect.MasterVolume}. During playback {@code SoundEffectInstance.Volume} is multiplied by
	 * {@code SoundEffect.MasterVolume}.
	 * <p>
	 * This property is used to adjust the volume on all current and newly created {@code SoundEffectInstance}s. The
	 * volume of an individual {@code SoundEffectInstance} can be adjusted on its own.
	 * 
	 * @param value
	 *        The new master volume value of this {@code SoundEffect}.
	 */
	public static void setMasterVolume(float value)
	{
		if (value < 0.0f || value > 1.0f)
			throw new IllegalArgumentException("value of masterVolume is out of range");

		if (_masterVolume == value)
			return;

		_masterVolume = value;
		SoundEffectInstancePool.updateMasterVolume();
	}

	static float _distanceScale = 1.0f;

	/**
	 * Returns the scale of distance calculations applied to sounds.
	 * 
	 * <p>
	 * DistanceScale defaults to 1.0 and must be greater than 0.0.
	 * <p>
	 * Higher values reduce the rate of falloff between the sound and listener.
	 * 
	 * @return The scale value of distance calculations applied to sounds.
	 */
	public static float getDistanceScale()
	{
		return _distanceScale;
	}

	/**
	 * Sets the scale of distance calculations applied to sounds.
	 * 
	 * <p>
	 * DistanceScale defaults to 1.0 and must be greater than 0.0.
	 * <p>
	 * Higher values reduce the rate of falloff between the sound and listener.
	 * 
	 * @param value
	 *        The new scale value of distance calculations applied to sounds.
	 */
	public static void setDistanceScale(float value)
	{
		if (value <= 0f)
			throw new IllegalArgumentException("value of DistanceScale is out of range");

		_distanceScale = value;
	}

	static float _dopplerScale = 1f;

	/**
	 * Returns the scale of Doppler calculations applied to sounds.
	 * 
	 * <p>
	 * DopplerScale defaults to 1.0 and must be greater or equal to 0.0
	 * <p>
	 * Affects the relative velocity of emitters and listeners.
	 * <p>
	 * Higher values more dramatically shift the pitch for the given relative velocity of the emitter and listener.
	 * 
	 * @return The scale value of Doppler calculations applied to sounds.
	 */
	public static float getDopplerScale()
	{
		return _dopplerScale;

	}

	/**
	 * Sets the scale of Doppler calculations applied to sounds.
	 * 
	 * <p>
	 * DopplerScale defaults to 1.0 and must be greater or equal to 0.0
	 * <p>
	 * Affects the relative velocity of emitters and listeners.
	 * <p>
	 * Higher values more dramatically shift the pitch for the given relative velocity of the emitter and listener.
	 * 
	 * @param value
	 *        The new scale value of Doppler calculations applied to sounds.
	 */
	public static void setDopplerScale(float value)
	{
		// As per documenation it does not look like the value can be less than 0
		// although the documentation does not say it throws an error we will anyway
		// just so it is like the DistanceScale
		if (value < 0.0f)
			throw new IllegalArgumentException("value of DopplerScale cannot be less than zero");

		_dopplerScale = value;
	}

	static float speedOfSound = 343.5f;

	/**
	 * Returns the speed of sound used when calculating the Doppler effect.
	 * 
	 * <p>
	 * Defaults to 343.5. Value is measured in meters per second.
	 * <p>
	 * Has no effect on distance attenuation.
	 * 
	 * @return
	 */
	public static float getSpeedOfSound()
	{
		return speedOfSound;
	}

	/**
	 * Stes the speed of sound used when calculating the Doppler effect.
	 * 
	 * @param value
	 *        the new speed of sound of this {@code SoundEffect}.
	 */
	public static void setSpeedOfSound(float value)
	{
		if (value <= 0.0f)
			throw new IllegalArgumentException();

		speedOfSound = value;
	}

	/**
	 * Indicates whether the object is disposed or not.
	 * 
	 * @return {@code true} if the object is disposed, {@code false} otherwise.
	 */
	public boolean isDisposed()
	{
		return _isDisposed;
	}

	/**
	 * Releases the resources held by this {@code SoundEffect}.
	 */
	@Override
	public void close()
	{
		dispose(true);
//		GC.SuppressFinalize(this);
	}

	/**
	 * Releases the resources held by this {@code SoundEffect}.
	 * 
	 * <p>
	 * If the disposing parameter is {@code true}, the {@code dispose} method was called explicitly. This means that
	 * managed objects referenced by this instance should be disposed or released as required. If the disposing
	 * parameter is {@code false}, {@code dispose} was called by the finalizer and no managed objects should be touched
	 * because we do not know if they are still valid or not at that time. Unmanaged resources should always be
	 * released.
	 * 
	 * @param disposing
	 *        If set to {@code true}, {@code dispose} was called explicitly.
	 */
	void dispose(boolean disposing)
	{
		if (!_isDisposed)
		{
			platformDispose(disposing);
			_isDisposed = true;
		}
	}

	// ########################################################################
	// #                        SoundEffect.OpenAL.cs                         #
	// ########################################################################

	// TODO: Should I change this to SourceDatatLine (more similar to SharpDX.XAudio2.AudioBuffer)
	// see SoundEffect.XAudio.cs
	private byte[] data;
	
	protected float rate;

	protected int size;

//	protected ALFormat Format { get; set; }
	protected AudioChannels format;

	private void platformLoadAudioStream(Stream s) // TODO: create my own or use FileInputStream or InputStream ? 
	{
// #if OPENAL && !(MONOMAC || IOS)
        
//		ALFormat format;
		AudioChannels format = null;
		int size = 0;
		int freq = 0;

		Stream stream = s;
//#if ANDROID
//		var needsDispose = false;
//		try
//		{
			// If seek is not supported (usually an indicator of a stream opened into the AssetManager), then copy
			// into a temporary MemoryStream.
//			if (!s.CanSeek)
//			{
//				needsDispose = true;
//				stream = new MemoryStream();
//				 s.CopyTo(stream);
//				stream.Position = 0;
//			}
//#endif
//			_data = AudioLoader.Load(stream, out format, out size, out freq);
//			data = AudioLoader.load(AudioLoader.getAudioInputStream(stream)); // TODO finish this method
//#if ANDROID
//		}
//		finally
//		{
//			if (needsDispose)
//				stream.Dispose();
//		}
//#endif
		this.format = format;
		this.size = size;
		this.rate = freq;
//#endif

//#if MONOMAC || IOS
//
//		var audiodata = new byte[s.Length];
//		s.Read(audiodata, 0, (int)s.Length);
//
//		using (AudioFileStream afs = new AudioFileStream (AudioFileType.WAVE))
//		{
//			afs.ParseBytes (audiodata, false);
//			Size = (int)afs.DataByteCount;
//
//			_data = new byte[afs.DataByteCount];
//			Array.Copy (audiodata, afs.DataOffset, _data, 0, afs.DataByteCount);
//
//			AudioStreamBasicDescription asbd = afs.DataFormat;
//			int channelsPerFrame = asbd.ChannelsPerFrame;
//			int bitsPerChannel = asbd.BitsPerChannel;
//
//			// There is a random chance that properties asbd.ChannelsPerFrame and asbd.BitsPerChannel are invalid because of a bug in Xamarin.iOS
//			// See: https://bugzilla.xamarin.com/show_bug.cgi?id=11074 (Failed to get buffer attributes error when playing sounds)
//			if (channelsPerFrame <= 0 || bitsPerChannel <= 0)
//			{
//				NSError err;
//				using (NSData nsData = NSData.FromArray(audiodata))
//				using (AVAudioPlayer player = AVAudioPlayer.FromData(nsData, out err))
//				{
//					channelsPerFrame = (int)player.NumberOfChannels;
//					bitsPerChannel = player.SoundSetting.LinearPcmBitDepth.GetValueOrDefault(16);
//
//					Rate = (float)player.SoundSetting.SampleRate;
//					_duration = TimeSpan.FromSeconds(player.Duration);
//				}
//			}
//			else
//			{
//				Rate = (float)asbd.SampleRate;
//				double duration = (Size / ((bitsPerChannel / 8) * channelsPerFrame)) / asbd.SampleRate;
//				_duration = TimeSpan.FromSeconds(duration);
//			}
//
//			if (channelsPerFrame == 1)
//				Format = (bitsPerChannel == 8) ? ALFormat.Mono8 : ALFormat.Mono16;
//			else
//				Format = (bitsPerChannel == 8) ? ALFormat.Stereo8 : ALFormat.Stereo16;
//		}
//
//#endif
    }
	
	private void platformInitialize(byte[] buffer, int sampleRate, AudioChannels channels)
	{
		rate = (float)sampleRate;
		size = (int)buffer.length;

//#if OPENAL && !(MONOMAC || IOS)

        data = buffer;
//        format = (channels == AudioChannels.Stereo) ? ALFormat.Stereo16 : ALFormat.Mono16;
        format = channels;
        
        // NOTE: Adde this
        _format = new AudioFormat(sampleRate, 16, channels.getValue(), true, false);
        return;

//#endif

//#if MONOMAC || IOS

        //buffer should contain 16-bit PCM wave data
//        short bitsPerSample = 16;

//        if ((int)channels <= 1)
//            Format = bitsPerSample == 8 ? ALFormat.Mono8 : ALFormat.Mono16;
//        else
//            Format = bitsPerSample == 8 ? ALFormat.Stereo8 : ALFormat.Stereo16;

//        _name = "";
//        _data = buffer;

//#endif
    }
	
//	private void platformInitialize(byte[] buffer, int offset, int count, int sampleRate, AudioChannels channels, int loopStart, int loopLength)
//    {
//        _duration = getSampleDuration(buffer.length, sampleRate, channels);
        
//        throw new UnsupportedOperationException();
//    }

//    private void platformSetupInstance(SoundEffectInstance inst)
//    {
//        inst.initializeSound();
//        inst.bindDataBuffer(data, format, size, (int)rate);
//    }

    private void platformDispose(boolean disposing)
    {
        // A no-op for OpenAL
    }

    public static void platformShutdown()
    {
//        OpenALSoundController.DestroyInstance();
    }
    
    // ########################################################################
    // #                        SoundEffect.XAudio.cs                         #
    // ########################################################################

//    private static XAudio2 Device { get; private set; }
//    private static MasteringVoice MasterVoice { get; private set; }

//    private static X3DAudio _device3D;
//    private static bool _device3DDirty = true;
//    private static Speakers _speakers = Speakers.Stereo;

 // XNA does not expose this, but it exists in X3DAudio.
//    [CLSCompliant(false)]
//    public static Speakers Speakers
//    {
//        get { return _speakers; }

//        set
//        {
//            if (_speakers == value)
//                return;
            
//            _speakers = value;
//            _device3DDirty = true;
//        }
//    }

//    internal static X3DAudio Device3D
//    {
//        get
//        {
//            if (_device3DDirty)
//            {
//                _device3DDirty = false;
//                _device3D = new X3DAudio(_speakers);
//            }

//            return _device3D;
//        }
//    }

//    internal DataStream _dataStream;
//    internal AudioBuffer _buffer;
//    internal AudioBuffer _loopedBuffer;
//    internal WaveFormat _format;
    protected AudioFormat _format;
    
//    static SoundEffect()
//    {
//        initializeSoundEffect();
//    }

    private void initializeSoundEffect()
    {
//        try
//        {
//            if (Device == null)
//            {
//                {
//                    Device = new XAudio2(XAudio2Flags.None, ProcessorSpecifier.DefaultProcessor);
//                    Device.StartEngine();
//                }
//            }

            // Just use the default device.
//            string deviceId = null;

//            if (MasterVoice == null)
//            {
                // Let windows autodetect number of channels and sample rate.
//                MasterVoice = new MasteringVoice(Device, XAudio2.DefaultChannels, XAudio2.DefaultSampleRate, deviceId);
//            }

            // The autodetected value of MasterVoice.ChannelMask corresponds to the speaker layout.
//            Speakers = (Speakers)MasterVoice.ChannelMask;
//        }
//        catch
//        {
            // Release the device and null it as
            // we have no audio support.
//            if (Device != null)
//            {
//                Device.Dispose();
//                Device = null;
//            }

//            MasterVoice = null;
//        }
    }

//    public void platformInitialize(byte[] buffer, int sampleRate, AudioChannels channels)
//    {
//        createBuffers(new WaveFormat(sampleRate, (int)channels),
//                      DataStream.Create(buffer, true, false),
//                      0, 
//                      buffer.Length);
//    }

    private void platformInitialize(byte[] buffer, int offset, int count, int sampleRate, AudioChannels channels, int loopStart, int loopLength)
    {
        // NOTE: Added this
        platformInitialize(buffer, sampleRate, channels);
        
//        createBuffers(new WaveFormat(sampleRate, (int)channels),
//                      DataStream.Create(buffer, true, false, offset),
//                      loopStart, 
//                      loopLength);
    }

//    private void platformLoadAudioStream(Stream s)
//    {
//        var soundStream = new SoundStream(s);
//        var dataStream = soundStream.ToDataStream();
//        var sampleLength = (int)(dataStream.Length / ((soundStream.Format.Channels * soundStream.Format.BitsPerSample) / 8));
//        createBuffers(soundStream.Format,
//                      dataStream,
//                      0,
//                      sampleLength);
//    }

//    private void createBuffers(WaveFormat format, DataStream dataStream, int loopStart, int loopLength)
//    {
//        _format = format;
//        _dataStream = dataStream;

//        _buffer = new AudioBuffer
//        {
//            Stream = _dataStream,
//            AudioBytes = (int)_dataStream.Length,
//            Flags = BufferFlags.EndOfStream,
//            PlayBegin = loopStart,
//            PlayLength = loopLength,
//            Context = new IntPtr(42),
//        };

//        _loopedBuffer = new AudioBuffer
//        {
//            Stream = _dataStream,
//            AudioBytes = (int)_dataStream.Length,
//            Flags = BufferFlags.EndOfStream,
//            LoopBegin = loopStart,
//            LoopLength = loopLength,
//            LoopCount = AudioBuffer.LoopInfinite,
//            Context = new IntPtr(42),
//        };
//    }

    private void platformSetupInstance(SoundEffectInstance inst)
    {
//        SourceVoice voice = null;
//        if (Device != null)
//            voice = new SourceVoice(Device, _format, VoiceFlags.None, XAudio2.MaximumFrequencyRatio);

    	SourceVoice voice = new SourceVoice(_format, data);
		
        inst.setSourceVoice(voice);
    }

//    private void platformDispose(boolean disposing)
//    {
//        if (disposing)
//        {
//            if (_dataStream != null)
//                _dataStream.Dispose();
//        }
//        _dataStream = null;
//    }

//    public static void platformShutdown()
//    {
//        if (MasterVoice != null)
//        {
//            MasterVoice.DestroyVoice();
//            MasterVoice.Dispose();
//            MasterVoice = null;
//        }
//
//        if (Device != null)
//        {
//            Device.StopEngine();
//            Device.Dispose();
//            Device = null;
//        }
//
//        _device3DDirty = true;
//        _speakers = Speakers.Stereo;
//    }
    
}
