package com.phoenix.lib.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

import java.util.List;

/**
 * Created by Dylan on 8/25/2014.
 * <p/>
 * Example usage:
 * SoundPoolPlayer sound = new SoundPoolPlayer(getContext, 4);
 * In on create:
 * sound.playShortResource(R.raw.<sound_name>);
 * In on pause or on destroy
 * sound.release();
 */
public class SoundPoolPlayer {
    public static String TAG = SoundPoolPlayer.class.getName();
    SoundPool.OnLoadCompleteListener onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (status == 0) {
                // success
                Log.d(TAG, "Sound id: " + String.valueOf(sampleId) + " has finished loading");
            } else {
                Log.e(TAG, "Sound id: " + String.valueOf(sampleId) + " couldn't be loaded");
            }
        }
    };
    private int mCurrentStreamId;
    private SoundPool mPlayer = null;
    private float rate = 1.0f;        // Playback rate
    private float masterVolume = 1.0f;    // Master volume level
    private float leftVolume = 1.0f;    // Volume levels for left and right channels
    private float rightVolume = 1.0f;
    private float balance = 0.5f;        // A balance value used to calculate left/right volume levels
    //private HashMap<Integer, SoundResourceInfo> mSounds = new HashMap<Integer, SoundResourceInfo>();
    private SparseIntArray mSounds = new SparseIntArray();
    private Context mContext;

    public SoundPoolPlayer(Context context, int soundCount, final List<Integer> soundResources) {
        this.mContext = context;
        // setup SoundPool
        this.mPlayer = new SoundPool(soundCount, AudioManager.STREAM_MUSIC, 0);

        mPlayer.setOnLoadCompleteListener(onLoadCompleteListener);

        if (soundResources != null) {
            for (Integer resource : soundResources) {
                Log.d(TAG, "Loading id: " + String.valueOf(resource));

                int soundId = mPlayer.load(context, resource, 1);
                mSounds.append(resource, soundId);
            }
        }
    }

    public static int getSoundFileLengthInMs(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        int duration = mp.getDuration();
        mp.release();
        return duration;
    }

    public void playShortResource(int resource) {
        mCurrentStreamId = mPlayer.play(mSounds.get(resource), leftVolume, rightVolume, 0, 0, rate);
        Log.d(TAG, "stream id is " + String.valueOf(mCurrentStreamId));
    }

    public SoundPool getPlayer() {
        return mPlayer;
    }

    public int playLoopedResource(int resource) {
        mCurrentStreamId = mPlayer.play(mSounds.get(resource), 0, 0, 1, -1, rate);
        mPlayer.setLoop(mCurrentStreamId, -1);

        float volume = getVolumeLevel();
        mPlayer.setVolume(mCurrentStreamId, volume, volume);
        return mCurrentStreamId;
    }


    private float getVolumeLevel() {
        AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return streamVolumeCurrent / streamVolumeMax;
    }

    public void pause() {
        mPlayer.autoPause();
    }

    public void stop() {
        Log.d(TAG, "Stopping");

        mPlayer.stop(mCurrentStreamId);
    }

    public void stop(int resource) {
        Log.d(TAG, "Stopping");

        mPlayer.stop(resource);
    }

    public void pause(int resource) {
        Log.d(TAG, "Pausing");
    }

    public void resume() {
        Log.d(TAG, "Resuming");
        mPlayer.autoResume();
    }

    // Set volume values based on existing balance value
    public void setVolume(float volumeLevel) {
        Log.d(TAG, "Changing the volume");
        masterVolume = volumeLevel;

        if (balance < 1.0f) // Left dominant
        {
            leftVolume = masterVolume;
            rightVolume = masterVolume * balance;
        } else  // Right dominant
        {
            rightVolume = masterVolume;
            leftVolume = masterVolume * (2.0f - balance);
        }

        if (mCurrentStreamId != 0) {
            mPlayer.setVolume(mCurrentStreamId, leftVolume, rightVolume);
        }
    }


    public void setSpeed(float speed) {
        rate = speed;

        // Speed of zero is invalid
        if (rate < 0.01f)
            rate = 0.01f;

        // Speed has a maximum of 2.0
        if (rate > 2.0f)
            rate = 2.0f;
    }


    public void setBalance(float balVal) {
        balance = balVal;

        // Recalculate volume levels
        setVolume(masterVolume);
    }


    public void unload() {
        if (mPlayer != null)
            mPlayer.release();
    }

    // Cleanup
    public void release() {
        mPlayer.release();
        mPlayer = null;
    }

    private class SoundResourceInfo {
        public int resourceId = 0;
        public int soundId = 0;
        public boolean isPlaying = false;
        public int streamId = 0;
        public boolean isLoaded = false;

        private SoundResourceInfo(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}