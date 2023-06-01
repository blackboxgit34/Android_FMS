package com.humbhi.blackbox.ui.ui.drivingBehaviour.harshBreakingReport

import com.humbhi.blackbox.ui.data.models.HarshBreakingModel

interface HarshBreakingView {

    fun getHarshBreakingData(harshBreakingModel: HarshBreakingModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)

}