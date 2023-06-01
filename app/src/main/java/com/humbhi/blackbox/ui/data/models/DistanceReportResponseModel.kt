package com.humbhi.blackbox.ui.data.models

data class DistanceReportResponseModel(
    val aaData: List<DistanceReportItemData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)