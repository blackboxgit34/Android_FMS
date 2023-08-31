package com.humbhi.blackbox.ui.data.network.api

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.data.db.DBHelper
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.CommonResponse
import com.humbhi.blackbox.ui.data.network.ResponseResolver
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_CONSOLIDATE_VOILATION_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_CUSTOMER_CARE_COMPAINT_NUMBERS
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DAILY_STATUS_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DISTANCE_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DRIVERS_COMPARISON
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DRIVERS_TO_DRIVERS_COMPARISON
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DRIVER_BEHAVIOUR_DASHBOARD
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DRIVER_RANKING
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_DRIVING_LIMIT_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_FLEET_UTILIZATION_DATA
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_FUEL_FILLING_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_FUEL_MIELAGE_CHART
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_FUEL_ROD_DISCONNECTIONS_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_FUEL_THEFT_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_HARSH_ACCELERATION_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_HARSH_BREAK_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_MONTHLY_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_NOTIFICATION_LIST
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_NO_DRIVING_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_OVER_SPEED_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_OVER_STOPPAGE_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_RASH_TURN_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_ROUTE_PLAYBACK
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_ROUTE_PLAYBACK_DRIVER_BEHAVIOUR
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_SPEED_ANALYSIS
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_STOPPAGE_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_TEMP_DETAIL_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_TEMP_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_TOTAL_VOILATION_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_VEHICLES_ON_MAP
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_VEHICLE_COUNT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_VEHICLE_DETAILS
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_VEHICLE_LIVE_STATUS
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.GET_WORKING_HOUR_REPORT
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints.LOGIN
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Array
import java.util.concurrent.Executors

