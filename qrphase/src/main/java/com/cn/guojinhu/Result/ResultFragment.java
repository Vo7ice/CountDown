package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import abe.no.seimei.qrphase.R;

public abstract class ResultFragment extends Fragment implements View.OnClickListener {

    protected Button mConfirmButton;
    protected TextView mTextInfo;

    public ResultFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(getLayoutResId(), container, false);
        mConfirmButton = (Button) rootView.findViewById(R.id.button_confirm);
        mTextInfo = (TextView) rootView.findViewById(R.id.text_info);
        mConfirmButton.setOnClickListener(this);
        initConfirmButton();
        initInfoText();
        return rootView;
    }


    @Override
    public void onClick(View view) {
        if (view == mConfirmButton) {
            confirmResult();
        }
    }

    protected abstract void initInfoText();

    protected abstract void initConfirmButton();

    protected abstract void confirmResult();

    protected abstract int getLayoutResId();
}
