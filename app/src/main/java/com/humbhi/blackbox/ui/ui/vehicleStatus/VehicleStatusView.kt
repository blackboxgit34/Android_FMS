package com.humbhi.blackbox.ui.ui.vehicleStatus

import com.humbhi.blackbox.ui.data.models.LiveTrackingResponse
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusModel

interface VehicleStatusView {

    fun getVehicleStatus(liveTrackingResponse: VehicleLiveStatusModel)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)
}