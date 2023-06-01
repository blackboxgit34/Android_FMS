package com.humbhi.blackbox.ui.ui.dashboard

interface DashboardFragPresenter {

    fun callGetVehiclesApi(custid:String)

    fun callFleetUtilizationChart(custid:String)

    fun callFuelAnalysisChartAPI(custid:String)

    fun callSpeedAnalysisAPI(custid:String)

    fun callDriverBehaviourApi(custid:String)

}