package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConsolidatedViolationSub(
    val DrName: String?,
    val Location: String?,
    val SubSNo: Int?,
    val ViolationType: String?,
    val sDate: String?
): Parcelable