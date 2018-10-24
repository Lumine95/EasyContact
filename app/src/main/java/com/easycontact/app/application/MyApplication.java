package com.easycontact.app.application;

import android.app.Application;

import com.android.library.utils.U;

/**
 * Created by ZMM on 2018/2/5.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        U.init(this);
    }
}
