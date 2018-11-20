package com.yigotone.app.ui.call;

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
public class CallPresenter extends BasePresenterImpl<CallContract.View> implements CallContract.Presenter {

    public CallPresenter(CallContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void postParams(String url, Map<String, Object> map, String message) {
        Api.getInstance().postCallParams(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onResult(bean, message);
                }, throwable -> view.onError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCallDetail(String url, HashMap<String, Object> map, String message) {
        Api.getInstance().getCallDetail(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean, message);
                }, throwable -> view.onError(throwable));
    }
}
