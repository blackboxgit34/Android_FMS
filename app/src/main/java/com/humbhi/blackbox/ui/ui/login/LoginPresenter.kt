package com.humbhi.blackbox.ui.ui.login

interface LoginPresenter {

    fun callLoginApi(userID:String,password:String,deviceType:String,DeviceId:String,clearpass:String,token:String)
}