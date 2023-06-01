package com.humbhi.blackbox.ui.ui.reports.distanceReport

data class AaData(
    val DriverMobNo: Any,
    val DriverName: String,
    val SerialNo: Int,
    val TotalDistance: String,
    val VehicleName: String,
    val bbid: String,
    val dataCount: Int,
    val objTravelReport: List<ObjTravelReport>
)