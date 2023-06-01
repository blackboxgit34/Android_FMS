package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverMonthlyComparison

interface DriverMonthlyComparisonPresenter {
    fun getMonthlyComparison(
        custid: String,
        stMonth : String,
        edMonth : String,
        drid : String,
        bbid : String,
    )
}