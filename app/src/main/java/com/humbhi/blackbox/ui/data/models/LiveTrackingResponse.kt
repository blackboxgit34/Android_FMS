package com.humbhi.blackbox.ui.data.models

data class LiveTrackingResponse(
    val aaData: List<LiveTrackingVehicleDataResponse>,
    val iTotalRecords:Int,
    val iTotalDisplayRecords:Int,
)
