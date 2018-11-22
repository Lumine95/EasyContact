package com.yigotone.app.ui.order;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/11/14 14:59.
 */
public class OrderPresenter extends BasePresenterImpl<OrderContract.View> implements OrderContract.Presenter {
    OrderPresenter(OrderContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getOrderList(String url, Map<String, Object> map, String message) {
        Api.getInstance().getOrderList(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.onResult(bean, message);
                }, throwable -> view.onLayoutError(throwable));
    }
}
