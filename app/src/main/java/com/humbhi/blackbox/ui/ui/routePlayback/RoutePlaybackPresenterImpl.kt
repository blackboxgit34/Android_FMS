package com.humbhi.blackbox.ui.ui.routePlayback

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.DrivingBehaviourRouteDataModel
import com.humbhi.blackbox.ui.data.models.RoutePlaybackResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class RoutePlaybackPresenterImpl(
    private val mRoutePlaybackView: RoutePlaybackView,
    private val mDataManager: DataManager
) : RoutePlaybackPresenter {
    override fun hitRoutePlaybackApi(tableName: String, fromDate: String, toDate: String) {
        when {
            mRoutePlaybackView.isNetworkConnected() -> {
                mRoutePlaybackView.isShowLoading()
                mDataManager.apiCallRoutePlayback(
                    tableName,
                    fromDate,
                    toDate,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mRoutePlaybackView.isHideLoading()

                            val getRoutePlaybackData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                RoutePlaybackResponseModel::class.java
                            )
                            mRoutePlaybackView.getRoutePlaybackResponse(getRoutePlaybackData)
                        }

                        override fun onError(errorId: Any) {
                            mRoutePlaybackView.isHideLoading()
                            mRoutePlaybackView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mRoutePlaybackView.isHideLoading()
                            mRoutePlaybackView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }

    override fun hitDrivingBehaviourRouteAPI(tableName: String, fromDate: String, toDate: String) {
        when {
            mRoutePlaybackView.isNetworkConnected() -> {
                mRoutePlaybackView.isShowLoading()
                mDataManager.apiCallRoutePlaybackForDrivingBehaviour(
                    tableName,
                    fromDate,
                    toDate,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mRoutePlaybackView.isHideLoading()

                            val getRoutePlaybackData = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                DrivingBehaviourRouteDataModel::class.java
                            )
                            mRoutePlaybackView.getDrivingBehavourRouteData(getRoutePlaybackData)
                        }

                        override fun onError(errorId: Any) {
                            mRoutePlaybackView.isHideLoading()
                            mRoutePlaybackView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mRoutePlaybackView.isHideLoading()
                            mRoutePlaybackView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
            }
        }
    }
}