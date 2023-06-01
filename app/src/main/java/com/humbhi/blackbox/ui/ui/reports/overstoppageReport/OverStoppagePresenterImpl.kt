package com.humbhi.blackbox.ui.ui.reports.overstoppageReport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.OverStoppageResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class OverStoppagePresenterImpl(
    private val mOverStoppageReportView: OverStoppageReportView,
    private val mDataManager: DataManager
) : OverStoppagePresenter {
    override fun hitOverStoppageReportApi(
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
            mOverStoppageReportView.isNetworkConnected() -> {
                mOverStoppageReportView.isShowLoading()
                mDataManager.apiCallOverStoppageReport(
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
                            mOverStoppageReportView.isHideLoading()

                            val getOverStoppageReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                OverStoppageResponseModel::class.java
                            )
                            mOverStoppageReportView.getOverStoppageReportResponse(
                               getOverStoppageReport
                            )
                        }

                        override fun onError(errorId: Any) {
                            mOverStoppageReportView.isHideLoading()
                            mOverStoppageReportView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mOverStoppageReportView.isHideLoading()
                            mOverStoppageReportView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }


}