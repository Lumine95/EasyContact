package com.yigotone.app.ui.setting;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;

/**
 * Created by ZMM on 2018/11/2 10:12.
 */
public class AboutUsActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
       // new BaseTitleBar(this).setTitleText("关于易沟通").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
