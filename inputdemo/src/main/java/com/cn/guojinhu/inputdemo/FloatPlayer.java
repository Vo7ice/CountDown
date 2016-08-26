package com.cn.guojinhu.inputdemo;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.io.IOException;

/**
 * Created by guojin.hu on 2016/8/25.
 */

public class FloatPlayer implements SurfaceHolder.Callback,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "FloatPlayer";
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;

    private Context mContext;
    private WindowManager mWm;
    private WindowManager.LayoutParams mWmParams;
    private DisplayMetrics mDm;
    private AudioManager mAm;
    private MediaPlayer mMediaPlayer;

    public RelativeLayout mRootView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private int mPosition;
    private int mSeekPositionWhenPrepared;

    private int mStatusBarHeight;//状态栏高度
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mVideoWidth;
    private int mVideoHeight;

    private int mCurrentState;
    private int mTargetState;
    private int mState;

    private Uri mUri;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "focusChange:" + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS:
                    mState = mCurrentState;
                    if (mCurrentState == STATE_PLAYING) {
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN:
                    KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Service.KEYGUARD_SERVICE);
                    if (!keyguardManager.isKeyguardLocked()) {
                        if (mCurrentState != STATE_PLAYING && mState == STATE_PLAYING) {
                            play();
                            //hiddenBtn();
                            mState = STATE_IDLE;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public FloatPlayer(Context context) {
        mContext = context;
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mStatusBarHeight = getStatusBarHeight();
        DisplayMetrics mDm = new DisplayMetrics();
        mWm.getDefaultDisplay().getMetrics(mDm);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                int messageId;
                if (i == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {

                } else {

                }
                return false;
            }
        });
        mMediaPlayer.setWakeMode(mContext, PowerManager.FULL_WAKE_LOCK);

        mGestureDetector = new GestureDetector(mContext, new GestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureListener());
    }

    public void play() {
        Log.d(TAG, "start paly isPlay:" + isInPlaybackState() + ",state:" + mCurrentState);
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            setCurrentState(STATE_PLAYING);
        }
        mTargetState = STATE_PLAYING;
        mAm.requestAudioFocus(
                afChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    public void seekTo(int position) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(position);
            mSeekPositionWhenPrepared = 0;
        } else {
            mSeekPositionWhenPrepared = position;
        }
        mPosition = position;
    }

    public int getPosition() {
        if (mMediaPlayer != null
                && (mCurrentState == STATE_PAUSED || mCurrentState == STATE_PLAYING)) {
            mPosition = mMediaPlayer.getCurrentPosition();
            return mPosition;
        } else if (mMediaPlayer == null) {
            return mPosition;
        } else {
            return 0;
        }
    }

    public void pause() {
        if (isInPlaybackState()) {
            mPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            setCurrentState(STATE_PAUSED);
        }
        mTargetState = STATE_PAUSED;
    }

    public void stop() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            setCurrentState(STATE_IDLE);
            mTargetState = STATE_IDLE;
        }
    }

    public void openVideo() {
        if (mUri == null) {
            Log.w(TAG, "openVideo mUri or mSurfaceHolder is null");
            return;
        }
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            // mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }

    }

    public void release() {
        MediaPlayer tempPlayer;
        tempPlayer = mMediaPlayer;
        mMediaPlayer = null;
        if (tempPlayer != null) {
            tempPlayer.reset();
            tempPlayer.release();
        }
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    public void setCurrentState(int state) {
        mCurrentState = state;
    }

    public int getCurrentState() {
        return mCurrentState;
    }


    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void addToWindow() {
        mWm = (WindowManager) mContext.getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        mWmParams = new WindowManager.LayoutParams();
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//set type
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWmParams.gravity = Gravity.START | Gravity.TOP;
        mWmParams.format = 1;
        mWmParams.x = 0;
        mWmParams.y = 0;
        mWmParams.width = 500;
        mWmParams.height = 300;
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.float_window, null);
        mSurfaceView = (SurfaceView) mRootView.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mWm.addView(mRootView, mWmParams);

        mAm = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAm.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public void setVideoUri(Uri uri) {
        Log.d(TAG, "setVideoUri start");
        try {
            mUri = uri;
            openVideo();
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepareAsync();
                setCurrentState(STATE_PREPARING);
                mSeekPositionWhenPrepared = 0;
                mSurfaceView.refreshDrawableState();
                Log.d(TAG, "setVideoUri end");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "error:" + e.getMessage());
        }

    }

    public void removeFromWindow() {
        mWm.removeView(mRootView);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surface created");
        mSurfaceHolder = surfaceHolder;
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height) {
        Log.d(TAG, "surface changed" + arg0 + " " + arg1 + " width=" + width + " height=" + height);
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (mSeekPositionWhenPrepared > 0) {
            seekTo(mSeekPositionWhenPrepared);
        }
        if (mTargetState == STATE_PLAYING && mCurrentState != STATE_PLAYING) {
            play();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surface destroyed");
        release();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.d(TAG, "onBufferingUpdate");
        
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        setCurrentState(STATE_PREPARED);
        mVideoWidth = mMediaPlayer.getVideoWidth();
        mVideoHeight = mMediaPlayer.getVideoHeight();
        if (mVideoHeight == 0 || mVideoWidth == 0) {
            mSurfaceView.setBackgroundColor(Color.BLACK);
            mVideoWidth = 480;
            mVideoHeight = 320;
        } else {
            mSurfaceView.setBackgroundColor(Color.TRANSPARENT);
        }
        if (mVideoHeight != 0 && mVideoWidth != 0) {
            if (mSeekPositionWhenPrepared > 0) {
                seekTo(mSeekPositionWhenPrepared);
            }
            Log.d(TAG, "onPrepared mTargetState=" + mTargetState + " mCurrentState=" + mCurrentState);
            if (mTargetState == STATE_PLAYING && mCurrentState != STATE_PLAYING) {
                play();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion");
        onCompletion();
    }

    public void onCompletion() {
        setCurrentState(STATE_PLAYBACK_COMPLETED);
        mTargetState = STATE_PLAYBACK_COMPLETED;
        //updateBtnState();
        mPosition = -1;
    }

    private class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.d(TAG, "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.d(TAG, "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.d(TAG, "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d(TAG, "onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.d(TAG, "onLongPress");

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d(TAG, "onFling");
            return false;
        }
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Log.d(TAG, "onScale");
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            Log.d(TAG, "onScaleBegin");
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            Log.d(TAG, "onScaleEnd");

        }
    }
}
