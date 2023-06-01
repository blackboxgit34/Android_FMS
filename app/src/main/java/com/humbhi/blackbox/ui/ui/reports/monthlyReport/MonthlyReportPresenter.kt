package com.humbhi.blackbox.ui.ui.reports.monthlyReport

interface MonthlyReportPresenter {

    fun hitMonthlyReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        CommandName: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String
    )
}