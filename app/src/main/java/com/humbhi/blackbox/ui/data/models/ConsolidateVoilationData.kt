package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ConsolidateVoilationData(
    val BBID: String,
    val ConsolidatedViolationSubList:@RawValue List<ConsolidatedViolationSub>,
    val HACount: Int,
    val HBCount: Int,
    val OSCount: Int,
    val RTCount: Int,
    val SNo: Int,
    val VehicleName: String,
    val count: Int,
    val totViolation: Int
):Parcelable{

}