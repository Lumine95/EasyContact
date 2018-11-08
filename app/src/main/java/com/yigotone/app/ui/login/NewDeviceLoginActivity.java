package com.yigotone.app.ui.login;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
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
                showDialog();
                break;
        }
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

}
