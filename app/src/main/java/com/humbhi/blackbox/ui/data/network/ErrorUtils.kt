package com.humbhi.blackbox.ui.data.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

object ErrorUtils {
    const val DEFAULT_STATUS_CODE = 900

    fun parseError(mRetrofit:Retrofit,response: Response<*>):ApiError{
        val converter: Converter<ResponseBody,ApiError> =
            mRetrofit.responseBodyConverter(ApiError::class.java, arrayOfNulls(0))
        val error:ApiError
        try {
            error = if (response.errorBody() != null){
                converter.convert(response.errorBody()!!)!!
            }
            else
            {
                ApiError(response.code(),response.message())
            }
        } catch (e:Exception){
            var statusCode = DEFAULT_STATUS_CODE
            var message = ""
            if (response.code()!=0){
                statusCode = response.code()
            }
            if (response.message() != null && response.message().isNotEmpty()){
                message = response.message()
            }
            return ApiError(statusCode,message)
        }
        return error
    }
}