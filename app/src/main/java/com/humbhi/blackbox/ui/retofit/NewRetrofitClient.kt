package com.humbhi.blackbox.ui.retofit
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NewRetrofitClient {
    val baseUrl = "http://api1.trackmaster.in/api/"
    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient = OkHttpClient.Builder() //Use For Time Out
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor { chain ->
//                var request = chain.request()
//                var response = chain.proceed(request)
//                var retryCount = 0
//                while (!response.isSuccessful && retryCount < 3) {
//                    retryCount++
//                    response.close()
//                    // Delay before retrying
//                    Thread.sleep(2000)
//                    request = chain.request()
//                    response = chain.proceed(request)
//                }
//                response
//            }
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .followRedirects(true) // Enable following redirects
            .followSslRedirects(true) // Enable following SSL redirects
            .retryOnConnectionFailure(true) // Enable retries on connection failures
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()

    }
}