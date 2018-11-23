package com.yigotone.app.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.application.MyApplication;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.ui.activity.ForgetPwdActivity;
import com.yigotone.app.ui.login.LoginActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/2 9:54.
 */
public class SettingActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("设置").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }

    @OnClick({R.id.rl_modify_pwd, R.id.rl_about_us, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class)
                        .putExtra("tag", "modify"));
                break;
            case R.id.rl_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定退出登录？")
                .setPositiveButton("确定", (d, which) -> {
                    showLoadingDialog("正在删除");
                    Map<String, Object> map = new HashMap<>();
                    map.put("uid", UserManager.getInstance().userData.getUid());
                    map.put("token", UserManager.getInstance().userData.getToken());
                    presenter.logout(UrlUtil.LOG_OUT, map, "logout");
                })
                .setNegativeButton("取消", (d, which) -> d.dismiss())
                .create().show();
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "logout":
                CodeBean code = (CodeBean) result;
                if (code.getStatus() == 0) {
                    U.savePreferences("uid", "");
                    U.savePreferences("token", "");
                    MyApplication.getInstance().exit();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }
    }
}
