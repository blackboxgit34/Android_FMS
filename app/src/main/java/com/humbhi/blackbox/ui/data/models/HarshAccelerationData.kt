package com.humbhi.blackbox.ui.data.models

data class HarshAccelerationData(
    val BBID: String,
    val SNo: Int,
    val VehicleName: String,
    val count: Int,
    val drHarshAcceleratorSubLst: List<DrHarshAcceleratorSubLst>
)