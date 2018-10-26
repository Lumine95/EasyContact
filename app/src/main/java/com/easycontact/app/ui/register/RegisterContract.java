package com.easycontact.app.ui.register;

import com.easycontact.app.base.BasePresenter;
import com.easycontact.app.base.BaseView;

import java.util.HashMap;

/**
 * Created by ZMM on 2018/10/26 17:38.
 */
public class RegisterContract {
    public interface View extends BaseView {
    }

    public interface Presenter extends BasePresenter {
        void  getRandomCode(HashMap<String, Object> map);
    }
}
