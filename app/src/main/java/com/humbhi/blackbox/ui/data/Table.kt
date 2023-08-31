package com.humbhi.blackbox.ui.data

data class Table(
    val CCID: String?,
    val DeviceStatus: String?,
    val ExpireIndays: Int,
    val OneYearCharge: Double,
    val PaymentStatus: String,
    val Status: Int,
    val TotalAmountOneYear: Double,
    val TotalAmountTwoYear: Double,
    val TwoYearCharge: Double,
    val bbid: String?,
    val commercialvalidity: String?,
    val instDate: String?,
    val latefee: Double,
    val requestactivationdate: Any?,
    val vehname: String?
)