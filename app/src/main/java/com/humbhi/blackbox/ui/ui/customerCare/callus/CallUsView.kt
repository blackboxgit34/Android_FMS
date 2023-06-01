package com.humbhi.blackbox.ui.ui.customerCare.callus

import com.humbhi.blackbox.ui.data.models.CustomerCareDataModel

interface CallUsView {

    fun getCustomerCareNumberResponse(customerCareDataModel: CustomerCareDataModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}