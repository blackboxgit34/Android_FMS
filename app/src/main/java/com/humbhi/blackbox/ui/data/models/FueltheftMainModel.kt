package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FueltheftMainModel(
    val ActualDistance: String?,
    val DifferenceFuelLevel: String?,
    val Distance: String?,
    val Duration: String?,
    val EndFuelLevel: String?,
    val EndLocation: String?,
    val EndateTime: String?,
    val FirstFilling: Double?,
    val LastFiliing: Double?,
    val Speed: String?,
    val StartDateTime: String?,
    val StartFuelLevel: String?,
    val StartLocation: String?,
    val VehicleName: String?,
    val bbid: String?
):Parcelable{

}