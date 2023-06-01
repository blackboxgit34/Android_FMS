package com.humbhi.blackbox.ui.ui.drivingBehaviour.rashTurn

import com.humbhi.blackbox.ui.data.models.RashTurnDataModel

interface RashTurnView {

    fun getRashTurnData(rashTurnDataModel: RashTurnDataModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}