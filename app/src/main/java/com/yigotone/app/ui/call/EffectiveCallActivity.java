package com.yigotone.app.ui.call;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.library.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CallBean;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/26 17:08.
 */
public class EffectiveCallActivity extends BaseActivity<CallContract.Presenter> implements CallContract.View {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<CallBean.DataBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message;
    }

    @Override
    public CallContract.Presenter initPresenter() {
        return new CallPresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("通话详情").setLeftIcoListening(v -> finish());
        initRecyclerView();
        getCallList(true);
    }

    private void getCallList(boolean isLoadingLayout) {
        if (isLoadingLayout) {
            statusLayoutManager.showLoadingLayout();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
//        map.put("page", pageIndex);
//        map.put("count", pageSize);
        presenter.postCallParams(UrlUtil.GET_EFFECTIVE_CALL_RECORD, map, "getCallList");
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(refreshLayout).setOnStatusClickListener(v -> {
            pageIndex = 1;
            getCallList(true);
        }).build();
        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<CallBean.DataBean, BaseViewHolder>(R.layout.item_effective_call_record) {
            @Override
            protected void convert(BaseViewHolder helper, CallBean.DataBean item) {
                helper.setText(R.id.tv_phone, item.getMobile());
                helper.setText(R.id.tv_duration, Utils.formatSecond(Integer.parseInt(item.getDuration())));
                helper.setText(R.id.tv_time, DateUtil.getDate(Long.valueOf(item.getCreateAt()), "yyyy/MM/dd HH:mm"));
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        refreshLayout.setOnRefreshListener(() -> {
            pageIndex = 1;
            getCallList(false);
        });
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getCallList":
                CallBean bean = (CallBean) result;
                if (bean.getData().size() > 0) {
                    mAdapter.setNewData(bean.getData());
                } else {
                    statusLayoutManager.showEmptyLayout();
                }
                break;
        }
    }

    @Override
    public void onFinish() {
        statusLayoutManager.showSuccessLayout();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable) {
        refreshLayout.setRefreshing(false);
        statusLayoutManager.showErrorLayout();
    }
}
