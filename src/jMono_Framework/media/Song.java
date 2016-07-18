package jMono_Framework.media;

import jMono_Framework.audio.AudioLoader;
import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.time.TimeSpan;

import java.net.URI;
import java.nio.file.Paths;

public class Song implements AutoCloseable
{
	private String _name;
	private int _playCount = 0;
    private TimeSpan _duration = TimeSpan.ZERO;
    boolean disposed;
/*TODO: finish All the other classes and enable these getters
    /// <summary>
    /// Gets the Album on which the Song appears.
    /// </summary>
    public Album getAlbum()
    {
        return platformGetAlbum();
    }

// #if WINDOWS_STOREAPP || WINDOWS_UAP
    protected void setAlbum(Album value) { platformSetAlbum(value); }
// #endif

    /// <summary>
    /// Gets the Artist of the Song.
    /// </summary>
    public Artist getArtist()
    {
        return PlatformGetArtist();
    }

    /// <summary>
    /// Gets the Genre of the Song.
    /// </summary>
    public Genre getGenre()
    {
        return PlatformGetGenre();
    }
*/
// #if ANDROID || OPENAL || WEB || IOS
    // protected delegate void FinishedPlayingHandler(object sender, EventArgs args);
    protected interface FinishedPlayingHandler
    {
    	void handleEvent(Object sender, EventArgs e);
    }
//#if !DESKTOPGL
    FinishedPlayingHandler donePlaying;
//#endif
//#endif
    public Song(String fileName, int durationMS)
    {
    	this(fileName);
        _duration = TimeSpan.fromMilliseconds(durationMS);
    }

	protected Song(String fileName)
	{			
		_name = fileName;

		// TODO: See Song.WMS.cs
        platformInitialize(fileName);
    }

    protected void finalize()
    {
        dispose(false);
    }

    protected String getFilePath()
	{
		return _name;
	}

    public static Song fromUri(String name, URI uri)
    {
        if (!uri.isAbsolute())
        {
        	// TODO: Need to test that
        	Song song = new Song(uri.toString());
            song._name = name;
            return song;
        }
        else
        {
            throw new UnsupportedOperationException("Loading songs from an absolute path is not implemented");
        }
    }
	
    @Override
	public void close()
	{
    	dispose(true);
    	//GC.SuppressFinalize(this);
	}
    
    void dispose(boolean disposing)
    {
        if (!disposed)
        {
            if (disposing)
            {
            	// TODO: See Song.WMS.cs 
                platformDispose(disposing);
            }

            disposed = true;
        }
    }

    @Override
	public int hashCode()
	{
		return super.hashCode ();
	}

    public boolean equals(Song song)
    {
// #if DIRECTX
        // return song != null && song.FilePath == FilePath;
// #else
		return ((Object)song != null) && (getName().equals(song.getName()));
// #endif
	}
	
    @Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		
		return equals(As.as(obj, Song.class));  
	}
	
    public boolean notEquals(Song other)
	{
	  return ! (this.equals(other));
	}
    
	public static boolean equals(Song song1, Song song2)
	{
		if((Object)song1 == null)
		{
			return (Object)song2 == null;
		}

		return song1.equals(song2);
	}
	
	public static boolean notEquals(Song song1, Song song2)
	{
	  return ! (song1.equals(song2));
	}

	// TODO: See Song.WMS.cs and others
	
    public TimeSpan getDuration()
    {
        return platformGetDuration();
    }	

    public boolean isProtected()
    {
        return platformIsProtected();
    }

    public boolean isRated()
    {
        return platformIsRated();
    }

    public String getName()
    {
        return platformGetName();
    }

    public int getPlayCount()
    {
        return platformGetPlayCount();
    }

    public int getRating()
    {
        return platformGetRating();
    }

    public int getTrackNumber()
    {
        return platformGetTrackNumber();
    }

    // ########################################################################
    // #                     Song.Default.cs                                  #
    // ########################################################################

    // NOTE: I added this
    byte[] data;
    
//    private SoundEffectInstance _sound;

	private void platformInitialize(String fileName)
	{
// #if MONOMAC || (WINDOWS && OPENGL) || WEB

//        try (var s = File.OpenRead(_name))
//        {
//            var soundEffect = SoundEffect.FromStream(s);
//            _sound = soundEffect.CreateInstance();
//        }
// #endif
		data = AudioLoader.load(AudioLoader.getAudioInputStream(fileName, MediaPlayer.PLAYBACK_FORMAT));
	}

    private void platformDispose(boolean disposing)
    {
//        if (_sound == null)
//            return;
//
//        _sound.dispose();
//        _sound = null;
    }

	protected void onFinishedPlaying (Object sender, EventArgs args)
	{
		if (donePlaying == null)
			return;
		
		donePlaying.handleEvent(sender, args);
	}
//TODO: add methods to FinishedPlayingHandler like EventHandler 
	/// <summary>
	/// Set the event handler for "Finished Playing". Done this way to prevent multiple bindings.
	/// </summary>
	protected void setEventHandler(FinishedPlayingHandler handler)
	{
		if (donePlaying != null)
			return;
		
//		donePlaying.add(handler);
	}

	protected void play()
	{	
//		if ( _sound == null )
//			return;

        platformPlay();

        _playCount++;
    }

    private void platformPlay()
    {
//        _sound.play();
    }

	protected void resume()
	{
//		if (_sound == null)
//			return;

        platformResume();
	}

    private void platformResume()
    {
//        _sound.resume();
    }
	
	protected void pause()
	{			            
//		if ( _sound == null )
//			return;
		
//		_sound.pause();
    }
	
	protected void stop()
	{
//		if ( _sound == null )
//			return;
		
//		_sound.stop();
		_playCount = 0;
	}

	protected float getVolume()
	{
//		if (_sound != null)
//			return _sound.getVolume();
//		else
			return 0.0f;
	}
		
	protected void setVolume(float value)
	{
//		if ( _sound != null && _sound.getVolume() != value )
//			_sound.setVolume(value);
	}

	protected TimeSpan getPosition()
    {
		// TODO: Implement
		return new TimeSpan(0);				
    }

//    private Album platformGetAlbum()
//    {
//        return null;
//    }

//    private Artist platformGetArtist()
//    {
//        return null;
//    }

//    private Genre platformGetGenre()
//    {
//        return null;
//    }

//    private TimeSpan platformGetDuration()
//    {
//        return _duration;
//    }

//    private boolean platformIsProtected()
//    {
//        return false;
//    }

//    private boolean platformIsRated()
//    {
//        return false;
//    }

//    private String platformGetName()
//    {
//        return Path.GetFileNameWithoutExtension(_name);
//    }

//    private int platformGetPlayCount()
//    {
//        return _playCount;
//    }

//    private int platformGetRating()
//    {
//        return 0;
//    }

//    private int platformGetTrackNumber()
//    {
//        return 0;
//    }

    // TODO: Add other methods and fields
    // ########################################################################
    // #                        Song.WMS.cs                                   #
    // ########################################################################
    
    private TimeSpan platformGetDuration()
    {
        return _duration;
    }

    private boolean platformIsProtected()
    {
        return false;
    }

    private boolean platformIsRated()
    {
        return false;
    }

    private String platformGetName()
    {
        return Paths.get(_name).getFileName().toString();
    }

    private int platformGetPlayCount()
    {
        return _playCount;
    }

    private int platformGetRating()
    {
        return 0;
    }

    private int platformGetTrackNumber()
    {
        return 0;
    }
}
