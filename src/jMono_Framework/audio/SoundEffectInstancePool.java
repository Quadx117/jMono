package jMono_Framework.audio;

import java.util.ArrayList;
import java.util.List;

public class SoundEffectInstancePool
{
// #if WINDOWS || (WINRT && !WINDOWS_PHONE) || DESKTOPGL || WEB || ANGLE

	// These platforms are only limited by memory.
	private static final int MAX_PLAYING_INSTANCES = Integer.MAX_VALUE;

// #elif MONOMAC

	// Reference: http://stackoverflow.com/questions/3894044/maximum-number-of-openal-sound-buffers-on-iphone
//	private final int MAX_PLAYING_INSTANCES = 256;

// #elif WINDOWS_PHONE

	// Reference: http://msdn.microsoft.com/en-us/library/microsoft.xna.framework.audio.instanceplaylimitexception.aspx
//	private final int MAX_PLAYING_INSTANCES = 64;

// #elif IOS

	// Reference: http://stackoverflow.com/questions/3894044/maximum-number-of-openal-sound-buffers-on-iphone
//	private final int MAX_PLAYING_INSTANCES = 32;

// #elif ANDROID

	// Set to the same as OpenAL on iOS
//	private final int MAX_PLAYING_INSTANCES = 32;

// #endif

	private static List<SoundEffectInstance> _playingInstances;
	private static List<SoundEffectInstance> _pooledInstances;

	// NOTE: Make sure we can't instantiate this class
	private SoundEffectInstancePool() {}

	static
	{
		// Reduce garbage generation by allocating enough capacity for
		// the maximum playing instances or at least some reasonable value.
		int maxInstances = MAX_PLAYING_INSTANCES < 1024 ? MAX_PLAYING_INSTANCES : 1024;
		_playingInstances = new ArrayList<SoundEffectInstance>(maxInstances);
		_pooledInstances = new ArrayList<SoundEffectInstance>(maxInstances);
	}

	/**
	 * Returns whether the platform has capacity for more sounds to be played at this time.
	 * 
	 * @return {@code true} if more sounds can be played, {@code false} otherwise.
	 */
	protected static boolean isSoundsAvailable()
	{
		return _playingInstances.size() < MAX_PLAYING_INSTANCES;
	}

	/**
	 * Add the specified instance to the pool if it is a pooled instance and removes it from the list of playing
	 * instances.
	 * 
	 * @param inst
	 *        The {@link SoundEffectInstance} to be added.
	 */
	protected static void add(SoundEffectInstance inst)
	{
		if (inst._isPooled)
		{
			_pooledInstances.add(inst);
			inst._effect = null;
		}

		_playingInstances.remove(inst);
	}

	/**
	 * Adds the SoundEffectInstance to the list of playing instances.
	 * 
	 * @param inst
	 *        The {@link SoundEffectInstance} to be added to the playing list.
	 */
	protected static void remove(SoundEffectInstance inst)
	{
		_playingInstances.add(inst);
	}

	/**
	 * Returns a pooled {@link SoundEffectInstance} if one is available, or allocates a new {@code SoundEffectInstance}
	 * if the pool is empty.
	 * 
	 * @param forXAct
	 *        Whether or not the instance is for XAct audio.
	 * @return The {@code SoundEffectInstance}.
	 */
	protected static SoundEffectInstance getInstance(boolean forXAct)
	{
		SoundEffectInstance inst = null;
		int count = _pooledInstances.size();
		if (count > 0)
		{
			// Grab the item at the end of the list so the remove doesn't copy all
			// the list items down one slot.
			inst = _pooledInstances.get(count - 1);
			_pooledInstances.remove(count - 1);

			// Reset used instance to the "default" state.
			inst._isPooled = true;
			inst._isXAct = forXAct;
			inst.setVolume(1.0f);
			inst.setPan(0.0f);
			inst.setPitch(0.0f);
			inst.setIsLooped(false);
		}
		else
		{
			inst = new SoundEffectInstance();
			inst._isPooled = true;
			inst._isXAct = forXAct;
		}

		return inst;
	}

	/**
	 * Iterates the list of playing instances, returning them to the pool if they have stopped playing.
	 */
	public static void update()
	{
// #if OPENAL
//		OpenALSoundController.GetInstance.Update();
// #endif

		SoundEffectInstance inst = null;
		// Cleanup instances which have finished playing.
		for (int x = 0; x < _playingInstances.size();)
		{
			inst = _playingInstances.get(x);

			if (inst.getState() == SoundState.Stopped || inst.isDisposed() || inst._effect == null)
			{
				add(inst);
				continue;
			}
			else if (inst._effect.isDisposed())
			{
				add(inst);
				// Instances created through SoundEffect.CreateInstance need to be disposed when
				// their owner SoundEffect is disposed.
				if (!inst._isPooled)
					inst.close();
				continue;
			}

			++x;
		}
	}

	protected static void updateMasterVolume()
	{
		for (SoundEffectInstance inst : _playingInstances)
		{
			// XAct sounds are not controlled by the SoundEffect
			// master volume, so we can skip them completely.
			if (inst._isXAct)
				continue;

			// Re-applying the volume to itself will update
			// the sound with the current master volume.
			inst.setVolume(inst.getVolume());
		}
	}
}
