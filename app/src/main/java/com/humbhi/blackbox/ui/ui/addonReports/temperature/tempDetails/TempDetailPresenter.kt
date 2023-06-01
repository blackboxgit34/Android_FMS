package com.humbhi.blackbox.ui.ui.addonReports.temperature.tempDetails

interface TempDetailPresenter {
    fun hitTemperatureDetailReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        timeInterval: String,
        iDisplayLength: Int,
        sSearch: String,
        offset: Int,
        iSortCol_0: Int,
        sSortDir_0:String
    )
}