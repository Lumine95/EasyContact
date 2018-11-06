package com.yigotone.app.ui.contact;

import android.view.View;
import android.widget.TextView;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.view.BaseTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/1  22:44.
 */
public class ContactDetailActivity extends BaseActivity {
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_phone) TextView tvPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_detail;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("联系人详情").setLeftIcoListening(v -> finish());
        ContactBean data = (ContactBean) getIntent().getSerializableExtra("data");
        tvName.setText(data.getName());
        tvPhone.setText(data.getPhone());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick({R.id.iv_phone, R.id.iv_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_phone:
                break;
            case R.id.iv_message:
                break;
        }
    }
}
