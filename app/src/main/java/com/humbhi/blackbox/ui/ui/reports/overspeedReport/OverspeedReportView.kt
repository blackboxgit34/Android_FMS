package com.humbhi.blackbox.ui.ui.reports.overspeedReport

import com.humbhi.blackbox.ui.data.models.OverspeedResponseModel

interface OverspeedReportView {

    fun getOverSpeedResponseData(overSpeedResponseModel: OverspeedResponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)

}