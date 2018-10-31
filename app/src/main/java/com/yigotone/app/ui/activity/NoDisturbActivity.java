package com.yigotone.app.ui.activity;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/10/24 11:50.
 */
public class NoDisturbActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_no_disturb;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("免打扰设置").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
