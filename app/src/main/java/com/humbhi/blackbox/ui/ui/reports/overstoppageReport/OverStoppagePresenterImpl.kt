package com.humbhi.blackbox.ui.ui.reports.overstoppageReport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.OverStoppageResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import java.net.SocketTimeoutException

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
                            if (throwable is SocketTimeoutException) {
                                mOverStoppageReportView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mOverStoppageReportView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mOverStoppageReportView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mOverStoppageReportView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }


}