package com.yigotone.app.bean;

/**
 * Created by ZMM on 2018/11/1 16:35.
 */
public class ContactBean {
    private String name;
    private String phone;
    private String letter;
    private int id;

    public ContactBean(String name, String phone, String letter, int id) {
        this.name = name;
        this.phone = phone;
        this.letter  = letter ;
        this.id = id;
    }

    public String getLetter() {
        return letter == null ? "" : letter;
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
