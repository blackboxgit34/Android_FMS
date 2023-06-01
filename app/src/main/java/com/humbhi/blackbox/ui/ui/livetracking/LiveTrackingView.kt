package com.humbhi.blackbox.ui.ui.livetracking

import com.humbhi.blackbox.ui.data.models.LiveTrackingResponse

interface LiveTrackingView {

    fun getLiveVehicleDetail(liveTrackingResponse:LiveTrackingResponse)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)
}