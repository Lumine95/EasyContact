package com.yigotone.app.api;

import android.util.Log;

import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ZMM on 2018/1/17.
 */

public class BaseApiImpl implements BaseApi {
    private volatile static Retrofit retrofit = null;
    protected Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    protected OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

    public BaseApiImpl(String baseUrl) {
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpBuilder.addInterceptor(getLogInterceptor()).build())
                .baseUrl(baseUrl);
    }

    /**
     * 构建retrofit
     *
     * @return Retrofit对象
     */
    @Override
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (BaseApiImpl.class) {
                if (retrofit == null) {
                    retrofit = retrofitBuilder.build(); //创建retrofit对象
                }
            }
        }
        return retrofit;
    }

    @Override
    public OkHttpClient.Builder setInterceptor(Interceptor interceptor) {
        return okHttpBuilder.addInterceptor(interceptor);
    }

    @Override
    public Retrofit.Builder setConverterFactory(Converter.Factory factory) {
        return retrofitBuilder.addConverterFactory(factory);
    }

    /**
     * 日志拦截
     */
    public HttpLoggingInterceptor getLogInterceptor() {

        // 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        // 新建日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.d("ApiUrl", "--->" + message));
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
