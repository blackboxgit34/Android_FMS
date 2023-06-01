package com.humbhi.blackbox.ui.data.models

data class WorkingHourDataResponse(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int,
    val aaData: List<WorkingHourData>
)