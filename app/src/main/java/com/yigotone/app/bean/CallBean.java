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
        private String othermobile;
        private String status;
        private String targetStatus;
        private String createAt;
        private String duration;
        private String callId;
        private String targetName;
        private int type;
        private int callNum;

        public int getCallNum() {
            return callNum;
        }

        public String getOthermobile() {
            return othermobile == null ? "" : othermobile;
        }

        public String getStatus() {
            return status == null ? "" : status;
        }

        public String getTargetStatus() {
            return targetStatus == null ? "" : targetStatus;
        }

        public String getCreateAt() {
            return createAt == null ? "" : createAt;
        }

        public String getDuration() {
            return duration == null ? "" : duration;
        }

        public String getCallId() {
            return callId == null ? "" : callId;
        }

        public String getTargetName() {
            return targetName == null ? "" : targetName;
        }

        public int getType() {
            return type;
        }

        public String getMobile() {
            return othermobile == null ? "" : othermobile;
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
