package com.yigotone.app.ui.setting;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class SettingContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);
    }

    public interface Presenter extends BasePresenter {
        void setDisturbStatus(String url, HashMap<String, Object> map, String message);

        void logout(String url, Map<String, Object> map, String message);

    }
}
