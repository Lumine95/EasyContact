package com.yigotone.app.ui.message;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZMM on 2018/11/14 14:54.
 */
public class MessageContract {
    public interface View extends BaseView {
        void onResult(Object result, String message);

        void onLayoutError(Throwable throwable);
    }

    public interface Presenter extends BasePresenter {
        void sendMessage(String url, Map<String, Object> map, String message);

        void getMessageList(String url, HashMap<String, Object> map, String message);

        void getMessageDetail(String url, HashMap<String, Object> map, String message);
    }
}
