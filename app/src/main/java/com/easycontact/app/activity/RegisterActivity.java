package com.easycontact.app.activity;

import android.os.Build;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseActivity;
import com.easycontact.app.ui.register.RegisterContract;
import com.easycontact.app.ui.register.RegisterPresenter;

import java.util.HashMap;

/**
 * Created by ZMM on 2018/10/24 17:34.
 */
public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void initView() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", "18237056520");
        map.put("type", "1");
        map.put("deviceId", Build.SERIAL);
        presenter.getRandomCode(map);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
