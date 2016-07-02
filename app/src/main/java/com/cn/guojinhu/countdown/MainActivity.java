package com.cn.guojinhu.countdown;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CountDownRing.onChangeListener, Animation.AnimationListener {

    CountDownRing ring;
    ImageView image_background;
    int[] imageResource = {R.color.material_amber_600, R.color.material_blue_600, R.color.material_brown_600};
    int currentIndex;

    Animation fade_out, fade_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = (CountDownRing) findViewById(R.id.ring);
        image_background = (ImageView) findViewById(R.id.image_background);
        ring.setSize(imageResource.length);
        ring.setIndex(0);
        image_background.setBackgroundResource(imageResource[0]);
        currentIndex = 0;
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ring.setOnChangeListener(this);
    }

    @Override
    public void onChangeImage(int index) {
        Toast.makeText(this,"index:"+index,Toast.LENGTH_SHORT).show();
        currentIndex = index;
        image_background.startAnimation(fade_out);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        image_background.setBackgroundResource(imageResource[currentIndex]);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
