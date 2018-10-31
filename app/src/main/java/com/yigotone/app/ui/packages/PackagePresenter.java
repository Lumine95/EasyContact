package com.yigotone.app.ui.packages;

import com.yigotone.app.base.BasePresenterImpl;

/**
 * Created by ZMM on 2018/10/26 17:40.
 */
public class PackagePresenter extends BasePresenterImpl<PackageContract.View> implements PackageContract.Presenter {

    public PackagePresenter(PackageContract.View view) {
        super(view);
    }

//    @SuppressLint("CheckResult")
//    @Override
//    public void getPackageList() {
//        Api.getInstance().getPackageList(UrlUtil.GET_PACKAGE_LIST)
//                .subscribeOn(Schedulers.io())
//                .map(bean -> bean)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bean -> {
//
//                }, throwable -> view.onError(throwable));
//    }
}
