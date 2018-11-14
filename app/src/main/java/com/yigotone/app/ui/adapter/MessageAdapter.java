package com.yigotone.app.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.bean.MessageDataBean;

import java.util.List;

/**
 * Created by ZMM on 2018/11/9 13:59.
 */
public class MessageAdapter extends BaseMultiItemQuickAdapter<MessageDataBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MessageAdapter(List<MessageDataBean> data) {
        super(data);
        addItemType(MessageDataBean.THIS, R.layout.item_message_this);
        addItemType(MessageDataBean.THAT, R.layout.item_message_that);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageDataBean item) {
        switch (helper.getItemViewType()) {
            case MessageDataBean.THIS:
                helper.setText(R.id.tv_content, item.getContent());
                break;
            case MessageDataBean.THAT:
                helper.setText(R.id.tv_content, item.getContent());
                break;
        }

    }
}
