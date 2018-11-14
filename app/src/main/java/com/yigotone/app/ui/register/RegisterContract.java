package com.yigotone.app.ui.register;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.UserBean;

import java.util.Map;

/**
 * Created by ZMM on 2018/10/26 17:38.
 */
public class RegisterContract {
    public interface View extends BaseView {
        void codeObtained(UserBean bean);

        void onRegisterResult(CodeBean bean);

        void onModifyResult(CodeBean bean);
    }

    public interface Presenter extends BasePresenter {
        void getRandomCode(String url);

        void register(String url, Map<String, Object> map);

        void modifyPassword(String url, Map<String, Object> map);
    }
}
