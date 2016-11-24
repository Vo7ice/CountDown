package com.cn.guojinhu.Result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import abe.no.seimei.qrphase.R;

/**
 * Created by guojin.hu on 2016/11/24.
 */

public class ContactResultFragment extends ResultFragment {

    private Bundle mBundle;

    public static ContactResultFragment newInstance(Bundle bundle) {
        ContactResultFragment fragment = new ContactResultFragment();
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
    protected void initInfoText() {
        mTextInfo.setVisibility(View.GONE);
    }

    @Override
    protected void initConfirmButton() {
        mConfirmButton.setText("插入联系人");
    }

    @Override
    protected void confirmResult() {
        //调通讯录 新增联系人
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contact;
    }
}
