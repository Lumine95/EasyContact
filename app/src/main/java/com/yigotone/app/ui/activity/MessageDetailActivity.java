package com.yigotone.app.ui.activity;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/11/8 16:41.
 */
public class MessageDetailActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("短信详情").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
