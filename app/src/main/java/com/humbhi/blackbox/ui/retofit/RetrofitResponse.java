package com.humbhi.blackbox.ui.retofit;

/**
 * Created by pro202 on 18/1/18.
 */
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface RetrofitResponse
{
    void onServiceResponse(int requestCode, Response<ResponseBody> response);
}