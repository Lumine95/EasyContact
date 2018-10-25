package com.easycontact.app.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by ZMM on 2018/1/17.
 */

public interface BasePresenter {

    // 初始化
    void start();

    // Activity destroy 置空view
    void detach();

    // 将网络请求的disposable添加入compositeDisposable,退出时销毁
    void addDisposable(Disposable subscription);

    // 注销所有请求
    void unDisposable();
}
