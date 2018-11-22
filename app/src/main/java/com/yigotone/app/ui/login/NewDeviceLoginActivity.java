package com.yigotone.app.ui.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebauth.biz.auth.OnAuthcodeListener;
import com.ebupt.ebjar.EbLoginDelegate;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.activity.MainActivity;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.SecurityCodeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/7 14:14.
 */
public class NewDeviceLoginActivity extends BaseActivity implements SecurityCodeView.InputCompleteListener, EbLoginDelegate.LoginCallback {
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.tv_get_code) TextView tvGetCode;
    @BindView(R.id.code_view) SecurityCodeView codeView;
    private String phoneNumber;
    private CountDownTimer countDownTimer;

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
        tvPhone.setText("短信验证码已发送至" + (phoneNumber = UserManager.getInstance().userData.getMobile()));
        codeView.setInputCompleteListener(this);
        new BaseTitleBar(this).setTitleText("信息验证").setLeftIcoListening(v -> finish());
        EbLoginDelegate.setLoginCallback(this);
        tvGetCode.setOnClickListener(v -> getRandomCode());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void inputComplete() {
        // 鉴权登陆
        showLoadingDialog("");
        EbLoginDelegate.SetJustAddress(Constant.JUSTALK_KEY, Constant.JUSTALK_IP);
        DataUtils.saveAccount(phoneNumber, this);
        EbAuthDelegate.AuthloginByVfc(phoneNumber, codeView.getEditContent(), new OnAuthLoginListener() {
            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Logger.d("authcode " + authcode + deadline);
                DataUtils.saveDeadline(phoneNumber, deadline, NewDeviceLoginActivity.this);
                if (AuthUtils.isDeadlineAvailable(deadline)) {
                    EbLoginDelegate.login(codeView.getEditContent(), "ebupt");
                    Logger.d("login");
                }
            }

            @Override
            public void ebAuthFailed(int code, String reason) {
                Logger.d("ebAuthFailed: " + code + reason);
                U.showToast("验证错误");
            }
        });


    }

    @Override
    public void deleteContent(boolean isDelete) {

    }

    @OnClick({R.id.tv_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tip:
                showDialog();
                break;
        }
    }

    private void getRandomCode() {
        EbAuthDelegate.getAuthcode(phoneNumber, new OnAuthcodeListener() {
            @Override
            public void ebAuthCodeOk() {
                U.showToast("获取验证码成功");
                tvPhone.setText("短信验证码已发送至" + (phoneNumber = UserManager.getInstance().userData.getMobile()));
                countDown();
            }

            @Override
            public void ebAuthCodeFailed(int code, String reason) {
                Logger.d("ebAuthCodeFailed:" + code + reason);
            }
        });

    }

    private void countDown() {
        tvGetCode.setTextColor(getResources().getColor(R.color.color_9));
        tvGetCode.setOnClickListener(view -> {
        });
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                tvGetCode.setText((l / 1000) + "S 后重新发送");
            }

            @Override
            public void onFinish() {
                tvGetCode.setTextColor(getResources().getColor(R.color.color_3CA3ED));
                tvGetCode.setText("获取验证码");
                tvGetCode.setOnClickListener(view -> getRandomCode());
            }
        };
        countDownTimer.start();
    }


    @Override
    public void ebLoginResult(int i, String s) {
        Logger.d("sdk登录result i=" + i + "||s=" + s);
        if (i == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            U.showToast("登录失败");
        }
    }

    @Override
    public void ebLogoutOk() {

    }

    @Override
    public void ebLogouted() {

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_warning, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        tv_title.setText("收不到验证码");
        tv_content.setText("1 请确认手机号是否为当前使用的手机号\n2 请检查短信是否被安全软件拦截\n3 由于运营商网络原因，短信可能延迟到达");
        btn_ok.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.setView(view);
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(this, 330), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
