package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkingHourData(
    val DriverName: String,
    val IgnitionOnOffCounter: String,
    val PrevAllVehWorkingHours: String,
    val TotalAllVehWorkingHours: String,
    val TotalIgnitionTime: String,
    val TotalWorkingHours: String,
    val VehicleName: String,
    val bbid: String,
    val custid: String,
    val objIgnitionStatusReport: List<ObjIgnitionStatusReport>
): Parcelable {
    fun getVehicleNam():String{
        return VehicleName
    }
}