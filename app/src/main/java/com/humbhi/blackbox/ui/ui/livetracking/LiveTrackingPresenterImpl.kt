package com.humbhi.blackbox.ui.ui.livetracking

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.LiveTrackingResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import java.net.SocketTimeoutException

class LiveTrackingPresenterImpl(
    private val mLiveTrackView: LiveTrackingView,
    private val mDataManager: DataManager
) : LiveTrackingPresenter {
    override fun hitLiveTrackingVehicleDataApi(
        custid: String,
        beginDate: String,
        endDate: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            mLiveTrackView.isNetworkConnected() -> {
                mLiveTrackView.isShowLoading()
                mDataManager.apiCallToGetLiveTracking(
                    custid,
                    StatusCode,
                    sEcho,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mLiveTrackView.isHideLoading()
                            val getVehicleLiveStatusResponse = Gson().fromJson(
                                Gson().toJson(commonResponse), LiveTrackingResponse::class.java
                            )
                            mLiveTrackView.getLiveVehicleDetail(getVehicleLiveStatusResponse)
                        }

                        override fun onError(errorId: Any) {
                            mLiveTrackView.isHideLoading()
                            mLiveTrackView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mLiveTrackView.isHideLoading()
                            if (throwable is SocketTimeoutException) {
                                mLiveTrackView.showErrorMessage("Connection time out, please try again")
                            } else if (throwable is java.net.UnknownHostException) {
                                mLiveTrackView.showErrorMessage("No internet available, please try again")
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (throwable is DnsResolver.DnsException) {
                                    mLiveTrackView.showErrorMessage("Connectivity issue")
                                }
                            } else {
                                mLiveTrackView.showErrorMessage("Something went wrong")
                            }
                        }

                    })
            }
        }
    }
}