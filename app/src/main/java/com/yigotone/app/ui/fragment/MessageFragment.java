package com.yigotone.app.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class MessageFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }
    @Override
    public void onFinish( ) {

    }

    @Override
    public void onError(Throwable throwable ) {

    }
}
