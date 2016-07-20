package com.cn.guojinhu.systemwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;


public class MyScrollerView extends ViewGroup {


    private int mScreenHeight;//屏幕高度
    private Scroller mScroller;

    private int mLastY;//保存上次的Y坐标
    private int mStart;

    public MyScrollerView(Context context) {
        super(context);
        initViews(context);
    }

    public MyScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MyScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public MyScrollerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mScreenHeight * childCount;
        setLayoutParams(mlp);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initViews(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenHeight = dm.heightPixels;
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int deltaY = mLastY - y;
                Log.d("Vo7ice", "getScrollY-->" + getScrollY() + ",position:" + (getHeight() - mScreenHeight));
                if (getScrollY() < 0) {
                    deltaY = 0;
                }
                if (getScrollY() > getHeight() - mScreenHeight) {
                    deltaY = 0;
                }

                scrollBy(0, deltaY);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                int dScrollerY = checkAlighment();//获得增量
                if (dScrollerY > 0) {
                    if (dScrollerY < mScreenHeight / 3) {//增量不足以换图
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -dScrollerY);
                    } else {//增量可以换图
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, mScreenHeight - dScrollerY);
                    }
                } else {
                    if (-dScrollerY < mScreenHeight / 3) {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -dScrollerY);
                    } else {
                        mScroller.startScroll(
                                0, getScrollY(),
                                0, -mScreenHeight - dScrollerY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    private int checkAlighment() {
        int mEnd = getScrollY();
        boolean isUp = ((mEnd - mStart) > 0);
        int lastPrev = mEnd % mScreenHeight;
        int lastNext = mScreenHeight - lastPrev;
        Log.d("Vo7ice", "lastPrev-->" + lastPrev + ",lastNext-->" + lastNext);
        if (isUp) {
            //向上
            return lastPrev;
        } else {
            return -lastNext;
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
