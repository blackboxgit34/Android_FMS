package com.humbhi.blackbox.ui.retofit;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.humbhi.blackbox.ui.data.models.VehicleCountData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String GITHUB_BASE_URL = "http://api1.trackmaster.in/api/";
    private static RetrofitClient instance;
    NetworkService retrofitAPI;
    private RetrofitClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//Use For Time Out
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();
        System.setProperty("http.keepAlive", "false");
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
         retrofitAPI = retrofit.create(NetworkService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public NetworkService getMyApi() {
        return retrofitAPI;
    }
}
