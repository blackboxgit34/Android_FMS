package com.humbhi.blackbox.ui.ui.reports.stoppagereport

interface StoppageReportPresenter {

    fun hitStoppageReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        interval: String,
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