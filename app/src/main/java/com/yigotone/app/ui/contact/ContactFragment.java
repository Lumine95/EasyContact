package com.yigotone.app.ui.contact;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.ui.adapter.ContactAdapter;
import com.yigotone.app.view.contact.IndexableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/1 15:21.
 */
public class ContactFragment extends BaseFragment {
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.indexableLayout) IndexableLayout indexableLayout;

    ArrayList<ContactBean> contactList = new ArrayList<>();
    private ContactAdapter mAdapter;

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
//        recyclerView.setAdapter(new BaseQuickAdapter<ContactBean, BaseViewHolder>(R.layout.item_contact, contactList) {
//            @Override
//            protected void convert(BaseViewHolder helper, ContactBean item) {
//                helper.setText(R.id.tv_name, item.getName() + "|" + item.getId()  );
//                helper.setText(R.id.tv_phone, item.getPhone());
////                helper.itemView.setOnClickListener(v -> startActivity(new Intent(mContext, WebViewActivity.class)
////                        .putExtra("title",    item.getTitle())
////                        .putExtra("url", item.getLink())));
//            }
//        });
        indexableLayout.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ContactAdapter(mContext);
        indexableLayout.setAdapter(mAdapter);
        mAdapter.setDatas(contactList);
        indexableLayout.setOverlayStyle_Center();
        indexableLayout.setCompareMode(IndexableLayout.MODE_ALL_LETTERS);
        mAdapter.setOnItemContentClickListener((v, originalPosition, currentPosition, entity) -> startActivity(new Intent(mContext, ContactDetailActivity.class).putExtra("data", entity)));

        //        etSearch.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
//                    searchContacts(etSearch.getText().toString().trim());
//                }
//                return true;
//            }
//            return false;
//        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    mAdapter.setDatas(contactList);
                } else {
                    searchContacts(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
            ContactBean bean = new ContactBean(name, number.replaceAll("\\s*", ""), Id);
            contactList.add(bean);
        }
        cursor.close();
        //  return list;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }


    public void searchContacts(String keyword) {
        List<ContactBean> list = new ArrayList<>();
        for (ContactBean bean : contactList) {
            if (isPhoneNum(keyword)) {
                if (bean.getPhone().contains(keyword)) {
                    list.add(bean);
                }
            } else {
                if (bean.getName().contains(keyword)) {
                    list.add(bean);
                }
            }
        }
        mAdapter.setDatas(list);
    }

    private boolean isPhoneNum(String keyword) {
        // 正则 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码
        if (keyword.matches("^([0-9]|[/+]).*")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
