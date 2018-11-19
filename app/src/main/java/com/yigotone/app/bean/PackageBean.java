package com.yigotone.app.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZMM on 2018/10/31 11:56.
 */
public class PackageBean {

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

        private String notes;
        @SerializedName("package") private List<PackageEntity> packageX;

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public List<PackageEntity> getPackageX() {
            return packageX;
        }

        public void setPackageX(List<PackageEntity> packageX) {
            this.packageX = packageX;
        }

        public static class PackageEntity  implements Serializable {
            private String packageId;
            private String packageName;
            private String iosIapId;
            private String price;
            private String intro;

            public String getIntro() {
                return intro == null ? "" : intro;
            }

            public String getPackageId() {
                return packageId;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getIosIapId() {
                return iosIapId;
            }

            public void setIosIapId(String iosIapId) {
                this.iosIapId = iosIapId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
