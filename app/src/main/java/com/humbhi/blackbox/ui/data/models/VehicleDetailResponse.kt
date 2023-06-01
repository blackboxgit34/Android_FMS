package com.humbhi.blackbox.ui.data.models

data class VehicleDetailResponse(
    val aaData: List<VehicleDetailData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)