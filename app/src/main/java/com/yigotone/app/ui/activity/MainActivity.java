package com.yigotone.app.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.library.utils.U;
import com.orhanobut.logger.Logger;
import com.yigotone.app.R;
import com.yigotone.app.application.MyApplication;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.bean.ContactBean;
import com.yigotone.app.ui.fragment.DataFragment;
import com.yigotone.app.ui.fragment.MineFragment;
import com.yigotone.app.ui.home.HomeFragment;
import com.yigotone.app.ui.login.LoginActivity;
import com.yigotone.app.ui.message.MessageFragment;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.ll_content) LinearLayout llContent;
    @BindView(R.id.ll_home_btn) LinearLayout llHomeBtn;
    @BindView(R.id.ll_message_btn) LinearLayout llMessageBtn;
    @BindView(R.id.ll_data_btn) LinearLayout llDataBtn;
    @BindView(R.id.ll_mine_btn) LinearLayout llMineBtn;

    @BindView(R.id.tv_select_all) public TextView tvSelectAll;
    @BindView(R.id.tv_delete) public TextView tvDelete;
    @BindView(R.id.tv_cancel) public TextView tvCancel;
    @BindView(R.id.rl_call_delete) public RelativeLayout rlDelete;

    @BindView(R.id.tv_all_read) public TextView tvAllRead;
    @BindView(R.id.tv_delete_sms) public TextView tvDeleteSMS;
    @BindView(R.id.rl_sms_read) public RelativeLayout rlSMS;

    private Fragment mContent;
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private DataFragment dataFragment;
    private MineFragment mineFragment;

    @Override
    protected int getLayoutId() {
        // QMUIStatusBarHelper.translucent(this);
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        setBottomButton(llHomeBtn);
        switchContentFragment(homeFragment == null ? homeFragment = new HomeFragment() : homeFragment);
        homeFragment.setActivity(this);
        queryContactInfo();
    }

    private void queryContactInfo() {  // 遍历通讯录并存储
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "contact_id",
                        "data1"}, null, null, null);
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            ContactBean bean = new ContactBean(name, number.replaceAll("\\s*", ""), Id);
            UserManager.getInstance().contactList.add(bean);
        }
        cursor.close();
    }

    @OnClick({R.id.ll_home_btn, R.id.ll_message_btn, R.id.ll_data_btn, R.id.ll_mine_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home_btn:
                setBottomButton(llHomeBtn);
                switchContentFragment(homeFragment == null ? homeFragment = new HomeFragment() : homeFragment);
                break;
            case R.id.ll_message_btn:
                setBottomButton(llMessageBtn);
                switchContentFragment(messageFragment == null ? messageFragment = new MessageFragment() : messageFragment);
                messageFragment.setActivity(this);
                break;
            case R.id.ll_data_btn:
                setBottomButton(llDataBtn);
                switchContentFragment(dataFragment == null ? dataFragment = new DataFragment() : dataFragment);
                break;
            case R.id.ll_mine_btn:
                setBottomButton(llMineBtn);
                switchContentFragment(mineFragment == null ? mineFragment = new MineFragment() : mineFragment);
                break;
        }
    }

    private void setBottomButton(ViewGroup llSelect) {
        llHomeBtn.setSelected(false);
        llMessageBtn.setSelected(false);
        llDataBtn.setSelected(false);
        llMineBtn.setSelected(false);

        llSelect.setSelected(true);
    }

    public void switchContentFragment(Fragment to) {
        if (mContent == null) {
            mContent = new Fragment();
        }
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                // Can not perform this action after onSaveInstanceState ,solve :commit -> commitAllowingStateLoss
                transaction.hide(mContent).add(R.id.ll_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    private static long currentBackPressedTime = 0;
    int BACK_PRESSED_INTERVAL = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);// 继续执行父类其他点击事件
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(String event) {
//        Logger.d("eventBus: " + event);
//        switch (event) {
//            case "tokenInvalid":
//                U.savePreferences("uid", "");
//                U.savePreferences("token", "");
//                MyApplication.getInstance().exit();
//                startActivity(new Intent(this, LoginActivity.class));
//                break;
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (homeFragment != null) {
            homeFragment.refreshMobileStatus();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeEvent(Integer statusCode) {
        Logger.d("statusCode: " + statusCode);

        switch (statusCode) {
            case Constant.ERROR_PARAM:
                U.showToast("参数验证失败");
                break;
            case Constant.ERROR_MYSQL:
                   U.showToast("数据库操作失败");
                break;
            case Constant.ERROR_NO_USER:
                U.showToast("用户不存在");
                break;
            case Constant.ERROR_REPEAT_REG:
                U.showToast("重复注册");
                break;
            case Constant.ERROR_CODE:
                U.showToast("验证码错误");
                break;
            case Constant.ERROR_PASSWORD:
                U.showToast("密码错误");
                break;
            case Constant.ERROR_TOKEN:
                U.showToast("账号异地登录,请重新登录");
               // EventBus.getDefault().post("tokenInvalid");
                U.savePreferences("uid", "");
                U.savePreferences("token", "");
                MyApplication.getInstance().exit();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case Constant.ERROR_USER_OPEN:
                U.showToast("开户失败");
                break;
            case Constant.ERROR_TRUSTEESHIP_AlREADY:
                U.showToast("用户已被托管");
                break;
            case Constant.ERROR_TRUSTEESHIP_FAILURE:
                EventBus.getDefault().post("mobileNotShutDown");
                break;
            case Constant.ERROR_TRUSTEESHIP_NONE:
                U.showToast("用户未被托管");
                break;
            case Constant.ERROR_TRUSTEESHIP_CANCEL:
                U.showToast("取消托管失败");
                break;
            case Constant.ERROR_SEND_MESSAGE:
                U.showToast("短信发送失败");
                break;
        }
    }
}