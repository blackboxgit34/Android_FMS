package com.humbhi.blackbox.ui.ui.livetracking

interface LiveTrackingPresenter {

    fun hitLiveTrackingVehicleDataApi(custid:String,beginDate:String,endDate: String,StatusCode:String,sEcho:String,iDisplayStart:Int
    ,iDisplayLength:Int,sSearch:String,iSortCol_0:String,sSortDir_0:String)
}