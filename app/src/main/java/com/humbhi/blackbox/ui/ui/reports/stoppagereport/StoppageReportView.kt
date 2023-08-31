package com.humbhi.blackbox.ui.ui.reports.stoppagereport

import com.humbhi.blackbox.ui.data.models.StoppageReportResponse

interface StoppageReportView {
    fun getStoppageReportResponse(stoppageReportResponseModel: StoppageReportResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}