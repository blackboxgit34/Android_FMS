package com.humbhi.blackbox.ui.ui.addonReports.workingHour

import com.humbhi.blackbox.ui.data.models.WorkingHourDataResponse

interface WorkingHourView {

    fun getWorkHourReportData(workingHourDataResponse: WorkingHourDataResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}