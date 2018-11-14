package com.yigotone.app.api;

/**
 * Created by ZMM on 2018/10/29  13:50.
 */
public class UrlUtil {
    private static String BASE_URL = "http://yigoutong.1bu2bu.com/index.php?s=/Api/";
    // 获取验证码
    public static String GET_RANDOM_CODE = BASE_URL + "Login/randomCode/";
    // 新用户注册
    public static String REGISTER_NEW_USER = BASE_URL + "Login/register";
    // 登录
    public static String LOGIN = BASE_URL + "Login/login";
    // 自动登录
    public static String AUTO_LOGIN = BASE_URL + "Login/autoLogin";
    // 密码重置
    public static String MODIFY_PWD = BASE_URL + "Login/resetPassword";
    // 获取手机套餐列表
    public static String GET_PACKAGE_LIST = BASE_URL + "Call/getCallSetmeal";
    // 获取通话列表
    public static String GET_CALL_RECORD = BASE_URL + "Call/getCallRecord";
    // 更新手机托管状态
    public static String UPDATE_MOBILE_STATUS = BASE_URL + "Login/trusteeship";
    // 获取手机号码托管状态
    public static String GET_MOBILE_STATUS = BASE_URL + "Login/looktrusteeship";
    // 发送信息
    public static String SEND_MESSAGE = BASE_URL + "Call/sendmessage";
    // 获取信息列表
    public static String GET_MESSAGE_LIST = BASE_URL + "Personal/getMyMessage";
    // 获取信息详情
    public static String GET_MESSAGE_DETAIL = BASE_URL + "Personal/getMyMessageInfo";
}
