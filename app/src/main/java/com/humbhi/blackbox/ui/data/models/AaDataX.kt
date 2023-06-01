package com.humbhi.blackbox.ui.data.models

data class AaDataX(
    val DrLimit: String,
    val SNo: Int,
    val TotDur: String,
    val VehicleName: String,
    val count: Int,
    val drDrivingLimitSubLst: List<Any>,
    val haltTime: String
)