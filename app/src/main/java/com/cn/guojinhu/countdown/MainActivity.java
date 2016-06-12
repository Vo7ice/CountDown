package com.cn.guojinhu.countdown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CountDownRing ring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = (CountDownRing) findViewById(R.id.ring);
        if (ring != null) {
            ring.setSumTime(100);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
