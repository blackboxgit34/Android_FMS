package com.humbhi.blackbox.ui.data.models

data class MonthlyDataReponseModel(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val aaData: List<MonthData>,
    val sEcho: Int
)