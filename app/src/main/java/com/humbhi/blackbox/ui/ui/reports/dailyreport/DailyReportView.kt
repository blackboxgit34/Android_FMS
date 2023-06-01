package com.humbhi.blackbox.ui.ui.reports.dailyreport

import com.humbhi.blackbox.ui.data.models.DailyReportResponse

interface DailyReportView {

    fun getDailyReportResponse(dailyReportResponse: DailyReportResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}