package com.humbhi.blackbox.ui.data.db

import io.paperdb.Paper

interface DBHelper {

    fun setFCMToken(fcmToken:String)

    fun getFCMToken():String

    fun setCustId(custId:String)

    fun getCustIdFromDB():String


    fun setCustName(custName:String)

    fun getCustNameFromDB():String

    fun clearLocalAppData()
}