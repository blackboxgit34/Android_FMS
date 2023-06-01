package com.humbhi.blackbox.ui.data.models

data class DrivingBehaviourRouteDataModel(
    val DistanceTravelled: String,
    val Drivingduration: String,
    val Idlingduration: String,
    val RouteDataList: List<RouteDataX>,
    val Stoppageduration: String
)