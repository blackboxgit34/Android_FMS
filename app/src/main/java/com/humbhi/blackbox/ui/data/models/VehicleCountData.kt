package com.humbhi.blackbox.ui.data.models

data class VehicleCountData(
    val Backdoor: Int,
    val BreakDown: Int,
    val Frontdoor: Int,
    val HasNextPage: Boolean,
    val HasPreviousPage: Boolean,
    val Hispeed: Int,
    val Ignitionon: Int,
    val Moving: Int,
    val PageCount: Int,
    val PageNumber: Int,
    val Parked: Int,
    val TotalVehicles: Int,
    val Unreach: Int,
    val ac: Int,
    val acoff: Int,
    val batteryvoltage: Int,
    val firefighting: Int,
    val liveReport: Any,
    val temp: Int,
    val towed: Int,
    val waterfilling: Int
)