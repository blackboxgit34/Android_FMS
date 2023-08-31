package com.humbhi.blackbox.ui.ui.dashboard

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.net.DnsResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.FragmentDashboardBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.AlarmReceiver
import com.humbhi.blackbox.ui.Utility.FragmentManagerHolder
import com.humbhi.blackbox.ui.Utility.NetworkChangeListener
import com.humbhi.blackbox.ui.Utility.NetworkChangeReceiver
import com.humbhi.blackbox.ui.Utility.TimeCountingService
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.*
import com.humbhi.blackbox.ui.data.models.CheckDriverBehaviour.CheckDashPage.CheckDashPageModel
import com.humbhi.blackbox.ui.data.models.CheckInstalledDevices.CheckInstalledDevicesModel
import com.humbhi.blackbox.ui.data.network.AsyncApicall
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.addonReports.AddOnReportActivity
import com.humbhi.blackbox.ui.ui.ais140.AIS140VehicleActivity
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingBehaviourActivity
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.OverspeedReportActivity
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusActivity
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.MyWorker
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DashboardFragment : Fragment() , RetrofitResponse, NetworkChangeListener {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var VehicleCountData:VehicleCountData
    private lateinit var FleetData : FleetUtilizationResponse
    private lateinit var FuelData : VehicleMielageResponse
    private lateinit var DrivingData : DriverBehaviourDataModel
    private lateinit var SpeedData : SpeedAnalysisResponse
    private lateinit var BillData : BillDetailModel
    private lateinit var AISData : AisModel
    var list : ArrayList<DashReminderModel> = ArrayList()
    private var DistanceChart: BarChart? = null
    private var riskyDrivers:Double = 0.0
    private var excellentDrivers:Double = 0.0
    private var moderateDrivers: Double = 0.0
    lateinit var checkDashPageResponse : CheckDashPageModel
    lateinit var checkInstalledDevicesResponse : CheckInstalledDevicesModel
    private var DriverBehaviourAvailability = ""
    private var DriverBehaviourAvailabilityClick = ""
    val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
    var startlimit = 0
    var limit = 20
    var search = ""
    var startDateParam = ""
    var endDateParam = ""
    var showDialog = false
    var startTime = ""
    var endTime = ""
    var executor: ExecutorService = Executors.newSingleThreadExecutor()
    var mainHandler = Handler(Looper.getMainLooper())
    private var reminderCount = 0
    private val networkChangeReceiver = NetworkChangeReceiver(this)
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    var getAllResultJob: Job ?= null
    var load = false
    private var isReceiverRegistered = false
    private var appUpdateManager: AppUpdateManager?= null
    private lateinit var installStateListener: InstallStateUpdatedListener
    private lateinit var handler: Handler
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(requireContext())
        installStateListener = InstallStateUpdatedListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                showAppUpdateConfirmation()
            }
        }
        var firstDate = CommonData.getFirstTimeDays()
        if(firstDate==null){
            val calendarNew = Calendar.getInstance()
            calendarNew.timeInMillis = calendarNew.timeInMillis + 5 * 24 * 60 * 60 * 1000
            CommonData.setFirstTimeDays(calendarNew)
            firstDate = CommonData.getFirstTimeDays()
            Log.e("Next Date: ",
                firstDate!!.time.toString()
            )
        }
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(checkDialogConditionRunnable, 0)
        checkForAppUpdate()
        getUserData()
        getAllResult()
        binding.swipeView.setOnRefreshListener {
            binding.swipeView.isRefreshing = true
            val intent = Intent(activity,DashboardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            binding.swipeView.isRefreshing = false
        }

        binding.llDrivingBehaviour.setOnClickListener {
            if (DriverBehaviourAvailability != "") {
                if (DriverBehaviourAvailability == "true") {
                    if (DriverBehaviourAvailabilityClick == "true") {
                        val intent = Intent(activity, DrivingBehaviourActivity::class.java)
                        intent.putExtra("excellent", excellentDrivers)
                        intent.putExtra("risky", riskyDrivers)
                        intent.putExtra("moderate", moderateDrivers)
                        intent.putExtra("totalVehicle", binding.tvTotalVehicle.text.toString())
                        startActivity(intent)
                    }
                } else {
                    binding.progress.progressLayout.visibility = View.VISIBLE
                    Api.CheckInstalledDevices(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckInstalledDevicesModel>{
                        override fun onResponse(
                            call: Call<CheckInstalledDevicesModel>,
                            response: Response<CheckInstalledDevicesModel>
                        ) {
                            binding.progress.progressLayout.visibility = View.GONE
                            checkInstalledDevicesResponse = response.body()!!
                            if(checkInstalledDevicesResponse.data[0].Cnt==0){
                                if (isAdded) {
                                    activity?.let { it1 ->
                                        showConfirmationDialog(
                                            it1,
                                            "To enable Driving Behavior Monitoring feature, you need TM33+ or TM88 or AIS140 devices. Please contact your sales executive.",
                                            "Are you Interested ?"
                                        ) { saveInterest() }
                                    }
                                }
                            }
                            else{
                                Api.checkIfFreeTrail(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckDashPageModel>{
                                    override fun onResponse(
                                        call: Call<CheckDashPageModel>,
                                        response: Response<CheckDashPageModel>
                                    ) {
                                        binding.progress.progressLayout.visibility = View.GONE
                                        checkDashPageResponse = response.body()!!
                                        if(checkDashPageResponse.data==1) {
                                            if (isAdded) {
                                                Constants.alertDialog(
                                                    activity,
                                                    "Thank you. Your Free Trial will be activated in 24 hrs."
                                                )
                                            }
                                        }
                                        else{
                                            Api.checkFreeTrail(CommonData.getCustIdFromDB()).enqueue(object : Callback<CheckDashPageModel>{
                                                override fun onResponse(
                                                    call: Call<CheckDashPageModel>,
                                                    response: Response<CheckDashPageModel>
                                                ) {
                                                    binding.progress.progressLayout.visibility = View.GONE
                                                    checkDashPageResponse = response.body()!!
                                                    if(checkDashPageResponse.data==1){
                                                        if (isAdded) {
                                                            activity?.let { it1 ->
                                                                showConfirmationDialog(
                                                                    it1,
                                                                    "Your free trial is over. To enable this feature click enable, 25 will be added to your existing plan monthly per vehicle.",
                                                                    "Do you want to enable this feature?"
                                                                ) { enableFeature() }
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        if (isAdded) {
                                                            activity?.let { it1 ->
                                                                showConfirmationDialog(
                                                                    it1,
                                                                    "Driving Behavior Monitoring feature, for just Rs 25, Start your 1 month free Trial today.",
                                                                    "Do you want to start your free trail?"
                                                                ) { saveTrail() }
                                                            }
                                                        }
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<CheckDashPageModel>,
                                                    t: Throwable
                                                ) {
                                                    binding.progress.progressLayout.visibility = View.GONE
                                                    if (isAdded) {
                                                        if (t is SocketTimeoutException) {
                                                            Constants.alertDialog(
                                                                activity,
                                                                requireActivity().getString(R.string.time_out)
                                                            )
                                                        } else if (t is UnknownHostException) {
                                                            Constants.alertDialog(
                                                                activity,
                                                                requireActivity().getString(R.string.no_network)
                                                            )
                                                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                            if (t is DnsResolver.DnsException) {
                                                                Constants.alertDialog(
                                                                    activity,
                                                                    requireActivity().getString(R.string.dns_error)
                                                                )
                                                            }
                                                        } else {
                                                            Constants.alertDialog(
                                                                activity,
                                                                requireActivity().getString(R.string.something_went_wrong)
                                                            )
                                                        }
                                                    }
                                                }
                                            })
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<CheckDashPageModel>,
                                        t: Throwable
                                    ) {
                                        binding.progress.progressLayout.visibility = View.GONE
                                        if (isAdded) {
                                            if (t is SocketTimeoutException) {
                                                Constants.alertDialog(
                                                    activity,
                                                    requireActivity().getString(R.string.time_out)
                                                )
                                            } else if (t is UnknownHostException) {
                                                Constants.alertDialog(
                                                    activity,
                                                    requireActivity().getString(R.string.no_network)
                                                )
                                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                if (t is DnsResolver.DnsException) {
                                                    Constants.alertDialog(
                                                        activity,
                                                        requireActivity().getString(R.string.dns_error)
                                                    )
                                                }
                                            } else {
                                                Constants.alertDialog(
                                                    activity,
                                                    requireActivity().getString(R.string.something_went_wrong)
                                                )
                                            }
                                        }
                                    }

                                })
                            }
                        }

                        override fun onFailure(call: Call<CheckInstalledDevicesModel>, t: Throwable) {
                            binding.progress.progressLayout.visibility = View.GONE
                            if (isAdded) {
                                if (t is SocketTimeoutException) {
                                    Constants.alertDialog(
                                        activity,
                                        requireActivity().getString(R.string.time_out)
                                    )
                                } else if (t is UnknownHostException) {
                                    Constants.alertDialog(
                                        activity,
                                        requireActivity().getString(R.string.no_network)
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (t is DnsResolver.DnsException) {
                                        Constants.alertDialog(
                                            activity,
                                            requireActivity().getString(R.string.dns_error)
                                        )
                                    } else {
                                        Constants.alertDialog(
                                            activity,
                                            requireActivity().getString(R.string.something_went_wrong)
                                        )
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }

        binding.llMoving.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","M")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llParked.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","P")
            startActivity(intent)
            requireActivity().finish()
        }
        binding.llUnreachable.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","U")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llIgnitionOn.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","I")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llHighspeed.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","H")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llBatteryDisconnected.setOnClickListener {
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","BD")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llAis140Details.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    AIS140VehicleActivity::class.java
                )
            )
            requireActivity().finish()
        }

        binding.llBillReminder.setOnClickListener {
            startActivity(Intent(activity, BillAccountActivity::class.java))
            requireActivity().finish()
        }

        binding.llOverspeed.setOnClickListener {
            val intent = Intent(activity, OverspeedReportActivity::class.java)
            intent.putExtra("back","")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.llAvgSpeed.setOnClickListener {
            val intent = Intent(activity, OverspeedReportActivity::class.java)
            intent.putExtra("back","")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.clFuel.setOnClickListener {
            startActivity(Intent(activity,AddOnReportActivity::class.java))
            requireActivity().finish()
        }

        binding.llAllVehicle.setOnClickListener{
            val intent = Intent(activity, VehicleStatusActivity::class.java)
            intent.putExtra("filterValue","")
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun saveInterest() {
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.saveInterest(CommonData.getCustIdFromDB()).enqueue(object: Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1) {
                    if (isAdded) {
                        Constants.alertDialog(
                            activity,
                            "Thank you for showing interest in Driving behavior monitoring. Sales executive will call you soon."
                        )
                    }
                }
                else {
                    if (isAdded) {
                        Constants.alertDialog(activity, "Something went wrong.")
                    }
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if (isAdded) {
                    if (t is SocketTimeoutException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.time_out)
                        )
                    } else if (t is UnknownHostException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.no_network)
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (t is DnsResolver.DnsException) {
                            Constants.alertDialog(
                                activity,
                                requireActivity().getString(R.string.dns_error)
                            )
                        } else {
                            Constants.alertDialog(
                                activity,
                                requireActivity().getString(R.string.something_went_wrong)
                            )
                        }
                    }
                }
            }
        })
    }

    private fun enableFeature(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.enableFeature(CommonData.getCustIdFromDB()).enqueue(object:Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1) {
                    if (isAdded) {
                        Constants.alertDialog(activity, "Driving Behavior Monitoring enabled.")
                    }
                }
                else {
                    if (isAdded) {
                        Constants.alertDialog(activity, "Something went wrong.")
                    }
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if (isAdded) {
                    if (t is SocketTimeoutException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.time_out)
                        )
                    } else if (t is UnknownHostException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.no_network)
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (t is DnsResolver.DnsException) {
                            Constants.alertDialog(
                                activity,
                                requireActivity().getString(R.string.dns_error)
                            )
                        }
                    } else {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.something_went_wrong)
                        )
                    }
                }
            }
        })
    }

    private fun saveTrail() {
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.saveFreeTrail(CommonData.getCustIdFromDB()).enqueue(object:Callback<CheckDashPageModel>{
            override fun onResponse(
                call: Call<CheckDashPageModel>,
                response: Response<CheckDashPageModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                checkDashPageResponse = response.body()!!
                if(checkDashPageResponse.data==1) {
                    if (isAdded) {
                        Constants.alertDialog(
                            activity,
                            "Thank you. Your Free Trial will be activated in 24 hrs."
                        )
                    }
                }
                else {
                    if (isAdded) {
                        Constants.alertDialog(activity, "Something went wrong.")
                    }
                }
            }

            override fun onFailure(call: Call<CheckDashPageModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if (isAdded) {
                    if (t is SocketTimeoutException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.time_out)
                        )
                    } else if (t is UnknownHostException) {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.no_network)
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (t is DnsResolver.DnsException) {
                            Constants.alertDialog(
                                activity,
                                requireActivity().getString(R.string.dns_error)
                            )
                        }
                    } else {
                        Constants.alertDialog(
                            activity,
                            requireActivity().getString(R.string.something_went_wrong)
                        )
                    }
                }
            }
        })

    }

    private fun showAppUpdateConfirmation() {
        Constants.alertDialog(activity,"Updated successfully.")
    }

    private fun getUserData(){
        Retrofit2(activity,this,
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
            Constants.EXPIRE_ACCOUNT_DETAILS+"custid="+ CommonData.getCustIdFromDB()).callService(false)
    }

    private fun DisplayVehicleData(VehicleData:VehicleCountData) {
        binding.tvTotalVehicle.text = VehicleData.TotalVehicles.toString()
        binding.tvMovingVehicle.text = VehicleData.Moving.toString()
        binding.tvParkedVehicle.text = VehicleData.Parked.toString()
        binding.tvUnreachVehicle.text = VehicleData.Unreach.toString()
        binding.tvBatDisconnectionVehicle.text = VehicleData.Ignitionon.toString()
        binding.tvHighspeed.text = VehicleData.Hispeed.toString()
        binding.tvTotal.text = VehicleData.TotalVehicles.toString()
        binding.tvBatteryDisconneted.text = VehicleData.batteryvoltage.toString()
        binding.llTotalVehicleMain.visibility = View.VISIBLE
        binding.llTotalVhclProgress.visibility = View.GONE
    }

    private fun DisplayFleetData(fleetUtilizationResponse: FleetUtilizationResponse){
        if (fleetUtilizationResponse.IgnitionON == 0 && fleetUtilizationResponse.Parked == 0 && fleetUtilizationResponse.Moving == 0) {
            binding.llFleetProgress.visibility = View.GONE
            binding.clFleet.visibility = View.INVISIBLE
            binding.llNoFleet.visibility = View.VISIBLE
        }
        else {
            val totalNumbers = fleetUtilizationResponse.Moving + fleetUtilizationResponse.Parked + fleetUtilizationResponse.IgnitionON
            val movingPrecentage = (fleetUtilizationResponse.Moving.toFloat() / totalNumbers.toFloat()) * 100
            val parkedPrecentage = (fleetUtilizationResponse.Parked.toFloat() / totalNumbers.toFloat()) * 100
            val idealPrecentage = (fleetUtilizationResponse.IgnitionON.toFloat() / totalNumbers.toFloat()) * 100
            binding.tvMovingPercentage.text = String.format(Locale.ENGLISH, "%.2f", movingPrecentage).toDouble().toString()+"%"
            binding.tvParkedPercentage.text = String.format(Locale.ENGLISH, "%.2f", parkedPrecentage).toDouble().toString()+"%"
            binding.tvIdealPercentage.text = String.format(Locale.ENGLISH, "%.2f", idealPrecentage).toDouble().toString()+"%"
            binding.llFleetProgress.visibility = View.GONE
            binding.clFleet.visibility = View.VISIBLE
            setFleetChart(
                fleetUtilizationResponse.Moving,
                fleetUtilizationResponse.Parked,
                fleetUtilizationResponse.IgnitionON
            )
        }
    }
    private fun DisplayFuelData(vehicleMielageResponse: VehicleMielageResponse) {
        if (vehicleMielageResponse.BestMileage==0 && vehicleMielageResponse.WorstMileage==0){
            binding.rlFuelNotAvailable.visibility = View.VISIBLE
            binding.clFuel.visibility = View.GONE
            binding.llFuelProgress.visibility = View.GONE
            if(vehicleMielageResponse.notworking>0){
                binding.noFuelAvailable.text = "No consumption today"
            }
        }
        else {
            binding.rlFuelNotAvailable.visibility = View.GONE
            binding.tvHighestAvg.text = vehicleMielageResponse.BestMileage.toString()
            binding.tvAboveAvg.text = vehicleMielageResponse.WorstMileage.toString()
            binding.clFuel.visibility = View.VISIBLE
            binding.llFuelProgress.visibility = View.GONE
//            binding.clFuel.setOnClickListener {
//                startActivity(Intent(requireContext(),AddOnReportActivity::class.java))
//            }
            setFuelChart(vehicleMielageResponse.BestMileage, vehicleMielageResponse.WorstMileage)
        }
    }
    private fun DisplaySpeedData(speedAnalysisResponse: SpeedAnalysisResponse){
//        binding.llSpeedAnalysis.setOnClickListener {
//            startActivity(Intent(requireContext(),OverspeedReportActivity::class.java))
//        }
        binding.tvOverspeed.text = speedAnalysisResponse.OverspeedVehicles.toString()
        binding.tvAvgSpeed.text = speedAnalysisResponse.NonOverspeedVehicles.toString()
        binding.llOverspeed.visibility = View.VISIBLE
        binding.llAvgSpeed.visibility = View.VISIBLE
        binding.llOverspeedProgress.visibility = View.GONE
        binding.llAvgSpeedProgress.visibility = View.GONE
    }
    private fun DisplayDriverData(driverBehaviourDashModel: DriverBehaviourDataModel){
        if (driverBehaviourDashModel.data.isNotEmpty()){
            binding.llDrivingData.visibility = View.VISIBLE
            binding.rlDrivingNotAvailable.visibility = View.GONE
            binding.tvHarshAccCount.text = driverBehaviourDashModel.data[0].HAPer.toString()+"%"
            binding.tvHarshBreakCount.text = driverBehaviourDashModel.data[0].HBPer.toString()+"%"
            binding.tvHarshOverspeedCount.text = driverBehaviourDashModel.data[0].OSPer.toString()+"%"
            binding.tvRashTurnCount.text = driverBehaviourDashModel.data[0].RTPer.toString()+"%"
            binding.clDriver.visibility = View.VISIBLE
            binding.llDrivingBehavProgress.visibility = View.GONE
            riskyDrivers = driverBehaviourDashModel.data[0].RDPer
            excellentDrivers = driverBehaviourDashModel.data[0].ExDPer
            moderateDrivers = driverBehaviourDashModel.data[0].ModPer
            DriverBehaviourAvailabilityClick = "true"
            setDistancGraphData(driverBehaviourDashModel.data[0].HAPer,driverBehaviourDashModel.data[0].HBPer,driverBehaviourDashModel.data[0].OSPer,driverBehaviourDashModel.data[0].RTPer)
        }
        else{
            DriverBehaviourAvailabilityClick = "false"
            binding.llDrivingData.visibility = View.GONE
            binding.rlDrivingNotAvailable.visibility = View.VISIBLE
            binding.clDriver.visibility = View.VISIBLE
            binding.llDrivingBehavProgress.visibility = View.GONE
        }
    }
    private fun setFleetChart(moving: Int, parked: Int, ideal: Int) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"
        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = moving
        typeAmountMap["2"] = parked
        typeAmountMap["3"] = ideal
        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#A2B06D"))
        colors2.add(Color.parseColor("#FFD200"))
        colors2.add(Color.parseColor("#FF4646"))
        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }
        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)
        binding.fleetChart.data = pieData
        binding.fleetChart.setDrawEntryLabels(false)
        binding.fleetChart.isRotationEnabled = true
        binding.fleetChart.isDrawHoleEnabled = true
        binding.fleetChart.description = null
        binding.fleetChart.setDrawEntryLabels(false)
        binding.fleetChart.legend.isEnabled = false
        binding.fleetChart.animateY(3000)
        binding.fleetChart.invalidate()
        binding.fleetChart.holeRadius = 80f
        binding.fleetChart.setHoleColor(ContextCompat.getColor(requireActivity(), R.color.primary_little_fade))
    }
    // Driving behaviour chart data
    private fun setDistancGraphData(HA:Double,HB:Double,OS:Double,RT:Double) {
        DistanceChart = binding.DistanceChart
        val L: Legend = DistanceChart!!.legend
        val desc: Description = DistanceChart!!.description
        desc.text = "" // this is the weirdest way to clear something!!
        L.isEnabled = false
        val leftAxis = DistanceChart!!.axisLeft
        val rightAxis = DistanceChart!!.axisRight
        val xAxis = DistanceChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)
        leftAxis.textSize = 10f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        val data = BarData(setData(HA,HB,OS,RT))
        data.barWidth = 0.4f // set custom bar width
        DistanceChart!!.data = data
        DistanceChart!!.setFitBars(true) // make the x-axis fit exactly all bars
        DistanceChart!!.invalidate() // refresh
        DistanceChart!!.setScaleEnabled(false)
        DistanceChart!!.isDoubleTapToZoomEnabled = false
        //DistanceChart.setBackgroundResource(R.drawable.round_bars);
        DistanceChart!!.setBackgroundColor(Color.BLACK)
        DistanceChart!!.animateXY(2000, 2000)
        DistanceChart!!.setDrawBorders(false)
        DistanceChart!!.description = desc
        data.setValueTextColor(Color.rgb(255, 255, 255))
        DistanceChart!!.setDrawValueAboveBar(true)
    }
    private fun setData(HA:Double,HB:Double,OS:Double,RT:Double): BarDataSet? {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, HA.toFloat()))
        entries.add(BarEntry(1f, HB.toFloat()))
        entries.add(BarEntry(2f, OS.toFloat()))
        entries.add(BarEntry(3f, RT.toFloat()))
        val set = BarDataSet(entries, "")
        set.setColors(
            ContextCompat.getColor(DistanceChart!!.context, R.color.blue),
            ContextCompat.getColor(DistanceChart!!.context, R.color.secondary_yellow),
            ContextCompat.getColor(DistanceChart!!.context, R.color.red),
            ContextCompat.getColor(DistanceChart!!.context, R.color.voilet_red),
        )
        set.valueTextColor = Color.rgb(255, 255, 255)
        return set
    }
    // fuel chart data
    private fun setFuelChart(bestMileage: Int, worstMileage: Int) {
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"
        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["1"] = bestMileage
        typeAmountMap["2"] = worstMileage
        //initializing colors for the entries
        val colors2 = ArrayList<Int>()
        colors2.add(Color.parseColor("#FF4646"))
        colors2.add(Color.parseColor("#FFD200"))
        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }
        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors2
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false)
        binding.fuelChart.data = pieData
        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.isRotationEnabled = true
        binding.fuelChart.isDrawHoleEnabled = false
        binding.fuelChart.description = null
        binding.fuelChart.setDrawEntryLabels(false)
        binding.fuelChart.legend.isEnabled = false
        binding.fuelChart.animateY(4000)
        binding.fuelChart.invalidate()
        binding.fuelChart.setHoleColor(this.resources.getColor(R.color.primary_little_fade))
    }

    private fun getVehicleCount() {
        val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        coroutineScope.launch {
            try {
                while (isActive) {
                    val vehicleCountResponse = asyncApiCallWithErrorHandling {
                        Api.getVehicleCount(CommonData.getCustIdFromDB())
                    }.await()
                    handleVehicleCountResponse(vehicleCountResponse)
                    delay(30000)
                }
            }
            catch (e: HttpException) {
                // Handle HTTP exception (e.g., status code 500)
                e.printStackTrace()
            } catch (e: Exception) {
                // Handle other exceptions
                e.printStackTrace()
            }
        }
    }
    private fun stopRepeatingTask() {
        coroutineScope.cancel()
        getAllResultJob?.cancel()
        handler.removeCallbacks(checkDialogConditionRunnable)
        if (isReceiverRegistered) {
            activity?.unregisterReceiver(networkChangeReceiver)
            isReceiverRegistered = false
            Log.e("stop","stopped")
        }
    }
    private fun getAllResult() {
        getAllResultJob = coroutineScope.launch {
            try {
                getVehicleCount()
                val fleetUtilizationResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.getFleetUtilization(CommonData.getCustIdFromDB())
                }
                val speedAnalysisResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.getSpeedAnalysis(CommonData.getCustIdFromDB())
                }
                val mileageAnalysisResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.getMileageAnalysis(CommonData.getCustIdFromDB())
                }
                val billDetailsResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.getBillDetails(
                        CommonData.getCustIdFromDB(),
                        "1",
                        1,
                        1,
                        "1",
                        ""
                    )
                }
                val ais140ResponseDeferred = async {
                    if (!CommonData.getAisCount().equals("0")) {
                        Api.getAis140VehicleStatuses(CommonData.getCustIdFromDB())
                    } else {
                        null
                    }
                }

                val checkDashPageResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.checkDashPage(CommonData.getCustIdFromDB())
                }

                val driverBehaviourResponseDeferred = asyncApiCallWithErrorHandling {
                    Api.getDriverBehaviour(CommonData.getCustIdFromDB())
                }

                val deferredResults = listOf(
                    fleetUtilizationResponseDeferred,
                    speedAnalysisResponseDeferred,
                    mileageAnalysisResponseDeferred,
                    billDetailsResponseDeferred,
                    ais140ResponseDeferred,
                    checkDashPageResponseDeferred,
                    driverBehaviourResponseDeferred
                )

                for ((index, deferred) in deferredResults.withIndex()) {
                    val result = deferred.await()
                    handleApiResult(index, result)
                }
            }
            catch (e:Exception){
                coroutineScope.coroutineContext.cancelChildren()
                when (e) {
                    is ConnectException, is UnknownHostException -> R.string.no_network
                    is SocketTimeoutException -> R.string.time_out
                    is CancellationException, is SocketException -> null // Handle network loss
                    else -> R.string.something_went_wrong
                }?.let {
                    withContext(Dispatchers.Main) {
                        if (isAdded) {
                            Constants.Toastmsg(requireActivity(), getString(it))
                        }
                    }
                }
                throw e
            }
        }
    }
    private fun handleVehicleCountResponse(response: Response<VehicleCountData>?) {
        if (isAdded) {
            if (response != null) {
                if (response.isSuccessful) {
                    mainHandler.post {
                        VehicleCountData = response.body()!!
                        DisplayVehicleData(VehicleCountData)
                    }
                }
            }
        }
    }
    private inline fun <reified T> asyncApiCallWithErrorHandling(crossinline block: suspend () -> Response<T>): Deferred<Response<T>> {
        return coroutineScope.async(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                coroutineScope.coroutineContext.cancelChildren()
                when (e) {
                    is ConnectException, is UnknownHostException -> R.string.no_network
                    is SocketTimeoutException -> R.string.time_out
                    is CancellationException, is SocketException -> null // Handle network loss
                    else -> R.string.something_went_wrong
                }?.let {
                    withContext(Dispatchers.Main) {
                        if (isAdded) {
                            Constants.Toastmsg(requireActivity(), getString(it))
                        }
                    }
                }
                throw e
            }
        }
    }
    private suspend fun handleApiResult(index: Int, response: Response<*>?) {
        response?.let { it ->
            if (it.isSuccessful) {
                when (index) {
                    0 -> {
                        val fleetDataResponse = it as Response<FleetUtilizationResponse>
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                if (fleetDataResponse.isSuccessful) {
                                    FleetData = fleetDataResponse.body()!!
                                    DisplayFleetData(FleetData)
                                }
                            }
                        }
                    }
                    1 -> {
                        val speedDataResponse = it as Response<SpeedAnalysisResponse>
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                if (speedDataResponse.isSuccessful) {
                                    if (binding.swipeView.isRefreshing == true) {
                                        binding.swipeView.isRefreshing = false
                                    }
                                    SpeedData = speedDataResponse.body()!!
                                    DisplaySpeedData(SpeedData)
                                }
                            }
                        }
                    }
                    2 -> {
                        val fuelMilageDataResponse = it as Response<VehicleMielageResponse>
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                if (fuelMilageDataResponse.isSuccessful) {
                                    FuelData = fuelMilageDataResponse.body()!!
                                    DisplayFuelData(FuelData)
                                }
                            }
                        }
                    }
                    3 -> {
                        val getPaymentDetailResponse = it as Response<BillDetailModel>
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                if (getPaymentDetailResponse.isSuccessful) {
                                    reminderCount = 0
                                    BillData = getPaymentDetailResponse.body()!!
                                    list.clear()
                                    val iTotalRecords = BillData.iTotalRecords
                                    val blinkingPointAnimation = binding.blinkingPointBR.background as AnimationDrawable
                                    binding.reminderProgress.visibility = View.GONE
                                    binding.reminderLayout.visibility = View.VISIBLE
                                    if (iTotalRecords > 0) {
                                        reminderCount++
                                        blinkingPointAnimation.start()
                                        binding.blinkingPointBR.visibility = View.VISIBLE
                                    } else {
                                        blinkingPointAnimation.stop()
                                        binding.blinkingPointBR.visibility = View.GONE
                                    }
                                    if (reminderCount > 1) {
                                        binding.tvReminderHeading.text = "Reminders"
                                    } else {
                                        binding.tvReminderHeading.text = "Reminder"
                                    }
                                    binding.tvReminderCount.text = reminderCount.toString()
                                    binding.daysBR.text = iTotalRecords.toString()
                                }
                            }
                        }
                    }
                    4 -> {
                        if (!CommonData.getAisCount().equals("0")) {
                            val aisDataResponse = it as Response<AisModel>
                            withContext(Dispatchers.Main) {
                                if (isAdded) {
                                    if (aisDataResponse.isSuccessful) {
                                        AISData = aisDataResponse.body()!!
                                        val responseFromBody = AISData
                                        if (isAdded) {
                                            val blinkingPointAnimation = binding.blinkingPoint.background as AnimationDrawable
                                            binding.days.text = (responseFromBody.Table1[0].ExpiredDevices + responseFromBody.Table1[0].ExpiringSoon + responseFromBody.Table1[0].PdDevices).toString()
                                            if (responseFromBody.Table1[0].ExpiredDevices + responseFromBody.Table1[0].ExpiringSoon + responseFromBody.Table1[0].PdDevices > 0) {
                                                reminderCount += 1
                                                blinkingPointAnimation.start()
                                                binding.blinkingPoint.visibility = View.VISIBLE
                                            } else {
                                                blinkingPointAnimation.stop()
                                                binding.blinkingPoint.visibility = View.GONE
                                            }
                                            val responseSkip = MyApplication.skip
      //                                      if (responseSkip != "yes") {
                                                val vehicleWithLeastValidity = responseFromBody.Table.minByOrNull {it.ExpireIndays}
                                                var expiry = ""
                                                if (vehicleWithLeastValidity != null) {
                                                    val expireIndays = vehicleWithLeastValidity.ExpireIndays
                                                    val paymentStatus = vehicleWithLeastValidity.PaymentStatus
                                                    when {
                                                        expireIndays in 29 downTo 9 && paymentStatus == "Not Paid" -> {
                                                            if (CommonData.getFirstTime().equals("true")) {
                                                                if (responseSkip != "yes") {
                                                                    CommonData.setFirstTime("false")
                                                                    MyApplication.cantSkip = "no"
                                                                    expiry = "justAlert"
                                                                    vehicleWithLeastValidity.commercialvalidity?.let { it1 ->
                                                                        vehicleWithLeastValidity.vehname?.let { it2 ->
                                                                            showDialog(
                                                                                this@DashboardFragment,
                                                                                expiry,
                                                                                it1,
                                                                                it2
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                    if (showDialog==true) {
                                                                        MyApplication.cantSkip = "no"
                                                                        showDialog=false
                                                                        expiry = "justAlert"
                                                                        vehicleWithLeastValidity.commercialvalidity?.let { it1 ->
                                                                            vehicleWithLeastValidity.vehname?.let { it2 ->
                                                                                showDialog(
                                                                                    this@DashboardFragment,
                                                                                    expiry,
                                                                                    it1,
                                                                                    it2
                                                                                )
                                                                            }
                                                                        }
                                             //                       }
                                                                }
                                                            }
                                                        }
                                                        expireIndays in 9 downTo 4 && paymentStatus == "Not Paid" -> {
                                                            val stopServiceIntent = Intent(requireContext(), TimeCountingService::class.java)
                                                            stopServiceIntent.action = TimeCountingService.ACTION_STOP_SERVICE
                                                            requireContext().startService(stopServiceIntent)
                                                            if (responseSkip != "yes") {
                                                                MyApplication.cantSkip = "yes"
                                                                expiry = "attentionAlert"
                                                                vehicleWithLeastValidity.commercialvalidity?.let { it1 ->
                                                                    vehicleWithLeastValidity.vehname?.let { it2 ->
                                                                        showDialog(
                                                                            this@DashboardFragment,
                                                                            expiry,
                                                                            it1,
                                                                            it2
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        expireIndays in 4 downTo 0 && paymentStatus == "Not Paid" -> {
                                                            if (responseSkip != "yes") {
                                                                MyApplication.cantSkip = "yes"
                                                                expiry = "expiringSoon"
                                                                vehicleWithLeastValidity.commercialvalidity?.let { it1 ->
                                                                    vehicleWithLeastValidity.vehname?.let { it2 ->
                                                                        showDialog(
                                                                            this@DashboardFragment,
                                                                            expiry,
                                                                            it1,
                                                                            it2
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        expireIndays < 0 && paymentStatus == "Not Paid" -> {
                                                            if (responseSkip != "yes") {
                                                                MyApplication.cantSkip = "yes"
                                                                expiry = "expired"
                                                                vehicleWithLeastValidity.commercialvalidity?.let { it1 ->
                                                                    vehicleWithLeastValidity.vehname?.let { it2 ->
                                                                        showDialog(
                                                                            this@DashboardFragment,
                                                                            expiry,
                                                                            it1,
                                                                            it2
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            if (reminderCount > 1) {
                                                binding.tvReminderHeading.text = "Reminders"
                                            } else {
                                                binding.tvReminderHeading.text = "Reminder"
                                            }
                                            binding.tvReminderCount.text = reminderCount.toString()
                                            binding.totalDevices.text = responseFromBody.Table.size.toString()
                                            binding.expiredDevices.text =
                                                responseFromBody.Table1[0].ExpiredDevices.toString()
                                            binding.renewalDevices.text =
                                                responseFromBody.Table1[0].ExpiringSoon.toString()
                                            binding.pdDevices.text =
                                                responseFromBody.Table1[0].PdDevices.toString()
                                            binding.llAis140.setOnClickListener {
                                                if (binding.llAis140Details.visibility == View.GONE) {
                                                    binding.llAis140Details.visibility =
                                                        View.VISIBLE
                                                } else {
                                                    binding.llAis140Details.visibility =
                                                        View.GONE
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    5 -> {
                        val checkDashPageResponse = it as Response<CheckDashPageModel>
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                var DriverBehaviour: Response<DriverBehaviourDataModel>? = null
                                val checkDashPageResponseX = checkDashPageResponse.body()!!
                                if (checkDashPageResponseX.data == 1) {
                                    withContext(Dispatchers.IO) {
                                        try {
                                            DriverBehaviour = AsyncApicall.callApi { Api.getDriverBehaviour(CommonData.getCustIdFromDB()) }
                                        } catch (e: SocketException) {
                                            // Handle the socket exception, show a message to the user, etc.
                                        } catch (e: Exception) {
                                            // Handle other exceptions
                                        }
                                        catch (e: UnknownHostException) {
                                            // Handle the DNS resolution error
                                        }
                                        withContext(Dispatchers.Main) {
                                            if (isAdded) {
                                                DrivingData = DriverBehaviour!!.body()!!
                                                DriverBehaviourAvailability = "true"
                                                DisplayDriverData(DrivingData)
                                            }
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        DriverBehaviourAvailability = "false"
                                        binding.llDrivingData.visibility = View.VISIBLE
                                        binding.rlDrivingNotAvailable.visibility = View.GONE
                                        binding.tvHarshAccCount.text = "12" + "%"
                                        binding.tvHarshBreakCount.text = "19" + "%"
                                        binding.tvHarshOverspeedCount.text = "14" + "%"
                                        binding.tvRashTurnCount.text = "28" + "%"
                                        binding.clDriver.visibility = View.VISIBLE
                                        binding.llDrivingBehavProgress.visibility = View.GONE
                                        riskyDrivers = (VehicleCountData.TotalVehicles / 2).toDouble()
                                        excellentDrivers = (VehicleCountData.TotalVehicles / 10).toDouble()
                                        moderateDrivers =
                                            (VehicleCountData.TotalVehicles / 2.5).toInt().toDouble()
                                        setDistancGraphData(16.44, 26.03, 19.18, 38.36)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode) {
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS -> {
                executor.execute {
                    try {
                        var SoftBanner = ""
                        var hardBanner = ""
                        var aisCount = 0
                        var msg = ""
                        val result = JSONObject(response!!.body()!!.string())
                        val table = result.getJSONArray("Table")
                        for (i in 0 until table.length()) {
                            val jsonObject = table.getJSONObject(0)
                            msg = jsonObject.getString("msg")
                            SoftBanner = jsonObject.getString("SoftBanner")
                            hardBanner = jsonObject.getString("hardBanner")
                            aisCount = jsonObject.getString("Ais140Count").toInt()
                            if (msg != "null") {
                                mainHandler.post() {
                                    binding.marqueeText.text = msg
                                    binding.marqueeText.isSelected = true
                                }
                            } else {
                                mainHandler.post() {
                                    binding.marqueeText.text = "Welcome to Blackbox GPS application, you can track your vehicles from this app."
                                    binding.marqueeText.isSelected = true
                                }
                            }
                        }
                        CommonData.setAisCount(aisCount.toString())
                        if (CommonData.getSkip() != "yes") {
                            if (SoftBanner != "false") {
                                mainHandler.post() {
                                    val intent = Intent(activity, BillBanner::class.java)
                                    intent.putExtra("SoftBanner", SoftBanner)
                                    intent.putExtra("hardBanner", hardBanner)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }
                        }
                        if (hardBanner != "false") {
                            mainHandler.post() {
                                val intent = Intent(activity, BillBanner::class.java)
                                intent.putExtra("SoftBanner", SoftBanner)
                                intent.putExtra("hardBanner", hardBanner)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                    } catch (e: Exception) {
                        mainHandler.post {
                            when (e) {
                                is SocketException, is ConnectException, is UnknownHostException -> {
                                    Constants.Toastmsg(
                                        requireActivity(),
                                        requireActivity().getString(R.string.no_network)
                                    )
                                }

                                is SocketTimeoutException -> {
                                    Constants.Toastmsg(
                                        activity,
                                        requireActivity().getString(R.string.time_out)
                                    )
                                }

                                else -> {
                                    Constants.Toastmsg(
                                        activity,
                                        requireActivity().getString(R.string.something_went_wrong)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        onYesClicked: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_confirm, null)
        builder.setView(view)
        val yesButton: Button = view.findViewById(R.id.dialog_button_yes)
        val noButton: Button = view.findViewById(R.id.dialog_button_no)
        val Title: TextView = view.findViewById(R.id.dialog_title)
        val Msg: TextView = view.findViewById(R.id.dialog_message)
        Title.text = title
        Msg.text = message
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        yesButton.setOnClickListener {
            onYesClicked.invoke()
            dialog.dismiss()
        }
        noButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }
    override fun onResume() {
        super.onResume()
        if (!isReceiverRegistered) {
            activity?.registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            isReceiverRegistered = true
        }
        appUpdateManager?.registerListener(installStateListener)
    }
    private fun checkForAppUpdate() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                newUpdateDialog()
            }
        }
    }
    private fun newUpdateDialog() {
        mainHandler.post() {
            if (isAdded) {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                val li = LayoutInflater.from(requireContext())
                val promptsView: View = li.inflate(R.layout.update_dialog, null)
                dialog.setContentView(promptsView)
                val tvUpdateButton: TextView =
                    promptsView.findViewById<TextView>(R.id.tvUpdateButton)
                tvUpdateButton.setOnClickListener { view: View? ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.humbhi.blackbox")
                    )
                    startActivity(intent)
                    requireActivity().finish()
                }
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }
        }
    }
    private fun showDialog(fragment: Fragment, expiry: String, validity: String, vehicleName: String) {
        val dialogFragment = WhatsNewDialogFragment(fragment.requireContext(), expiry, validity, vehicleName)
        if (isAdded) {
            dialogFragment.show(fragment.parentFragmentManager, "WhatsNewDialog")
        }
    }
    override fun onNetworkChanged(isConnected: Boolean) {
        if (isConnected) {
            Log.e("internet", "ON")
            if(load==true){
                load=false
                val intent = Intent(activity,DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            Log.e("internet", "OFF")
            load=true
        }
    }
    private val checkDialogConditionRunnable = object : Runnable {
        override fun run() {
            val currentDate = Calendar.getInstance()
            val firstDate = CommonData.getFirstTimeDays()
            if (firstDate != null) {
                val elapsedTimeMillis = currentDate.timeInMillis - firstDate.timeInMillis
                val remainingTimeMillis = 5 * 24 * 60 * 60 * 1000 - elapsedTimeMillis
                if (remainingTimeMillis <= 0) {
                    // Show your dialog here
                    showDialog = true
                    currentDate.timeInMillis = currentDate.timeInMillis+ 5 * 24 * 60 * 60 * 1000
                    CommonData.setFirstTimeDays(currentDate)
                }
            }
            handler.postDelayed(this, 5 * 24 * 60 * 60 * 1000) // Check every 5th day till first 20 days
        }
    }
}