package com.yigotone.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/11/14 17:41.
 */
public class MessageBean {
    private int status;
    private String errorMsg;
    private List<MessageDataBean> data;

    public int getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public List<MessageDataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }
}
