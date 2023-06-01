package com.humbhi.blackbox.ui.data.db

import io.paperdb.Paper

object CommonData {
    private var PAPER_DEVICE_TOKEN = "paper_device_token"
    private var PAPER_CUST_ID = "custId"
    private var PAPER_USERNAME = "name"
    private var DEVICE_ID = "deviceId"
    private var FIREBASE_TOKEN = "firebase_token"
    private var USERNAME = "user_name"
    private var PASSWORD = "password"
    private var FUELROD = "fuelrod"
    private var TEMPROD = "temprod"
    private var ENCRYPASSWORD = "encrypassword"
    private var ReadNotification = "ReadNotification"
    private var BANNER = "banner"

    fun updateFcmToken(token:String){
        Paper.book().write(FIREBASE_TOKEN,token)
    }

    fun getFcmToken():String{
        return Paper.book().read(PAPER_DEVICE_TOKEN)?:""
    }

    fun setCustId(custId:String){
        Paper.book().write(PAPER_CUST_ID,custId)
    }

    fun getCustIdFromDB():String{
        return Paper.book().read(PAPER_CUST_ID)?:""
    }
    fun setFuelRodStatus(fuelRod:Boolean){
        Paper.book().write(FUELROD,fuelRod)
    }

    fun getFuelRodStatus():Boolean{
        return Paper.book().read(FUELROD)?:false
    }
    fun setTempRodStatus(tempSensor:Boolean){
        Paper.book().write(TEMPROD,tempSensor)
    }

    fun getTempRodStatus():Boolean{
        return Paper.book().read(TEMPROD)?:false
    }
    fun setBanner(custId:String){
        Paper.book().write(BANNER,custId)
    }

    fun getBanner():String{
        return Paper.book().read(BANNER)?:""
    }
    fun setUserName(username:String){
        Paper.book().write(USERNAME,username)
    }

    fun getUserName():String{
        return Paper.book().read(USERNAME)?:""
    }

    fun setRead(username:String){
        Paper.book().write(ReadNotification,username)
    }

    fun getRead():String{
        return Paper.book().read(ReadNotification)?:""
    }
    fun setPassword(pswd:String){
        Paper.book().write(PASSWORD,pswd)
    }

    fun getPassword():String{
        return Paper.book().read(PASSWORD)?:""
    }

    fun setPasswordEncry(pswd:String){
        Paper.book().write(ENCRYPASSWORD,pswd)
    }

    fun getPasswordEncry():String{
        return Paper.book().read(ENCRYPASSWORD)?:""
    }

    fun setDeviceId(device_id : String){
        Paper.book().write(DEVICE_ID,device_id)
    }

    fun getDeviceId():String{
        return Paper.book().read(DEVICE_ID)?:""
    }

    fun setFirebaseToken(firebase_token:String){
        Paper.book().write(FIREBASE_TOKEN,firebase_token)
    }

    fun getFirebaseToken():String{
        return Paper.book().read(FIREBASE_TOKEN)?:""
    }

    fun setCustName(custName:String){
        Paper.book().write(PAPER_USERNAME,custName)
    }

    fun getCustNameFromDB():String{
        return Paper.book().read(PAPER_USERNAME)?:""
    }

    fun clearData(){
        val fcmToken:String = getFirebaseToken()
        Paper.book().destroy()
        updateFcmToken(fcmToken)

    }

    
}