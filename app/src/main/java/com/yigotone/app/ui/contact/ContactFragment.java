package com.yigotone.app.ui.contact;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/11/1 15:21.
 */
public class ContactFragment extends BaseFragment {
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    Unbinder unbinder;
    List<ContactBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
        getPhoneContacts();

        recyclerView.setAdapter(new BaseQuickAdapter<ContactBean, BaseViewHolder>(R.layout.item_contact,list) {
            @Override
            protected void convert(BaseViewHolder helper, ContactBean item) {
                helper.setText(R.id.tv_name, item.getName() + "|" + item.getId() + "|" + item.getSortKey());
                helper.setText(R.id.tv_phone, item.getPhone());
//                helper.itemView.setOnClickListener(v -> startActivity(new Intent(mContext, WebViewActivity.class)
//                        .putExtra("title", item.getTitle())
//                        .putExtra("url", item.getLink())));
            }
        });

    }

    public void getPhoneContacts() {
        Cursor cursor = mContext.getContentResolver().query(Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "contact_id",
                        "data1"}, null, null, null);
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(Phone.CONTACT_ID));
            String SortKey = getSortKey(cursor.getString(1));
            ContactBean bean = new ContactBean(name, number, SortKey, Id);
            list.add(bean);
        }
        cursor.close();
        //  return list;
    }

    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else
            return "#";
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
