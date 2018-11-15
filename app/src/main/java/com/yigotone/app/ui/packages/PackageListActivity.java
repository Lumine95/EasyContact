package com.yigotone.app.ui.packages;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.library.view.MyGridView;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.PackageBean;
import com.yigotone.app.ui.adapter.CommonAdapter;
import com.yigotone.app.ui.adapter.MyViewHolder;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/10/31 14:57.
 */
public class PackageListActivity extends BaseActivity<PackageContract.Presenter> implements PackageContract.View {

    @BindView(R.id.grid_view) MyGridView gridView;
    @BindView(R.id.tv_tip) TextView tvTip;
    @BindView(R.id.ll_tip) LinearLayout llTip;
    @BindView(R.id.scrollView) ScrollView scrollView;
    private StatusLayoutManager statusLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subscribe;
    }

    @Override
    public PackageContract.Presenter initPresenter() {
        return new PackagePresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("订购套餐").setLeftIcoListening(v -> finish());
        initStatusLayoutManager();
        getPackageList(true);
    }

    private void initStatusLayoutManager() {
        statusLayoutManager = new StatusLayoutManager.Builder(scrollView).setOnStatusClickListener(v -> {
            getPackageList(true);
        }).build();
    }

    private void getPackageList(boolean isLoadingLayout) {
        if (isLoadingLayout) {
            statusLayoutManager.showLoadingLayout();
        }
        presenter.getPackageList("getPackageList");
    }

    private void setData(PackageBean.DataBean data) {
        if (!TextUtils.isEmpty(data.getNotes())) {
            llTip.setVisibility(View.VISIBLE);
            tvTip.setText(data.getNotes());
        }

        gridView.setAdapter(new CommonAdapter<PackageBean.DataBean.PackageEntity>(this, data.getPackageX(), R.layout.item_package) {
            @Override
            public void convert(MyViewHolder helper, PackageBean.DataBean.PackageEntity item, int position) {
                helper.setText(R.id.tv_title, item.getPackageName());
                helper.setText(R.id.tv_price, item.getPrice());
                helper.getConvertView().setOnClickListener(v ->
                        startActivity(new Intent(PackageListActivity.this, PackageDetailActivity.class))
                );
            }
        });
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getPackageList":
                PackageBean.DataBean data = (PackageBean.DataBean) result;
                if (data.getPackageX().size() > 0) {
                    setData(data);
                } else {
                    statusLayoutManager.showEmptyLayout();
                }
                break;
        }
    }

    @Override
    public void onLayoutError(Throwable throwable) {
         statusLayoutManager.showErrorLayout();
        dismissLoadingDialog();
        Logger.e("onError: " + throwable);
    }
}
