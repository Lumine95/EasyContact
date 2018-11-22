package com.yigotone.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMM on 2018/11/22 9:24.
 */
public class OrderBean {
    private int status;
    private String errorMsg;
    private List<DataBean> data;

    public class DataBean implements Serializable {
        private String orderId;
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
