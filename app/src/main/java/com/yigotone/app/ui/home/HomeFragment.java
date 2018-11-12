package com.yigotone.app.ui.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
import com.android.library.utils.U;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.ui.activity.DialActivity;
import com.yigotone.app.ui.activity.NoDisturbActivity;
import com.yigotone.app.ui.packages.SubScribePackageActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.TriangleDrawable;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.btn_take_over) Button btnTakeOver;
    @BindView(R.id.tv_balance) TextView tvBalance;
    @BindView(R.id.iv_add) ImageView ivAdd;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.iv_dial) ImageView ivDial;
    @BindView(R.id.iv_take_over) ImageView ivTakeOver;
    @BindView(R.id.ll_take_over) LinearLayout llTakeOver;

    private EasyPopup popup;
    private String mobileStatus;   //1未托管，2托管中

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomeContract.Presenter initPresenter() {
        return new HomePresenter(this);
    }


    @Override
    public void initView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        tvPhone.setText(Utils.hidePhoneNumber(UserManager.getInstance().userData.getMobile()));
        presenter.getPackageList();
        mobileStatus = UserManager.getInstance().userData.getMobileStatus();
        refreshTakeOverLayout();
    }

    private void refreshTakeOverLayout() {
        btnTakeOver.setVisibility(mobileStatus.equals("1") ? View.VISIBLE : View.GONE);
        llTakeOver.setVisibility(mobileStatus.equals("2") ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.btn_take_over, R.id.iv_add, R.id.iv_dial, R.id.ll_take_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_over:
            case R.id.ll_take_over:
                takeOver();
                break;
            case R.id.iv_add:
                showMenu();
                break;
            case R.id.iv_dial:
                new RxPermissions(this).request(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS).subscribe(granted -> {
                    if (granted) {
                        startActivity(new Intent(mContext, DialActivity.class));
                    } else {
                        U.showToast("没有获取到权限");
                    }
                });
                break;
        }
    }

    private void takeOver() {
        showLoadingDialog("");
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", mobileStatus);
        map.put("mobile", UserManager.getInstance().userData.getMobile());
        presenter.updateMobileStatus(map);

    }

    private void showMenu() {
        popup = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.layout_right_pop)
                //   .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener((view, basePopup) -> {
                    View arrowView = view.findViewById(R.id.v_arrow);
                    arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.parseColor("#FFFFFF")));
                    view.findViewById(R.id.tv_no_disturb).setOnClickListener(v -> {
                        startActivity(new Intent(mContext, NoDisturbActivity.class));
                        popup.dismiss();
                    });
                    view.findViewById(R.id.tv_subscribe).setOnClickListener(v -> {
                        startActivity(new Intent(mContext, SubScribePackageActivity.class));
                        popup.dismiss();
                    });
                })
                .setFocusAndOutsideEnable(true)
//                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .apply();
        int offsetX = DensityUtil.dpToPx(mContext, 20) - ivAdd.getWidth() / 2;
        // int offsetY = (mTitleBar.getHeight() - ivAdd.getHeight()) / 2;
        popup.showAtAnchorView(ivAdd, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, 5);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }

    @Override
    public void onMobileStatusResult(String status) {
        dismissLoadingDialog();
        if (!TextUtils.isEmpty(status)) {
            UserManager.getInstance().userData.setMobileStatus(mobileStatus = mobileStatus.equals("1") ? "2" : "1");
            refreshTakeOverLayout();
            U.showToast(mobileStatus.equals("1") ? "设置托管成功" : "取消托管成功");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        Logger.d("eventBus: " + event);
        switch (event) {
            case "mobileNotShutDown":
                showDialogTip();
                break;
        }
    }

    private void showDialogTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_warning, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        tv_title.setText("提示");
        tv_content.setText("您的号码" + UserManager.getInstance().userData.getMobile() + "还处于开机状态，请设置飞行模式或者拔卡后再进行接管。");
        btn_ok.setText("知道了");
        btn_ok.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.setView(view);
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(mContext, 330), LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
