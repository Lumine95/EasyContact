package com.yigotone.app.ui.disturb;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.library.utils.DateUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.UserBean;
import com.yigotone.app.ui.setting.SettingContract;
import com.yigotone.app.ui.setting.SettingPresenter;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/24 11:50.
 */
public class NoDisturbActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {
    @BindView(R.id.btn_switch) Button btnSwitch;
    @BindView(R.id.btn_switch_time) Button btnSwitchTime;
    @BindView(R.id.tv_start_time) TextView tvStartTime;
    @BindView(R.id.rl_start) RelativeLayout rlStart;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.rl_end) RelativeLayout rlEnd;
    private int isDisturb;
    private String startTime = "22:00";
    private String endTime = "7:00";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_no_disturb;
    }

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("免打扰设置").setLeftIcoListening(v -> finish());
        isDisturb = UserManager.getInstance().userData.getDisturb();

        refreshDisturbStatus();
        setStartEndTime();
    }

    private void setStartEndTime() {
        if (!TextUtils.isEmpty(startTime)) {
            tvStartTime.setText(startTime);
        }
        if (!TextUtils.isEmpty(endTime)) {
            tvEndTime.setText(timeCompare() ? endTime : "次日" + endTime);
        }
    }

    private void refreshDisturbStatus() {
        isDisturb = UserManager.getInstance().userData.getDisturb();
        btnSwitch.setSelected(isDisturb == 2);
        if (!btnSwitch.isSelected()) {
            btnSwitchTime.setSelected(false);
        }

        if (!TextUtils.isEmpty(UserManager.getInstance().userData.getStarttime())) {
            startTime = UserManager.getInstance().userData.getStarttime();
            endTime = UserManager.getInstance().userData.getEndtime();
            btnSwitchTime.setSelected(true);
        } else {
            btnSwitchTime.setSelected(false);
        }

        rlEnd.setVisibility(btnSwitchTime.isSelected() ? View.VISIBLE : View.GONE);
        rlStart.setVisibility(btnSwitchTime.isSelected() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }

    @OnClick({R.id.btn_switch, R.id.btn_switch_time, R.id.rl_start, R.id.rl_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_switch:
                setDisturbStatus(true);
                break;
            case R.id.btn_switch_time:

                if (btnSwitch.isSelected()) {
                    if (btnSwitchTime.isSelected()) {
                        startTime = "";
                        endTime = "";
                    } else {
                        startTime = "22:00";
                        endTime = "7:00";
                    }
                    setDisturbStatus(false);
                }
                break;
            case R.id.rl_start:
                selectStartTime();
                break;
            case R.id.rl_end:
                selectEndTime();
                break;
        }
    }

    private void setDisturbStatus(boolean isMainSwitch) {
        showLoadingDialog("");
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        if (isMainSwitch) {
            map.put("type", btnSwitch.isSelected() ? 1 : 2);
        } else {
            map.put("type", 2);
        }
        if (!TextUtils.isEmpty(startTime)) {
            map.put("starttime", startTime);
            map.put("endtime", endTime);
        }
        presenter.setDisturbStatus(UrlUtil.DO_NOT_DISTURB, map, "disturb");
    }

    private void selectEndTime() {
        new TimePickerBuilder(this, (date, v) -> {
            endTime = DateUtil.getDate(date.getTime() / 1000, "HH:mm");
            setStartEndTime();
            setDisturbStatus(false);
        }).setType(new boolean[]{false, false, false, true, true, false}).build().show();
    }

    private void selectStartTime() {
        new TimePickerBuilder(this, (date, v) -> {
            startTime = DateUtil.getDate(date.getTime() / 1000, "HH:mm");
            setStartEndTime();
            setDisturbStatus(false);
        }).setType(new boolean[]{false, false, false, true, true, false}).build().show();
    }

    private boolean timeCompare() {
        long start = DateUtil.getStringToDate(startTime, "HH:mm");
        long end = DateUtil.getStringToDate(endTime, "HH:mm");
        return start < end;
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "disturb":
                UserBean.DataBean user = (UserBean.DataBean) result;
                UserManager.getInstance().userData.setDisturb(user.getDisturb());
                UserManager.getInstance().userData.setStarttime(user.getStarttime());
                UserManager.getInstance().userData.setEndtime(user.getEndtime());
                refreshDisturbStatus();
                break;
        }
    }
}
