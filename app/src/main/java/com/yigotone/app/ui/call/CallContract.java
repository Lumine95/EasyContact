package com.yigotone.app.ui.call;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.Map;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class CallContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);
    }

    public interface Presenter extends BasePresenter {

        void postParams(String url, Map<String, Object> map, String message);
    }
}
