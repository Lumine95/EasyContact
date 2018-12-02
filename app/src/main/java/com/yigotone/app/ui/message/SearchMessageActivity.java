package com.yigotone.app.ui.message;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.library.utils.U;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.Utils;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/12/1  20:15.
 */
public class SearchMessageActivity extends BaseActivity<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.et_search) EditText etSearch;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;
    private BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder> mAdapter;
    private String keyword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_search;
    }

    @Override
    public MessageContract.Presenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("信息搜索").setLeftIcoListening(v -> finish());
        initRecyclerView();
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    U.showToast("请输入关键字");
                } else {
                    getMessageList(true);
                }
                return true;
            }
            return false;
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(refreshLayout).setOnStatusClickListener(v -> {
            // pageIndex = 1;
            getMessageList(true);
        }).build();
        recyclerView.setAdapter(mAdapter = new BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder>(R.layout.item_message) {
            @Override
            protected void convert(BaseViewHolder helper, MessageBean.DataBean item) {
                helper.setText(R.id.tv_title, item.getContent());
                helper.setText(R.id.tv_name, Utils.getContactName(item.getMobile()));
                helper.setText(R.id.tv_date, Utils.getShortTime(Long.parseLong(item.getCreateAt())));
                helper.setOnClickListener(R.id.content, view -> startActivity(new Intent(mContext, MessageDetailActivity.class)
                        .putExtra("targetMobile", item.getMobile())
                        .putExtra("messageId", item.getMessageId())));
            }
        });
//        mAdapter.setOnLoadMoreListener(() -> {
//          //  pageIndex++;
//            getMessageList(false);
//        }, recyclerView);
        refreshLayout.setOnRefreshListener(() -> {
            //   pageIndex = 1;
            getMessageList(false);
        });
    }

    private void getMessageList(boolean isLoadingLayout) {
        if (isLoadingLayout) {
            statusLayoutManager.showLoadingLayout();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("keyword", keyword);
//        map.put("page", pageIndex);
//        map.put("count", pageSize);
        presenter.getMessageList(UrlUtil.SEARCH_MESSAGE, map, "searchMessage");
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "searchMessage":
                MessageBean bean = (MessageBean) result;
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
        dismissLoadingDialog();
        U.hideSoftKeyboard(etSearch);
        statusLayoutManager.showErrorLayout();
    }

    @Override
    public void onFinish() {
        U.hideSoftKeyboard(etSearch);
        dismissLoadingDialog();
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
