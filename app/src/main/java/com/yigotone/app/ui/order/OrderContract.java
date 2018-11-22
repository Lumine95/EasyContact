package com.yigotone.app.ui.order;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.Map;

/**
 * Created by ZMM on 2018/11/24 14:54.
 */
public class OrderContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);

        void onLayoutError(Throwable throwable);
    }

    public interface Presenter extends BasePresenter {
        void getOrderList(String url, Map<String, Object> map, String message);
    }
}
