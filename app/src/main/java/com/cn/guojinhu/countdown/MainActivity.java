package com.cn.guojinhu.countdown;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements CountDownRing.onChangeListener {

    CountDownRing ring;
    ImageView image_background;
    int[] imageResource = {};
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = (CountDownRing) findViewById(R.id.ring);
        image_background = (ImageView) findViewById(R.id.image_background);
        ring.setSize(imageResource.length);
        ring.setIndex(0);

    }

    @Override
    public void onChangeImage(int index) {
        image_background.setBackgroundResource(imageResource[index]);
    }

}
