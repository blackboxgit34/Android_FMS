package com.humbhi.blackbox.ui.ui.reports.distanceReport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DistanceReportResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class DistanceReportPresenterImpl(
    private val mDistanceReportView: DistanceReportView,
    private val mDataManager: DataManager
) : DistanceReportPresenter {
    override fun hitDistanceReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String
    ) {
        when {
            mDistanceReportView.isNetworkConnected() -> {
                mDistanceReportView.isShowLoading()
                mDataManager.apiCallDistanceReport(
                    beginDate,
                    endDate,
                    bbid,
                    custid,
                    downloadtype,
                    sEcho,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    reportName,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mDistanceReportView.isHideLoading()

                            val getDistanceReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                DistanceReportResponseModel::class.java
                            )
                            mDistanceReportView.getDistanceReport(getDistanceReport)
                        }

                        override fun onError(errorId: Any) {
                            mDistanceReportView.isHideLoading()
                            mDistanceReportView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mDistanceReportView.isHideLoading()
                            mDistanceReportView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}