package com.yigotone.app.api;

import com.android.library.utils.U;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.yigotone.app.bean.Result;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final int ERROR_PARAM = 1001;      // 参数验证失败
    private static final int ERROR_TOKEN = 1002;      // 客户端Token验证失败
    private static final int ERROR_MYSQL = 1003;      // 数据库操作失败
    private static final int ERROR_NO_USER = 1004;  // 用户不存在
    private static final int ERROR_REPEAT_REG = 1005;  // 重复注册
    private static final int ERROR_CODE = 1006;  // 验证码错误
    private static final int ERROR_PASSWORD = 1007;  // 密码错误
    private static final int ERROR_USER_OPEN = 1008;  // 用户开户失败
    private static final int ERROR_TRUSTEESHIP_AlREADY = 1009;  // 用户已被托管
    private static final int ERROR_TRUSTEESHIP_FAILURE = 1010;  // 托管失败 User phone not shut down
    private static final int ERROR_TRUSTEESHIP_NONE = 1011;  // 用户未被托管
    private static final int ERROR_TRUSTEESHIP_CANCEL = 1012;  // 取消托管失败

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    ResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            verify(json);
            return adapter.read(gson.newJsonReader(new StringReader(json)));
        } finally {
            value.close();
        }
    }

    private void verify(String json) {
        Result result = gson.fromJson(json, Result.class);
        if (result.status != 0) {
            switch (result.status) {
                case ERROR_PARAM:
                    U.showToast("参数验证失败");
                    break;
                case ERROR_MYSQL:
                    U.showToast("数据库操作失败");
                    break;
                case ERROR_NO_USER:
                    U.showToast("用户不存在");
                    break;
                case ERROR_REPEAT_REG:
                    U.showToast("重复注册");
                    break;
                case ERROR_CODE:
                    U.showToast("验证码错误");
                    break;
                case ERROR_PASSWORD:
                    U.showToast("密码错误");
                    break;
                case ERROR_TOKEN:
                    U.showToast("账号异地登录,请重新登录");
                    // TODO: 2018/10/31 Token Invalid
                    break;
                case ERROR_USER_OPEN:
                    U.showToast("开户失败");
                    break;
                case ERROR_TRUSTEESHIP_AlREADY:
                    U.showToast("用户已被托管");
                    break;
                case ERROR_TRUSTEESHIP_FAILURE:
                    EventBus.getDefault().post("mobileNotShutDown");
                    break;
                case ERROR_TRUSTEESHIP_NONE:
                    U.showToast("用户未被托管");
                    break;
                case ERROR_TRUSTEESHIP_CANCEL:
                    U.showToast("取消托管失败");
                    break;
            }
        }
    }
}
