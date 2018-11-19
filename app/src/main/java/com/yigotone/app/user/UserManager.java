package com.yigotone.app.user;

import android.content.Context;

import com.android.library.utils.U;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/10/31 11:08.
 */
public class UserManager {
    private static UserManager mUserManager;
    public UserBean.DataBean userData;
    public  List<ContactBean> contactList = new ArrayList<>();

    public static UserManager getInstance() {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
        return mUserManager;
    }

    /**
     * 存储用户数据
     *
     * @param context
     * @param user
     */
    public void save(Context context, UserBean.DataBean user) {
        U.savePreferences("uid", user.getUid());
        U.savePreferences("token", U.MD5(user.getToken() + "_" + Constant.API_KEY));
        userData = user;
    }

}
