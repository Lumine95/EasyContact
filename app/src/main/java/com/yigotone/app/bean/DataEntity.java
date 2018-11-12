package com.yigotone.app.bean;

/**
 * Created by ZMM on 2018/11/12 14:55.
 */
public class DataEntity {
    private int status;
    private String errorMsg;
    private DataBean data;

    public class DataBean {
        private String mobileStatus;

        public String getMobileStatus() {
            return mobileStatus == null ? "" : mobileStatus;
        }
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public DataBean getData() {
        return data;
    }
}
