package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport

interface HarshBreakingPresenter {

    fun hitHarshBreakReportApi(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    )
}