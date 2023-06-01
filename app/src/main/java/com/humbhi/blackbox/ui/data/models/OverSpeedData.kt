package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OverSpeedData(
    val DateTime: String,
    val Location: String,
    val Speed: String,
    val status: String?  = null
):Parcelable