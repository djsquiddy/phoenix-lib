package com.phoenix.lib.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import com.phoenix.lib.log.Logger;

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
    private static final String TAG = SoundPoolManager.class.getName();
    private static final int INVALID_STREAM_ID = -1;
    private int mCurrentStreamId = INVALID_STREAM_ID;
    private static final int INVALID_SAMPLE_ID = -1;
    private static final int DEFAULT_STREAM_COUNT = 1;
    private static final int LOOP_FLAG = -1;
    private static final int NO_LOOP_FLAG = 0;
    private final int MAX_SOUND_CAPACITY;
    private final Stack<Integer> mPreviouslyUsed = new Stack<Integer>();
    private final SoundPool mPlayer;
    private final AudioManager mAudioManager;
    private final SparseIntArray mSounds = new SparseIntArray();
    private final Context mContext;
    private int mCurrentSampleId;
    private boolean isLooped;
    private float rate = 1.0f;        // Playback rate
    private float masterVolume = 1.0f;    // Master volume level
    private float leftVolume = 1.0f;    // Volume levels for left and right channels
    private float rightVolume = 1.0f;
    private float balance = 0.5f;        // A balance value used to calculate left/right volume levels

    public SoundPoolManager(Context context) {
        this(context, DEFAULT_STREAM_COUNT, 10);
    }

    public SoundPoolManager(Context context, int soundCount, int maxSoundLoaded) {
        this.MAX_SOUND_CAPACITY = maxSoundLoaded;
        this.mContext = context;
        // setup SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mPlayer = new SoundPool.Builder().setMaxStreams(soundCount).build();
        } else {
            //noinspection deprecation
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

        mCurrentSampleId = INVALID_SAMPLE_ID;
    }

    private void loadResource(int resource, boolean playAfterLoad) {
        if (mPreviouslyUsed.size() >= MAX_SOUND_CAPACITY) {
            int soundId = mPreviouslyUsed.pop();

            mPlayer.unload(soundId);
            int index = mSounds.indexOfValue(soundId);
            mSounds.removeAt(index);
        }

        Logger.d(TAG, "Loading id: %s", String.valueOf(resource));

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
            Logger.d(TAG, "Sound id: %s has finished loading", sampleId);

            if (sampleId == mCurrentSampleId) {
                playSampleId(sampleId);
            }
        } else {
            Logger.e(TAG, "Sound id: %s couldn't be loaded", sampleId);
        }
    }

    private int playSampleId(int sampleId) {
        if (isLooped) {
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, LOOP_FLAG, rate);
            mPlayer.setLoop(mCurrentStreamId, LOOP_FLAG);
        } else {
            mCurrentStreamId = mPlayer.play(sampleId, leftVolume, rightVolume, 1, NO_LOOP_FLAG, rate);
        }

        Logger.d(TAG, "stream sample is %s", mCurrentSampleId);
        Logger.d(TAG, "stream id is %s", mCurrentStreamId);


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
        mCurrentSampleId = mSounds.get(resource, INVALID_SAMPLE_ID);

        if (mCurrentSampleId == INVALID_SAMPLE_ID) {
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
        mCurrentSampleId = mSounds.get(resource, INVALID_SAMPLE_ID);
        if (mCurrentSampleId == INVALID_SAMPLE_ID) {
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
        Logger.d(TAG, "Changing the volume");

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
        int soundId = mSounds.get(resource, INVALID_SAMPLE_ID);
        if (soundId != INVALID_SAMPLE_ID) {
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
                Logger.e(TAG, "Audio focus gain");
                // resume playback
                resume();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Logger.e(TAG, "Audio focus loss");
                // Lost focus for an unbounded amount of time: stop playback and release media mediaPlayer
                pause();

                release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Logger.e(TAG, "Audio focus loss transient");
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media mediaPlayer because playback
                // is likely to resume
                pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Logger.e(TAG, "Audio focus loss transient can duck");
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                setVolume(.1f);
                break;
        }
    }

    public void resume() {
        Logger.d(TAG, "Resume");

        mPlayer.autoResume();
    }

    public void pause() {
        Logger.d(TAG, "Pausing");

        mPlayer.autoPause();
    }

    // Cleanup
    public void release() {
        if (mPlayer != null) {
            if (mCurrentStreamId != INVALID_STREAM_ID) {
                mPlayer.unload(mCurrentStreamId);
            }

            mAudioManager.abandonAudioFocus(this);

            mCurrentStreamId = INVALID_STREAM_ID;
            mPlayer.release();
        }
    }

    public void stop() {
        Logger.d(TAG, "Stopping");

        if (mPlayer != null) {
            mPlayer.stop(mCurrentStreamId);
            mCurrentStreamId = INVALID_STREAM_ID;
        }
    }

}
