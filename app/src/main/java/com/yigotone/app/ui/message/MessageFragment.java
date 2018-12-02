package com.yigotone.app.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
import com.android.library.utils.U;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseFragment;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.ui.activity.MainActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;
import com.yigotone.app.view.swipeMenu.EasySwipeMenuLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class MessageFragment extends BaseFragment<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.tv_edit) TextView tvEdit;
    @BindView(R.id.tv_select_all) TextView tv_select_all;
    @BindView(R.id.iv_add) ImageView ivAdd;
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder> mAdapter;
    private MainActivity mainActivity;
    private boolean isEdit = false;
    private boolean isSelectAll = false;

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
        etSearch.setOnClickListener(v -> startActivity(new Intent(mContext, SearchMessageActivity.class)));
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
                ((EasySwipeMenuLayout) helper.getView(R.id.swipe_menu)).setCanLeftSwipe(!isEdit);
                helper.setText(R.id.tv_title, item.getContent());
                helper.setText(R.id.tv_name, Utils.getContactName(item.getMobile()));
                helper.setGone(R.id.iv_red_dot, item.getIsread() == 0);
                helper.getView(R.id.iv_select).setSelected(item.isSelect);
                helper.setText(R.id.tv_date, Utils.getShortTime(Long.parseLong(item.getMessagetime())));
                helper.setOnClickListener(R.id.right, view -> deleteDialog(item.getMobile()));
                helper.setOnClickListener(R.id.iv_select, view -> {
                    item.isSelect = !item.isSelect;
                    mAdapter.notifyDataSetChanged();
                });
                helper.setOnClickListener(R.id.content, view -> startActivity(new Intent(mContext, MessageDetailActivity.class)
                        .putExtra("targetMobile", item.getMobile())
                        .putExtra("messageId", item.getMessageId())));
                helper.setGone(R.id.iv_select, isEdit);
            }
        });
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            startActivity(new Intent(mContext, MessageDetailActivity.class));
        });
        //  mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
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


    @OnClick({R.id.tv_edit, R.id.iv_add, R.id.tv_select_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                refreshEditStatus();
                break;
            case R.id.iv_add:
                startActivity(new Intent(mContext, NewMessageActivity.class));
                break;
            case R.id.tv_select_all:
                isSelectAll = !isSelectAll;
                tv_select_all.setText(isSelectAll ? "全不选" : "全选");
                for (MessageBean.DataBean bean : mAdapter.getData()) {
                    bean.isSelect = isSelectAll;
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void refreshEditStatus() {
        isEdit = !isEdit;
        tvEdit.setText(isEdit ? "取消" : "编辑");
        mAdapter.notifyDataSetChanged();
        mainActivity.rlSMS.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        ivAdd.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        tv_select_all.setVisibility(isEdit ? View.VISIBLE : View.GONE);
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

            case "markRead":
                CodeBean code = (CodeBean) result;
                if (code.getStatus() == 0) {
                    U.showToast("标记成功");
                    getMessageList(false);
                } else {
                    U.showToast("标记失败");
                }
                break;
            case "delete":
                CodeBean codeBean = (CodeBean) result;
                if (codeBean.getStatus() == 0) {
                    U.showToast("删除成功");
                    getMessageList(false);
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
                getMessageList(false);
                break;
        }
    }

    public void setActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivity.rlSMS.setOnClickListener(v -> {
        });
        mainActivity.tvAllRead.setOnClickListener(v -> {
            setMessageRead();
        });
        mainActivity.tvDeleteSMS.setOnClickListener(v -> {
            deleteDialog("");
        });
    }

    private void setMessageRead() {
        StringBuilder sb = new StringBuilder();
        for (MessageBean.DataBean bean : mAdapter.getData()) {
            if (bean.isSelect) {
                sb.append(bean.getMobile()).append(",");
            }
        }
        if (TextUtils.isEmpty(sb.toString())) {
            U.showToast("请至少选择一项");
            return;
        }
        showLoadingDialog("");
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("type", isSelectAll ? 1 : 2);
        map.put("targetMobile", sb.toString().substring(0, sb.toString().length() - 1));
        presenter.sendMessage(UrlUtil.MARK_MESSAGE_READ, map, "markRead");
    }

    private void deleteDialog(String mobile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_package_buy, null);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        tv_cancel.setText("取消");
        tv_sure.setText("确认");
        tv_tip.setText("确认删除所选短消息？");
        tv_cancel.setOnClickListener(v -> dialog.dismiss());
        tv_sure.setOnClickListener(v -> {
            dialog.dismiss();
            deleteMessage(mobile);
        });

        dialog.setCancelable(true);
        dialog.setView(view);
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(mContext, 340), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void deleteMessage(String mobile) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(mobile)) {
            for (MessageBean.DataBean bean : mAdapter.getData()) {
                if (bean.isSelect) {
                    sb.append(bean.getMobile()).append(",");
                }
            }
            if (TextUtils.isEmpty(sb.toString())) {
                U.showToast("请选择至少一项删除");
                return;
            }
        }
        showLoadingDialog("正在删除");
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("type", isSelectAll ? 1 : 2);
        if (TextUtils.isEmpty(mobile)) {
            map.put("targetMobile", sb.toString().substring(0, sb.toString().length() - 1));
        } else {
            map.put("targetMobile", mobile);
        }
        Logger.d(map);
        presenter.sendMessage(UrlUtil.DELETE_MESSAGE, map, "delete");
    }
}
