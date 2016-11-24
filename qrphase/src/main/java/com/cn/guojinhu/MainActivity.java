package com.cn.guojinhu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import abe.no.seimei.qrphase.R;

public class MainActivity extends AppCompatActivity {

    private Button mButtonNormal;
    private Button mButtonURL;
    private Button mButtonContact;

    private static final String code_contact =
            "BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "N:Tort\n" +
                    "TEL;TYPE=home:222\n" +
                    "TEL;TYPE=home:2525\n" +
                    "END:VCARD";

    private static final String code_url_right = "http://weibo.cn/qr/userinfo?uid=1665356464";
    private static final String code_url_error = "this is a test string";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mButtonNormal = (Button) findViewById(R.id.button_normal);
        mButtonURL = (Button) findViewById(R.id.button_url);
        mButtonContact = (Button) findViewById(R.id.button_contact);
        mButtonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startActionPhase(MainActivity.this, code_url_error);
            }
        });

        mButtonURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startActionPhase(MainActivity.this, code_url_right);
            }
        });

        mButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startActionPhase(MainActivity.this, code_contact);
            }
        });

    }

}
