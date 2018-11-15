package com.yigotone.app.ui.packages;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class PackageContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);

        void onLayoutError(Throwable throwable);
    }

    public interface Presenter extends BasePresenter {
        void getPackageList(String message);
    }
}
