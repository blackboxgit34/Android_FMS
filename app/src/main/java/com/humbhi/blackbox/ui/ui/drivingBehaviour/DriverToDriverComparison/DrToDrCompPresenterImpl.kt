package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverToDriverComparison

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DriverToDriverCompModel
import com.humbhi.blackbox.ui.data.models.DrivingLimitModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class DrToDrCompPresenterImpl (
    private val drToDrComparisonView: DrToDrComparisonView,
    private val mDataManager: DataManager
    ) : DrToDrPresenter{
    override fun apiCallDriverToDriverComparisonReport(
        custid: String,
        stMonth: String,
        drid1: String,
        drid2: String,
        bbid1: String,
        bbid2: String,
        drName1: String,
        drName2: String
    ) {
        when {
            drToDrComparisonView.isNetworkConnected() -> {
                drToDrComparisonView.isShowLoading()
                mDataManager.apiCallDriverToDriverComparisonReport(
                    custid,
                    stMonth,
                    drid1,
                    drid2,
                    bbid1,
                    bbid2,
                    drName1,
                    drName2,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            drToDrComparisonView.isHideLoading()

                            val getOverSpeedReport = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                DriverToDriverCompModel::class.java
                            )
                            drToDrComparisonView.getDrivingLimitData(getOverSpeedReport)
                        }

                        override fun onError(errorId: Any) {
                            drToDrComparisonView.isHideLoading()
                            drToDrComparisonView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            drToDrComparisonView.isHideLoading()
                            drToDrComparisonView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

}