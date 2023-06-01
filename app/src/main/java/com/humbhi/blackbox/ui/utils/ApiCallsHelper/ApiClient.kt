package com.humbhi.blackbox.ui.utils.ApiCallsHelper

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ApiClient {
    private const val BASE_URL = "http://api1.trackmaster.in/api/"

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder().build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    fun <T> callApi(
        call: retrofit2.Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        executor.execute {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        runOnMainThread { onSuccess(data) }
                    } else {
                        runOnMainThread { onError(Throwable("Data is null")) }
                    }
                } else {
                    runOnMainThread { onError(Throwable(response.message())) }
                }
            } catch (e: Exception) {
                runOnMainThread { onError(e) }
            }
        }
    }

    private fun runOnMainThread(action: () -> Unit) {
        Handler(Looper.getMainLooper()).post(action)
    }
}
