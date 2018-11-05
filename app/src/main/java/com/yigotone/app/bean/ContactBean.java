package com.yigotone.app.bean;

import com.yigotone.app.view.contact.IndexableEntity;

import java.io.Serializable;

/**
 * Created by ZMM on 2018/11/1 16:35.
 */
public class ContactBean implements IndexableEntity, Serializable {
    private String name;
    private String phone;
    private String letter;
    private String pinyin;
    private int id;

    public ContactBean(String name, String phone, String letter, int id) {
        this.name = name;
        this.phone = phone;
        this.letter = letter;
        this.id = id;
    }

    public String getLetter() {
        return letter == null ? "" : letter;
    }

    public String getPinyin() {
        return pinyin == null ? "" : pinyin;
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

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        this.pinyin = pinyin;
    }
}
