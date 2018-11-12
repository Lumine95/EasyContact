package com.yigotone.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.view.CustomProgressDialog;
import com.android.library.view.UIHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/10/23 15:36.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    public P presenter;
    public BaseActivity mActivity;
    public Context mContext;
    private CustomProgressDialog loadingDialog;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //   View view = View.inflate(mActivity, getLayoutId(), null);
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = initPresenter();
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    protected abstract int getLayoutId();

    protected abstract P initPresenter();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected void showLoadingDialog(String text) {
        if (loadingDialog == null) {
            loadingDialog = UIHelper.newNormalProgressDialog(mContext, text);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        dismissLoadingDialog();
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
