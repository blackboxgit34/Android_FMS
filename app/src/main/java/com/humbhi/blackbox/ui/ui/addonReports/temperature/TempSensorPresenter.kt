package com.humbhi.blackbox.ui.ui.addonReports.temperature

interface TempSensorPresenter {
    fun hitTemperatureReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        CommandName: String,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0:String
    )
}