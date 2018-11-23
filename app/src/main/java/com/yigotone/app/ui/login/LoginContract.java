package com.yigotone.app.ui.login;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;
import com.yigotone.app.bean.UserBean;

/**
 * Created by ZMM on 2018/10/26 17:38.
 */
public class LoginContract {
    public interface View extends BaseView {
        void loginSuccess(UserBean bean);

        void loginFail(String errorMsg);
    }

    public interface Presenter extends BasePresenter {
        void login(String mobile, String pwd);

        void autoLogin(String uid, String token);

    }
}
