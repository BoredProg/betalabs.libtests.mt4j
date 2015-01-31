/**
 * *****************************************************************************
 * AudioTrack.java
 *
 * ® Sébastien Parodi (capturevision), 2015.
 * http://capturevision.wordpress.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *****************************************************************************
 */
package advanced.interaction.minim;

import org.mt4j.*;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/**
 * Wrapper class to the minim framework.
 *
 * For wav file formats
 * For best results, do not use the mp3 file format.
 *
 * @author Andrew McGlynn
 *
 */
public class AudioTrack
{

    private Minim minim;
    private AudioPlayer player;
    private AudioMetaData metaData;

    /**
     *
     * @param fileName
     * @param mtapp
     */
    public AudioTrack(String fileName, MTApplication mtapp)
    {
        minim = new Minim(mtapp);
        player = minim.loadFile(fileName);
        metaData = player.getMetaData();
    }

    /**
     * Check if the track is playing
     *
     * @return
     */
    public boolean isPlaying()
    {
        return player.isPlaying();
    }

    /**
     * Pause the audio track
     */
    public void pause()
    {
        if (player.isPlaying())
        {
            player.pause();
        }
    }

    /**
     * Play the audio track
     */
    public void play()
    {
        if (!player.isPlaying())
        {
            player.play();
        }
    }

    /**
     * Rewind the track to the start
     */
    public void rewind()
    {
        player.rewind();
    }

    /**
     * Change where the current position in time that the track is currently
     * playing
     *
     * @param millis
     */
    public void cue(int millis)
    {
        final int mill = millis;
        Thread cueThread = new Thread()
        {
            public void run()
            {
                player.cue(mill);
                try
                {
                    this.join();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.err.println("Unable to join");
                }
            }

        };
        cueThread.start();
    }

    /**
     * Clean up the track, clearing resources
     */
    public void close()
    {
        player.close();
        minim.stop();
    }

    /**
     * Get the track artist
     *
     * @return track artist
     */
    public String getArtist()
    {
        return metaData.author();
    }

    /**
     * Get the name of the album
     *
     * @return album name
     */
    public String getAlbum()
    {
        return metaData.album();
    }

    /**
     * Get the track title
     *
     * @return
     */
    public String getTitle()
    {
        return metaData.title();
    }

    /**
     * Get the length of the track in milliseconds
     *
     * @return length of the track
     */
    public int getLength()
    {
        return player.length();
    }

    /**
     * Get the position that the track is currently at in milliseconds
     *
     * @return the current position of the track
     */
    public int getPosition()
    {
        return player.position();
    }

    /**
     * Set the volume of the track
     *
     * @param volume
     */
    public void setVolume(float volume)
    {
        if (player.isMuted())
        {
            player.unmute();
        }
        player.setGain(volume);
    }

    /**
     * Mute the audio track
     */
    public void mute()
    {
        player.mute();
    }

}
