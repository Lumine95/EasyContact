package com.yigotone.app.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.bean.CallDetailBean;

import java.util.List;

/**
 * Created by ZMM on 2018/11/9 13:59.
 */
public class MessageAdapter extends BaseMultiItemQuickAdapter<CallDetailBean.DataBean.InfoBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MessageAdapter(List<CallDetailBean.DataBean.InfoBean> data) {
        super(data);
        addItemType(CallDetailBean.DataBean.InfoBean.THIS, R.layout.item_message_this);
        addItemType(CallDetailBean.DataBean.InfoBean.THAT, R.layout.item_message_that);
    }

    @Override
    protected void convert(BaseViewHolder helper, CallDetailBean.DataBean.InfoBean item) {
        switch (helper.getItemViewType()) {
            case CallDetailBean.DataBean.InfoBean.THIS:
                helper.setText(R.id.tv_content, item.getContent());
                break;
            case CallDetailBean.DataBean.InfoBean.THAT:
                helper.setText(R.id.tv_content, item.getContent());
                break;
        }

    }
}
