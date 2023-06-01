package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverToDriverComparison

import com.humbhi.blackbox.ui.data.network.api.ApiHelper

interface DrToDrPresenter {

    fun apiCallDriverToDriverComparisonReport(
        custid: String,
        stMonth : String,
        drid1 : String,
        drid2 : String,
        bbid1 : String,
        bbid2 : String,
        drName1 : String,
        drName2 : String
    )

}


