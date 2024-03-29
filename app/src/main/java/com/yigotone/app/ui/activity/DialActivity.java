package com.yigotone.app.ui.activity;

import android.database.ContentObserver;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.ui.adapter.CommonFragmentAdapter;
import com.yigotone.app.ui.contact.ContactFragment;
import com.yigotone.app.ui.fragment.DialFragment;
import com.yigotone.app.view.BaseTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/10/26 10:12.
 */
public class DialActivity extends BaseActivity {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private CommonFragmentAdapter fragmentAdapter;

    private ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {//这里就是联系人变化的相关操作，根据自己的 逻辑来处理
            EventBus.getDefault().post("SystemContactChanged");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dial;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mObserver);
        new BaseTitleBar(this).setTitleText("拨号盘").setLeftIcoListening(v -> finish());
        titleList.add("拨号盘");
        titleList.add("联系人");
        fragmentList.add(new DialFragment());
        fragmentList.add(new ContactFragment());

        if (fragmentAdapter == null) {
            fragmentAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        } else {
            fragmentAdapter.setFragments(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(fragmentAdapter);

        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
