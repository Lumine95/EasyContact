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
        private String uid;
        private String token;
        private String mobile;
        private String mobileStatus;
        private int talkTime;

        public String getUid() {
            return uid == null ? "" : uid;
        }

        public void setMobileStatus(String mobileStatus) {
            this.mobileStatus = mobileStatus;
        }

        public String getToken() {
            return token == null ? "" : token;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public String getMobileStatus() {
            return mobileStatus == null ? "" : mobileStatus;
        }

        public String getTalkTimeText() {
            return talkTime == 0 ? "0分钟" : talkTime + "分钟";
        }

        public int getTalkTime() {
            return talkTime;
        }

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
