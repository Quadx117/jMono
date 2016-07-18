package jMono_Framework.audio;

import jMono_Framework.math.MathHelper;

/**
 * Represents a single instance of a playing, paused, or stopped sound.
 * 
 * <p>
 * SoundEffectInstances are created through SoundEffect.CreateInstance() and used internally by SoundEffect.Play()
 * 
 * @author Eric Perron
 *
 */
public class SoundEffectInstance implements AutoCloseable
{
	private boolean _isDisposed = false;
	protected boolean _isPooled = true;
	protected boolean _isXAct;
	protected SoundEffect _effect;
	private float _pan;
	private float _volume;
	private float _pitch;

	/**
	 * Returns whether the {@code SoundEffectInstance} should repeat after playback.
	 * 
	 * <p>
	 * This value has no effect on an already playing sound.
	 * 
	 * @return {@code true} if the {@code SoundEffectInstance} should repeat after playback, {@code false otherwise}.
	 */
	public boolean isLooped() { return platformGetIsLooped(); }

	/**
	 * Enables or disables whether the {@code SoundEffectInstance} should repeat after playback.
	 * 
	 * @param value
	 *        {@code true} if the {@code SoundEffectInstance} should repeat after playback, {@code false otherwise}.
	 */
	public void setIsLooped(boolean value) { platformSetIsLooped(value); }

	/**
	 * Returns the pan, or speaker balance.
	 * 
	 * @return Pan value ranging from -1.0 (left speaker) to 0.0 (centered), 1.0 (right speaker).
	 */
	public float getPan() { return _pan; }

	/**
	 * Sets the pan, or speaker balance.
	 * 
	 * @param value
	 *        Pan value ranging from -1.0 (left speaker) to 0.0 (centered), 1.0 (right speaker). Values outside of this
	 *        range will throw an exception.
	 */
	public void setPan(float value)
	{
		if (value < -1.0f || value > 1.0f)
			throw new IllegalArgumentException("pan value is out of range");

		_pan = value;
		platformSetPan(value);
	}

	/**
	 * Returns the pitch adjustment.
	 * 
	 * @return Pitch adjustment, ranging from -1.0 (down an octave) to 0.0 (no change) to 1.0 (up an octave).
	 */
	public float getPitch() { return _pitch; }

	/**
	 * Sets the pitch adjustment.
	 * 
	 * @param value
	 *        Pitch adjustment, ranging from -1.0 (down an octave) to 0.0 (no change) to 1.0 (up an octave). Values
	 *        outside of this range will throw an Exception.
	 */
	public void setPitch(float value)
	{
		if (value < -1.0f || value > 1.0f)
			throw new IllegalArgumentException("pitch value is out of range");

		_pitch = value;
		platformSetPitch(value);
	}

	/**
	 * Returns the volume of the {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * This is the volume relative to {@link SoundEffect#getMasterVolume()}. Before playback, this Volume property is
	 * multiplied by {@link SoundEffect#getMasterVolume()} when determining the final mix volume.
	 * 
	 * @return Volume, ranging from 0.0 (silence) to 1.0 (full volume). Volume during playback is scaled by
	 *         SoundEffect.MasterVolume.
	 */
	public float getVolume() { return _volume; }

	/**
	 * Sets the volume of the {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * This is the volume relative to {@link SoundEffect#getMasterVolume()}. Before playback, this Volume property is
	 * multiplied by {@link SoundEffect#getMasterVolume()} when determining the final mix volume.
	 * 
	 * @param value
	 *        Volume, ranging from 0.0 (silence) to 1.0 (full volume). Volume during playback is scaled by
	 *        SoundEffect.MasterVolume.
	 */
	public void setVolume(float value)
	{
		// XAct sound effects don't have volume limits.
		if (!_isXAct && (value < 0.0f || value > 1.0f))
			throw new IllegalArgumentException("volume value is out of range");

		_volume = value;

		// XAct sound effects are not tied to the SoundEffect master volume.
		if (_isXAct)
			platformSetVolume(value);
		else
			platformSetVolume(value * SoundEffect.getMasterVolume());
	}

	/**
	 * Returns the {@code SoundEffectInstance}'s current playback state.
	 * 
	 * @return The {@code SoundEffectInstance}'s current playback state
	 */
	public SoundState getState() { return platformGetState(); }

	/**
	 * Indicates whether the object is disposed.
	 * 
	 * @return {@code true} if the object is disposed, {@code false} otherwise.
	 */
	public boolean isDisposed() { return _isDisposed; }

