package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection

interface FuelRodPresenter {

    fun hitFuelRodDisconnectionApi(
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
        sSortDir_0: Int
    )

}