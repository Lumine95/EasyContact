package com.yigotone.app.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.order.OrderActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/6 10:29.
 */
public class PayResultActivity extends BaseActivity {
    @BindView(R.id.tv_pay_money) TextView tvPayMoney;
    @BindView(R.id.ll_success) LinearLayout llSuccess;
    @BindView(R.id.ll_fail) LinearLayout llFail;

    @Override
    protected int getLayoutId() {
        QMUIStatusBarHelper.translucent(this);
        return R.layout.activity_pay_result;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        llFail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick({R.id.btn_back_home, R.id.btn_order, R.id.btn_cancel, R.id.btn_retry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back_home:
                break;
            case R.id.btn_order:
                finish();
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_retry:
                break;
        }
    }
}
