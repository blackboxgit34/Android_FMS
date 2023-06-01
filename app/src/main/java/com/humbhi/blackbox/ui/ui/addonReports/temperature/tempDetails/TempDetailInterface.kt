package com.humbhi.blackbox.ui.ui.addonReports.temperature.tempDetails

import com.humbhi.blackbox.ui.data.models.TempDetailResponse

interface TempDetailInterface {

    fun getTempReportDetailData(tempDetailResponse: TempDetailResponse)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}