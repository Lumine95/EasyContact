package com.yigotone.app.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebjar.EbLoginDelegate;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.login.LoginActivity;
import com.yigotone.app.ui.login.LoginContract;
import com.yigotone.app.ui.login.LoginPresenter;
import com.yigotone.app.ui.login.NewDeviceLoginActivity;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;

/**
 * Created by ZMM on 2018/11/12 9:59.
 */
public class SplashActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View, EbLoginDelegate.LoginCallback {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        EbLoginDelegate.setLoginCallback(this);
        new RxPermissions(this).request(Manifest.permission.READ_CONTACTS).subscribe(granted -> {
            if (granted) {
                permissionGranted();
            } else {
                finish();
            }
        });

        // 移动SDK鉴权

    }

    private void permissionGranted() {

        String uid = (String) U.getPreferences("uid", "");
        String token = (String) U.getPreferences("token", "");

        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) {
            presenter.autoLogin(uid, token);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void loginSuccess(UserBean bean) {
        UserManager.getInstance().save(this, bean.getData().get(0));
//        startActivity(new Intent(this, MainActivity.class));
//        finish();

        String phoneNumber = UserManager.getInstance().userData.getMobile();
        EbLoginDelegate.SetJustAddress(Constant.JUSTALK_KEY, Constant.JUSTALK_IP);
        DataUtils.saveAccount(phoneNumber, this);

        EbAuthDelegate.AuthloginByVfc(phoneNumber, null, new OnAuthLoginListener() {
            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Logger.d("EbLoginDelegate: authcode " + authcode + deadline);
                DataUtils.saveDeadline(phoneNumber, deadline, SplashActivity.this);
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
                if (AuthUtils.isDeadlineAvailable(deadline)) {
                    EbLoginDelegate.login(phoneNumber, "ebupt");
                    Logger.d("EbLoginDelegate: login");
                }
            }

            @Override
            public void ebAuthFailed(int code, String reason) {
                Logger.d("ebAuthFailed: " + code + reason);
                startActivity(new Intent(SplashActivity.this, NewDeviceLoginActivity.class));
            }
        });
    }

    @Override
    public void loginFail(String errorMsg) {
        U.showToast("登录信息失效，请重新登录");
        U.savePreferences("uid", "");
        U.savePreferences("token", "");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void ebLoginResult(int i, String s) {
        dismissLoadingDialog();
        if (i == 0) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, NewDeviceLoginActivity.class));
        }
    }

    @Override
    public void ebLogoutOk() {

    }

    @Override
    public void ebLogouted() {

    }
}
