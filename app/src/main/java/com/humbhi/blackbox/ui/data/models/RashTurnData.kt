package com.humbhi.blackbox.ui.data.models

data class RashTurnData(
    val BBID: String,
    val SNo: Int,
    val VehicleName: String,
    val count: Int,
    val drRashTurnSubLst: List<DrRashTurnSubLst>
)