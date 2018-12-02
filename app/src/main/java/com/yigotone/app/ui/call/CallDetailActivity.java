package com.yigotone.app.ui.call;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.library.utils.DateUtil;
import com.android.library.view.CircleImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CallBean;
import com.yigotone.app.bean.CallDetailBean;
import com.yigotone.app.ui.message.MessageDetailActivity;
import com.yigotone.app.ui.message.NewMessageActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/20 15:33.
 */
public class CallDetailActivity extends BaseActivity<CallContract.Presenter> implements CallContract.View {

    @BindView(R.id.iv_avatar) CircleImageView ivAvatar;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private CallBean.DataBean data;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<CallDetailBean.DataBean.InfoBean, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_detail;
    }

    @Override
    public CallContract.Presenter initPresenter() {
        return new CallPresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("通话详情").setLeftIcoListening(v -> finish());
        data = (CallBean.DataBean) getIntent().getSerializableExtra("data");
        tvPhone.setText(data.getMobile());
        tvName.setText(data.getTargetName());
        initRecyclerView();
        getCallDetail();
    }

    private void getCallDetail() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("page", pageIndex);
        map.put("count", pageSize);
        map.put("targetMobile", data.getMobile());
        presenter.getCallDetail(UrlUtil.GET_CALL_RECORD_DETAIL, map, "getCallDetail");
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(recyclerView).setOnStatusClickListener(view -> {
            pageIndex = 1;
        }).build();
        statusLayoutManager.showLoadingLayout();

        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<CallDetailBean.DataBean.InfoBean, BaseViewHolder>(R.layout.item_call_detail) {
            @Override
            protected void convert(BaseViewHolder helper, CallDetailBean.DataBean.InfoBean item) {
                helper.setText(R.id.tv_time, DateUtil.getDate(Long.valueOf(item.getCreateAt()), "yyyy/MM/dd HH:mm"));
                helper.setText(R.id.tv_duration, Utils.formatSecond(Integer.parseInt(item.getDuration())));
                String status;
                if (item.getType() == 1) {  // 呼出
                    if (item.getStatus().equals("2")) {
                        status = "已拒接";
                    } else {
                        status = "呼出电话";
                    }
                } else { // 呼入
                    if (item.getStatus().equals("1")) {
                        status = "未接来电";
                    }
                    if (item.getStatus().equals("1")) {
                        status = "已拒接";
                    } else {
                        status = "呼入电话";
                    }
                }
                helper.setText(R.id.tv_status, status);
                helper.setTextColor(R.id.tv_status, status.equals("未接来电") ? getResources().getColor(R.color.color_ef2626) : getResources().getColor(R.color.color_3));
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            getCallDetail();
        }, recyclerView);
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "getCallDetail":
                CallDetailBean bean = (CallDetailBean) result;
                List<CallDetailBean.DataBean.InfoBean> data = bean.getData().getInfo();
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
    public void onFinish() {
        dismissLoadingDialog();
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        statusLayoutManager.showErrorLayout();
    }

    @OnClick({R.id.iv_message, R.id.iv_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message:
                startActivity(new Intent(this, NewMessageActivity.class)
                        .putExtra("targetName", data.getTargetName())
                        .putExtra("targetPhone", data.getMobile()));
//                startActivity(new Intent(this, MessageDetailActivity.class)
//                        .putExtra("targetMobile", data.getMobile())
//                        .putExtra("messageId", data.getCallId()));
                break;
            case R.id.iv_call:
                startActivity(new Intent(this, CallActivity.class)
                        .putExtra("comefrom", "dial")
                        .putExtra("phonenum", data.getMobile()));
                break;
        }
    }
}
