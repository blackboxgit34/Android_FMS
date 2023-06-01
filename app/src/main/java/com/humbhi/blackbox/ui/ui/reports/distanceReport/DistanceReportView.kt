package com.humbhi.blackbox.ui.ui.reports.distanceReport

import com.humbhi.blackbox.ui.data.models.DailyReportResponse
import com.humbhi.blackbox.ui.data.models.DistanceReportResponseModel

interface DistanceReportView {

    fun getDistanceReport(distanceReportResponse: DistanceReportResponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}