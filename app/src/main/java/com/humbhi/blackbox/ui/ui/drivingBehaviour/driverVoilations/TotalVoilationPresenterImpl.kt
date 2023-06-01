package com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.ExcellentDriverDataModel
import com.humbhi.blackbox.ui.data.models.RiskyDriverModel
import com.humbhi.blackbox.ui.data.models.TotalVoilationDataModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class TotalVoilationPresenterImpl(
    private val mTotalVoilationView: TotalVoilationView,
    private val mDataManager: DataManager
) : TotalVoilationPresenter {
    override fun callDriverTotalVoilationBehaviourApi(custid: String) {
        when {
            mTotalVoilationView.isNetworkConnected() -> {
                mTotalVoilationView.isShowLoading()
                mDataManager.apiCallTotalVoilation(
                    custid,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mTotalVoilationView.isHideLoading()

                            val getTotalVoilationData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                TotalVoilationDataModel::class.java
                            )
                            mTotalVoilationView.getDriverTotalVoilationData(getTotalVoilationData)
                        }

                        override fun onError(errorId: Any) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

    override fun callExcellentDriverApi(custid: String, rankType: String) {
        when {
            mTotalVoilationView.isNetworkConnected() -> {
                mTotalVoilationView.isShowLoading()
                mDataManager.apiCallGetDriversRanking(
                    custid,
                    rankType,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mTotalVoilationView.isHideLoading()

                            val getExcellentDriverRankingData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                ExcellentDriverDataModel::class.java
                            )
                            mTotalVoilationView.getExcellentDriversData(getExcellentDriverRankingData)
                        }

                        override fun onError(errorId: Any) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

    override fun callRiskyDriverApi(custid: String, rankType: String) {
        when {
            mTotalVoilationView.isNetworkConnected() -> {
                mTotalVoilationView.isShowLoading()
                mDataManager.apiCallGetDriversRanking(
                    custid,
                    rankType,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mTotalVoilationView.isHideLoading()

                            val getRiskyDriverRankingData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                RiskyDriverModel::class.java
                            )
                            mTotalVoilationView.getRiskyDriverData(getRiskyDriverRankingData)
                        }

                        override fun onError(errorId: Any) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mTotalVoilationView.isHideLoading()
                            mTotalVoilationView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}