package com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit

import com.humbhi.blackbox.ui.data.models.DrivingLimitModel

interface DrivingLimitView {

    fun getDrivingLimitData(drivingResponseModel: DrivingLimitModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)

}