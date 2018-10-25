package com.easycontact.app.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.utils.DensityUtil;
import com.easycontact.app.activity.NoDisturbActivity;
import com.easycontact.app.R;
import com.easycontact.app.base.BaseFragment;
import com.easycontact.app.base.BasePresenter;
import com.easycontact.app.view.TriangleDrawable;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/10/23 15:46.
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.btn_take_over) Button btnTakeOver;
    @BindView(R.id.tv_balance) TextView tvBalance;
    @BindView(R.id.iv_add) ImageView ivAdd;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.iv_dial) ImageView ivDial;

    private EasyPopup popup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_take_over, R.id.iv_add, R.id.iv_dial})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_over:
                break;
            case R.id.iv_add:
                showMenu();
                break;
            case R.id.iv_dial:
                break;
        }
    }

    private void showMenu() {
        List<String> data = new ArrayList<>();
        data.add("免打扰设置");
        data.add("订购套餐");
        //   .setAnimationStyle(R.style.RightTop2PopAnim)
//                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
        popup = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.layout_right_pop)
                //   .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener((view, basePopup) -> {
                    View arrowView = view.findViewById(R.id.v_arrow);
                    arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.parseColor("#FFFFFF")));
                    view.findViewById(R.id.tv_no_disturb);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mContext,NoDisturbActivity.class));
                            popup.dismiss();
                        }
                    });
                })
                .setFocusAndOutsideEnable(true)
//                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .apply();
        int offsetX = DensityUtil.dpToPx(mContext, 20) - ivAdd.getWidth() / 2;
        // int offsetY = (mTitleBar.getHeight() - ivAdd.getHeight()) / 2;
        popup.showAtAnchorView(ivAdd, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, 5);
    }

    @Override
    public void onFinish(Object result, String message) {

    }

    @Override
    public void onError(Throwable throwable, String message) {

    }
}
