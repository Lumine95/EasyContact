package com.yigotone.app.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebjar.EbDelegate;
import com.yigotone.app.ui.activity.MainActivity;
import com.yigotone.app.ui.call.CallActivity;

/**
 * Created by ZMM on 2018/2/5.
 */

public class MyApplication extends Application {
    private static Context mAppContext;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver mOfflineReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        U.init(this);
        if (EbDelegate.init(this, CallActivity.class) == EbDelegate.InitStat.Meb_INIT_SUCCESS &&
                EbAuthDelegate.init(this) == EbAuthDelegate.InitStat.Meb_INIT_SUCCESS) {
            Log.i("EbDelegate ", "sdk init ok");
            //注册一个鉴权下线广播
            broadcastManager = LocalBroadcastManager.getInstance(this);
            if (mOfflineReceiver == null) {
                mOfflineReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        Intent tent = new Intent(MyApplication.this, MainActivity.class);
                        tent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(tent);
                    }
                };
            }
            broadcastManager.registerReceiver(mOfflineReceiver, new IntentFilter("mebofflinenotification"));
        } else {
            Log.i("EbDelegate ","sdk init fail");
            return;
        }
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
