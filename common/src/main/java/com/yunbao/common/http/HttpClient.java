package com.yunbao.common.http;

import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.tencent.live.OkHttpBuilder;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;

import okhttp3.OkHttpClient;

/**
 * Created by cxf on 2018/9/17.
 */

public class HttpClient {

    private static HttpClient sInstance;
    private String mLanguage;//语言
    private String mUrl;
    private OkHttpClient mOkHttpClient;
    private OkHttpBuilder mBuilder;

    private HttpClient() {
        mUrl = CommonAppConfig.HOST + "/appapi/?service=";
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
        mBuilder = new OkHttpBuilder();
        mOkHttpClient = mBuilder
                .setHost(CommonAppConfig.HOST)
                .setTimeout(10000)
                .setLoggingInterceptor(loggingInterceptor)
                .build(CommonAppContext.sInstance);
    }

    public static HttpClient getInstance() {
        if (sInstance == null) {
            synchronized (HttpClient.class) {
                if (sInstance == null) {
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }


    public GetRequest<JsonBean> get(String serviceName, String tag) {
        return mBuilder.req1(mUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);

    }

    public PostRequest<JsonBean> post(String serviceName, String tag) {
        return mBuilder.req2(mUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public void cancel(String tag) {
        mBuilder.cancel(mOkHttpClient,tag);
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

}
