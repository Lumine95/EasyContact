package com.yigotone.app.ui.packages;

import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
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
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        selectPayment();
    }

    private void selectPayment() {
        SelectPaymentPopupView popupView = new SelectPaymentPopupView(this);
        popupView.showAtLocation(content, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.setOnSuccessListener(channel -> {
            U.showToast(channel + "");
        });
    }
}
