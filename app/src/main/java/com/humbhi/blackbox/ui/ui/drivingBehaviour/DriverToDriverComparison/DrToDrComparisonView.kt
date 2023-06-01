package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverToDriverComparison

import com.humbhi.blackbox.ui.data.models.DriverToDriverCompModel


interface DrToDrComparisonView {

    fun getDrivingLimitData(driverToDriverCompModel: DriverToDriverCompModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}