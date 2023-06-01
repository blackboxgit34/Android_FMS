package com.humbhi.blackbox.ui.ui.drivingBehaviour.driverClassificationReports

import com.humbhi.blackbox.ui.data.models.ConsolidateVoilationModel

interface SafeDriversView {

    fun getConsolidationViolationData(consolidateVoilationModel: ConsolidateVoilationModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}