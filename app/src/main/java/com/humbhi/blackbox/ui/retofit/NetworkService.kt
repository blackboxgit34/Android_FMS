package com.humbhi.blackbox.ui.retofit

import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.models.*
import com.humbhi.blackbox.ui.data.models.CheckDriverBehaviour.CheckDashPage.CheckDashPageModel
import com.humbhi.blackbox.ui.data.models.CheckInstalledDevices.CheckInstalledDevicesModel
import com.humbhi.blackbox.ui.ui.reports.distanceReport.DistanceReportModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("FmsAPI/GetVehicleInfo?")
    fun getFMSVehicles(@Query("CustId") CustId: String,
                       @Query("vehicleId") vehicleId: String,
                       @Query("vehicleStatus") vehicleStatus: String,
                       @Query("downloadType") downloadType: String,
                       @Query("reportName") reportName: String,
                       @Query("sEcho") sEcho: String,
                       @Query("iDisplayStart") iDisplayStart: String,
                       @Query("iDisplayLength") iDisplayLength: String,
                       @Query("sSearch") sSearch: String,
                       @Query("iSortCol_0") iSortCol_0: String,
                       @Query("sSortDir_0") sSortDir_0: String) : Call<FmsVehiclesModel>
    @GET("FmsAPI/GetRenewalReminders")
    fun getDocumentReminder(@Query("beginDate", encoded = true) beginDate: String,
                            @Query("endDate", encoded = true) endDate: String,
                            @Query("CustId") CustId: String,
                            @Query("renewalId") renewalId: String,
                            @Query("downloadType") downloadType: String,
                            @Query("reportName") reportName: String,
                            @Query("Status") Status: String,
                            @Query("sEcho") sEcho: String,
                            @Query("iDisplayStart") iDisplayStart: String,
                            @Query("iDisplayLength") iDisplayLength: String,
                            @Query("sSearch") sSearch: String,
                            @Query("iSortCol_0") iSortCol_0: String,
                            @Query("sSortDir_0") sSortDir_0: String) : Call<DocumenTReminderModel>

    @GET("FmsAPI/GetRenewalReminders")
    suspend fun getDocumentReminderWithAsync(@Query("beginDate", encoded = true) beginDate: String,
                            @Query("endDate", encoded = true) endDate: String,
                            @Query("CustId") CustId: String,
                            @Query("renewalId") renewalId: String,
                            @Query("downloadType") downloadType: String,
                            @Query("reportName") reportName: String,
                            @Query("Status") Status: String,
                            @Query("sEcho") sEcho: String,
                            @Query("iDisplayStart") iDisplayStart: String,
                            @Query("iDisplayLength") iDisplayLength: String,
                            @Query("sSearch") sSearch: String,
                            @Query("iSortCol_0") iSortCol_0: String,
                            @Query("sSortDir_0") sSortDir_0: String) : Response<DocumenTReminderModel>
    @GET("CommonApi/VerifyUserOtp")
    fun verifyOTP(@Query("custid") custid: String,
                  @Query("website") website: String,
                  @Query("OTP") OTP: String) : Call<VerifyOtpModel>
    @GET("AdminAPI/OTPChangePassword")
    fun changePassword(@Query("custid") custid: String,
                       @Query("CurrentPassword") CurrentPassword: String,
                       @Query("NewPassword") NewPassword: String) : Call<String>

    @POST("FmsAPI/AddUpdateRenewalReminder?")
    suspend fun addUpdateReminder(@Query("renewalID") renewalID: String,
                          @Query("RenewalTypeId") RenewalTypeId: String,
                          @Query("DueDate") DueDate: String,
                          @Query("remBefore") remBefore: String,
                          @Query("vehicleID") vehicleID: String,
                          @Query("Remarks") Remarks: String) :ResponseBody

    @GET("FmsAPI/GetFmsVehicles")
    fun fmsVehiclesList(@Query("custid") custid: String) : Call<FMSVehicleListModel>

    @GET("FmsAPI/GetRenewalTypeList")
    fun getAllRenewalTypes() : Call<RenewalTypesModel>

    @GET("ReportsApi/GetDrBOverSpeed")
    fun getOverSpeedReport(@Query("beginDate", encoded = true) beginDate:String,
                           @Query("endDate", encoded = true) endDate:String,
                           @Query("custid") custid:String,
                           @Query("downloadType") downloadType:String,
                           @Query("reportName") reportName:String,
                           @Query("sEcho") sEcho:String,
                           @Query("iDisplayStart") iDisplayStart:Int,
                           @Query("iDisplayLength") iDisplayLength:Int,
                           @Query("sSearch") sSearch:String,
                           @Query("iSortCol_0") iSortCol_0:String,
                           @Query("sSortDir_0") sSortDir_0:String
    ) : Call<OverSpeedDBModel>

    @GET("customapi/GetAIS140DataRecords")
    fun getAis140VehicleStatus(@Query("custid") custid: String) : Call<AisModel>

    @GET("customapi/GetAIS140DataRecords")
    suspend fun getAis140VehicleStatuses(@Query("custid") custid: String) : Response<AisModel>

    @GET("ReportsApi/GetDrBScoreCardReportDash")
    fun getTop10Drivers(@Query("custid") custid: String, @Query("device") device: String) : Call<Top10Drivers>

    @GET("MapApi/getRoutPlaybackViolation")
    fun getVoilationCounts(@Query("tableName") tableName: String, @Query("fromDate", encoded = true) fromDate: String, @Query("toDate", encoded = true) toDate: String) : Call<DBVoilationCountsModel>

    @GET("AppApi/UpdateEmailAis140")
    fun updateEmail(@Query("custid") custid: String, @Query("EmailId", encoded = true) EmailId: String) : Call<UpdateEmailModel>

    @GET("AdminAPI/GetBoxInfo")
    fun boxInfo(@Query("custid") custid:String,
                @Query("sEcho") sEcho:String,
                @Query("iDisplayStart") iDisplayStart:Int,
                @Query("iDisplayLength") iDisplayLength:Int,
                @Query("downloadType") downloadType:String,
                @Query("reportName") reportName:String,
                @Query("sSearch") sSearch:String,
                @Query("iSortCol_0") iSortCol_0:String,
                @Query("sSortDir_0") sSortDir_0:String) : Call<BoxInfoModel>

    @GET("InvoiceAPI/GetAccountSummary")
    suspend fun getBillDetails(@Query("custid") custid:String,
                @Query("sEcho") sEcho:String,
                @Query("iDisplayStart") iDisplayStart:Int,
                @Query("iDisplayLength") iDisplayLength:Int,
                @Query("iSortCol_0") iSortCol_0:String,
                @Query("sSortDir_0") sSortDir_0:String) : Response<BillDetailModel>

    @GET("GeofenceAPI/BindAllFences")
    fun viewAllFences(@Query("userId") userId: String,
                      @Query("sEcho") sEcho:String,
                      @Query("iDisplayStart") iDisplayStart:Int,
                      @Query("iDisplayLength") iDisplayLength:Int,
                      @Query("sSearch") sSearch:String,
                      @Query("iSortCol_0") iSortCol_0:String,
                      @Query("sSortDir_0") sSortDir_0:String) : Call<ViewFenceModel>
}