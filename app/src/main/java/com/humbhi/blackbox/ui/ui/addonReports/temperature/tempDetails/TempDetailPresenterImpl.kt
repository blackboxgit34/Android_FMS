package com.humbhi.blackbox.ui.ui.addonReports.temperature.tempDetails

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.TempDetailResponse
import com.humbhi.blackbox.ui.data.models.TemperatureReportResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.ui.addonReports.temperature.TempSensorView

class TempDetailPresenterImpl(
    private val mTempDetailInterface: TempDetailInterface,
    private val mDataManager: DataManager
) : TempDetailPresenter{
    override fun hitTemperatureDetailReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        timeInterval: String,
        iDisplayLength: Int,
        sSearch: String,
        offset: Int,
        iSortCol_0: Int,
        sSortDir_0: String
    ) {
        when {
            mTempDetailInterface.isNetworkConnected() -> {
                mTempDetailInterface.isShowLoading()
                mDataManager.apiCallTempSensorDetailReportApi(
                    beginDate,
                    endDate,
                    bbid,
                    custid,
                    sEcho,
                    iDisplayStart,
                    downloadType,
                    reportName,
                    timeInterval,
                    iDisplayLength,
                    sSearch,
                    offset,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mTempDetailInterface.isHideLoading()

                            val getTemperatureDetailReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                TempDetailResponse::class.java
                            )
                            mTempDetailInterface.getTempReportDetailData(getTemperatureDetailReport)
                        }

                        override fun onError(errorId: Any) {
                            mTempDetailInterface.isHideLoading()
                            mTempDetailInterface.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mTempDetailInterface.isHideLoading()
                            mTempDetailInterface.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }
}