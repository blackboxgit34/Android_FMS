package com.humbhi.blackbox.ui.data.models

data class FuelRodDisconnectionResponseModel(
    val aaData: List<DisconnectionData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)