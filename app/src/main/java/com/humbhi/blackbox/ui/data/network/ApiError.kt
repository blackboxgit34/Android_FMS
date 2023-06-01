package com.humbhi.blackbox.ui.data.network

import com.google.gson.annotations.SerializedName

class ApiError (
    @SerializedName("statusCode")
    private var statusCode:Int,
    @SerializedName("message")
    private var message:String?
){
    fun getStatusCode():Int{
        return if (statusCode == 0){
            ErrorUtils.DEFAULT_STATUS_CODE
        } else statusCode
    }

    fun getMessage():String?{
        return message ?:""
    }
}
