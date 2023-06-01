package com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft

interface FuelTheftPresenter {

    fun hitFuelTheftReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        type: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0:String
    )

}