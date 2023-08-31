package com.humbhi.blackbox.ui.ui.vehicleStatus

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import java.net.SocketTimeoutException

class VehcileStatusPresenterImpl(
    private val mVehicleStatusView: VehicleStatusView,
    private val mDataManager: DataManager
) : VehicleStatusPresenter {
    override fun hitLiveStatusApi(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            mVehicleStatusView.isNetworkConnected() -> {
                mVehicleStatusView.isShowLoading()
                mDataManager.apiCallToGetLiveTracking(custid,StatusCode,sEcho,iDisplayStart,iDisplayLength,sSearch,iSortCol_0,sSortDir_0, object : ApiHelper.ApiListener {
                    override fun onSuccess(commonResponse: Any) {
                        mVehicleStatusView.isHideLoading()
                        val getVehicleStatusResponse = Gson().fromJson(
                            Gson().toJson(commonResponse), VehicleLiveStatusModel::class.java
                        )
                        mVehicleStatusView.getVehicleStatus(getVehicleStatusResponse)
                    }

                    override fun onError(errorId: Any) {
                        mVehicleStatusView.isHideLoading()
                        mVehicleStatusView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mVehicleStatusView.isHideLoading()
                        if (throwable is SocketTimeoutException) {
                            mVehicleStatusView.showErrorMessage("Connection time out, please try again")
                        } else if (throwable is java.net.UnknownHostException) {
                            mVehicleStatusView.showErrorMessage("No internet available, please try again")
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (throwable is DnsResolver.DnsException) {
                                mVehicleStatusView.showErrorMessage("Connectivity issue")
                            }
                        } else {
                            mVehicleStatusView.showErrorMessage("Something went wrong")
                        }
                    }
                })
            }
        }
    }
}