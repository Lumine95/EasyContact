package com.yigotone.app.ui.packages;

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
public class PackagePresenter extends BasePresenterImpl<PackageContract.View> implements PackageContract.Presenter {

    PackagePresenter(PackageContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getPackageList(String message) {
        Api.getInstance().getPackageList(UrlUtil.GET_PACKAGE_LIST)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    if (bean.getStatus() == 0) {
                        view.onResult(bean.getData(), message);
                    }
                }, throwable -> view.onLayoutError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void submitOrder(String url, HashMap<String, Object> map, String message) {
        Api.getInstance().postCallParams(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    if (bean.getStatus() == 0) {
                        view.onResult(bean.getData(), message);
                    }
                }, throwable -> view.onLayoutError(throwable));
    }
}
