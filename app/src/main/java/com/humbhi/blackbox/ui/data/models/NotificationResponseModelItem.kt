package com.humbhi.blackbox.ui.data.models

data class NotificationResponseModelItem(
    val DeviceId: String,
    val Isdeclined: Int,
    val MessageId: String,
    val Name: String,
    val NotificationDate: String,
    val NotificationMsg: String,
    val NotificationRead: String,
    val ResponseStatus: ResponseStatus,
    val TypeId: String,
    val UserCustid: String,
    val VehicleId: String,
    val lat: String,
    val longi: String
)