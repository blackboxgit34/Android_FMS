package com.humbhi.blackbox.ui.data.models

data class RashTurnDataModel(
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val aaData: List<RashTurnData>,
    val sEcho: Int
)