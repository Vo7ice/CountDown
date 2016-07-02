package com.cn.guojinhu.systemwidget;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TopBar.OnTopBarClickListener {

    TopBar topBar;
    com.cn.guojinhu.systemwidget.Extra.TopBar mTopbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        topBar = (TopBar) findViewById(R.id.topBar);
        assert topBar != null;
        topBar.setOnTopBarClickListener(this);
        //topBar.setFlag(TopBar.SHOW_ALL);

        Log.d("Vo7ice","getTitle:"+topBar.getTitleText());

        // 获得我们创建的topbar
        mTopbar = (com.cn.guojinhu.systemwidget.Extra.TopBar) findViewById(R.id.topBar2);
        // 为topbar注册监听事件，传入定义的接口
        // 并以匿名类的方式实现接口内的方法
        mTopbar.setOnTopbarClickListener(
                new com.cn.guojinhu.systemwidget.Extra.TopBar.topbarClickListener() {

                    @Override
                    public void rightClick() {
                        Toast.makeText(MainActivity.this,
                                "right", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void leftClick() {
                        Toast.makeText(MainActivity.this,
                                "left", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        // 控制topbar上组件的状态
        //mTopbar.setButtonVisable(0, true);
        //mTopbar.setButtonVisable(1, false);
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

    @Override
    public void onLeftButtonClick(View view) {
        Snackbar.make(view, "Left action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onRightButtonClick(View view) {
        Snackbar.make(view, "Right action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
