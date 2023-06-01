package com.humbhi.blackbox.ui.ui.customerCare

import com.humbhi.blackbox.ui.data.models.HarshAccelerationDataModel

interface AddServiceRequestView {
    fun getAllVehicles(harshAccelerationDataModel: HarshAccelerationDataModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}