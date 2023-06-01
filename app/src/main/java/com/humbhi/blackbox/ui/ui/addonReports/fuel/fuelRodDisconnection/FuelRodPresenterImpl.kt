package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.FuelRodDisconnectionResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

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
                            mFuelRodView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

}