package com.easycontact.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseActivity;
import com.easycontact.app.fragment.DataFragment;
import com.easycontact.app.fragment.HomeFragment;
import com.easycontact.app.fragment.MessageFragment;
import com.easycontact.app.fragment.MineFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ll_content) LinearLayout llContent;
    @BindView(R.id.ll_home_btn) LinearLayout llHomeBtn;
    @BindView(R.id.ll_message_btn) LinearLayout llMessageBtn;
    @BindView(R.id.ll_data_btn) LinearLayout llDataBtn;
    @BindView(R.id.ll_mine_btn) LinearLayout llMineBtn;

    private Fragment mContent;
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private DataFragment dataFragment;
    private MineFragment mineFragment;

    @Override
    protected int getLayoutId() {
        QMUIStatusBarHelper.translucent(this);
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setBottomButton(llHomeBtn);
        switchContentFragment(homeFragment == null ? homeFragment = new HomeFragment() : homeFragment);
    }

    @Override
    public void initData() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
