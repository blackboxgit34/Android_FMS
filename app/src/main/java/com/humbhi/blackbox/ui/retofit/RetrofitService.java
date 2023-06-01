package com.humbhi.blackbox.ui.retofit;

/**
 * Created by pro202 on 18/1/18.
 */

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitService
{
    @GET
    Call<ResponseBody> callGetService(@Url String url);

    @POST
    Call<ResponseBody> callPostService(@Url String url, @Body RequestBody params);

    @Multipart
    @POST
    Call<ResponseBody> callMultipartService(@Url String url, @PartMap HashMap<String, RequestBody> map, @Part MultipartBody.Part part);

    @POST
    Call<ResponseBody> callMultipartService(@Url String url, @QueryMap HashMap<String, String> map);
    @Multipart
    @POST
    Call<ResponseBody> callMultipartService(@Url String url, @PartMap HashMap<String, RequestBody> map, @Part MultipartBody.Part part, @Part MultipartBody.Part thumb);

    @POST
    @Multipart
    Call<ResponseBody> callMultipartList(@Url String url, @PartMap HashMap<String, RequestBody> map, @Part ArrayList<MultipartBody.Part> parts);

    @POST
    @Multipart
    Call<ResponseBody> uploadMedia(@Url String url, @PartMap HashMap<String, RequestBody> map, @Part MultipartBody.Part part);

}



