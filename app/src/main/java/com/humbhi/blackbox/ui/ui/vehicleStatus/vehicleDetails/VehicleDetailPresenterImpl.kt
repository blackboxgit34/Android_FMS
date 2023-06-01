package com.humbhi.blackbox.ui.ui.vehicleStatus.vehicleDetails

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.VehicleDetailResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class VehicleDetailPresenterImpl(
    private val mVehicleDetailView: VehicleDetailView,
    private val mDataManager: DataManager
) : VehicleDetailPresenter {
    override fun hitVehicleDetailApi(
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
            mVehicleDetailView.isNetworkConnected() -> {
                mVehicleDetailView.isShowLoading()
                mDataManager.apiCallToGetVehicleDetails(
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
                            mVehicleDetailView.isHideLoading()

                            val getVehicleDetailResponse = Gson().fromJson(
                                Gson().toJson(commonResponse), VehicleDetailResponse::class.java
                            )
                            mVehicleDetailView.getVehicleDetails(getVehicleDetailResponse)
                        }

                        override fun onError(errorId: Any) {
                            mVehicleDetailView.isHideLoading()
                            mVehicleDetailView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mVehicleDetailView.isHideLoading()
                            mVehicleDetailView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }
}