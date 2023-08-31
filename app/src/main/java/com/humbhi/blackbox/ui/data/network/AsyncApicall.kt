package com.humbhi.blackbox.ui.data.network

import com.google.gson.GsonBuilder
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
    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    val gson = GsonBuilder().setLenient().create()
    var okHttpClient = OkHttpClient.Builder() //Use For Time Out
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .retryOnConnectionFailure(true) // Enable retries on connection failures
        .followRedirects(true) // Enable following redirects
        .followSslRedirects(true) // Enable following SSL redirects
        .retryOnConnectionFailure(true) // Enable retries on connection failures
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()
    // Create a Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
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