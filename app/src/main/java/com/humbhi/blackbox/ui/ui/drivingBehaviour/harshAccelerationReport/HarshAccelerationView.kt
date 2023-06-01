package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshAccelerationReport

import com.humbhi.blackbox.ui.data.models.HarshAccelerationDataModel

interface HarshAccelerationView {

    fun getHarshAccelerationData(harshAccelerationDataModel: HarshAccelerationDataModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}