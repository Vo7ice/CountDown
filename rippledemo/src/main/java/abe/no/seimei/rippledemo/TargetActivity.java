package abe.no.seimei.rippledemo;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import abe.no.seimei.rippledemo.Ripple.Point;
import abe.no.seimei.rippledemo.Ripple.Ripple;
import abe.no.seimei.rippledemo.Ripple.RippleLayout;

public class TargetActivity extends AppCompatActivity {

    private RippleLayout mRippleLayout;
    private Point mPoint;
    private AppBarLayout mToolbarRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        initToolBar();
        initView();
        initFragment();

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_settings);
        toolbar.setTitleTextAppearance(this, R.style.ActionBar_Title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

    }

    private void initView() {
        mRippleLayout = (RippleLayout) findViewById(R.id.ripple);
        mRippleLayout.setBackgroundColor(R.color.window_color);

        Bundle bundle = getIntent().getExtras();
        mPoint = bundle.getParcelable(Ripple.KEY_START_POINT);

        mRippleLayout.setOnStateChangeListener(new RippleLayout.OnStateChangeListener() {
            @Override
            public void onOpened() {
                startIntoAnimation();
            }

            @Override
            public void onClosed() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        mRippleLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRippleLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                mRippleLayout.setPoint(mPoint);
                mRippleLayout.start();
                return true;
            }
        });

        mToolbarRoot = (AppBarLayout) findViewById(R.id.toolbar_root);
        mToolbarRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mToolbarRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                mToolbarRoot.setTranslationY(-mToolbarRoot.getHeight());
                return true;
            }
        });
    }

    private void initFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = SettingsFragment.newInstance(null, null);
        ft.replace(R.id.root, settingsFragment, "Settings");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mRippleLayout.canBack()) {
            if (mRippleLayout.isAnimationEnd()) {
                startOutAnimation();
                mRippleLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRippleLayout.back();
                    }
                }, 300);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void startIntoAnimation() {
        mToolbarRoot.animate().translationY(0).setDuration(400).setInterpolator(new DecelerateInterpolator());
    }

    private void startOutAnimation() {
        mToolbarRoot.animate().translationY(-mToolbarRoot.getHeight()).alpha(0.0f).setDuration(400).setInterpolator(new DecelerateInterpolator());
    }
}
