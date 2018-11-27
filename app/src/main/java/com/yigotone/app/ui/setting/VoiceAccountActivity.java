package com.yigotone.app.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.AccountBean;
import com.yigotone.app.ui.packages.PackageListActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZMM on 2018/11/2 11:10.
 */
public class VoiceAccountActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {
    @BindView(R.id.tv_minute) TextView tvMinute;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_minute_num) TextView tvMinuteNum;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.tv_date) TextView tvDate;
    @BindView(R.id.tv_day) TextView tvDay;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_account;
    }

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("语音账户")
                .setLeftIcoListening(v -> finish())
                .setTitleRight("订购").setRightIcoListening(v ->
                startActivity(new Intent(this, PackageListActivity.class)));
        getMyAccount();
    }

    private void getMyAccount() {
        showLoadingDialog("");
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        presenter.getMyAccountInfo(UrlUtil.GET_MY_ACCOUNT_INFO, map, "getMyAccountInfo");
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
    public void onResult(Object result, String message) {
        switch (message) {
            case "getMyAccountInfo":
                AccountBean bean = (AccountBean) result;
                setData(bean.getData());
                break;
        }
    }

    private void setData(AccountBean.DataBean data) {

    }
}
