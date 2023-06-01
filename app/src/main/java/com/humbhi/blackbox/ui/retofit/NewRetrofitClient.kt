package com.humbhi.blackbox.ui.retofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NewRetrofitClient {
    val baseUrl = "http://api1.trackmaster.in/api/"
    fun getInstance(): Retrofit {
        var interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        var okHttpClient = OkHttpClient.Builder() //Use For Time Out
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}