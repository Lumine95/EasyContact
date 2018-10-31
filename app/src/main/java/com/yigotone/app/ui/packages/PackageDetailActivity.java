package com.yigotone.app.ui.packages;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.view.BaseTitleBar;

/**
 * Created by ZMM on 2018/10/31 17:03.
 */
public class PackageDetailActivity extends BaseActivity<PackageContract.Presenter> implements PackageContract.View {
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
}
