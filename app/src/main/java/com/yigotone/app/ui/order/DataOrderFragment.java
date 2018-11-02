package com.yigotone.app.ui.order;

import android.os.Bundle;
import android.view.View;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;

/**
 * Created by ZMM on 2018/11/2 15:07.
 */
public  class DataOrderFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.recycler_view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
