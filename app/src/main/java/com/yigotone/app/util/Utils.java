package com.yigotone.app.util;

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
}
