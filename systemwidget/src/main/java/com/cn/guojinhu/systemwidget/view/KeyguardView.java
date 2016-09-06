package com.cn.guojinhu.systemwidget.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;


/**
 * Created by guojin.hu on 2016/8/4.
 */

public class KeyguardView extends ImageView {

    private Context mContext;

    private Paint mCirclePaint;
    private int mCircleColor;
    private float mCircleRadius;
    private int mNormalColor;
    private int mInverseColor;

    private ValueAnimator mCircleAnimator;

    private ArgbEvaluator mColorInterpolator;

    private boolean isDown = false;
    private boolean isUp = false;
    private int mCenterX;
    private int mCenterY;
    private int[] mTempPoint = new int[2];
    private float mMaxCircleSize;

    public KeyguardView(Context context) {
        this(context, null);
    }

    public KeyguardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyguardView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public KeyguardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCircleColor = 0xffffffff;//白色
        mCirclePaint.setColor(mCircleColor);

        mNormalColor = 0xffffffff;//白色
        mInverseColor = 0xff000000;//黑色

        mColorInterpolator = new ArgbEvaluator();

    }

    private void updateIconColor() {
        Drawable drawable = getDrawable().mutate();//mutate 复制另一份ConstantState
        int color = (int) mColorInterpolator.evaluate(0.5f, mNormalColor, mInverseColor);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        mMaxCircleSize = getMaxCircleSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("Vo7ice", "width-->" + getWidth() + "height-->" + getHeight());
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        mCircleRadius = 60;
        //if (isUp) {
            drawBackgourndCircle(canvas);
        //}
        canvas.save();
        canvas.scale(0.5f, 0.5f, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
        canvas.restore();
    }

    private void drawBackgourndCircle(Canvas canvas) {
        if (mCircleRadius > 0) {
            updateIconColor();
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);
        }
    }

    private float getMaxCircleSize() {
        getLocationInWindow(mTempPoint);
        float rootWidth = getRootView().getWidth();
        Log.d("Vo7ice", "rootwidth-->" + rootWidth);
        float width = mTempPoint[0] + mCenterX;
        Log.d("Vo7ice", "width000-->" + width);
        width = Math.max(rootWidth - width, width);
        Log.d("Vo7ice", "width111-->" + width);
        float height = mTempPoint[1] - mCenterY;
        return (float) Math.hypot(width, height);
    }


    public float getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        mCircleRadius = circleRadius;
        updateIconColor();
        invalidate();
    }

    public void setCircleRadius(@DimenRes int resId) {
        mCircleRadius = mContext.getResources().getDimension(resId);
        invalidate();
    }
}
