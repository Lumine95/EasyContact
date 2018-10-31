package com.yigotone.app.ui.login;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/10/26 17:40.
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void login(String mobile, String pwd) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", pwd);
        Api.getInstance().login(UrlUtil.LOGIN, map)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getStatus() == 0) {
                        view.loginSuccess(bean);
                    } else {
                        view.loginFail(bean.getErrorMsg());
                    }
                }, throwable -> view.onError(throwable));
    }
}
