package com.humbhi.blackbox.ui.ui.login

import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.LoginResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class LoginPresenterImpl (
    private val mLoginView: LoginView,
    private val mDataManager:DataManager):LoginPresenter
{
    override fun callLoginApi(
        userID: String,
        password: String,
        deviceType: String,
        DeviceId: String,
        clearpass: String,
        token: String
    ) {
        when{
            mLoginView.isNetworkConnected()->{
                mLoginView.isShowLoading()
                mDataManager.apiCallToLogin(userID,password,deviceType,DeviceId,clearpass,token,object :ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mLoginView.isHideLoading()
                        val getLoginResponse = Gson().fromJson(
                            Gson().toJson(commonResponse),LoginResponse::class.java
                        )
                        mLoginView.getLoginUserData(getLoginResponse)
                    }

                    override fun onError(errorId: Any) {
                        mLoginView.isHideLoading()
                        mLoginView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mLoginView.isHideLoading()
                        mLoginView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }
                })
            }
        }
    }
}