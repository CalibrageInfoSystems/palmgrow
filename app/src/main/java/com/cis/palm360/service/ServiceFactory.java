package com.cis.palm360.service;

import android.content.Context;

import com.cis.palm360.BuildConfig;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    public static <T> T createRetrofitService(Context context, final Class<T> clazz) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstantURL.LOCAL_URL)
                .client(getHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(clazz);
    }


    private static OkHttpClient getHttpClient(final Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        //Enable log in debug mode
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logInterceptor);
        }

        return httpClient.build();
    }
}
