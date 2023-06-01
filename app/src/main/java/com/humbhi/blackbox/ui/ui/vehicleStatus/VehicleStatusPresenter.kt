package com.humbhi.blackbox.ui.ui.vehicleStatus

interface VehicleStatusPresenter {

    fun hitLiveStatusApi(custid:String,StatusCode:String,sEcho:String,iDisplayStart:Int
                         ,iDisplayLength:Int,sSearch:String,iSortCol_0:String,sSortDir_0:String)
}