package com.yigotone.app.ui.setting;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/11/2 11:10.
 */
public class VoiceAccountActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_account;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("语音账户").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
