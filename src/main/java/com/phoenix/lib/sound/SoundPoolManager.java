package com.phoenix.lib.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;

import com.phoenix.lib.BuildConfig;

import java.util.List;
import java.util.Stack;

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
public class SoundPoolManager implements SoundPool.OnLoadCompleteListener, AudioManager.OnAudioFocusChangeListener {
    private final static int INVALID_STREAM_ID = -1;
    private int mCurrentStreamId = INVALID_STREAM_ID;
    private final static int INVALID_SAMPLE_ID = -1;
    private final static int LOOP_FLAG = -1;
    private final static int NO_LOOP_FLAG = 0;
    public static String TAG = SoundPoolManager.class.getName();
    private final int MAX_SOUND_CAPACITY;
    private final Stack<Integer> mPreviouslyUsed = new Stack<Integer>();
    private final SoundPool mPlayer;
    private int mCurrentSampleId;
    private boolean isLooped;
    private AudioManager mAudioManager;
    private float rate = 1.0f;        // Playback rate
    private float masterVolume = 1.0f;    // Master volume level
    private float leftVolume = 1.0f;    // Volume levels for left and right channels
    private float rightVolume = 1.0f;
    private float balance = 0.5f;        // A balance value used to calculate left/right volume levels
    private SparseIntArray mSounds = new SparseIntArray();
    private Context mContext;

    public SoundPoolManager(Context context) {
        this(context, 1, 10);
    }

    public SoundPoolManager(Context context, int soundCount, int maxSoundLoaded) {
        this.MAX_SOUND_CAPACITY = maxSoundLoaded;
        this.mContext = context;
        // setup SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mPlayer = new SoundPool.Builder().setMaxStreams(soundCount).build();
        } else {
            this.mPlayer = new SoundPool(soundCount, AudioManager.STREAM_MUSIC, 0);
        }

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        mPlayer.setOnLoadCompleteListener(this);
    }

    public Context getContext() {
        return mContext;
    }

    public void loadList(List<Integer> soundResources) {
        for (Integer resourceId : soundResources) {
            loadResource(resourceId, false);
        }

        mCurrentSampleId = INVALID_STREAM_ID;
    }

    private void loadResource(int resource, boolean playAfterLoad) {
        if (mPreviouslyUsed.size() >= MAX_SOUND_CAPACITY) {
            int soundId = mPreviouslyUsed.pop();

            mPlayer.unload(soundId);
            int index = mSounds.indexOfValue(soundId);
            mSounds.removeAt(index);
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading id: " + String.valueOf(resource));
        }

        int sampleId = mPlayer.load(mContext, resource, 1);
        if (playAfterLoad) {
            mCurrentSampleId = sampleId;
        }

        mSounds.append(resource, sampleId);
        mPreviouslyUsed.push(sampleId);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            // success
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Sound id: " + String.valueOf(sampleId) + " has finished loading");
            }
            if (sampleId == mCurrentSampleId) {
                playSampleId(sampleId);
            }
        } else {
            Log.e(TAG, "Sound id: " + String.valueOf(sampleId) + " couldn't be loaded");
        }
    }

    private int playSampleId(int sampleId) {
        if (isLooped) {
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, LOOP_FLAG, rate);
            mPlayer.setLoop(mCurrentStreamId, LOOP_FLAG);
        } else {
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, NO_LOOP_FLAG, rate);
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "stream sample is " + String.valueOf(mCurrentSampleId));
            Log.d(TAG, "stream id is " + String.valueOf(mCurrentStreamId));
        }
        float volume = getVolumeLevel();
        mPlayer.setVolume(mCurrentStreamId, volume, volume);

        return mCurrentStreamId;
    }

    private float getVolumeLevel() {
        float streamVolumeCurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return streamVolumeCurrent / streamVolumeMax;
    }

    public int playShortResource(int resource) {
        isLooped = false;
        mCurrentSampleId = mSounds.get(resource, -1);

        if (mCurrentSampleId == -1) {
            loadResource(resource, true);

            return mCurrentStreamId + 1;
        } else {
            int index = mPreviouslyUsed.indexOf(mCurrentSampleId);
            mPreviouslyUsed.remove(index);
            mPreviouslyUsed.push(mCurrentSampleId);
            return playSampleId(mCurrentSampleId);
        }
    }

    public SoundPool getPlayer() {
        return mPlayer;
    }

    public int playLoopedResource(int resource) {
        isLooped = true;
        mCurrentSampleId = mSounds.get(resource, -1);
        if (mCurrentSampleId == -1) {
            loadResource(resource, true);

            return mCurrentStreamId + 1;
        } else {
            int index = mPreviouslyUsed.indexOf(mCurrentSampleId);
            mPreviouslyUsed.remove(index);
            mPreviouslyUsed.push(mCurrentSampleId);

            return playSampleId(mCurrentSampleId);
        }
    }

    public void setSpeed(float speed) {
        rate = speed;

        // Speed of zero is invalid
        if (rate < 0.01f) {
            rate = 0.01f;
        }
        // Speed has a maximum of 2.0
        if (rate > 2.0f) {
            rate = 2.0f;
        }
    }

    public void setBalance(float balVal) {
        balance = balVal;

        // Recalculate volume levels
        setVolume(masterVolume);
    }

    // Set volume values based on existing balance value
    public void setVolume(float volumeLevel) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Changing the volume");
        }
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

    public void unloadResource(int resource) {
        int soundId = mSounds.get(resource, -1);
        if (soundId != -1) {
            mPlayer.unload(soundId);
            int index = mPreviouslyUsed.indexOf(soundId);
            mPreviouslyUsed.remove(index);
        }
    }

    public void unload() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
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
                pause();

                release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.e(TAG, "Audio focus loss transient");
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media mediaPlayer because playback
                // is likely to resume
                pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.e(TAG, "Audio focus loss transient can duck");
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                setVolume(.1f);
                break;
        }
    }

    public void resume() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Resume");
        }

        mPlayer.autoResume();
    }

    public void pause() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Pausing");
        }

        mPlayer.autoPause();
    }

    // Cleanup
    public void release() {
        if (mPlayer != null) {
            if (mCurrentStreamId != INVALID_STREAM_ID) {
                mPlayer.unload(mCurrentStreamId);
            }

            mAudioManager.abandonAudioFocus(this);

            mCurrentStreamId = -1;
            mPlayer.release();
        }
    }

    public void stop() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Stopping");
        }

        if (mPlayer != null) {
            mPlayer.stop(mCurrentStreamId);
            mCurrentStreamId = -1;
        }
    }

}
