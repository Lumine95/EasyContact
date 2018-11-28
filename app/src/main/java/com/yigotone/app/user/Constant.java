package com.yigotone.app.user;

/**
 * Created by M on 2018/7/30.
 */
public class Constant {

    // API_KEY
    public static String API_KEY = "Ij638hd*(#Jfy72f";

    public static String JUSTALK_KEY = "0f622e38d779a01437c95096";
    public static String JUSTALK_IP = "http:router.justalkcloud.com:8080";
    public static String JUSTTALK_PWD = "ebupt";

    public static final int ERROR_PARAM = 1001;      // 参数验证失败
    public static final int ERROR_TOKEN = 1002;      // 客户端Token验证失败
    public static final int ERROR_MYSQL = 1003;      // 数据库操作失败
    public static final int ERROR_NO_USER = 1004;  // 用户不存在
    public static final int ERROR_REPEAT_REG = 1005;  // 重复注册
    public static final int ERROR_CODE = 1006;  // 验证码错误
    public static final int ERROR_PASSWORD = 1007;  // 密码错误
    public static final int ERROR_USER_OPEN = 1008;  // 用户开户失败
    public static final int ERROR_TRUSTEESHIP_AlREADY = 1009;  // 用户已被托管
    public static final int ERROR_TRUSTEESHIP_FAILURE = 1010;  // 托管失败 User phone not shut down
    public static final int ERROR_TRUSTEESHIP_NONE = 1011;  // 用户未被托管
    public static final int ERROR_TRUSTEESHIP_CANCEL = 1012;  // 取消托管失败
    public static final int ERROR_SEND_MESSAGE = 1013;    // 短信发送失败
}
