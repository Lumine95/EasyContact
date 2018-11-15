package com.yigotone.app.ui.message;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/11/14 14:59.
 */
public class MessagePresenter extends BasePresenterImpl<MessageContract.View> implements MessageContract.Presenter {
    MessagePresenter(MessageContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void sendMessage(String url, Map<String, Object> map, String message) {
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
    public void getMessageList(String url, HashMap<String, Object> map, String message) {
        Api.getInstance().getMessageList(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean, message);
                }, throwable -> view.onLayoutError(throwable));

    }
}
