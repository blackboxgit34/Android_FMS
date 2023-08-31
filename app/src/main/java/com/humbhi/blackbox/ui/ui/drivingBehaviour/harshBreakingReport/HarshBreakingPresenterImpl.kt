package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DistanceReportResponseModel
import com.humbhi.blackbox.ui.data.models.HarshBreakingModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.reports.distanceReport.DistanceReportView
import java.net.SocketTimeoutException

class HarshBreakingPresenterImpl(
    private val mHarshBreakingView: HarshBreakingView,
    private val mDataManager: DataManager
) :HarshBreakingPresenter{
    override fun hitHarshBreakReportApi(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            mHarshBreakingView.isNetworkConnected() -> {
                mHarshBreakingView.isShowLoading()
                mDataManager.apiCallHarshBreakingReport(
                    beginDate,
                    endDate,
                    custid,
                    downloadType,
                    reportName,
                    sEcho,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mHarshBreakingView.isHideLoading()

                            val getHarshBreakData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                HarshBreakingModel::class.java
                            )
                            mHarshBreakingView.getHarshBreakingData(getHarshBreakData)
                        }

                        override fun onError(errorId: Any) {
                            mHarshBreakingView.isHideLoading()
                            mHarshBreakingView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mHarshBreakingView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mHarshBreakingView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mHarshBreakingView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mHarshBreakingView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mHarshBreakingView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }
}