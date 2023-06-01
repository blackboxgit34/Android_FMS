package com.humbhi.blackbox.ui.ui.login

import com.humbhi.blackbox.ui.data.models.LoginResponse

interface LoginView {

    fun getLoginUserData(loginResponseModel:LoginResponse)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)

}