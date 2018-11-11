package com.yigotone.app.util;

import android.util.Log;

import com.ebupt.ebjar.util.TimeManagement;

import java.text.SimpleDateFormat;

/**
 * Created by mga on 2017/9/14 16:42.
 */

public class AuthUtils {
    public static String TAG=AuthUtils.class.getSimpleName();
    public static boolean isDeadlineAvailable(String deadline) {
//        /*鉴权有效期判定*/
        if ("".equals(deadline)) {
            Log.d(TAG, "invalid authdeadline!");
            return false;
        }
        long now = System.currentTimeMillis();
        long deadlinetime = TimeManagement.stringToLongDate(deadline, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        Log.d(TAG, "deadlinetime=" + deadlinetime + "___now=" + now);
        if (TimeManagement.compare(deadlinetime, now) == 1) {
            Log.d(TAG, "in deadline date");
            return true;
        } else {
            return false;
        }
    }
}
