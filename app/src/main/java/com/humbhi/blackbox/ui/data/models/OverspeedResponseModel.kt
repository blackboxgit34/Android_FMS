package com.humbhi.blackbox.ui.data.models

data class OverspeedResponseModel(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val aaData: List<Overspeed>,
    val sEcho: Int
)