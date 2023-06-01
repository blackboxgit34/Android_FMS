package com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft

import com.humbhi.blackbox.ui.data.models.FuelTheftReponseModel

interface FuelTheftView {

    fun getFuelTheftData(fuelTheftReponseModel: FuelTheftReponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)

}