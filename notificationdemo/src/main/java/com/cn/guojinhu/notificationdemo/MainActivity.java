package com.cn.guojinhu.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
        Intent intent = getIntent();
        

//        RingtoneManager manager = this.getSystemService(RingtoneManager.class);
        absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        StorageManager ma = this.getSystemService(StorageManager.class);
        Log.d("Vo7ice", "path:" + absolutePath);
        Toast.makeText(MainActivity.this, "path:" + absolutePath, Toast.LENGTH_LONG).show();
        
        Notification.Builder builder1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        notification = builder.setSmallIcon(R.drawable.ic_local_play_24dp)//小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//launcher 图标
                .setAutoCancel(false)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setTicker("You clicked BaseNF!")
                .setContentTitle("这是通知标题")
                .setContentText("这是通知内容")
                /*.setOngoing(true)*/
                .setFullScreenIntent(createIntent(MainActivity.this), true)
                .setWhen(System.currentTimeMillis())
                .setColor(Color.RED)
                .build();
        notification.extras.putInt("headsup", 0);//取消head-up风格通知,使fullScreenIntent起效
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                mNotificationManager.notify(1000, notification);
            }
        });

        Button view = (Button) findViewById(R.id.btn_cancel);
        assert view != null;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mNotificationManager.cancel(1000);
                //mNotificationManager.notify(1001, notification);
                //startActivity(new Intent(MainActivity.this, AlertActivity.class));
                Toast.makeText(MainActivity.this, "Toast Start", Toast.LENGTH_SHORT).show();
                addFile();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Toast.makeText(MainActivity.this, "Toast End", Toast.LENGTH_LONG).show();
//                    }
//                }).start();

            }
        });

    }

    private void addFile() {
        File file = null;
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Log.d("Vo7ice", "f:" + f.exists() + "--path:" + f.getAbsolutePath());
        if (f.exists()) {
            file = new File(f, "test.txt");
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("Vo7ice", "file:" + file.exists() + "--path:" + file.getAbsolutePath());
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write("this is a test");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private PendingIntent createIntent(Context context) {
        Intent intent = new Intent(context, TargetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
