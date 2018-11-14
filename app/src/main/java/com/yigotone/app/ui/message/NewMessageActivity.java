package com.yigotone.app.ui.message;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.view.BaseTitleBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/14 11:11.
 */
public class NewMessageActivity extends BaseActivity<MessageContract.Presenter> implements MessageContract.View {
    @BindView(R.id.et_message) EditText etMessage;
    @BindView(R.id.tv_user) TextView tvUser;

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
        new BaseTitleBar(this).setTitleText("新信息").setLeftIcoListening(v -> finish());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

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
        map.put("callednumber","17600095930");
        map.put("calledname", "哈哈");
        map.put("content", content);
        presenter.sendMessage(UrlUtil.SEND_MESSAGE, map, "sendMessage");
    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "sendMessage":
                break;
        }
    }
}
