package com.easycontact.app.activity;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseActivity;
import com.easycontact.app.base.BasePresenter;
import com.easycontact.app.view.BaseTitleBar;

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
