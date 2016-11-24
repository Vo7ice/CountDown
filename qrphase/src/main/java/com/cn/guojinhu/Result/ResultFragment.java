package com.cn.guojinhu.Result;

import android.content.ClipboardManager;
import android.content.Context;
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
    protected ClipboardManager mClipboardManager;
    protected Context mContext;

    public ResultFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        mConfirmButton = (Button) rootView.findViewById(R.id.button_confirm);
        mTextInfo = (TextView) rootView.findViewById(R.id.text_info);
        initConfirmButton();
        initInfoText();
        iniSpecialLayout(rootView);
        mConfirmButton.setOnClickListener(this);
        return rootView;
    }

    protected void iniSpecialLayout(View view) {

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
