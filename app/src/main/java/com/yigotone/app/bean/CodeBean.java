package com.yigotone.app.bean;

/**
 * Created by ZMM on 2018/11/9 17:47.
 */
public class CodeBean {
    private int status;
    private String errorMsg;

    public int getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }
}
