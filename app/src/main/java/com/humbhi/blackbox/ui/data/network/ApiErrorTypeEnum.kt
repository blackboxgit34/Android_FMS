package com.humbhi.blackbox.ui.data.network

import com.humbhi.blackbox.R
import java.lang.IllegalArgumentException
import java.util.*

enum class ApiErrorTypeEnum( val errorMessageId:Int) {

    INTERNAL_ERROR_OCCURRED(R.string.app_name);

    companion object {
        fun getErrorTypeByName(name:String?) =
            try {
                valueOf(name!!.toUpperCase(Locale.ENGLISH))
            }
            catch (illegalArgumentException: IllegalArgumentException){
                INTERNAL_ERROR_OCCURRED
            }

        fun getEnumForValue(errorMessageId: Int):String{
            for (messageTypeValue in values()){
                if (messageTypeValue.errorMessageId == errorMessageId)
                    return messageTypeValue.name
            }
            return ""
        }
    }
}