package com.humbhi.blackbox.ui.ui.drivingBehaviour.rashTurn

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.RashTurnDataModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class RashTurnPresenterImpl(
    private val mRashTurnView: RashTurnView,
    private val mDataManager: DataManager
) : RashTurnPresenter {

    override fun hitRashTurnReportApi(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String
    ) {
        when {
            mRashTurnView.isNetworkConnected() -> {
                mRashTurnView.isShowLoading()
                mDataManager.apiCallRashTurnData(
                    beginDate,
                    endDate,
                    custid,
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
                            mRashTurnView.isHideLoading()

                            val getRashTurnsData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                RashTurnDataModel::class.java
                            )
                            mRashTurnView.getRashTurnData(getRashTurnsData)
                        }

                        override fun onError(errorId: Any) {
                            mRashTurnView.isHideLoading()
                            mRashTurnView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mRashTurnView.isHideLoading()
                            mRashTurnView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}