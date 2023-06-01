package com.humbhi.blackbox.ui.ui.reports.dailyreport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DailyReportResponse
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusView

class DailyReportPresenterImpl(
    private val mDailyReportView: DailyReportView,
    private val mDataManager: DataManager
):DailyReportPresenter {
    override fun getDailyReportDataApi(
        fromDate: String,
        toDate: String,
        custid: String,
        lowerbound: Int,
        upperbound: Int,
        searchVehName: String
    ) {
        when {
            mDailyReportView.isNetworkConnected() -> {
                mDailyReportView.isShowLoading()
                mDataManager.apiCallDailyReport(fromDate,toDate,custid,lowerbound,upperbound,searchVehName,object : ApiHelper.ApiListener {
                    override fun onSuccess(commonResponse: Any) {
                        mDailyReportView.isHideLoading()

                        val getDailyReportData = Gson().fromJson(
                            Gson().toJson(commonResponse), DailyReportResponse::class.java
                        )
                        mDailyReportView.getDailyReportResponse(getDailyReportData)
                    }

                    override fun onError(errorId: Any) {
                        mDailyReportView.isHideLoading()
                        mDailyReportView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mDailyReportView.isHideLoading()
                        mDailyReportView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }

                })
            }
        }
    }
}