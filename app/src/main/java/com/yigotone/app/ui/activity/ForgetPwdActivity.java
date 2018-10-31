package com.yigotone.app.ui.activity;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/10/24 15:36.
 */
public class ForgetPwdActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_froget_pwd;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("忘记密码").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
