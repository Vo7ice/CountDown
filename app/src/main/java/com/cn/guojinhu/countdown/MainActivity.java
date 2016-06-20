package com.cn.guojinhu.countdown;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CountDownRing.onChangeListener {

    CountDownRing ring;
    ImageView image_background;
    int[] imageResource = {R.color.material_amber_600,R.color.material_blue_600,R.color.material_brown_600};
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = (CountDownRing) findViewById(R.id.ring);
        image_background = (ImageView) findViewById(R.id.image_background);
        ring.setSize(imageResource.length);
        ring.setIndex(0);
        image_background.setBackgroundColor(imageResource[0]);
        ring.setOnChangeListener(this);
    }

    @Override
    public void onChangeImage(int index) {
        //Toast.makeText(this,"index:"+index,Toast.LENGTH_SHORT).show();
        image_background.setBackgroundResource(imageResource[index]);
    }

}
