package com.yigotone.app.application;

import android.app.Application;
import android.content.Context;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;

/**
 * Created by ZMM on 2018/2/5.
 */

public class MyApplication extends Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        U.init(this);
        EbAuthDelegate.init(this);
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
