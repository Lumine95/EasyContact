package com.yigotone.app.ui.packages;

import android.content.Intent;
import android.widget.TextView;

import com.android.library.view.MyGridView;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.ui.adapter.CommonAdapter;
import com.yigotone.app.ui.adapter.MyViewHolder;
import com.yigotone.app.view.BaseTitleBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/10/31 14:57.
 */
public class SubScribePackageActivity extends BaseActivity<PackageContract.Presenter> implements PackageContract.View {

    @BindView(R.id.grid_view) MyGridView gridView;
    @BindView(R.id.tv_tip) TextView tvTip;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subscribe;
    }

    @Override
    public PackageContract.Presenter initPresenter() {
        return new PackagePresenter(this);
    }

    @Override
    public void initView() {
        new BaseTitleBar(this).setTitleText("订购套餐").setLeftIcoListening(v -> finish());

        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            strings.add("套餐" + i);
        }

        gridView.setAdapter(new CommonAdapter<String>(this, strings, R.layout.item_package) {
            @Override
            public void convert(MyViewHolder helper, String item, int position) {
                helper.setText(R.id.tv_title, item);
                helper.setText(R.id.tv_price, position + "");
                helper.getConvertView().setOnClickListener(v ->
                        startActivity(new Intent(SubScribePackageActivity.this, PackageDetailActivity.class))
                );
            }
        });
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
