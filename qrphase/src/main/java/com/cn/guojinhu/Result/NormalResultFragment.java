package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import abe.no.seimei.qrphase.R;

import static com.cn.guojinhu.Result.ResultActivity.KEY_RESULT;


/**
 * Created by guojin.hu on 2016/11/23.
 */

public class NormalResultFragment extends ResultFragment {

    private Bundle mBundle;

    public static NormalResultFragment newInstance(Bundle bundle) {
        NormalResultFragment fragment = new NormalResultFragment();
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
        Log.d("Vo7ice", "result:" + result);
        textResult.setText(result);
    }

    @Override
    protected void initInfoText() {
        mTextInfo.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initConfirmButton() {
        mConfirmButton.setText("复制文本");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_normal;
    }

    @Override
    protected void confirmResult() {
//        mClipboardManager.setPrimaryClip(ClipData.newPlainText("text",));
    }
}
