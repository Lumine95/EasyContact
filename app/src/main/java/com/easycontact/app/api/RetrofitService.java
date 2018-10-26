package com.easycontact.app.api;


import com.easycontact.app.bean.UserBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZMM on 2018/1/17.
 */

public interface RetrofitService {
    String BASE_URL = "http://yigoutong.1bu2bu.com/index.php?s=/Api/";

    @POST("Login/randomCode")
    Observable<UserBean> getRandomCode(@Body Map map);
//
//    @POST
//    Observable<UserBean> registerUser(@Url String url, @Body Map map);
//
//    @POST
//    Observable<UserBean> commitIndividualData(@Url String url, @Body Map map);


}
