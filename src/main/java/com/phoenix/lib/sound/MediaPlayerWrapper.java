package com.phoenix.lib.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.phoenix.lib.BuildConfig;
import com.phoenix.lib.log.Logger;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.EnumSet;

/**
 * <p/>
 * <p/>
 * While running this class during Debug mode, methods that can only be called during a certain
 * state will throw a RuntimeException and log out the current state the Media Player is in.
 * <p/>
 * I want to thank Daniel Hawkes for the MediaPlayer state wrapper code.
 * Link Wrapper code from https://gist.github.com/danielhawkes/1029568
 *
 * @author Dylan Jones
 */
public class MediaPlayerWrapper implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener, AudioManager.OnAudioFocusChangeListener {
    /**
     * Wrapper interface for {@link android.media.MediaPlayer.OnPreparedListener}
     *
     * @see {@link android.media.MediaPlayer.OnPreparedListener}
     */
    public interface IOnPreparedListener {
        void onPrepared(MediaPlayerWrapper mediaPlayer);
    }

    /**
     * Wrapper interface for {@link android.media.MediaPlayer.OnCompletionListener}
     *
     * @see {@link android.media.MediaPlayer.OnCompletionListener}
     */
    public interface IOnCompletionListener {
        void onCompletion(MediaPlayerWrapper mediaPlayer);
    }

    /**
     * Wrapper interface for {@link android.media.MediaPlayer.OnInfoListener}
     *
     * @see {@link android.media.MediaPlayer.OnInfoListener}
     */
    public interface IOnInfoListener {
        boolean onInfo(MediaPlayerWrapper mediaPlayer, int what, int extra);
    }

    /**
     * Wrapper interface for {@link android.media.MediaPlayer.OnErrorListener}
     *
     * @see {@link android.media.MediaPlayer.OnErrorListener}
     */
    public interface IOnErrorListener {
        boolean onError(MediaPlayerWrapper mediaPlayer, int what, int extra);
    }

    /**
     * Wrapper interface for {@link android.media.MediaPlayer.OnSeekCompleteListener}
     *
     * @see {@link android.media.MediaPlayer.OnSeekCompleteListener}
     */
    public interface IOnSeekCompleteListener {
        void onSeekComplete(MediaPlayerWrapper mediaPlayer);
    }

    public enum State {
        IDLE, ERROR, INITIALIZED, PREPARING, PREPARED, STARTED, STOPPED, PLAYBACK_COMPLETE, PAUSED
    }

    private final static String TAG = MediaPlayerWrapper.class.getSimpleName();
    private final MediaPlayer mPlayer = new MediaPlayer();
    private final String mName;
    private IOnSeekCompleteListener mOnSeekCompleteListener;
    private State currentState;
    private IOnCompletionListener mOnCompletionListener;
    private IOnPreparedListener mOnPreparedListener;
    private IOnErrorListener mOnErrorListener;
    private IOnInfoListener mOnInfoListener;

    /**
     * Wrapper for {@link com.phoenix.lib.sound.MediaPlayerWrapper#MediaPlayerWrapper(String, int)} where the audioStreamType is {@link android.media.AudioManager#STREAM_MUSIC}
     *
     * @param name for the Media Player
     */
    public MediaPlayerWrapper(String name) {
        this(name, AudioManager.STREAM_MUSIC);
    }

    /**
     * @param name            for the Media Player
     * @param audioStreamType for the Media Player {@link android.media.MediaPlayer#setAudioStreamType(int)}
     */
    public MediaPlayerWrapper(String name, int audioStreamType) {
        mName = name;
        mPlayer.setAudioStreamType(audioStreamType);

        currentState = State.IDLE;

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
    }

    /**
     * @param onSeekCompleteListener sets the OnSeekCompleteListener
     */
    public void setOnSeekCompleteListener(IOnSeekCompleteListener onSeekCompleteListener) {
        mOnSeekCompleteListener = onSeekCompleteListener;
    }

    /**
     * @param onInfoListener sets the OnInfoListener
     */
    public void setOnInfoListener(IOnInfoListener onInfoListener) {
        mOnInfoListener = onInfoListener;
    }

