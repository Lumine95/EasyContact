package com.yigotone.app.ui.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CallDetailBean;
import com.yigotone.app.ui.adapter.MessageAdapter;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/8 16:41.
 */
public class MessageDetailActivity extends BaseActivity<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.et_message) EditText etMessage;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private StatusLayoutManager statusLayoutManager;
    private MessageAdapter mAdapter;
    private int count;
    private String messageId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    public MessagePresenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void initView() {
        messageId = getIntent().getStringExtra("messageId");
        new BaseTitleBar(this).setTitleText("短信详情").setLeftIcoListening(v -> finish());
        initRecyclerView();
        getMessageList();
        recyclerView.setAdapter(mAdapter = new MessageAdapter(new ArrayList<>()));

        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        mAdapter.setUpFetchListener(this::startUpFetch);
    }

    private void getMessageList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("messageId", messageId);
        map.put("page", pageIndex);
        map.put("count", pageSize);
        presenter.getMessageDetail(UrlUtil.GET_MESSAGE_DETAIL, map, "getMessageDetail");
    }

    private void startUpFetch() {
        pageIndex++;
        mAdapter.setUpFetching(true);
        getMessageList();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(recyclerView).setOnStatusClickListener(view -> {

        }).build();
        statusLayoutManager.showLoadingLayout();
    }

    @Override
    public void onFinish() {
        statusLayoutManager.showSuccessLayout();
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
    }


    @OnClick(R.id.iv_send)
    public void onViewClicked() {
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getMessageDetail":
                CallDetailBean bean = (CallDetailBean) result;
                List<CallDetailBean.DataBean.InfoBean> data = bean.getData().getInfo();
                //  Collections.reverse(data);
                if (bean.getStatus() == 0) {
                    if (data.size() > 0) {
                        if (pageIndex == 1) {
                            mAdapter.setNewData(data);
                            mAdapter.setUpFetchEnable(true);
                        } else {
                            mAdapter.addData(0, data);
                            mAdapter.setUpFetching(false);
                        }
                        if (data.size() < pageSize) {
                            mAdapter.loadMoreEnd(true);
                        } else {
                            mAdapter.loadMoreComplete();
                            mAdapter.setUpFetchEnable(false);//??
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
}
