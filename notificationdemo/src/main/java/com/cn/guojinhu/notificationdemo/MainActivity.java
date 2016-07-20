package com.cn.guojinhu.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {


    NotificationManager mNotificationManager;
    Notification notification;
    private String absolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        RingtoneManager manager = this.getSystemService(RingtoneManager.class);
        absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("Vo7ice","path:"+absolutePath);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        notification = builder.setSmallIcon(R.drawable.ic_local_play_24dp)//小图标
                              .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//launcher 图标
                              .setAutoCancel(false)
                              .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                              .setTicker("You clicked BaseNF!")
                              .setContentTitle("这是通知标题")
                              .setContentText("这是通知内容")
                              .setWhen(System.currentTimeMillis())
                              .setColor(Color.RED)
                              .build();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                mNotificationManager.notify(1000,notification);
            }
        });

        Button view = (Button) findViewById(R.id.btn_cancel);
        assert view != null;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mNotificationManager.cancel(1000);
                mNotificationManager.notify(1001, notification);
                Toast.makeText(MainActivity.this, "Toast String", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
