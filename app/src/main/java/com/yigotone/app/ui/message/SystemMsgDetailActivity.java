package com.yigotone.app.ui.message;

import android.widget.TextView;

import com.android.library.utils.DateUtil;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.view.BaseTitleBar;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/11/23 11:49.
 */
public class SystemMsgDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_date) TextView tvDate;
    @BindView(R.id.tv_content) TextView tvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message_detail;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("消息详情").setLeftIcoListening(v -> finish());
        MessageBean.DataBean data = (MessageBean.DataBean) getIntent().getSerializableExtra("data");
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
        tvDate.setText("发布时间：" + DateUtil.getDate(Long.valueOf(data.getCreateAt()), "yyyy/MM/dd HH:mm"));
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
