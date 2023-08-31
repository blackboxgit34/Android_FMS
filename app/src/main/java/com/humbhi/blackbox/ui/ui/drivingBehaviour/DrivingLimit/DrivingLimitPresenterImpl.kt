package com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit


import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DrivingLimitModel
import com.humbhi.blackbox.ui.data.models.OverspeedResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.OverSpeedReportPresenter
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.OverspeedReportView
import java.net.SocketTimeoutException

class DrivingLimitPresenterImpl(
    private val drivingLimitView: DrivingLimitView,
    private val mDataManager: DataManager
)  : DrivingLimitPresenter{
    override fun getDrivingLimitData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadtype: String,
        reportName: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            drivingLimitView.isNetworkConnected() -> {
                drivingLimitView.isShowLoading()
                mDataManager.getCallDrivingLimitData(
                    beginDate,
                    endDate,
                    custid,
                    downloadtype,
                    reportName,
                    sEcho,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            drivingLimitView.isHideLoading()

                            val getOverSpeedReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                DrivingLimitModel::class.java
                            )
                            drivingLimitView.getDrivingLimitData(getOverSpeedReport)
                        }

                        override fun onError(errorId: Any) {
                            drivingLimitView.isHideLoading()
                            drivingLimitView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            drivingLimitView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                drivingLimitView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                drivingLimitView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    drivingLimitView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                drivingLimitView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }

}