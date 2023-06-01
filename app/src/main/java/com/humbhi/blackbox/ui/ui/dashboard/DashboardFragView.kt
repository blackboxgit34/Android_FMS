package com.humbhi.blackbox.ui.ui.dashboard

import com.humbhi.blackbox.ui.data.models.*

interface DashboardFragView {

    fun getVehicleCountData(vehicleCountDataModel:VehicleCountData)

    fun getFleetUtilizationData(fleetUtilizationResponse: FleetUtilizationResponse)

    fun getVehicleFuelGraph(vehicleMielageResponse: VehicleMielageResponse)

    fun getSpeedAnalysisCount(speedAnalysisResponse: SpeedAnalysisResponse)

    fun getDriverBehaviourData(driverBehaviourDashModel: DriverBehaviourDataModel)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)

}