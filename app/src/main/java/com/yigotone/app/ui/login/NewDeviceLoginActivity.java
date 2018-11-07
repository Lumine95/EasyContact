package com.yigotone.app.ui.login;

import android.view.View;
import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.SecurityCodeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/7 14:14.
 */
public class NewDeviceLoginActivity extends BaseActivity implements SecurityCodeView.InputCompleteListener {
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.tv_get_code) TextView tvGetCode;
    @BindView(R.id.code_view) SecurityCodeView codeView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_device_login;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        codeView.setInputCompleteListener(this);
        new BaseTitleBar(this).setTitleText("新设备登录").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void inputComplete() {
        U.showToast(codeView.getEditContent());
    }

    @Override
    public void deleteContent(boolean isDelete) {

    }

    @OnClick({R.id.tv_get_code, R.id.tv_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                break;
            case R.id.tv_tip:
                break;
        }
    }

}
