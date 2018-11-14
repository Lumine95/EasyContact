package com.yigotone.app.ui.message;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.Map;

/**
 * Created by ZMM on 2018/11/14 14:54.
 */
public class MessageContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);
    }

    public interface Presenter extends BasePresenter {
        void sendMessage(String url, Map<String, Object> map, String message);
    }
}