class ApiHelperImpl(
    private var mRetrofit: Retrofit,
    private var mDBHelper: DBHelper
) :ApiHelper{
    private var isApiCalling = false
    var executor = Executors.newSingleThreadExecutor()
    var mainHandler = Handler(Looper.getMainLooper())
    /*
    * Get api interface
    * @return api interface*/
    private fun getApiInterface():ApiInterface{
        return mRetrofit.create(ApiInterface::class.java)
    }
    /*
    * Execute API Call*/
    private fun <T>executeApiCall(
        mApiCall: Observable<Response<T>>,
        mApiListener:ApiHelper.ApiListener?
    ){
        executor.execute {
                if (!isApiCalling) {
                    isApiCalling = true
                    Log.e("CallingApi", isApiCalling.toString())
                    mApiCall.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : ResponseResolver<T>(mRetrofit) {
                            override fun onSuccess(t: T) {
                                Log.e("OnSuccess", t.toString())
                                isApiCalling = false
                                if (t is CommonResponse) {
                                    Log.e("WhatIsT", "COMMON_RESPONSE")
                                    val commonResponse = t as CommonResponse
                                    if (commonResponse.errors.isNullOrEmpty()) {
                                        mApiListener?.onSuccess(commonResponse)
                                    } else {
                                        mApiListener?.onError(commonResponse.errors[0].getErrorMessageId())
                                    }
                                } else if (t is Any) {
                                    Log.e("WhatIsT", "Any")
                                    var response: CommonResponse? = null
                                    try {
                                        response = Gson().fromJson(
                                            Gson().toJson(t),
                                            CommonResponse::class.java
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    if (response!!.errors.isNullOrEmpty()) {
                                        mApiListener?.onSuccess(t as Any)
                                    } else {
                                        mApiListener?.onError(response.errors[0].getErrorMessageId())
                                    }
                                } else if (t is String) {
                                    Log.e("WhatIsT", "STRING")
                                    Log.e("t is String", t)
                                } else if (t is Array) {
                                }
                            }

                            override fun onFailure(throwable: Throwable) {
                                Log.e("OnFailure", throwable.message!!)
                                isApiCalling = false
                                mApiListener?.onFailure(null, throwable)
                            }

                            override fun onReceiveHeaders(headers: Headers) {
                                isApiCalling = false
                            }

                            override fun onError(error: ApiError) {
                                Log.e("OnError", error.getMessage()!!)
                                isApiCalling = false
                                mApiListener?.onFailure(error, null)
                            }
                        })
                }
            }
    }

    override fun apiCallToLogin(
        userID: String,
        password: String,
        deviceType: String,
        DeviceId: String,
        clearpass: String,
        token: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(LOGIN+"userID="+userID+"&password="+password+"&deviceType=Android&DeviceId="+DeviceId+"&token="+token+"&clearpass="+clearpass)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallToGetVehicleCount(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCall(GET_VEHICLE_COUNT+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }
    override fun apiCallToGetNotification(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCallArray(GET_NOTIFICATION_LIST+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }


    override fun apiCallToGetFleetUtilization(
        custid: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_FLEET_UTILIZATION_DATA+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiToCallFuelAnalysis(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCall(GET_FUEL_MIELAGE_CHART+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCalltoGetSpeedAnalysis(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCall(GET_SPEED_ANALYSIS+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCalltoGetDriverBehaviour(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCall(GET_DRIVER_BEHAVIOUR_DASHBOARD+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallTotalVoilation(custid: String, mApiListener: ApiHelper.ApiListener?) {
        val mCommonResponseCall = getApiInterface().getCall(GET_TOTAL_VOILATION_REPORT+custid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallGetDriversRanking(
        custid: String,
        rankType: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall("$GET_DRIVER_RANKING$custid&rankType=$rankType")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallToGetLiveTracking(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_VEHICLE_LIVE_STATUS+"custid="+custid+"&StatusCode="+StatusCode+"&sEcho=0&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallToVehcileOnMap(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_VEHICLES_ON_MAP+"custid="+custid+"&StatusCode="+StatusCode+"&sEcho=0&iDisplayStart=${iDisplayStart}&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallToGetVehicleDetails(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_VEHICLE_DETAILS+"custid="+custid+"&StatusCode="+StatusCode+"&sEcho=0&iDisplayStart=${iDisplayStart}&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallDailyReport(
        fromDate: String,
        toDate: String,
        custid: String,
        lowerbound: Int,
        upperbound: Int,
        searchVehName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_DAILY_STATUS_REPORT+"fromDate="+fromDate+"&toDate="+toDate+"&custid="+custid+"&lowerbound="+lowerbound+"&upperbound="+upperbound+"&searchVehName="+searchVehName)
        executeApiCall(mCommonResponseCall,mApiListener)
    }
    override fun apiCallDistanceReport(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_DISTANCE_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&bbid=&custid="+custid+"&downloadtype=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=&reportName=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallStoppageReport(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        interval: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_STOPPAGE_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&bbid=&custid="+custid+"&interval="+interval+"&downloadtype=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=&reportName=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallFuelFillingReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        type: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_FUEL_FILLING_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&CustId="+CustId+"&bbid=&downloadType=null&reportName=&sEcho=0&type=0&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallTempSensorReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        CommandName: String,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_TEMP_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&bbid="+bbid+"&custid="+custid+"&sEcho="+sEcho+"&iDisplayStart="+iDisplayStart
                +"&downloadType="+downloadType+"&reportName="+reportName+"&CommandName="+CommandName+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallWorkingHourReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_WORKING_HOUR_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&bbid="+bbid+"&CustId="+CustId+"&sEcho="+sEcho+"&iDisplayStart="+iDisplayStart
                +"&downloadType="+downloadType+"&reportName="+reportName+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallTempSensorDetailReportApi(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        sEcho: Int,
        iDisplayStart: Int,
        downloadType: String,
        reportName: String,
        timeInterval: String,
        iDisplayLength: Int,
        sSearch: String,
        offset: Int,
        iSortCol_0: Int,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_TEMP_DETAIL_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&bbid="+bbid+"&custid="+custid+"&sEcho="+sEcho+"&iDisplayStart="+iDisplayStart
                +"&downloadType="+downloadType+"&reportName="+reportName+"&timeInterval="+timeInterval+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&offset=0&iSortCol_0=0&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallFuelTheftReportApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        type: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_FUEL_THEFT_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&CustId="+CustId+"&bbid=&downloadType=null&reportName=&sEcho=0&type=0&iDisplayStart=${iDisplayStart}&iDisplayLength=${iDisplayLength}&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallFuelRodDisconnectionApi(
        beginDate: String,
        endDate: String,
        CustId: String,
        bbid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: Int,
        sSortDir_0: Int,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_FUEL_ROD_DISCONNECTIONS_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&CustId="+CustId+"&bbid=&downloadType=null&reportName=&sEcho=0&iDisplayStart=${iDisplayStart}&iDisplayLength=${iDisplayLength}&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallRoutePlayback(
        tableName: String,
        fromDate: String,
        toDate: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_ROUTE_PLAYBACK+"tableName="+tableName+
                "&fromDate="+fromDate+"&bbid=&toDate="+toDate)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallRoutePlaybackForDrivingBehaviour(
        tableName: String,
        fromDate: String,
        toDate: String,
        vehicleName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_ROUTE_PLAYBACK_DRIVER_BEHAVIOUR+"tableName="+tableName+
                "&fromDate="+fromDate+"&bbid=&toDate="+toDate+"&vehicleName="+vehicleName)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallOverStoppageReport(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_OVER_STOPPAGE_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&bbid=&custid="+custid+"&downloadtype=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=&reportName=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun getCallDrivingLimitData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadtype: String,
        reportName: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(
            GET_DRIVING_LIMIT_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&custid="+custid+"&downloadtype=null&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun getCallNoDrivingLimitData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadtype: String,
        reportName: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(
            GET_NO_DRIVING_REPORT+"beginDate="+beginDate+"&endDate="+endDate+"&custid="+custid+"&downloadtype=null&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=0&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }


    override fun apiCallOverSpeedReport(
        beginDate: String,
        endDate: String,
        bbid: String,
        mode: String,
        custid: String,
        downloadtype: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_OVER_SPEED_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&bbid=&mode=over&custid="+custid+"&downloadtype=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=&reportName=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallMonthlyReport(
        beginDate: String,
        endDate: String,
        bbid: String,
        custid: String,
        CommandName: String,
        downloadtype: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        reportName: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_MONTHLY_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&bbid=&mode=over&custid="+custid+"&CommandName=&downloadtype=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=&reportName=")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallMonthlyComparisonReport(
        custid: String,
        stMonth: String,
        edMonth: String,
        drid: String,
        bbid: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(
            GET_DRIVERS_COMPARISON+"custid="+custid+
                    "&stMonth="+stMonth+"&edMonth="+edMonth+"&drid="+drid+"&bbid="+bbid)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallDriverToDriverComparisonReport(
        custid: String,
        stMonth: String,
        drid1: String,
        drid2: String,
        bbid1: String,
        bbid2: String,
        drName1: String,
        drName2: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(
            GET_DRIVERS_TO_DRIVERS_COMPARISON+"custid="+custid+
                    "&stMonth="+stMonth+"&drid1="+drid1+"&drid2="+drid2+"&bbid1="+bbid1+"&bbid2="+bbid2+"&drName1="+drName1+"&drName2="+drName2)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallHarshBreakingReport(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_HARSH_BREAK_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&custid="+custid+"&downloadType=&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallConsolidateVoilation(
        fDate: String,
        tdate: String,
        CustId: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_CONSOLIDATE_VOILATION_REPORT+"fDate="+fDate+
                "&tdate="+tdate+"&CustId="+CustId+"&downloadType=&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallHarshAccelerationReportApi(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_HARSH_ACCELERATION_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&custid="+custid+"&downloadType=&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallRashTurnData(
        beginDate: String,
        endDate: String,
        custid: String,
        downloadType: String,
        reportName: String,
        sEcho: Int,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_RASH_TURN_REPORT+"beginDate="+beginDate+
                "&endDate="+endDate+"&custid="+custid+"&downloadType=&reportName=&sEcho=1&iDisplayStart="+iDisplayStart+"&iDisplayLength="+iDisplayLength+"&sSearch="+sSearch+"&iSortCol_0=1&sSortDir_0=asc")
        executeApiCall(mCommonResponseCall,mApiListener)
    }

    override fun apiCallCustomerCareCompaintApi(
        custID: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        val mCommonResponseCall = getApiInterface().getCall(GET_CUSTOMER_CARE_COMPAINT_NUMBERS+custID)
        executeApiCall(mCommonResponseCall,mApiListener)
    }

}