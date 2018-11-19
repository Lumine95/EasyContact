package com.yigotone.app.ui.packages;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.PackageBean;
import com.yigotone.app.ui.activity.PayResultActivity;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.SelectPaymentPopupView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/31 17:03.
 */
public class PackageDetailActivity extends BaseActivity<PackageContract.Presenter> implements PackageContract.View {
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.tv_intro) TextView tvIntro;
    @BindView(R.id.content) ConstraintLayout content;
    private PackageBean.DataBean.PackageEntity data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_package_detail;
    }

    @Override
    public PackageContract.Presenter initPresenter() {
        return new PackagePresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("套餐详情").setLeftIcoListening(v -> finish());
        data = (PackageBean.DataBean.PackageEntity) getIntent().getSerializableExtra("data");
        setData();
    }

    private void setData() {
        tvTitle.setText(data.getPackageName());
        tvPrice.setText("价格: RMB " + data.getPrice() + "元");
        tvIntro.setText("描述: " + data.getIntro());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_package_buy, null);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_sure = view.findViewById(R.id.tv_sure);

        tv_tip.setText("购买【40分钟30天】套餐后，您的主叫分钟数将有54分钟，有效期至本地时间2018-11-06  18:00时");
        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        tv_sure.setOnClickListener(v -> {
            dialog.dismiss();
            selectPayment();
        });

        dialog.setCancelable(true);
        dialog.setView(view);
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(this, 340), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void selectPayment() {
        SelectPaymentPopupView popupView = new SelectPaymentPopupView(this);
        popupView.showAtLocation(content, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.setOnSuccessListener(channel -> {
            U.showToast(channel + "");
            startActivity(new Intent(this, PayResultActivity.class));
        });
    }

    @Override
    public void onResult(Object result, String message) {

    }

    @Override
    public void onLayoutError(Throwable throwable) {

    }
}
