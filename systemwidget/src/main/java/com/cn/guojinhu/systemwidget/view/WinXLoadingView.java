package com.cn.guojinhu.systemwidget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by guojin.hu on 2016/9/20.
 */

public class WinXLoadingView extends View {

    private Paint mPaint;

    private Path mPath;

    private PathMeasure mPathMeasure;

    private ValueAnimator valueAnimator;

    private float progress;

    private int mWidth, mHeight;


    public WinXLoadingView(Context context) {
        this(context, null);
    }

    public WinXLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinXLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WinXLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPath = new Path();
        RectF oval = new RectF(-150, -150, 150, 150);
        mPath.addArc(oval, -90, 359.9f);
        mPathMeasure = new PathMeasure(mPath, false);

        initAnimate();
    }

    private void initAnimate() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Path dst = new Path();
        mPathMeasure.getSegment(mPathMeasure.getLength() * progress,
                mPathMeasure.getLength() * progress + 1, dst, true);
        canvas.drawPath(dst, mPaint);
    }
}
