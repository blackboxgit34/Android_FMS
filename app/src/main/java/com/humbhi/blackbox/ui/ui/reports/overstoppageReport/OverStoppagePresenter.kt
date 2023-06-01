package com.humbhi.blackbox.ui.ui.reports.overstoppageReport

interface OverStoppagePresenter {

    fun hitOverStoppageReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
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