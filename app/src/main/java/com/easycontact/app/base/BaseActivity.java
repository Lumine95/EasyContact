package com.easycontact.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.library.view.CustomProgressDialog;
import com.android.library.view.UIHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/10/23 14:07.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private CustomProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(getLayoutId());

        unbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        // EventBus.getDefault().unregister(this);
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
