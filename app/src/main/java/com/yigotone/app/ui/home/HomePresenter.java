package com.yigotone.app.ui.home;

import android.annotation.SuppressLint;

import com.yigotone.app.api.Api;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BasePresenterImpl;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZMM on 2018/10/26 17:40.
 */
public class HomePresenter extends BasePresenterImpl<HomeContract.View> implements HomeContract.Presenter {

    HomePresenter(HomeContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCallRecords(HashMap<String, Object> map) {
        Api.getInstance().getCallRecordList(UrlUtil.GET_CALL_RECORD, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onFinish();
                    view.callRecordsResult(bean.getData());
                }, throwable -> view.onRecyclerViewError(throwable));
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateMobileStatus(Map<String, Object> map) {
        String url = UrlUtil.UPDATE_MOBILE_STATUS;
        Api.getInstance().updateMobileStatus(url, map)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.onMobileStatusResult(bean.getData().getMobileStatus());
                }, throwable -> view.onError(throwable));

    }

    @SuppressLint("CheckResult")
    @Override
    public void getMobileStatus(String url, Map<String, Object> map) {
        Api.getInstance().updateMobileStatus(url, map)
                .subscribeOn(Schedulers.io())
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    view.refreshMobileStatus(bean.getData().getMobileStatus());
                }, throwable -> view.onError(throwable));
    }
}
