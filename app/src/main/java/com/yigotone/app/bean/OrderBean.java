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
        private int orderType;
        private String orderTime;
        private String packageId;
        private String packageName;
        private String price;
        private int packageType;  // 语音套餐类型 1、购买 2、注册赠送 3、邀请赠送

        public String getOrderId() {
            return orderId == null ? "" : orderId;
        }

        public int getOrderType() {
            return orderType;
        }

        public String getOrderTime() {
            return orderTime == null ? "" : orderTime;
        }

        public String getPackageId() {
            return packageId == null ? "" : packageId;
        }

        public String getPackageName() {
            return packageName == null ? "" : packageName;
        }

        public String getPrice() {
            return price == null ? "" : price;
        }

        public int getPackageType() {
            return packageType;
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
