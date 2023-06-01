package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Theftdata(
    val FueltheftMainModel: List<FueltheftMainModel>,
    val TotalTheft: Int,
    val VehicleName: String,
    val bbid: String,
    val objCount: Int
):Parcelable{
    fun getVehicleNam():String{
        return VehicleName
    }
}