	protected SoundEffectInstance()
	{
		_pan = 0.0f;
		_volume = 1.0f;
		_pitch = 0.0f;
	}

	protected SoundEffectInstance(byte[] buffer, int sampleRate, int channels)
	{
		this();
		platformInitialize(buffer, sampleRate, channels);
	}

	/**
	 * Releases unmanaged resources and performs other cleanup operations before the {@code SoundEffectInstance} is
	 * reclaimed by garbage collection.
	 */
	@Override
	public void finalize()
	{
		dispose(false);
	}

	// TODO: AudioListener and AudioEmitter class
	/// <summary>Applies 3D positioning to the {@code SoundEffectInstance} using a single listener.</summary>
	/// <param name="listener">Data about the listener.</param>
	/// <param name="emitter">Data about the source of emission.</param>
	// public void apply3D(AudioListener listener, AudioEmitter emitter)
	// {
	// platformApply3D(listener, emitter);
	// }

	/// <summary>Applies 3D positioning to the {@code SoundEffectInstance} using multiple listeners.</summary>
	/// <param name="listeners">Data about each listener.</param>
	/// <param name="emitter">Data about the source of emission.</param>
	// public void Apply3D(AudioListener[] listeners, AudioEmitter emitter)
	// {
	// for (var l : listeners)
	// platformApply3D(l, emitter);
	// }

	/**
	 * Pauses playback of a {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * Paused instances can be resumed with {@link #play()} or {@link #resume()}.
	 */
	public void pause()
	{
		platformPause();
	}

	/**
	 * Plays or resumes a {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * Throws an exception if more sounds are playing than the platform allows.
	 */
	public void play()
	{
		if (getState() == SoundState.Playing)
			return;

		// We don't need to check if we're at the instance play limit
		// if we're resuming from a paused state.
		if (getState() != SoundState.Paused)
		{
			SoundEffectInstancePool.remove(this);

			if (!SoundEffectInstancePool.isSoundsAvailable())
				throw new IllegalStateException("There is no sound available to play");
		}

		// For non-XAct sounds we need to be sure the latest
		// master volume level is applied before playback.
		if (!_isXAct)
			platformSetVolume(_volume * SoundEffect.getMasterVolume());

		platformPlay();
	}

	/**
	 * Resumes playback for a {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * Only has effect on a {@code SoundEffectInstance} in a paused state.
	 */
	public void resume()
	{
		platformResume();
	}

	/**
	 * Immediately stops playing a {@code SoundEffectInstance}.
	 */
	public void stop()
	{
		platformStop(true);
	}

	/**
	 * Stops playing a {@code SoundEffectInstance}, either immediately or as authored.
	 * 
	 * <p>
	 * Stopping a sound with the immediate argument set to false will allow it to play any release phases, such as fade,
	 * before coming to a stop.
	 * 
	 * @param immediate
	 *        Determines whether the sound stops immediately, or after playing its release phase and/or transitions.
	 */
	public void stop(boolean immediate)
	{
		platformStop(immediate);
	}

	/**
	 * Releases the resources held by this {@code SoundEffectInstance}.
	 */
	@Override
	public void close()
	{
		dispose(true);
		// GC.SuppressFinalize(this);
	}

	/**
	 * Releases the resources held by this {@code SoundEffectInstance}.
	 * 
	 * <p>
	 * If the disposing parameter is {@code true}, the {@code dispose} method was called explicitly. This means that
	 * managed objects referenced by this instance should be disposed or released as required. If the disposing
	 * parameter is false, the {@code dispose} method was called by the finalizer and no managed objects should be
	 * touched because we do not know if they are still valid or not at that time. Unmanaged resources should always be
	 * released.
	 * 
	 * @param disposing
	 *        If set to {@code true}, Dispose was called explicitly.
	 */
	protected void dispose(boolean disposing)
	{
		if (!_isDisposed)
		{
			platformDispose(disposing);
			_isDisposed = true;
		}
	}

	// ########################################################################
	// #                    SoundEffectInstance.OpenAL.cs                     #
	// ########################################################################

//	private SoundState soundState = SoundState.Stopped;
//	private boolean _looped = false;
//	private float _alVolume = 1;

//	int sourceId;

//    private OALSoundBuffer soundBuffer;
//    private OpenALSoundController controller;
    
//    boolean hasSourceId = false;

