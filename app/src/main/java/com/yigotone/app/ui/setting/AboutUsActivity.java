package com.yigotone.app.ui.setting;

import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/2 10:12.
 */
public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_version) TextView tvVersion;
    @BindView(R.id.tv_intro) TextView tvIntro;

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
        new BaseTitleBar(this).setTitleText("关于易沟通").setLeftIcoListening(v -> finish());
        tvVersion.setText("version " + U.getVersionName());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
