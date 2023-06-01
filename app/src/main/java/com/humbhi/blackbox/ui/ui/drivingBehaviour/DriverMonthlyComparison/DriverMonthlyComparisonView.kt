package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverMonthlyComparison

import com.humbhi.blackbox.ui.data.models.DriverMonthlyResponse
import com.humbhi.blackbox.ui.data.models.DrivingLimitModel

interface DriverMonthlyComparisonView {

    fun getMonthlyComparison(drivingMonthlyComparison: DriverMonthlyResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}