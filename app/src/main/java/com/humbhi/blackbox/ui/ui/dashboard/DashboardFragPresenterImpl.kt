package com.humbhi.blackbox.ui.ui.dashboard

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.*
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class DashboardFragPresenterImpl(
    private val mDashboardFragView: DashboardFragView,
    private val mDataManager: DataManager
) : DashboardFragPresenter{

    override fun callGetVehiclesApi(custid: String) {
            when{
                mDashboardFragView.isNetworkConnected()->{
                    mDashboardFragView.isShowLoading()
                    mDataManager.apiCallToGetVehicleCount(custid,object :ApiHelper.ApiListener{
                        override fun onSuccess(commonResponse: Any) {
                            mDashboardFragView.isHideLoading()
                            val getVehicleCountResponse = Gson().fromJson(
                                Gson().toJson(commonResponse), VehicleCountData::class.java
                            )
                            mDashboardFragView.getVehicleCountData(getVehicleCountResponse)
                        }

                        override fun onError(errorId: Any) {
                            mDashboardFragView.isHideLoading()
                            mDashboardFragView.showErrorMessage("Network Issue, Please try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mDashboardFragView.isHideLoading()
                            mDashboardFragView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                        }

                    })
                }
            }
    }

    override fun callFleetUtilizationChart(custid: String) {
        when{
            mDashboardFragView.isNetworkConnected()->{
                mDashboardFragView.isShowLoading()
                mDataManager.apiCallToGetFleetUtilization(custid,object :ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mDashboardFragView.isHideLoading()
                        val getFleetUtilizationChart = Gson().fromJson(
                            Gson().toJson(commonResponse), FleetUtilizationResponse::class.java
                        )
                        mDashboardFragView.getFleetUtilizationData(getFleetUtilizationChart)
                    }

                    override fun onError(errorId: Any) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }
                })
            }
        }
    }

    override fun callFuelAnalysisChartAPI(custid: String) {
        when{
            mDashboardFragView.isNetworkConnected()->{
                mDashboardFragView.isShowLoading()
                mDataManager.apiToCallFuelAnalysis(custid,object :ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mDashboardFragView.isHideLoading()
                        val getFuelGraph = Gson().fromJson(
                            Gson().toJson(commonResponse), VehicleMielageResponse::class.java
                        )
                        mDashboardFragView.getVehicleFuelGraph(getFuelGraph)
                    }

                    override fun onError(errorId: Any) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }
                })
            }
        }
    }

    override fun callSpeedAnalysisAPI(custid: String) {
        when{
            mDashboardFragView.isNetworkConnected()->{
                mDashboardFragView.isShowLoading()
                mDataManager.apiCalltoGetSpeedAnalysis(custid,object :ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mDashboardFragView.isHideLoading()
                        val getSpeedAnalysis = Gson().fromJson(
                            Gson().toJson(commonResponse), SpeedAnalysisResponse::class.java
                        )

                        mDashboardFragView.getSpeedAnalysisCount(getSpeedAnalysis)
                    }

                    override fun onError(errorId: Any) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }
                })
            }
        }
    }

    override fun callDriverBehaviourApi(custid: String) {
        when{
            mDashboardFragView.isNetworkConnected()->{
                mDashboardFragView.isShowLoading()
                mDataManager.apiCalltoGetDriverBehaviour(custid,object :ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mDashboardFragView.isHideLoading()
                        val getDriverBehaviour = Gson().fromJson(
                            Gson().toJson(commonResponse), DriverBehaviourDataModel::class.java
                        )
                            mDashboardFragView.getDriverBehaviourData(getDriverBehaviour)
                    }

                    override fun onError(errorId: Any) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mDashboardFragView.isHideLoading()
                        mDashboardFragView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }
                })
            }
        }
    }

}