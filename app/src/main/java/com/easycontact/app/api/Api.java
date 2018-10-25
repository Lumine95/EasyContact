package com.easycontact.app.api;

/**
 * Created by ZMM on 2018/1/17.
 */

public class Api extends BaseApiImpl {
    private static Api api = new Api(RetrofitService .BASE_URL);

    public Api(String baseUrl) {
        super(baseUrl);
    }

    public static RetrofitService getInstance() {
        return api.getRetrofit().create(RetrofitService.class);
    }

}
