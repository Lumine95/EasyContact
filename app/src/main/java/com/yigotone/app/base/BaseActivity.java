package com.yigotone.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.library.view.CustomProgressDialog;
import com.android.library.view.UIHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/10/23 14:07.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    public P presenter;
    private Unbinder unbinder;
    private CustomProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        presenter = initPresenter();
        unbinder = ButterKnife.bind(this);
        initView();
    }

    protected abstract int getLayoutId();

    public abstract P initPresenter();

    public abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        dismissLoadingDialog();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
    }

    public void showLoadingDialog(String text) {
        if (loadingDialog == null) {
            loadingDialog = UIHelper.newNormalProgressDialog(this, text);
            loadingDialog.getDialog().setCancelable(false);
            loadingDialog.getDialog().setCanceledOnTouchOutside(true);
        }
        loadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
