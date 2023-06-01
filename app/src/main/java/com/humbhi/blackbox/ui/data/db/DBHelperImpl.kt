package com.humbhi.blackbox.ui.data.db

class DBHelperImpl : DBHelper {

    override fun setFCMToken(fcmToken: String) {
        CommonData.updateFcmToken(fcmToken)
    }

    override fun getFCMToken(): String {
        return CommonData.getFcmToken()
    }

    override fun setCustId(custId: String) {
        CommonData.setCustId(custId)
    }

    override fun getCustIdFromDB(): String {
        return CommonData.getCustIdFromDB()
    }

    override fun setCustName(custName: String) {
        CommonData.setCustName(custName)
    }

    override fun getCustNameFromDB(): String {
        return CommonData.getCustNameFromDB()
    }

    override fun clearLocalAppData() {
        CommonData.clearData()
    }
}