    /// <summary>
    /// Creates a standalone SoundEffectInstance from given wavedata.
    /// </summary>
//    private void platformInitialize(byte[] buffer, int sampleRate, int channels)
//    {
//        initializeSound();
//        bindDataBuffer(
//            buffer,
//            (channels == 2) ? AudioChannels.Stereo : AudioChannels.Mono,
//            buffer.length,
//            sampleRate
//		    );
//    }

    /// <summary>
    /// Preserves the given data buffer by reference and binds its contents to the OALSoundBuffer
    /// that is created in the InitializeSound method.
    /// </summary>
    /// <param name="data">The sound data buffer</param>
    /// <param name="format">The sound buffer data format, e.g. Mono, Mono16 bit, Stereo, etc.</param>
    /// <param name="size">The size of the data buffer</param>
    /// <param name="rate">The sampling rate of the sound effect, e.g. 44 khz, 22 khz.</param>
//    protected void bindDataBuffer(byte[] data, AudioChannels format, int size, int rate)
//    {
//        soundBuffer.bindDataBuffer(data, format, size, rate);
//    }

    /// <summary>
    /// Gets the OpenAL sound controller, constructs the sound buffer, and sets up the event delegates for
    /// the reserved and recycled events.
    /// </summary>
//    public void initializeSound()
//    {
//        controller = OpenALSoundController.GetInstance;
//        soundBuffer = new OALSoundBuffer();
//        soundBuffer.Reserved += HandleSoundBufferReserved;
//        soundBuffer.Recycled += HandleSoundBufferRecycled;
//    }

    /// <summary>
    /// Event handler that resets internal state of this instance. The sound state will report
    /// SoundState.Stopped after this event handler.
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
//    private void handleSoundBufferRecycled(Object sender, EventArgs e)
//    {
//        sourceId = 0;
//        hasSourceId = false;
//        soundState = SoundState.Stopped;
        //Console.WriteLine ("recycled: " + soundEffect.Name);
//    }

    /// <summary>
    /// Called after the hardware has allocated a sound buffer, this event handler will
    /// maintain the numberical ID of the source ID.
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
//    private void handleSoundBufferReserved(Object sender, EventArgs e)
//    {
//        sourceId = soundBuffer.SourceId;
//        hasSourceId = true;
//    }

    /// <summary>
    /// Converts the XNA [-1, 1] pitch range to OpenAL pitch (0, INF) or Android SoundPool playback rate [0.5, 2].
    /// <param name="xnaPitch">The pitch of the sound in the Microsoft XNA range.</param>
    /// </summary>
//    private static float XnaPitchToAlPitch(float xnaPitch)
//    {
        /*XNA sets pitch bounds to [-1.0f, 1.0f], each end being one octave.
        •OpenAL's AL_PITCH boundaries are (0.0f, INF). *
        •Consider the function f(x) = 2 ^ x
        •The domain is (-INF, INF) and the range is (0, INF). *
        •0.0f is the original pitch for XNA, 1.0f is the original pitch for OpenAL.
        •Note that f(0) = 1, f(1) = 2, f(-1) = 0.5, and so on.
        •XNA's pitch values are on the domain, OpenAL's are on the range.
        •Remember: the XNA limit is arbitrarily between two octaves on the domain. *
        •To convert, we just plug XNA pitch into f(x).*/

//        if (xnaPitch < -1.0f || xnaPitch > 1.0f)
//            throw new IllegalArgumentException("XNA PITCH MUST BE WITHIN [-1.0f, 1.0f]!");

//        return (float)Math.pow(2, xnaPitch);
//    }

