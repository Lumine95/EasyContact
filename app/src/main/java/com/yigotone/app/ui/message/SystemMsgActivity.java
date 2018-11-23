package com.yigotone.app.ui.message;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.library.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/23 10:41.
 */
public class SystemMsgActivity extends BaseActivity<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message;
    }

    @Override
    public MessageContract.Presenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("系统消息").setLeftIcoListening(v -> finish());
        initRecyclerView();
        getMessageList(true);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(refreshLayout).setOnStatusClickListener(v -> {
            pageIndex = 1;
            getMessageList(true);
        }).build();
        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder>(R.layout.item_system_message) {
            @Override
            protected void convert(BaseViewHolder helper, MessageBean.DataBean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_content, item.getContent());
                helper.setText(R.id.tv_date, DateUtil.getDate(Long.valueOf(item.getCreateAt()), "yyyy/MM/dd HH:mm"));
            }
        });
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            startActivity(new Intent(this, SystemMsgDetailActivity.class)
                    .putExtra("data", (MessageBean.DataBean) adapter.getData().get(position)));
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            getMessageList(false);
        }, recyclerView);
        mAdapter.disableLoadMoreIfNotFullPage();
        refreshLayout.setOnRefreshListener(() -> {
            pageIndex = 1;
            getMessageList(false);
        });
    }

    private void getMessageList(boolean isLoadingLayout) {
        if (isLoadingLayout) {
            statusLayoutManager.showLoadingLayout();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", pageIndex);
        map.put("count", pageSize);
        presenter.getMessageList(UrlUtil.GET_SYSTEM_MESSAGE, map, "getMessage");
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getMessage":
                MessageBean bean = (MessageBean) result;
                List<MessageBean.DataBean> data = bean.getData();
                if (bean.getStatus() == 0) {
                    if (data.size() > 0) {
                        if (pageIndex == 1) {
                            mAdapter.setNewData(data);
                        } else {
                            mAdapter.addData(data);
                        }
                        if (data.size() < pageSize) {
                            mAdapter.loadMoreEnd(true);
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    } else {
                        if (pageIndex == 1) {
                            statusLayoutManager.showEmptyLayout();
                        } else {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onLayoutError(Throwable throwable) {
        statusLayoutManager.showErrorLayout();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFinish() {
        statusLayoutManager.showSuccessLayout();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        refreshLayout.setRefreshing(false);
    }
}
