package com.yigotone.app.ui.register;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/10/26 17:40.
 */
public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getRandomCode(String url) {
        Api.getInstance().getRandomCode(url)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.codeObtained(bean);
                }, throwable -> view.onError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void register(String url, Map<String, String> map) {
        Api.getInstance().register(url, map)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onRegisterResult(bean);
                }, throwable -> view.onError(throwable));
    }
}
