package com.easycontact.app.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseActivity;
import com.easycontact.app.base.BasePresenter;
import com.easycontact.app.ui.adapter.CommonFragmentAdapter;
import com.easycontact.app.ui.fragment.DialFragment;
import com.easycontact.app.ui.fragment.MessageFragment;
import com.easycontact.app.view.BaseTitleBar;

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
        new BaseTitleBar(this).setTitleText("拨号盘").setLeftIcoListening(v -> finish());
        titleList.add("拨号盘");
        titleList.add("联系人");
        fragmentList.add(new DialFragment());
        fragmentList.add(new MessageFragment());

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
}