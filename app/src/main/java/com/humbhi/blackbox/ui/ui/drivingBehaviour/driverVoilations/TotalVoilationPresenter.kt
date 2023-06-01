package com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations

interface TotalVoilationPresenter {

    fun callDriverTotalVoilationBehaviourApi(custid:String)

    fun callExcellentDriverApi(custid:String,rankType:String)

    fun callRiskyDriverApi(custid:String,rankType:String)
}