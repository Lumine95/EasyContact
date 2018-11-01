package com.yigotone.app.ui.setting;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/11/1 9:26.
 */
public class UserProtocolActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_protocol;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("用户协议").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
