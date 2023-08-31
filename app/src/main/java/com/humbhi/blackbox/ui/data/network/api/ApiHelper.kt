package com.humbhi.blackbox.ui.data.network.api

import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.CommonResponse

interface ApiHelper {

    fun apiCallToLogin(
        userID: String,
        password: String,
        deviceType: String,
        DeviceId: String,
        clearpass: String,
        token: String,
        mApiListener:ApiListener?
    )
    fun apiCallToGetVehicleCount(
        custid: String,
        mApiListener:ApiListener?
    )

    fun apiCallToGetNotification(
        custid: String,
        mApiListener:ApiListener?
    )

    fun apiCallToGetFleetUtilization(
        custid: String,
        mApiListener:ApiListener?
    )
    fun apiToCallFuelAnalysis(
        custid: String,
        mApiListener:ApiListener?
    )
    fun apiCalltoGetSpeedAnalysis(
        custid: String,
        mApiListener:ApiListener?
    )
    fun apiCalltoGetDriverBehaviour(
        custid: String,
        mApiListener:ApiListener?
    )
    fun apiCallTotalVoilation(
        custid: String,
        mApiListener:ApiListener?
    )
    fun apiCallGetDriversRanking(
        custid: String,
        rankType: String,
        mApiListener:ApiListener?
    )
    fun apiCallToGetLiveTracking(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener:ApiListener?
    )
    fun apiCallToGetVehicleDetails(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener:ApiListener?
    )
    fun apiCallDailyReport(
        fromDate: String,
        toDate: String,
        custid: String,
        lowerbound: Int,
        upperbound: Int,
        searchVehName: String,
        mApiListener:ApiListener?
    )

    fun apiCallDistanceReport(
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
        mApiListener:ApiListener?
    )
    fun apiCallStoppageReport(
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
        mApiListener:ApiListener?
    )

    fun apiCallFuelFillingReportApi(
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
        sSortDir_0:String,
        mApiListener:ApiListener?
    )

    fun apiCallTempSensorReportApi(
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
        sSortDir_0:String,
        mApiListener:ApiListener?
    )
    fun apiCallWorkingHourReportApi(
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
        mApiListener:ApiListener?
    )

    fun apiCallTempSensorDetailReportApi(
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
        sSortDir_0:String,
        mApiListener:ApiListener?
    )
    fun apiCallFuelTheftReportApi(
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
        sSortDir_0:String,
        mApiListener:ApiListener?
    )

    fun apiCallFuelRodDisconnectionApi(
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
        mApiListener:ApiListener?
    )

    fun apiCallRoutePlayback(
        tableName: String,
        fromDate: String,
        toDate: String,
        mApiListener:ApiListener?
    )

    fun apiCallRoutePlaybackForDrivingBehaviour(
        tableName: String,
        fromDate: String,
        toDate: String,
        vehicleName: String,
        mApiListener:ApiListener?
    )

    fun apiCallOverStoppageReport(
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
        mApiListener:ApiListener?
    )

    fun getCallDrivingLimitData(
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
        mApiListener:ApiListener?
    )

    fun getCallNoDrivingLimitData(
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
        mApiListener:ApiListener?
    )

    fun apiCallOverSpeedReport(
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
        reportName:String,
        mApiListener:ApiListener?
    )

    fun apiCallMonthlyReport(
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
        mApiListener:ApiListener?
    )

    fun apiCallMonthlyComparisonReport(
        custid: String,
        stMonth : String,
        edMonth : String,
        drid1 : String,
        bbid : String,
        mApiListener:ApiListener?
    )

    fun apiCallDriverToDriverComparisonReport(
        custid: String,
        stMonth : String,
        drid1 : String,
        drid2 : String,
        bbid1 : String,
        bbid2 : String,
        drName1 : String,
        drName2 : String,
        mApiListener:ApiListener?
    )

    fun apiCallHarshBreakingReport(
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
        mApiListener:ApiListener?
    )
    fun apiCallConsolidateVoilation(
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
        mApiListener:ApiListener?
    )

    fun apiCallHarshAccelerationReportApi(
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
        mApiListener:ApiListener?
    )
    fun apiCallRashTurnData(
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
        mApiListener:ApiListener?
    )

    fun apiCallToVehcileOnMap(
        custid: String,
        StatusCode: String,
        sEcho: String,
        iDisplayStart: Int,
        iDisplayLength: Int,
        sSearch: String,
        iSortCol_0: String,
        sSortDir_0: String,
        mApiListener: ApiHelper.ApiListener?
    )

    fun apiCallCustomerCareCompaintApi(
        custID: String,
        mApiListener:ApiListener?
    )

    interface ApiListener{
        fun onSuccess(commonResponse: Any)

        fun onError(errorId:Any)

        fun onFailure(apiError:ApiError?,throwable:Throwable?)
    }
}