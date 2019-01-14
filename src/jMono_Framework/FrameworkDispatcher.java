package jMono_Framework;

import jMono_Framework.audio.SoundEffect;
import jMono_Framework.audio.SoundEffectInstancePool;

/**
 * Helper class for processing internal framework events.
 * <p>
 * If you use {@link Game} class, {@link #update()} is called automatically. Otherwise you must call
 * it as part of your game loop.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class FrameworkDispatcher
{
    private static boolean _initialized = false;

    /**
     * Processes framework events.
     */
    public static void update()
    {
        if (!_initialized)
            initialize();

        doUpdate();
    }

    private static void doUpdate()
    {
        // TODO(Eric): DynamicSoundEffectInstanceManager class
        // DynamicSoundEffectInstanceManager.UpdatePlayingInstances();
        SoundEffectInstancePool.update();
    }

    private static void initialize()
    {
        // Initialize sound system
        SoundEffect.initializeSoundEffect();

        _initialized = true;
    }
}
