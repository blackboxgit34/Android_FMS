package com.humbhi.blackbox.ui.data.network

data class ErrorType(var type:String,val context:String?){
    fun getErrorMessageId():Any{
        val apiErrorTypeEnum:ApiErrorTypeEnum = ApiErrorTypeEnum.getErrorTypeByName(type)
        if (apiErrorTypeEnum.name.equals(
                ApiErrorTypeEnum.INTERNAL_ERROR_OCCURRED.name,
                ignoreCase = true
        ))
        {
            return type
        }
        else{
            return apiErrorTypeEnum.errorMessageId
        }
    }
}
