package com.humbhi.blackbox.ui.ui.customerCare.callus

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.CustomerCareDataModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class CallUsPresenterImpl(
    private val mCallUsView: CallUsView,
    private val mDataManager: DataManager
) : CallUsPresenter {
    override fun hitCustomerCareComplaintApi(custID: String) {
        when {
            mCallUsView.isNetworkConnected() -> {
                mCallUsView.isShowLoading()
                mDataManager.apiCallCustomerCareCompaintApi(
                    custID,
                    object : ApiHelper.ApiListener {
                        override fun onSuccess(commonResponse: Any) {
                            mCallUsView.isHideLoading()

                            val getCompaintNumbers = Gson().fromJson(
                                Gson().toJson(commonResponse),
                                CustomerCareDataModel::class.java
                            )
                            mCallUsView.getCustomerCareNumberResponse(getCompaintNumbers)
                        }

                        override fun onError(errorId: Any) {
                            mCallUsView.isHideLoading()
                            mCallUsView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                        override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                            mCallUsView.isHideLoading()
                            mCallUsView.showErrorMessage("Something went wrong. Try after sometime")
                        }

                    })
            }
        }
    }
}