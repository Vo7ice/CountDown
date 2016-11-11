package com.cn.guojinhu.inputdemo;

import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.IOException;

/**
 * Created by guojin.hu on 2016/8/25.
 */

public class FloatPlayer implements SurfaceHolder.Callback,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener,
        View.OnClickListener,
        View.OnTouchListener,
        SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "FloatPlayer";
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_SEEK_FLING = 6;

    private static final int MEDIA_PLAY = 0;
    private static final int MEDIA_PAUSE = 1;

    private Context mContext;
    private WindowManager mWm;
    private WindowManager.LayoutParams mWmParams;
    private DisplayMetrics mDm;
    private AudioManager mAm;
    private MediaPlayer mMediaPlayer;

    public RelativeLayout mRootView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private SeekBar mSeekBar;
    private ImageButton mCloseButton;
    private ImageButton mPlayPauseButton;
    private ImageButton mBackToNormalButton;

    private int mPosition;
    private int mSeekPositionWhenPrepared;

    private int mStatusBarHeight;//状态栏高度
    private int mSurfaceWidth;//surfaceView宽度
    private int mSurfaceHeight;//surfaceView高度
    private int mVideoWidth;//video宽度
    private int mVideoHeight;//video高度
    private int mWindowWidth;//屏幕宽度
    private int mWindowHeight;//屏幕高度

    private int mCurrentState;
    private int mTargetState;
    private int mState;

    private Uri mUri;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private final Seekhandler mHandler;
    private final Runnable mPositionUpdate;
    private final Runnable mStartHidingUI;

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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

        mHandler = new Seekhandler();
        mPositionUpdate = new Runnable() {
            @Override
            public void run() {
                if (null != mMediaPlayer) {
                    int currentPosition = mMediaPlayer.getCurrentPosition();
                    //Log.d(TAG, "runnable:currentPosition-->" + currentPosition);
                    mSeekBar.setProgress(currentPosition);
                    mHandler.postDelayed(mPositionUpdate, 500);
                }
            }
        };

        mStartHidingUI = new Runnable() {
            @Override
            public void run() {
                startFadeOut();
            }
        };

    }

    public void play() {
        Log.d(TAG, "start paly isPlay:" + isInPlaybackState() + ",state:" + mCurrentState);
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            setCurrentState(STATE_PLAYING);
            updateBtnState();
        }
        startFadeIn();
        mTargetState = STATE_PLAYING;
        mAm.requestAudioFocus(
                afChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        mHandler.sendEmptyMessage(MEDIA_PLAY);
    }

    public void seekFling() {
        Log.d(TAG, "start seek fling,mTargetState-->" + mTargetState);
        if (isInPlaybackState()) {
            mMediaPlayer.pause();
            mHandler.removeCallbacks(mPositionUpdate);//去除更新进度的线程 会导致系统崩溃
            setCurrentState(STATE_SEEK_FLING);
            updateBtnState();
        }
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
                && (mCurrentState == STATE_PAUSED ||
                mCurrentState == STATE_PLAYING ||
                mCurrentState == STATE_SEEK_FLING)) {
            mPosition = mMediaPlayer.getCurrentPosition();
            Log.d(TAG, "getPosition-->" + mPosition);
            return mPosition;
        } else if (mMediaPlayer == null) {
            Log.d(TAG, "getPosition-->" + mPosition);
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
            updateBtnState();
        }
        startFadeIn();
        mTargetState = STATE_PAUSED;
        mHandler.sendEmptyMessage(MEDIA_PAUSE);
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

    private void updateBtnState() {
        if (mCurrentState == STATE_PLAYING) {
            mPlayPauseButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        } else {
            mPlayPauseButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        }
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
        mWmParams = createLayoutParams();
        mDm = new DisplayMetrics();
        mWm.getDefaultDisplay().getMetrics(mDm);
        //Log.d("Vo7ice","mDm:"+mDm.toString());

        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.float_window, null);
        mRootView.setOnTouchListener(this);
        mSurfaceView = (SurfaceView) mRootView.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        mSeekBar = (SeekBar) mRootView.findViewById(R.id.position_seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mCloseButton = (ImageButton) mRootView.findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(this);
        mPlayPauseButton = (ImageButton) mRootView.findViewById(R.id.paly_pause_button);
        mPlayPauseButton.setOnClickListener(this);
        mBackToNormalButton = (ImageButton) mRootView.findViewById(R.id.back_normal_button);
        mBackToNormalButton.setOnClickListener(this);

        mWm.addView(mRootView, mWmParams);

        mAm = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAm.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private LayoutParams createLayoutParams() {
        LayoutParams wmParams = new LayoutParams();
        wmParams.type = LayoutParams.TYPE_PHONE;//set type
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_KEEP_SCREEN_ON;
        //wmParams.gravity = Gravity.CENTER;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = LayoutParams.MATCH_PARENT;
        wmParams.height = 150;
        return wmParams;
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
        } catch (IOException | IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
            Log.d(TAG, "error:" + e.getMessage());
        }

    }

    public void onCompletion() {
        setCurrentState(STATE_PLAYBACK_COMPLETED);
        mTargetState = STATE_PLAYBACK_COMPLETED;
        updateBtnState();
        mPosition = -1;
        closeWindow();
    }

    public void removeFromWindow() {
        if (null != mRootView) {
            mWm.removeView(mRootView);
            mRootView = null;
        }
        mAm.abandonAudioFocus(afChangeListener);
    }

    public void closeWindow() {
        removeFromWindow();
        ((Service) mContext).stopSelf();
    }

    private boolean mIsVisible = true;//UI状态

    /**
     * 隐藏UI动画
     */
    private void startFadeOut() {
        ValueAnimator animator = ValueAnimator.ofFloat(1F, 0F);
        animator.setDuration(500);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentAlpha = (float) valueAnimator.getAnimatedValue();
                mCloseButton.setAlpha(currentAlpha);
                mPlayPauseButton.setAlpha(currentAlpha);
                mBackToNormalButton.setAlpha(currentAlpha);
                mSeekBar.setAlpha(currentAlpha);
            }
        });
        animator.start();
        mHandler.removeCallbacks(mStartHidingUI);
        mIsVisible = false;
    }

    /**
     * 显示UI动画
     */
    private void startFadeIn() {
        ValueAnimator animator = ValueAnimator.ofFloat(0F, 1F);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentAlpha = (float) valueAnimator.getAnimatedValue();
                mCloseButton.setAlpha(currentAlpha);
                mPlayPauseButton.setAlpha(currentAlpha);
                mBackToNormalButton.setAlpha(currentAlpha);
                mSeekBar.setAlpha(currentAlpha);
            }
        });
        if (!mIsVisible) {
            animator.start();
        }
        mHandler.removeCallbacks(mStartHidingUI);
        //Log.d("Vo7ice", "mIsVisible," + mIsVisible);
        if (mCurrentState == STATE_PLAYING || mCurrentState == STATE_PAUSED && mIsVisible) {
            mHandler.postDelayed(mStartHidingUI, 7000);
        }
        mIsVisible = true;
    }

    /**
     * 判断当前UI状态,决定是否显示UI
     */
    private void performUI() {
        if (mIsVisible) {
            startFadeOut();
        } else {
            startFadeIn();
        }
    }

    /**
     * 回到正常播放模式
     */
    private void backToNormal() {

    }

    private void reSize() {
        if (null != mMediaPlayer && null != mRootView) {
            mWindowWidth = mDm.widthPixels;
            mWindowHeight = mDm.heightPixels;
            int targetWidth = mVideoWidth;
            int targetHeight = mVideoHeight;
            float widthRate = (float) mWindowWidth / mVideoWidth;//屏幕视频宽度比
            float heightRate = (float) mWindowHeight / mVideoHeight;//屏幕视频高度比
            float videoRate = (float) mVideoHeight/mVideoWidth;//视频宽高比
        }
    }

    /*SurfaceView监听*/
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

    /*MediaPlayer监听*/
    //缓冲进度
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.d(TAG, "onBufferingUpdate");
        if (null != mSeekBar) {
            mSeekBar.setSecondaryProgress(i);
        }


    }

    /*MediaPlayer监听*/
    //准备就绪
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
        //mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mSeekBar.setMax(mMediaPlayer.getDuration());
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

    /*MediaPlayer监听*/
    //播放完毕
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion");
        onCompletion();
    }


    /*按钮点击监听*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_button:
                closeWindow();
                break;
            case R.id.paly_pause_button:
                if (mCurrentState == STATE_PLAYING) {
                    pause();
                } else {
                    play();
                }
                break;
            case R.id.back_normal_button:
                closeWindow();
                break;
        }
    }

    /*SeekBar 滑动监听*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //Log.d(TAG, "i-->" + i);
        if (getCurrentState() == STATE_SEEK_FLING) {
            mMediaPlayer.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekFling();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position = getPosition();
        //Log.d(TAG, "position-->" + position);
        if (mTargetState == STATE_PLAYING) {
            seekTo(position);
            play();
        } else if (mTargetState == STATE_PAUSED) {
            seekTo(position);
            pause();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() > 1) {
            mScaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    private boolean mSingleTapState = false;//单击状态

    /*手势监听*/
    private class GestureListener implements GestureDetector.OnGestureListener {

        final int FLING_MIN_DISTANCE = 100;//应该为像素

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.d("GestureListener", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.d("GestureListener", "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.d("GestureListener", "onSingleTapUp");
            Log.d("GestureListener", "mSingleTap-->" + mSingleTapState + ",mIsVisible-->" + mIsVisible);
            if (mSingleTapState) {
                mSingleTapState = false;
                performUI();
            } else {
                mSingleTapState = true;
                performUI();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("GestureListener", "onScroll-->" + Math.abs(distanceY));
            //if (Math.abs(distanceY) > FLING_MIN_DISTANCE) {
            mWmParams.x = (int) (e2.getRawX() - e1.getRawX());
            mWmParams.y = (int) (e2.getRawY() - e1.getRawY());
            if (null != mRootView) {
                mWm.updateViewLayout(mRootView, mWmParams);
            }
            //}
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.d("GestureListener", "onLongPress");

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GestureListener", "onFling");
            return false;
        }
    }

    /*放大缩小手势*/
    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Log.d(TAG, "onScale");
            float scale = scaleGestureDetector.getScaleFactor();

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

    private final class Seekhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MEDIA_PLAY:
                    mHandler.removeCallbacks(mPositionUpdate);
                    mHandler.postDelayed(mPositionUpdate, 500);
                    break;
                case MEDIA_PAUSE:
                    mHandler.removeCallbacks(mPositionUpdate);
                    break;
            }
        }
    }
}
