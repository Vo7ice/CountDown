package abe.no.seimei.rippledemo.Ripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by guojin.hu on 2016/11/11.
 */

public class RippleLayout extends View {

    private static final int DEFAULT_DURATION = 400;

    private Paint mPaint;
    private int mDuration = DEFAULT_DURATION;
    private int mCenterX = -1;
    private int mCenterY = -1;
    private int mRadius;
    private boolean mIsAnimationEnd = true;
    private boolean mIsOpened = false;
    private Point mPoint;
    private ObjectAnimator mAnimator;

    private OnStateChangeListener mListener;

    public RippleLayout(Context context) {
        this(context, null);
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBackgroundColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }


    public void setCenterX(int centerX) {
        this.mCenterX = centerX;
        invalidate();
    }

    public void setCenterY(int centerY) {
        this.mCenterY = centerY;
        invalidate();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    public boolean isAnimationEnd() {
        return mIsAnimationEnd;
    }

    public void setAnimationEnd(boolean isAnimationEnd) {
        this.mIsAnimationEnd = isAnimationEnd;
    }

    public void setPoint(Point point) {
        this.mPoint = point;
    }

    public void setonStateChangeListener(OnStateChangeListener listener) {
        this.mListener = listener;
    }

    public void start() {
        if (null != mPoint) {
            start(mPoint.x, mPoint.y);
        }
    }

    public boolean canBack() {
        return mPoint != null;
    }

    public void back() {
        if (canBack()) {
            back(mPoint.x, mPoint.y);
        }
    }

    public void back(Point point) {
        back(point.x, point.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsOpened) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        } else {
            canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        }
    }

    /**
     * 初始化画笔
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
    }

    /**
     * 勾股定理 算出最大半径
     *
     * @return radius
     */
    private double computeRadius() {
        return Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth()) / 2;
    }

    private void start(int startX, int startY) {
        mIsAnimationEnd = false;
        mIsOpened = false;
        setCenterX(startX);
        setCenterY(startY);
        double maxRadius = computeRadius();

        PropertyValuesHolder radiusPVH = PropertyValuesHolder.ofInt("radius", 0, (int) maxRadius);
        PropertyValuesHolder centerXPVH = PropertyValuesHolder.ofInt("centerX", startX, getWidth() / 2);
        PropertyValuesHolder centerYPVH = PropertyValuesHolder.ofInt("centerY", startY, getHeight() / 2);

        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, radiusPVH, centerXPVH, centerYPVH);
        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onOpened();
            }
        });
        mAnimator.start();
    }

    private void back(int startX, int startY) {
        mIsAnimationEnd = false;
        mIsOpened = false;
        setCenterX(startX);
        setCenterY(startY);
        double maxRadius = computeRadius();

        PropertyValuesHolder radiusPVH = PropertyValuesHolder.ofInt("radius", (int) maxRadius, 0);
        PropertyValuesHolder centerXPVH = PropertyValuesHolder.ofInt("centerX", getWidth() / 2, startX);
        PropertyValuesHolder centerYPVH = PropertyValuesHolder.ofInt("centerY", getHeight() / 2, startY);

        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, radiusPVH, centerXPVH, centerYPVH);
        mAnimator.setDuration(mDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onClosed();
            }
        });
        mAnimator.start();
    }

    /**
     * 正在打开
     */
    private void onOpened() {
        mIsOpened = true;
        mIsAnimationEnd = true;
        invalidate();

        if (null != mListener) {
            mListener.onOpened();
        }
    }

    /**
     * 正在关闭
     */
    private void onClosed() {
        mIsOpened = false;
        mIsAnimationEnd = true;
        setVisibility(GONE);

        if (null != mListener) {
            mListener.onClosed();
        }
    }

    interface OnStateChangeListener {
        void onOpened();

        void onClosed();
    }

}
