package com.humbhi.blackbox.ui.data.models

data class VehicleLiveStatusModel(
    val aaData: List<VehicleLiveStatusDataItem>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)