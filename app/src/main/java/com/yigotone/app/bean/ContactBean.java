package com.yigotone.app.bean;

/**
 * Created by ZMM on 2018/11/1 16:35.
 */
public class ContactBean {
    private String name;
    private String phone;
    private String sortKey;
    private int id;

    public ContactBean(String name, String phone, String sortKey, int id) {
        this.name = name;
        this.phone = phone;
        this.sortKey = sortKey;
        this.id = id;
    }

    public String getSortKey() {
        return sortKey == null ? "" : sortKey;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }
}
