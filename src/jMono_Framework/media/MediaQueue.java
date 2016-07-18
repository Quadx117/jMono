package jMono_Framework.media;

import jMono_Framework.math.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MediaQueue
{
	List<Song> songs = new ArrayList<Song>();
	private int _activeSongIndex = -1;
	private Random random = new Random();

// #if WINDOWS_PHONE
//    private MsMediaQueue mediaQueue;
//
//    public static implicit operator MediaQueue(MsMediaQueue mediaQueue)
//    {
//        return new MediaQueue(mediaQueue);
//    }
//
//    private MediaQueue(MsMediaQueue mediaQueue)
//    {
//        this.mediaQueue = mediaQueue;
//    }
// #endif

	public MediaQueue()
	{
		
	}
	
	public Song getActiveSong()
	{
// #if WINDOWS_PHONE
//		    if (mediaQueue != null)
//		        return new Song(mediaQueue.ActiveSong);
// #endif
		if (songs.size() == 0 || _activeSongIndex < 0)
			return null;

		return songs.get(_activeSongIndex);
	}
	
	public int getActiveSongIndex()
	{
// #if WINDOWS_PHONE
//		    if (mediaQueue != null)
//		        return mediaQueue.ActiveSongIndex;
// #endif
	        return _activeSongIndex;
	    }
	
	public void setActiveSongIndex(int value)
    {
// #if WINDOWS_PHONE
//	        if (mediaQueue != null)
//	            mediaQueue.ActiveSongIndex = value;
// #endif
        _activeSongIndex = value;
	}

    protected int getCount()
    {
// #if WINDOWS_PHONE
//            if (mediaQueue != null)
//                return mediaQueue.Count;
// #endif
    	return songs.size();
    }

    public Song getSong(int index)
    {
// #if WINDOWS_PHONE
//            if (mediaQueue != null)
//                return new Song(mediaQueue[index]);
// #endif
    	return songs.get(index);
    }

    protected Iterator<Song> getSongs()
    {
    	return songs.iterator();
    }

	protected Song getNextSong(int direction, boolean shuffle)
	{
		if (shuffle)
			_activeSongIndex = random.nextInt(songs.size());
		else			
			_activeSongIndex = (int)MathHelper.clamp(_activeSongIndex + direction, 0, songs.size() - 1);
		
		return songs.get(_activeSongIndex);
	}
	
	protected void clear()
	{
		Song song;
		for(; songs.size() > 0; )
		{
			song = songs.get(0);
// #if !DIRECTX
//			song.stop();
// #endif
			songs.remove(song);
		}
	}

// #if !DIRECTX
    protected void setVolume(float volume)
    {
        int count = songs.size();
        for (int i = 0; i < count; ++i)
            songs.get(i).setVolume(volume);
    }
// #endif

    protected void add(Song song)
    {
        songs.add(song);
    }

// #if !DIRECTX
    protected void stop()
    {
        int count = songs.size();
        for (int i = 0; i < count; ++i)
            songs.get(i).stop();
    }
// #endif
}
