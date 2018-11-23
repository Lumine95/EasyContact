package com.yigotone.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.message.SystemMsgActivity;
import com.yigotone.app.ui.order.OrderActivity;
import com.yigotone.app.ui.setting.SettingActivity;
import com.yigotone.app.ui.setting.VoiceAccountActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_phone) TextView tvPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        tvPhone.setText(Utils.hidePhoneNumber(UserManager.getInstance().userData.getMobile()));
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick({R.id.iv_setting, R.id.rl_voice_account, R.id.rl_data_account, R.id.rl_my_order, R.id.rl_call_detail, R.id.rl_message, R.id.rl_feedback, R.id.rl_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.rl_voice_account:
                startActivity(new Intent(mContext, VoiceAccountActivity.class));
                break;
            case R.id.rl_data_account:
                break;
            case R.id.rl_my_order:
                startActivity(new Intent(mContext, OrderActivity.class));
                break;
            case R.id.rl_call_detail:
                break;
            case R.id.rl_message:
                startActivity(new Intent(mContext, SystemMsgActivity.class));
                break;
            case R.id.rl_feedback:
                break;
            case R.id.rl_share:
                break;
        }
    }
}
