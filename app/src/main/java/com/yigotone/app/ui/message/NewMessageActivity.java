package com.yigotone.app.ui.message;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/14 11:11.
 */
public class NewMessageActivity extends BaseActivity<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.et_message) EditText etMessage;
    @BindView(R.id.tv_user) TextView tvUser;
    @BindView(R.id.rl_people) RelativeLayout rlPeople;
    private String targetName;
    private String targetPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_message;
    }

    @Override
    public MessagePresenter initPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void initView() {
        String title;
        ContactBean data = (ContactBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            title = targetName = data.getName();
            targetPhone = data.getPhone();
            rlPeople.setVisibility(View.GONE);
        } else {
            title = "新信息";
            rlPeople.setVisibility(View.VISIBLE);
        }
        new BaseTitleBar(this).setTitleText(title).setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        U.showToast("网络错误");
    }

    @OnClick({R.id.iv_send, R.id.iv_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_send:
                sendMessage();
                break;
            case R.id.iv_contact:
                break;
        }
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            U.showToast("请输入短信内容");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("callingnumber", UserManager.getInstance().userData.getMobile());
        map.put("callednumber", "13260450335");
        map.put("calledname", "MM");
        map.put("content", content);
        presenter.sendMessage(UrlUtil.SEND_MESSAGE, map, "sendMessage");
        showLoadingDialog("正在发送");
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "sendMessage":
                CodeBean bean = (CodeBean) result;
                if (bean.getStatus() == 0) {
                    EventBus.getDefault().post("refreshMessageList");
                    U.showToast("短信发送成功");
                    finish();
                }
                break;
        }
    }

    @Override
    public void onLayoutError(Throwable throwable) {
        dismissLoadingDialog();
    }
}
