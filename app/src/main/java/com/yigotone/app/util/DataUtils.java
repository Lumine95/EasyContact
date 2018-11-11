package com.yigotone.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mga on 2017/9/14 16:58.
 */

public class DataUtils {
    private static String TABLE_NAME="ebsdk_init_info";

    public static String readAccount(Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        return sp.getString("account", "");
    }

    public static void saveAccount(String number, Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("account", number).commit();
    }
    public static String readDeadline(Context mContext, String number) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        return sp.getString(number+"deadline", "");
    }

    public static void saveDeadline(String number, String deadline, Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(number+"deadline", deadline).commit();
    }

}
