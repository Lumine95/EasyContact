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
        private long talkTime;
        private long expirationTime;
        private long allTalkTime;
        private int disturb;  // 是否免打扰 1否 2是

        private String starttime;
        private String endtime;

        public int getDisturb() {
            return disturb;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public long getAllTalkTime() {
            return allTalkTime;
        }

        public String getStarttime() {
            return starttime.equals("0") ? "" : starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime.equals("0") ? "" : endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public void setDisturb(int disturb) {
            this.disturb = disturb;
        }

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
            return talkTime == 0 ? "0分钟" : talkTime / 60 + "分钟";
        }

        public long getTalkTime() {
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
