package com.yigotone.app.ui.home;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.Map;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class HomeContract {
    public interface View extends BaseView {
        void onMobileStatusResult(String status);
    }

    public interface Presenter extends BasePresenter {
        void getPackageList();

        void updateMobileStatus(Map<String, Object> map);
    }
}
