package com.humbhi.blackbox.ui.ui.reports.distanceReport

interface DistanceReportPresenter {

    fun hitDistanceReportApi(
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