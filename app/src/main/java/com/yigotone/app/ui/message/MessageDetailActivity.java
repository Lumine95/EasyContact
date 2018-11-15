package com.yigotone.app.ui.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.ui.adapter.MessageAdapter;
import com.yigotone.app.view.BaseTitleBar;
import com.yigotone.app.view.statusLayoutView.StatusLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/8 16:41.
 */
public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.et_message) EditText etMessage;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private StatusLayoutManager statusLayoutManager;
    private MessageAdapter mAdapter;
    private int count;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("短信详情").setLeftIcoListening(v -> finish());
        initRecyclerView();
        recyclerView.setAdapter(mAdapter = new MessageAdapter(genData()));
        mAdapter.setNewData(genData());
        mAdapter.setUpFetchEnable(true);
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        mAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                startUpFetch();
            }
        });
    }

    private void startUpFetch() {
        count++;
        /**
         * set fetching on when start network request.
         */
        mAdapter.setUpFetching(true);
        /**
         * get data from internet.
         */
        recyclerView.postDelayed(() -> {
            mAdapter.addData(0, genData());
            /**
             * set fetching off when network request ends.
             */
            mAdapter.setUpFetching(false);
            /**
             * set fetch enable false when you don't need anymore.
             */
            if (count > 5) {
                mAdapter.setUpFetchEnable(false);
            }
        }, 300);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        statusLayoutManager = new StatusLayoutManager.Builder(recyclerView).setOnStatusClickListener(view -> {

        }).build();
        //   statusLayoutManager.showLoadingLayout();
    }

    private List<MessageBean.DataBean> genData() {
        List<MessageBean.DataBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageBean.DataBean movie = new MessageBean.DataBean(i % 2 == 0 ? MessageBean.DataBean.THAT : MessageBean.DataBean.THIS, i + " 消息消息");
            list.add(movie);
        }
        return list;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }


    @OnClick(R.id.iv_send)
    public void onViewClicked() {
    }
}
