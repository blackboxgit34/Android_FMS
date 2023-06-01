package com.humbhi.blackbox.ui.ui.reports.overspeedReport

interface OverSpeedReportPresenter {

    fun hitOverSpeedReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        mode: String,
        custid: String,
        downloadtype: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName:String
    )
}