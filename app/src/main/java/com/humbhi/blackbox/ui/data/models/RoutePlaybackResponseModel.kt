package com.humbhi.blackbox.ui.data.models

data class RoutePlaybackResponseModel(
    val DistanceTravelled: String,
    val Drivingduration: String,
    val Idlingduration: String,
    val RouteDataList: ArrayList<RouteData>,
    val Stoppageduration: String,
    val type : String
)