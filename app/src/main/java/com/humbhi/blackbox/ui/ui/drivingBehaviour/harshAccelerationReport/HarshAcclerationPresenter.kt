package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshAccelerationReport

interface HarshAcclerationPresenter {

    fun hitHarshAccelerationReportApi(
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