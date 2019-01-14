package jMono_Framework.media;

import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.math.MathHelper;
import jMono_Framework.time.TimeSpan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MediaPlayer
{
	// Need to hold onto this to keep track of how many songs
	// have played when in shuffle mode
	private static int _numSongsInQueuePlayed = 0;
	private static MediaState _state = MediaState.Stopped;
	private static float _volume = 1.0f;
	private static boolean _isMuted;
	private static boolean _isRepeating;
	private static boolean _isShuffled;
	private static final MediaQueue _queue = new MediaQueue();

// #if WINDOWS_PHONE
	// playingInternal should default to true to be to work with the user's default playing music
	private static boolean playingInternal = true;
// #endif

	public static Event<EventArgs> ActiveSongChanged = new Event<EventArgs>();
	public static Event<EventArgs> MediaStateChanged = new Event<EventArgs>();

	static
	{
		platformInitialize();
	}

	// #region Properties

	public static MediaQueue getQueue()
	{
		return _queue;
	}

	public static boolean isMuted()
	{
		return platformGetIsMuted();
	}

	public static void setIsMuted(boolean value)
	{
		platformSetIsMuted(value);
	}

	public static boolean isRepeating()
	{
		return platformGetIsRepeating();
	}

	public static void setIsRepeating(boolean value)
	{
		platformSetIsRepeating(value);
	}

	public static boolean isShuffled()
	{
		return platformGetIsShuffled();
	}

	public static void setIsShuffled(boolean value)
	{
		platformSetIsShuffled(value);
	}

	public static boolean isVisualizationEnabled()
	{
		return false;
	}

	public static TimeSpan getPlayPosition()
	{
		return platformGetPlayPosition();
	}

	public static void setPlayPosition(TimeSpan value)
	{
// #if IOS || ANDROID
		// platformSetPlayPosition(value);
// #endif
	}

	public static MediaState getState()
	{
		return platformGetState();
	}

	private static void setState(MediaState value)
	{
		if (_state != value)
		{
			_state = value;
			if (MediaStateChanged != null)
			{
// #if WINDOWS_PHONE
				// Playing music using XNA, we shouldn't fire extra state changed events
				if (!playingInternal)
				{
// #endif
					MediaStateChanged.handleEvent(null, EventArgs.Empty);
				}
			}
		}
	}

	public static boolean getGameHasControl()
	{
		return platformGetGameHasControl();
	}

	public static float getVolume()
	{
		return platformGetVolume();
	}

	public static void setVolume(float value)
	{
		float volume = MathHelper.clamp(value, 0f, 1f);

		platformSetVolume(volume);
	}

	// #endregion

	public static void pause()
	{
		if (getState() != MediaState.Playing || _queue.getActiveSong() == null)
			return;

		platformPause();

		setState(MediaState.Paused);
	}

	/**
	 * Play clears the current playback queue, and then queues up the specified song for playback.
	 * Playback starts immediately at the beginning of the song.
	 * 
	 * @param song
	 * 			The song to be played by the MediaPlayer.
	 */
	public static void play(Song song)
	{
		Song previousSong = _queue.getCount() > 0 ? _queue.getSong(0) : null;
		_queue.clear();
		_numSongsInQueuePlayed = 0;
		_queue.add(song);
		_queue.setActiveSongIndex(0);

		playSong(song);

		if (song.notEquals(previousSong) && ActiveSongChanged != null)
			// TODO: should invoke be handleEevent
			// ActiveSongChanged.Invoke(null, EventArgs.Empty);
			ActiveSongChanged.handleEvent(null, EventArgs.Empty);

		// TODO: should I add this to satisfy the compiler ?
		if (previousSong != null)
			previousSong.close();
	}

	// TODO: this method + 1 overload
	// public static void play(SongCollection collection, int index = 0)
	// {
	// _queue.clear();
	// _numSongsInQueuePlayed = 0;
	//
	// foreach(var song in collection)
	// _queue.Add(song);
	//
	// _queue.ActiveSongIndex = index;
	//
	// PlaySong(_queue.ActiveSong);
	// }

	private static void playSong(Song song)
	{
		platformPlaySong(song);
		setState(MediaState.Playing);
	}

	protected static void onSongFinishedPlaying(Object sender, EventArgs args)
	{
		// TODO: Check args to see if song sucessfully played
		_numSongsInQueuePlayed++;

		if (_numSongsInQueuePlayed >= _queue.getCount())
		{
			_numSongsInQueuePlayed = 0;
			if (!isRepeating())
			{
				stop();

				if (ActiveSongChanged != null)
				{
					// TODO: should invoke be handleEvent
					// ActiveSongChanged.Invoke(null, null);
					ActiveSongChanged.handleEvent(null, null);
				}

				return;
			}
		}

// #if WINDOWS_PHONE
		// if (IsRepeating)
		// {
		// System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
		// {
		// _mediaElement.Position = TimeSpan.Zero;
		// _mediaElement.Play();
		// });
		// }
// #endif

		moveNext();
	}

	public static void resume()
	{
		if (getState() != MediaState.Paused)
			return;

		platformResume();
		setState(MediaState.Playing);
	}

	public static void stop()
	{
		if (getState() == MediaState.Stopped)
			return;

		platformStop();
		setState(MediaState.Stopped);
	}

	public static void moveNext()
	{
		nextSong(1);
	}

	public static void movePrevious()
	{
		nextSong(-1);
	}

	private static void nextSong(int direction)
	{
		stop();

		if (isRepeating() && _queue.getActiveSongIndex() >= _queue.getCount() - 1)
		{
			_queue.setActiveSongIndex(0);

			// Setting direction to 0 will force the first song
			// in the queue to be played.
			// if we're on "shuffle", then it'll pick a random one
			// anyway, regardless of the "direction".
			direction = 0;
		}

		Song nextSong = _queue.getNextSong(direction, isShuffled());

		if (nextSong != null)
			playSong(nextSong);

		if (ActiveSongChanged != null)
		{
			// TODO: should invoke be handleEvent
			// ActiveSongChanged.Invoke(null, null);
			ActiveSongChanged.handleEvent(null, null);
		}
	}

	// @formatter:off
	// ########################################################################
	// #                        MediaPlayer.Default.cs                        #
	// ########################################################################
	// @formatter:on

	// #region Properties
	/*
	private static void platformInitialize()
	{

	}

	private static boolean platformGetIsMuted()
	{
		return _isMuted;
	}

	private static void platformSetIsMuted(boolean muted)
	{
		_isMuted = muted;

		if (_queue.getCount() == 0)
			return;

		float newVolume = _isMuted ? 0.0f : _volume;
		_queue.setVolume(newVolume);
	}

	private static boolean platformGetIsRepeating()
	{
		return _isRepeating;
	}

	private static void platformSetIsRepeating(boolean repeating)
	{
		_isRepeating = repeating;
	}

	private static boolean platformGetIsShuffled()
	{
		return _isShuffled;
	}

	private static void platformSetIsShuffled(boolean shuffled)
	{
		_isShuffled = shuffled;
	}

	private static TimeSpan platformGetPlayPosition()
	{
		if (_queue.getActiveSong() == null)
			return TimeSpan.ZERO;

		return _queue.getActiveSong().getPosition();
	}

// #if IOS || ANDROID
	// private static void platformSetPlayPosition(TimeSpan playPosition)
	// {
		// if (_queue.getActiveSong() != null)
			// _queue.getActiveSong().setPosition(playPosition);
	// }
// #endif

	private static MediaState platformGetState()
	{
		return _state;
	}

	private static float platformGetVolume()
	{
		return _volume;
	}

	private static void platformSetVolume(float volume)
	{
		_volume = volume;

		if (_queue.getActiveSong() == null)
			return;

		_queue.setVolume(_isMuted ? 0.0f : _volume);
	}

	private static boolean platformGetGameHasControl()
	{
// #if IOS
		// boolean isOtherAudioPlaying;
		// AVAudioSession avAudioSession = AVAudioSession.SharedInstance();
		// if (avAudioSession.RespondsToSelector(new ObjCRuntime.Selector("isOtherAudioPlaying")))
			// isOtherAudioPlaying = avAudioSession.OtherAudioPlaying; // iOS 6+
		// else
			// isOtherAudioPlaying = AudioSession.OtherAudioIsPlaying;
		// return !isOtherAudioPlaying;
// #else
		// TODO: Fix me!
		return true;
// #endif
	}
	// #endregion

	private static void platformPause()
	{
		if (_queue.getActiveSong() == null)
		return;

		_queue.getActiveSong().pause();
	}

	private static void platformPlaySong(Song song)
	{
		if (_queue.getActiveSong() == null)
		return;

		song.setEventHandler(MediaPlayer::onSongFinishedPlaying);

		song.setVolume(_isMuted ? 0.0f : _volume);
		song.play();
	}

	private static void platformResume()
	{
		if (_queue.getActiveSong() == null)
		return;

		_queue.getActiveSong().resume();
	}

	private static void platformStop()
	{
		// Loop through so that we reset the PlayCount as well
		while (getQueue().getSongs().hasNext())
		{
			getQueue().getSongs().next();
			_queue.getActiveSong().stop();
		}
	}
	 */

	// @formatter:off
	// ########################################################################
	// #                          MediaPlayer.WMS.cs                          #
	// ########################################################################
	// @formatter:on

	// NOTE: Things I added for Java sound playback
	// uncompressed, 44100Hz, 16-bit, stereo, signed, little-endian
	public static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 2, true, false);
	// TODO: Should I querry audioStream.getFormat().getChannels() in Song.getSound(AudioInputStream audioStream)
	//       to check how many channels we have or simply put 2 ? check how it works in MonoGame

	private static ThreadLocal<SourceDataLine> localLine;
	private static ThreadLocal<byte[]> localBuffer;
	private static Object pausedLock;

	static
	{
		localLine = new ThreadLocal<SourceDataLine>();
		localBuffer = new ThreadLocal<byte[]>();
		pausedLock = new Object();
	}
	// END NOTE

	// private static MediaSession _session;
	// private static AudioStreamVolume _volumeController;
	// NOTE: Types of Control for a SourceDataLine: Master Gain, Mute, Balance, Pan
	private static FloatControl _volumeController;
	// private static PresentationClock _clock;
	private static Song _nextSong;
	private static Song _currentSong;

	private enum SessionState
	{
		Stopped, Stopping, Started, Paused, Ended
	}

	private static SessionState _sessionState = SessionState.Stopped;

	// private static Guid AudioStreamVolumeGuid;

	// private static Variant PositionCurrent = new Variant();
	// private static Variant PositionBeginning = new Variant { ElementType = VariantElementType.Long, Value = 0L };

	// private static Callback _callback;

	// TODO: Check if needed
	/*
	private class Callback : IAsyncCallback
	{
		public void Dispose()
		{
		}

		public IDisposable Shadow { get; set; }
		public void Invoke(AsyncResult asyncResultRef)
		{
			var ev = _session.EndGetEvent(asyncResultRef);

			switch (ev.TypeInfo)
			{
				case MediaEventTypes.EndOfPresentation:
					_sessionState = SessionState.Ended;
					OnSongFinishedPlaying(null, null);
					break;
				case MediaEventTypes.SessionTopologyStatus:
					if (ev.Get(EventAttributeKeys.TopologyStatus) == TopologyStatus.Ready)
					OnTopologyReady();
					break;
				case MediaEventTypes.SessionStopped:
					OnSessionStopped();
					break;
			}

			_session.BeginGetEvent(this, null);
		}

		public AsyncCallbackFlags Flags { get; private set; }
		public WorkQueueId WorkQueueId { get; private set; }
	}
	 */

	private static void platformInitialize()
	{
		// The GUID is specified in a GuidAttribute attached to the class
		// AudioStreamVolumeGuid = Guid.Parse(((GuidAttribute)typeof(AudioStreamVolume).GetCustomAttributes(typeof(GuidAttribute), false)[0]).Value);

		// MediaManagerState.CheckStartup();
		// MediaFactory.CreateMediaSession(null, out _session);

		// _callback = new Callback();
		// _session.BeginGetEvent(_callback, null);

		// _clock = _session.Clock.QueryInterface<PresentationClock>();
	}

	private static boolean platformGetIsMuted()
	{
		return _isMuted;
	}

	private static void platformSetIsMuted(boolean muted)
	{
		_isMuted = muted;

		setChannelVolumes();
	}

	private static boolean platformGetIsRepeating()
	{
		return _isRepeating;
	}

	private static void platformSetIsRepeating(boolean repeating)
	{
		_isRepeating = repeating;
	}

	private static boolean platformGetIsShuffled()
	{
		return _isShuffled;
	}

	private static void platformSetIsShuffled(boolean shuffled)
	{
		_isShuffled = shuffled;
	}

	private static TimeSpan platformGetPlayPosition()
	{
		if ((_sessionState == SessionState.Stopped) || (_sessionState == SessionState.Stopping))
			return TimeSpan.ZERO;
//		try
//		{
//			return TimeSpan.fromTicks(_clock.Time);
//		}
//		catch (SharpDXException)
//		{
			// The presentation clock is most likely not quite ready yet
			return TimeSpan.ZERO;
//		}
	}

	private static boolean platformGetGameHasControl()
	{
		// TODO: Fix me!
		return true;
	}

	private static MediaState platformGetState()
	{
		return _state;
	}

	private static float platformGetVolume()
	{
		return _volume;
	}

	private static void setChannelVolumes()
	{
		if (_volumeController == null)
			return;

		float volume = _isMuted ? 0f : _volume;
		float value = (_volumeController.getMaximum() - _volumeController.getMinimum()) * volume + _volumeController.getMinimum();
		_volumeController.setValue(value);
		// for (int i = 0; i < _volumeController.ChannelCount; i++)
		// _volumeController.SetChannelVolume(i, volume);
	}

	private static void platformSetVolume(float volume)
	{
		_volume = volume;
		setChannelVolumes();
	}

	private static void platformPause()
	{
		if (_sessionState != SessionState.Started)
			return;
		_sessionState = SessionState.Paused;
		// _session.pause();
	}

	private static void platformPlaySong(Song song)
	{
		if (song.equals(_currentSong))
			replayCurrentSong(song);
		else
			playNewSong(song);
	}

	private static void replayCurrentSong(Song song)
	{
		if (_sessionState == SessionState.Stopping)
		{
			// The song will be started after the SessionStopped event is received
			_nextSong = song;
			return;
		}

		// startSession(PositionBeginning);
		startSession();
	}

	private static void playNewSong(Song song)
	{
		if (_sessionState != SessionState.Stopped)
		{
			// The session needs to be stopped to reset the play position
			// The new song will be started after the SessionStopped event is received
			_nextSong = song;
			platformStop();
			return;
		}

		startNewSong(song);
	}

	private static void startNewSong(Song song)
	{
//		if (_volumeController != null)
//		{
//			_volumeController.Dispose();
//			_volumeController = null;
//		}

		_currentSong = song;

//		_session.SetTopology(SessionSetTopologyFlags.Immediate, song.Topology);

//		 startSession(PositionBeginning);
		startSession();
		
		platformStartNewSong(_currentSong);

		// The volume service won't be available until the session topology
		// is ready, so we now need to wait for the event indicating this
	}

