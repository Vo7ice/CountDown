package com.cn.guojinhu.systemwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TriGraph extends View {

    Path mPath;
    Paint mPaint;

    private float mGap;
    private float mLine;

    public TriGraph(Context context) {
        super(context);
        init();
    }

    public TriGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriGraph);
        mGap = ta.getDimension(R.styleable.TriGraph_gap, 30);
        ta.recycle();

        mLine = (float) (Math.sqrt(3.0) / 2 * mGap);
        init();
    }

    public TriGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        init();
    }

    public TriGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int originalX = getWidth()/2;
        int originalY = getHeight()/2;
        mPath.moveTo(originalX, originalY);
        mPath.lineTo((originalX - mLine / 2), (originalY - mGap));
        mPath.lineTo((originalX + mLine/2), (originalY - mGap));
        mPath.lineTo(originalX, originalY);
        canvas.drawPath(mPath, mPaint);
    }
}
