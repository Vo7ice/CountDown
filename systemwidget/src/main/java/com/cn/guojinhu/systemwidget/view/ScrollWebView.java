package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by guojin.hu on 2016/9/19.
 */

public class ScrollWebView extends WebView {

    ScrollCallBack mCallBack;

    public void setCallBack(ScrollCallBack callBack) {
        mCallBack = callBack;
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mCallBack) {
            mCallBack.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public interface ScrollCallBack {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
