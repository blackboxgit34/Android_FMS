package com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport

interface ConsolidateViolationPresenter {
    fun hitConsolidationViolationApi(
        fDate: String,
        tdate: String,
        CustId: String,
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