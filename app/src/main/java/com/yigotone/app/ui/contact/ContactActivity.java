package com.yigotone.app.ui.contact;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.ui.adapter.ContactAdapter;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.contact.IndexableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/21 13:48.
 */
public class ContactActivity extends BaseActivity {
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.indexableLayout) IndexableLayout indexableLayout;

    ArrayList<ContactBean> contactList = new ArrayList<>();
    private ContactAdapter mAdapter;
    private boolean tag;
    private String phoneNumber;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        tag = getIntent().getBooleanExtra("tag", false);
        new BaseTitleBar(this).setTitleText("通讯录").setLeftIcoListening(v -> finish()).setTitleRight("确定").setRightIcoListening(v -> {
            submitSelectedContact();
        });
        initRecyclerView();
        getPhoneContacts();
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ContactAdapter(this, tag);
        indexableLayout.setAdapter(mAdapter);
        mAdapter.setDatas(contactList);
        indexableLayout.setOverlayStyle_Center();
        indexableLayout.setCompareMode(IndexableLayout.MODE_ALL_LETTERS);
        mAdapter.setOnItemContentClickListener((v, originalPosition, currentPosition, entity) -> {
            if (!TextUtils.isEmpty(phoneNumber)) {
                addExistContact(entity);
            }
        });

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

    private void addExistContact(ContactBean entity) {
        com.orhanobut.logger.Logger.d(entity.getId());
        com.orhanobut.logger.Logger.d(entity.getPhone());
        com.orhanobut.logger.Logger.d(entity.getName());
        com.orhanobut.logger.Logger.d(phoneNumber);
        // TODO: 2018/11/26 test
    }

    private void submitSelectedContact() {
        UserManager.getInstance().selectedList.clear();
        for (ContactBean bean : contactList) {
            if (bean.isSelect) {
                UserManager.getInstance().selectedList.add(bean);
            }
        }
        if (UserManager.getInstance().selectedList.size() == 0) {
            U.showToast("还未选中任何联系人");
            return;
        }
        setResult(9527);
        finish();
    }

    public void getPhoneContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "contact_id",
                        "data1"}, null, null, null);
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            ContactBean bean = new ContactBean(name, number.replaceAll("\\s*", ""), Id);
            contactList.add(bean);
        }
        cursor.close();
        //  return list;

        for (ContactBean bean : contactList) {
            if (UserManager.getInstance().selectedList.contains(bean)) {
                bean.isSelect = true;
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
