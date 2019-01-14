package blackjack.misc;

import jMono_Framework.Game;
import jMono_Framework.audio.SoundEffect;
import jMono_Framework.audio.SoundEffectInstance;
import jMono_Framework.audio.SoundState;
import jMono_Framework.components.GameComponent;
import jMono_Framework.media.MediaPlayer;
import jMono_Framework.media.MediaState;
import jMono_Framework.media.Song;

import java.util.HashMap;

/**
 * Component that manages audio playback for all sounds.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public class AudioManager extends GameComponent
{
	/**
	 * The singleton for this type.
	 */
	static AudioManager audioManager = null;

	public static AudioManager getInstance()
	{
		return audioManager;
	}

	static final String soundAssetLocation = "sounds/";

	// Audio Data
	HashMap<String, SoundEffectInstance> soundBank;
	HashMap<String, Song> musicBank;

	private AudioManager(Game game)
	{
		super(game);
	}

	/**
	 * Initialize the static AudioManager functionality.
	 * 
	 * @param game
	 *        The game that this component will be attached to.
	 */
	public static void initialize(Game game)
	{
		audioManager = new AudioManager(game);
		audioManager.soundBank = new HashMap<String, SoundEffectInstance>();
		audioManager.musicBank = new HashMap<String, Song>();

		game.getComponents().add(audioManager);
	}

	/**
	 * Loads a single sound into the sound manager, giving it a specified alias.
	 * 
	 * <p>
	 * Loading a sound with an alias that is already used will have no effect.
	 * 
	 * @param contentName
	 *        The content name of the sound file. Assumes all sounds are located
	 *        under the "sounds" folder in the content project.
	 * @param alias
	 *        Alias to give the sound. This will be used to identify the sound uniquely.
	 */
	public static void loadSound(String contentName, String alias)
	{
		SoundEffect soundEffect = audioManager.getGame().getContent().load(soundAssetLocation + contentName, SoundEffect.class);
		SoundEffectInstance soundEffectInstance = soundEffect.createInstance();

		if (!audioManager.soundBank.containsKey(alias))
		{
			audioManager.soundBank.put(alias, soundEffectInstance);
		}
	}

	/**
	 * Loads a single song into the sound manager, giving it a specified alias.
	 * 
	 * <p>
	 * Loading a song with an alias that is already used will have no effect.
	 * 
	 * @param contentName
	 *        The content name of the sound file containing the song. Assumes all sounds are located under the "Sounds"
	 *        folder in the content project.
	 * @param alias
	 *        Alias to give the song. This will be used to identify the song uniquely.
	 */
	public static void loadSong(String contentName, String alias)
	{
		Song song = audioManager.getGame().getContent().load(soundAssetLocation + contentName, Song.class);

		if (!audioManager.musicBank.containsKey(alias))
		{
			audioManager.musicBank.put(alias, song);
		}
	}

	/**
	 * Loads and organizes the sounds used by the game.
	 */
	public static void loadSounds()
	{
		loadSound("bet", "Bet");
		loadSound("cardFlip", "Flip");
		loadSound("cardsShuffle", "Shuffle");
		loadSound("deal", "Deal");
	}

	/**
	 * Loads and organizes the music used by the game.
	 */
	public static void loadMusic()
	{
		loadSong("inGameSong_Loop", "InGameSong_Loop");
		loadSong("menuMusic_Loop", "MenuMusic_Loop");
	}

	/**
	 * Indexer. Return a sound instance by name.
	 */
	public SoundEffectInstance getSoundEffectInstance(String soundName)
	{
		if (audioManager.soundBank.containsKey(soundName))
		{
			return audioManager.soundBank.get(soundName);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Plays a sound by name.
	 * 
	 * @param soundName
	 *        The name of the sound to play.
	 */
	public static void playSound(String soundName)
	{
		// If the sound exists, start it
		if (audioManager.soundBank.containsKey(soundName))
		{
			audioManager.soundBank.get(soundName).play();
		}
	}

	/**
	 * Plays a sound by name.
	 * 
	 * @param soundName
	 *        The name of the sound to play.
	 * @param isLooped
	 *        Indicates if the sound should loop.
	 */
	public static void playSound(String soundName, boolean isLooped)
	{
		// If the sound exists, start it
		if (audioManager.soundBank.containsKey(soundName))
		{
			if (audioManager.soundBank.get(soundName).isLooped() != isLooped)
			{
				audioManager.soundBank.get(soundName).setLooped(isLooped);
			}

			audioManager.soundBank.get(soundName).play();
		}
	}

	/**
	 * Plays a sound by name.
	 * 
	 * @param soundName
	 *        The name of the sound to play.
	 * @param isLooped
	 *        Indicates if the sound should loop.
	 * @param volume
	 *        Indicates if the volume
	 */
	public static void playSound(String soundName, boolean isLooped, float volume)
	{
		// If the sound exists, start it
		if (audioManager.soundBank.containsKey(soundName))
		{
			if (audioManager.soundBank.get(soundName).isLooped() != isLooped)
			{
				audioManager.soundBank.get(soundName).setLooped(isLooped);
			}

			audioManager.soundBank.get(soundName).setVolume(volume);
			audioManager.soundBank.get(soundName).play();
		}
	}

	/**
	 * Stops a sound mid-play. If the sound is not playing, this method does nothing.
	 * 
	 * @param soundName
	 *        The name of the sound to stop.
	 */
	public static void stopSound(String soundName)
	{
		// If the sound exists, stop it
		if (audioManager.soundBank.containsKey(soundName))
		{
			audioManager.soundBank.get(soundName).stop();
		}
	}

	/**
	 * Stops all currently playing sounds.
	 */
	public static void stopSounds()
	{
		for (SoundEffectInstance sound : audioManager.soundBank.values())
		{
			if (sound.getState() != SoundState.Stopped)
			{
				sound.stop();
			}
		}
	}

	/**
	 * Pause or resume all sounds.
	 * 
	 * @param resumeSounds
	 *        {@code true} to resume all paused sounds or {@code false} to pause all playing sounds.
	 */
	public static void pauseResumeSounds(boolean resumeSounds)
	{
		SoundState state = resumeSounds ? SoundState.Paused : SoundState.Playing;

		for (SoundEffectInstance sound : audioManager.soundBank.values())
		{
			if (sound.getState() == state)
			{
				if (resumeSounds)
				{
					sound.resume();
				}
				else
				{
					sound.pause();
				}
			}
		}
	}

	/**
	 * Play music by name. This stops the currently playing music first. Music will loop until stopped.
	 * 
	 * <p>
	 * If the desired music is not in the music bank, nothing will happen.
	 * 
	 * @param musicSoundName
	 *        The name of the music sound.
	 */
	public static void playMusic(String musicSoundName)
	{
		// If the music sound exists
		if (audioManager.musicBank.containsKey(musicSoundName))
		{
			// Stop the old music sound
			if (MediaPlayer.getState() != MediaState.Stopped)
			{
				MediaPlayer.stop();
			}

			MediaPlayer.setIsRepeating(true);

			MediaPlayer.play(audioManager.musicBank.get(musicSoundName));
		}
	}

	/**
	 * Stops the currently playing music.
	 */
	public static void stopMusic()
	{
		if (MediaPlayer.getState() != MediaState.Stopped)
		{
			MediaPlayer.stop();
		}
	}

	/**
	 * Clean up the component when it is disposing.
	 */
	@Override
	protected void dispose(boolean disposing)
	{
		try
		{
			if (disposing)
			{
				for (SoundEffectInstance item : soundBank.values())
				{
					item.close();
				}
				soundBank.clear();
				soundBank = null;
			}
		}
		finally
		{
			super.dispose(disposing);
		}
	}
}
