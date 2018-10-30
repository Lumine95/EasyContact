package com.yigotone.app.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.activity.ForgetPwdActivity;
import com.yigotone.app.activity.MainActivity;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.ui.register.RegisterActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 14:51.
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {
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


    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd})
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
        }
    }

    private void login() {
        String phoneNum = etPhone.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (!phoneNum.matches("0?(13|14|15|17|18)[0-9]{9}")) {
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
    public void loginSuccess() {
        dismissLoadingDialog();
        // TODO: 2018/10/30  save info.
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginFail(String errorMsg) {
        dismissLoadingDialog();
        U.showToast("登录失败");
        Log.e("loginFail ", errorMsg);
    }
}
