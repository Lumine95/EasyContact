package com.yigotone.app.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.bean.MessageBean;

import java.util.List;

/**
 * Created by ZMM on 2018/11/9 13:59.
 */
public class MessageAdapter extends BaseMultiItemQuickAdapter<MessageBean.DataBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MessageAdapter(List<MessageBean.DataBean> data) {
        super(data);
        addItemType(MessageBean.DataBean.THIS, R.layout.item_message_this);
        addItemType(MessageBean.DataBean.THAT, R.layout.item_message_that);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean.DataBean item) {
        switch (helper.getItemViewType()) {
            case MessageBean.DataBean.THIS:
                helper.setText(R.id.tv_content, item.getContent());
                break;
            case MessageBean.DataBean.THAT:
                helper.setText(R.id.tv_content, item.getContent());
                break;
        }

    }
}
