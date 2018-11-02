package com.yigotone.app.ui.setting;

import android.content.Intent;
import android.view.View;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.activity.ForgetPwdActivity;
import com.yigotone.app.view.BaseTitleBar;

import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/2 9:54.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("设置").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick({R.id.rl_modify_pwd, R.id.rl_about_us, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class)
                        .putExtra("tag", "modify"));
                break;
            case R.id.rl_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_logout:
                break;
        }
    }
}
