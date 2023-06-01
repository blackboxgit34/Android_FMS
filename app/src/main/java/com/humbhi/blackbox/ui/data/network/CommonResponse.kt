package com.humbhi.blackbox.ui.data.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

class CommonResponse(
    val errors:ArrayList<ErrorType>,
    @SerializedName(
        value = "persons",
        alternate = ["messages","maintenances"]
    )
    val data:ArrayList<*>,
    @SerializedName(
        value = "message"
    )
    val dataObject: Any
) {

    /**
     * Parses the reeponse into a instance of provided class
     * @param <T> the class type
     * @return the parsed response object
     * */
    fun <T> toResponseModelArray(classRef:Class<T>?):ArrayList<T>{
        val arrayList = ArrayList<T>()
        for (dataItem in data){
            arrayList.add(Gson().fromJson(Gson().toJson(dataItem),classRef))
        }
        return arrayList
    }
}