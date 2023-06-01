package com.humbhi.blackbox.ui.data.models

data class OverStoppageResponseModel(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val aaData: List<OverStoppageData>,
    val sEcho: Int
)