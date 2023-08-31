package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshAccelerationReport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.HarshAccelerationDataModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import java.net.SocketTimeoutException

class HarshAccelerationPresenterImpl(
    private val mHarshAccelerationView: HarshAccelerationView,
    private val mDataManager: DataManager
) : HarshAcclerationPresenter {
    override fun hitHarshAccelerationReportApi(
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
            mHarshAccelerationView.isNetworkConnected() -> {
                mHarshAccelerationView.isShowLoading()
                mDataManager.apiCallHarshAccelerationReportApi(
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
                            mHarshAccelerationView.isHideLoading()

                            val getHarshAccData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                HarshAccelerationDataModel::class.java
                            )
                            mHarshAccelerationView.getHarshAccelerationData(getHarshAccData)
                        }

                        override fun onError(errorId: Any) {
                            mHarshAccelerationView.isHideLoading()
                            mHarshAccelerationView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mHarshAccelerationView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mHarshAccelerationView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mHarshAccelerationView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mHarshAccelerationView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mHarshAccelerationView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }
}