package com.humbhi.blackbox.ui.ui.reports.stoppagereport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.StoppageReportResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import java.net.SocketTimeoutException

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
                                StoppageReportResponse::class.java
                            )
                            mStoppageReportView.getStoppageReportResponse(getStoppageReport)
                        }

                        override fun onError(errorId: Any) {
                            mStoppageReportView.isHideLoading()
                            mStoppageReportView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mStoppageReportView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mStoppageReportView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mStoppageReportView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mStoppageReportView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mStoppageReportView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }

} 