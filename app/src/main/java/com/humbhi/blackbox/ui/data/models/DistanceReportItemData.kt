package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class DistanceReportItemData(
    var DriverMobNo: @RawValue Any?,
    var DriverName: String,
    var SerialNo: Int,
    var TotalDistance: String,
    var VehicleName: String,
    var bbid: String,
    var dataCount: Int,
    var objTravelReport: @RawValue ArrayList<ObjTravelReport>?
) : Parcelable{
    fun getDistanceTravelObject(): @RawValue ArrayList<ObjTravelReport>? {
        return objTravelReport
    }
}