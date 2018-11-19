package com.yigotone.app.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebjar.EbLoginDelegate;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.activity.ForgetPwdActivity;
import com.yigotone.app.ui.activity.MainActivity;
import com.yigotone.app.ui.register.RegisterActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 14:51.
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View, EbLoginDelegate.LoginCallback {
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_pwd) EditText etPwd;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.tv_register) TextView tvRegister;
    @BindView(R.id.tv_forget_pwd) TextView tvForgetPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }


    @Override
    public void initView() {
        etPhone.setText("18237056520");
        etPwd.setText("123456");
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.iv_logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.iv_logo:
                startActivity(new Intent(this, NewDeviceLoginActivity.class));
                break;
        }
    }

    private void login() {
        String phoneNum = etPhone.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (!phoneNum.matches("0?(13|14|15|16|17|18)[0-9]{9}")) {
            U.showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            U.showToast("请输入密码");
            return;
        }
        showLoadingDialog("正在登录");
        presenter.login(phoneNum, pwd);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        U.showToast("服务器错误");
    }

    @Override
    public void loginSuccess(UserBean bean) {
        dismissLoadingDialog();
        UserManager.getInstance().save(this, bean.getData().get(0));
        String phoneNumber = UserManager.getInstance().userData.getMobile();

        EbAuthDelegate.AuthloginByVfc(phoneNumber, null, new OnAuthLoginListener() {
            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Logger.d("EbLoginDelegate: authcode " + authcode + deadline);
                DataUtils.saveDeadline(phoneNumber, deadline, LoginActivity.this);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                if (AuthUtils.isDeadlineAvailable(deadline)) {
                    EbLoginDelegate.login(phoneNumber, "ebupt");
                    Logger.d("EbLoginDelegate: login");
                }
            }

            @Override
            public void ebAuthFailed(int code, String reason) {
                Logger.d("ebAuthFailed: " + code + reason);
                startActivity(new Intent(LoginActivity.this, NewDeviceLoginActivity.class));
            }
        });
    }

    @Override
    public void loginFail(String errorMsg) {
        dismissLoadingDialog();
        Log.e("loginFail ", errorMsg);
    }

    @Override
    public void ebLoginResult(int i, String s) {
        if (i == 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else {    startActivity(new Intent(LoginActivity.this, NewDeviceLoginActivity.class));}
    }

    @Override
    public void ebLogoutOk() {

    }

    @Override
    public void ebLogouted() {

    }
}
