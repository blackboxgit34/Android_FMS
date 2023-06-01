package com.humbhi.blackbox.ui.ui.reports.monthlyReport

import com.humbhi.blackbox.ui.data.models.MonthlyDataReponseModel

interface MonthlyReportView {

    fun getMonthlyReport(monthlyDataReponseModel: MonthlyDataReponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)

}