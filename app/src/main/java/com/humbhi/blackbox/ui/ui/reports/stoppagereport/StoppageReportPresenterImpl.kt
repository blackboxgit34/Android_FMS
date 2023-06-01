package com.humbhi.blackbox.ui.ui.reports.stoppagereport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.StoppageReportResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class StoppageReportPresenterImpl(
    private val mStoppageReportView: StoppageReportView,
    private val mDataManager: DataManager
) : StoppageReportPresenter {
    override fun hitStoppageReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        interval: String,
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
            mStoppageReportView.isNetworkConnected() -> {
                mStoppageReportView.isShowLoading()
                mDataManager.apiCallStoppageReport(
                    beginDate,
                    endDate,
                    bbid,
                    custid,
                    interval,
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
                            mStoppageReportView.isHideLoading()

                            val getStoppageReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                StoppageReportResponseModel::class.java
                            )
                            mStoppageReportView.getStoppageReportResponse(getStoppageReport)
                        }

                        override fun onError(errorId: Any) {
                            mStoppageReportView.isHideLoading()
                            mStoppageReportView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mStoppageReportView.isHideLoading()
                            mStoppageReportView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }

} 