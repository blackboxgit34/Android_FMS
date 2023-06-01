package com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.FuelTheftReponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class FuelTheftPresenterImpl(
    private val mFuelTheftView: FuelTheftView,
    private val mDataManager: DataManager
) : FuelTheftPresenter {
    override fun hitFuelTheftReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        type: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String
    ) {
        when {
            mFuelTheftView.isNetworkConnected() -> {
                mFuelTheftView.isShowLoading()
                mDataManager.apiCallFuelTheftReportApi(
                    beginDate,
                    endDate,
                    CustId,
                    bbid,
                    downloadType,
                    reportName,
                    sEcho,
                    type,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mFuelTheftView.isHideLoading()

                            val getFuelTheftResponse = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                FuelTheftReponseModel::class.java
                            )
                            mFuelTheftView.getFuelTheftData(getFuelTheftResponse)
                        }

                        override fun onError(errorId: Any) {
                            mFuelTheftView.isHideLoading()
                            mFuelTheftView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mFuelTheftView.isHideLoading()
                            mFuelTheftView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }

}