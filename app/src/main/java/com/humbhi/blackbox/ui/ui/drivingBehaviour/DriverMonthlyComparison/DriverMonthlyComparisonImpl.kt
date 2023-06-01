package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverMonthlyComparison

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DriverMonthlyResponse
import com.humbhi.blackbox.ui.data.models.DrivingLimitModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper


class DriverMonthlyComparisonImpl(
    private val driverMonthlyComparisonView: DriverMonthlyComparisonView,
    private val mDataManager: DataManager
) : DriverMonthlyComparisonPresenter{
    override fun getMonthlyComparison(
        custid: String,
        stMonth: String,
        edMonth: String,
        drid: String,
        bbid: String
    ) {
        when {
            driverMonthlyComparisonView.isNetworkConnected() -> {
                driverMonthlyComparisonView.isShowLoading()
                mDataManager.apiCallMonthlyComparisonReport(
                    custid,
                    stMonth,
                    edMonth,
                    drid,
                    bbid,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            driverMonthlyComparisonView.isHideLoading()

                            val getOverSpeedReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                DriverMonthlyResponse::class.java
                            )
                            driverMonthlyComparisonView.getMonthlyComparison(getOverSpeedReport)
                        }

                        override fun onError(errorId: Any) {
                            driverMonthlyComparisonView.isHideLoading()
                            driverMonthlyComparisonView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            driverMonthlyComparisonView.isHideLoading()
                            driverMonthlyComparisonView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

}