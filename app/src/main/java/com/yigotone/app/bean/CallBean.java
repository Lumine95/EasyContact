package com.yigotone.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/11/13 16:00.
 */
public class CallBean {
    private int status;
    private String errorMsg;
    private List<DataBean> data;

    public static class DataBean {
        private String mobile;
        private String status;
        private String createAt;
        private String duration;

        public String getMobile() {
            return mobile == null ? "" : mobile;
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
