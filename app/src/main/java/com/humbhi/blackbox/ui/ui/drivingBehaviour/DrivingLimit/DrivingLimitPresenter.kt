package com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit

import retrofit2.http.Query

interface DrivingLimitPresenter {

    fun getDrivingLimitData(
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