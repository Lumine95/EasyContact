package com.yigotone.app.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.utils.U;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class MessageFragment extends BaseFragment<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.tv_edit) TextView tvEdit;
    @BindView(R.id.iv_add) ImageView ivAdd;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected MessagePresenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        initRecyclerView();
        EventBus.getDefault().register(this);
        getMessageList(true);
    }

    private void getMessageList(boolean isLoadingLayout) {
        if (isLoadingLayout) {
            statusLayoutManager.showLoadingLayout();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("page", pageIndex);
        map.put("count", pageSize);
        presenter.getMessageList(UrlUtil.GET_MESSAGE_LIST, map, "getMessage");
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(refreshLayout).setOnStatusClickListener(v -> {
            pageIndex = 1;
            getMessageList(true);
        }).build();
        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder>(R.layout.item_message) {
            @Override
            protected void convert(BaseViewHolder helper, MessageBean.DataBean item) {
                helper.setText(R.id.tv_title, item.getContent());
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_date, Utils.getShortTime(Long.parseLong(item.getMessagetime())));
                helper.setOnClickListener(R.id.right, view -> U.showToast(helper.getLayoutPosition() + ""));
                helper.setOnClickListener(R.id.content, view -> startActivity(new Intent(mContext, MessageDetailActivity.class)
                        .putExtra("messageId", item.getMessageId())));
            }
        });
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            startActivity(new Intent(mContext, MessageDetailActivity.class));
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


    @OnClick({R.id.tv_edit, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                break;
            case R.id.iv_add:
                startActivity(new Intent(mContext, NewMessageActivity.class));
                break;
        }
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
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
        refreshLayout.setRefreshing(false);
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        refreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        Logger.d("eventBus: " + event);
        switch (event) {
            case "refreshMessageList":
                pageIndex = 1;
                getMessageList(true);
                break;
        }
    }
}
