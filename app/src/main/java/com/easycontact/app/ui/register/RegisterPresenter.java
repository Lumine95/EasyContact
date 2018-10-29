package com.easycontact.app.ui.register;

import android.annotation.SuppressLint;
import android.util.Log;

import com.easycontact.app.api.Api;
import com.easycontact.app.api.UrlUtil;
import com.easycontact.app.base.BasePresenterImpl;

import java.util.HashMap;

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
    public void getRandomCode(HashMap<String, Object> map) {
        Api.getInstance().getRandomCode(UrlUtil.GET_RANDOM_CODE, map)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .map(bean -> bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String code = bean.getCode();
                    Log.d("test-----", code);
                }, throwable -> view.onError(throwable));
    }
}
