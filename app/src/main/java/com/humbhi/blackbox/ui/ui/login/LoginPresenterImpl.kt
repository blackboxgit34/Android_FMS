package com.humbhi.blackbox.ui.ui.login

import android.net.DnsResolver
import android.os.Build
import com.google.gson.Gson
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.LoginResponse
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import org.chromium.net.NetworkException
import java.net.SocketTimeoutException

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
                        if (throwable is SocketTimeoutException) {
                            mLoginView.showErrorMessage("Connection time out, please try again")
                        }
                        else if (throwable is java.net.UnknownHostException) {
                            mLoginView.showErrorMessage("No internet available, please try again")
                        }
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (throwable is DnsResolver.DnsException) {
                                mLoginView.showErrorMessage("Connectivity issue")
                            }
                        } else {
                            mLoginView.showErrorMessage("Something went wrong")
                        }
                    }
                })
            }
        }
    }
}