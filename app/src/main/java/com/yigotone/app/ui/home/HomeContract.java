package com.yigotone.app.ui.home;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;
import com.yigotone.app.bean.CallBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZMM on 2018/10/31 11:53.
 */
public class HomeContract {
    public interface View extends BaseView {
        void onMobileStatusResult(String status);

        void callRecordsResult(List<CallBean.DataBean> data);
    }

    public interface Presenter extends BasePresenter {
        void getCallRecords(  HashMap<String, Object> map);

        void updateMobileStatus(Map<String, Object> map);
    }
}
