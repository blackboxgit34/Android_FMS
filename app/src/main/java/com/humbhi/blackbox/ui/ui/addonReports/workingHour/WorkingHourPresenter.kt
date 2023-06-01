package com.humbhi.blackbox.ui.ui.addonReports.workingHour

interface WorkingHourPresenter {

    fun hitWorkingHourReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0:String
    )
}