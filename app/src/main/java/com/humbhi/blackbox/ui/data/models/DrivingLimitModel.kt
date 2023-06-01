package com.humbhi.blackbox.ui.data.models

data class DrivingLimitModel(
    val aaData: List<AaDataX>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)