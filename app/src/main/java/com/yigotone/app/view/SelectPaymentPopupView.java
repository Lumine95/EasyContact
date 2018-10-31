package com.yigotone.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yigotone.app.R;

/**
 * Created by ZMM on 2017/11/22.
 */

public class SelectPaymentPopupView extends PopupWindow {
    private Activity mActivity;
    private TextView tv_sure;
    private TextView tv_cancel;
    private RelativeLayout rl_alipay;
    private RelativeLayout rl_wechat;
    private ImageView iv_alipay;
    private ImageView iv_wechat;

    public SelectPaymentPopupView(Context context) {
        super(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        initView();
        setListener();
    }


    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_payment, null);

        tv_sure = view.findViewById(R.id.tv_sure);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        rl_alipay = view.findViewById(R.id.rl_alipay);
        rl_wechat = view.findViewById(R.id.rl_wechat);
        iv_alipay = view.findViewById(R.id.iv_alipay);
        iv_wechat = view.findViewById(R.id.iv_wechat);

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.popup_delete);
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置透明度，改变popupWindow上边视图
        setParams(0.5f);
    }

    // 事件监听
    private void setListener() {
        //  监听popupWindow消失
        this.setOnDismissListener(() -> {
            setParams(1f);
            dismiss();
        });
        tv_cancel.setOnClickListener(v -> {
            setParams(1f);
            dismiss();
        });
        tv_sure.setOnClickListener(v -> {
            if (onSuccessListener != null) {
                onSuccessListener.onSuccess(iv_alipay.getVisibility() == View.VISIBLE ? 1 : 2);
            }
            setParams(1f);
            dismiss();
        });

        rl_alipay.setOnClickListener(v -> {
            iv_alipay.setVisibility(View.VISIBLE);
            iv_wechat.setVisibility(View.GONE);
        });
        rl_wechat.setOnClickListener(v -> {
            iv_alipay.setVisibility(View.GONE);
            iv_wechat.setVisibility(View.VISIBLE);
        });
    }

    private onSuccessListener onSuccessListener;

    public void setOnSuccessListener(onSuccessListener listener) {
        onSuccessListener = listener;
    }

    public interface onSuccessListener {
        void onSuccess(int channel);
    }

    private void setParams(float v) {
        // 设置透明度，改变popupWindow上边视图
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = v;
        mActivity.getWindow().setAttributes(params);
    }
}
