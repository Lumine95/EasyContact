package com.yigotone.app.bean;

/**
 * Created by ZMM on 2018/11/27  22:59.
 */
public class AccountBean {
    private int status;
    private String errorMsg;
    private DataBean data;

    public static class DataBean {
        private long talkTime;
        private long allTalkTime;
        private long expirationTime;


        public long getTalkTime() {
            return talkTime;
        }

        public long getAllTalkTime() {
            return allTalkTime;
        }

        public long getExpirationTime() {
            return expirationTime;
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
