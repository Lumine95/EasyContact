package com.yigotone.app.bean;

import java.util.List;

/**
 * Created by ZMM on 2018/11/20 16:15.
 */
public class CallDetailBean {
    private int status;
    private String errorMsg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String callednumber;
        private String calledname;
        private List<InfoBean> info;

        public String getCallednumber() {
            return callednumber;
        }

        public void setCallednumber(String callednumber) {
            this.callednumber = callednumber;
        }

        public String getCalledname() {
            return calledname;
        }

        public void setCalledname(String calledname) {
            this.calledname = calledname;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            private String uid;
            private String mobile;
            private String status;
            private String createAt;
            private String duration;
            private int type;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreateAt() {
                return createAt;
            }

            public void setCreateAt(String createAt) {
                this.createAt = createAt;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
