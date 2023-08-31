package com.humbhi.blackbox.ui.data.models

data class ReminderData(
    val DaysDue: Int?,
    val DueDate: String?,
    val DueDateString: String?,
    val Remarks: String?,
    val RenewalType: String?,
    val RenewalTypeId: Int?,
    val remBefore: Int?,
    val renewalID: Int?,
    val status: String?,
    val vehicleID: Int?,
    val vehicleName: Any?
)