package com.easycontact.app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ZMM on 2017/12/22.
 */

public class CommonFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void setFragments(FragmentManager fm, List<Fragment> fragments) {
        if (fragmentList != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragmentList) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }
}
