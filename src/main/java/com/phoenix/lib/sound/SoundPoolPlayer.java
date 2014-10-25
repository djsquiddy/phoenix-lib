package com.phoenix.lib.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
 * <p/>
 * This class only supports one sound playing at a time
 */
public class SoundPoolPlayer implements SoundPool.OnLoadCompleteListener {

    private final static int INVALID_STREAM_ID = -1;
    private final static int LOOP_FLAG = -1;
    private final static int NO_LOOP_FLAG = 0;
    private int mCurrentStreamId = INVALID_STREAM_ID;
    public static String TAG = SoundPoolPlayer.class.getName();
    private int mCurrentSampleId;
    private boolean isLooped;
    private AudioManager mAudioManager;
    private SoundPool mPlayer = null;
    private float rate = 1.0f;        // Playback rate
    private float masterVolume = 1.0f;    // Master volume level
    private float leftVolume = 1.0f;    // Volume levels for left and right channels
    private float rightVolume = 1.0f;
    private float balance = 0.5f;        // A balance value used to calculate left/right volume levels
    private SparseIntArray mSounds = new SparseIntArray();
    private Context mContext;

    public SoundPoolPlayer(Context context, final List<Integer> soundResources) {
        this(context, 1, soundResources);
    }

    public SoundPoolPlayer(Context context, int soundCount, final List<Integer> soundResources) {
        this.mContext = context;
        // setup SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mPlayer = new SoundPool.Builder().setMaxStreams(soundCount).build();
        } else {
            this.mPlayer = new SoundPool(soundCount, AudioManager.STREAM_MUSIC, 0);
        }

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        mPlayer.setOnLoadCompleteListener(this);

        if (soundResources != null) {
            for (Integer resource : soundResources) {
                Log.d(TAG, "Loading id: " + String.valueOf(resource));

                int soundId = mPlayer.load(context, resource, 1);
                mSounds.append(resource, soundId);
            }
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            // success
            Log.d(TAG, "Sound id: " + String.valueOf(sampleId) + " has finished loading");
            if (sampleId == mCurrentSampleId) {
                playSampleId(sampleId, isLooped);
            }
        } else {
            Log.e(TAG, "Sound id: " + String.valueOf(sampleId) + " couldn't be loaded");
        }
    }

    public int playShortResource(int resource) {
        mCurrentSampleId = mSounds.get(resource);
        return playSampleId(mCurrentSampleId, false);
    }

    private int playSampleId(int sampleId, boolean isLooped) {
        if (isLooped) {
            isLooped = true;
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, LOOP_FLAG, rate);
            mPlayer.setLoop(mCurrentStreamId, LOOP_FLAG);
        } else {
            isLooped = false;
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, NO_LOOP_FLAG, rate);
        }

        Log.d(TAG, "stream sample is " + String.valueOf(mCurrentSampleId));
        Log.d(TAG, "stream id is " + String.valueOf(mCurrentStreamId));

        float volume = getVolumeLevel();
        mPlayer.setVolume(mCurrentStreamId, volume, volume);

        return mCurrentStreamId;
    }

    public SoundPool getPlayer() {
        return mPlayer;
    }

    public int playLoopedResource(int resource) {
        mCurrentSampleId = mSounds.get(resource);
        return playSampleId(mCurrentSampleId, true);
    }

    private float getVolumeLevel() {
        float streamVolumeCurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return streamVolumeCurrent / streamVolumeMax;
    }

    public void pause() {
        mPlayer.autoPause();
    }

    public void stop() {
        Log.d(TAG, "Stopping");

        if (mPlayer != null) {
            mPlayer.stop(mCurrentStreamId);
            mCurrentStreamId = -1;
        }
    }

    public void stop(int resource) {
        Log.d(TAG, "Stopping");

        mPlayer.stop(resource);
    }

    public void pause(int resource) {
        Log.d(TAG, "Pausing");
    }

    public void resume() {
        Log.d(TAG, "Resume");
        mPlayer.autoResume();
    }

    // Set volume values based on existing balance value
    public void setVolume(float volumeLevel) {
        Log.d(TAG, "Changing the volume");
        masterVolume = volumeLevel;

        if (balance < 1.0f) { // Left dominant
            leftVolume = masterVolume;
            rightVolume = masterVolume * balance;
        } else { // Right dominant
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
        if (rate < 0.01f) {
            rate = 0.01f;
        }
        // Speed has a maximum of 2.0
        if (rate > 2.0f){
            rate = 2.0f;
        }
    }


    public void setBalance(float balVal) {
        balance = balVal;

        // Recalculate volume levels
        setVolume(masterVolume);
    }


    public void unload() {
        if (mPlayer != null){
            mPlayer.release();}
    }

    // Cleanup
    public void release() {
        if (mPlayer != null) {
            if (mCurrentStreamId != INVALID_STREAM_ID) {
                mPlayer.unload(mCurrentStreamId);
            }

            mCurrentStreamId = -1;
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.e(TAG, "Audio focus gain");
                // resume playback
                resume();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Log.e(TAG, "Audio focus loss");
                // Lost focus for an unbounded amount of time: stop playback and release media mediaPlayer
                stop();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.e(TAG, "Audio focus loss transient");
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media mediaPlayer because playback
                // is likely to resume
                stop();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.e(TAG, "Audio focus loss transient can duck");
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                setVolume(.1f);
                break;
        }
    }

}