    /**
     * @param onErrorListener sets the onErrorListener
     */
    public void setOnErrorListener(IOnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    /**
     * @param onCompletionListener sets the onCompletionListener
     */
    public void setOnCompletionListener(IOnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    /**
     * @param onPreparedListener sets the onPreparedListener
     */
    public void setOnPreparedListener(IOnPreparedListener onPreparedListener) {
        mOnPreparedListener = onPreparedListener;
    }

    /**
     * @return the name of the MediaPlayer
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the MediaPlayer
     */
    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    /**
     * Wrapper method for {@link #setDataSource(android.content.Context, int)} that calls {@link #prepare()} after
     *
     * @param context    the Context to use
     * @param resourceId audio resource id
     *
     * @see {@link #setDataSource(android.content.Context, int)}
     */
    public void setDataPrepare(final Context context, int resourceId) {
        if (currentState == State.IDLE) {
            setDataSource(context, resourceId);
            prepare();
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#setDataSource(java.io.FileDescriptor, long, long)}
     *
     * @param context    the Context to use
     * @param resourceId audio resource id
     *
     * @see {@link android.media.MediaPlayer#setDataSource(java.io.FileDescriptor, long, long)}
     */
    public void setDataSource(final Context context, int resourceId) {
        if (currentState == State.IDLE) {
            AssetFileDescriptor assetFileDescriptor = null;

            try {
                assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
                if (assetFileDescriptor == null) {
                    Logger.e(TAG, "Couldn't get assetFileDescriptor For: %s", mName);
                    return;
                }

                mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                currentState = State.INITIALIZED;
            } catch (IllegalArgumentException e) {
                Logger.e(TAG, e, "Passed in an illegal Argument while loading resource id For: %s", mName);
            } catch (IllegalStateException e) {
                Logger.e(TAG, e, "Illegal State while loading resource id For: %s", mName);
            } catch (IOException e) {
                Logger.e(TAG, e, "IO exception while loading resource id For: %s", mName);
            } finally {
                if (assetFileDescriptor != null) {
                    try {
                        assetFileDescriptor.close();
                    } catch (IOException e) {
                        Logger.e(TAG, "Exception while closing asset file descriptor", e);
                    }
                }
            }
        } else {
            Logger.e(TAG, "setDataSource(int resourceId) exception. Current MediaPlayer state: %s For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#prepare()}
     *
     * @see {@link android.media.MediaPlayer#prepare()}
     */
    public void prepare() {
        Logger.d(TAG, "Calling prepare() For: %s", mName);

        if (EnumSet.of(State.INITIALIZED, State.STOPPED).contains(currentState)) {
            try {
                mPlayer.prepare();
                currentState = State.PREPARED;
            } catch (IOException e) {
                Logger.e(TAG, e, "Error preparing audio file: %s", mName);
            }
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link #setDataSource(android.content.Context, int)}  that calls {@link #prepareAsync()} after
     *
     * @param context    the Context to use
     * @param resourceId audio file resource id
     *
     * @see {@link #setDataSource(android.content.Context, int)}
     */
    public void setDataPrepareAsync(final Context context, int resourceId) {
        if (currentState == State.IDLE) {
            setDataSource(context, resourceId);
            prepareAsync();
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#prepare()}
     *
     * @see {@link android.media.MediaPlayer#prepare()}
     */
    public void prepareAsync() {
        Logger.d(TAG, "Calling prepareAsync() For: %s", mName);
        if (EnumSet.of(State.INITIALIZED, State.STOPPED).contains(currentState)) {
            mPlayer.prepareAsync();
            currentState = State.PREPARING;
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link #setDataSource(java.lang.String)} that calls {@link #prepare()} after
     *
     * @param path the path of the file, or the http/rtsp URL of the stream you want to play
     *
     * @see {@link #setDataSource(java.lang.String)}
     */
    public void setDataPrepare(String path) {
        if (currentState == State.IDLE) {
            setDataSource(path);
            prepare();
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#setDataSource(java.lang.String)}
     *
     * @param path the path of the file, or the http/rtsp URL of the stream you want to play
     *
     * @see {@link android.media.MediaPlayer#setDataSource(java.lang.String)}
     */
    public void setDataSource(String path) {
        if (currentState == State.IDLE) {
            try {
                mPlayer.setDataSource(path);

                currentState = State.INITIALIZED;
            } catch (IllegalArgumentException e) {
                Logger.e(TAG, e, "Passed in an illegal Argument while loading  audio from path: %s, for: %s", path, mName);
            } catch (IllegalStateException e) {
                Logger.e(TAG, e, "Illegal State while loading  audio from path: %s, for: %s", path, mName);
            } catch (IOException e) {
                Logger.e(TAG, e, "IO exception while loading audio from path: %s, for: %s", path, mName);
            } catch (SecurityException e) {
                Logger.e(TAG, e, "Permission denied opening audio from path: %s, for: %s", path, mName);
            }
        } else {
            Logger.e(TAG, "setDataSource(String path) invalid state. Current MediaPlayer state: %s For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link #setDataSource(java.lang.String)} that calls {@link #prepareAsync()} after
     *
     * @param path the path of the file, or the http/rtsp URL of the stream you want to play
     *
     * @see {@link #setDataSource(java.lang.String)}
     */
    public void setDataPrepareAsync(String path) {
        if (currentState == State.IDLE) {
            setDataSource(path);
            prepareAsync();
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#setLooping(boolean)}
     *
     * @param looping whether to loop or not
     *
     * @see {@link android.media.MediaPlayer#setLooping(boolean)}
     */
    public void setLooping(boolean looping) {
        Logger.d(TAG, "Calling setLooping(%b) For: %s", looping, mName);
        if (EnumSet.of(State.IDLE, State.INITIALIZED, State.STOPPED, State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            mPlayer.setLooping(looping);
        } else {
            Logger.e(TAG, "Current MediaPlayer state: " + currentState.toString() + " For: " + mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#seekTo(int)}
     *
     * @param milliseconds the offset in milliseconds from the start to seek to
     *
     * @see {@link android.media.MediaPlayer#seekTo(int)}
     */
    public void seekTo(int milliseconds) {
        Logger.d(TAG, "Calling seekTo() For: %s", mName);
        if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            mPlayer.seekTo(milliseconds);
        } else {
            Logger.e(TAG, "Current MediaPlayer state: " + currentState.toString() + " For: " + mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#reset()}
     */
    public void reset() {
        Logger.d(TAG, "Calling reset() For: %s", mName);

        mPlayer.reset();
        currentState = State.IDLE;
    }

    /**
     * @return the current state of the {@link android.media.MediaPlayer}
     */
    public State getState() {
        Logger.d(TAG, "Calling getState() == " + currentState.toString() + " For: " + mName);
        return currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        Logger.d(TAG, "on prepared For: %s", mName);
        currentState = State.PREPARED;

        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "on completion For: %s", mName);
        currentState = State.PLAYBACK_COMPLETE;

        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        String whatTag = StringUtils.EMPTY;
        String extraTag = StringUtils.EMPTY;

        if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            whatTag = "Media error unknown";
        } else if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            whatTag = "Media error server died";
        }

        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                extraTag = "Media error IO";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                extraTag = "Media error malformed";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                extraTag = "Media error unsupported";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                extraTag = "Media error timed out";
                break;
        }

        // Logger the integer values if the values are implementation dependent.
        Logger.e(TAG, "onError For: %s, What: %s, Extra: %s",
                mName,
                (StringUtils.isBlank(whatTag) ? String.valueOf(what) : whatTag),
                (StringUtils.isBlank(extraTag) ? String.valueOf(extra) : extraTag));
        currentState = State.ERROR;

        return mOnErrorListener != null && mOnErrorListener.onError(this, what, extra);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
        Logger.d(TAG, "onInfo For: %s, What: %s, Extra: %s", mName, what, extra);

        return mOnInfoListener != null && mOnInfoListener.onInfo(this, what, extra);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Logger.d(TAG, "onSeekCompete For: %s", mName);

        if (mOnSeekCompleteListener != null) {
            mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#getCurrentPosition()}
     *
     * @return the current position in milliseconds
     *
     * @see {@link android.media.MediaPlayer#getCurrentPosition()}
     */
    public int getCurrentPosition() {
        if (currentState != State.ERROR) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * Wrapper method for {@link android.media.MediaPlayer#getDuration()} ()}
     *
     * @return the duration in milliseconds, if no duration is available (for example, if streaming live content), -1 is returned.
     *
     * @see {@link android.media.MediaPlayer#getDuration()} ()}
     */
    public int getDuration() {
        // Prepared, Started, Paused, Stopped, PlaybackCompleted
        if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.STOPPED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            return mPlayer.getDuration();
        } else {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Logger.e(TAG, "Audio focus gain For: %s", mName);
                // resume playback
                start();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Logger.e(TAG, "Audio focus loss For: %s", mName);
                // Lost focus for an unbounded amount of time: stop playback and release media mediaPlayer
                if (isPlaying()) {
                    stop();
                }
                release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Logger.e(TAG, "Audio focus loss transient For: %s", mName);
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media mediaPlayer because playback
                // is likely to resume
                if (isPlaying()) {
                    pause();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Logger.e(TAG, "Audio focus loss transient can duck For: %s", mName);
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                setVolume(.1f);
                break;
        }
    }

    /**
     *
     */
    public void start() {
        Logger.d(TAG, "Calling start() For: %s", mName);
        if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            mPlayer.start();
            currentState = State.STARTED;
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s, For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    public boolean isPlaying() {
        Logger.d(TAG, "Calling isPlaying() For: %s", mName);
        if (currentState != State.ERROR) {
            return mPlayer.isPlaying();
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s, For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }

        return false;
    }

    public void stop() {
        Logger.d(TAG, "Calling stop() For: %s", mName);
        if (EnumSet.of(State.PREPARED, State.STARTED, State.STOPPED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            mPlayer.stop();
            currentState = State.STOPPED;
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s, For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    public void release() {
        Logger.d(TAG, "Calling release() For: %s", mName);

        stop();
        mPlayer.release();
    }

    public void pause() {
        Logger.d(TAG, "Calling pause() For: %s", mName);
        if (EnumSet.of(State.STARTED, State.PAUSED).contains(currentState)) {
            mPlayer.pause();
            currentState = State.PAUSED;
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s, For: %s", currentState.toString(), mName);


            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

    public void setVolume(float level) {
        setVolume(level, level);
    }

    public void setVolume(float left, float right) {
        Logger.d(TAG, "Calling setVolume(%.2f, %.2f) For: %s", left, right, mName);
        if (EnumSet.of(State.IDLE, State.INITIALIZED, State.STOPPED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
            mPlayer.setVolume(left, right);
        } else {
            Logger.e(TAG, "Current MediaPlayer state: %s, For: %s", currentState.toString(), mName);

            if (BuildConfig.DEBUG) {
                throw new RuntimeException();
            }
        }
    }

}
