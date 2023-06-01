package com.humbhi.blackbox.ui.ui.reports.distanceReport

data class DistanceReportModel(
    val aaData: List<AaData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)