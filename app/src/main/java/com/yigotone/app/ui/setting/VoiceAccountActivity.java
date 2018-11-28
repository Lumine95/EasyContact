package com.yigotone.app.ui.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.library.utils.DateUtil;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.packages.PackageListActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;

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
    @BindView(R.id.rl_date) RelativeLayout rlDate;

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
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        presenter.setDisturbStatus(UrlUtil.GET_MY_ACCOUNT_INFO, map, "getMyAccountInfo");
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
                UserBean.DataBean bean = (UserBean.DataBean) result;
                setData(bean);
                break;
        }
    }

    private void setData(UserBean.DataBean data) {
        tvMinute.setText((int) (data.getTalkTime() / 60) + "");
        tvMinuteNum.setText((int) (data.getTalkTime() / 60) + "");
        tvTitle.setText("共" + (int) (data.getAllTalkTime() / 60) + "分钟，已用" + (int) ((data.getAllTalkTime() - data.getTalkTime()) / 60) + "分钟");
        progressBar.setProgress((int) (((data.getTalkTime() * 100.00) / data.getAllTalkTime())));

        if (data.getExpirationTime() > 0) {
            rlDate.setVisibility(View.VISIBLE);
            tvDate.setText(DateUtil.getDate(data.getExpirationTime(), "yyyy.MM.dd HH:mm") + " 到期");
            int day = (int) ((data.getExpirationTime() - new Date().getTime() / 1000) / (60 * 60 * 24));
            tvDay.setText(day + "");
        } else {
            rlDate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog("");
        getMyAccount();
    }
}
