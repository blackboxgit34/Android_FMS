package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.FuelRodDisconnectionResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import org.chromium.net.NetworkException
import java.net.SocketTimeoutException

class FuelRodPresenterImpl(
    private val mFuelRodView: FuelRodView,
    private val mDataManager: DataManager
) : FuelRodPresenter {
    override fun hitFuelRodDisconnectionApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: Int
    ) {
        when {
            mFuelRodView.isNetworkConnected() -> {
                mFuelRodView.isShowLoading()
                mDataManager.apiCallFuelRodDisconnectionApi(
                    beginDate,
                    endDate,
                    CustId,
                    bbid,
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
                            mFuelRodView.isHideLoading()

                            val getFuelRodDisconnections = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                FuelRodDisconnectionResponseModel::class.java
                            )
                            mFuelRodView.getFuelRodDisconnectionData(getFuelRodDisconnections)
                        }


                        override fun onError(errorId: Any) {
                            mFuelRodView.isHideLoading()
                            mFuelRodView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mFuelRodView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mFuelRodView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mFuelRodView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mFuelRodView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mFuelRodView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }

}