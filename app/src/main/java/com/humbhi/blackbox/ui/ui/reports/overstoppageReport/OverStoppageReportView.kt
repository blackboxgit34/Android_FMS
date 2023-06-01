package com.humbhi.blackbox.ui.ui.reports.overstoppageReport

import com.humbhi.blackbox.ui.data.models.OverStoppageResponseModel

interface OverStoppageReportView {

    fun getOverStoppageReportResponse(overStoppageReportResponseModel: OverStoppageResponseModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}