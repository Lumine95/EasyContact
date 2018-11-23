package com.yigotone.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/11/14 17:41.
 */
public class MessageBean {
    private int status;
    private String errorMsg;
    private List<DataBean> data;

    public static class DataBean implements MultiItemEntity,Serializable {
        public static final int THIS = 1;
        public static final int THAT = 2;
        public boolean isSelect = false;
        private int itemType;
        private int isread;

        private String content;
        private String name;
        private String mobile;
        private String messageId;
        private String messagetime;
        private String title;
        private String createAt;

        public DataBean(int itemType, String content) {
            this.itemType = itemType;
            this.content = content;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public String getCreateAt() {
            return createAt == null ? "" : createAt;
        }

        public String getContent() {
            return content == null ? "" : content;
        }

        public int getIsread() {
            return isread;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public String getMessageId() {
            return messageId == null ? "" : messageId;
        }

        public String getMessagetime() {
            return messagetime == null ? "" : messagetime;
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
