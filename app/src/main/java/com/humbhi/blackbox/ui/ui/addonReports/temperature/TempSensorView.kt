package com.humbhi.blackbox.ui.ui.addonReports.temperature

import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData
import com.humbhi.blackbox.ui.data.models.TemperatureReportResponse

interface TempSensorView  {

    fun getTempReportData(temperatureReportResponse: TemperatureReportResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}