package com.humbhi.blackbox.ui.ui.addonReports.workingHour

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.WorkingHourDataResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class WorkingHourPresenterImpl(
    private val mWorkingHourView: WorkingHourView,
    private val mDataManager: DataManager
) : WorkingHourPresenter {
    override fun hitWorkingHourReportApi(
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
        sSortDir_0: String
    ) {
        when {
            mWorkingHourView.isNetworkConnected() -> {
                mWorkingHourView.isShowLoading()
                mDataManager.apiCallWorkingHourReportApi(
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
                            mWorkingHourView.isHideLoading()

                            val getWorkHourReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                WorkingHourDataResponse::class.java
                            )
                            mWorkingHourView.getWorkHourReportData(getWorkHourReport)
                        }

                        override fun onError(errorId: Any) {
                            mWorkingHourView.isHideLoading()
                            mWorkingHourView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mWorkingHourView.isHideLoading()
                            mWorkingHourView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }


}