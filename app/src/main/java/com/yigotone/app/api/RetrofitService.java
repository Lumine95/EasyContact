package com.yigotone.app.api;


import com.yigotone.app.bean.CallBean;
import com.yigotone.app.bean.CallDetailBean;
import com.yigotone.app.bean.CodeBean;
import com.yigotone.app.bean.DataEntity;
import com.yigotone.app.bean.MessageBean;
import com.yigotone.app.bean.OrderBean;
import com.yigotone.app.bean.PackageBean;
import com.yigotone.app.bean.UserBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by ZMM on 2018/1/17.
 */

public interface RetrofitService {
    String BASE_URL = "http://yigoutong.1bu2bu.com/";

    @POST()
    Observable<CodeBean> getRandomCode(@Url String url);

    @POST()
    Observable<CodeBean> register(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<UserBean> login(@Url String url, @QueryMap Map<String, String> map);

    @POST()
    Observable<DataEntity> updateMobileStatus(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<CallBean> getCallRecordList(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<PackageBean> getPackageList(@Url String url);

    @POST()
    Observable<MessageBean> getMessageList(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<CallBean> postCallParams(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<CallDetailBean> getCallDetail(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<OrderBean> getOrderList(@Url String url, @QueryMap Map<String, Object> map);

    @POST()
    Observable<UserBean> getUserBeanResult(@Url String url, @QueryMap Map<String, Object> map);

//
//    @POST
//    Observable<UserBean> registerUser(@Url String url, @Body Map map);
//
//    @POST
//    Observable<UserBean> commitIndividualData(@Url String url, @Body Map map);


}
