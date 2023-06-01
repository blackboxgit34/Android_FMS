package com.humbhi.blackbox.ui.ui.drivingBehaviour.NoDrivingReport

interface NoDrivingPresenter {
    fun getNoDrivingLimitData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadtype: String,
        reportName: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    )
}