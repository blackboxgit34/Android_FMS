package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable
@Parcelize
data class Overspeed(
    val OverCustomCount: Int,
    val bbid: String,
    val driverName: String,
    val maxSpeed: Int,
    val overSpeedData: @RawValue List<OverSpeedData>,
    val overSpeedDuration: String,
    val overspeedCount: Int,
    val overspeedLimit: Int,
    val serialno: Int,
    val vehname: String
): Parcelable{

}