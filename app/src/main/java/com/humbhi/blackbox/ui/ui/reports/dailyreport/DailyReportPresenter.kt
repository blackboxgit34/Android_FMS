package com.humbhi.blackbox.ui.ui.reports.dailyreport

interface DailyReportPresenter {
    fun getDailyReportDataApi(
        fromDate: String,
        toDate: String,
        custid: String,
        lowerbound: Int,
        upperbound: Int,
        searchVehName: String
    )
}