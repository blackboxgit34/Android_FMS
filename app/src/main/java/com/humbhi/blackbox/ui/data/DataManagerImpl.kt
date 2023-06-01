package com.humbhi.blackbox.ui.data

import com.humbhi.blackbox.ui.data.db.DBHelper
import com.humbhi.blackbox.ui.data.db.DBHelperImpl
import com.humbhi.blackbox.ui.data.network.api.ApiHelper
import com.humbhi.blackbox.ui.data.network.api.ApiHelperImpl
import retrofit2.Retrofit

class DataManagerImpl(mRetrofit:Retrofit):DataManager {

    private var mApiHelper:ApiHelper
    private var mDbHelper:DBHelper = DBHelperImpl()

    init {
        mApiHelper = ApiHelperImpl(mRetrofit,mDbHelper)
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
        mApiHelper.apiCallToLogin(userID,password,deviceType,DeviceId,clearpass,token,mApiListener)
    }

    override fun apiCallToGetVehicleCount(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiCallToGetVehicleCount(custid,mApiListener)
    }

    override fun apiCallToGetNotification(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiCallToGetNotification(custid,mApiListener)
    }

    override fun apiCallToGetFleetUtilization(
        custid: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallToGetFleetUtilization(custid,mApiListener)
    }

    override fun apiToCallFuelAnalysis(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiToCallFuelAnalysis(custid,mApiListener)
    }

    override fun apiCalltoGetSpeedAnalysis(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiCalltoGetSpeedAnalysis(custid, mApiListener)
    }

    override fun apiCalltoGetDriverBehaviour(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiCalltoGetDriverBehaviour(custid, mApiListener)
    }

    override fun apiCallTotalVoilation(custid: String, mApiListener: ApiHelper.ApiListener?) {
        mApiHelper.apiCallTotalVoilation(custid, mApiListener)
    }

    override fun apiCallGetDriversRanking(
        custid: String,
        rankType: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallGetDriversRanking(custid,rankType, mApiListener)
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
        mApiHelper.apiCallToGetLiveTracking(custid,StatusCode,sEcho,iDisplayStart,iDisplayLength,sSearch, iSortCol_0, sSortDir_0, mApiListener)
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
        mApiHelper.apiCallToGetVehicleDetails(custid,StatusCode,sEcho,iDisplayStart,iDisplayLength,sSearch, iSortCol_0, sSortDir_0, mApiListener)
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
        mApiHelper.apiCallDailyReport(fromDate,toDate,custid,lowerbound,upperbound,searchVehName, mApiListener)
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
        mApiHelper.apiCallDistanceReport(beginDate,
            endDate,
            bbid,
            custid,
            downloadtype,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            reportName, mApiListener)
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
        mApiHelper.apiCallStoppageReport(
            beginDate,
            endDate,
            bbid,
            custid,
            interval,
            downloadtype,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            reportName, mApiListener)
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
        mApiHelper.apiCallFuelFillingReportApi(
            beginDate,
            endDate,
            CustId,
            bbid,
            downloadType,
            reportName,
            sEcho,
            type,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener
        )
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
        mApiHelper.apiCallTempSensorReportApi(
            beginDate,
            endDate,
            bbid,
            custid,
            sEcho,
            iDisplayStart,
            downloadType,
            reportName,
            CommandName,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener
        )
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
        mApiHelper.apiCallWorkingHourReportApi(
            beginDate,
            endDate,
            CustId,
            bbid,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            mApiListener
        )
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
        mApiHelper.apiCallTempSensorDetailReportApi(
            beginDate,
            endDate,
            bbid,
            custid,
            sEcho,
            iDisplayStart,
            downloadType,
            reportName,
            timeInterval,
            iDisplayLength,
            sSearch,
            offset,
            iSortCol_0,
            sSortDir_0, mApiListener
        )
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
        mApiHelper.apiCallFuelTheftReportApi(
            beginDate,
            endDate,
            CustId,
            bbid,
            downloadType,
            reportName,
            sEcho,
            type,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener
        )
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
        mApiHelper.apiCallFuelRodDisconnectionApi(
            beginDate,
            endDate,
            CustId,
            bbid,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener
        )
    }

    override fun apiCallRoutePlayback(
        tableName: String,
        fromDate: String,
        toDate: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallRoutePlayback(
            tableName,
            fromDate,
            toDate,
            mApiListener)
    }

    override fun apiCallRoutePlaybackForDrivingBehaviour(
        tableName: String,
        fromDate: String,
        toDate: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallRoutePlaybackForDrivingBehaviour(
            tableName,
            fromDate,
            toDate,
            mApiListener)
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
        mApiHelper.apiCallOverStoppageReport(
            beginDate,
            endDate,
            bbid,
            custid,
            downloadtype,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            reportName, mApiListener)
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
        mApiHelper.getCallDrivingLimitData(
            beginDate,
            endDate,
            custid,
            downloadtype,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            mApiListener)
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
        mApiHelper.getCallNoDrivingLimitData(
            beginDate,
            endDate,
            custid,
            downloadtype,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            mApiListener)
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
        mApiHelper.apiCallOverSpeedReport(
            beginDate,
            endDate,
            bbid,
            mode,
            custid,
            downloadtype,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            reportName, mApiListener)
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
        mApiHelper.apiCallMonthlyReport(
            beginDate,
            endDate,
            bbid,
            custid,
            CommandName,
            downloadtype,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0,
            reportName, mApiListener)
    }

    override fun apiCallMonthlyComparisonReport(
        custid: String,
        stMonth: String,
        edMonth: String,
        drid: String,
        bbid: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallMonthlyComparisonReport(
            custid,
            stMonth,
            edMonth,
            drid,
            bbid,
            mApiListener)
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
        mApiHelper.apiCallDriverToDriverComparisonReport(
            custid,
            stMonth,
            drid1,
            drid2,
            bbid1,
            bbid2,
            drName1,
            drName2,
            mApiListener
        )
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
        mApiHelper.apiCallHarshBreakingReport(
            beginDate,
            endDate,
            custid,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener)
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
        mApiHelper.apiCallConsolidateVoilation(
            fDate,
            tdate,
            CustId,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener)
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
        mApiHelper.apiCallHarshAccelerationReportApi(
            beginDate,
            endDate,
            custid,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener)
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
        mApiHelper.apiCallRashTurnData(
            beginDate,
            endDate,
            custid,
            downloadType,
            reportName,
            sEcho,
            iDisplayStart,
            iDisplayLength,
            sSearch,
            iSortCol_0,
            sSortDir_0, mApiListener)
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
        TODO("Not yet implemented")
    }

    override fun apiCallCustomerCareCompaintApi(
        custID: String,
        mApiListener: ApiHelper.ApiListener?
    ) {
        mApiHelper.apiCallCustomerCareCompaintApi(
            custID, mApiListener)
    }

    override fun setFCMToken(fcmToken: String) {
        mDbHelper.setFCMToken(fcmToken)
    }

    override fun getFCMToken(): String {
        return mDbHelper.getFCMToken()
    }

    override fun setCustId(custId: String) {
        mDbHelper.setCustId(custId)
    }

    override fun getCustIdFromDB(): String {
        return mDbHelper.getCustIdFromDB()
    }

    override fun setCustName(custName: String) {
        mDbHelper.setCustName(custName)
    }

    override fun getCustNameFromDB(): String {
        return mDbHelper.getCustNameFromDB()
    }


    override fun clearLocalAppData() {
        mDbHelper.clearLocalAppData()
    }
}