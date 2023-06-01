package com.humbhi.blackbox.ui.data.models

data class RiskyDriverModel(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val data: List<RiskyDriverData>,
    val sEcho: Int
)