package com.humbhi.blackbox.ui.ui.reports.overspeedReport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.OverspeedResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class OverspeedReportPresenterImpl(
    private val mOverspeedReportView: OverspeedReportView,
    private val mDataManager: DataManager
) : OverSpeedReportPresenter {
    override fun hitOverSpeedReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        mode: String,
        custid: String,
        downloadtype: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String
    ) {
        when {
            mOverspeedReportView.isNetworkConnected() -> {
                mOverspeedReportView.isShowLoading()
                mDataManager.apiCallOverSpeedReport(
                    beginDate,
                    endDate,
                    bbid,
                    mode,
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
                            mOverspeedReportView.isHideLoading()

                            val getOverSpeedReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                OverspeedResponseModel::class.java
                            )
                            mOverspeedReportView.getOverSpeedResponseData(getOverSpeedReport)
                        }

                        override fun onError(errorId: Any) {
                            mOverspeedReportView.isHideLoading()
                            mOverspeedReportView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mOverspeedReportView.isHideLoading()
                            mOverspeedReportView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}