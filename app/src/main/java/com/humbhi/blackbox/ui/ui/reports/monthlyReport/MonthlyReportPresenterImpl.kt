package com.humbhi.blackbox.ui.ui.reports.monthlyReport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DistanceReportResponseModel
import com.humbhi.blackbox.ui.data.models.MonthlyDataReponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.reports.distanceReport.DistanceReportView
import java.net.SocketTimeoutException

class MonthlyReportPresenterImpl(
    private val mMonthlyReportView: MonthlyReportView,
    private val mDataManager: DataManager
): MonthlyReportPresenter {
    override fun hitMonthlyReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        CommandName: String,
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
            mMonthlyReportView.isNetworkConnected() -> {
                mMonthlyReportView.isShowLoading()
                mDataManager.apiCallMonthlyReport(
                    beginDate,
                    endDate,
                    bbid,
                    custid,
                    CommandName,
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
                            mMonthlyReportView.isHideLoading()

                            val getMonthlyReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                MonthlyDataReponseModel::class.java
                            )
                            mMonthlyReportView.getMonthlyReport(getMonthlyReport)
                        }

                        override fun onError(errorId: Any) {
                            mMonthlyReportView.isHideLoading()
                            mMonthlyReportView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mMonthlyReportView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mMonthlyReportView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mMonthlyReportView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mMonthlyReportView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mMonthlyReportView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }
}