    // TODO: AudioListener and AudioEmitter
//    private void platformApply3D(AudioListener listener, AudioEmitter emitter)
//    {
//        // get AL's listener position
//        float x, y, z;
//        AL.GetListener(ALListener3f.Position, out x, out y, out z);
//
//        // get the emitter offset from origin
//        Vector3 posOffset = emitter.Position - listener.Position;
//        // set up orientation matrix
//        Matrix orientation = Matrix.CreateWorld(Vector3.Zero, listener.Forward, listener.Up);
//        // set up our final position and velocity according to orientation of listener
//        Vector3 finalPos = new Vector3(x + posOffset.X, y + posOffset.Y, z + posOffset.Z);
//        finalPos = Vector3.Transform(finalPos, orientation);
//        Vector3 finalVel = emitter.Velocity;
//        finalVel = Vector3.Transform(finalVel, orientation);
//
//        // set the position based on relative positon
//        AL.Source(sourceId, ALSource3f.Position, finalPos.X, finalPos.Y, finalPos.Z);
//        AL.Source(sourceId, ALSource3f.Velocity, finalVel.X, finalVel.Y, finalVel.Z);
//    }

//    private void platformPause()
//    {
//        if (!hasSourceId || soundState != SoundState.Playing)
//            return;

//        controller.PauseSound(soundBuffer);
//        soundState = SoundState.Paused;
//    }

//    private void platformPlay()
//    {
//        if (hasSourceId)
//            return;
        
//        boolean isSourceAvailable = controller.ReserveSource (soundBuffer);
//        if (!isSourceAvailable)
//            throw new InstancePlayLimitException();
//
//        int bufferId = soundBuffer.OpenALDataBuffer;
//        AL.Source(soundBuffer.SourceId, ALSourcei.Buffer, bufferId);
//
//        // Send the position, gain, looping, pitch, and distance model to the OpenAL driver.
//        if (!hasSourceId)
//			return;
//TODO: Do I need to set these in the other platformPlay() method ?
//		// Distance Model
//		AL.DistanceModel (ALDistanceModel.InverseDistanceClamped);
//		// Pan
//		AL.Source (sourceId, ALSource3f.Position, _pan, 0, 0.1f);
//		// Volume
//		AL.Source (sourceId, ALSourcef.Gain, _alVolume);
//		// Looping
//		AL.Source (sourceId, ALSourceb.Looping, IsLooped);
//		// Pitch
//		AL.Source (sourceId, ALSourcef.Pitch, XnaPitchToAlPitch(_pitch));
//
//        controller.PlaySound (soundBuffer);
        //Console.WriteLine ("playing: " + sourceId + " : " + soundEffect.Name);
//        soundState = SoundState.Playing;
//    }

//    private void platformResume()
//    {
//        if (!hasSourceId)
//        {
//            play();
//            return;
//        }
        
//        if (soundState == SoundState.Paused)
//            controller.ResumeSound(soundBuffer);
//        soundState = SoundState.Playing;
//    }

//    private void platformStop(boolean immediate)
//    {
//        if (hasSourceId)
//        {
            //Console.WriteLine ("stop " + sourceId + " : " + soundEffect.Name);
//            controller.StopSound(soundBuffer);
//        }
//        soundState = SoundState.Stopped;
//    }

//    private void platformSetIsLooped(boolean value)
//    {
//        _looped = value;
        
//        if (hasSourceId)
//            AL.Source(sourceId, ALSourceb.Looping, _looped);
//    }

//    private boolean platformGetIsLooped()
//    {
//        return _looped;
//    }

//    private void platformSetPan(float value)
//    {
//        if (hasSourceId)
//            AL.Source(sourceId, ALSource3f.Position, value, 0.0f, 0.1f);
//    }

//    private void platformSetPitch(float value)
//    {
//        if (hasSourceId)
//            AL.Source (sourceId, ALSourcef.Pitch, XnaPitchToAlPitch(value));
//    }

//    private SoundState platformGetState()
//    {
//        if (!hasSourceId)
//            return SoundState.Stopped;
        
//        var alState = AL.GetSourceState(sourceId);
//
//        switch (alState)
//        {
//            case ALSourceState.Initial:
//            case ALSourceState.Stopped:
//                soundState = SoundState.Stopped;
//                break;
//
//            case ALSourceState.Paused:
//                soundState = SoundState.Paused;
//                break;
//
//            case ALSourceState.Playing:
//                soundState = SoundState.Playing;
//                break;
//        }

//        return soundState;
//    }

//    private void platformSetVolume(float value)
//    {
//        _alVolume = value;

//        if (hasSourceId)
//            AL.Source(sourceId, ALSourcef.Gain, _alVolume);
//    }

//    private void platformDispose(boolean disposing)
//    {
//        if (disposing)
//        {
//            if (soundBuffer != null)
//            {
//                this.Stop(true);
//                soundBuffer.Reserved -= HandleSoundBufferReserved;
//                soundBuffer.Recycled -= HandleSoundBufferRecycled;
//                soundBuffer.Dispose();
//                soundBuffer = null;
//            }
//        }
//    }
    
