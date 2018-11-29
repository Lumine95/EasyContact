package com.yigotone.app.ui.message;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.library.utils.U;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.ui.contact.ContactActivity;
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
    @BindView(R.id.et_user) EditText etUser;
    @BindView(R.id.rl_people) RelativeLayout rlPeople;
    private String targetName;
    private String targetPhone;
    private boolean isInput = true; // 是否手输

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
        String targetName = getIntent().getStringExtra("targetName");
        if (targetName != null) {
            title = this.targetName = targetName;
            targetPhone = getIntent().getStringExtra("targetPhone");
            rlPeople.setVisibility(View.GONE);
        } else {
            title = "新信息";
            rlPeople.setVisibility(View.VISIBLE);
        }
        new BaseTitleBar(this).setTitleText(title).setLeftIcoListening(v -> finish());

        etUser.setOnKeyListener((v, keyCode, event) -> {
            if (!isInput) {
                etUser.setText("");
                isInput = true;
            }
            return false;
        });

        etUser.setOnTouchListener((view, motionEvent) -> {
            if (!isInput) {
                etUser.setText("");
                isInput = true;
            }
            return false;
        });
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
                startActivityForResult(new Intent(this, ContactActivity.class)
                        .putExtra("tag", true), 9527);
                break;
        }
    }

    private void sendMessage() {
        if (isInput) {
            targetPhone = targetName = etUser.getText().toString();
        }

        if (TextUtils.isEmpty(targetPhone) && TextUtils.isEmpty(targetName)) {
            U.showToast("联系人不能为空");
            return;
        }
        String content = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            U.showToast("请输入短信内容");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("callingnumber", UserManager.getInstance().userData.getMobile());
        map.put("callednumber", targetPhone);
        map.put("calledname", targetName);
        map.put("content", content);
        showLoadingDialog("正在发送");
        presenter.sendMessage(UrlUtil.SEND_MESSAGE, map, "sendMessage");
      //  Logger.d(map);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9527) {
//            StringBuilder nameStr = new StringBuilder();
//            StringBuilder phoneStr = new StringBuilder();
//            for (ContactBean bean : UserManager.getInstance().selectedList) {
//                nameStr.append(bean.getName()).append(",");
//                phoneStr.append(bean.getPhone()).append(",");
//            }
//            targetName = nameStr.toString().substring(0, nameStr.toString().length() - 1);
//            targetPhone = phoneStr.toString().substring(0, phoneStr.toString().length() - 1);
//            etUser.setText(targetName);
            if (data != null) {
                targetPhone = data.getStringExtra("number");
                etUser.setText(targetName = data.getStringExtra("name"));
                etUser.setSelection(targetName.length());
                isInput = false;
            }
        }
    }
}
