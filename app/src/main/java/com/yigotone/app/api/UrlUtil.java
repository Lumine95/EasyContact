package com.yigotone.app.api;

/**
 * Created by ZMM on 2018/10/29  13:50.
 */
public class UrlUtil {
    private static String BASE_URL = "http://yigoutong.1bu2bu.com/index.php?s=/Api/";

    public static String GET_RANDOM_CODE = BASE_URL + "Login/randomCode/";
    public static String REGISTER_NEW_USER = BASE_URL + "Login/register";
    public static String LOGIN = BASE_URL + "Login/login";
    public static String GET_PACKAGE_LIST = BASE_URL + "Call/getCallSetmeal";
    public static String UPDATE_MOBILE_STATUS = BASE_URL + "Login/trusteeship";
}
