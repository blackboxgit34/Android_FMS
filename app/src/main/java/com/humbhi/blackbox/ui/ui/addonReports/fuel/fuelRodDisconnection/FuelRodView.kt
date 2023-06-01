package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection

import com.humbhi.blackbox.ui.data.models.FuelRodDisconnectionResponseModel

interface FuelRodView {

    fun getFuelRodDisconnectionData(fuelRodDisconnectionResponseModel: FuelRodDisconnectionResponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}