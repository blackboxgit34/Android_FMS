package com.humbhi.blackbox.ui.ui.reports.stoppagereport

import com.humbhi.blackbox.ui.data.models.StoppageReportResponseModel

interface StoppageReportView {
    fun getStoppageReportResponse(stoppageReportResponseModel: StoppageReportResponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}