package com.humbhi.blackbox.ui.retofit

import com.humbhi.blackbox.ui.data.models.*
import com.humbhi.blackbox.ui.data.models.CheckDriverBehaviour.CheckDashPage.CheckDashPageModel
import com.humbhi.blackbox.ui.data.models.CheckInstalledDevices.CheckInstalledDevicesModel
import com.humbhi.blackbox.ui.ui.reports.distanceReport.DistanceReportModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService
{
    @GET("APPApi/VehiclesStatusGraphAPP")
    suspend fun getVehicleCount(@Query("custid") custid: String?): Response<VehicleCountData>

    @GET("customapi/mileageanalysis")
    suspend fun getMileageAnalysis(@Query("custid") custid: String?): Response<VehicleMielageResponse>

    @GET("Adminapi/VehicleUtilizationChart")
    suspend fun getFleetUtilization(@Query("custid") custid: String?): Response<FleetUtilizationResponse>

    @GET("ReportsApi/GetOverSpeedGraphReport")
    suspend fun getSpeedAnalysis(@Query("custid") custid: String?): Response<SpeedAnalysisResponse>

    @GET("ReportsApi/getDriverGraphPer")
    suspend fun getDriverBehaviour(@Query("custid") custid: String?): Response<DriverBehaviourDataModel>

    @GET("ReportsApi/GetDrBScoreCardReport")
    fun getScoreCardReport(@Query("month") month: String,
                                  @Query("year") year: String,
                                  @Query("custid") custid: String,
                                  @Query("downloadtype") downloadtype: String,
                                  @Query("reportName") reportName: String,
                                  @Query("sEcho") sEcho: String,
                                  @Query("iDisplayStart") iDisplayStart: Int,
                                  @Query("iDisplayLength") iDisplayLength: Int,
                                  @Query("sSearch") sSearch: String,
                                  @Query("iSortCol_0") iSortCol_0: String,
                                  @Query("sSortDir_0") sSortDir_0: String): Call<ScoreCardReportModel>

    @GET("ReportsApi/GetDrivingLimitReport")
    fun getDrivingLimitReport(@Query("beginDate") beginDate: String,
                           @Query("endDate") endDate: String,
                           @Query("custid") custid: String,
                           @Query("downloadtype") downloadtype: String,
                           @Query("reportName") reportName: String,
                           @Query("sEcho") sEcho: String,
                           @Query("iDisplayStart") iDisplayStart: Int,
                           @Query("iDisplayLength") iDisplayLength: Int,
                           @Query("sSearch") sSearch: String,
                           @Query("iSortCol_0") iSortCol_0: String,
                           @Query("sSortDir_0") sSortDir_0: String): Call<DrivingLimitModel>

    @GET("AdminApi/chkdashpage?")
    suspend fun checkDashPage(@Query("custid") custid: String) : Response<CheckDashPageModel>

    @GET("AdminApi/IsFeatureValid?")
    fun CheckInstalledDevices(@Query("custid") custid: String) : Call<CheckInstalledDevicesModel>

    @GET("AdminApi/SaveIntrestedCust?")
    fun saveInterest(@Query("custid") custid: String) : Call<CheckDashPageModel>

    @GET("AdminApi/chkIffreeTrialIsActive?")
    fun checkIfFreeTrail(@Query("custid") custid: String) : Call<CheckDashPageModel>

    @GET("AdminApi/ChkFreeTrial?")
    fun checkFreeTrail(@Query("custid") custid: String) : Call<CheckDashPageModel>

    @GET("AdminApi/SaveFreeTrial?")
    fun saveFreeTrail(@Query("custid") custid: String) : Call<CheckDashPageModel>

    @GET("AdminApi/UpdateSubscription?")
    fun enableFeature(@Query("custid") custid: String) : Call<CheckDashPageModel>

    @GET("appapi/UpdateMqttCmd?")
    fun updateMQTTCmd(@Query("Bbid") Bbid: String,@Query("Command") Command: String) : Call<CommonResponseMQTT>

}