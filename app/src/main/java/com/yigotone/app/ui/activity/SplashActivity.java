package com.yigotone.app.ui.activity;

import android.content.Intent;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.login.LoginActivity;

/**
 * Created by ZMM on 2018/11/12 9:59.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        // 移动SDK鉴权


        startActivity(new Intent(this, LoginActivity.class));

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
