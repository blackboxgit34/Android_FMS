package com.humbhi.blackbox.ui.ui.drivingBehaviour.driverVoilations

import com.humbhi.blackbox.ui.data.models.DriverBehaviourDataModel
import com.humbhi.blackbox.ui.data.models.ExcellentDriverDataModel
import com.humbhi.blackbox.ui.data.models.RiskyDriverModel
import com.humbhi.blackbox.ui.data.models.TotalVoilationDataModel

interface TotalVoilationView {

    fun getDriverTotalVoilationData(totalVoilationDataModel: TotalVoilationDataModel)

    fun getExcellentDriversData(excellentDriverDataModel: ExcellentDriverDataModel)

    fun getRiskyDriverData(riskyDriverModel: RiskyDriverModel)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)
}