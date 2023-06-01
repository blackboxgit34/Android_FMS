package com.humbhi.blackbox.ui.data.network

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.Headers
import retrofit2.Response
import retrofit2.Retrofit

abstract class ResponseResolver <T>(private val mRetrofit: Retrofit) : Observer<Response<T>>{

    abstract fun onSuccess(t:T)
    abstract fun onFailure(throwable: Throwable)
    abstract fun onReceiveHeaders(headers: Headers)
    abstract fun onError(error: ApiError)

    override fun onComplete() {

    }

    override fun onError(throwable: Throwable) {
        onFailure(throwable)
    }

    override fun onNext(tResponse: Response<T>) {
        Log.e("Response", tResponse.toString())
        if (tResponse.isSuccessful){
            Log.e("Response is Successful",tResponse.toString())
            if (!tResponse.headers().get("ApiKeys.X_SESSION").isNullOrEmpty())
                onReceiveHeaders(tResponse.headers())
                onSuccess(tResponse.body()!!)
            }
        else{
            Log.e("Response not successful",tResponse.toString())
            onError(ErrorUtils.parseError(mRetrofit,tResponse))
        }

    }

    override fun onSubscribe(d: Disposable) {

    }

}