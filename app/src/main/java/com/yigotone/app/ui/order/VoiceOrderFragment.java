package com.yigotone.app.ui.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/2 15:07.
 */
public class VoiceOrderFragment extends BaseFragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.recycler_view;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            strings.add("订单 " + i);
        }
        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_voice_order, strings) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_title, item);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        statusLayoutManager = new StatusLayoutManager.Builder(recyclerView).build();
        //   statusLayoutManager.showLoadingLayout();
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
