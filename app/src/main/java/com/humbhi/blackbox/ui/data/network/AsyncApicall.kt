package com.humbhi.blackbox.ui.data.network

import com.humbhi.blackbox.ui.retofit.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AsyncApicall {

    private const val BASE_URL = "http://api1.trackmaster.in/api/"
    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var okHttpClient = OkHttpClient.Builder() //Use For Time Out
        .readTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(interceptor)
        .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        .build()
    // Create a Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    // Define the API service
    private val apiService = retrofit.create(NetworkService::class.java)
    // Define a suspend function for making API calls asynchronously
    suspend fun <T> callApi(apiCall: suspend NetworkService.() -> T): T {
        return withContext(Dispatchers.IO) {
            apiCall(apiService)
        }
    }
}