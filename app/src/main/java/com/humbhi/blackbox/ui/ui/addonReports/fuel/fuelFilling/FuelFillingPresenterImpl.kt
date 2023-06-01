package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelFilling

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class FuelFillingPresenterImpl(
    private val mFuelFillingView: FuelFillingView,
    private val mDataManager: DataManager
) : FuelFillingPresenter {
    override fun hitFuelFillingReportApi(
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
            mFuelFillingView.isNetworkConnected() -> {
                mFuelFillingView.isShowLoading()
                mDataManager.apiCallFuelFillingReportApi(
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
                            mFuelFillingView.isHideLoading()

                            val getFuelFillingResponse = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                FuelFillingResponseData::class.java
                            )
                            mFuelFillingView.getFuelFillingData(getFuelFillingResponse)
                        }

                        override fun onError(errorId: Any) {
                            mFuelFillingView.isHideLoading()
                            mFuelFillingView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mFuelFillingView.isHideLoading()
                            mFuelFillingView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

}