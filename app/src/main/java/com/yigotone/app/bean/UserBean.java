package com.yigotone.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/10/26 17:31.
 */
public class UserBean {
    private int status;
    private String errorMsg;
    private List<DataBean> data;

    public static class DataBean {
        private String code;

        public String getCode() {
            return code == null ? "" : code;
        }
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }
}
