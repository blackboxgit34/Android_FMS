package com.humbhi.blackbox.ui.data.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.humbhi.blackbox.BuildConfig
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.utils.AppConstant.BASE_URL_HITECH
import com.humbhi.blackbox.ui.utils.AppConstant.BASE_URL_TRACKMASTER
import com.humbhi.blackbox.ui.utils.AppConstant.BASE_URL_TRACKMASTER_SECURE
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    private const val TIME_OUT = 12000
    
    private const val BKS_KEYSTORE_RAW_FILE_ID = 0
    private const val SSL_KEY_PASSWORD_STRING_ID = 0
    private var retrofit: Retrofit? = null
    private var retrofitWithIncreaseTimeOut:Retrofit? = null
    val gson = GsonBuilder().setLenient().create()
    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    fun getRetrofitBuilderForHitec():Retrofit{
        Log.e("Gson_Retrofit","Builder")
        val baseURL:String = BASE_URL_HITECH
        Log.e("BASE_URL",baseURL)
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .followRedirects(true) // Enable following redirects
            .followSslRedirects(true) // Enable following SSL redirects
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit!!
    }

    fun getRetrofitBuilderForTrackMaster():Retrofit{
        Log.e("Gson_Retrofit","Builder")
        val baseURL:String = BASE_URL_TRACKMASTER
        Log.e("BASE_URL",baseURL)
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .followRedirects(true) // Enable following redirects
            .followSslRedirects(true) // Enable following SSL redirects
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit!!
    }
    fun getRetrofitBuilderForTrackMasterSecure():Retrofit{
        Log.e("Gson_Retrofit","Builder")
        val baseURL:String = BASE_URL_TRACKMASTER_SECURE
        Log.e("BASE_URL",baseURL)
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .followRedirects(true) // Enable following redirects
            .followSslRedirects(true) // Enable following SSL redirects
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit!!
    }

    private fun getRequestHeader():OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(TIME_OUT.toLong(),TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT.toLong(),TimeUnit.SECONDS)
            .build()
    }

    private fun httpClient():OkHttpClient.Builder{
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.connectTimeout(60,TimeUnit.SECONDS)
        httpClient.readTimeout(60,TimeUnit.SECONDS)
        httpClient.addInterceptor(getLoggingInterceptor())
        return httpClient
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor{
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG){
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
        return logging
    }

}