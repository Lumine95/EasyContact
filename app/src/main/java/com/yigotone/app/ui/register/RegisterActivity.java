package com.yigotone.app.ui.register;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebauth.biz.auth.OnAuthcodeListener;
import com.ebupt.ebjar.EbLoginDelegate;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.activity.MainActivity;
import com.yigotone.app.ui.login.NewDeviceLoginActivity;
import com.yigotone.app.ui.setting.UserProtocolActivity;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;
import com.yigotone.app.util.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 17:34.
 */
public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View, EbLoginDelegate.LoginCallback {
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
        EbLoginDelegate.setLoginCallback(this);
        btnGetCode.setSelected(true);
    }

    @Override
    public void codeObtained(UserBean bean) {
//        if (bean.getStatus() == 0) {
//            countDown();
//        }
    }

    @Override
    public void onRegisterResult(CodeBean bean) {
        dismissLoadingDialog();
        if (bean.getStatus() == 0) {
            U.showToast("注册成功");
//           finish();
            login();
        } else {
            U.showToast("注册失败");
        }
    }

    @Override
    public void onModifyResult(CodeBean bean) {

    }

    @Override
    public void loginSuccess(UserBean bean) {
        UserManager.getInstance().save(this, bean.getData().get(0));
        String phoneNumber = UserManager.getInstance().userData.getMobile();
        EbLoginDelegate.SetJustAddress(Constant.JUSTALK_KEY, Constant.JUSTALK_IP);
        DataUtils.saveAccount(phoneNumber, this);
        EbAuthDelegate.AuthloginByVfc(phoneNumber, etCode.getText().toString(), new OnAuthLoginListener() {
            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Logger.d("EbLoginDelegate: authcode " + authcode + deadline);
                DataUtils.saveDeadline(phoneNumber, deadline, RegisterActivity.this);
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
                startActivity(new Intent(RegisterActivity.this, NewDeviceLoginActivity.class));
            }
        });
    }

    @Override
    public void loginFail(String errorMsg) {
        dismissLoadingDialog();
        Log.e("loginFail ", errorMsg);
    }


    @Override
    public void onFinish() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        // U.showToast("服务器错误");
        Log.d("onError", throwable.toString());
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
        HashMap<String, Object> map = new HashMap<>();
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
        //  String url = UrlUtil.GET_RANDOM_CODE + "mobile/" + phoneNum + "/type/1";
        // presenter.getRandomCode(url);

        EbAuthDelegate.getAuthcode(phoneNum, new OnAuthcodeListener() {
            @Override
            public void ebAuthCodeOk() {
                countDown();
            }

            @Override
            public void ebAuthCodeFailed(int code, String reason) {
                Log.d("ebAuthCodeFailed: ", code + reason);
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void ebLoginResult(int i, String s) {
        dismissLoadingDialog();
        if (i == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, NewDeviceLoginActivity.class));
        }
    }

    @Override
    public void ebLogoutOk() {

    }

    @Override
    public void ebLogouted() {

    }
}
