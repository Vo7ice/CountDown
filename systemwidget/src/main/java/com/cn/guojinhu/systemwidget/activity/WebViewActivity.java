package com.cn.guojinhu.systemwidget.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;

import com.cn.guojinhu.systemwidget.R;
import com.cn.guojinhu.systemwidget.view.ScrollWebView;

public class WebViewActivity extends AppCompatActivity {

    private ScrollWebView webView;
    private CheckBox checkBox;

    private float mScale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (ScrollWebView) findViewById(R.id.webview);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//是否支持js
        settings.setSupportZoom(true);//是否支持缩放
        checkBox.setEnabled(false);
        webView.setCallBack(new ScrollWebView.ScrollCallBack() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                //Log.d("Vo7ice", "scale:" + webView.getScale() + "new scale:" + mScale);
                float webviewContentHeight = webView.getContentHeight() * webView.getScale();
                int webviceCurrentHeight = webView.getHeight() + webView.getScrollY();
                if ((webviewContentHeight - webviceCurrentHeight) < 10) {//位于底部10的位移
                    checkBox.setEnabled(true);
                } else {
                    checkBox.setEnabled(false);
                }
            }
        });
        try {
            webView.loadUrl("file:///android_asset/terms.html");
        } catch (Throwable e) {
        }


    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            mScale = newScale;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