//	private static void startSession(Variant startPosition)
	private static void startSession()
	{
		_sessionState = SessionState.Started;
	// _session.Start(null, startPosition);
	}

	// @formatter:off
	// ########################################################################
	// #                           Methods I added                            #
	// ########################################################################
	// @formatter:off
	private static void platformStartNewSong(Song song)
	{
		// NOTE: Had to create a separate class to add a boolean sine isInterrupted()
		//       doesn't seem to work properly.
		SoundPlayer sp = new SoundPlayer(song);
		sp.setName(song.getName());
		sp.start();
	}

	/**
	 * Creates the Thread's line and buffer
	 */
	private static void threadStarted()
	{
		// wait for the SoundManager constructor to finish
//		synchronized (this)
//		{
//			try
//			{
//				wait();
//			}
//			catch (InterruptedException ex)
//			{
//			}
//		}

		// use a short, 100ms (1/10th sec) buffer for filters that change in real-time
		int bufferSize = PLAYBACK_FORMAT.getFrameSize() * Math.round(PLAYBACK_FORMAT.getSampleRate() / 10);

		// create, open, and start the line
		SourceDataLine line;
		DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, PLAYBACK_FORMAT);
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(lineInfo);
			line.open(PLAYBACK_FORMAT, bufferSize);
		}
		catch (LineUnavailableException ex)
		{
			// the line is unavailable - signal to end this thread
			Thread.currentThread().interrupt();
			return;
		}

		line.start();

		// create the buffer
		byte[] buffer = new byte[bufferSize];

		// set this thread's locals
		localLine.set(line);
		localBuffer.set(buffer);
		
		onTopologyReady();
	}

	/**
	 * Drains and closes the Tread's line.
	 */
	private static void threadStopped()
	{
		SourceDataLine line = (SourceDataLine) localLine.get();
		if (line != null)
		{
			line.drain();
			line.close();
		}
		
		onSessionStopped();
	}

	// NOTE: Created this class to add a boolean isRunning since isInterrupted() doesn't work
	private static class SoundPlayer extends Thread
	{
		public boolean isRunning = false;
		
		private Song song;
		
		public SoundPlayer(Song song)
		{
            this.song = song;
        }
		
		@Override
		public void run()
		{
			isRunning = true;
			InputStream source;
			if (song != null)
			{
				source = new ByteArrayInputStream(song.data);

				if (source != null)
				{
					threadStarted();
		
					// get line and buffer from ThreadLocals
					SourceDataLine line = (SourceDataLine) localLine.get();
					byte[] buffer = (byte[]) localBuffer.get();
					if (line == null || buffer == null)
					{
						// the line is unavailable
						return;
					}
		
					// copy data to the line
					try
					{
						int numBytesRead = 0;
						// NOTE: isInterrupted doesn't seem to work properly
//						while (numBytesRead != -1  && !isInterrupted())
						while (numBytesRead != -1 && isRunning)
						{
							// if paused, wait until unpaused
							synchronized (pausedLock)
							{
								if (MediaPlayer.getState() == MediaState.Paused)
								{
									try
									{
										pausedLock.wait();
									}
									catch (InterruptedException ex)
									{
										return;
									}
								}
							}
							// copy data
							numBytesRead = source.read(buffer, 0, buffer.length);
							if (numBytesRead != -1)
							{
								line.write(buffer, 0, numBytesRead);
							}
							else
							{
								if (isRepeating())
								{
									source.reset();
									numBytesRead = 0;
								}
							}
						}
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
					}
				}
				threadStopped();
			}
		}
	}

	// ###################################################

	private static void onTopologyReady()
	{
		// IntPtr volumeObjectPtr;
		// MediaFactory.GetService(_session, MediaServiceKeys.StreamVolume, AudioStreamVolumeGuid, out volumeObjectPtr);
		// _volumeController = CppObject.FromPointer<AudioStreamVolume>(volumeObjectPtr);
		_volumeController = (FloatControl) localLine.get().getControl(FloatControl.Type.MASTER_GAIN);

		setChannelVolumes();
	}

	private static void platformResume()
	{
		if (_sessionState != SessionState.Paused)
			return;
		// startSession(PositionCurrent);
		startSession();
	}

	private static void platformStop()
	{
		if ((_sessionState == SessionState.Stopped) || (_sessionState == SessionState.Stopping))
			return;
		boolean hasFinishedPlaying = (_sessionState == SessionState.Ended);
		_sessionState = SessionState.Stopping;
		if (hasFinishedPlaying)
		{
			// The play position needs to be reset before stopping otherwise the next song may not start playing
			// _session.start(null, PositionBeginning);
		}
		// _session.stop();
		Thread thread = null;
		for (Thread t : Thread.getAllStackTraces().keySet())
		{
	        if (t.getName().equals(_currentSong.getName()))
	        {
	        	thread = t;
	        	break;
	        }
	    }

		SoundPlayer soundPlayer = As.as(thread, SoundPlayer.class);
		if (soundPlayer != null)
		{
			soundPlayer.isRunning = false;
		}
//		System.out.println(thread.getName() + "_" + thread.isAlive());
//		System.out.println(thread.getName() + "_" + thread.isInterrupted());
//		if (thread != null)
//		{
//			thread.interrupt();;
//		}
//		System.out.println(thread.getName() + "_" + thread.isAlive());
//		System.out.println(thread.getName() + "_" + thread.isInterrupted());
	}

	private static void onSessionStopped()
	{
		_sessionState = SessionState.Stopped;
		if (_nextSong != null)
		{
			if (_nextSong.notEquals(_currentSong))
				startNewSong(_nextSong);
			else
//				startSession(PositionBeginning);
				startSession();
			_nextSong = null;
		}
	}
}

// TODO: Create a stand-alone class for SoundPlayer (see MediaPlayer.WMS)