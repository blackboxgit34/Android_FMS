package com.humbhi.blackbox.ui.data.models

data class Ais140Models(
    val veh:String?, val activationDate: String?, val expirationDate: String?,
    val billAmountOneYear:Double?,
    val bbid:String?,
    val expiryInDays: String?, val lateFee: Double?, val DeviceStatus: String?, val PaymentStatus: String,
    val totalAmountOneYear: Double, val status: Int?,
    val billAmountTwoYear:Double?,val totalAmountTwoYear: Double
)