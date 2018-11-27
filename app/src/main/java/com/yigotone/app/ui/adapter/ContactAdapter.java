package com.yigotone.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigotone.app.R;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.view.contact.IndexableAdapter;

/**
 * Created by YoKey on 16/10/8.
 */
public class ContactAdapter extends IndexableAdapter<ContactBean> {
    private boolean isEdit;
    private LayoutInflater mInflater;

    public ContactAdapter(Context context, boolean isEdit) {
        mInflater = LayoutInflater.from(context);
        this.isEdit = isEdit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_contact, parent, false);
        return new IndexVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_contact, parent, false);
        return new ContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        IndexVH vh = (IndexVH) holder;
        vh.tv.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, ContactBean entity) {
        ContentVH vh = (ContentVH) holder;
        vh.tvName.setText(entity.getName()+entity.getId());
        vh.tvMobile.setText(entity.getPhone());
        vh.ivSelect.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        vh.ivSelect.setSelected(entity.isSelect);
        vh.ivSelect.setOnClickListener(v -> {
            entity.isSelect = !entity.isSelect;
            notifyDataSetChanged();
        });
    }

    private class IndexVH extends RecyclerView.ViewHolder {
        TextView tv;

        public IndexVH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_index);
        }
    }

    private class ContentVH extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile;
        ImageView ivSelect;

        public ContentVH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobile = itemView.findViewById(R.id.tv_phone);
            ivSelect = itemView.findViewById(R.id.iv_select);
        }
    }
}
