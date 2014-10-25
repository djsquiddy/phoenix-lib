package com.phoenix.lib.sound;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Dylan on 10/25/2014.
 */
public class SoundUtils {
    private SoundUtils(){

    }

    /**
     * Get the audio file length
     *
     * @param context Context for get the resource
     * @param resId resource id
     * @return audio file length in milliseconds.
     */
    public static int getAudioLength(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        int duration = mp.getDuration();
        mp.release();

        return duration;
    }

    public static int getAudioLength(Context context, String file){
        MediaPlayerWrapper wrapper = new MediaPlayerWrapper(file);

        wrapper.setDataPrepare(file);

        int duration = wrapper.getDuration();

        wrapper.release();
        return duration;
    }
}
