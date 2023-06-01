package com.humbhi.blackbox.ui.ui.drivingBehaviour.NoDrivingReport

import com.humbhi.blackbox.ui.data.models.DrivingLimitModel
import com.humbhi.blackbox.ui.data.models.NoDrivingModel

interface NoDrivingView {
    fun getNoDrivingLimitData(noDrivingResponseModel: NoDrivingModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}