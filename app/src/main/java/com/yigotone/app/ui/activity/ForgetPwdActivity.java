package com.yigotone.app.ui.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.register.RegisterContract;
import com.yigotone.app.ui.register.RegisterPresenter;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 15:36.
 */
public class ForgetPwdActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.btn_get_code) Button btnGetCode;
    @BindView(R.id.et_code) EditText etCode;
    @BindView(R.id.et_new_pwd) EditText etNewPwd;
    @BindView(R.id.et_confirm_pwd) EditText etConfirmPwd;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_froget_pwd;
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void initView() {
        btnGetCode.setOnClickListener(v -> getRandomCode());
        new BaseTitleBar(this).setTitleText("忘记密码").setLeftIcoListening(v -> finish());
    }

    private void getRandomCode() {
        if (!Utils.isPhoneNumber(etPhone.getText().toString().trim())) {
            U.showToast("请输入正确的手机号");
        } else {
            String url = UrlUtil.GET_RANDOM_CODE + "mobile/" + etPhone.getText().toString().trim() + "/type/3";
            presenter.getRandomCode(url);
        }
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        modifyPwd();
    }

    private void modifyPwd() {
        String phone = etPhone.getText().toString();
        String code = etCode.getText().toString();
        String pwd = etNewPwd.getText().toString();
        String pwdConfirm = etConfirmPwd.getText().toString();
        if (!Utils.isPhoneNumber(phone)) {
            U.showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            U.showToast("请输入验证码");
            return;
        }
        if (!Utils.isLetterDigit(pwd)) {
            U.showToast("请输入正确格式的密码");
            return;
        }

        if (!TextUtils.equals(pwd, pwdConfirm)) {
            U.showToast("密码输入不一致，请重新输入");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("code", code);
        map.put("password", pwd);
        presenter.modifyPassword(UrlUtil.MODIFY_PWD, map);
    }

    @Override
    public void codeObtained(UserBean bean) {
        if (bean.getStatus() == 0) {
            countDown();
            U.showToast("验证码发送成功");
        } else {
            U.showToast("验证码发送失败");
        }
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

    @Override
    public void onRegisterResult(CodeBean bean) {

    }

    @Override
    public void onModifyResult(CodeBean bean) {
        if (bean.getStatus() == 0) {
            U.showToast("密码修改成功");
            finish();
        }
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
