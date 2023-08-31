package com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.ConsolidateVoilationModel
import com.humbhi.blackbox.ui.data.models.HarshBreakingModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport.HarshBreakingPresenter
import com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport.HarshBreakingView
import java.net.SocketTimeoutException

class ConsolidateViolationPresenterImpl(
    private val mConsolidateViolationView: ConsolidateViolationView,
    private val mDataManager: DataManager
) : ConsolidateViolationPresenter {
    override fun hitConsolidationViolationApi(
        fDate: String,
        tdate: String,
        CustId: String,
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
            mConsolidateViolationView.isNetworkConnected() -> {
                mConsolidateViolationView.isShowLoading()
                mDataManager.apiCallConsolidateVoilation(
                    fDate,
                    tdate,
                    CustId,
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
                            mConsolidateViolationView.isHideLoading()

                            val getVoilationData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                ConsolidateVoilationModel::class.java
                            )
                            mConsolidateViolationView.getConsolidationViolationData(getVoilationData)
                        }

                        override fun onError(errorId: Any) {
                            mConsolidateViolationView.isHideLoading()
                            mConsolidateViolationView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mConsolidateViolationView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mConsolidateViolationView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mConsolidateViolationView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mConsolidateViolationView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mConsolidateViolationView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }

}