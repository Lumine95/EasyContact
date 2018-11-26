package com.yigotone.app.ui.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.bean.OrderBean;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/2 15:07.
 */
public class VoiceOrderFragment extends BaseFragment<OrderContract.Presenter> implements OrderContract.View {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<OrderBean.DataBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.recycler_view;
    }

    @Override
    protected OrderPresenter initPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
        getOrderList();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        statusLayoutManager = new StatusLayoutManager.Builder(refreshLayout).setOnStatusClickListener(v -> getOrderList()).build();
        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<OrderBean.DataBean, BaseViewHolder>(R.layout.item_voice_order) {
            @Override
            protected void convert(BaseViewHolder helper, OrderBean.DataBean item) {
                helper.setText(R.id.tv_title, item.toString());
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        refreshLayout.setOnRefreshListener(this::getOrderList);
    }

    private void getOrderList() {
        statusLayoutManager.showLoadingLayout();
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("type", 1);
        presenter.getOrderList(UrlUtil.GET_MY_ORDER, map, "getOrderList");
    }

    @Override
    public void onFinish() {
        statusLayoutManager.showSuccessLayout();
        dismissLoadingDialog();  refreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getOrderList":
                OrderBean bean = (OrderBean) result;
                if (bean.getData().size() > 0) {
                    mAdapter.setNewData(bean.getData());
                } else {
                    statusLayoutManager.showEmptyLayout();
                }
                break;
        }
    }

    @Override
    public void onLayoutError(Throwable throwable) {
        statusLayoutManager.showErrorLayout();
        dismissLoadingDialog();
    }
}
