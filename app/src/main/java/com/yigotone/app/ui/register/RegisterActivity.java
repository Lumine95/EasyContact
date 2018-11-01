package com.yigotone.app.ui.register;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.setting.UserProtocolActivity;
import com.yigotone.app.util.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 17:34.
 */
public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_code) EditText etCode;
    @BindView(R.id.btn_get_code) Button btnGetCode;
    @BindView(R.id.et_pwd) EditText etPwd;
    @BindView(R.id.et_confirm_pwd) EditText etConfirmPwd;
    @BindView(R.id.btn_register) Button btnRegister;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void initView() {
        btnGetCode.setSelected(true);
    }

    @Override
    public void codeObtained(UserBean bean) {
        if (bean.getStatus() == 0) {
            countDown();
        } else if (bean.getStatus() == 1005) {
            U.showToast("该账号已经被注册");
        }
    }

    @Override
    public void onRegisterResult(UserBean bean) {
        dismissLoadingDialog();
        if (bean.getStatus() == 0) {
            U.showToast("登录成功");
            finish();
        } else {
            U.showToast("注册失败");
        }
    }


    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        U.showToast("服务器错误");
    }

    @OnClick({R.id.btn_get_code, R.id.btn_register, R.id.tv_exist, R.id.tv_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                getRandomCode();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_protocol:
                startActivity(new Intent(this, UserProtocolActivity.class));
                break;
            case R.id.tv_exist:
                finish();
                break;
        }
    }

    private void register() {
        String phoneNum = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String confirm = etConfirmPwd.getText().toString().trim();
        if (!phoneNum.matches("0?(13|14|15|17|18)[0-9]{9}")) {
            U.showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            U.showToast("请输入正确的验证码");
            return;
        }
        if (!Utils.isLetterDigit(pwd)) {
            U.showToast("请输入正确格式的密码");
            return;
        }
        if (!pwd.equals(confirm)) {
            U.showToast("输入密码不一致");
            return;
        }
        showLoadingDialog("");
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phoneNum);
        map.put("code", code);
        map.put("password", pwd);
        presenter.register(UrlUtil.REGISTER_NEW_USER, map);
    }

    private void countDown() {
        btnGetCode.setSelected(false);
        btnGetCode.setOnClickListener(view -> {
        });
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                btnGetCode.setText((l / 1000) + "S");
            }

            @Override
            public void onFinish() {
                btnGetCode.setSelected(true);
                btnGetCode.setText("获取验证码");
                btnGetCode.setOnClickListener(view -> getRandomCode());
            }
        };
        countDownTimer.start();
    }

    private void getRandomCode() {
        String phoneNum = etPhone.getText().toString();
        if (!phoneNum.matches("0?(13|14|15|17|18|16|19)[0-9]{9}")) {
            U.showToast("请输入正确的手机号码");
            return;
        }
        String url = UrlUtil.GET_RANDOM_CODE + "mobile/" + phoneNum + "/type/1";
        presenter.getRandomCode(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
