package com.yigotone.app.util;

import android.text.TextUtils;

import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.orhanobut.logger.Logger;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.user.UserManager;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZMM on 2018/10/30 17:11.
 */
public class Utils {

    /**
     * 判断包含大小写字母及数字且在8-16位
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,16}$";
        return isDigit && isLetter && str.matches(regex);
    }

    /**
     * 隐藏手机号码中间部分
     *
     * @param phone
     * @return
     */
    public static String hidePhoneNumber(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 判断手机号是否合法
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        return phone.matches("0?(13|14|15|16|17|18|19)[0-9]{9}");
    }

    public static String getShortTime(long dateline) {
        String shortstring = null;
        String time = timestampToStr(dateline);
        Date date = getDateByString(time);
        if (date == null) return shortstring;

        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
        } else if (deltime > 24 * 60 * 60) {
            shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "分前";
        } else if (deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }

    //Timestamp转化为String:
    private static String timestampToStr(long dateline) {
        Timestamp timestamp = new Timestamp(dateline * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        return df.format(timestamp);
    }

    static Date getDateByString(String time) {
        Date date = null;
        if (time == null)
            return date;
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static boolean isOK;

    public static boolean CMAuthenticate(String phone) {
        EbAuthDelegate.AuthloginByTrust(phone, new OnAuthLoginListener() {

            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Logger.d("authcode " + authcode + deadline);
                if (AuthUtils.isDeadlineAvailable(deadline)) {
                    isOK = true;
                    //  EbLoginDelegate.login(phone, "ebupt");
                }
            }

            @Override
            public void ebAuthFailed(int code, String reason) {
                Logger.d("ebAuthFailed: " + code + reason);
                isOK = false;
            }
        });
        return isOK;
    }

    /**
     * 根据手机号码获取在通讯录中的名字
     *
     * @param phone
     * @return
     */
    public static String getContactName(String phone) {

        for (ContactBean bean : UserManager.getInstance().contactList) {
            if (TextUtils.equals(phone, bean.getPhone())) {
                return bean.getName();
            }
        }
        return phone;
    }

    /**
     * 格式化秒数
     *
     * @param time
     * @return
     */
    public static String formatSecond(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
//                if (hour > 99)
//                    return "99时59分59秒";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    /**
     * 货币分转换为元
     *
     * @param cent
     * @return
     */
    public static String cent2Yuan(String cent) {
        if (TextUtils.isEmpty(cent)) {
            cent = "0";
        }
        float yuan = Float.parseFloat(cent) / 100;
        return new DecimalFormat("0.00").format(yuan);
    }
}
