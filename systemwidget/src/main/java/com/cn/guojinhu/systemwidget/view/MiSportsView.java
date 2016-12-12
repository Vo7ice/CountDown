package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;

import com.cn.guojinhu.systemwidget.R;

/**
 * Created by guojin.hu on 2016/12/5.
 */

public class MiSportsView extends View {

    private float mArcRadius;
    private float mDotRadius;
    private int mArcColor;
    private int mDotColor;
    private Paint mArcPaint;
    private Paint mDotPaint;
    private Path mPath;

    public MiSportsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MiSportsView(Context context) {
        this(context, null);
    }

    public MiSportsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiSportsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.MiSportsView);
        mArcRadius = ty.getDimension(R.styleable.MiSportsView_arc_radius, 50);
        mDotRadius = ty.getDimension(R.styleable.MiSportsView_dot_radius, 10);
        mArcColor = ty.getColor(R.styleable.MiSportsView_arc_color, Color.BLUE);
        mDotColor = ty.getColor(R.styleable.MiSportsView_dot_radius, Color.RED);
        ty.recycle();

        init();
    }

    private void init(){
        mArcPaint = new Paint();
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.FILL);
        mArcPaint.setAntiAlias(true);

        mDotPaint = new Paint();
        mDotPaint.setColor(mDotColor);
        mDotPaint.setStyle(Paint.Style.STROKE);
        mDotPaint.setStrokeWidth(2.0f);

        mPath = new Path();
        String sysui_nav_bar = Settings.Secure.getString(getContext().getContentResolver(), "sysui_nav_bar");
        update(R.string.action_settings);
    }

    private void update(@StringRes int resId) {
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
