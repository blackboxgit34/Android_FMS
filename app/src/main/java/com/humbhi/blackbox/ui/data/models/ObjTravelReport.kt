package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ObjTravelReport(
    val CumulativeDistance: Double? = null,
    val DistanceTravelled: String? = null,
    val DistanceTravelledSail: Double? = null,
    val DriverName: String? = null,
    val Duration: String? = null,
    val EndDateTime: String? = null,
    val EndLatitude: String? = null,
    val EndLocation: String? = null,
    val EndLongitude: String? = null,
    val ReportDate: String? = null,
    val StartDateTime: String? = null,
    val StartLatitude: String? = null,
    val StartLocation: String? = null,
    val StartLongitude: String? = null,
    val VehicleName: String? = null
):Parcelable{


    fun getStartLat():String{
        return StartLatitude ?: ""
    }
    fun getEndLat():String{
        return EndLatitude ?: ""
    }
    fun getStartLongi():String{
        return StartLongitude ?: ""
    }
    fun getEndLongi():String{
        return EndLongitude ?: ""
    }
    fun getCumulativeDistanc():Double{
        return CumulativeDistance ?: 0.0
    }
    fun getDistanceTravell():String{
        return DistanceTravelled ?: ""
    }
    fun getDriverNam():String{
        return DriverName ?: ""
    }
}