package com.yigotone.app.ui.setting;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/10/26 17:40.
 */
public class SettingPresenter extends BasePresenterImpl<SettingContract.View> implements SettingContract.Presenter {

    public SettingPresenter(SettingContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void setDisturbStatus(String url, HashMap<String, Object> map, String message) {
        Api.getInstance().getUserBeanResult(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean.getData().get(0), message);
                }, throwable -> view.onError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void logout(String url, Map<String, Object> map, String message) {
        Api.getInstance().register(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean, message);
                }, throwable -> view.onError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMyAccountInfo(String url, Map<String, Object> map, String message) {
        Api.getInstance().postAccountBean(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean, message);
                }, throwable -> view.onError(throwable));
    }
}

