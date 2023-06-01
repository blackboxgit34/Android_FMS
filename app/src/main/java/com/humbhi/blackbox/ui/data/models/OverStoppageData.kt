package com.humbhi.blackbox.ui.data.models

data class OverStoppageData(
    val bbid: String,
    val custid: Int,
    val driverName: String,
    val overStoppageData: List<Any>,
    val overstoppageCount: Int,
    val overstoppageDuration: String,
    val overstoppageLimit: Int,
    val vehname: String
)