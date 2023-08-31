package com.humbhi.blackbox.ui.data.models

data class ObjStoppageReport(
    val AddPoi: String,
    val Duration: String,
    val FuelLevel: Any,
    val IgnitionStatus: Boolean,
    val IgnitionStatusDet: Any,
    val InLIdOpenDate: Any,
    val InLidCloseDate: Any,
    val InLidDuration: Any,
    val InLidStatus: Any,
    val OutLIdCloseDate: Any,
    val OutLIdOpenDate: Any,
    val OutLidDuration: Any,
    val OutLidStatus: Any,
    val ReportDate: Any,
    val Speed: Any,
    val StartDate: String,
    val StopDate: String,
    val StopLatitude: String,
    val StopLocation: String,
    val StopLongitude: String,
    val TotalDistance: Any,
    val VehicleName: Any,
    val poi: String
)