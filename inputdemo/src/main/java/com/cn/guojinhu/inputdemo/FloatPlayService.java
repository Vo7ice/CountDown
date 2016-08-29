package com.cn.guojinhu.inputdemo;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;

import com.cn.guojinhu.inputdemo.FloatPlayer;

public class FloatPlayService extends Service {

    private FloatPlayer mPlayer = null;

    public FloatPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            stopSelf();
        } else {
            Uri data = intent.getData();
            String type = intent.getType();
            Log.d("Vo7ice", "data ==" + data.getPath());
            createFloatWindow();
            mPlayer.setVideoUri(data);
            mPlayer.play();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void createFloatWindow() {
        if (null == mPlayer) {
            mPlayer = new FloatPlayer(this);
            if (mPlayer.mRootView == null) {
                mPlayer.addToWindow();
            }
        }
    }


    @Override
    public void onDestroy() {
        if (null != mPlayer) {
            mPlayer.removeFromWindow();
            mPlayer = null;
        }
        super.onDestroy();
    }
}
