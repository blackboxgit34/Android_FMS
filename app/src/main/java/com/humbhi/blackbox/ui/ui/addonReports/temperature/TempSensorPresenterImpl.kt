package com.humbhi.blackbox.ui.ui.addonReports.temperature

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.TemperatureReportResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class TempSensorPresenterImpl(
    private val mTempSensorView: TempSensorView,
    private val mDataManager: DataManager
) : TempSensorPresenter {
    override fun hitTemperatureReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        CommandName: String,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String
    ) {
        when {
            mTempSensorView.isNetworkConnected() -> {
                mTempSensorView.isShowLoading()
                mDataManager.apiCallTempSensorReportApi(
                    beginDate,
                    endDate,
                    bbid,
                    custid,
                    sEcho,
                    iDisplayStart,
                    downloadType,
                    reportName,
                    CommandName,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mTempSensorView.isHideLoading()

                            val getTemperatureReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                TemperatureReportResponse::class.java
                            )
                            mTempSensorView.getTempReportData(getTemperatureReport)
                        }

                        override fun onError(errorId: Any) {
                            mTempSensorView.isHideLoading()
                            mTempSensorView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mTempSensorView.isHideLoading()
                            mTempSensorView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}