package com.yigotone.app.ui.contact;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.library.utils.U;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.ui.adapter.ContactAdapter;
import com.yigotone.app.util.PinyinComparator;
import com.yigotone.app.util.PinyinUtils;
import com.yigotone.app.view.contact.IndexableAdapter;
import com.yigotone.app.view.contact.IndexableLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/11/1 15:21.
 */
public class ContactFragment extends BaseFragment {
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.indexableLayout) IndexableLayout indexableLayout;
    Unbinder unbinder;
    ArrayList<ContactBean> list = new ArrayList<>();

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

        // 根据a-z进行排序源数据
        PinyinComparator pinyinComparator = new PinyinComparator();
        Collections.sort(list, pinyinComparator);

        recyclerView.setAdapter(new BaseQuickAdapter<ContactBean, BaseViewHolder>(R.layout.item_contact, list) {
            @Override
            protected void convert(BaseViewHolder helper, ContactBean item) {
                helper.setText(R.id.tv_name, item.getName() + "|" + item.getId() + "|" + item.getLetter());
                helper.setText(R.id.tv_phone, item.getPhone());
//                helper.itemView.setOnClickListener(v -> startActivity(new Intent(mContext, WebViewActivity.class)
//                        .putExtra("title",    item.getTitle())
//                        .putExtra("url", item.getLink())));
            }
        });

        indexableLayout.setLayoutManager(new LinearLayoutManager(mContext));
        ContactAdapter mAdapter = new ContactAdapter(mContext);
        indexableLayout.setAdapter(mAdapter);
        mAdapter.setDatas(list);
        indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        indexableLayout.setCompareMode(IndexableLayout.MODE_ALL_LETTERS);
        mAdapter.setOnItemTitleClickListener(new IndexableAdapter.OnItemTitleClickListener() {
            @Override
            public void onItemClick(View v, int currentPosition, String indexTitle) {
                U.showToast("选中:" + indexTitle + "  当前位置:" + currentPosition);
            }
        });

    }

    public void getPhoneContacts() {
        // TODO: 2018/11/3  联系人详情多条电话

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
            String letter = getLetter(name);
            ContactBean bean = new ContactBean(name, number, letter, Id);
            list.add(bean);
        }
        cursor.close();
        //  return list;
    }

    private String getLetter(String name) {
        String key = PinyinUtils.getPingYin(name).substring(0, 1).toUpperCase();
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
