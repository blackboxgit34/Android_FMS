package com.humbhi.blackbox.ui.data.models

data class ScoreCardReportModel(
    val aaData: List<AaData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)