    // ########################################################################
 	// #                    SoundEffectInstance.XAudio.cs                     #
 	// ########################################################################
    
//    internal SourceVoice _voice;
    private SourceVoice _voice;
    protected void setSourceVoice(SourceVoice voice)
    {
    	if (_voice != null)
    	{
    		_voice.dispose();
    	}
    	_voice = voice;
    }

    private static float[] _panMatrix;

    private boolean _paused;
    private boolean _loop;

    private void platformInitialize(byte[] buffer, int sampleRate, int channels)
    {
        throw new UnsupportedOperationException();
    }

 // TODO: AudioListener and AudioEmitter
//    private void platformApply3D(AudioListener listener, AudioEmitter emitter)
//    {
        // If we have no voice then nothing to do.
//        if (_voice == null)
//            return;

        // Convert from XNA Emitter to a SharpDX Emitter
//        var e = emitter.ToEmitter();
//        e.CurveDistanceScaler = SoundEffect.DistanceScale;
//        e.DopplerScaler = SoundEffect.DopplerScale;
//        e.ChannelCount = _effect._format.Channels;

        // Convert from XNA Listener to a SharpDX Listener
//        var l = listener.ToListener();

        // Number of channels in the sound being played.
        // Not actually sure if XNA supported 3D attenuation of sterio sounds, but X3DAudio does.
//        var srcChannelCount = _effect._format.Channels;

        // Number of output channels.
//        var dstChannelCount = SoundEffect.MasterVoice.VoiceDetails.InputChannelCount;

        // XNA supports distance attenuation and doppler.            
//        var dpsSettings = SoundEffect.Device3D.Calculate(l, e, CalculateFlags.Matrix | CalculateFlags.Doppler, srcChannelCount, dstChannelCount);

        // Apply Volume settings (from distance attenuation) ...
//        _voice.SetOutputMatrix(SoundEffect.MasterVoice, srcChannelCount, dstChannelCount, dpsSettings.MatrixCoefficients, 0);

        // Apply Pitch settings (from doppler) ...
//        _voice.SetFrequencyRatio(dpsSettings.DopplerFactor);
//    }

    private void platformPause()
    {
        if (_voice != null)
            _voice.stop();
        _paused = true;
    }

    private void platformPlay()
    {
        if (_voice != null)
        {
            // Choose the correct buffer depending on if we are looped.            
//            var buffer = _loop ? _effect._loopedBuffer : _effect._buffer;

//            if (_voice.State.BuffersQueued > 0)
//            {
//                _voice.Stop();
//                _voice.FlushSourceBuffers();
//            }
        	if (_voice.available() > 0)
        	{
        		_voice.stop();
        		_voice.flush();
        	}
//            _voice.SubmitSourceBuffer(buffer, null);
        	_voice.setFramePosition(0);

            _voice.start(_loop);
        }

        _paused = false;
    }

    private void platformResume()
    {
        if (_voice != null)
        {
            // Restart the sound if (and only if) it stopped playing
            if (!_loop)
            {
//                if (_voice.State.BuffersQueued == 0)
//                {
//                    _voice.Stop();
//                    _voice.FlushSourceBuffers();
//                    _voice.SubmitSourceBuffer(_effect._buffer, null);
//                }
            	if (_voice.available() == 0)
            	{
            		_voice.stop();
            		_voice.flush();
            		_voice.setFramePosition(0);
            	}
            }
            _voice.start(_loop);
        }
        _paused = false;
    }

    private void platformStop(boolean immediate)
    {
        if (_voice != null)
        {
//            if (immediate)
//            {
//                _voice.Stop(0);
        	_voice.stop();
//                _voice.FlushSourceBuffers();
        	_voice.flush();
//            }
//            else
//                _voice.Stop((int)PlayFlags.Tails);
        }

        _paused = false;
    }

    private void platformSetIsLooped(boolean value)
    {
        _loop = value;
    }

    private boolean platformGetIsLooped()
    {
        return _loop;
    }

