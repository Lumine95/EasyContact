package com.easycontact.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseActivity;
import com.easycontact.app.base.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 14:51.
 */
public class LoginActivity extends BaseActivity {
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
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
