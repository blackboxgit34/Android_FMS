package com.humbhi.blackbox.ui.data.models

data class BoxInfoModel(
    val aaData: List<SettingsData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)