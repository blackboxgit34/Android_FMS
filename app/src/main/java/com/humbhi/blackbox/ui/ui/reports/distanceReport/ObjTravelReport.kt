package com.humbhi.blackbox.ui.ui.reports.distanceReport

data class ObjTravelReport(
    val CumulativeDistance: Double,
    val DistanceTravelled: String,
    val DistanceTravelledSail: Double,
    val DriverName: Any,
    val Duration: String,
    val EndDateTime: String,
    val EndLatitude: String,
    val EndLocation: String,
    val EndLongitude: String,
    val ReportDate: Any,
    val StartDateTime: String,
    val StartLatitude: String,
    val StartLocation: String,
    val StartLongitude: String,
    val VehicleName: String
)