package com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport

import com.humbhi.blackbox.ui.data.models.ConsolidateVoilationModel
import com.humbhi.blackbox.ui.data.models.HarshBreakingModel

interface ConsolidateViolationView {

    fun getConsolidationViolationData(consolidateVoilationModel: ConsolidateVoilationModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}