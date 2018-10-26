package com.easycontact.app.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easycontact.app.R;
import com.easycontact.app.base.BaseFragment;
import com.easycontact.app.base.BasePresenter;
import com.easycontact.app.view.BaseTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/26 10:41.
 */
public class DialFragment extends BaseFragment {
    @BindView(R.id.iv_keyboard) ImageView ivKeyboard;
    @BindView(R.id.iv_add) ImageView ivAdd;
    @BindView(R.id.dial_keyword) LinearLayout dialKeyword;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dial;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onFinish() {


    }

    @Override
    public void onError(Throwable throwable) {

    }

    @OnClick({R.id.iv_keyboard, R.id.iv_add, R.id.iv_one, R.id.iv_two, R.id.iv_three, R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine, R.id.iv_asterisk, R.id.iv_zero, R.id.iv_pound, R.id.iv_call, R.id.iv_collapse, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                break;
            case R.id.iv_one:
                break;
            case R.id.iv_two:
                break;
            case R.id.iv_three:
                break;
            case R.id.iv_four:
                break;
            case R.id.iv_five:
                break;
            case R.id.iv_six:
                break;
            case R.id.iv_seven:
                break;
            case R.id.iv_eight:
                break;
            case R.id.iv_nine:
                break;
            case R.id.iv_asterisk:
                break;
            case R.id.iv_zero:
                break;
            case R.id.iv_pound:
                break;
            case R.id.iv_call:
                break;
            case R.id.iv_collapse:
                collapseDialKeyboard();
                break;
            case R.id.iv_keyboard:
                showDialKeyboard();
                break;
            case R.id.iv_delete:
                break;
        }
    }

    private void collapseDialKeyboard() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        hideAnim.setDuration(360);
        dialKeyword.startAnimation(hideAnim);
        dialKeyword.setVisibility(View.GONE);
    }

    private void showDialKeyboard() {
        TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showAnim.setDuration(360);
        dialKeyword.startAnimation(showAnim);
        dialKeyword.setVisibility(View.VISIBLE);
    }
}
