package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ObjIgnitionStatusReport(
    val Date: String?,
    val Duration: String?,
    val EFuel: String?,
    val ELocation: String?,
    val EngHour: String?,
    val FuelLevel: String?,
    val FuelTheft: String?,
    val IgnitionOffTime: String?,
    val IgnitionOnTime: String?,
    val SFuel: String?,
    val SLocation: String?,
    val Threshour: String?,
    val TotalConsumption: String?,
    val TotalDistance: String?,
    val TotalFilling: String?,
    val TotalMileage: String?,
    val Vehname: String?,
    val bbid: String?
): Parcelable {

    fun getDat():String?{
        return Date ?:""
    }

    fun getDuratn():String?{
        return Duration ?:""
    }
    fun getEFuels():String?{
        return EFuel ?:""
    }
    fun getELocations():String?{
        return ELocation ?:""
    }
}