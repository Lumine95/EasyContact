package com.easycontact.app.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseFragment;
import com.easycontact.app.base.BasePresenter;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class MineFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onFinish(Object result, String message) {

    }

    @Override
    public void onError(Throwable throwable, String message) {

    }
}
