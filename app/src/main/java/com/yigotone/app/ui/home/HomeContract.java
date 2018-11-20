package com.yigotone.app.ui.home;

import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.base.BaseView;
import com.yigotone.app.bean.CallBean;
import com.yigotone.app.bean.CodeBean;

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

        void refreshMobileStatus(String mobileStatus);

        void onRecyclerViewError(Throwable throwable);

        void onDeleteResult(CodeBean bean);
    }

    public interface Presenter extends BasePresenter {
        void getCallRecords(HashMap<String, Object> map);

        void updateMobileStatus(Map<String, Object> map);

        void getMobileStatus(String url, Map<String, Object> map);

        void deleteCallRecord(String url,  Map<String, Object> map);
    }
}
