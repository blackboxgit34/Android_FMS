package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelFilling

import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData

interface FuelFillingView {


    fun getFuelFillingData(fuelFillingResponseData: FuelFillingResponseData)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}