package com.humbhi.blackbox.ui.data.models

data class HarshBreakData(
    val BBID: String,
    val SNo: Int,
    val VehicleName: String,
    val count: Int,
    val drHarshBrakingSubLst: List<DrHarshBrakingSubLst>
)