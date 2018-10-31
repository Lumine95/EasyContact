package com.yigotone.app.ui.home;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class HomeContract {
    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter {
        void getPackageList();
    }
}
