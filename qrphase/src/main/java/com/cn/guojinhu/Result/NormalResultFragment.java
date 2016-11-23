package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.annotation.Nullable;

import abe.no.seimei.qrphase.R;


/**
 * Created by guojin.hu on 2016/11/23.
 */

public class NormalResultFragment extends ResultFragment {

    private static NormalResultFragment mFragment;
    private Bundle mBundle;

    public static NormalResultFragment newInstance(Bundle bundle) {
        mFragment = new NormalResultFragment();
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBundle = getArguments();
        }
    }

    @Override
    protected void initInfoText() {
        
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

    }
}