    private void platformSetPan(float value)
    {
        // According to XNA documentation:
        // "Panning, ranging from -1.0f (full left) to 1.0f (full right). 0.0f is centered."
        _pan = MathHelper.clamp(value, -1.0f, 1.0f);

        // If we have no voice then nothing more to do.
        if (_voice == null || _effect == null)
            return;

        int srcChannelCount = _effect._format.getChannels();
        // TODO: SharpDX MasteringVoice (Mixer ?)
//        int dstChannelCount = SoundEffect.MasterVoice.VoiceDetails.InputChannelCount;
        int dstChannelCount = 2;

        if (_panMatrix == null || _panMatrix.length < dstChannelCount)
            _panMatrix = new float[Math.max(dstChannelCount, 8)];

        // Default to full volume for all channels/destinations   
        for (int i = 0; i < _panMatrix.length; ++i)
            _panMatrix[i] = 1.0f;

        // From X3DAudio documentation:
        /*
            For submix and mastering voices, and for source voices without a channel mask or a channel mask of 0, 
               XAudio2 assumes default speaker positions according to the following table. 

            Channels

            Implicit Channel Positions

            1 Always maps to FrontLeft and FrontRight at full scale in both speakers (special case for mono sounds) 
            2 FrontLeft, FrontRight (basic stereo configuration) 
            3 FrontLeft, FrontRight, LowFrequency (2.1 configuration) 
            4 FrontLeft, FrontRight, BackLeft, BackRight (quadraphonic) 
            5 FrontLeft, FrontRight, FrontCenter, SideLeft, SideRight (5.0 configuration) 
            6 FrontLeft, FrontRight, FrontCenter, LowFrequency, SideLeft, SideRight (5.1 configuration) (see the following remarks) 
            7 FrontLeft, FrontRight, FrontCenter, LowFrequency, SideLeft, SideRight, BackCenter (6.1 configuration) 
            8 FrontLeft, FrontRight, FrontCenter, LowFrequency, BackLeft, BackRight, SideLeft, SideRight (7.1 configuration) 
            9 or more No implicit positions (one-to-one mapping)                      
         */

        // Notes:
        //
        // Since XNA does not appear to expose any 'master' voice channel mask / speaker configuration,
        // I assume the mappings listed above should be used.
        //
        // Assuming it is correct to pan all channels which have a left/right component.

        float lVal = 1.0f - _pan;
        float rVal = 1.0f + _pan;

//        switch (SoundEffect.Speakers)
        switch(_effect.format)
        {
            case Stereo:
//            case Speakers.TwoPointOne:
//            case Speakers.Surround:
                _panMatrix[0] = lVal;
                _panMatrix[1] = rVal;
                break;

//            case Speakers.Quad:
//                _panMatrix[0] = _panMatrix[2] = lVal;
//                _panMatrix[1] = _panMatrix[3] = rVal;
//                break;

//            case Speakers.FourPointOne:
//                _panMatrix[0] = _panMatrix[3] = lVal;
//                _panMatrix[1] = _panMatrix[4] = rVal;
//                break;

//            case Speakers.FivePointOne:
//            case Speakers.SevenPointOne:
//            case Speakers.FivePointOneSurround:
//                _panMatrix[0] = _panMatrix[4] = lVal;
//                _panMatrix[1] = _panMatrix[5] = rVal;
//                break;

//            case Speakers.SevenPointOneSurround:
//                _panMatrix[0] = _panMatrix[4] = _panMatrix[6] = lVal;
//                _panMatrix[1] = _panMatrix[5] = _panMatrix[7] = rVal;
//                break;

            case Mono:
            default:
                // don't do any panning here   
                break;
        }

        // TODO: Finish pan implementation
//        _voice.setOutputMatrix(srcChannelCount, dstChannelCount, _panMatrix);
    }

    private void platformSetPitch(float value)
    {
        _pitch = value;

        if (_voice == null)
            return;

        // NOTE: This is copy of what XAudio2.SemitonesToFrequencyRatio() does
        // which avoids the native call and is actually more accurate.
        float pitch = (float)Math.pow(2.0, value);
         _voice.setFrequencyRatio(pitch);
    }

    private SoundState platformGetState()
    {
        // If no voice or no buffers queued the sound is stopped.
        if (_voice == null || !_voice.isRunning())	// || _voice.State.BuffersQueued == 0)
            return SoundState.Stopped;

        // Because XAudio2 does not actually provide if a SourceVoice is Started / Stopped
        // we have to save the "paused" state ourself.
        if (_paused)
            return SoundState.Paused;

        return SoundState.Playing;
    }

    private void platformSetVolume(float value)
    {
        if (_voice != null)
            _voice.setVolume(value);	//, XAudio2.CommitNow);
    }

    private void platformDispose(boolean disposing)
    {
        if (disposing)
        {
            if (_voice != null)
            {
//                _voice.destroyVoice();
                _voice.dispose();
            }
        }
        _voice = null;
        _effect = null;
    }
}
