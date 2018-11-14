package com.yigotone.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ZMM on 2018/11/9 14:00.
 */
public class MessageDataBean implements MultiItemEntity {
    public static final int THIS = 1;
    public static final int THAT = 2;
    private int itemType;

    private String content;

    public MessageDataBean(int itemType, String content) {
        this.itemType = itemType;
        this.content = content;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
