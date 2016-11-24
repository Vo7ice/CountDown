package com.cn.guojinhu.Result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import abe.no.seimei.qrphase.R;

import static com.cn.guojinhu.Result.ResultActivity.KEY_RESULT;

/**
 * Created by guojin.hu on 2016/11/24.
 */

public class UrlResultFragment extends ResultFragment {

    private Bundle mBundle;

    public static UrlResultFragment newInstance(Bundle bundle) {
        UrlResultFragment fragment = new UrlResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBundle = getArguments();
        }
    }

    @Override
    protected void iniSpecialLayout(View view) {
        TextView textResult = (TextView) view.findViewById(R.id.text_result);
        String result = mBundle.getString(KEY_RESULT);
        textResult.setText(result);
    }

    @Override
    protected void initInfoText() {
        mTextInfo.setVisibility(View.GONE);
    }

    @Override
    protected void initConfirmButton() {
        mConfirmButton.setText("浏览一下网址");
    }

    @Override
    protected void confirmResult() {
        String content = mBundle.getString(KEY_RESULT);
        Intent intent = new Intent();
        Uri uri = Uri.parse(content);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_url;
    }
}
