package com.humbhi.blackbox.ui.data.network.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {

    @POST
    fun postCallWithAny(@Url url:String,@Body jsonObject: JsonObject?):Observable<Response<Any>>

    @GET
    fun getCall(@Url url: String): Observable<Response<Any>>

    @GET
    fun getCallArray(@Url url: String): Observable<Response<Any>>


}