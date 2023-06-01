package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class FuelFillingResponseData(
    val aaData: List<FuelFillingData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)