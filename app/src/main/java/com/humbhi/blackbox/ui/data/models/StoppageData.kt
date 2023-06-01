package com.humbhi.blackbox.ui.data.models

data class StoppageData(
    val BBID: String,
    val DriverName: String,
    val StoppageCount: Int,
    val TotalStoppageTime: String,
    val VehicleName: String,
    val objStoppageReport: List<Any>
)