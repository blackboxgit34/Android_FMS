package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FuelData(
    val ActualDistance: String ?= null,
    val DifferenceFuelLevel: String,
    val Distance: String,
    val Duration: String ?= null,
    val EndFuelLevel: String,
    val EndLocation: String ?= null,
    val EndateTime: String,
    val FirstFilling: Double,
    val LastFiliing: Double,
    val Speed: String ?= null,
    val StartDateTime: String,
    val StartFuelLevel: String,
    val StartLocation: String,
    val VehicleName: String,
    val latlong: String
) : Parcelable {
    fun getVehicleNam(): String {
        return VehicleName
    }
}