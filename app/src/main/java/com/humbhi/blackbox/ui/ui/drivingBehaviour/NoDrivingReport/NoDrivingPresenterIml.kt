package com.humbhi.blackbox.ui.ui.drivingBehaviour.NoDrivingReport

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.NoDrivingModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper


class NoDrivingPresenterIml(
    private val noDrivingLimitView: NoDrivingView,
    private val mDataManager: DataManager
)  : NoDrivingPresenter {
    override fun getNoDrivingLimitData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadtype: String,
        reportName: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            noDrivingLimitView.isNetworkConnected() -> {
                noDrivingLimitView.isShowLoading()
                mDataManager.getCallNoDrivingLimitData(
                    beginDate,
                    endDate,
                    custid,
                    downloadtype,
                    reportName,
                    sEcho,
                    iDisplayStart,
                    iDisplayLength,
                    sSearch,
                    iSortCol_0,
                    sSortDir_0,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            noDrivingLimitView.isHideLoading()

                            val getOverSpeedReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                NoDrivingModel::class.java
                            )
                            noDrivingLimitView.getNoDrivingLimitData(getOverSpeedReport)
                        }

                        override fun onError(errorId: Any) {
                            noDrivingLimitView.isHideLoading()
                            noDrivingLimitView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            noDrivingLimitView.isHideLoading()
                            noDrivingLimitView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

}